import tensorflow as tf
import numpy as np
import csv
import random

class LoadSet():
    def __init__(self, filename, input_s, output_s):
        self._input_s = input_s
        self._output_s = output_s
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
        return len(self._input)

    def shuffle(self):
        # Prepare two same shapped input and output arrays 
        new_input = np.empty((self.get_len(), self._input_s))
        new_output = np.empty((self.get_len(), self._output_s))

        # Shuffle the indices of the input and output array
        shuffled_indices = range(0, self.get_len())
        random.shuffle(shuffled_indices)

        # Reorder the input and output arrays
        for i, i_ in enumerate(shuffled_indices):
            new_input[i], self._input[i_]
            new_output[i], self._output[i_]

        # Store the shuffled arrays into the object
        self._input = new_input
        self._output = new_output

    def get_data_len(self):
        return self._data_len

    def shuffle(self):
        # Prepare two same shapped input and output arrays 
        new_input = np.empty((self.get_len(), self._input_s))
        new_output = np.empty((self.get_len(), self._output_s))

        # Shuffle the indices of the input and output array
        shuffled_indices = range(0, self.get_len())
        random.shuffle(shuffled_indices)

        # Reorder the input and output arrays
        for i, i_ in enumerate(shuffled_indices):
            new_input[i], self._input[i_]
            new_output[i], self._output[i_]

        # Store the shuffled arrays into the object
        self._input = new_input
        self._output = new_output


