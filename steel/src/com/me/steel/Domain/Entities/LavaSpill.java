package com.me.steel.Domain.Entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.me.steel.Domain.Entities.Animations.SpriteAnimation;
import com.me.steel.Utils.SpriteActor;
import com.me.steel.Utils.Timer;

public class LavaSpill {
	
	private SpriteActor lava;
	private float maxWidth;
	private float[] verts;
	private Timer timer;
	
	public LavaSpill(TextureAtlas atlas) {
		lava = new SpriteActor(SpriteAnimation.create(atlas, "smelt-pot-objects/lava-spill"));
		lava.getSprite().setSize(0, 0);
		lava.setVisible(false);
		verts = lava.getSprite().getVertices();
		timer = new Timer();
	}
	
	public void setMaxWidth(float maxWidth) {
		this.maxWidth = maxWidth;
	}
	
	public void setHeight(float height) {
		verts[SpriteBatch.Y2] = verts[SpriteBatch.Y1] + height;
		verts[SpriteBatch.Y3] = verts[SpriteBatch.Y2];
	}
	
	/** Normally should be called every loop */
	public void update() {
		if (timer.getTicks() >= 25) {
			float reduceBy = 1 * maxWidth / 100;
			verts[SpriteBatch.X4] -= reduceBy;
			verts[SpriteBatch.X3] = verts[SpriteBatch.X4];	
			
			timer.stop();
			if (verts[SpriteBatch.X4] > lava.getSprite().getX()) timer.start();
			else lava.setVisible(false);
		}
	}
	
	public void updateLava(float percentChange) {
		verts[SpriteBatch.X4] += percentChange * maxWidth / 100;
		verts[SpriteBatch.X3] = verts[SpriteBatch.X4];
		
		if (!timer.isStarted()) {
			timer.start();
			lava.setVisible(true);
		}
	}
	
	public SpriteActor getLavaActor() {
		return lava;
	}
	
	public void reset() {
		verts[SpriteBatch.X4] = verts[SpriteBatch.X1];
		verts[SpriteBatch.X3] = verts[SpriteBatch.X2];
	}
}
