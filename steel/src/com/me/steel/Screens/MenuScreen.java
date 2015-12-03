package com.me.steel.Screens;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Screens.GameScreens.CampScreen;
import com.me.steel.Services.SoundManager.TyrianSound;
import com.me.steel.Utils.DefaultActorListener;

public class MenuScreen extends AbstractScreen {
	public MenuScreen(Steel game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		// retrieve the default table actor
		Table table = getTable();
		table.add("Steel").spaceBottom(50);
		table.row();

		// register the button "start game"
		TextButton startGameButton = new TextButton("Go to camp", getSkin());
		startGameButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.getSoundManager().play(TyrianSound.CLICK);
				game.setScreen(new CampScreen(game));
			}
		});
		table.add(startGameButton).size(300, 60).uniform().spaceBottom(10);
		table.row();

		// register the button "options"
		TextButton optionsButton = new TextButton("Options", getSkin());
		optionsButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.getSoundManager().play(TyrianSound.CLICK);
				game.setScreen(new OptionsScreen(game));
			}
		});
		table.add(optionsButton).uniform().fill().spaceBottom(10);
		table.row();

		// register the button "high scores"
		TextButton highScoresButton = new TextButton("High Scores", getSkin());
		highScoresButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				game.getSoundManager().play(TyrianSound.CLICK);
				game.setScreen(new HighScoresScreen(game));
			}
		});
		table.add(highScoresButton).uniform().fill();
	}
}
