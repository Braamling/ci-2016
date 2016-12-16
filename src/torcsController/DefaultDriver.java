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
import training.CustomNeuralNetwork;
import utils.PredictionTools;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
//import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;


public class DefaultDriver extends AbstractDriver {

    private CustomNeuralNetwork neuralNetwork;
    private double[] _output;
//    private double[] _prev_steering = new double[10];
    private GAModel _gaModel;
    
	//private String _loadPath = "/variations/";
	//private String _savePath = "./resources/variations2/";
	private double _speed; 
	private int _stepsAmount;
	private Double[] degrees = new Double[]{-90.0, -1.0, -45.0, -30.0, -25.0, -20.0, -15.0, -10.0, -5.0, 0.0, 5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 45.0, 1.0, 90.0};
	
	private boolean _done = false;

    public DefaultDriver(GAModel gaModel){
    	_gaModel = gaModel;
        initialize();
        _speed = 0;
        _gaModel.setPaths();
        String _loadPath = _gaModel.getLoadPath();
        System.out.println(_gaModel.getLoadPath());
		LoadNewNN(_loadPath + "var_" + Integer.toString(_gaModel.getIndividual()) + ".json");

    }
    
    public DefaultDriver() {
    	_gaModel = null;
        initialize();
        LoadNewNN("/weights_nn1.json");
        _speed = 0;
    }

    private void initialize() {
        this.enableExtras(new AutomatedClutch());
        this.enableExtras(new AutomatedGearbox());
        this.enableExtras(new AutomatedRecovering());
        this.enableExtras(new ABS());
        _speed = 0;
        _done = false;
        //_generationCounter = 0;
    }
    
    static int[] indexesOfMinElements(Double[] orig, int nummin) {
    	int[] result = new int[nummin];
    	List<Double> placeholder = Arrays.asList(orig);
    	
    	for(int i = 0; i < nummin; i++){
    		double min = Collections.min(placeholder);
    		int index = Arrays.asList(orig).indexOf(min);
    		
    		System.out.println(Double.toString(orig[index]));
    		System.out.println(Integer.toString(index));
    		
    		result[i] = index;
    		placeholder.set(index, Double.MAX_VALUE);
    	}
    	
    	return result;
    }
    
    private void LoadNewNN(String path ){
    	PredictionTools predictor = new PredictionTools(path);
        TrainedModel trainedModel = predictor.getModel();
        int[] hidden_layers = {100, 50, 20};
        neuralNetwork = new CustomNeuralNetwork(22, hidden_layers, 3, trainedModel);
    }
    
    private void createNewGeneration(int [] parentIndices){
    	String _loadPath = _gaModel.getLoadPath();
    	String _savePath = _gaModel.getSavePath();
    	for(int i=0; i <parentIndices.length; i++){    		
    		// Retrieve the two parents
    		if (i == parentIndices.length-1){
    			// i == 9 
    			PredictionTools predictor1 = new PredictionTools(_loadPath
    					+ "var_" + Integer.toString(parentIndices[i]) + ".json");
    			PredictionTools predictor2 = new PredictionTools(_loadPath
    					+ "var_" + Integer.toString(parentIndices[0]) + ".json");
    			//PredictionTools predictor3 = new PredictionTools(_loadPath
    			//		+ "var_" + Integer.toString(parentIndices[parentIndices.length-1])
    			//		+ ".json");
    			
    			TrainedModel trainedModel1 = predictor1.getModel();
                TrainedModel trainedModel2 = predictor2.getModel();
                //TrainedModel trainedModel3 = predictor3.getModel();
                
                // Breed two kids and store them
                BreedWeights breedWeights = new BreedWeights(trainedModel1, trainedModel2, 1, 5);
                breedWeights.getKids(1).storeJson(_savePath + "var_" 
    					+ Integer.toString(i) + ".json");
                breedWeights.getKids(2).storeJson(_savePath + "var_" 
    					+ Integer.toString(10 + i) + ".json");
                
                trainedModel1.storeJson(_savePath + "var_" 
    					+ Integer.toString(20 + i) + ".json");
                
    		} else {
    			PredictionTools predictor1 = new PredictionTools(_loadPath + "var_" 
    					+ Integer.toString(parentIndices[i]) + ".json");
    			PredictionTools predictor2 = new PredictionTools(_loadPath + "var_" 
					+ Integer.toString(parentIndices[i + 1]) + ".json");
    			
    			TrainedModel trainedModel1 = predictor1.getModel();
                TrainedModel trainedModel2 = predictor2.getModel();
                
                // Breed two kids and store them
                BreedWeights breedWeights = new BreedWeights(trainedModel1, trainedModel2, 1, 5);
                breedWeights.getKids(1).storeJson(_savePath + "var_" 
    					+ Integer.toString(i) + ".json");
                breedWeights.getKids(2).storeJson(_savePath + "var_" 
    					+ Integer.toString(10 + i) + ".json");
                
                trainedModel1.storeJson(_savePath + "var_" 
    					+ Integer.toString(20 + i) + ".json");
    		}
      	}
    	//updatePaths();
    	_gaModel.setPaths();
    	
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
    
    	
    	if(_gaModel != null){
        	checkGenerations(action, sensors);
    	}
    	_speed += sensors.getSpeed()/100;
    	_stepsAmount++;
    	if (_speed < sensors.getSpeed()){
    		_speed = sensors.getSpeed();
    	}
    	AutomatedRecovering recover = new AutomatedRecovering();
    	recover.process(action, sensors);
//    	
//        System.out.println("--------------" + getDriverName() + "--------------");
//        System.out.println("Steering: " + action.steering);
//        System.out.println("Acceleration: " + action.accelerate);
//        System.out.println("Brake: " + action.brake);
//        System.out.println("-----------------------------------------------");
    	
    	return action;
    }
    	
    
    
    
    
    /*
     * Use opponent sensors to find local optimum, the best possible 
     * race position, and try to overtake this. 
     * Also, if position is 1, try to block car in order to stay at 
     * local optimum.
     * 
     */
    public Action swarmControl(Action action, SensorModel sensors) {
    	if (action == null){
    		action = new Action();
    	}
    	int position = sensors.getRacePosition();
    	double[] opponents = sensors.getOpponentSensors();
    	int numOpSen = opponents.length;
    	int numOpSenFront = numOpSen/2;
    	
    	// go to local optimal position, try to get to global opt
    	if (position > 1){
    		boolean leftSide = false; 
    		boolean rightSide = false;
    		// check left
    		for (int i=0; i<numOpSenFront/2 -1; i++){
    			double dist = opponents[i];
    			if (dist < 100){
    				leftSide = true;
    			}
    		}
    		//check right
    		for (int i=numOpSenFront/2-1; i<numOpSenFront; i++){
    			double dist = opponents[i];
    			if (dist < 100){
    				rightSide = true;
    			}
    		}
    		double trackPos = sensors.getTrackPosition();
    		if(leftSide && !rightSide){
    			// if on the left side of the road
    			if(trackPos < 0){
    				action.steering = DriversUtils.alignToTrackAxis(sensors, 0.5);
    			}
    			// if rightside, keep location
    		} else if(rightSide && !leftSide){
    			// if on the right side of the road
    			if(trackPos > 0){
    				action.steering = DriversUtils.alignToTrackAxis(sensors, -0.5);
    			}
    		} else if(leftSide && rightSide){
    			// check middle
    			boolean middle = true;
    			for (int i=numOpSenFront/2- numOpSenFront/4; i<numOpSenFront/2+numOpSenFront/4; i++){
    				double dist = opponents[i];
    				if (dist>100){
    					middle = false;
    				}
    			}
    			if(!middle){
    				// steer to middle to pass
    				action.steering = DriversUtils.alignToTrackAxis(sensors, 0);
    			}
    		}
    	} else {
    		// if position is 1, and global max is achieved
    		boolean leftBack = false; 
    		boolean rightBack = false;
    		// check behind right
    		for (int i= numOpSenFront; i<numOpSenFront/2 + numOpSenFront -1; i++){
    			double dist = opponents[i];
    			if (dist < 100){
    				rightBack = true;
    			}
    		}
    		//check behind left
    		for (int i=numOpSenFront/2 + numOpSenFront; i<numOpSen; i++){
    			double dist = opponents[i];
    			if (dist < 100){
    				leftBack = true;
    			}
    		}
    		double trackPos = sensors.getTrackPosition();
    		if(leftBack && !rightBack){
    			// if on the left side of the road
    			if(trackPos > 0){
    				action.steering = DriversUtils.alignToTrackAxis(sensors, -0.5);
    			}
    		} else if(rightBack && !leftBack){
    			// if on the right side of the road
    			if(trackPos < 0){
    				action.steering = DriversUtils.alignToTrackAxis(sensors, 0.5);
    			}
    		} else if(leftBack && rightBack){
    			// check middle
    			boolean middle = true;
    			for (int i=numOpSenFront/2- numOpSenFront/4; i<numOpSenFront/2+numOpSenFront/4; i++){
    				double dist = opponents[i];
    				if (dist>100){
    					middle = false;
    				}
    			}
    			if(middle){
    				// steer to middle to block
    				action.steering = DriversUtils.alignToTrackAxis(sensors, 0);
    			}
    		}
    	}
    	getOutput(sensors);
    	action.accelerate = getAcceleration(sensors);
    	action.brake = getBreak(sensors);
    	
    	if(_gaModel != null){
        	checkGenerations(action, sensors);
    	}
    	_speed += sensors.getSpeed()/100;
    	_stepsAmount++;
    	if (_speed < sensors.getSpeed()){
    		_speed = sensors.getSpeed();
    	}
    	AutomatedRecovering recover = new AutomatedRecovering();
    	recover.process(action, sensors);
    	
    	return action;
    }
    
    /*
    private void updatePaths(){
    	String _loadPath;
    	String _savePath;
//    	 Where to load and store the variations
    	if (_loadPath == "./resources/variations/"){
    		_loadPath = "./resources/variations2/";
    		_savePath = "./resources/variations/";
    	} else {
    		_loadPath = "./resources/variations/";
    		_savePath = "./resources/variations2/";
    	}
    	
    }
    */
    

    /**
     * Check whether a new individual or generation should be tested.
     * 
     * @param action
     * @param sensors
     * @return
     */
    private Action checkGenerations(Action action, SensorModel sensors){
    	if((sensors.getLaps() == 1 || sensors.getTime() > 200) && _done == false){
    		_done = true;
    		double avgSpeed = (_speed / _stepsAmount)*100;
    		double performance = sensors.getTime() - avgSpeed/2 ;
    		//System.out.println("Time: " + Double.toString(sensors.getTime()));
    		System.out.println("avg_speed: " + Double.toString(avgSpeed) + " avg_speed/2: " + Double.toString((avgSpeed/2)));
        	_gaModel.setGenResult(_gaModel.getIndividual(), performance);
    		System.out.println(Arrays.toString(_gaModel.getGenResults()));
    		// Give the score
    		System.out.println("The performace of individual " + 
    						   Integer.toString(_gaModel.getIndividual()) + 
    						   " is " + Double.toString(performance));  		
    		
    		// Uber best just to store.
    		int generationCounter = _gaModel.getGenerations();
    		if( generationCounter == 1){
    			//System.out.println("I am cheking this");
    			if(performance < _gaModel.getBestResult()){
    				System.out.println("This car is the best so far and has performance: " + Double.toString(performance));
    				neuralNetwork.storeJson(_gaModel.getSavePath() + "best_i.json"); 
    				System.out.println("!!! I HAVE SAVED THE BEST NOW!!!!");
    				_gaModel.setBestResult(performance);
    			}
    		}
    			// check values in the list of all performances (of the previous run, so from the individual number to the end) and find worst 
    			//for (int i=_gaModel.getIndividual()+1; i<_gaModel.getGenResults().length; i++){
    			//	double currentVal = _gaModel.getGenResult(i);
    			//	if(currentVal > worstOfPop){
    			//		worstOfPop = currentVal;
    			//	}
    			//}
    			//System.out.println("Worst performance of population is: "+ Double.toString(worstOfPop));
    		
    			//if(performance < worstOfPop){
    			//	System.out.println("This variation is better than the worst previous results!");
    			//	neuralNetwork.storeJson(_savePath + "best_" + Integer.toString(_gaModel.getIndividual()) + ".json");
    			//	_gaModel.setBestResult(performance);
    			//}
    		//}
    		
    		//System.out.println("The generation is: " + Integer.toString( generationCounter));
    		// Generate 10 new children if all are tested  	
    		_gaModel.individualPlusPlus();
    		if (_gaModel.getIndividual() == _gaModel.getGenerationSize()){
    			// generate 20 new children based on 10 best.
    			createNewGeneration(indexesOfMinElements(_gaModel.getGenResults(), 10));
    			_gaModel.generationPlusPLus();
    			_gaModel.setIndividual(0);
    			System.out.println("Generation: " + Integer.toString(_gaModel.getGenerations()));
    		}
    		
    		
    		System.out.println("New individual start");
    		action.restartRace = true;
    	}
    	
    	return action;
    }
}