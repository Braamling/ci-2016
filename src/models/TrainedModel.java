package models;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TrainedModel {
	public double[][] _weight1;
	public double[][] _weight2;
	public double[][] _weight3;
	public double[][] _weight4;

	public double[] _bias1;
	public double[] _bias2;
	public double[] _bias3;
	public double[] _bias4;

	public TrainedModel(){

	}

	public TrainedModel(double[][] weight1, double[][] weight2, double[][] weight3, 
			double[][] weight4, double[] bias1, double[] bias2, 
			double[] bias3, double[] bias4){
		_weight1 = weight1;
		_weight2 = weight2;
		_weight3 = weight3;
		_weight4 = weight4;	

		_bias1 = bias1;
		_bias2 = bias2;
		_bias3 = bias3;
		_bias4 = bias4;	

	}
	
	public TrainedModel getClone(){
		return new TrainedModel(_weight1.clone(), _weight2.clone(), _weight3.clone(), 
							    _weight4.clone(),_bias1.clone(),_bias2.clone(),
							    _bias3.clone(),_bias4.clone());
	}

	public double[][] getWeights(int layer){
		switch(layer){
		case 1: 
			return _weight1;
		case 2: 
			return _weight2;
		case 3:
			return _weight3;
		case 4:
			return _weight4;
		default:
			return _weight4;
		}
	}

	public double[] getBias(int layer){
		switch(layer){
		case 1:
			return _bias1;
		case 2: 
			return _bias2; 
		case 3: 
			return _bias3;
		case 4: 
			return _bias4;
		default:
			return _bias4;
		}
	}

	public void setWeights(int layer, double[][] weights){
		switch (layer) {
		case 1:  _weight1 = weights;
		break;
		case 2:  _weight2 = weights;
		break;
		case 3:  _weight3 = weights;
		break;
		case 4:  _weight4 = weights;
		break;
		}
	}

	public void setBias(int layer, double[] bias){
		switch (layer) {
		case 1:  _bias1 = bias;
		break;
		case 2:  _bias2 = bias;
		break;
		case 3:  _bias3 = bias;
		break;
		case 4:  _bias4 = bias;
		break;
		}
	}

	public int getTotalSize(){
		int weight_size = _weight1.length + _weight2.length + _weight3.length + _weight4.length;
		int bias_size = _bias1.length + _bias2.length + _bias3.length + _bias4.length;

		return weight_size + bias_size;
	}

	/**
	 * Get the value of a hidden unit in the network mapped to an 1d array.
	 * 
	 * @param index
	 * @return
	 */
	 public double getStretchedIndexValue(int index){		
		 int[] sizes = {_weight1.length * _weight1[0].length, _weight2.length * _weight2[0].length, 
				 _weight3.length * _weight3[0].length, _weight4.length * _weight4[0].length, 
				 _bias1.length, _bias2.length, _bias3.length, _bias4.length};


		 // Search for the correct weight of bias value corresponding to the index
		 for (int i = 0; i < sizes.length; i++) { 
			 if((index - sizes[i]) < 0){
				 if(i < sizes.length/2){
					 double[][] weights = getWeights(i + 1);
					 for(int j = 0; j < weights.length; j++){
						 if(index - weights[0].length < 0){
							 return weights[j][index];
						 }else{
							 index -= weights[0].length;
						 }
					 }
				 }else{
					 System.out.println(i - ((sizes.length/2) - 1));
					 System.out.println(i);

					 return getBias(i - ((sizes.length/2) - 1))[index];
				 }

			 }else{
				 index -= sizes[i];
			 }
		 }

		 return 0.0;
	 }

	 /**
	  * Set a hidden unit in the network to a specific value. The index is the whole network
	  * mapped to a 1d array.
	  * 
	  * @param index
	  * @param value
	  */
	 public void setStretchedIndexValue(int index, double value){

		 int[] sizes = {_weight1.length * _weight1[0].length, _weight2.length * _weight2[0].length, 
				 _weight3.length * _weight3[0].length, _weight4.length * _weight4[0].length, 
				 _bias1.length, _bias2.length, _bias3.length, _bias4.length};


		 // Search for the correct weight of bias value corresponding to the index
		 for (int i = 0; i < sizes.length; i++) { 
			 if((index - sizes[i]) < 0){
				 if(i < (sizes.length/2)){
					 double[][] weights = getWeights(i + 1);
					 // Search in the 2d hidden layer for the 1d index.
					 for(int j = 0; j < weights.length; j++){
						 if(index - weights[0].length < 0){
							 weights[j][index] = value;
							 setWeights(i + 1, weights);
							 return;
						 }else{
							 index -= weights[0].length;
						 }
					 }
				 }else{
					 double[] bias = getBias(i - ((sizes.length/2) - 1));
					 bias[index] = value;
					 setBias(i - ((sizes.length/2) - 1), bias);
				 }

			 }else{
				 index -= sizes[i];
			 }
		 }

	 }

	 public String doubleArrayToString(double[] array){
		 StringBuilder stringBuilder = new StringBuilder();


		 for (int i = 0; i < array.length; i++){
			 stringBuilder.append(array[i] + " - ");
		 }

		 return stringBuilder.toString();

	 }

	 public String doubleArrayToString(double[][] array){
		 StringBuilder stringBuilder = new StringBuilder();


		 for (int i = 0; i < array.length; i++){
			 for (int j = 0; j < array[0].length; j++){
				 stringBuilder.append(array[i][j] + " - ");
			 }
		 }

		 return stringBuilder.toString();

	 }

	 @Override
	 public String toString(){
		 StringBuilder stringBuilder = new StringBuilder();

		 stringBuilder.append(doubleArrayToString(_weight1));
		 stringBuilder.append(doubleArrayToString(_weight2));
		 stringBuilder.append(doubleArrayToString(_weight3));
		 stringBuilder.append(doubleArrayToString(_weight4));	
		 stringBuilder.append(doubleArrayToString(_bias1));
		 stringBuilder.append(doubleArrayToString(_bias2));
		 stringBuilder.append(doubleArrayToString(_bias3));
		 stringBuilder.append(doubleArrayToString(_bias4));	

		 return stringBuilder.toString();

	 }

	 public void storeJson(String filename){
		 JSONArray output = new JSONArray();

		 for(int i = 1; i < 5; i++)
		 {
			 JSONObject object = new JSONObject();
			 
			 double[][] weights = getWeights(i);
			 double[] bias = getBias(i);

			 JSONArray json_weights = new JSONArray();
			 JSONArray json_bias = new JSONArray();

			 for(int j = 0; j < weights[0].length; j++){
				 JSONArray json_weights_i = new JSONArray();
				 
				 for(int k = 0; k < weights.length; k++){
					 json_weights_i.add(weights[k][j]);
				 }
				 json_weights.add(json_weights_i);
			 }

			 // Extract all values from the model
			 for(int j = 0; j < bias.length; j++){
				 json_bias.add(bias[j]);
			 }

			 object.put("weights", json_weights);
			 object.put("bias", json_bias);
			 
			 output.add(object);

		 }
		 
		 // try-with-resources statement based on post comment below :)
		 try (FileWriter file = new FileWriter(filename)) {
			 file.write(output.toJSONString());
//			 System.out.println("Successfully Copied JSON Object to File...");
//			 System.out.println("\nJSON Object: " + output);
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 
	   /**
	     * 
	     * @param shift_max Maximum shift per hidden unit
	     * @param shift_percentage Maximum amount of hidden units to shift.
	     */
	    public void variate(double shift_max, double shift_percentage) {    	
	    	int modelSize = getTotalSize();
	    	System.out.println(modelSize);
	    	
	    	int shift_amount = (int) (((float)modelSize) * shift_percentage);
	    	
	    	int[] indices = getRandomHiddenUnitIndices(modelSize, shift_amount);
	    	
	    	for( int index: indices){
	    		double value = getStretchedIndexValue(index);

	    		value = value + randomDouble(-shift_max, shift_max);
	    		
	    		setStretchedIndexValue(index, value);   		
	    		
	    	}
	    }
	    
	    /**
	     * 
	     * @param shift_max Maximum shift per hidden unit
	     * @param shift_percentage Maximum amount of hidden units to shift.
	     */
	    public void mutate(double mutate_range, double mutate_percentage) {    	
	    	int modelSize = getTotalSize();
	    	System.out.println(modelSize);
	    	
	    	int shift_amount = (int) (((float)modelSize) * mutate_percentage);
	    	
	    	int[] indices = getRandomHiddenUnitIndices(modelSize, shift_amount);
	    	
	    	for( int index: indices){
	    		double value = randomDouble(-mutate_range, mutate_range);
	    		System.out.println(value);
	    		
	    		setStretchedIndexValue(index, value);   		
	    		
	    	}
	    }
	    
	    public int[] getRandomHiddenUnitIndices(int networkLength, int amount) {
	        ArrayList<Integer> list = new ArrayList<Integer>();
	        
	        for (int i=1; i<networkLength; i++) {
	            list.add(new Integer(i));
	        }
	        
	        int[] indices = new int[amount];
	        
	        Collections.shuffle(list);
	        for (int i=0; i<amount; i++) {
	        	indices[i] = (list.get(i));
	        }
	        
	        return indices;
	    }
	    
	    private double randomDouble(double min, double max){
	        Random rand = new Random();
	        return rand.nextDouble() * (max - min) + min;
	    }

}
