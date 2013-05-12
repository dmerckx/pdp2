package shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.communication.CommunicationAPI;
import rinde.sim.core.model.communication.CommunicationUser;
import rinde.sim.core.model.communication.Message;
import rinde.sim.util.TimeWindow;

public class CommParcel extends MyParcel implements CommunicationUser{

	private final double radius;
	private final List<Delivery> messages = new ArrayList<Delivery>();
	
	private CommunicationAPI commAPI;
	
	public CommParcel(Point from, Point pDestination, long pPickupDuration,
			TimeWindow pickupTW, long pDeliveryDuration, TimeWindow deliveryTW, double radius) {
		super(from, pDestination, pPickupDuration, pickupTW, pDeliveryDuration, deliveryTW);
		this.radius = radius;
	}

	public void setCommunicationAPI(CommunicationAPI api) {
		this.commAPI = api;
	}

	public double getRadius() {
		return radius;
	}

	public double getReliability() {
		return 1.0d; 
	}

	public void receive(Message message) {
		messages.add((Delivery) message);
	}
	
	protected Iterator<Delivery> getMessages(){
		return messages.iterator();
	}
	
	protected void send(CommunicationUser recipient, MyMessage msg){
		commAPI.send(recipient, new Delivery(this, msg));
	}
	
	protected void broadcast(MyMessage msg){
		commAPI.broadcast(new Delivery(this, msg));
	}
}
