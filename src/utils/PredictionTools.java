package utils;

import java.nio.FloatBuffer;

import org.bytedeco.javacpp.tensorflow;
import org.bytedeco.javacpp.tensorflow.*;

import scr.SensorModel;


public class PredictionTools {
	Status _status;
	final Session _session;
	
	public PredictionTools(String filename){
		
		_session = new Session(new SessionOptions());
		
		GraphDef def = new GraphDef();
		tensorflow.ReadBinaryProto(Env.Default(), 
				filename + ".proto", def);
		_status = _session.Create(def);
		if (!_status.ok()) {
		    throw new RuntimeException(_status.error_message().getString());
		}
		
		// restore
		Tensor fn = new Tensor(tensorflow.DT_STRING, new TensorShape(1));
//		StringArray a = fn.createStringArray();
//		a.position(0).put(filename + ".sd"); 
		_status = _session.Run(new StringTensorPairVector(new String[]{"save/Const:0"}, 
												   new Tensor[]{fn}), 
												   new StringVector(), 
												   new StringVector("save/restore_all"), 
												   new TensorVector());
		
		if (!_status.ok()) {
		   throw new RuntimeException(_status.error_message().getString());
		}
	}
	
	public double[] predict(SensorModel sensors){
		// try to predict for two (2) sets of inputs.
		Tensor inputs = new Tensor(
		         tensorflow.DT_FLOAT, new TensorShape(2,5));
		
		FloatBuffer x = inputs.createBuffer();
		x.put(new float[]{-6.0f,22.0f,383.0f,27.781754111198122f,-6.5f});
		x.put(new float[]{66.0f,22.0f,2422.0f,45.72160947712418f,0.4f});
		
		Tensor keepall = new Tensor(
		        tensorflow.DT_FLOAT, new TensorShape(2,1));
		
		((FloatBuffer)keepall.createBuffer()).put(new float[]{1f, 1f});
		
		TensorVector outputs = new TensorVector();
		// to predict each time, pass in values for placeholders
		outputs.resize(0);
		
		_status = _session.Run(new StringTensorPairVector(new String[] {"Placeholder", "Placeholder_2"}, new Tensor[] {inputs, keepall}),
		 new StringVector("Sigmoid"), new StringVector(), outputs);
		
		if (!_status.ok()) {
		   throw new RuntimeException(_status.error_message().getString());
		}
		
		// this is how you get back the predicted value from outputs
		FloatBuffer output = outputs.get(0).createBuffer();
		
		for (int k=0; k < output.limit(); ++k){
		   System.out.println("prediction=" + output.get(k));
		}
		
		return null;
	}
}