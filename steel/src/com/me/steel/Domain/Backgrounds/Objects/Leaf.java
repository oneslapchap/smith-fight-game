package com.me.steel.Domain.Backgrounds.Objects;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
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

public class Leaf {
	private TweenManager manager;
	private Sprite sprite1;
	private Sprite sprite2;
	
	public Leaf(TextureAtlas atlas) {
		manager = new TweenManager();
		Tween.call(windCallback).start(manager);
		
		float w = MathUtils.random() * 100 + 100;
		
		sprite1 = atlas.createSprite("jungle-objects/leaf1");
		sprite1.setSize(w, w * sprite1.getHeight() / sprite1.getWidth());
		
		sprite2 = atlas.createSprite("jungle-objects/leaf2");
		sprite2.setSize(w, w * sprite2.getHeight() / sprite2.getWidth());
	}
	
	public void update() {
		manager.update(Gdx.graphics.getDeltaTime());
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            float d = MathUtils.random() * 0.5f + 1f;   // duration
            float t = -0.4f * sprite1.getHeight();   	// amplitude
            
            Timeline.createParallel()
            .push(Tween.to(sprite1, SpriteAccessor.SKEW_X1X4, d).target(t, t).ease(Sine.INOUT).repeatYoyo(1, 0).setCallback(windCallback))
            .push(Tween.to(sprite2, SpriteAccessor.SKEW_X1X4, d).target(t, t).ease(Sine.INOUT).delay(d/3).repeatYoyo(1, 0))
            .start(manager);
        }
    };
    
    public void setPosition(float x, float y) {
    	sprite1.setPosition(x, y);
    	sprite2.setPosition(x, y);
    }
    
    public void draw(SpriteBatch batch) {
    	sprite2.draw(batch);
    	sprite1.draw(batch);
    }
    
    public float getHeight() {
    	return sprite1.getHeight();
    }
    
    public float getWidth() {
    	return sprite1.getWidth();
    }
    
    public float getX() {
    	return sprite1.getX();
    }
    
    public float getY() {
    	return sprite1.getY();
    }
}
