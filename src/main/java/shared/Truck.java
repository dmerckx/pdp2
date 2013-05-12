package shared;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.math3.random.RandomGenerator;

import rinde.sim.core.TimeLapse;
import rinde.sim.core.graph.Point;
import rinde.sim.core.model.pdp.PDPModel;
import rinde.sim.core.model.pdp.PDPModel.ParcelState;
import rinde.sim.core.model.pdp.Parcel;
import rinde.sim.core.model.pdp.Vehicle;
import rinde.sim.core.model.road.RoadModel;

public abstract class Truck<S extends Enum<?>> extends Vehicle{

	private final double speed;
	private final RandomGenerator rng;
	private final Point startLocation;
	
	private S state;
	private Queue<Point> path;
	
	private RoadModel roadModel;
	private PDPModel pdpModel;
	
	public Truck(RandomGenerator rng, Point location, double speed, S initialState) {
		this.rng = rng;
		this.speed = speed;
		this.startLocation = location;
		this.state = initialState;
		this.path = new LinkedList<Point>();
		
		setCapacity(1);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	protected void advance(TimeLapse time){
		if(! isDriving() || ! time.hasTimeLeft()) return;
		roadModel.followPath(this, path, time);
	}
	
	protected boolean isDriving(){
		return !path.isEmpty();	
	}
	
	protected void setTarget(Point target){
		path.clear();
		path.addAll(roadModel.getShortestPathTo(this, target));
	}
	
	protected Point getRandomLocation(){
		return roadModel.getRandomPosition(rng);
	}
	
	protected MyParcel findClosestAvailableParcel(){
		MyParcel closest = null;
		double dist = Double.POSITIVE_INFINITY;
		
		Point location = getPosition();
		
		for(Parcel p:pdpModel.getAvailableParcels()){
			if(Point.distance(location, ((MyParcel) p).getPosition()) < dist){
				dist = Point.distance(location, ((MyParcel) p).getPosition());
				closest = (MyParcel) p;
			}
		}
		
		return closest;
	}
	
	protected boolean tryPickup(TimeLapse time, Parcel p){
		if(pdpModel.getParcelState(p) != ParcelState.AVAILABLE) return false;
		pdpModel.pickup(this, p, time);
		return true;
	}
	
	protected void tryDelivery(TimeLapse time, Parcel p){
		pdpModel.deliver(this, p, time);
	}
	
	protected void changeState(S newState){
		this.state = newState;
	}
	
	protected S getState(){
		return state;
	}

	public Point getPosition(){
		return roadModel.getPosition(this);
	}
	
	@Override
	public void initRoadPDP(RoadModel pRoadModel, PDPModel pPdpModel) {
		this.roadModel = pRoadModel;
		this.pdpModel = pPdpModel;
		
		roadModel.addObjectAt(this, startLocation);
	}

	
}
