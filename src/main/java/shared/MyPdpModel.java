package shared;

import rinde.sim.core.TimeLapse;
import rinde.sim.core.model.pdp.PDPModel;
import rinde.sim.core.model.pdp.PDPObject;
import rinde.sim.core.model.pdp.Parcel;
import rinde.sim.core.model.pdp.Vehicle;

public class MyPdpModel extends PDPModel{

	private final PdpObserver observer;
	
	public MyPdpModel(PdpObserver obs) {
		this.observer = obs;
	}
	
	@Override
	public void pickup(Vehicle vehicle, Parcel parcel, TimeLapse time) {
		super.pickup(vehicle, parcel, time);
		observer.packagePickedUp();
	}
	
	@Override
	public void deliver(Vehicle vehicle, Parcel parcel, TimeLapse time) {
		super.deliver(vehicle, parcel, time);
		observer.packageDelivered(parcel);
	}
	
	@Override
	public boolean unregister(PDPObject element) {
		if(element instanceof MyParcel){
			parcelState.remove(getParcelState((MyParcel) element), element);
			return true;
		}
		else return super.unregister(element);
	}
}
