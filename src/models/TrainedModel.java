package models;

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
	    case 0:  _weight1 = weights;
	    		break;
	    case 1:  _weight2 = weights;
	    		break;
	    case 2:  _weight3 = weights;
	    		break;
	    case 3:  _weight4 = weights;
	    		break;
		}
	}
	
    public void setBias(int layer, double[] bias){
		switch (layer) {
	    case 0:  _bias1 = bias;
	    		break;
	    case 1:  _bias2 = bias;
	    		break;
	    case 2:  _bias3 = bias;
	    		break;
	    case 3:  _bias4 = bias;
	    		break;
		}
	}
}
