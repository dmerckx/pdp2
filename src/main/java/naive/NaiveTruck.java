package naive;

import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.TimeLapse;
import rinde.sim.core.graph.Point;
import shared.MyParcel;
import shared.Truck;

public class NaiveTruck extends Truck<State>{
	
	MyParcel target;
	
	public NaiveTruck(RandomGenerator rng, Point location, double speed) {
		super(rng, location, speed, State.SEARCHING);
	}
	
	@Override
	protected void tickImpl(TimeLapse time) {
		advance(time);	//Drive as far as possible
		
		if(isDriving() || !time.hasTimeLeft())
			return;
		
		switch(getState()){
		case SEARCHING:
			target = findClosestAvailableParcel();
			if(target != null){
				setTarget(target.getPosition());
				changeState(State.DRIVING_TO_PICKUP);
			}
			else{
				setTarget(getRandomLocation());
			}
			break;
		case DRIVING_TO_PICKUP:
			if(tryPickup(time, target)){	
				setTarget(target.getDestination());
				changeState(State.DRIVING_TO_DELIVERY);
			}
			else{
				changeState(State.SEARCHING);
			}
			break;
		case DRIVING_TO_DELIVERY:
			tryDelivery(time, target);
			changeState(State.SEARCHING);
			break;
		}
	}
}

enum State{
	SEARCHING,
	DRIVING_TO_PICKUP,
	DRIVING_TO_DELIVERY;
}