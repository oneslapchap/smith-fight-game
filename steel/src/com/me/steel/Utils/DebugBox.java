package com.me.steel.Utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class DebugBox {
	
	private static ShapeRenderer shapeRenderer;
	
	public DebugBox() {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setColor(Color.RED);
	}
	
	public static void setColor(Color color) {
		shapeRenderer.setColor(color);
	}
	
	public static void drawDebug(float x, float y, float w, float h, ParallaxCamera camera) {
		if (camera != null)
			shapeRenderer.setProjectionMatrix(camera.calculateParallaxMatrix(1f, 1));
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.rect(x, y, w, h);
		shapeRenderer.end();
	}
	
	public void dispose() {
		shapeRenderer.dispose();
	}
}
