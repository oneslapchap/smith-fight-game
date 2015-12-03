package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Rune;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Screens.Scene2d.Rune2D;
import com.me.steel.Services.SoundManager.TyrianSound;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Stats;

public class EnchantScreen extends AbstractScreen {

	private Stats stats;
	private Outline wep;
	private Material material;
	private Handle handle;

	public EnchantScreen(Steel game, Stats stats, Outline wep, Material material,
			Handle handle) {
		super(game);
		
		this.stats = stats;
		this.wep = wep;
		this.material = material;
		this.handle = handle;
	}

	@Override
	public void show() {
		super.show();

		float runeScale = 5f;
		float scale = 3f;

		// set up outline2d
		final Outline2D outline2d = new Outline2D(getAtlas(), wep, false);
		outline2d.setSize(outline2d.getWidth() * scale, outline2d.getHeight()
				* scale);
		float outlineW = outline2d.getWidth();
		float outlineH = outline2d.getHeight();
		outline2d.setInitialPosition(MENU_VIEWPORT_WIDTH / 2 - outlineW / 2,
				-outlineH / 2);
		outline2d
				.setColor(material.getRedColorValue(),
						material.getGreenColorValue(),
						material.getBlueColorValue(), 1f);

		// set up rune2d
		final Rune2D rune2d = new Rune2D(getAtlas());
		rune2d.setSize(rune2d.getWidth() * scale, rune2d.getHeight() * scale);
		float runeW = rune2d.getWidth();
		rune2d.setInitialPosition(outline2d.getX() + outlineW / 2 - runeW / 2,
				outline2d.getY() + outlineH / 2);

		// get table from the super class
		Table table = getTable();
		table.setFillParent(false);

		// set up the next screen button
		final TextButton nextScreenButton = new TextButton("Continue",
				getSkin());
		nextScreenButton.setPosition(0,
				MENU_VIEWPORT_HEIGHT - nextScreenButton.getHeight());
		nextScreenButton.setTouchable(Touchable.disabled);
		nextScreenButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// add the stats
				stats.setAttackPower(stats.getAttackPower() + rune2d.getCurrentRune().getAttackPower());
				stats.setMagicPower(stats.getMagicPower() + rune2d.getCurrentRune().getMagicPower());
				stats.setDefensePower(stats.getDefensePower() + rune2d.getCurrentRune().getDefensePower());
				
				game.getSoundManager().play(TyrianSound.CLICK);
				game.setScreen(new FinishScreen(game, stats, wep, material, handle,
						rune2d.getCurrentRune()));
			}
		});

		// a scroll pane for the runes
		final ScrollPane pane = new ScrollPane(table, getSkin());

		String prefix = "runes/";
		float defaultRuneSize = 20;

		// adding all available runes as columns to the table
		for (final Rune rune : Rune.values()) {
			// find the current weapon region
			TextureRegion runeRegion = super.getAtlas().findRegion(
					prefix + rune.getSimpleName());

			Image image = new Image(runeRegion);
			image.addListener(new DefaultActorListener() {

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);

					// check if the pane is standing still
					if (pane.isPanning() == false) {
						// replace the rune2d drawable with the selected one
						rune2d.replaceRegion(rune);
						// allow the user to continue
						nextScreenButton.setTouchable(Touchable.enabled);
					}
				}
			});

			table.add(image).size(defaultRuneSize * runeScale);
			table.row();
		}

		// combine the table with a scroll pane
		pane.setScrollingDisabled(true, false);
		pane.setHeight(MENU_VIEWPORT_HEIGHT);
		pane.setPosition(MENU_VIEWPORT_WIDTH - pane.getWidth(), 0);

		// add actors to the stage
		stage.addActor(pane);
		stage.addActor(outline2d);
		stage.addActor(nextScreenButton);
		stage.addActor(rune2d);
	}
}
