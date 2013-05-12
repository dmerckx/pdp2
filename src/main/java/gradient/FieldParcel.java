package gradient;

import rinde.sim.core.graph.Point;
import rinde.sim.util.TimeWindow;
import shared.MyParcel;

public class FieldParcel extends MyParcel implements FieldEmitter{
	
	private final double strength;
	
	public FieldParcel(Point from, Point pDestination, long pPickupDuration,
			TimeWindow pickupTW, long pDeliveryDuration, TimeWindow deliveryTW, double strength) {
		super(from, pDestination, pPickupDuration, pickupTW, pDeliveryDuration, deliveryTW);
		this.strength = strength;
	}

	public void setGradientModel(GradientModel model) {
		
	}

	public boolean isActive() {
		return true;
	}

	public double getStrenght() {
		return strength;
	}
}

