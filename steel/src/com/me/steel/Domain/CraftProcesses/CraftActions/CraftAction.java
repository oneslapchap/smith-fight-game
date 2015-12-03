package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public abstract class CraftAction implements CraftableAction {
	protected boolean complete;
	protected boolean firstTime;
	protected Group group;
	protected Stage stage;
	
	protected float x;
	protected float y;
	
	public CraftAction(Stage stage) {
		group = new Group();
		group.setPosition(0, Gdx.graphics.getHeight());
		this.stage = stage;
		firstTime = true;
	}
	
	@Override
	public void update() {}
	
	@Override
	public void reset() {
		complete = false;
		firstTime = true;
	}
	
	@Override
	public boolean isComplete() {
		return complete;
	}
	
	public void startCompleteSequence() {
		group.addAction(Actions.sequence(Actions.moveTo(0, Gdx.graphics.getHeight(), .5f), completeAction));
	}
	
	final Action completeAction = new Action() {
		@Override
		public boolean act(float delta) {
			group.remove();
			complete = true;
			return true;
		}
	};
	
	@Override
	public void addToStage() {
		stage.addActor(group);
		group.toBack();
		group.addAction(Actions.moveTo(x, y, .5f));
	}
	
	@Override
	public void removeFromStage() {
		group.remove();
	}
}
