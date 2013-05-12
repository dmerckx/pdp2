package contractnet.messages;

import shared.MyMessage;

public class Bid extends MyMessage{

	public final double value;
	
	public Bid(double value){
		this.value = value;
	}
}
