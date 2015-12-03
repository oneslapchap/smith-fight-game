package com.me.steel.Domain.Entities.Weapons;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.WeaponPart;

public interface CraftableWeapon {
	/** 
	 * Should be called before the update method.
	 * Every part of the weapon should be set before calling this function.
	 */
	public void buildWeapon();
	public void update();
	public void addToGroup(Group group);
	public void setPivot(Image pivot);
	public void setRotation(float rotation);
	/** Scales the weapon depending on its type and the wielder's height.
	 * It is assumed that the weapon is added to the entities group before this function is called.
	 * @param entityHeight - the height of the fightableEntity
	 */
	public void scaleToEntity(float entityHeight);
	public void setOutline(Image outline);
	public void setBgOutline(Image bgOutline);
	public void setMaterial(Material material);
	public Image getOutline();
	public Image getBgOutline();
	public Material getMaterial();
	public WeaponPart[] getParts();
	public Group getGroup();
	public void drawDebug(Group group, ParallaxCamera camera);
}
