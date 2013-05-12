package gradient;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.road.RoadUser;

public interface FieldEmitter extends RoadUser{
	
	void setGradientModel(GradientModel model);
	
	/**
	 * Should always provide the same response during the same tick.
	 */
	boolean isActive();
	
	double getStrenght();
	
	Point getPosition();
}
