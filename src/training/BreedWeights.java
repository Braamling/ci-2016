package training;

import scr.SensorModel;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

import models.TrainedModel;
import training.CustomNeuralNetworkIris;

public class BreedWeights {
	
	private int totalSize;
	private TrainedModel parent1;
	private TrainedModel parent2;
	public TrainedModel kid1;
	public TrainedModel kid2;
	private int[] breedMethod; //each number is linked to a crossing method for breeding accompanied with a number for n points 
	// [1,n] = n-point cross over, with n as the step size 
	
	
	public BreedWeights(TrainedModel trainedModel1, TrainedModel trainedModel2,  int[] method){
		parent1 = trainedModel1;
		parent2 = trainedModel2;
		kid1 = parent1.getClone();
		kid2 = parent2.getClone();
		totalSize = parent1.getTotalSize();
		//weightsKid1 = new double[totalSize];
		//weightsKid2 = new double[totalSize];
		breedMethod = method;
	}
	
	public void breedWeights(){
		int type = breedMethod[0];
		int n = breedMethod[1];
		// n-point crossover
		if (type == 1){
			int flag = 1;
			for (int i=0; i<totalSize; i+= n){
				int step = i+n;
				if (flag == 0){
					flag = 1;
				} else {
					flag = 0;
				}
				for (int j=i; j<step; j++){
					if (flag == 0){
						kid1.setStretchedIndexValue(j, parent1.getStretchedIndexValue(j));
						kid2.setStretchedIndexValue(j, parent2.getStretchedIndexValue(j));
					} else {
						kid1.setStretchedIndexValue(j, parent2.getStretchedIndexValue(j));
						kid2.setStretchedIndexValue(j, parent1.getStretchedIndexValue(j));
					}
				}
			}
		// uniform crossover
		} else if (type == 2){
			int counter = 0;
			for (int i=0; i<totalSize; i++){
				double valP1 = parent1.getStretchedIndexValue(i);
				double valP2 = parent2.getStretchedIndexValue(i);
				if (valP1 != valP2){
					// if even
					// kid1 gets parent1 and kid2 gets parent2
					if((counter % 2) == 0){
						kid1.setStretchedIndexValue(i, parent1.getStretchedIndexValue(i));
						kid2.setStretchedIndexValue(i, parent2.getStretchedIndexValue(i));
					// some 'randomness' to prevent structural pattern
					} else if((counter % 3) == 0) {
						kid1.setStretchedIndexValue(i, parent2.getStretchedIndexValue(i));
						kid2.setStretchedIndexValue(i, parent1.getStretchedIndexValue(i));
					} else if((counter % 9) == 0) {
						kid1.setStretchedIndexValue(i, parent1.getStretchedIndexValue(i));
						kid2.setStretchedIndexValue(i, parent2.getStretchedIndexValue(i));
					} else {
						kid1.setStretchedIndexValue(i, parent2.getStretchedIndexValue(i));
						kid2.setStretchedIndexValue(i, parent1.getStretchedIndexValue(i));
					}
				}
				counter++;
			}
		}
	}
	
	
	public TrainedModel getKids(int i){
		if (i == 1){
			return kid1;
		} else if(i == 2){
			return kid2;
		}
		// default
		return kid1;
	}
	
	

}