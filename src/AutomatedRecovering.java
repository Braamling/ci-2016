import cicontest.algorithm.abstracts.DriversUtils;
import cicontest.torcs.controller.extras.IExtra;
import scr.Action;
import scr.SensorModel;

public class AutomatedRecovering
        implements IExtra {
    private static final long serialVersionUID = -2521301170636027868L;
    final int stuckTime = 25;
    final float stuckAngle = 0.5235988F;
    final float steerLock = 0.785398F;

    private int stuck = 0;
    private int stuckstill = 0;

    public void process(Action action, SensorModel sensors) {
//    	System.out.println(sensors.getAngleToTrackAxis());
        if ((sensors.getSpeed() < 5.0D) && (sensors.getDistanceFromStartLine() > 0.0D)) {
            this.stuckstill += 1;
        }
        if (Math.abs(sensors.getAngleToTrackAxis()) > 0.5235987901687622D) {
            if ((this.stuck > 0) || (Math.abs(sensors.getTrackPosition()) > 1.0D)) {
                this.stuck += 1;

            }

        } else if ((this.stuck > 0) && (Math.abs(sensors.getAngleToTrackAxis()) < 0.3D)) {
            this.stuck = 0;
            this.stuckstill = 0;
        }

        if (this.stuckstill > 150) {
        	this.stuckstill = 0;
        }
        
        if (this.stuck < 1 && sensors.getSpeed() < 20.0D){
            float steer = (float) (sensors.getAngleToTrackAxis() / 0.785398006439209D);
            
    		action.accelerate = 1.0D;
    		action.brake = 0.0D;
    		action.gear = 1;
    		action.steering = steer;
        }
        
        if (this.stuck > 0 && this.stuckstill > 25 && this.stuckstill < 150) {
        	 safeDriver(action, sensors, 30.0);
        }

        if (this.stuck > 0 && this.stuckstill > 25 && this.stuckstill < 100) {
            float steer = -(float) (sensors.getAngleToTrackAxis() / 0.785398006439209D);
            
    		action.accelerate = 1.0D;
    		action.brake = 0.0D;
    		action.gear = -1;
    		action.steering = steer;
        }
    }
    
    public void safeDriver(Action action, SensorModel sensors, double target_speed) {
    	action.steering = DriversUtils.alignToTrackAxis(sensors, 0.5);
    	if (sensors.getSpeed() > target_speed - 10 ) {
    		action.accelerate = 0.0D;
    		action.brake = 0.0D;
    	}

    	if (sensors.getSpeed() > target_speed) {
    		action.accelerate = 0.0D;
    		action.brake = -1.0D;
    	}

    	if (sensors.getSpeed() <= target_speed - 10) {
    		action.accelerate = (80.0D - sensors.getSpeed()) / 80.0D;
    		action.brake = 0.0D;
    	}

    	if (sensors.getSpeed() < target_speed - 10) {
    		action.accelerate = 1.0D;
    		action.brake = 0.0D;
    	}
    }


    public void reset() {
        this.stuck = 0;
    }
}