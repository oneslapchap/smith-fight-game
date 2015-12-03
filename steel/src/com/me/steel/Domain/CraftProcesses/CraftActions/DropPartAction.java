package com.me.steel.Domain.CraftProcesses.CraftActions;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.CraftProcesses.WarriorProcess;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Item;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.TweenAnimation.ImageAccessor;

public class DropPartAction extends CraftAction {

	private Image outline;
	private Image[] dragImage;
	private ScrollPane[] pane;
	private CraftableWeapon weapon;
	private TweenManager manager;
	private int index;
	
	private float scale;
	private boolean flip;
	private boolean dropped;
	private float tweenY;
	
	public DropPartAction(final CraftableProcess process) {
		super(process.getStage());
		
		weapon = process.getWeapon();
		manager = new TweenManager();
		
		scale = 3f;
		
		outline = new Image();
		dragImage = new Image[weapon.getParts().length];
		for (int i = 0; i < dragImage.length; i++)
			dragImage[i] = new Image();
		
		if (process instanceof WarriorProcess) {
			flip = true;
			outline.rotate(180);
		}
		
		// set up the next screen button
		final TextButton nextScreenButton = new TextButton("Continue", process.getSkin());
		nextScreenButton.setPosition(0,
				Gdx.graphics.getHeight() - nextScreenButton.getHeight());
		nextScreenButton.setTouchable(Touchable.disabled);
		nextScreenButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				startCompleteSequence();
			}
		});
		
		final Label label = new Label("", process.getSkin());
		label.setPosition(0, 50);
		label.setFontScale(3);
		
		pane = new ScrollPane[dragImage.length];
			
		for (int i = 0; i < weapon.getParts().length; i++) {
			Table table = new Table();
			table.setFillParent(false);
	
			// combine the table with a scroll pane
			pane[i] = new ScrollPane(table, process.getSkin());
			pane[i].setScrollingDisabled(true, false);
			pane[i].setHeight(Gdx.graphics.getHeight());
			pane[i].setPosition(Gdx.graphics.getWidth() - pane[i].getWidth(), 0);
			pane[i].setCancelTouchFocus(false);
			if (i != 0) pane[i].setVisible(false);
					
			final String prefix = weapon.getParts()[i].getPrefix();
	
			// adding all available weapons as columns to the table
			for (final Item item : weapon.getParts()[i].getItemValues()) {
				// find the current weapon region
				TextureRegion region = process.getAtlas().findRegion(
						prefix + item.getSimpleName());
				
				final Image dragImageTemp = new Image(new TextureRegion(process.getAtlas().findRegion(
						prefix + item.getSimpleName())));
				final float dragImageW = dragImageTemp.getWidth() * scale;
				final float dragImageH = dragImageTemp.getHeight() * scale;
				((TextureRegionDrawable)dragImageTemp.getDrawable()).getRegion().flip(false, flip);
				
				final Image image = new Image(region);
				image.addListener(new DefaultActorListener() {
	
					@Override
					public boolean touchDown(InputEvent event, float x, float y, int pointer,
							int button) {
						// check if the pane is standing still
						if (pane[index].isPanning() == false) {				
							// replace the current region with a specified one
							dragImage[index].setDrawable(dragImageTemp.getDrawable());
							dragImage[index].setSize(dragImageW, dragImageH);
							dragImage[index].setPosition(
									event.getStageX() - dragImage[index].getWidth() / 2, 
									event.getStageY() - dragImage[index].getHeight() / 2);
						}		
								
						return true;
					}
	
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						
						if (process instanceof WarriorProcess)
							tweenY -= dragImage[index].getHeight();
						
						Tween.to(dragImage[index], ImageAccessor.POS_XY, 0.5f)
						.target(dragImage[index].getX(), tweenY)
						.start(manager);
						
						float partMidPrc = (dragImage[index].getX() + dragImage[index].getWidth() / 2) * 100 / Gdx.graphics.getWidth();
						float outlineMidPrc = (outline.getX() + outline.getWidth() / 2) * 100 / Gdx.graphics.getWidth();
								
						if (partMidPrc >= outlineMidPrc - 0.5f && partMidPrc <= outlineMidPrc + 0.5f) {
							label.setText("Perfect");
						}
						else if (partMidPrc >= outlineMidPrc - 1 && partMidPrc <= outlineMidPrc + 1)
							label.setText("Great");
						else
							label.setText("Too bad");
						
						weapon.getParts()[index].setPartImage(dragImageTemp);
						
						if (index + 1 < dragImage.length) {
							pane[index].setVisible(false);
							index++;
							pane[index].setVisible(true);	
							dropped = true;
						}
						else
							nextScreenButton.setTouchable(Touchable.enabled);
					}
				});
						
				table.add(image);
				table.row();
			}
			
			group.addActor(pane[i]);
		}
		
		group.addListener(new DragListener() {
			
			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
				
				dragImage[index].setPosition(x - dragImage[index].getWidth() / 2, y - dragImage[index].getHeight() / 2);
				
				if (dragImage[index].getY() + dragImage[index].getHeight() < Gdx.graphics.getHeight())
					dragImage[index].setY(Gdx.graphics.getHeight() - dragImage[index].getHeight());
			}
					
		});
				
		// add the actors to the stage
		group.addActor(outline);
		group.addActor(nextScreenButton);
		for (Image element : dragImage) group.addActor(element);
		group.addActor(label);
	}
	
	@Override
	public void update() {
		if (firstTime) {
			Material material = weapon.getMaterial();
			Drawable drawable = weapon.getOutline().getDrawable();
			
			outline.setDrawable(drawable);
			float outlineW = drawable.getMinWidth() * scale;
			float outlineH = drawable.getMinHeight() * scale;
			
			outline.setSize(outlineW, outlineH);
			outline.setColor(
					material.getRedColorValue(),
					material.getGreenColorValue(),
					material.getBlueColorValue(), 1f);
			
			outline.setPosition(Gdx.graphics.getWidth() / 2 - outlineW,
					-outlineH / 1.5f);
			outline.setOrigin(outlineW / 2, outlineH / 2);
			
			tweenY = outline.getY() + outline.getHeight();
			
			firstTime = false;
		}
		
		manager.update(Gdx.graphics.getDeltaTime());
		
		if (dropped == true) {
			if (manager.getRunningTweensCount() == 0) {
				Tween.to(outline, ImageAccessor.POS_XY, 0.5f)
				.target(outline.getX(), outline.getY() - outline.getHeight() / 2)
				.start(manager);
				
				float targetY = 0;
				for (int i = 0; i < index; i++) {
					targetY = dragImage[i].getY() - dragImage[i].getHeight() / 2;
					
					Tween.to(dragImage[i], ImageAccessor.POS_XY, 0.5f)
					.target(dragImage[i].getX(), targetY)
					.start(manager);
				}
				
				tweenY = targetY + dragImage[index-1].getHeight();
				dropped = false;
			}	
		}
	}
	
	@Override
	public void reset() {
		super.reset();
		
		index = 0;
		
		for (ScrollPane element : pane)
			element.setVisible(false);
		pane[0].setVisible(true);
		
		for (Image image : dragImage)
			image.setDrawable(null);
	}
}
