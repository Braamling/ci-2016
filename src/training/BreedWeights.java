package training;

import scr.SensorModel;

import java.io.*;
import java.util.Arrays;
import java.util.Random;

import models.TrainedModel;
import training.CustomNeuralNetworkIris;

public class BreedWeights {
	
	private int layers;
	private int[] hiddenNodes;
	private double[] weightsParent1;
	private double[] weightsParent2;
	private double[][] w1Parent1;
	private double[][] w2Parent1;
	private double[][] w3Parent1;
	private double[][] w4Parent1;
	private double[][] w1Parent2;
	private double[][] w2Parent2;
	private double[][] w3Parent2;
	private double[][] w4Parent2;
	private int[] breedMethod; //each number is linked to a crossing method for breeding accompanied with a number for n points 
	// [1,n] = n-point cross over, with n as the step size 
	
	public BreedWeights(CustomNeuralNetworkIris neuralNetwork1, CustomNeuralNetworkIris neuralNetwork2, int[] method){
		// initialize the values
		hiddenNodes = neuralNetwork1.getLayers();
		layers = hiddenNodes.length;
		weightsParent1 = new double[layers];
		weightsParent2 = new double[layers];
		
		w1Parent1 = neuralNetwork1.getWeights(1);
		w2Parent1 = neuralNetwork1.getWeights(2);
		w3Parent1 = neuralNetwork1.getWeights(3);
		w4Parent1 = neuralNetwork1.getWeights(4);
		
		w1Parent2 = neuralNetwork2.getWeights(1);
		w2Parent2 = neuralNetwork2.getWeights(2);
		w3Parent2 = neuralNetwork2.getWeights(3);
		w4Parent2 = neuralNetwork2.getWeights(4);
		
		breedMethod = method;
	}
	
	public double[][] breedWeights(int layer, int kids){
		if(layer == 1){
			double[][] newWeightLayer = breedWeightsOneLayer(w1Parent1, w1Parent2, breedMethod, kids);
			
		}
			
		
	}
	
	
	private double[][] breedWeightsOneLayer(double[][] wp1, double[][] wp2, int[] method, int kinds){
		// n point cross over 
		double[][] newWeights = new double[wp1.length][wp1[1].length];
		if(method[0] == 1){
			int n = method[1];
			for(int i=0; i<wp1.length; i++){
				for (int j=0; j<wp1[1].length; j+= n){
					
				}
			}
		}
	}
}