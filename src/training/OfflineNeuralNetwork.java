package training;

import models.OfflineDataPoint;
import scr.SensorModel;

public class OfflineNeuralNetwork extends NeuralNetwork {

	private static final long serialVersionUID = 1L;

	public OfflineNeuralNetwork(int inputs, int hidden, int outputs) {
		super(inputs, hidden, outputs);
	}

	public double[] getOutput(OfflineDataPoint datapoint ) {
		return null;
	}
}
