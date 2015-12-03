package com.me.steel.Domain.Entities.Weapons;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Utils.WeaponPart;

public class SwordEntity extends CraftWeapon {
	
	public SwordEntity() {
		super();
		
		parts = new WeaponPart[1];
		parts[0] = new WeaponPart(Handle.values(), "handles/");
	}
	
	@Override
	public void update() {
		if (pivot == null) return;
		group.setPosition(pivot.getX() + pivotW - outlineW, pivot.getY() + pivot.getRotation() / 2);
		group.setRotation(pivot.getRotation() + rotation);
	}
	
	@Override
	public void buildWeapon() {		
		Image handle = parts[0].getPartImage();
		
		outline.setColor(
				material.getRedColorValue(),
				material.getGreenColorValue(),
				material.getBlueColorValue(), 1f);
		
		flip(parts[0].getPartImage(), false, true);
		parts[0].getPartImage().setPosition(
				outline.getX() + outline.getWidth() / 2 - handle.getWidth() / 2, 
				outline.getY());
		
		group.setHeight(outline.getHeight());
		group.setWidth(outline.getWidth());
		
		group.addActor(outline);
		group.addActor(handle);
	}
	
	@Override
	public void scaleToEntity(float entityHeight) {		
		float scale = entityHeight / 2 / group.getHeight();
		group.setScale(scale);
	}
}
