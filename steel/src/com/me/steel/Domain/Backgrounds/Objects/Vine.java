package com.me.steel.Domain.Backgrounds.Objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Sine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.me.steel.Utils.TweenAnimation.SpriteAccessor;

public class Vine {
	private TweenManager manager;
	private Sprite sprite;
	
	public Vine(TextureAtlas atlas) {
		manager = new TweenManager();
		Tween.call(windCallback).start(manager);
		
		sprite = atlas.createSprite("jungle-objects/vine");
		
		float h = sprite.getHeight() + MathUtils.random() * 150;

		sprite.setSize(h * sprite.getWidth() / sprite.getHeight(), h);
	}
	
	public void update() {
		manager.update(Gdx.graphics.getDeltaTime());
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            float d = MathUtils.random() * 0.5f + 4f;   // duration
            float t = -0.3f * sprite.getHeight();   	// amplitude
            
            Tween.to(sprite, SpriteAccessor.SKEW_X1X4, d)
            .target(t, t)
            .ease(Sine.INOUT)
            .repeatYoyo(1, 0)
            .setCallback(windCallback)
            .start(manager);
        }
    };
    
    public void setPosition(float x, float y) {
    	sprite.setPosition(x, y);
    }
    
    public float getHeight() {
    	return sprite.getHeight();
    }
    
    public float getWidth() {
    	return sprite.getWidth();
    }
    
    public float getX() {
    	return sprite.getX();
    }
    
    public float getY() {
    	return sprite.getY();
    }
    
    public void draw(SpriteBatch batch) {
    	sprite.draw(batch);
    }
}
