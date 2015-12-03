package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Utils.DebugBox;

public class Outline2D extends Image {

	private Vector2 position;

	// not using a factory method because of the class simplicity
	/**
	 * 
	 * @param textureAtlas
	 * @param wep
	 * @param withBackground
	 *            - whether to use specified weapon with a background or not
	 */
	public Outline2D(TextureAtlas textureAtlas, Outline wep,
			boolean withBackground) {
		super(new TextureRegion(textureAtlas.findRegion(((withBackground == true) ? "outlines/"
				: "weapons/")
				+ wep.getType()
				+ "-"
				+ "tier-"
				+ wep.getTierlevel() + "-" + wep.getSimpleName())));

		position = new Vector2();
	}

	public void setInitialPosition(float x, float y) {
		position.set(x, y);

		// update the actual position
		setX(position.x);
		setY(position.y);
	}
	
	public void flip(boolean flipX, boolean flipY) {
		((TextureRegionDrawable)getDrawable()).getRegion().flip(flipX, flipY);
	}
	
	public void drawDebug() {
		DebugBox.setColor(Color.GREEN);
		DebugBox.drawDebug(
				getX(), 
				getY(), 
				getWidth(), 
				getHeight(), 
				null);
	}
}
