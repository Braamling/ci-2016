package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.File;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import models.TrainedModel;

public class PredictionTools {
	private TrainedModel _trainedModel;

	public PredictionTools(){
		
	}
	
	public PredictionTools(String filename){
		_trainedModel = loadWeights(filename);
	}
	
	public TrainedModel getModel(){
		return _trainedModel;
	}
	
	public void createVariations(){
		for(int i = 0; i < 30; i++){
			TrainedModel model = loadWeights("./resources/variations/best_i.json");
			model.variate(0.01, 0.1);
			model.storeJson("./resources/variations/var_" + i + ".json");
		}
	}
	
	private String readFileStream(String filename){
		File file = new File(filename);
		FileInputStream fis;
		try {
			fis = new FileInputStream(file);
			byte[] data = new byte[(int) file.length()];
			fis.read(data);
			fis.close();
			return new String(data, "UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
		
	}
	
	private TrainedModel loadWeights(String filename){
		JSONParser parser = new JSONParser();
		TrainedModel trained_model = new TrainedModel();
        try {
            

            InputStream in = getClass().getResourceAsStream(filename); 
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        	
            JSONArray hidden_layer_arr = (JSONArray) parser.parse(reader);
//            JSONArray hidden_layer_arr = (JSONArray) parser.parse(readFileStream(
//                    filename));
            
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
                  trained_model.setBias(i+1, hidden_layer_bias_arr);
                  trained_model.setWeights(i+1, hidden_layer_weights_arr);

            }
            
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return trained_model;
	}
}
