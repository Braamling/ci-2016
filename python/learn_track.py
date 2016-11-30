import tensorflow as tf
import os
from model import Model

import numpy as np
class LearnTrack():
    _current_pointer = 0
    _current_pointer_val = 0
    _current_pointer_test = 0
    mdl = 0
    def __init__(self, train_set, test_set, valid_set, model_dir_name):
        self._train_set = train_set
        self._train_set.shuffle()
        self._test_set = test_set
        self._valid_set = valid_set
        self.model_name = model_dir_name

    def train(self, sess, mdl, saver, epochs, batch_size):

        n_batches = len(self._train_set.getInput()) / batch_size
        n_batches_val = len(self._valid_set.getInput()) / batch_size
        n_batches_test = len(self._test_set.getInput()) / batch_size
        smallest_valid_error = 200000


        # Make a directory if it doesnt exist yet.
        if not os.path.exists(self.model_name):
            os.makedirs(self.model_name)

        # Train for each epoch
        for epoch in range(epochs):
            # shuffle TODO
            t_loss = 0.0

            # Train the network on the training set
            for i in range(n_batches+1):
                batch_xs, batch_ys = self.next_batch(batch_size)
                (_, train_loss) = sess.run([mdl.train_step, mdl.loss], feed_dict={mdl.x: batch_xs, mdl.y_: batch_ys})
                t_loss += train_loss
            t_loss *= 1.0/n_batches
            print (" The training loss for epoch %d and is %g"% (epoch, t_loss))

            # Run a feedforward through the validation set
            v_loss = 0.0
            for i in range(n_batches_val+1):
                batch_xs, batch_ys = self.next_val_batch(batch_size)
                valid_loss, correct, pred = sess.run([mdl.loss, mdl.y, mdl.y_], feed_dict={mdl.x: batch_xs, mdl.y_: batch_ys})
                v_loss += valid_loss
                if epoch == epochs-1:
                    print str(correct[0]) + str(pred[0])
            v_loss *= 1.0 / n_batches_val
            print (" The validation loss for epoch %d and is %g" % (epoch, v_loss))

            if v_loss < smallest_valid_error and epoch > 10000:
                saver.save(sess, self.model_name + 'model.ckpt')
                tf.train.write_graph(sess.graph_def, '.', 'trained_model.proto', as_text=False)
                tf.train.write_graph(sess.graph_def, '.', 'trained_model.txt', as_text=True)
                smallest_valid_error = v_loss
                print("saved model with error: " + str(smallest_valid_error))

            # reset the pointer for batches
            self._current_pointer = 0
            self._current_pointer_val = 0


    def next_batch(self, size):
        prev_pointer = self._current_pointer
        self._current_pointer += size
        if (self._current_pointer > self._train_set.get_len):
            self._current_pointer = self._train_set.get_len-1
        return self._train_set.getInput()[prev_pointer: self._current_pointer],\
            self._train_set.getOutput()[prev_pointer: self._current_pointer]

    def next_val_batch(self, size):
        prev_pointer = self._current_pointer_val
        self._current_pointer_val += size
        if (self._current_pointer_val > self._valid_set.get_len):
            self._current_pointer_val = self._valid_set.get_len - 1
        return self._valid_set.getInput()[prev_pointer: self._current_pointer_val], \
               self._valid_set.getOutput()[prev_pointer: self._current_pointer_val]

    def next_test_batch(self, size):
        prev_pointer = self._current_pointer_test
        self._current_pointer_test += size
        if (self._current_pointer_test > self._test_set.get_len):
            self._current_pointer_test = self._test_set.get_len - 1
        return self._test_set.getInput()[prev_pointer: self._current_pointer_test], \
               self._test_set.getOutput()[prev_pointer: self._current_pointer_test]
