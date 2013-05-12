package shared;

import rinde.sim.core.model.pdp.Parcel;

public interface PdpObserver {

   void packagePickedUp();
   
   void packageDelivered(Parcel p);
   
}
