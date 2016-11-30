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
    input = np.asarray([S['speedX']] + [S['trackPos']]  + [S['angle']]  + S['track'])
    print(input)
    response = mdl.feedforward(sess, input)
    print response

    # Automatic Transmission

    R['gear'] = 1
    if S['speedX'] > 50:
        R['gear'] = 2
    if S['speedX'] > 80:
        R['gear'] = 3
    if S['speedX'] > 110:
        R['gear'] = 4
    if S['speedX'] > 140:
        R['gear'] = 5
    if S['speedX'] > 170:
        R['gear'] = 6

    # if abs(response[1]-1) < abs(response[0]-1):
    #     response[1] = 1
    #     response[0] = 0
    # else:
    #     response[1] = 0
    #     response[0] = 1
    R['accel'] = response[0]
    R['brake'] = response[1]
    R['steer'] = response[2]
def drive(sess, mdl):
    # C = snakeoil2.Client()
    C = snakeoil2.Client()
    for step in xrange(C.maxSteps, 0, -1):
        C.get_servers_input()
        drive_tensorflow(C, sess, mdl)
        C.respond_to_server()
    C.shutdown()

def train(sess, mdl, saver, model_name, input_s, output_s, batch_size):
    train_set = LoadSet("../resources/train_data/aalborg.csv", input_s, output_s)
    test_set = LoadSet("../resources/train_data/aalborg.csv", input_s, output_s)
    valid_set = LoadSet("../resources/train_data/aalborg.csv", input_s, output_s)

    print("Loaded data")

    learn_track = LearnTrack(train_set, test_set, valid_set, model_name)
    learn_track.train(sess, mdl, saver, epochs=100000, batch_size=batch_size)
    # start = 2074
    # for i in range(40):
    #     print train_set.getInput()[start+i]
    #     print train_set.getOutput()[start + i]
    #     print mdl.feedforward(sess, train_set.getInput()[start+i])

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
    n_hidden_neurons = [100, 50, 20]
    model_name = 'aalborg_overfit/' #'h100_50_20_l_008_test4'
    batch_size = 4000

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

    train(sess, mdl, saver, model_name, input_s, output_s, batch_size)

    # drive(sess, mdl)

    # export_weights_to_json(sess, mdl)

if __name__ == '__main__':
	main()
