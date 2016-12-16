import tensorflow as tf
import numpy as np

def weight_variable(shape):
    initial = tf.truncated_normal(shape, stddev=0.1)
    return tf.Variable(initial)

def bias_variable(shape):
    initial = tf.constant(0.1, shape=shape)
    return tf.Variable(initial)

def layerNN(input, weights, bias):
    return tf.matmul(input, weights) + bias

class Model():
    def __init__(self, input_s, output_s, n_hidden_neurons, model_dir_name, batch_size):
        self._input_s = input_s
        self._output_s = output_s
        self.n_hidden_neurons = n_hidden_neurons
        self.model_name = model_dir_name

        # Define input and target variables
        self.x = tf.placeholder(tf.float32, [None, self._input_s])
        self.y_ = tf.placeholder(tf.float32, [None, self._output_s])

        self.W = []
        self.b = []
        self.W.append(weight_variable([self._input_s, self.n_hidden_neurons[0]]))
        self.b.append(bias_variable([self.n_hidden_neurons[0]]))
        for i in range(1, len(self.n_hidden_neurons)):
            self.W.append(weight_variable([self.n_hidden_neurons[i - 1], self.n_hidden_neurons[i]] ))
            self.b.append(bias_variable([self.n_hidden_neurons[i]] ))
        self.W.append(weight_variable([self.n_hidden_neurons[-1], self._output_s] ))
        self.b.append(bias_variable([self._output_s]))

        # Initialize layers
        self.Layers = []
        self.Layers.append(layerNN(self.x, self.W[0], self.b[0]))
        self.Layers[0] = tf.nn.sigmoid(self.Layers[0])
        for i in range(1, len(self.n_hidden_neurons)):
            self.Layers.append(tf.nn.sigmoid(layerNN(self.Layers[i - 1], self.W[i], self.b[i])))
        self.y = layerNN(self.Layers[-1], self.W[-1], self.b[-1])
        # Hardcoded activation function last layer
        self.y = tf.nn.tanh(self.y)
        # self.y[:1] = tf.nn.tanh(self.y)
        # self.y[-1] = tf.nn.sigmoid(self.y[-1])


        # define the loss
        # self.reg_losses = tf.get_collection(tf.GraphKeys.REGULARIZATION_LOSSES)
        # self.reg_constant = 0.01
        self.loss = tf.nn.l2_loss(self.y - self.y_) + self.reg_constant * sum(self.reg_losses)
        self.train_step = tf.train.AdamOptimizer(0.0001, epsilon=0.1).minimize(self.loss)

    def feedforward(self, sess, input):
        values = sess.run(self.y, feed_dict={self.x: [input], self.y_: np.zeros((len(input), self._output_s))})
        return values[0]

    def get_loss(self, sess, input, output):
        loss = sess.run(self.loss, feed_dict={self.x: input, self.y_: output})
        return loss
