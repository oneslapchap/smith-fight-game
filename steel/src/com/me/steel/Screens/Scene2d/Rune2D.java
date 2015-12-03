package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.Enums.Rune;

public class Rune2D extends Image {

	private Vector2 position;
	private Vector2 velocity;
	private TextureAtlas textureAtlas;
	private Rune currentRune;

	// not using a factory method because of the class simplicity
	public Rune2D(TextureAtlas textureAtlas) {
		super(textureAtlas.findRegion("runes/" + Rune.ATTACK_POWER_RUNE.getSimpleName()));
		super.setTouchable(Touchable.disabled);
		super.setVisible(false);
		
		this.textureAtlas = textureAtlas;
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

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
	
	public void setVelocity(float x, float y) {
		velocity.set(x, y);
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);
		
		// update the actual position
		setX(position.x);
		setY(position.y);
	}
	
	public void replaceRegion(Rune rune) {
		if (super.isVisible() == false)
			super.setVisible(true);
		
		// rune to be sent to the next screen
		currentRune = rune;
		
		// replace the current region with a specified one
		((TextureRegionDrawable)getDrawable()).setRegion(textureAtlas.findRegion("runes/" + rune.getSimpleName()));
	}
	
	public Rune getCurrentRune() {
		return currentRune;
	}
}
