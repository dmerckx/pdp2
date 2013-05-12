package contractnet;

import java.util.Iterator;

import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.TimeLapse;
import rinde.sim.core.graph.Point;
import shared.CommTruck;
import shared.Delivery;
import shared.MyMessage;
import shared.MyParcel;
import contractnet.messages.Accept;
import contractnet.messages.Auction;
import contractnet.messages.Bid;
import contractnet.messages.Reject;
import contractnet.messages.Winner;

public class ContractTruck extends CommTruck<State> {

	private MyParcel target;
	
	public ContractTruck(RandomGenerator rng, Point location, double speed, double radius) {
		super(rng, location, speed, State.SEARCHING, radius);
	}
	
	@Override
	public void tickImpl(TimeLapse time) {
		handleMail();
		drive(time);
	}
	
	private void handleMail(){
		Iterator<Delivery> messages = getMessages();
		
		while(messages.hasNext()){
			Delivery d = messages.next();
			MyMessage m = d.message;
			
			switch(getState()){
			case SEARCHING:
				if(m instanceof Auction){
					double bid = 1 / Point.distance(getPosition(), ((Auction) m).location);
					send(d.sender, new Bid(bid));
				}
				else if(m instanceof Winner){
					send(d.sender, new Accept());
					target = ((Winner) m).parcel;
					setTarget(target.getPosition());
					changeState(State.DRIVING_TO_PICKUP);
				}
				break;
			case DRIVING_TO_PICKUP:
			case DRIVING_TO_DELIVERY:
				if(m instanceof Winner){
					send(d.sender, new Reject());
				}
				break;
			}
			
			messages.remove();
		}
	}
	
	private void drive(TimeLapse time){
		advance(time);	//Drive as far as possible
		
		if(isDriving() || !time.hasTimeLeft())
			return;
		
		switch(getState()){
		case SEARCHING:
			setTarget(getRandomLocation());
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
