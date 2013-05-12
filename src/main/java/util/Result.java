package util;

public class Result{
	public final int pickups;
	public final int deliveries;
	public final double interactionRate;
	
	public Result(int pickups, int deliveries, double interactionRate){
		this.pickups = pickups;
		this.deliveries = deliveries;
		this.interactionRate = interactionRate;
	}
}