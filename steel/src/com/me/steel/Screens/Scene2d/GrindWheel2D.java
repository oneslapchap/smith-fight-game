package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GrindWheel2D extends Image {

	private boolean spinWheel;
	private float spinSpeed;
	
	// not using a factory method because of the class simplicity
	public GrindWheel2D(TextureAtlas textureAtlas) {
		super(textureAtlas.findRegion("single-objects/grind-wheel"));
		super.setTouchable(Touchable.disabled);

		// whether to spin the wheel
		spinWheel = false;
		spinSpeed = 1000;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
		if (spinWheel == true)
			moveWheel(delta);
	}

	private void moveWheel(float delta) {
		setRotation(getRotation() + spinSpeed * delta);
	}
	
	public void setSpinning(boolean spin) {
		spinWheel = spin;
	}
}
