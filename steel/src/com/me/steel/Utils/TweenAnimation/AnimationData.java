package com.me.steel.Utils.TweenAnimation;

import java.util.List;

public class AnimationData {
	
	private String name;
	private List<Movement> movements;
	
	public AnimationData(String name, List<Movement> movements) {
		this.name = name;
		this.movements = movements;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Movement> getMovements() {
		return movements;
	}
}
