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

public class Cloud {
	private TweenManager manager;
	private Sprite sprite;
	private float cloudW;
	private float cloudH;
	
	// convenience variables
	private float screenW;
	private float screenH;
	
	public Cloud(TextureAtlas atlas) {
		manager = new TweenManager();
		Tween.call(windCallback).start(manager);
		Tween.call(flyCallback).start(manager);

		sprite = atlas.createSprite("jungle-objects/cloud");
		
		cloudW = sprite.getWidth() / 2;
		cloudH = sprite.getHeight() / 2;
		
		screenW = Gdx.graphics.getWidth();
		screenH = Gdx.graphics.getHeight();
	}
    
	public void update() {
		manager.update(Gdx.graphics.getDeltaTime());
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            float d = MathUtils.random() * 0.5f + 3.5f;	// duration
            float t = 0.5f * sprite.getHeight();   		// amplitude
            
            Tween.to(sprite, SpriteAccessor.SKEW_X2X3, d)
            .target(t, t).ease(Sine.INOUT)
            .repeatYoyo(1, 0)
            .setCallback(windCallback)
            .start(manager);
        }
    };
    
    private final TweenCallback flyCallback = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            float d = MathUtils.random() * 40 + 25;   	// duration
            float y = MathUtils.random() * 150 + 300;
            
            sprite.setSize(
    				cloudW * MathUtils.random() * 2 + cloudW,
    				cloudH * MathUtils.random() * 2 + cloudH);
    		
            sprite.setPosition(-sprite.getWidth() - MathUtils.random() * 100 - screenW / 2, y - screenH / 2);
            
            Tween.to(sprite, SpriteAccessor.POS_XY, d)
            .target(screenW, y - screenH / 2).setCallback(flyCallback)
            .start(manager);
        }
    };
    
    public void draw(SpriteBatch batch) {
    	sprite.draw(batch);
    }
}
