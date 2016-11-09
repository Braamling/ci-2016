package models;

import java.util.Arrays;

/**
 * A representation of a single measurement used for offline training a neural network.
 * 
 * @author bram
 *
 */
public class OfflineDataPoint {
	private double[] _trackSensors;
	private double[] _output;
	

	/**
	 * Initiate a datapoint with a list of string values representing all measurents.
	 * 
	 * @param datapoint 
	 */
	public OfflineDataPoint(String[] datapoint){
		 double[] datapoint_d = parseDoubles(datapoint);
		 
		 // Split the data into the output data (Accelaration, Brake, Steering)
		 _output = Arrays.copyOfRange(datapoint_d, 0, 3);
		 
		 // Retrive the tracksensors data (Speed and tracksensors)
		 _trackSensors = Arrays.copyOfRange(datapoint_d, 3, datapoint_d.length);
	}
	
	
	/**
	 * Parse all double from a list of double represented as strings.
	 * 
	 * @param datapoint
	 * @return List of doubles parsed from input.
	 */
	private double[] parseDoubles(String[] datapoint){
		double[] datapoint_d = new double[datapoint.length];
		
		
		for(int i = 0; i < datapoint.length; i++){
			datapoint_d[i] = Double.parseDouble(datapoint[i]);
		}
		
		return datapoint_d;
	}

	// Getters and settings.

	public double[] getTrackSensors() {
		return _trackSensors;
	}

	public void setTrackSensors(double[] _trackSensors) {
		this._trackSensors = _trackSensors;
	}

	public double[] getOutput() {
		return _output;
	}

	public void setOutput(double[] _output) {
		this._output = _output;
	}
}
