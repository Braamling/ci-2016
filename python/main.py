from load_set import LoadSet
from learn_track import LearnTrack

def main():
	input_s = 22
	output_s = 3

	loadSet = LoadSet("../resources/train_data/train.csv", input_s, output_s)

	learn_track = LearnTrack(loadSet.getInput(), loadSet.getOutput(), input_s, output_s)
	loadValSet = LoadSet("../resources/train_data/valid.csv", input_s, output_s)
	learn_track.train(loadValSet)

if __name__ == '__main__':
	main()
