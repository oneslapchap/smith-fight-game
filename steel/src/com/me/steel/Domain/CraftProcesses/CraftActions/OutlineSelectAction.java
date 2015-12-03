package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Utils.DefaultActorListener;

public class OutlineSelectAction extends CraftAction {
	
	private TextureAtlas atlas;
	private Skin skin;
	
	private ScrollPane pane;
	private Table table;
	private float[][] statsValues;
	private int wepsInTier;
	private int wepW;
	private int wepH;
	private int statIconSize;
	
	public OutlineSelectAction(final CraftableProcess process) {
		super(process.getStage());
		
		final CraftableWeapon weapon = process.getWeapon();
		
		this.atlas = process.getAtlas();
		this.skin = process.getSkin();
		
		// some in scope finals for convenience
		final int attackPowerI = 0;
		final int magicPowerI = 1;
		final int defensePowerI = 2;
		
		// setup a table
		table = new Table();
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
		
		pane = new ScrollPane(table);
		pane.setScrollingDisabled(false, true);
		pane.setHeight(wepH + statIconSize * 3);
		pane.setWidth(Gdx.graphics.getWidth());
		pane.setPosition(0, Gdx.graphics.getHeight() / 2 - pane.getHeight() / 2);
		
		// helper variable for assigning the stats
		int statsIncrement = 0;
		
		// adding all available weapons as columns to the table
		for (final Outline wep : Outline.values()) {
			if (wep.getType().equals(process.getType()) && wep.getTierlevel().equals(process.getTierLevel())) {
						
				final Outline2D outline2d = new Outline2D(atlas, wep, false);
				final Outline2D bgOutline2d = new Outline2D(atlas, wep, true);
				bgOutline2d.addListener(new DefaultActorListener() {
							
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						// check if the pane is standing still
						if (!pane.isPanning()) {
							weapon.setOutline(outline2d);
							weapon.setBgOutline(bgOutline2d);		
							startCompleteSequence();
						}
					}
				});
				
			table.add(bgOutline2d).fillX().colspan(2);
						
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
		
		group.addActor(pane);
	}
	
	private void addStat(String regionName, int statToAdd) {
		for (int i = 0; i < wepsInTier; i++) {
			TextureRegion statsRegion = atlas.findRegion(regionName);
			
			Image image = new Image(statsRegion);
			Label stat = new Label(Float.toString(statsValues[i][statToAdd]), skin);
			
			table.add(image).left().width(statIconSize);
			table.add(stat).left().width(wepW - statIconSize - 30);
		}
	}
}
