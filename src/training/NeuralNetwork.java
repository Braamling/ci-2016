package training;

import scr.SensorModel;

import java.io.*;
import java.util.Arrays;
import java.util.Random;


public class NeuralNetwork implements Serializable {

    private static final long serialVersionUID = -88L;
    private double[][]weights1;
    private double[][]weights2;
    private double[]bias1;
    private double[]bias2;
    private int numInputNeurons;
    private int numHiddenNeurons;
    private int numOutputNeurons;
    private Random r = new Random();
    
    public NeuralNetwork(int inputs, int hidden, int outputs) {
    	// This should be a parameter..
    	double sigma = 1;
    	double mean = 0;
    	
    	// initialize class variables
    	weights1 = new double[hidden][inputs]; 
    	weights2 = new double[outputs][hidden]; 
    	bias1 = new double[hidden];
    	bias2 = new double[outputs];
    	numInputNeurons = inputs;
    	numHiddenNeurons = hidden;
    	numOutputNeurons = outputs;
    	
    	// initialize weights 1 with Gaussian distributed values with sigma and mean
    	for (int i=0; i< hidden; i++){
    		for (int j=0; j<inputs; j++){
    			weights1[i][j] = r.nextGaussian() * sigma + mean;
    		}
    	}
    	
    	// initialize weights 2 with Gaussian distributed values with sigma and mean
    	for (int i=0; i< outputs; i++){
    		for (int j=0; j<hidden; j++){
    			weights2[i][j] = r.nextGaussian() * sigma + mean; //different seed?
    		}
    	}
    	
    	// initialize bias with Gaussian distributed values with sigma and mean
    	for (int i=0; i< hidden; i++){
    		bias1[i] = r.nextGaussian() * sigma + mean; // different seed?
    	}
    	
    	for (int i=0; i< outputs; i++){
    		bias2[i] = r.nextGaussian() * sigma + mean; // different seed?
    	}
    }

    // Feed forward algorithm
    public double[] getOutput(SensorModel a) {
    	//Initialize intermediate variables
    	double[] input = {
    			a.getTrackEdgeSensors()[0],
    			a.getTrackEdgeSensors()[2],
    			a.getTrackEdgeSensors()[4],
    			a.getTrackEdgeSensors()[6],
    			a.getTrackEdgeSensors()[8],
    			a.getTrackEdgeSensors()[10],
    			a.getTrackEdgeSensors()[12],
    			a.getTrackEdgeSensors()[14],
    			a.getTrackEdgeSensors()[16],};
    	System.out.println(Arrays.toString(input));
    	double[] h = new double[numHiddenNeurons];
    	double[] output = new double[numInputNeurons];
    	
    	// put SensorModel a in input
    	
    	for (int i=0; i< numHiddenNeurons; i++){
    		for (int j=0; j<numInputNeurons; j++){
    			h[i] += input[j] * weights1[i][j];
    		}
    		
    		double tempH = h[i]  +bias1[i]; //add bias
    		h[i] = 1 / (1 + Math.exp(-tempH)); // Possibly use ReLU instead
    	}
    	
    	for (int i=0; i< numOutputNeurons; i++){
    		for (int j=0; j<numHiddenNeurons; j++){
    			output[i] += h[j] * weights2[i][j];
    		}
    		
    		double tempH = output[i] + bias2[i]; //add bias
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
    public NeuralNetwork loadGenome() {

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
                return (NeuralNetwork) obj_in.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    

}
