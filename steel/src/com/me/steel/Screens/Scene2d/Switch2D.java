package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Switch2D extends Image {
	
	private Vector2 position;
	private TextureAtlas textureAtlas;
	
	// not using a factory method because of the class simplicity
	public Switch2D(TextureAtlas textureAtlas) {
		super(textureAtlas.findRegion("single-objects/switch-up"));
		position = new Vector2();
		this.textureAtlas = textureAtlas;
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
	
	public void triggerSwitch(boolean up) {
		((TextureRegionDrawable)getDrawable()).setRegion(
				textureAtlas.findRegion(up ? "single-objects/switch-up" : "single-objects/switch-down"));
	}
}