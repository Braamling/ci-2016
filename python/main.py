from load_set import LoadSet
from learn_track import LearnTrack

def main():
	input_s = 3 
	output_s = 22

	loadSet = LoadSet("../resources/train_data/train.csv", input_s, output_s)

	learn_track = LearnTrack(LoadSet.getInput(), LoadSet.getOutput(), input_s, output_s)
	
	learn_track.train()

if __name__ == '__main__':
	main()