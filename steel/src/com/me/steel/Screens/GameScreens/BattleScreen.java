package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.me.steel.Steel;
import com.me.steel.Domain.Backgrounds.Themes.Jungle;
import com.me.steel.Domain.Entities.TweenAnimations.FightableEntity;

public class BattleScreen implements Screen {

	private Steel game;
	private TextureAtlas defaultAtlas;
	private TextureAtlas skeletonAtlas;
	private FightableEntity[] heroes;
	private Jungle jungleTheme;
	
	public BattleScreen(Steel game, TextureAtlas defaultAtlas, TextureAtlas skeletonAtlas, FightableEntity[] heroes) {
		this.game = game;
		this.defaultAtlas = defaultAtlas;
		this.skeletonAtlas = skeletonAtlas;
		this.heroes = heroes;
	}
	
	@Override
	public void show() {
		jungleTheme = new Jungle(game, defaultAtlas, skeletonAtlas, heroes);
	}
	
	@Override
	public void render(float delta) {
		jungleTheme.update(delta);
		jungleTheme.draw();
	}
	
	@Override
	public void dispose() {
		jungleTheme.dispose();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
}
