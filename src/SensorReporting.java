

import scr.SensorModel;

public class SensorReporting {
	public SensorReporting(SensorModel sensors){
		_sensors = sensors;
	}
	
	public void reportSensors(){
		System.out.println("Angle to Track axis: " +_sensors.getAngleToTrackAxis());
		System.out.println("Speed: " +_sensors.getSpeed());
		System.out.println("Wheel spin velocity: " +_sensors.getWheelSpinVelocity());
		System.out.println("Current time: " +_sensors.getTime());
		System.out.println("Track position: " +_sensors.getTrackPosition());
		System.out.println("Focus sensors: " +_sensors.getFocusSensors());
		System.out.println("Get opponent sensors: " +_sensors.getOpponentSensors());
		System.out.println("Track edge sensors: " +_sensors.getTrackEdgeSensors());
		reportTrackEdge();
		
	}
	
	private void reportTrackEdge(){
		for (int i = 0; i < _sensors.getTrackEdgeSensors().length; i++) {
			Double trackSensor = _sensors.getTrackEdgeSensors()[i];
			System.out.println("Track sensor " + i + ": " + trackSensor);
		}
	}
	
	private final SensorModel _sensors;
}
