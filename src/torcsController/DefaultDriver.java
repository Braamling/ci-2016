package torcsController;

import cicontest.algorithm.abstracts.AbstractDriver;
import cicontest.algorithm.abstracts.DriversUtils;
import cicontest.torcs.controller.extras.ABS;
import cicontest.torcs.controller.extras.AutomatedClutch;
import cicontest.torcs.controller.extras.AutomatedGearbox;
import cicontest.torcs.controller.extras.AutomatedRecovering;
import cicontest.torcs.genome.IGenome;
import models.GAModel;
import models.TrainedModel;
import scr.Action;
import scr.SensorModel;
import training.BreedWeights;
import training.CustomNeuralNetworkIris;
import utils.PredictionTools;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class DefaultDriver extends AbstractDriver {

    private CustomNeuralNetworkIris neuralNetwork;
    private double[] _output;
//    private double[] _prev_steering = new double[10];
    private GAModel _gaModel;
    
	private String _loadPath = "./resources/variations/";
	private String _savePath = "./resources/variations2/";
	
	private boolean _done = false;

    public DefaultDriver(GAModel gaModel){
    	_gaModel = gaModel;
        initialize();
       
		LoadNewNN(_loadPath + "var_" + Integer.toString(_gaModel.getIndividual()) + ".json");

    }
    
    public DefaultDriver() {
    	_gaModel = new GAModel();
        initialize();
        LoadNewNN("./python/weights_nn1.json");
    }

    private void initialize() {
        this.enableExtras(new AutomatedClutch());
        this.enableExtras(new AutomatedGearbox());
        this.enableExtras(new AutomatedRecovering());
        this.enableExtras(new ABS());
    }
    
    static int[] indexesOfMinElements(Double[] orig, int nummin) {
    	int[] result = new int[nummin];
    	
    	for(int i = 0; i < nummin; i++){
    		List<Double> placeholder = Arrays.asList(orig);
    		double min = Collections.min(placeholder);
    		int index = Arrays.asList(orig).indexOf(min);
    		
    		result[i] = index;
    		orig[i] = Double.MAX_VALUE;
    	}
    	
    	return result;
    }
    
    private void LoadNewNN(String path ){
    	PredictionTools predictor = new PredictionTools(path);
        TrainedModel trainedModel = predictor.getModel();
        int[] hidden_layers = {100, 50, 20};
        neuralNetwork = new CustomNeuralNetworkIris(22, hidden_layers, 3, trainedModel);
    }
    
    private void createNewGeneration(int [] parentIndices){
    	for(int i=0; i < parentIndices.length; i++){
    		
    		// Retrieve the two parents
    		PredictionTools predictor1 = new PredictionTools(_loadPath + "var_" 
    					+ Integer.toString(parentIndices[i]) + ".json");
    		PredictionTools predictor2 = new PredictionTools(_loadPath + "var_" 
					+ Integer.toString(parentIndices[i] + 1) + ".json");
            TrainedModel trainedModel1 = predictor1.getModel();
            TrainedModel trainedModel2 = predictor2.getModel();
            
            // Breed two kids and store them
            BreedWeights breedWeights = new BreedWeights(trainedModel1, trainedModel2, 2);
            breedWeights.getKids(1).storeJson(_savePath + "var_" 
					+ Integer.toString(i) + ".json");
            breedWeights.getKids(2).storeJson(_savePath + "var_" 
					+ Integer.toString(10 + i) + ".json");
            
            trainedModel1.storeJson(_savePath + "var_" 
					+ Integer.toString(20 + i) + ".json");
    	}
    	updatePaths();
    	
    }

    @Override
    public void loadGenome(IGenome genome) {
        if (genome instanceof DefaultDriverGenome) {
            DefaultDriverGenome myGenome = (DefaultDriverGenome) genome;
        } else {
            System.err.println("Invalid Genome assigned");
        }
    }
    
    public void getOutput(SensorModel sensors){
        _output = neuralNetwork.getOutput(sensors);

    }

    @Override
    public double getAcceleration(SensorModel sensors) {
//    	if(_output[0] > _output[1]){
//    		return _output[1] + _output[0];
//    	}
//    	return 0.0;
    	return _output[0];
    }
    
    private double getPercentageOffTrack(SensorModel sensors){
    	int offTrackSensors = 0;
    	for( int i = 0; i < 19; i++){
    		if(sensors.getTrackEdgeSensors()[i] == -1){
    			offTrackSensors++;
    		}
    	}
    	
    	return (double)offTrackSensors / 19.0;
    }
    
    @Override
    public double getSteering(SensorModel sensors) {
//    	Double steering = 0.0;
//    	for (int i = 0; i < 9; i++){
//    		steering += _prev_steering[i] * (i + 1);
//    		_prev_steering[i] = _prev_steering[i + 1];
//    	}
//    	
//    	steering += _output[2] * 10;
//    	_prev_steering[9] = _output[2];
//    	
//    	steering = steering/ (10 + 55);
//
//        return steering;
    	
    	double percentageOffTrack = getPercentageOffTrack(sensors);
    	
    	if (percentageOffTrack > .5){
    		_output[0] = 0.0;
    		_output[1] = 0.0;
    		_output[2] = DriversUtils.alignToTrackAxis(sensors, 0.5);

    	}
    	
    	return _output[2];
    }

    public double getBreak(SensorModel sensors) {
//    	if(_output[1] > _output[0]){
//    		return _output[1] + _output[0];
//    	}
//    	return 0.0;
        return _output[1];
    }
    
    @Override
    public String getDriverName() {
        return "Habris";
    }

    @Override
    public Action controlWarmUp(SensorModel sensors) {
        Action action = new Action();
        return defaultControl(action, sensors);
    }

    @Override
    public Action controlQualification(SensorModel sensors) {
        Action action = new Action();
        return defaultControl(action, sensors);
    }

    @Override
    public Action controlRace(SensorModel sensors) {
        Action action = new Action();
        return defaultControl(action, sensors);
    }

    //@Override
    public Action defaultControl2(Action action, SensorModel sensors) {
        if (action == null) {
            action = new Action();
        }
        
        SensorReporting reporter = new SensorReporting(sensors);
       	reporter.reportSensors();
        
        System.out.println(sensors.toString());
        action.steering = DriversUtils.alignToTrackAxis(sensors, 0.5);
        
        action.accelerate = 10.0D;
        action.brake = 0.0D;
        
        System.out.println("--------------" + getDriverName() + "--------------");
        System.out.println("Steering: " + action.steering);
        System.out.println("Acceleration: " + action.accelerate);
        System.out.println("Brake: " + action.brake);
        System.out.println("-----------------------------------------------");
        return action;
    }
    
    @Override
    public Action defaultControl(Action action, SensorModel sensors) {
    	if (action == null) {
            action = new Action();
        }
    	
    	getOutput(sensors);
    	action.steering = getSteering(sensors);
    	action.accelerate = getAcceleration(sensors);
    	action.brake = getBreak(sensors);
    	
    	checkGenerations(action, sensors);
    	
//    	
//        System.out.println("--------------" + getDriverName() + "--------------");
//        System.out.println("Steering: " + action.steering);
//        System.out.println("Acceleration: " + action.accelerate);
//        System.out.println("Brake: " + action.brake);
//        System.out.println("-----------------------------------------------");
    	
    	return action;
    }
    
    private void updatePaths(){
//    	 Where to load and store the variations
    	if (_loadPath == "./resources/variations/"){
    		_loadPath = "./resources/variations2/";
    		_savePath = "./resources/variations/";
    	} else {
    		_loadPath = "./resources/variations/";
    		_savePath = "./resources/variations2/";
    	}
    	
    }
    

    
    private Action checkGenerations(Action action, SensorModel sensors){
    	if(sensors.getLaps() == 1 || sensors.getTime() >96 && _done == false){
    		double lastLapTime = sensors.getTime();
        	_gaModel.setGenResult(_gaModel.getIndividual(), lastLapTime);
    		System.out.println(Arrays.toString(_gaModel.getGenResults()));
    		// Give the score
    		System.out.println("The laptime of individual " + 
    						   Integer.toString(_gaModel.getIndividual()) + 
    						   " is " + Double.toString(lastLapTime));
    		
    		// Store the trained model
    		// neuralNetwork.storeJson(_savePath + "var_" + Integer.toString(_gaModel.getIndividual()) + ".json");    		
    		
    		// Uber best just to store.
    		if(lastLapTime < _gaModel.getBestResult()){
    			neuralNetwork.storeJson(_savePath + "best_i.json");
    			_gaModel.setBestResult(lastLapTime);
    		}
    		
    		// Generate 10 new children if all are tested  	
    		_gaModel.individualPlusPlus();
    		if (_gaModel.getIndividual() == _gaModel.getGenerationSize()){
    			// generate 30 new children based on 10 best.
    			createNewGeneration(indexesOfMinElements(_gaModel.getGenResults(), 10));
    			_gaModel.generationPlusPLus();
    			_gaModel.setIndividual(0);
    		}
    		
    		
    		System.out.println("New individual start");
    		action.restartRace = true;
    		_done = true;
    	}
    	
    	return action;
    }
}