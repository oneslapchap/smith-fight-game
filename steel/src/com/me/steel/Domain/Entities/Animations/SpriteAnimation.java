package com.me.steel.Domain.Entities.Animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

public class SpriteAnimation extends Sprite {
	
	private Animation animation;
	private float stateTime;
	
	public SpriteAnimation(Array<AtlasRegion> boilAnimationFrames) {
		super(boilAnimationFrames.get(0));
		
		animation = new Animation(0.15f, boilAnimationFrames);
	}
	
	public static SpriteAnimation create(TextureAtlas atlas, String searchFor) {
		Array<AtlasRegion> regions = atlas.
				findRegions(searchFor);
		
		return new SpriteAnimation(regions);
	}
	
	public void update(float delta) {
		setRegion(animation.getKeyFrame(stateTime += delta, true));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
	}
}
