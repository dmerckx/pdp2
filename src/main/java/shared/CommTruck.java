package shared;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.communication.CommunicationAPI;
import rinde.sim.core.model.communication.CommunicationUser;
import rinde.sim.core.model.communication.Message;

public abstract class CommTruck<S extends Enum<?>> extends Truck<S> implements CommunicationUser{

	private final double radius;
	private final List<Delivery> messages = new ArrayList<Delivery>();
	
	private CommunicationAPI commAPI;
	
	public CommTruck(RandomGenerator rng, Point location, double speed, S initialState, double radius) {
		super(rng, location, speed, initialState);
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
