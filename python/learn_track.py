import tensorflow as tf

class LearnTrack():
    _input = None
    _output = None
    _input_s = None
    _output_s = None

    def __init__(self, input_data, output_data, input_s, output_s):
        _input = input_data
        _input_s = input_s
        _output = output_data
        _output_s = output_s

    def train():
          # Create the model
          x = tf.placeholder(tf.float32, [None, _input_s])
          # onderstaande x was de orginele x van bram.
          #x = tf.placeholder(tf.float32, [None, _output_s])
          # W = tf.Variable(tf.zeros([_output_s, _input_s]))
          W = tf.Variable(tf.zeros([_input_s, _output_s]))
          b = tf.Variable(tf.zeros([_output_s]))
          y = tf.matmul(x, W) + b

          # Define loss and optimizer
          y_ = tf.placeholder(tf.float32, [None, _input_s])

          # The raw formulation of cross-entropy,
          #
          #   tf.reduce_mean(-tf.reduce_sum(y_ * tf.log(tf.softmax(y)),
          #                                 reduction_indices=[1]))
          #
          # can be numerically unstable.
          #
          # So here we use tf.nn.softmax_cross_entropy_with_logits on the raw
          # outputs of 'y', and then average across the batch.
          cross_entropy = tf.reduce_mean(tf.nn.softmax_cross_entropy_with_logits(y, y_))
          train_step = tf.train.GradientDescentOptimizer(0.5).minimize(cross_entropy)

          sess = tf.InteractiveSession()
          # Train
          tf.initialize_all_variables().run()
          for _ in range(1000):
            batch_xs, batch_ys = mnist.train.next_batch(100)
            sess.run(train_step, feed_dict={x: batch_xs, y_: batch_ys})

          # Test trained model
          correct_prediction = tf.equal(tf.argmax(y, 1), tf.argmax(y_, 1))
          accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))
          print(sess.run(accuracy, feed_dict={x: mnist.test.images,
                                              y_: mnist.test.labels}))
