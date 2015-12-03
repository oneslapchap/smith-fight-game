package com.me.steel.Screens.GameScreens;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Screens.Scene2d.Handle2D;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Services.SoundManager.TyrianSound;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Stats;
import com.me.steel.Utils.TweenAnimation.ImageAccessor;

public class PowerSourceScreen extends AbstractScreen {

	private Stats stats;
	private Outline wep;
	private Material material;
	
	private Handle2D dragHandle;
	private Outline2D outline2d;
	
	private TweenManager manager;
	private boolean dropped;

	public PowerSourceScreen(Steel game, Stats stats, Outline wep, Material material) {
		super(game);
		
		this.stats = stats;
		this.wep = wep;
		this.material = material;
		
		manager = new TweenManager();
	}

	@Override
	public void show() {
		super.show();

		// scale textures by
		float scale = 3f;

		// set up outline2d
		outline2d = new Outline2D(getAtlas(), wep, false);
		outline2d.setSize(outline2d.getWidth() * scale, outline2d.getHeight()
				* scale);
		float outlineW = outline2d.getWidth();
		float outlineH = outline2d.getHeight();

		outline2d.setInitialPosition(MENU_VIEWPORT_WIDTH / 2 - outlineW,
				-outlineH / 1.5f);
		outline2d.setOrigin(outlineW / 2, outlineH / 2);
		outline2d.rotate(180);
		outline2d
				.setColor(material.getRedColorValue(),
						material.getGreenColorValue(),
						material.getBlueColorValue(), 1f);

		dragHandle = new Handle2D(getAtlas());
		dragHandle.setSize(dragHandle.getWidth() * scale, dragHandle.getHeight() * scale);
		
		Table table = getTable();
		table.setFillParent(false);

		// set up the next screen button
		final TextButton nextScreenButton = new TextButton("Continue",
				getSkin());
		nextScreenButton.setPosition(0,
				MENU_VIEWPORT_HEIGHT - nextScreenButton.getHeight());
		nextScreenButton.setTouchable(Touchable.disabled);
		nextScreenButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// add the stats
				stats.setAttackPower(stats.getAttackPower() + dragHandle.getCurrentHandle().getAttackPower());
				stats.setMagicPower(stats.getMagicPower() + dragHandle.getCurrentHandle().getMagicPower());
				stats.setDefensePower(stats.getDefensePower() + dragHandle.getCurrentHandle().getDefensePower());
				
				game.getSoundManager().play(TyrianSound.CLICK);
				game.setScreen(new EnchantScreen(game, stats, wep, material, dragHandle
						.getCurrentHandle()));
			}
		});

		// combine the table with a scroll pane
		final ScrollPane pane = new ScrollPane(table, getSkin());
		pane.setScrollingDisabled(true, false);
		pane.setHeight(MENU_VIEWPORT_HEIGHT);
		pane.setPosition(MENU_VIEWPORT_WIDTH - pane.getWidth(), 0);
		pane.setCancelTouchFocus(false);
		
		final Label label = new Label("", getSkin());
		label.setPosition(0, 50);
		label.setFontScale(3);
		
		String prefix = "handles/";

		// adding all available weapons as columns to the table
		for (final Handle handle : Handle.values()) {
			// find the current weapon region
			TextureRegion handleRegion = super.getAtlas().findRegion(
					prefix + handle.getSimpleName());

			Image image = new Image(handleRegion);
			image.addListener(new DefaultActorListener() {

				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer,
						int button) {
					// check if the pane is standing still
					if (pane.isPanning() == false) {
						// pass the selected weapon to another screen
						dragHandle.replaceRegion(handle);
						dragHandle.flip(false, true);
						dragHandle.setPosition(event.getStageX() - dragHandle.getWidth() / 2, event.getStageY() - dragHandle.getHeight() / 2);
						nextScreenButton.setTouchable(Touchable.enabled);
					}		
					
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					
					Tween.to(dragHandle, ImageAccessor.POS_XY, 0.5f)
					.target(dragHandle.getX(), outline2d.getY() + outline2d.getHeight() - dragHandle.getHeight())
					.start(manager);
					
					float handleMidPrc = (dragHandle.getX() + dragHandle.getWidth() / 2) * 100 / MENU_VIEWPORT_WIDTH;
					float outlineMidPrc = (outline2d.getX() + outline2d.getWidth() / 2) * 100 / MENU_VIEWPORT_WIDTH;
					
					if (handleMidPrc >= outlineMidPrc - 0.5f && handleMidPrc <= outlineMidPrc + 0.5f) {
						label.setText("Perfect");
						
						//Gdx.app.log("", Float.toString(handleMidPrc));
						//Gdx.app.log("", Float.toString(outlineMidPrc));
					}
					else if (handleMidPrc >= outlineMidPrc - 1 && handleMidPrc <= outlineMidPrc + 1)
						label.setText("Great");
					else
						label.setText("Too bad");
				}
			});
			
			table.add(image);
			table.row();
		}

		stage.addListener(new DragListener() {
			
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
			
				dragHandle.setPosition(x - dragHandle.getWidth() / 2, y - dragHandle.getHeight() / 2);
				
				if (dragHandle.getY() + dragHandle.getHeight() < MENU_VIEWPORT_HEIGHT)
					dragHandle.setY(MENU_VIEWPORT_HEIGHT - dragHandle.getHeight());
			}
			
		});
		
		
		// add the actors to the stage
		stage.addActor(outline2d);
		stage.addActor(nextScreenButton);
		stage.addActor(pane);
		stage.addActor(dragHandle);
		stage.addActor(label);
	}
	
	@Override
	public void render(float delta) {
		manager.update(delta);
		
		if (dropped) {
			
			
			
			
			//Gdx.app.log("", Float.toString(draghandleMid) + " " + Float.toString(outline2dMid));
			
			//if (dragHandle.getX() + dragHandle.getWidth() / 2 >= outline2d.getX() + outline2d.getWidth() / 2) {
			//	Gdx.app.log("", "PERFECT");
			//}
			
			//Gdx.app.log("", Float.toString(dragHandle.getX() + dragHandle.getWidth() / 2) + " " + Float.toString(outline2d.getX() + outline2d.getWidth() / 2));
			
			dropped = false;
		}
		
		super.render(delta);
		
		dragHandle.drawDebug();
		outline2d.drawDebug();
	}
}
