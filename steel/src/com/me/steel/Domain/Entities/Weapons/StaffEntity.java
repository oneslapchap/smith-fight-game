package com.me.steel.Domain.Entities.Weapons;

import com.me.steel.Domain.Enums.StaffMiddle;
import com.me.steel.Domain.Enums.StaffTop;
import com.me.steel.Utils.WeaponPart;

public class StaffEntity extends CraftWeapon {
	
	public StaffEntity() {
		super();
		
		parts = new WeaponPart[2];
		parts[0] = new WeaponPart(StaffMiddle.values(), "staff-middles/");
		parts[1] = new WeaponPart(StaffTop.values(), "staff-tops/");
	}
	
	@Override
	public void update() {
		if (pivot == null) return;
		group.setPosition(pivot.getX() + pivotW - outlineW / 2, pivot.getY() - group.getHeight() / 2);
	}
	
	@Override
	public void scaleToEntity(float entityHeight) {
		float scale = entityHeight / group.getHeight();
		group.setScale(scale);
	}
}
