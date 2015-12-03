package com.me.steel.Domain.Entities.Weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.Weapon;
import com.me.steel.Screens.Scene2d.Handle2D;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Utils.DebugBox;
import com.me.steel.Utils.ParallaxCamera;

public class WeaponEntity {

	private Outline2D outline2d;
	private Handle2D handle2d;
	// a reference of an image of the body part in which the weapon is held (an arm for example)
	private Image pivot;
	private boolean isFlipped;
	private float scale;
	private float rotation;
	
	// convenience variables
	private float pivotW;
	private float handle2dW;
	private float outline2dW;
	
	private Group group;
	
	public WeaponEntity(Weapon weapon, TextureAtlas textureAtlas, Group pivotGroup, Image pivot) {
	
		this.pivot = pivot;
		rotation = -30;
		scale = 0.5f;
		
		// build up the weapon
		
		outline2d = new Outline2D(textureAtlas, weapon.getOutline(), false);
		outline2d.setColor(
				weapon.getMaterial().getRedColorValue(),
				weapon.getMaterial().getGreenColorValue(),
				weapon.getMaterial().getBlueColorValue(), 1f);
		outline2d.setSize(outline2d.getWidth() * scale, outline2d.getHeight() * scale);
		
		handle2d = new Handle2D(textureAtlas);
		handle2d.replaceRegion(weapon.getHandle());
		handle2d.setSize(handle2d.getWidth() * scale, handle2d.getHeight() * scale);
		
		// perform necessary actions if the holder of the weapon is flip'ed
		isFlipped = ((TextureRegionDrawable)pivot.getDrawable()).getRegion().isFlipX();
		if (isFlipped == true) {
	
			outline2d.flip(true, false);
			handle2d.flip(true, false);
			
			rotation = (-rotation);
		}
		
		// set up the convenience variables
		pivotW = pivot.getWidth();
		handle2dW = handle2d.getWidth();
		outline2dW = outline2d.getWidth();
		
		group = new Group();
		group.addActor(outline2d);
		group.addActor(handle2d);
		group.setWidth(outline2dW);
		group.setHeight(outline2d.getHeight());
		
		handle2d.setPosition(outline2d.getX() + outline2dW / 2 - handle2dW / 2, outline2d.getY());
	}
	
	public void update() {
		group.setPosition(pivot.getX() + pivotW - outline2dW, pivot.getY() + pivot.getRotation() / 2);
		group.setRotation(pivot.getRotation() + rotation);
	}
	
	public void addToGroup(Group group) {
		group.addActor(this.group);
		pivot.toFront();
	}
	
	public void drawDebug(Group group, ParallaxCamera camera) {
		DebugBox.setColor(Color.BLUE);
		DebugBox.drawDebug(
				group.getX() + this.group.getX(), 
				group.getY() + this.group.getY(), 
				this.group.getWidth(), 
				this.group.getHeight(), 
				camera);
	}
}
