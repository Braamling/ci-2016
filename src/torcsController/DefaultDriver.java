package torcsController;

import cicontest.algorithm.abstracts.AbstractDriver;
import cicontest.algorithm.abstracts.DriversUtils;
import cicontest.torcs.controller.extras.ABS;
import cicontest.torcs.controller.extras.AutomatedClutch;
import cicontest.torcs.controller.extras.AutomatedGearbox;
import cicontest.torcs.controller.extras.AutomatedRecovering;
import cicontest.torcs.genome.IGenome;
import models.TrainedModel;
import scr.Action;
import scr.SensorModel;
import training.CustomNeuralNetworkIris;
import utils.PredictionTools;
import java.util.Arrays;

public class DefaultDriver extends AbstractDriver {

    private CustomNeuralNetworkIris neuralNetwork;
    private double[] _output;
//    private double[] _prev_steering = new double[10];
    private int _generation = 0;
    private int _generationSize = 30;
    private int _individual = 0;
    private double _bestResult = Double.POSITIVE_INFINITY;
    private double[] _genResults = new double [_generationSize];

    public DefaultDriver() {
        initialize();
        LoadNewNN("./python/weights_nn1.json");

//        neuralNetwork = neuralNetwork.loadGenome();
    }

    private void initialize() {
        this.enableExtras(new AutomatedClutch());
        this.enableExtras(new AutomatedGearbox());
        this.enableExtras(new AutomatedRecovering());
        this.enableExtras(new ABS());
    }
    
    static int[] indexesOfMinElements(double[] orig, int nummin) {
        double[] copy = Arrays.copyOf(orig,orig.length);
        Arrays.sort(copy);
        double[] honey = Arrays.copyOfRange(copy, 0 , nummin);
        int[] result = new int[nummin];
        int resultPos = 0;
        for(int i = 0; i < orig.length; i++) {
            double onTrial = orig[i];
            int index = Arrays.binarySearch(honey,onTrial);
            if(index < 0) continue;
            result[resultPos++] = i;
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
    	String loadPath = "./resources/variations2/";
		String savePath = "./resources/variations/";
		if (_generation % 2 == 0){
    		loadPath = "./resources/variations/";
    		savePath = "./resources/variations2/";
		}
    	
    	for(int i=0; i<parentIndices.length; i+=2){
    		PredictionTools predictor1 = new PredictionTools(loadPath + "var_" 
    					+ Integer.toString(parentIndices[i]));
    		PredictionTools predictor2 = new PredictionTools(loadPath + "var_" 
					+ Integer.toString(parentIndices[i] + 1));
            TrainedModel trainedModel1 = predictor1.getModel();
            TrainedModel trainedModel2 = predictor2.getModel();
            
            //make two childs and store them in savePath
    	}
    	
    	
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
    	//System.out.println(sensors.getDistanceFromStartLine());
    	
    	if(sensors.getLaps() == 1 || sensors.getTime() >96) {
    		double lapTime = sensors.getTime();
    		_genResults[_individual] = lapTime;
    		
    		String loadPath = "./resources/variations2/";
    		String savePath = "./resources/variations/";
    		if (_generation % 2 == 0){
        		loadPath = "./resources/variations/";
        		savePath = "./resources/variations2/";
    		}
    		
    		System.out.println("The laptime of individual" + Integer.toString(_individual)+ "is " + Double.toString(lapTime));
    		neuralNetwork.storeJson("./resources/variations/var_" + Integer.toString(_individual) + ".json");
    		
    		
    		
    		if(lapTime < _bestResult){
    			neuralNetwork.storeJson(savePath + "best_i.json");
    		}
    		
    		_individual++;
    		if (_individual == _generationSize){
    			createNewGeneration(indexesOfMinElements(_genResults, 10));
    			_generation ++;
    			_individual = 0;
    		}
    		
    		LoadNewNN("./resources/variations/var_" + Integer.toString(_individual));
    		System.out.println("New individual start");
    		action.restartRace = true;
    	}
//    	
//        System.out.println("--------------" + getDriverName() + "--------------");
//        System.out.println("Steering: " + action.steering);
//        System.out.println("Acceleration: " + action.accelerate);
//        System.out.println("Brake: " + action.brake);
//        System.out.println("-----------------------------------------------");
    	
    	return action;
    }
}