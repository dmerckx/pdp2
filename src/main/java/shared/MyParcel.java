package shared;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.pdp.PDPModel;
import rinde.sim.core.model.pdp.Parcel;
import rinde.sim.core.model.road.RoadModel;
import rinde.sim.util.TimeWindow;

public class MyParcel extends Parcel{

	private final Point from;
	
	public MyParcel(Point from, Point pDestination, long pPickupDuration,
			TimeWindow pickupTW, long pDeliveryDuration, TimeWindow deliveryTW) {
		super(pDestination, pPickupDuration, pickupTW, pDeliveryDuration, deliveryTW, 1);
		this.from = from;
	}

	@Override
	public void initRoadPDP(RoadModel pRoadModel, PDPModel pPdpModel) {
		pRoadModel.addObjectAt(this, from);
	}
	
	public Point getPosition(){
		return from;
	}
}
