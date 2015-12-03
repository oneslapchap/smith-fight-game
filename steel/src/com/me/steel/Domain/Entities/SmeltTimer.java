package com.me.steel.Domain.Entities;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Utils.TweenAnimation.ImageAccessor;

public class SmeltTimer {
	
	private Image smeltTimer;
	private Image arrow;
	private Group group;
	private TweenManager manager;
	
	private boolean melted;
	private boolean overHeat;
	private boolean start;
	
	public SmeltTimer(TextureAtlas atlas) {
		float scale = 0.5f;
		
		String prefix = "smelt-pot-objects/";
		smeltTimer = new Image(atlas.findRegion(prefix + "smelt-timer"));
		smeltTimer.setSize(smeltTimer.getWidth() * scale, smeltTimer.getHeight() * scale);
		arrow = new Image(atlas.findRegion(prefix + "smelt-timer-arrow"));
		arrow.setSize(arrow.getWidth() * scale, arrow.getHeight() * scale);
		group = new Group();
		manager = new TweenManager();
		
		arrow.setPosition(
				smeltTimer.getWidth() / 2 - arrow.getWidth() / 2, 
				smeltTimer.getHeight() / 2);
		
		smeltTimer.setOrigin(smeltTimer.getWidth() / 2, smeltTimer.getHeight() / 2);
		arrow.setOrigin(arrow.getWidth() / 2, 0);

		group.addActor(smeltTimer);
		group.addActor(arrow);
		group.setSize(smeltTimer.getWidth(), smeltTimer.getHeight());
	}
	
	public void start() {
		arrow.setRotation(0);
		
		Tween.to(arrow, ImageAccessor.ROTATION, 20)
		.target(-360)
		.start(manager);
		
		start = true;
	}
	
	public void stop() {
		manager.killAll();
		melted = false;
		overHeat = false;
		start = false;
	}
	
	public void update(float delta) {		
		if (start) {
			manager.update(delta);
			
			if (!melted) {
				if (arrow.getRotation() <= smeltTimer.getRotation()) {
					melted = true;
				}
			}
			
			if (melted && !overHeat) {
				if (arrow.getRotation() <= smeltTimer.getRotation() - 45)
					overHeat = true;
			}
		}
	}
	
	public void setMeltPoint(int meltPoint) {
		smeltTimer.setRotation(meltPoint);
	}
	
	public void setRotation(float rotation) {
		arrow.setRotation(rotation);
	}
	
	public int getMeltPoint() {
		return (int)smeltTimer.getRotation();
	}
	
	public float getRotation() {
		return arrow.getRotation();
	}
	
	public Group getGroup() {
		return group;
	}
	
	public boolean isMelted() {
		return melted;
	}
	
	public boolean isStarted() {
		return start;
	}
}
