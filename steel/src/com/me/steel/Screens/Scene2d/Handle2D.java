package com.me.steel.Screens.Scene2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Utils.DebugBox;

public class Handle2D extends Image {

	private Vector2 position;
	private TextureAtlas textureAtlas;
	private Handle currentHandle;

	// not using a factory method because of the class simplicity
	public Handle2D(TextureAtlas textureAtlas) {
		super(new TextureRegion(textureAtlas.findRegion("handles/" + Handle.ROUGH_HANDLE.getSimpleName())));
		super.setTouchable(Touchable.disabled);
		super.setVisible(false);
		
		this.textureAtlas = textureAtlas;
		position = new Vector2();
	}
	
	public void setInitialPosition(float x, float y) {
		position.set(x, y);
		
		// update the actual position
		setX(position.x);
		setY(position.y);
	}
	
	public void replaceRegion(Handle handle) {
		if (super.isVisible() == false)
			super.setVisible(true);
		
		// handle to be sent to the next screen
		currentHandle = handle;
		
		// replace the current region with a specified one
		((TextureRegionDrawable)getDrawable()).setRegion(new TextureRegion(textureAtlas.findRegion("handles/" + handle.getSimpleName())));
	}
	
	public Handle getCurrentHandle() {
		return currentHandle;
	}
	
	public void flip(boolean flipX, boolean flipY) {
		((TextureRegionDrawable)getDrawable()).getRegion().flip(flipX, flipY);
	}
	
	public void drawDebug() {
		DebugBox.setColor(Color.RED);
		DebugBox.drawDebug(
				getX(), 
				getY(), 
				getWidth(), 
				getHeight(), 
				null);
	}
}
