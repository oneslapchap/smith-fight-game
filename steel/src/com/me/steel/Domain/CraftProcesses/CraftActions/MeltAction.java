package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.Entities.SmeltPot;
import com.me.steel.Utils.DefaultActorListener;

public class MeltAction extends CraftAction {

	private SmeltPot smeltPot;
	
	public MeltAction(final CraftableProcess process) {
		super(process.getStage());
		
		smeltPot = new SmeltPot(process.getAtlas(), process.getSmeltTimer());
		smeltPot.getGroup().setVisible(true);
		smeltPot.getGroup().setPosition(
				Gdx.graphics.getWidth() / 2 - smeltPot.getGroup().getWidth() / 2,
				Gdx.graphics.getHeight() / 2 - smeltPot.getGroup().getHeight() / 2);
		
		group.addListener(new DefaultActorListener() {
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				smeltPot.getHatch().addAction(Actions.sequence(
						Actions.moveTo(smeltPot.getHatch().getX(), Gdx.graphics.getHeight(), 0.5f),
						endAction));
				return true;
			}
		});
		
		group.addActor(smeltPot.getGroup());
	}
	
	@Override
	public void update() {
		if (firstTime) {			
			smeltPot.getSmeltTimer().start();
			firstTime = false;
		}
		smeltPot.update(Gdx.graphics.getDeltaTime());
	}
	
	final Action endAction = new Action() {
		@Override
		public boolean act(float delta) {
			smeltPot.getSmeltTimer().stop();
			startCompleteSequence();
			return true;
		}
	};
	
	@Override
	public void reset() {
		super.reset();
		smeltPot.restart();
	}
}
