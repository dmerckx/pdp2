package gradient;

import java.util.Set;

import rinde.sim.core.graph.Point;
import rinde.sim.core.model.Model;
import util.Rectangle;

import com.google.common.collect.Sets;

public class GradientModel implements Model<FieldEmitter>{

	private Set<FieldEmitter> emitters = Sets.newLinkedHashSet();
	private Rectangle bounds;
	
	public GradientModel(Rectangle bounds){
		this.bounds = bounds;
	}
	
	/**
	 * Possibilities
	 * (-1,1)	(0,1)	(1,1)
	 * (-1,0)			(1,0
	 * (-1,-1)	(0,-1)	(1,-1)
	 */
	private final int[] x = {	-1,	0, 	1, 	1, 	1, 	0, 	-1,	-1	};
	private final int[] y = {	1,	1,	1,	0,	-1,	-1,	-1,	0	};
	
	
	public Point getTargetFor(FieldEmitter fe, double dist){
		double maxField = Double.NEGATIVE_INFINITY;
		Point maxFieldPoint = null;
		Point pos = fe.getPosition();
		
		for(int i = 0;i < x.length;i++){
			Point p = new Point(pos.x + dist * x[i], pos.y + dist * y[i]);
			
			if( p.x < bounds.xMin || p.x > bounds.xMax || p.y < bounds.yMin || p.y > bounds.yMax){
				continue;
			}
			
			double field = getField(p, fe);
			
			if(field >= maxField){
				maxField = field;
				maxFieldPoint = p;
			}
		}
		
		return maxFieldPoint;
	}
	
	public double getField(Point in, FieldEmitter fe){
		double field = 0.0f;
		
		for(FieldEmitter fe2:emitters){
			if(fe2 == fe) continue;
			if(!fe2.isActive()) continue;
			
			field += fe2.getStrenght() / Point.distance(in, fe2.getPosition());
		}
		return field;
	}
	
	public Class<FieldEmitter> getSupportedType() {
		return FieldEmitter.class;
	}

	public boolean register(FieldEmitter user) {
		emitters.add(user);
		user.setGradientModel(this);
		
		return true;
	}

	public boolean unregister(FieldEmitter user) {
		return emitters.remove(user);
	}
}
