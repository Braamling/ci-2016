import tensorflow as tf
import numpy as np
import csv
import random

class LoadSet():
    _input = None
    _output = None
    _filename = None
    _len = None

    def __init__(self, filename, input_s, output_s):
        self._input = np.empty((0, input_s))
        self._output = np.empty((0, output_s))
        self._filename = filename
        self._len = 0

        with open(filename, 'rb') as csvfile:
            file = csv.reader(csvfile, delimiter=',')
            next(file, None)
            counter = 0
            for row in file:
                values = map(float, row)
                if len(values) is input_s + output_s:
                    counter+=1
                    self._input = np.append(self._input, np.array([values[output_s:]]), axis=0)
                    self._output = np.append(self._output, np.array([values[:output_s]]), axis=0)
            self._len = counter

    def getInput(self):
        return self._input

    def getOutput(self):
        return self._output

    def get_len(self):
        return self._len