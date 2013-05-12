package comparison;

import contractnet.ContractScenario;
import gradient.GradientScenario;
import util.Result;
import naive.NaiveScenario;

public class Comparison {
	
	public static final double BROADCAST_RADIUS = 100;
	
	public static void main(String[] args) {
		int seed = 18;
		int speed = 1;
		int ticks = 500;
		int cars = 3;
		int proportion = 5;
		
		Scenario s = makeScenario(2, seed, speed, ticks, cars, proportion);
		s.init();
		s.runGUI();
		Result r = s.run();
		
		System.out.println("RESULT: " + r.deliveries + " " + r.pickups + " -> " + r.interactionRate);
	}
	
	public static final int NR_POLICIES = 1;
	
	private static Scenario makeScenario(int nr, int seed, int speed, int ticks, int cars, int proportion){
		switch (nr) {
		case 0:
			return new NaiveScenario(seed, speed, ticks, cars, proportion);
		case 1:
			return new GradientScenario(seed, speed, ticks, cars, proportion);
		case 2:
			return new ContractScenario(seed, speed, ticks, cars, proportion, BROADCAST_RADIUS);
		default:
			throw new IllegalArgumentException();
		}
	}
}
