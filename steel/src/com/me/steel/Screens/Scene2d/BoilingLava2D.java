package com.me.steel.Screens.Scene2d;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.me.steel.Utils.SpriteActor;

public class BoilingLava2D {

	private final Animation boilAnimation;

	private float boilAnimationStateTime;
	
	private SpriteActor actor;

	/**
	 * The drawables of the boil animation. Here we cache the drawables or we
	 * would have to create them on demand (hence waking up the garbage
	 * collector).
	 */
	private Map<TextureRegion, Drawable> boilAnimationDrawables;

	/** Creates a new BoilingLava2D */
	private BoilingLava2D(Array<AtlasRegion> boilAnimationFrames) {
		
		this.boilAnimation = new Animation(0.15f, boilAnimationFrames);

		// create the tilt animation drawable cache
		this.boilAnimationDrawables = new HashMap<TextureRegion, Drawable>();
		for (AtlasRegion region : boilAnimationFrames) {
			boilAnimationDrawables.put(region,
					new TextureRegionDrawable(region));
		}
		
		//actor = new SpriteActor(this);
	}

	/** Factory method to create a BoilingLava2D. */
	public static BoilingLava2D create(TextureAtlas textureAtlas) {
		// load all the regions of lava in the image atlas
		Array<AtlasRegion> regions = textureAtlas
				.findRegions("single-animations/lava");

		// create boiling lava
		return new BoilingLava2D(regions);
	}

	public TextureRegion getCurrentFrame(float delta) {
		return boilAnimation.getKeyFrame(boilAnimationStateTime += delta, true);
	}
	
	public SpriteActor getActor() {
		return actor;
	}
}