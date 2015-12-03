package com.me.steel.Screens.GameScreens;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Utils.DefaultActorListener;

public class TierSelectScreen extends AbstractScreen {

	// available weapon types
	private final String[] types = { "sword", "staff" };

	public TierSelectScreen(Steel game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();

		// specify selectable tiers for convenience
		boolean[] available = {true, true};
		
		// available tiers
		int tierCount = 2;

		// initialize table and pane list
		List<Table> table = new ArrayList<Table>();
		List<ScrollPane> pane = new ArrayList<ScrollPane>();

		// x position of the pane
		int panePositionX = 0;

		// step through the weapon types
		for (int i = 0; i < types.length; i++) {

			// setup a new table and add it to the table list
			table.add(new Table(getSkin()));
			table.get(i).debug();
			table.get(i).setFillParent(false);
			if (available[i] == false)
				table.get(i).setTouchable(Touchable.disabled);

			// setup a new pane and add it to the pane list
			pane.add(new ScrollPane(table.get(i), getSkin()));
			pane.get(i).setScrollingDisabled(true, false);
			pane.get(i).setHeight(MENU_VIEWPORT_HEIGHT);
			pane.get(i).setWidth(MENU_VIEWPORT_WIDTH / types.length);
			pane.get(i).setPosition(panePositionX, 0);

			// x position of the next pane
			panePositionX += pane.get(i).getWidth();

			// add a label at the top of every table
			table.get(i).add(new Label(types[i], getSkin()));
			table.get(i).row();

			// step through the tiers
			for (int j = 0; j < tierCount; j++) {
				// setup a text button for every tier
				TextButton textButton = new TextButton("Tier "
						+ Integer.toString(j + 1), getSkin());
				// register the current button
				registerButtonEvent(textButton, pane.get(i), i, j);

				// add the textButton to the table
				table.get(i).add(textButton).size(pane.get(i).getWidth(), 100);
				table.get(i).row();
			}

			// add the pane to the table
			stage.addActor(pane.get(i));
		}

	}

	// used to register the button events for convenience
	public void registerButtonEvent(TextButton textButton,
			final ScrollPane pane, final int i, final int j) {
		textButton.addListener(new DefaultActorListener() {

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// check if the pane is standing still
				if (pane.isPanning() == false)
					// pass the selected weapon type and tier
					game.setScreen(new OutlineScreen(game, types[i], Integer
							.toString(j + 1)));
			}
		});
	}
}
