import os
import sys
import glob
import time
import filecmp
import subprocess
from typing import Optional, Any


def get_input_and_output_path() -> list:
    parent_path = os.path.dirname(os.path.abspath((os.path.dirname(__file__))))
    input_path = parent_path + '\\resources\\input'
    output_path = parent_path + '\\resources\\output'
    solver_output_path = parent_path + '\\resources\\solver_output'

    return [input_path, output_path, solver_output_path]


def extract_file_path_from_abs_path(path: str) -> str:
    return '\\'.join(path.split('\\')[:-1])


def extract_file_name_from_abs_path(path: str) -> str:
    return path.split('\\')[-1]


def chdir(path: str):
    os.chdir(path)


def compare_outputs(output_path: str, solver_output_path: str, test_number: int) -> Optional[Any]:
    output_file = '\\'.join([output_path, f'test{test_number}-output.txt'])
    solver_output_file = '\\'.join([solver_output_path, f'solver{test_number}-output.txt'])

    try:
        return filecmp.cmp(output_file, solver_output_file)
    except FileNotFoundError:
        print(f'Unable to compare files: missing output or solver_output file')
        return None


def run_java(java_file_path: str, test_number: int):
    java_file = extract_file_name_from_abs_path(java_file_path)
    abs_path = extract_file_path_from_abs_path(java_file_path)

    chdir(abs_path)
    start_time = time.clock()
    time.sleep(0.5)
    # subprocess.check_call(['java', java_file, test_number], shell=True)
    end_time = time.clock()

    print(f'Test number: {test_number}')
    print(f'Time elapsed: {end_time - start_time}')


def start_testing(java_file_path: str, test_number: int):
    input_path, output_path, solver_output_path = get_input_and_output_path()

    chdir(input_path)
    print('-------------------------------------------------------')
    if test_number == -1:
        print('No second argument specified, executing all tests...')
        print('-------------------------------------------------------')
        for test_number, _ in enumerate(glob.glob('test[0-9]-input.txt')):
            run_java(java_file_path, test_number)
            print(f'Solution match: {compare_outputs(output_path, solver_output_path, test_number)}')
            print('-------------------------------------------------------')

        if test_number == -1:
            print(f'No input files with \'testX-input.txt\' format found in {os.getcwd()}')
            print('-------------------------------------------------------')
            return
    else:
        if os.path.isfile(f'test{test_number}-input.txt'):
            run_java(java_file_path, test_number)
            print(f'Solution match: {compare_outputs(output_path, solver_output_path, test_number)}')
            print('-------------------------------------------------------')

        else:
            print(f'File with test number {test_number} not found!')
            print('-------------------------------------------------------')
            return


if __name__ == "__main__":
    try:
        if len(sys.argv) == 2:
            sys.argv.append(-1)
        java_file_path = str(sys.argv[1])  # contains .java file
        test_number = int(sys.argv[2])

        start_testing(java_file_path, test_number)
    except ValueError:
        print('Wrong input type! Expected: str, int')
