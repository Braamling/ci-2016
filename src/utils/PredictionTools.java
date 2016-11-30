package utils;

import java.io.FileReader;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import models.TrainedModel;

public class PredictionTools {
	private TrainedModel _trainedModel;

	public PredictionTools(String filename){
		_trainedModel = loadWeights(filename);
	}
	
	public TrainedModel getModel(){
		return _trainedModel;
	}
	
	private TrainedModel loadWeights(String filename){
		JSONParser parser = new JSONParser();
		TrainedModel trained_model = new TrainedModel();
        try {
            
            JSONArray hidden_layer_arr = (JSONArray) parser.parse(new FileReader(
                    filename));
            
            for(int i = 0; i < hidden_layer_arr.size(); i++)
            {
                  JSONObject objects = (JSONObject) hidden_layer_arr.get(i);
                  
                  // Retrieve weight and bias for a single layer
                  JSONArray weight =(JSONArray) objects.get("weights");
                  JSONArray bias = (JSONArray) objects.get("bias");
                  
			      int layer_x = weight.size();
                  int layer_y = ((JSONArray)weight.get(0)).size();

                  double[] hidden_layer_bias_arr = new double[bias.size()];
                  double[][] hidden_layer_weights_arr = new double[layer_y][layer_x];

                  for(int j = 0; j < layer_x; j++){
                	  for(int k = 0; k < layer_y; k++){
                		  hidden_layer_weights_arr[k][j] = (double)((JSONArray)weight.get(j)).get(k);
                	  }
                  }
                  
                  // Extract all values from the model
                  for(int j = 0; j < bias.size(); j++){
                      hidden_layer_bias_arr[j] = (double)bias.get(j);
                  }
                  
                  // Set the bias into the trained model
                  trained_model.setBias(i, hidden_layer_bias_arr);
                  trained_model.setWeights(i, hidden_layer_weights_arr);

            }
            
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return trained_model;
	}
}
