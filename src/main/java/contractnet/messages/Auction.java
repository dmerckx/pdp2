package contractnet.messages;

import rinde.sim.core.graph.Point;
import shared.MyMessage;

public class Auction extends MyMessage{

	public final Point location;
	
	public Auction(Point location){
		this.location = location;
	}
	
	@Override
	public Auction clone() throws CloneNotSupportedException {
		return new Auction(location);
	}
}
