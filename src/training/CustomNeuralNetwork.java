package training;

import scr.SensorModel;

import java.io.*;
import java.util.Arrays;

import models.TrainedModel;


public class CustomNeuralNetwork implements Serializable {

    private static final long serialVersionUID = -88L;
    private int numInputNeurons;
    private int[] _hiddenL;
    private int numOutputNeurons;
    private TrainedModel _trainedModel;
    
    public CustomNeuralNetwork(int inputs, int[] hiddenL, int outputs, TrainedModel trainedModel) {
    	
    	// initialize class variables
    	numInputNeurons = inputs;
    	numOutputNeurons = outputs; 
    	
    	_hiddenL = hiddenL;
    	_trainedModel = trainedModel;
    }
    
    public CustomNeuralNetwork getClone(){
    	return new CustomNeuralNetwork(numInputNeurons, _hiddenL.clone(), 
    			                           numOutputNeurons, _trainedModel.getClone());
    }
    
    // Feed forward algorithm
    public double[] getOutput(SensorModel a) {
    	//Initialize intermediate variables
    	double[] input = {
    			a.getSpeed(),
    			a.getTrackPosition(),
    			a.getAngleToTrackAxis(),
    			a.getTrackEdgeSensors()[0],
    			a.getTrackEdgeSensors()[1],
    			a.getTrackEdgeSensors()[2],
    			a.getTrackEdgeSensors()[3],
    			a.getTrackEdgeSensors()[4],
    			a.getTrackEdgeSensors()[5],
    			a.getTrackEdgeSensors()[6],
    			a.getTrackEdgeSensors()[7],
    			a.getTrackEdgeSensors()[8],
    			a.getTrackEdgeSensors()[9],
    			a.getTrackEdgeSensors()[10],
    			a.getTrackEdgeSensors()[11],
    			a.getTrackEdgeSensors()[12],
    			a.getTrackEdgeSensors()[13],
    			a.getTrackEdgeSensors()[14],
    			a.getTrackEdgeSensors()[15],
    			a.getTrackEdgeSensors()[16],
    			a.getTrackEdgeSensors()[17],
    			a.getTrackEdgeSensors()[18]};
    	
    	double[] h1 = new double[_hiddenL[0]];
    	double[] h2 = new double[_hiddenL[1]];
    	double[] h3 = new double[_hiddenL[2]];
    	double[] output = new double[numOutputNeurons];
    	
    	// put SensorModel a in input
    	
    	return calculateOutput(input, output, h1, h2, h3);
    }
    
    public double[] calculateOutput(double[] input, double[] output, double[] h1, double[] h2, double[] h3){
    	for (int i=0; i< _hiddenL[0]; i++){
    		for (int j=0; j<numInputNeurons; j++){
    			h1[i] += input[j] * _trainedModel.getWeights(1)[i][j];
    		}
    		
    		double tempH = h1[i]  + _trainedModel.getBias(1)[i]; //add bias
    		h1[i] = 1 / (1 + Math.exp(-tempH)); // Possibly use ReLU instead
    	}
    	
    	
    	
    	for (int i=0; i< _hiddenL[1]; i++){
    		for (int j=0; j< _hiddenL[0]; j++){
    			h2[i] += h1[j] * _trainedModel.getWeights(2)[i][j];
    		}
    		
    		double tempH = h2[i] + _trainedModel.getBias(2)[i]; 
    		h2[i] = 1/(1 + Math.exp(-tempH)); 
    	}
    	
    	
    	
    	for (int i=0; i< _hiddenL[2]; i++){
    		for (int j=0; j< _hiddenL[1]; j++){
    			h3[i] += h2[j] * _trainedModel.getWeights(3)[i][j];
    		}
    		
    		double tempH = h3[i] + _trainedModel.getBias(3)[i];
    		h3[i] = 1/(1 + Math.exp(-tempH));
    	}
    	
    	for (int i=0; i< numOutputNeurons; i++){
    		for (int j=0; j<_hiddenL[2]; j++){
    			output[i] += h3[j] * _trainedModel.getWeights(4)[i][j];
    		}
    		
    		double tempH = output[i] + _trainedModel.getBias(4)[i]; //add bias
    		output[i] = (Math.exp(tempH * 2.0)-1.0)/(Math.exp(tempH * 2.0)+1.0);
    	}
    	
        return output;
    }
    
    

    //Store the state of this neural network
    public void storeGenome() {
        ObjectOutputStream out = null;
        try {
            //create the memory folder manually
            out = new ObjectOutputStream(new FileOutputStream("memory/mydriver.mem"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (out != null) {
                out.writeObject(this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load a neural network from memory
    public CustomNeuralNetwork loadGenome() {

        // Read from disk using FileInputStream
        FileInputStream f_in = null;
        try {
            f_in = new FileInputStream("memory/mydriver.mem");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Read object using ObjectInputStream
        ObjectInputStream obj_in = null;
        try {
            obj_in = new ObjectInputStream(f_in);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read an object
        try {
            if (obj_in != null) {
                return (CustomNeuralNetwork) obj_in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
	 public void storeJson(String filename){
		 _trainedModel.storeJson(filename);
	 }
    

}
