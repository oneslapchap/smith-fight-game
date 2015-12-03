package com.me.steel.Domain.Backgrounds.Objects;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.me.steel.Utils.TweenAnimation.SpriteAccessor;

public class Tree {
	private TweenManager manager;
	private Sprite sprite;
	private Random random;
	
	public Tree(TextureAtlas atlas) {
		manager = new TweenManager();
		random = new Random();
		sprite = atlas.createSprite("jungle-objects/tree");
		
		float mountainW = sprite.getWidth();
		float mountainH = sprite.getHeight();
		
		float w = MathUtils.random() *  100 + mountainW;
		float h = Gdx.graphics.getHeight()  + MathUtils.random() *  200;
		sprite.setSize(w, h);
		
		float t = MathUtils.random() *  mountainH / 2;
		if (random.nextBoolean() == true)
			t = -t;
		
		Tween.set(sprite, SpriteAccessor.SKEW_X2X3).target(t, t)
        .start(manager);
		
		manager.update(Gdx.graphics.getDeltaTime());
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void draw(SpriteBatch batch) {
    	sprite.draw(batch);
    }
	
	public float getX() {
		return sprite.getX();
	}
	
	public float getWidth() {
		return sprite.getWidth();
	}
}
