package com.me.steel.Utils.TweenAnimation;

public class Frame {

	private float x;
	private float y;
	
	// Skew (rotation)
	private float kX;
	private float kY;
	
	// Pivot
	private float pX;
	private float pY;
	
	// duration of the frame
	private float dr;
	
	// pivot's distance to 0, 0 coords
	private float distanceToX;
	private float distanceToY;
	
	public Frame(float x, float y, float kX, float kY,
			float pX, float pY, float dr) {
		this.x = x;
		this.y = y;
		this.kX = kX;
		this.kY = kY;
		this.pX = pX;
		this.pY = pY;
		this.dr = dr;
	}
	
	public float getPivotX() {
		return pX;
	}
	
	public float getPivotY() {
		return pY;
	}
	
	public float getRotation() {
		return kX;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float getWorldX() {
		return x - pX + distanceToX;
	}
	
	public float getWorldY() {
		return y - pY + distanceToY;
	}
	
	public float getDuration() {
		return dr;
	}
	
	public float getDistanceToX() {
		return distanceToX;
	}
	
	public float getDistanceToY() {
		return distanceToY;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public void setDistanceToX(float distanceToX) {
		this.distanceToX = distanceToX;
	}
	
	public void setDistanceToY(float distanceToY) {
		this.distanceToY = distanceToY;
	}
	
	public void setRotation(float kX) {
		this.kX = kX;
	}
	
	public void setPivotX(float pX) {
		this.pX = pX;
	}
	
	public void setPivotY(float pY) {
		this.pY = pY;
	}

}