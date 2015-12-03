package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Flare2D extends Image {
	
	private Vector2 position;
	
	private Vector2 velocity;
	
	// not using a factory method because of the class simplicity
	public Flare2D(TextureAtlas textureAtlas) {
		super(textureAtlas.findRegion("single-objects/flare"));
		position = new Vector2();
		velocity = new Vector2();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		move(delta);
	}

	private void move(float delta) {
		position.add(velocity.x * delta, velocity.y * delta);
		
		setX(position.x);
		setY(position.y);
	}
	
	public void setVelocity(float x, float y) {
		velocity.x = x;
		velocity.y = y;
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
}
