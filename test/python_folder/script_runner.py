import os
import sys
import glob
import time
import subprocess


def trash():
    parent_path = os.path.dirname(os.path.abspath((os.path.dirname(__file__))))
    input_path = parent_path + '\\resources\\input'
    output_path = parent_path + '\\resources\\output'

    print(parent_path)
    print(input_path)
    print(output_path)
    print(sys.path)
    sys.path.append(input_path)

    my_file = 'test0-input.txt'
    index = 0

    while not os.path.isfile(f'hello{index}.txt'):
        print('no file')
        index += 1
    else:
        print('yes file')

    try:
        print(os.path.isfile(input_path + '\\' + my_file))
        with open(input_path + '\\' + my_file) as file:
            file.read()
    except FileNotFoundError:
        print(f'No file named {my_file} found!')


def get_input_and_output_path():
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


def run_java(java_file_path: str, test_number: int):
    java_file = extract_file_name_from_abs_path(java_file_path)
    abs_path = extract_file_path_from_abs_path(java_file_path)

    start_time = time.clock()
    time.sleep(1)
    chdir(abs_path)
    print(f'Hi, your path: {abs_path}, test number: {test_number}')
    # subprocess.check_call(['java', java_file, test_number], shell=True)
    end_time = time.clock()
    print(f'time elapsed: {end_time - start_time}')


def start_testing(java_file_path: str, test_number: int):
    input_path, output_path, solver_output_path = get_input_and_output_path()

    chdir(input_path)
    if test_number == -1:
        print('No second argument specified, executing all tests...')
        for test_number, _ in enumerate(glob.glob('test[0-9]-input.txt')):
            run_java(java_file_path, test_number)

        if test_number == -1:
            print(f'No input files with \'testX-input.txt\' format found in {os.getcwd()}')
            return
    else:
        if os.path.isfile(f'test{test_number}-input.txt'):
            run_java(java_file_path, test_number)
        else:
            print(f'File with test number {test_number} not found!')
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
