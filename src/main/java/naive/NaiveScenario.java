package naive;

import rinde.sim.core.graph.Point;
import rinde.sim.util.TimeWindow;
import shared.MyParcel;
import comparison.Scenario;

public class NaiveScenario extends Scenario{

	public NaiveScenario(long seed, int speed, int ticks, int cars, int proportion) {
		super(seed, speed, ticks, cars, proportion);
	}

	@Override
	protected void registerModels() {}

	@Override
	protected void registerTruck(Point pos, int speed) {
		sim.register(new NaiveTruck(rng, pos, speed));
	}

	@Override
	protected void registerParcel(Point from, Point to, long pickupDur,
			TimeWindow pickupTW, long deliveryDur, TimeWindow deliveryTW) {
		sim.register(new MyParcel(from, to, pickupDur, pickupTW, deliveryDur, deliveryTW));
	}

}
