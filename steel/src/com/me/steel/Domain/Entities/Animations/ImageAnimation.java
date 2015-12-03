package com.me.steel.Domain.Entities.Animations;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;

public class ImageAnimation extends Image {
	
	private Vector2 position;
	private final Animation animation;
	private float stateTime;

	/**
	 * The drawables of the boil animation. Here we cache the drawables or we
	 * would have to create them on demand (hence waking up the garbage
	 * collector).
	 */
	private Map<TextureRegion, Drawable> boilAnimationDrawables;

	/** Creates a new BoilingLava2D */
	private ImageAnimation(Array<AtlasRegion> boilAnimationFrames) {
		// the super constructor does a lot of work
		super(boilAnimationFrames.get(0));
		super.setTouchable(Touchable.disabled);

		this.position = new Vector2();
		this.animation = new Animation(0.15f, boilAnimationFrames);

		// create the drawable cache
		this.boilAnimationDrawables = new HashMap<TextureRegion, Drawable>();
		for (AtlasRegion region : boilAnimationFrames) {
			boilAnimationDrawables.put(region,
					new TextureRegionDrawable(region));
		}
	}

	public static ImageAnimation create(TextureAtlas textureAtlas, String searchFor) {
		Array<AtlasRegion> regions = textureAtlas
				.findRegions(searchFor);

		return new ImageAnimation(regions);
	}

	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		update(delta);
	}

	private void update(float delta) {
		TextureRegion frame = animation.getKeyFrame(stateTime += delta, true);

		// there is no performance issues when setting the same frame multiple
		// times as the current region (the call will be ignored in this case)
		setDrawable(boilAnimationDrawables.get(frame));
	}
}
