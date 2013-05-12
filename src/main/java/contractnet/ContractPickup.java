package contractnet;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import rinde.sim.core.TickListener;
import rinde.sim.core.TimeLapse;
import rinde.sim.core.graph.Point;
import rinde.sim.core.model.communication.CommunicationUser;
import rinde.sim.util.TimeWindow;
import shared.CommParcel;
import shared.Delivery;
import shared.MyMessage;

import com.google.common.collect.Maps;

import contractnet.messages.Accept;
import contractnet.messages.Auction;
import contractnet.messages.Bid;
import contractnet.messages.Reject;
import contractnet.messages.Winner;

public class ContractPickup extends CommParcel implements TickListener{

	public static final int REFRESH_RATE = 20;
	
	private Map<CommunicationUser, Double> bids = Maps.newLinkedHashMap();
	private State state;
	private int counter;
	
	private enum State{
		BEFORE_AUCTION,
		AUCTIONING,
		WAITING_FOR_WINNER,
		SOLD;
	}

	public ContractPickup(Point from, Point pDestination, long pPickupDuration,
			TimeWindow pickupTW, long pDeliveryDuration, TimeWindow deliveryTW,
			double radius) {
		super(from, pDestination, pPickupDuration, pickupTW, pDeliveryDuration,
				deliveryTW, radius);
		this.state = State.BEFORE_AUCTION;
	}

	public void tick(TimeLapse time) {
		handleMail();
		handleState();
	}
	
	private void handleMail() {
		Iterator<Delivery> messages = getMessages();
		
		while(messages.hasNext()){
			Delivery d = messages.next();
			MyMessage m = d.message;
			
			switch(state){
			case BEFORE_AUCTION:
				break;
			case AUCTIONING:
				if(m instanceof Bid){
					bids.put(d.sender, ((Bid) m).value);
				}
				break;
			case WAITING_FOR_WINNER:
				if(m instanceof Accept){
					bids.clear();
					changeState(State.SOLD);
				}
				else if(m instanceof Reject){
					bids.remove(d.sender);
					changeState(State.AUCTIONING);
				}
				break;
			case SOLD:
				break;
			}
			
			messages.remove();
		}
	}
	
	private void handleState() {
		counter++;
		
		switch(state){
		case BEFORE_AUCTION:
			broadcast(new Auction(getPosition()));
			changeState(State.AUCTIONING);
			break;
		case AUCTIONING:
			if(counter > REFRESH_RATE){
				if(bids.isEmpty()){
					changeState(State.BEFORE_AUCTION);
				}
				else{
					CommunicationUser winner = null;
					double highest = 0;
					
					for(Entry<CommunicationUser, Double> e:bids.entrySet()){
						if(highest < e.getValue())
							winner = e.getKey();
					}
					send(winner, new Winner(this));
					changeState(State.WAITING_FOR_WINNER);
				}
			}
			break;
		case WAITING_FOR_WINNER:
			break;
		case SOLD:
			break;
		}
	}
	
	private void changeState(State newState){
		this.state = newState;
		this.counter = 0;
	}

	public void afterTick(TimeLapse timeLapse) {}
}
