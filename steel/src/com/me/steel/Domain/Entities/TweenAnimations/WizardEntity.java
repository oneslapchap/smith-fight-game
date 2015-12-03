package com.me.steel.Domain.Entities.TweenAnimations;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.steel.Domain.Entities.Weapons.StaffEntity;

public class WizardEntity extends HeroEntity {
	
	public WizardEntity(String xmlPath, TextureAtlas textureAtlas, Skin skin,
			float red, float green, float blue) {
		super(xmlPath, textureAtlas, skin, red, green, blue);
		craftableWeapon = new StaffEntity();
	}
}
