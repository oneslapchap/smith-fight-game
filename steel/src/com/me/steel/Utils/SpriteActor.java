package com.me.steel.Utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.me.steel.Domain.Entities.Animations.SpriteAnimation;

public class SpriteActor extends Actor {
	
	private Sprite sprite;
	
	public SpriteActor() {
		sprite = new Sprite();
	}
	
	public SpriteActor(Sprite sprite) {
		this.sprite = sprite;
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		if (sprite instanceof SpriteAnimation)
			((SpriteAnimation)sprite).update(delta);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		sprite.draw(batch);
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void copyVerts(Sprite src) {
		float[] verts = sprite.getVertices();
		verts[SpriteBatch.X1] = src.getVertices()[SpriteBatch.X1];
		verts[SpriteBatch.X2] = src.getVertices()[SpriteBatch.X2];
		verts[SpriteBatch.X3] = src.getVertices()[SpriteBatch.X3];
		verts[SpriteBatch.X4] = src.getVertices()[SpriteBatch.X4];
		verts[SpriteBatch.Y1] = src.getVertices()[SpriteBatch.Y1];
		verts[SpriteBatch.Y2] = src.getVertices()[SpriteBatch.Y2];
		verts[SpriteBatch.Y3] = src.getVertices()[SpriteBatch.Y3];
		verts[SpriteBatch.Y4] = src.getVertices()[SpriteBatch.Y4];
	}
}
