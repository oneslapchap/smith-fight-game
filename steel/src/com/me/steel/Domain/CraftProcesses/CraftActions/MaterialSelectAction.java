package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Utils.DefaultActorListener;

public class MaterialSelectAction extends CraftAction {
	
	public MaterialSelectAction(final CraftableProcess process) {
		super(process.getStage());
		
		final CraftableWeapon weapon = process.getWeapon();
		
		float scale = 1.5f;
		
		Table table = new Table(process.getSkin());
		table.setFillParent(false);
		
		final ScrollPane pane = new ScrollPane(table, process.getSkin());
		pane.setScrollingDisabled(false, true);
		pane.setWidth(Gdx.graphics.getWidth());
		pane.setHeight(Gdx.graphics.getHeight() / 4);
		
		String prefix = "ingots/";
		
		for (final Material material : Material.values()) {
			TextureRegion handleRegion = process.getAtlas().findRegion(
					prefix + material.getSimpleName());

			Image image = new Image(handleRegion);
			image.addListener(new DefaultActorListener() {

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);

					// check if the pane is standing still
					if (pane.isPanning() == false) {				
						weapon.setMaterial(material);
						process.getSmeltTimer().setMeltPoint(material.getMeltPoint());
						startCompleteSequence();
					}
				}
			});

			table.add(image).size(image.getWidth() * scale, image.getHeight() * scale);
		}
		
		y = Gdx.graphics.getHeight() - pane.getHeight();
		group.addActor(pane);
	}
}
