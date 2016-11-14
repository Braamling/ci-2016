import tensorflow as tf
import numpy as np
import csv

class LoadSet():
    _input = None
    _output = None
    _filename = None

    def __init__(self, filename, input_s, output_s):
        _input = np.empty((0, input_s))
        _output = np.empty((0, output_s))
        _filename = filename

        with open(filename, 'rb') as csvfile:
            file = csv.reader(csvfile, delimiter=',')
            next(file, None)
            for row in file:
                values = map(float, row)
                if len(values) is input_s + output_s:
                    _input = np.append(_input, np.array([values[:input_s]]), axis=0)
                    _output = np.append(_output, np.array([values[input_s:]]), axis=0)

        print(_input)
        print(_output)
                

