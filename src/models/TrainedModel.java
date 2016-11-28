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
	
}
