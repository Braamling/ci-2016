from load_set import LoadSet
from learn_track import LearnTrack
from model import Model
from math import sqrt
import numpy as np
import tensorflow as tf
import snakeoil2
import json

def drive_tensorflow(c, sess, mdl):
    S= c.S.d
    R= c.R.d

    # print vars()
    input = np.asarray([sqrt(S['speedX']**2 + S['speedY']**2)] + [S['trackPos']] + S['track'] + [0.])

    response = mdl.feedforward(sess, input)
    R['accel'] = response[0]
    R['break'] = response[1]
    R['steer'] = response[2]


    # Automatic Transmission

    R['gear']=1
    if S['speedX']>50:
        R['gear']=2
    if S['speedX']>80:
        R['gear']=3
    if S['speedX']>110:
        R['gear']=4
    if S['speedX']>140:
        R['gear']=5
    if S['speedX']>170:
        R['gear']=6

def drive(sess, mdl):
    C = snakeoil2.Client()

    for step in xrange(C.maxSteps, 0, -1):
        C.get_servers_input()
        drive_tensorflow(C, sess, mdl)
        C.respond_to_server()
    C.shutdown()

def train(sess, mdl, saver, model_name, input_s, output_s):
    train_set = LoadSet("../resources/train_data/train.csv", input_s, output_s)
    test_set = LoadSet("../resources/train_data/test.csv", input_s, output_s)
    valid_set = LoadSet("../resources/train_data/valid.csv", input_s, output_s)

    print("Loaded data")

    learn_track = LearnTrack(train_set, test_set, valid_set, model_name)
    learn_track.train(sess, mdl, saver, epochs=5000, batch_size=100)

def export_weights_to_json(sess, mdl):
    weights_list, bias_list  = sess.run([mdl.W, mdl.b])

    d = []
    for i in range(len(weights_list)):
        w = weights_list[i].tolist()
        b = bias_list[i].tolist()
        d.append({'weights': w, 'bias': b})

    with open('weights_nn1.json', 'w') as outfile:
        json.dump(d, outfile, indent=4)

def main():
    input_s = 22
    output_s = 3
    n_hidden_neurons = [100, 50, 20, 10, 5]
    model_name = 'h100_50_20_l_008_test4/'
    batch_size = 100

    # Load model structure
    mdl = Model(input_s, output_s, n_hidden_neurons, model_name, batch_size)

    # Can be moved somewhere else later.
    sess = tf.Session()
    sess.run(tf.initialize_all_variables())

    saver = tf.train.Saver()
    saver_def = saver.as_saver_def()
    print saver_def.filename_tensor_name
    print saver_def.restore_op_name

    # Load previous model values if they exists.
    ckpt = tf.train.get_checkpoint_state(model_name)
    if ckpt and ckpt.model_checkpoint_path:
        saver.restore(sess, ckpt.model_checkpoint_path)
    else:
        print "No checkpoint found, training from scratch!"

    #print(sess.run(mdl.W))
    #print(sess.run(mdl.b))

    # train(sess, mdl, saver, model_name, input_s, output_s)

    #drive(sess, mdl)

    export_weights_to_json(sess, mdl)

if __name__ == '__main__':
	main()
