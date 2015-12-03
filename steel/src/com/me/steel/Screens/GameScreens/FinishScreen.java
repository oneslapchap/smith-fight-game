package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Domain.Profile;
import com.me.steel.Domain.Weapon;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Domain.Enums.Rune;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Screens.MenuScreen;
import com.me.steel.Screens.Scene2d.Handle2D;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Screens.Scene2d.Rune2D;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Stats;

public class FinishScreen extends AbstractScreen {

	private Profile profile;
	private Weapon weapon;
	
	private Stats stats;
	private Outline outline;
	private Material material;
	private Handle handle;
	private Rune rune;

	public FinishScreen(Steel game, Stats stats, Outline outline, Material material,
			Handle handle, Rune rune) {
		super(game);
		
		this.stats = stats;
		this.outline = outline;
		this.material = material;
		this.handle = handle;
		this.rune = rune;
	}

	@Override
	public void show() {
		super.show();

		profile = game.getProfileManager().retrieveProfile();
		weapon = profile.getWeapon();
		
		// set up outline2d
		Outline2D outline2d = new Outline2D(getAtlas(), outline, false);
		float outlineW = outline2d.getWidth();
		float outlineH = outline2d.getHeight();
		outline2d.setInitialPosition(MENU_VIEWPORT_WIDTH / 2 - outlineW / 2, 0);
		outline2d
				.setColor(material.getRedColorValue(),
						material.getGreenColorValue(),
						material.getBlueColorValue(), 1f);

		// set up handle2d
		Handle2D handle2d = new Handle2D(getAtlas());
		handle2d.replaceRegion(handle);
		handle2d.setInitialPosition(
				outline2d.getX() + outlineW / 2 - handle2d.getWidth() / 2,
				outline2d.getY());

		// set up rune2d
		Rune2D rune2d = new Rune2D(getAtlas());
		rune2d.replaceRegion(rune);
		rune2d.setInitialPosition(
				outline2d.getX() + outlineW / 2 - rune2d.getWidth() / 2,
				outline2d.getY() + outlineH / 2);

		// set up the scrap button
		TextButton scrapButton = new TextButton("Scrap", getSkin());
		scrapButton.setY(MENU_VIEWPORT_HEIGHT - scrapButton.getHeight());
		scrapButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.setScreen(new MenuScreen(game));
			}
		});

		// set up the create button
		TextButton createButton = new TextButton("Create", getSkin());
		createButton.setPosition(MENU_VIEWPORT_WIDTH - createButton.getWidth(),
				MENU_VIEWPORT_HEIGHT - createButton.getHeight());
		createButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				// save the weapon
				weapon.install(material);
				weapon.install(outline);
				weapon.install(handle);
				weapon.install(rune);
				weapon.setStats(stats);
				
				game.setScreen(new MenuScreen(game));
			}
		});

		// set up labels
		Label attackLabel = new Label("Attack power: " + Float.toString(stats.getAttackPower()), getSkin());
		Label magicLabel = new Label("Magic power: " + Float.toString(stats.getMagicPower()), getSkin());
		Label defenseLabel = new Label("Defense power: " + Float.toString(stats.getDefensePower()), getSkin());
		
		// set up a table for the labels
		Table statsTable = new Table();
		statsTable.setPosition(100, MENU_VIEWPORT_HEIGHT - statsTable.getHeight() - 100);
		
		statsTable.add(attackLabel).row();
		statsTable.add(magicLabel).row();
		statsTable.add(defenseLabel);
		
		// add the actors to the scene
		stage.addActor(outline2d);
		stage.addActor(handle2d);
		stage.addActor(rune2d);
		stage.addActor(scrapButton);
		stage.addActor(createButton);
		stage.addActor(statsTable);
	}
}
