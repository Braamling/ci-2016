package training;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.util.TransferFunctionType;

import models.OfflineDataPoint;

public class OfflineLearning {
	static private void iterateTrainingData(){
	    String line = "";
		String trainingFile = "resources/train_data/train.csv";
	    String cvsSplitBy = ",";

	    // Init Neural network and training data
	    MultiLayerPerceptron myMlPerceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 22, 25, 3);
	    DataSet trainingSet = new DataSet(22, 3);
	    
        try (BufferedReader br = new BufferedReader(new FileReader(trainingFile))) {
        	// Discard header
        	br.readLine();
            while ((line = br.readLine()) != null) {

                // Retreive datapoints
//            	System.out.println(line);
                String[] data_point = line.split(cvsSplitBy);
                OfflineDataPoint data = new OfflineDataPoint(data_point);
                
                // Failsafe, we need the correct amount of input values. 
                if(data_point.length == (25)){
            		// Add training data to training set (logical OR function)
            		trainingSet.addRow(new DataSetRow (data.getTrackSensors(),
            				data.getOutput()));
                }
            }

        } catch (IOException e) {
        	
            e.printStackTrace();
        }
        
//         learn the training set
        System.out.println("Learn trainingset");
        myMlPerceptron.learn(trainingSet);
//		 save the trained network into file
		myMlPerceptron.save("or_perceptron.nnet");
		
		System.out.println("finished");
	}
	
	
	
    public static void main(String[] args) {
    	iterateTrainingData();
    }
}
