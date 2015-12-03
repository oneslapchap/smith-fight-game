package com.me.steel.Utils.TweenAnimation;

import java.util.List;

public class BodyPart {
	
	private String name;
	private List<Frame> frames;
	
	public BodyPart(String name, List<Frame> frames) {
		this.name = name;
		this.frames = frames;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Frame> getFrames() {
		return frames;
	}
}
