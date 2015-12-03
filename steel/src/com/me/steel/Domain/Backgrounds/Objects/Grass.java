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

public class Grass {

	private TweenManager manager;
	private Sprite sprite1;
	private Sprite sprite2;
	private Sprite sprite3;
	
	public Grass(TextureAtlas atlas) {
		manager = new TweenManager();
		Tween.call(windCallback).start(manager);
		
		String prefix = "backgrounds/objects/";
		float w = MathUtils.random() * 100 + 200;
		
		sprite1 = atlas.createSprite(prefix + "grass1");
		sprite1.setSize(w, w * sprite1.getHeight() / sprite1.getWidth());
		sprite1.setPosition(100, 100);
		
		sprite2 = atlas.createSprite(prefix + "grass2");
		sprite2.setSize(w, w * sprite2.getHeight() / sprite2.getWidth());
		sprite2.setPosition(100, 100);
		
		sprite3 = atlas.createSprite(prefix + "grass3");
		sprite3.setSize(w, w * sprite3.getHeight() / sprite3.getWidth());
		sprite3.setPosition(100, 100);
	}
	
	public void update() {
		manager.update(Gdx.graphics.getDeltaTime());
	}
	
	private final TweenCallback windCallback = new TweenCallback() {
        @Override
        public void onEvent(int type, BaseTween<?> source) {
            float d = MathUtils.random() * 0.5f + 0.5f; // duration
            float t = -0.5f * sprite1.getHeight();   	// amplitude
            
            Timeline.createParallel()
            .push(Tween.to(sprite1, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).repeatYoyo(1, 0).setCallback(windCallback))
            .push(Tween.to(sprite2, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d/3).repeatYoyo(1, 0))
            .push(Tween.to(sprite3, SpriteAccessor.SKEW_X2X3, d).target(t, t).ease(Sine.INOUT).delay(d/3*2).repeatYoyo(1, 0))
            .start(manager);
        }
    };
    
    public void draw(SpriteBatch batch) {
    	sprite3.draw(batch);
    	sprite2.draw(batch);
    	sprite1.draw(batch);
    }
}
