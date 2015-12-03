package com.me.steel;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.me.steel.Screens.MenuScreen;
import com.me.steel.Services.LevelManager;
import com.me.steel.Services.MusicManager;
import com.me.steel.Services.PreferencesManager;
import com.me.steel.Services.ProfileManager;
import com.me.steel.Services.SoundManager;
import com.me.steel.Utils.DebugBox;

/**
 * The game's main class, called as application events are fired.
 */
public class Steel extends Game {
	// constant useful for logging
	public static final String LOG = Steel.class.getSimpleName();

	// whether we are in development mode
	public static final boolean DEV_MODE = true;

	// helper variables. A way to display fps on every platform
	public SpriteBatch batch;
	private BitmapFont font;

	// services
	private PreferencesManager preferencesManager;
	private ProfileManager profileManager;
	private LevelManager levelManager;
	private MusicManager musicManager;
	private SoundManager soundManager;
	
	// debug box
	DebugBox debugBox;

	// Services' getters

	public PreferencesManager getPreferencesManager() {
		return preferencesManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public LevelManager getLevelManager() {
		return levelManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public SoundManager getSoundManager() {
		return soundManager;
	}

	// Game-related methods

	@Override
	public void create() {
		Gdx.app.log(Steel.LOG, "Creating game on " + Gdx.app.getType());
		
		// create the preferences manager
		preferencesManager = new PreferencesManager();

		// create the music manager
		musicManager = new MusicManager();
		musicManager.setVolume(preferencesManager.getVolume());
		musicManager.setEnabled(preferencesManager.isMusicEnabled());

		// create the sound manager
		soundManager = new SoundManager();
		soundManager.setVolume(preferencesManager.getVolume());
		soundManager.setEnabled(preferencesManager.isSoundEnabled());

		// create the profile manager
		profileManager = new ProfileManager();
		profileManager.retrieveProfile();

		// create the level manager
		levelManager = new LevelManager();

		// create the helper objects
		batch = new SpriteBatch();
		font = new BitmapFont();
		
		// initialize debug box so the static methods of the class become available
		debugBox = new DebugBox();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		Gdx.app.log(Steel.LOG, "Resizing game to: " + width + " x " + height);

		// show the splash screen when the game is resized for the first time;
		// this approach avoids calling the screen's resize method repeatedly
		if (getScreen() == null) {
			if (DEV_MODE) {
				setScreen(new MenuScreen(this));
			} else {
				setScreen(new MenuScreen(this));
			}
		}
	}

	@Override
	public void render() {
		super.render();

		// output the current FPS
		if (DEV_MODE) {
			batch.begin();
			font.draw(batch, "fps:" + Gdx.graphics.getFramesPerSecond(), 0, 20);
			batch.end();
		}
	}

	@Override
	public void pause() {
		super.pause();
		Gdx.app.log(Steel.LOG, "Pausing game");

		// persist the profile, because we don't know if the player will come
		// back to the game
		profileManager.persist();
	}

	@Override
	public void resume() {
		super.resume();
		Gdx.app.log(Steel.LOG, "Resuming game");
	}

	@Override
	public void setScreen(Screen screen) {
		super.setScreen(screen);
		Gdx.app.log(Steel.LOG, "Setting screen: "
				+ screen.getClass().getSimpleName());
	}

	@Override
	public void dispose() {
		super.dispose();
		Gdx.app.log(Steel.LOG, "Disposing game");

		// dipose some services
		musicManager.dispose();
		soundManager.dispose();

		// dispose helper objects
		batch.dispose();
		font.dispose();
		
		// dispose of debug box
		debugBox.dispose();
	}
}
