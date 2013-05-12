package gradient;

import rinde.sim.core.graph.Point;
import rinde.sim.util.TimeWindow;

import comparison.Scenario;

public class GradientScenario extends Scenario{
	
	public static final int TRUCK_STRENGTH = -2;
	public static final int PARCEL_STRENGTH = 5;
	

	public GradientScenario(long seed, int speed, int ticks, int cars, int proportion) {
		super(seed, speed, ticks, cars, proportion);
	}
	
	@Override
	protected void registerModels() {
		GradientModel gm = new GradientModel(bounds);
		
		sim.register(gm);
	}
	
	@Override
	protected void registerTruck(Point pos, int speed) {
		sim.register(new FieldTruck(rng, pos, speed, TRUCK_STRENGTH));
	}
	
	@Override
	protected void registerParcel(Point from, Point to, long pickupDur,
			TimeWindow pickupTW, long deliveryDur, TimeWindow deliveryTW) {
		sim.register(new FieldParcel(from, to, pickupDur, pickupTW, deliveryDur, deliveryTW, PARCEL_STRENGTH));
	}
	
	

}
