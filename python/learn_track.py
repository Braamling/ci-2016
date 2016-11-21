import tensorflow as tf

class LearnTrack():
    _current_pointer = 0


    def __init__(self, train_set, test_set, valid_set, input_s, output_s):
        self._train_set = train_set
        self._train_set.shuffle()
        self._test_set = test_set
        self._valid_set = valid_set

        self._input_s = input_s
        self._output_s = output_s

    def train(self):
        batch_size = 100
        # Create the model
        x = tf.placeholder(tf.float32, [None, self._input_s])
        W = tf.Variable(tf.zeros([self._input_s, self._output_s]))
        b = tf.Variable(tf.zeros([self._output_s]))
        y = tf.matmul(x, W) + b

        # Define loss and optimizer
        y_ = tf.placeholder(tf.float32, [None, self._output_s])

        # Define the cost function and optimizer. 
        # Cost is a least squares functions
        cost = tf.nn.l2_loss((y-y_)/batch_size)
        train_step = tf.train.AdamOptimizer(0.5).minimize(cost)

        # Prepare the session
        sess = tf.InteractiveSession()        
        tf.initialize_all_variables().run()

        print("Start training")
        for _ in range(self._train_set.getLength()/batch_size):
            batch_xs, batch_ys = self.next_batch(batch_size)
            _, loss = sess.run([train_step, cost], feed_dict={x: batch_xs, y_: batch_ys})
            # print(loss)

        print(W.eval())
        print(b.eval())

        print("Finished training")

    def next_batch(self, size):
        prev_pointer = self._current_pointer
        self._current_pointer += size

        return self._train_set.getInput()[prev_pointer: self._current_pointer],\
            self._train_set.getOutput()[prev_pointer: self._current_pointer]

