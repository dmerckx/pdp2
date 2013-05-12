package shared;

import rinde.sim.core.model.communication.CommunicationUser;
import rinde.sim.core.model.communication.Message;

public class Delivery extends Message{
	
	public final CommunicationUser sender;
	public final MyMessage message;
	
	public Delivery(CommunicationUser sender, MyMessage msg) {
		super(sender);
		this.sender = sender;
		this.message = msg;
	}
	
	@Override
	public Message clone() throws CloneNotSupportedException {
		return new Delivery(sender, message.clone());
	}
}
