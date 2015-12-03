package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Chain2D extends Image {
	
	private Vector2 position;
	
	// not using a factory method because of the class simplicity
	public Chain2D(TextureAtlas textureAtlas) {
		super(textureAtlas.findRegion("single-objects/chain"));
		position = new Vector2();
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
}
