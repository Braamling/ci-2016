package utils;

import java.io.FileReader;
 
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class PredictionTools {

	public PredictionTools(String filename){
		load_weights(filename);
	}
	
	
	private void load_weights(String filename){
		JSONParser parser = new JSONParser();
		 
        try {
 
        	JSONObject jsonObject = (JSONObject)parser.parse(new FileReader(
                    filename));
            
            JSONArray weights = (JSONArray) jsonObject.get("weights");

            for(int i = 0; i < weights.size(); i++)
            {
                  JSONObject objects = (JSONObject) weights.get(i);
                  System.out.println(objects.get("weight"));
                  System.out.println(objects.get("bias"));
                  
                  // TODO Load into workable object
            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
}