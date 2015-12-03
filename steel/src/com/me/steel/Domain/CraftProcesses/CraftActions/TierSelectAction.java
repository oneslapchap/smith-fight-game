package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Utils.DefaultActorListener;

public class TierSelectAction extends CraftAction {
	
	private int tier;
	
	public TierSelectAction(final Stage stage, Skin skin) {
		super(stage);
		
		int tierCount = 2;
		
		Table table = new Table(skin);
		table.setFillParent(false);
		
		final ScrollPane pane = new ScrollPane(table, skin);
		pane.setHeight(Gdx.graphics.getHeight());
		pane.setWidth(Gdx.graphics.getWidth() / 2);
		pane.setPosition(Gdx.graphics.getWidth() / 2 - pane.getWidth() / 2, Gdx.graphics.getHeight() / 2 - pane.getHeight() / 2);
		pane.setScrollingDisabled(true, false);
		
		for (int i = 0; i < tierCount; i++) {
			TextButton textButton = new TextButton("Tier "
					+ Integer.toString(i + 1), skin);
			
			final int onTier = i;
			textButton.addListener(new DefaultActorListener() {

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					
					if (!pane.isPanning()) {
						startCompleteSequence();
						tier = onTier;
					}
				}
				
			});
			
			table.add(textButton).size(pane.getWidth(), 100);
			table.row();
		}
		
		group.addActor(pane);
	}
	
	public int tierSelected() {
		return tier;
	}
}
