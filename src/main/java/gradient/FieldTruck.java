package gradient;

import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.TimeLapse;
import rinde.sim.core.graph.Point;
import shared.MyParcel;
import shared.Truck;

public class FieldTruck extends Truck<State> implements FieldEmitter{
	private final double strength;
	
	private GradientModel gradientAPI;
	private MyParcel target;

	public FieldTruck(RandomGenerator rng, Point location, double speed, double strength) {
		super(rng, location, speed, State.SEARCHING);
		this.strength = strength;
	}

	public double getStrenght() {
		return strength;
	}
	
	public void setGradientModel(GradientModel model) {
		this.gradientAPI = model;
	}

	public boolean isActive() {
		return true;
	}

	@Override
	protected void tickImpl(TimeLapse time) {
		switch(getState()){
		case SEARCHING:
			target = findClosestAvailableParcel();

			if(target != null && Point.distance(target.getPosition(), getPosition()) < 3 * getSpeed()){
				//drive to the most nearby package
				setTarget(target.getPosition());
				advance(time);

				if(!isDriving() && time.hasTimeLeft()){
					if(tryPickup(time, target)){
						setTarget(target.getDestination());
						changeState(State.DRIVING_TO_DELIVERY);
					}
				}
			}
			else{
				//let the field guide the way
				Point target = gradientAPI.getTargetFor(this, getSpeed());
				if(target == null) throw new IllegalStateException();
				
				setTarget(target);
				advance(time);
			}
			break;
		case DRIVING_TO_DELIVERY:
			advance(time);

			if(!isDriving() && time.hasTimeLeft()){
				tryDelivery(time, target);
				changeState(State.SEARCHING);
			}
			break;
		}
	}
}

enum State{
	SEARCHING,
	DRIVING_TO_DELIVERY;
}