package torcsController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import cicontest.algorithm.abstracts.AbstractAlgorithm;
import cicontest.algorithm.abstracts.DriversUtils;
import cicontest.torcs.controller.Driver;
import models.GAModel;
import race.TorcsConfiguration;
import utils.PredictionTools;

public class DefaultDriverAlgorithm extends AbstractAlgorithm {

    private static final long serialVersionUID = 654963126362653L;

    DefaultDriverGenome[] drivers = new DefaultDriverGenome[1];
    private int [] results = new int[1];
    private GAModel _gaModel;
//    private DefaultRace _race;

    public Class<? extends Driver> getDriverClass(){
        return DefaultDriver.class;
    }

    public void run(boolean continue_from_checkpoint) {
        if(!continue_from_checkpoint){
        	_gaModel = new GAModel();
        	System.out.println(_gaModel.getIndividual());
        	//_gaModel.individualPlusPlus();
            //init NN
            DefaultDriverGenome genome = new  DefaultDriverGenome();
            drivers[0] = genome;
            
            DefaultRace race;
            //Start a race
            race = new DefaultRace();
            race.setTrack("aalborg" , "road");

            race.laps = 3;
            
            results = race.runRace(drivers, false);	
        }else{
        	_gaModel = new GAModel();
        	System.out.println(_gaModel.getIndividual());
        	//_gaModel.individualPlusPlus();
        	
        	DefaultDriverGenome genome = new  DefaultDriverGenome();
            drivers[0] = genome;
        	
            DefaultRace race;
            //Start a race
            race = new DefaultRace();
            race.setTrack("aalborg" , "road");

            race.laps = 3;
            
            while(true){
                results = race.runRace(drivers, false, _gaModel);	
                //DriversUtils.createCheckpoint(this);
                System.out.println(_gaModel.getIndividual());
            }           
        }
            // create a checkpoint this allows you to continue this run later
//            DriversUtils.createCheckpoint(this);
//            DriversUtils.clearCheckpoint();
    }

    public static void main(String[] args) {

        //Set path to torcs.properties
//        InputStream in = getClass().getResourceAsStream("/torcs.properties"); 
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    	
        TorcsConfiguration.getInstance().initialize(new File("torcs.properties"));
		/*
		 *
		 * Start without arguments to run the algorithm
		 * Start with -continue to continue a previous run
		 * Start with -show to show the best found
		 * Start with -show-race to show a race with 10 copies of the best found
		 * Start with -human to race against the best found
		 *
		 */
        DefaultDriverAlgorithm algorithm = new DefaultDriverAlgorithm();
        DriversUtils.registerMemory(algorithm.getDriverClass());
        if(args.length > 0 && args[0].equals("-show")){
            new DefaultRace().showBest();
        } else if(args.length > 0 && args[0].equals("-show-race")){
            new DefaultRace().showBestRace();
        } else if(args.length > 0 && args[0].equals("-human")){
            new DefaultRace().raceBest();
        } else if(args.length > 0 && args[0].equals("-variate")){
        	PredictionTools predict = new PredictionTools();
        	predict.createVariations();           
        } else if(args.length > 0 && args[0].equals("-generate")){
        	algorithm.run(true);
        } else if(args.length > 0 && args[0].equals("-continue")){
//            if(DriversUtils.hasCheckpoint()){
//                DriversUtils.loadCheckpoint().run(true);
//            } else {
//                algorithm.run(true);
//            }
//        } else {
            algorithm.run(true);
        }else{
            algorithm.run();
        }
    }

}