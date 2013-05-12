package comparison;


import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.Simulator;
import rinde.sim.core.graph.Point;
import rinde.sim.core.model.pdp.PDPModel;
import rinde.sim.core.model.pdp.Parcel;
import rinde.sim.core.model.road.PlaneRoadModel;
import rinde.sim.core.model.road.RoadModel;
import rinde.sim.ui.View;
import rinde.sim.ui.renderers.PDPModelRenderer;
import rinde.sim.ui.renderers.PlaneRoadModelRenderer;
import rinde.sim.util.TimeWindow;
import shared.MyPdpModel;
import shared.PdpObserver;
import util.Rectangle;
import util.Result;

public abstract class Scenario implements PdpObserver{
	
	private static final int STEP = 1;

	protected final Simulator sim;
	protected final RandomGenerator rng;
	
	private final int ticks;
	
	private final int speed;
	private final int nrTrucks;
	private final int proportion;
	
	protected RoadModel roadModel;
	
	private double interactions = 0;
	
	private int pickups = 0;
	private int deliveries = 0;
	
	protected Rectangle bounds;
	
	public Scenario(long seed, int speed, int ticks, int cars, int proportion){
		this.rng = new MersenneTwister(seed);
		
		this.sim = new Simulator(rng, STEP);
		this.ticks = ticks;
		this.speed = speed;
		this.nrTrucks = cars;
		this.proportion = proportion;
	}
	
	public Result run(){
		while(sim.getCurrentTime() < ticks * STEP){
			sim.tick();
		}
		//System.out.println("speed: " + speed + "  interactions: " + ((interactions / ticks) / nrTrucks));
		return new Result(pickups, deliveries, (interactions / ticks) / nrTrucks);
	}
	
	public void runGUI(){
		View.startGui(sim, 10, new PlaneRoadModelRenderer(), new PDPModelRenderer());	
	}
	
	public void packagePickedUp() {
		interactions++;
		pickups++;
	}

	public void packageDelivered(Parcel p) {
		sim.unregister(p);
		interactions++;
		deliveries++;
		addParcel();
	}

	public void init() {
		double z = Math.max(100, Math.sqrt(nrTrucks) * 50);
		roadModel = new PlaneRoadModel(new Point(0, 0), new Point(z, z), false, 100);
		bounds = new Rectangle(0, z, 0, z);
		PDPModel pdpModel = new MyPdpModel(this);
		
		sim.register(roadModel);
		sim.register(pdpModel);
		
		registerModels();
		
		sim.configure();
		

		for (int i = 0; i < nrTrucks; i++) {
			addTruck();
		}

		for (int i = 0; i < nrTrucks * proportion; i++) {
			addParcel();
		}
	}
	
	private void addTruck(){
		registerTruck(roadModel.getRandomPosition(rng), speed);
	}
	
	private void addParcel(){
		Point from = roadModel.getRandomPosition(rng);
		
		int xMin = (int) Math.max(bounds.xMin, from.x - 40) + 1;
		int xMax = (int) Math.min(bounds.xMax, from.x + 40) - 1;
		
		int yMin = (int) Math.max(bounds.yMin, from.y - 40) + 1;
		int yMax = (int) Math.min(bounds.yMax, from.y + 40) - 1;
		
		//System.out.println(xMin + " " + xMax + "," + yMin + " " + yMax);
		
		int x = rng.nextInt(xMax - xMin) + xMin;
		int y = rng.nextInt(yMax - yMin) + yMin;
		
		Point to = new Point(x, y);
		//System.out.println("rect: " + rect);
		//System.out.println("x " + x + " y " + y);
	
		registerParcel(from, to, STEP*2, TimeWindow.ALWAYS, STEP*2, TimeWindow.ALWAYS);
	}
	
	abstract protected void registerModels();
	abstract protected void registerTruck(Point pos, int speed);
	abstract protected void registerParcel(Point from, Point to, long pickupDur,
            TimeWindow pickupTW, long deliveryDur, TimeWindow deliveryTW);
}
