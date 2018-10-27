"""
Automated test script runner for Kiwi challenge 2018.
Usage:
    >python script_runner.py *ABSOLUTE_PATH_TO_JAR_FILE (IN PATH USE / NOT \\ OR \)* *TEST_NUMBER*
    - Important - run it from /test folder
    - If test_number is not specified, all available tests are executed (according to the number
      of test input files - max 10 .txt files)
    - Absolute path to jar file MUST contain the .jar file to be executed (i.e. D:\foo\bar\file.jar)
Requirements:
    - Executable .jar file located in \jars
    - Input text files with file name pattern 'testX-input.txt' located in \test\resources\input
    - Output text files with expected result with file name pattern 'testX-output.txt' located in \test\resources\output
    - Calculated routes with file name pattern 'solverX-output.txt' located in \test\resources\solver_output
"""

import os
import sys
import glob
import time
import filecmp
import subprocess
from typing import Optional, Any


def get_input_and_output_path() -> list:
    parent_path = os.path.dirname(os.path.abspath((os.path.dirname(__file__))))
    input_path = parent_path + '\\test\\resources\\input'
    output_path = parent_path + '\\test\\resources\\output'
    solver_output_path = parent_path + '\\test\\resources\\solver-output'

    return [input_path, output_path, solver_output_path]


def extract_file_path_from_abs_path(path: str) -> str:
    return '\\'.join(path.split('/')[:-1])

def extract_jar_folder_from_abs_path(path: str) -> str:
    return '\\'.join(path.split('\\')[:-1])

def extract_file_name_from_abs_path(path: str) -> str:
    return path.split('/')[-1]


def chdir(path: str):
    os.chdir(path)


def compare_outputs_and_calculate_score(output_path: str, solver_output_path: str, test_number: int) -> Optional[Any]:
    output_file = '\\'.join([output_path, f'test{test_number}-output.txt'])
    solver_output_file = '\\'.join([solver_output_path, f'solver{test_number}-output.txt'])

    if not os.path.isfile(output_file):
        print(f'Unable to compare files: no main file available to compare solver result to in {output_path}')
        return None

    if not os.path.isfile(solver_output_file):
        print(f'Unable to compare files: no solver output file available in {solver_output_path}')
        return None

    with open(output_file) as f:
        base_price = int(f.readline())

    with open(solver_output_file) as f:
        solver_price = int(f.readline())

    return {'base_price': base_price,
            'solver_price': solver_price,
            'file_match': filecmp.cmp(output_file, solver_output_file),
            'final_score': round((base_price / solver_price) * 100, 2)}


def run_java(jar_file_path: str, test_number: int):
    jar_file = extract_file_name_from_abs_path(jar_file_path)
    abs_path = extract_jar_folder_from_abs_path(extract_file_path_from_abs_path(jar_file_path))
    chdir(abs_path)

    start_time = time.clock()

    subprocess.check_call('java -jar jars/'+jar_file+' '+str(test_number), shell=True)

    end_time = time.clock()

    print(f'Test number: {test_number}')
    print(f'Time elapsed: {end_time - start_time}')


def run_test(jar_file_path: str, test_number: int, output_path: str, solver_output_path: str):
    run_java(jar_file_path, test_number)
    results = compare_outputs_and_calculate_score(output_path, solver_output_path, test_number)

    print('Solution match: ' + str(results['file_match']))
    if results['final_score'] > 100:
        print('You found a cheaper route!')
    elif results['final_score'] == 100:
        print('You matched the base price')
    else:
        print('Your route is more expensive than the base price')

    print('Base price: ' + str(results['base_price']))
    print('Solver price: ' + str(results['solver_price']))
    print('FINAL SCORE: ' + str(results['final_score']))
    print('-------------------------------------------------------')


def start_testing(jar_file_path: str, test_number: int):
    input_path, output_path, solver_output_path = get_input_and_output_path()

    chdir(input_path)
    print('-------------------------------------------------------')
    if test_number == -1:
        print('No second argument specified, executing all tests...')
        print('-------------------------------------------------------')
        for test_number, _ in enumerate(glob.glob('test[0-3]-input.txt')):
            run_test(jar_file_path, test_number, output_path, solver_output_path)

        if test_number == -1:
            print(f'No input files with \'testX-input.txt\' format found in {os.getcwd()}')
            print('-------------------------------------------------------')
            return
    else:
        if os.path.isfile(f'test{test_number}-input.txt'):
            run_test(jar_file_path, test_number, output_path, solver_output_path)

        else:
            print(f'File with test number {test_number} not found!')
            print('-------------------------------------------------------')
            return


if __name__ == "__main__":
    try:
        if len(sys.argv) == 2:
            sys.argv.append(-1)
        jar_file_path = str(sys.argv[1])  # contains .jar file
        test_number = int(sys.argv[2])

        start_testing(jar_file_path, test_number)
    except ValueError:
        print('Wrong input type! Expected: str, int')

