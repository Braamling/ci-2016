from load_set import LoadSet
from learn_track import LearnTrack
from model import Model

def main():
    input_s = 22
    output_s = 3
    n_hidden_neurons = [100, 50, 20]
    model_name = 'h100_50_20_l_008_test2/'
    batch_size = 100

    train_set = LoadSet("../resources/train_data/train.csv", input_s, output_s)
    test_set =  LoadSet("../resources/train_data/test.csv", input_s, output_s)
    valid_set = LoadSet("../resources/train_data/valid.csv", input_s, output_s)

    print("Loaded data")


    mdl = Model(input_s, output_s, n_hidden_neurons, model_name, batch_size)

    learn_track = LearnTrack(train_set, test_set, valid_set, 'h100_50_20_l_008_test2/')
    learn_track.train(epochs=1000, mdl=mdl, batch_size = 100)


if __name__ == '__main__':
	main()