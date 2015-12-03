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

public class Mountain {
	
	private TweenManager manager;
	private Sprite sprite;
	private Random random;
	
	public Mountain(TextureAtlas atlas) {
		manager = new TweenManager();
		random = new Random();
		sprite = atlas.createSprite("jungle-objects/mountain");
		
		float mountainW = getSprite().getWidth();
		float mountainH = getSprite().getHeight();
		
		float w = MathUtils.random() *  100 + mountainW;
		float h = MathUtils.random() * 100 + mountainH;
		getSprite().setSize(w, h);
		
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
}
