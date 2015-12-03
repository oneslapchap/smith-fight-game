package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Stats;

public class MaterialSelectScreen extends AbstractScreen {

	private Outline wep;
	private Stats stats;
	
	public MaterialSelectScreen(Steel game, Stats stats, Outline wep) {
		super(game);
		this.wep = wep;
		this.stats = stats;
	}

	@Override
	public void show() {
		super.show();
		
		// scale textures by
		float scale = 1.5f;

		float defaultIngotSize = 100;
		
		Table table = getTable();
		table.setFillParent(false);
		
		// combine the table with a scroll pane
		final ScrollPane pane = new ScrollPane(table, getSkin());
		pane.setScrollingDisabled(false, true);
		pane.setWidth(MENU_VIEWPORT_WIDTH);
		pane.setHeight(defaultIngotSize * scale);
		pane.setPosition(MENU_VIEWPORT_WIDTH - pane.getWidth(), MENU_VIEWPORT_HEIGHT - pane.getHeight());

		String prefix = "ingots/";

		// adding all available weapons as columns to the table
		for (final Material material : Material.values()) {
			// find the current weapon region
			TextureRegion handleRegion = getAtlas().findRegion(
					prefix + material.getSimpleName());

			Image image = new Image(handleRegion);
			image.addListener(new DefaultActorListener() {

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);

					// check if the pane is standing still
					if (pane.isPanning() == false) {
						// add the stats
						stats.setAttackPower(stats.getAttackPower() + material.getAttackPower());
						stats.setMagicPower(stats.getMagicPower() + material.getMagicPower());
						stats.setDefensePower(stats.getDefensePower() + material.getDefensePower());
						
						game.setScreen(new SmeltScreen(game, stats, wep, material));			
					}
				}
			});

			table.add(image).size(image.getWidth() * scale, image.getHeight() * scale);
		}

		// add the actors to the stage
		stage.addActor(pane);
	}
}
