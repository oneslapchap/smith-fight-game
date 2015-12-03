package com.me.steel.Domain.Entities.Weapons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Utils.DebugBox;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.WeaponPart;

public abstract class CraftWeapon implements CraftableWeapon {
	
	protected Image outline;
	protected Image bgOutline;
	protected Material material;
	
	// a reference of an image of the body part in which the weapon is held (an arm for example)
	protected Image pivot;
	protected boolean isFlipped;
	protected float rotation;
	
	// convenience variables
	protected float pivotW;
	protected float outlineW;
	protected float outlineH;
	
	protected Group group;
	
	protected WeaponPart[] parts;
	
	public CraftWeapon() {
		outline = new Image();
		bgOutline = new Image();
		bgOutline.setSize(270, 360);
		group = new Group();
		
		rotation = -30;
	}
	
	@Override
	public void buildWeapon() {
		float groupH = outline.getHeight();
		
		outline.setColor(
				material.getRedColorValue(),
				material.getGreenColorValue(),
				material.getBlueColorValue(), 1f);
		
		Image partImage;
		float x, y;
		for (int i = 0; i < parts.length; i++) {
			partImage = parts[i].getPartImage();
			x = (outline.getX() + outline.getWidth() - partImage.getWidth()) / 2;
			
			if (i == 0) y = outline.getY() + outline.getHeight();
			else y = parts[i-1].getPartImage().getY() + parts[i-1].getPartImage().getHeight();
			
			partImage.setPosition(x, y);
			
			groupH += partImage.getHeight();
		}
		
		group.addActor(outline);
		for (WeaponPart element : parts) group.addActor(element.getPartImage());

		group.setSize(outline.getWidth(), groupH);
	}
	
	@Override
	public void addToGroup(Group group) {
		group.addActor(this.group);
	}
	
	protected void flip(Image image, boolean x, boolean y) {
		((TextureRegionDrawable)image.getDrawable()).getRegion().flip(x, y);
	}
	
	@Override
	public void setPivot(Image pivot) {
		this.pivot = pivot;
		pivotW = pivot.getWidth();
		isFlipped = ((TextureRegionDrawable)pivot.getDrawable()).getRegion().isFlipX();
		pivot.toFront();
	}
	
	@Override
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	@Override
	public void setOutline(Image outline) {
		this.outline.setDrawable(outline.getDrawable());
		outlineW = outline.getWidth();
		outlineH = outline.getHeight();
		this.outline.setSize(outlineW, outlineH);
	}
	
	@Override
	public void setBgOutline(Image bgOutline) {
		this.bgOutline.setDrawable(bgOutline.getDrawable());
	}
	
	@Override
	public void setMaterial(Material material) {
		this.material = material;
	}
	
	@Override
	public Image getOutline() {
		return outline;
	}
	
	@Override
	public Image getBgOutline() {
		return bgOutline;
	}
	
	@Override
	public Material getMaterial() {
		return material;
	}
	
	@Override
	public WeaponPart[] getParts() {
		return parts;
	}
	
	@Override
	public Group getGroup() {
		return group;
	}
	
	@Override
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
