package com.me.steel.Utils;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Domain.Enums.Item;

/** A helper class for easier iteration trough the weapon parts */
public class WeaponPart {
	private Image partImage;
	private Item[] itemValues;
	private String prefix;
	
	public WeaponPart(Item[] itemValues, String prefix) {
		partImage = new Image();
		this.itemValues = itemValues;
		this.prefix = prefix;
	}

	public void setPartImage(Image partImage) {
		this.partImage.setDrawable(partImage.getDrawable());
		this.partImage.setSize(partImage.getWidth(), partImage.getHeight());
	}
	
	public Image getPartImage() {
		return partImage;
	}

	public Item[] getItemValues() {
		return itemValues;
	}

	public String getPrefix() {
		return prefix;
	}
}
