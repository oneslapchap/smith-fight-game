package com.me.steel.Domain.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Utils.SpriteActor;

public class SmeltPot {
	
	private Image hatch;
	private SpriteActor pot;
	private SmeltTimer smeltTimer;
	private Group hatchGroup;
	private Group group;
	
	// convenience variables
	private Vector2 origHatchPos;
	
	public SmeltPot(TextureAtlas atlas, SmeltTimer smeltTimer) {
		float scale = 2;
		
		hatch = new Image(atlas.findRegion("smelt-pot-objects/pot-hatch"));
		hatch.setSize(hatch.getWidth() * scale, hatch.getHeight() * scale);
		
		pot = new SpriteActor(new Sprite(atlas.findRegion("smelt-pot-objects/pot")));
		pot.getSprite().setSize(pot.getSprite().getWidth() * scale, pot.getSprite().getHeight() * scale);
		pot.getSprite().setOrigin(pot.getSprite().getWidth() / 2, pot.getSprite().getHeight() / 2);
		
		this.smeltTimer = smeltTimer;
		hatchGroup = new Group();
		group = new Group();
		
		hatchGroup.addActor(hatch);
		hatchGroup.addActor(smeltTimer.getGroup());

		smeltTimer.getGroup().setPosition(
				hatch.getWidth() / 2 - smeltTimer.getGroup().getWidth() / 2, 
				-smeltTimer.getGroup().getHeight() / 2);
		
		hatchGroup.setPosition(
				pot.getSprite().getWidth() / 2 - hatch.getWidth() / 2,
				pot.getSprite().getHeight());
		
		group.addActor(pot);
		group.addActor(hatchGroup);
		group.setSize(hatch.getWidth(), pot.getSprite().getHeight() + hatch.getHeight());
		group.setVisible(false);
		
		origHatchPos = new Vector2();
		origHatchPos.x = hatchGroup.getX();
		origHatchPos.y = hatchGroup.getY();
	}

	public void update(float delta) {
		smeltTimer.update(delta);
	}
	
	public Group getGroup() {
		return group;
	}
	
	public Sprite getPot() {
		return pot.getSprite();
	}
	
	public SmeltTimer getSmeltTimer() {
		return smeltTimer;
	}
	
	public Group getHatch() {
		return hatchGroup;
	}
	
	public void restart() {
		hatchGroup.setPosition(
				origHatchPos.x, 
				origHatchPos.y);
	}
}
