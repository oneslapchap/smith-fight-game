package com.me.steel.Utils;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParticleEffectActor extends Actor {
	
	private ParticleEffect effect;
	private Vector2 position;
	
	public ParticleEffectActor(ParticleEffect effect) {
		this.effect = effect;
		position = new Vector2();
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		effect.draw(batch);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		effect.update(delta);
		effect.setPosition(position.x, position.y);
	}

	public ParticleEffect getEffect() {
		return effect;
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
}