from load_set import LoadSet
from learn_track import LearnTrack

def main():
	input_s = 22
	output_s = 3

	train_set = LoadSet("../resources/train_data/train.csv", input_s, output_s)
	test_set = LoadSet("../resources/train_data/test.csv", input_s, output_s)
	valid_set = LoadSet("../resources/train_data/valid.csv", input_s, output_s)

	learn_track = LearnTrack(train_set, test_set, valid_set, input_s, output_s)
	
	learn_track.train()

if __name__ == '__main__':
	main()
