package com.me.steel.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.me.steel.Steel;
import com.me.steel.Domain.Profile;
import com.me.steel.Screens.Scene2d.Ship2D;
import com.me.steel.Services.MusicManager.TyrianMusic;

public class LevelScreen extends AbstractScreen {
	private final Profile profile;

	private Ship2D ship2d;

	public LevelScreen(Steel game, int targetLevelId) {
		super(game);

		// set the basic attributes
		profile = game.getProfileManager().retrieveProfile();

		Gdx.app.log(Steel.LOG, "Level constructor called");
	}

	@Override
	protected boolean isGameScreen() {
		return true;
	}

	@Override
	public void show() {
		super.show();

		// play the level music
		game.getMusicManager().play(TyrianMusic.LEVEL);

		// create the ship and add it to the stage
		//ship2d = Ship2D.create(profile.getShip(), getAtlas());

		// center the ship horizontally
		ship2d.setInitialPosition(
				(stage.getWidth() / 2 - ship2d.getWidth() / 2),
				ship2d.getHeight());

		// add the ship to the stage
		stage.addActor(ship2d);

		// add a fade-in effect to the whole stage
		stage.getRoot().getColor().a = 0f;
		stage.getRoot().addAction(Actions.fadeIn(0.5f));
	}
}
