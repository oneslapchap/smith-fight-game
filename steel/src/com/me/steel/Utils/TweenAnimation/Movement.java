package com.me.steel.Utils.TweenAnimation;

import java.util.List;

public class Movement {
	
	private String name;
	private List<BodyPart> bodyParts;
	
	public Movement(String name, List<BodyPart> bodyParts) {
		this.name = name;
		this.bodyParts = bodyParts;
	}
	
	public String getName() {
		return name;
	}
	
	public List<BodyPart> getBodyParts() {
		return bodyParts;
	}
}
