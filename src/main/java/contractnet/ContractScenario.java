package contractnet;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.communication.CommunicationModel;
import rinde.sim.util.TimeWindow;
import comparison.Scenario;

public class ContractScenario extends Scenario{

	private final double radius;
	
	public ContractScenario(long seed, int speed, int ticks, int cars, int proportion, double radius) {
		super(seed, speed, ticks, cars, proportion);
		this.radius = radius;
	}

	@Override
	protected void registerModels(){
		CommunicationModel cm = new CommunicationModel(rng);
		
		sim.register(cm);
	}

	@Override
	protected void registerTruck(Point pos, int speed) {
		sim.register(new ContractTruck(rng, pos, speed, radius));
	}

	@Override
	protected void registerParcel(Point from, Point to, long pickupDur,
			TimeWindow pickupTW, long deliveryDur, TimeWindow deliveryTW) {
		sim.register(new ContractPickup(from, to, pickupDur, pickupTW, deliveryDur, deliveryTW, radius));
	}
	
}
