package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Stats;

public class OutlineScreen extends AbstractScreen {

	private String type;
	private String tierLevel;

	private int wepsInTier;
	private float[][] statsValues;
	private int wepH;
	private int wepW;
	private int statIconSize;
	private Table table;
	
	public OutlineScreen(Steel game, String type, String tierLevel) {
		super(game);
		this.type = type;
		this.tierLevel = tierLevel;
	}

	@Override
	public void show() {
		super.show();
		
		// keep track of stats
		final Stats stats = new Stats();
		
		// some in scope finals for convenience
		final int attackPowerI = 0;
		final int magicPowerI = 1;
		final int defensePowerI = 2;
		
		// setup a table
		table = new Table();
		table.debug();
		table.setFillParent(false);

		// default weapon height and width
		wepH = 360;
		wepW = 270;
		// default stat's icon's size
		statIconSize = 25;

		int statsCount = 3;
		// default weapons in tier count
		wepsInTier = 4;
		statsValues = new float[wepsInTier][statsCount];
		
		final ScrollPane pane = new ScrollPane(table);
		pane.setScrollingDisabled(false, true);
		pane.setHeight(wepH + statIconSize * 3);
		pane.setWidth(Gdx.graphics.getWidth());
		pane.setPosition(0, MENU_VIEWPORT_HEIGHT / 2 - pane.getHeight() / 2);
		
		// a prefix for the weapon outlines
		String prefix = "outlines/" + type + "-" + "tier-" + tierLevel
				+ "-";

		// helper variable for assigning the stats
		int statsIncrement = 0;
		
		// adding all available weapons as columns to the table
		for (final Outline wep : Outline.values()) {
			if (wep.getType().equals(type) && wep.getTierlevel().equals(tierLevel)) {
				// find the current weapon region
				TextureRegion weaponRegion = getAtlas().findRegion(
						prefix + wep.getSimpleName());
				
				Image image = new Image(weaponRegion);
				image.addListener(new DefaultActorListener() {
					
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);

						// check if the pane is standing still
						if (pane.isPanning() == false) {
							// add the stats
							stats.setAttackPower(stats.getAttackPower() + wep.getAttackPower());
							stats.setMagicPower(stats.getMagicPower() + wep.getMagicPower());
							stats.setDefensePower(stats.getDefensePower() + wep.getDefensePower());
							
							// pass the selected weapon to another screen
							game.setScreen(new MaterialSelectScreen(game, stats, wep));
						}
					}
				});

				table.add(image).fillX().colspan(2);
				
				// retrieve stat values
				statsValues[statsIncrement][attackPowerI] = wep.getAttackPower();
				statsValues[statsIncrement][magicPowerI] = wep.getMagicPower();
				statsValues[statsIncrement][defensePowerI] = wep.getDefensePower();
				statsIncrement++;
			}
		}
		
		table.row();
		
		// set up attack power row
		addStat("stats/attack-power", attackPowerI);
		table.row();
		
		// set up magic power row
		addStat("stats/magic-power", magicPowerI);
		table.row();
		
		// set up defense power row
		addStat("stats/defense-power", defensePowerI);
		
		stage.addActor(pane);
	}
	
	private void addStat(String regionName, int statToAdd) {
		for (int i = 0; i < wepsInTier; i++) {
			TextureRegion statsRegion = getAtlas().findRegion(regionName);
			
			Image image = new Image(statsRegion);
			Label stat = new Label(Float.toString(statsValues[i][statToAdd]), getSkin());
			
			table.add(image).left().width(statIconSize);
			table.add(stat).left().width(wepW - statIconSize - 30);
		}
	}
}