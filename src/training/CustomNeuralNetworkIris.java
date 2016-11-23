package training;

import scr.SensorModel;

import java.io.*;
import java.util.Arrays;
import java.util.Random;


public class CustomNeuralNetwork implements Serializable {

    private static final long serialVersionUID = -88L;
    private double[][]weights1;
    private double[][]weights2;
    private double[][]weights3;
    private double[][]weights4; 
    private double[]bias1;
    private double[]bias2;
    private double[]bias3;
    private double[]bias4;
    private int numInputNeurons;
    private int numHiddenNeuronsLayer1;
    private int numHiddenNeuronsLayer2;
    private int numHiddenNeuronsLayer3;
    private int numOutputNeurons;
    private Random r = new Random();
    
    public CustomNeuralNetwork(int inputs, int hidden, int hidden2, int hidden3, int outputs) {
    	// This should be a parameter..
    	double sigma = 1;
    	double mean = 0;
    	
    	// initialize class variables
    	weights1 = new double[hidden][inputs]; 
    	weights2 = new double[hidden2][hidden];
    	weights3 = new double[hidden3][hidden2];
    	weights4 = new double[outputs][hidden3];
    	bias1 = new double[hidden];
    	bias2 = new double[hidden2];
    	bias3 = new double[hidden3];
    	bias4 = new double[outputs];
    	numInputNeurons = inputs;
    	numHiddenNeuronsLayer1 = hidden;
    	numHiddenNeuronsLayer2 = hidden2;
    	numHiddenNeuronsLayer3 = hidden3;
    	numOutputNeurons = outputs;
    	
    	// initialize weights 1 with Gaussian distributed values with sigma and mean
    	for (int i=0; i< hidden; i++){
    		for (int j=0; j<inputs; j++){
    			weights1[i][j] = r.nextGaussian() * sigma + mean;
    		}
    	}
    	
    	// initialize weights of layer 2
    	for (int i=0; i< hidden2; i++){
    		for (int j=0; j<hidden; j++){
    			weights2[i][j] = r.nextGaussian() * sigma + mean;
    		}
    	}
    	
    	// initialize weights of layer 3
    	for (int i=0; i< hidden3; i++){
    		for (int j=0; j< hidden2; j++){
    			weights3[i][j] = r.nextGaussian() * sigma + mean;
    		}
    	}
    	
    	// initialize weights 4 with Gaussian distributed values with sigma and mean
    	for (int i=0; i< outputs; i++){
    		for (int j=0; j<hidden3; j++){
    			weights2[i][j] = r.nextGaussian() * sigma + mean; //different seed?
    		}
    	}
    	
    	// initialize bias with Gaussian distributed values with sigma and mean
    	for (int i=0; i< hidden; i++){
    		bias1[i] = r.nextGaussian() * sigma + mean; // different seed?
    	}
    	
    	for (int i=0; i< hidden2; i++){
    		bias2[i] = r.nextGaussian() * sigma + mean; 
    	}
    	
    	for (int i=0; i< hidden3; i++){
    		bias3[i] = r.nextGaussian() * sigma + mean;
    	}
    	
    	for (int i=0; i< outputs; i++){
    		bias4[i] = r.nextGaussian() * sigma + mean; // different seed?
    	}
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
    			a.getTrackEdgeSensors()[17],};
    	System.out.println(Arrays.toString(input));
    	
    	
    	
    	
    	//double[] input = {
    	//		a.getTrackEdgeSensors()[0],
    	//		a.getTrackEdgeSensors()[2],
    	//		a.getTrackEdgeSensors()[4],
    	//		a.getTrackEdgeSensors()[6],
    	//		a.getTrackEdgeSensors()[8],
    	//		a.getTrackEdgeSensors()[10],
    	//		a.getTrackEdgeSensors()[12],
    	//		a.getTrackEdgeSensors()[14],
    	//		a.getTrackEdgeSensors()[16],};
    	//System.out.println(Arrays.toString(input));
    	
    	double[] h1 = new double[numHiddenNeuronsLayer1];
    	double[] h2 = new double[numHiddenNeuronsLayer2];
    	double[] h3 = new double[numHiddenNeuronsLayer3];
    	double[] output = new double[numOutputNeurons];
    	
    	// put SensorModel a in input
    	
    	return calculateOutput(input, output, h1, h2, h3);
    }
    
    public double[] calculateOutput(double[] input, double[] output, double[] h1, double[] h2, double[] h3){
    	for (int i=0; i< numHiddenNeuronsLayer1; i++){
    		for (int j=0; j<numInputNeurons; j++){
    			h1[i] += input[j] * weights1[i][j];
    		}
    		
    		double tempH = h1[i]  +bias1[i]; //add bias
    		h1[i] = 1 / (1 + Math.exp(-tempH)); // Possibly use ReLU instead
    	}
    	
    	for (int i=0; i< numHiddenNeuronsLayer2; i++){
    		for (int j=0; j< numHiddenNeuronsLayer1; j++){
    			h2[i] += h1[j] * weights2[i][j];
    		}
    		
    		double tempH = h2[i] + bias2[i]; 
    		h2[i] = 1/(1 + Math.exp(-tempH)); 
    	}
    	
    	for (int i=0; i< numHiddenNeuronsLayer3; i++){
    		for (int j=0; j< numHiddenNeuronsLayer2; j++){
    			h3[i] += h2[j] * weights3[i][j];
    		}
    		
    		double tempH = h3[i] + bias3[i];
    		h3[i] = 1/(1 + Math.exp(-tempH));
    	}
    	
    	for (int i=0; i< numOutputNeurons; i++){
    		for (int j=0; j<numHiddenNeuronsLayer3; j++){
    			output[i] += h3[j] * weights4[i][j];
    		}
    		
    		double tempH = output[i] + bias4[i]; //add bias
    		output[i] = (Math.exp(tempH * 2.0)-1.0)/(Math.exp(tempH * 2.0)+1.0);
    		//output[i] = 1 / (1 + Math.exp(-tempH)); // Possibly use ReLU instead
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
    

}
