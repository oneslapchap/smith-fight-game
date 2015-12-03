package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.me.steel.Steel;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.CraftProcesses.WarriorProcess;
import com.me.steel.Domain.CraftProcesses.WizardProcess;

public class CraftScreen implements Screen {

	private Steel game;
	private String heroType;
	private CraftableProcess craftProcess;
	
	public CraftScreen(Steel game, String heroType) {
		this.game = game;
		this.heroType = heroType;
	}
	
	@Override
	public void show() {
		
		Gdx.gl.glClearColor(0 / 255f, 102 / 255f, 0 / 255f, 1);
		
	
	}

	@Override
	public void render(float delta) {
		craftProcess.update(delta);

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

	@Override
	public void dispose() {
		craftProcess.dispose();
	}
}
