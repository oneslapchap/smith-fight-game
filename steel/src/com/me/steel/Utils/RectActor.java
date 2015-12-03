package com.me.steel.Utils;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class RectActor extends Actor {

	private ShapeRenderer shapeRenderer;
	
	public RectActor(ShapeRenderer shapeRenderer) {
		this.shapeRenderer = shapeRenderer;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		// need to make sure the batch is ended before doing
		// shapeRenderer.begin()
		batch.end();
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.rect(getParent().getX() + getX(), getParent().getY() + getY(), getWidth(), getHeight());
		shapeRenderer.end();
		
		// continue the batch after calling shapeRenderer.end()
		batch.begin();
	}
	
	public ShapeRenderer getShapeRenderer() {
		return shapeRenderer;
	}
}