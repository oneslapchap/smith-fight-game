package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.Entities.LavaSpill;
import com.me.steel.Domain.Entities.Animations.SpriteAnimation;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Screens.Scene2d.Chain2D;
import com.me.steel.Screens.Scene2d.Switch2D;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.ParticleEffectActor;
import com.me.steel.Utils.SpriteActor;

public class SmeltAction extends CraftAction {

	private SpriteActor outline;
	private SpriteActor boilingLava;
	private LavaSpill lavaSpill;
	private Image chain;
	private SpriteActor rect;
	private Switch2D switch2d;
	private ParticleEffect smokeEffect;
	private ParticleEffect sparkEffect;
	private TextButton nextButton;

	// keeps track whether the is being held or not
	private boolean chainHold;
	
	// make sure the smoke only emits once
	private boolean smokeEmitted;
	
	// initial distance between chain.y and it's final destination (when it's
	// fully pulled out on the screen)
	private float chainInitialDistance;
	
	private CraftableWeapon weapon;
	
	// convenience variables
	private Vector2 origChainPos;
	
	public SmeltAction(final CraftableProcess process) {
		super(process.getStage());
		
		this.weapon = process.getWeapon();
		this.smokeEffect = process.getSmokeEffect();
		this.sparkEffect = process.getSparkEffect();
		
		outline = new SpriteActor();
		outline.getSprite().setSize(270 * 1.25f, 360 * 1.25f);
		outline.getSprite().setOrigin(outline.getSprite().getWidth() / 2, outline.getSprite().getHeight() / 2);
		outline.getSprite().rotate(-90);
		outline.setVisible(false);
		
		float[] verts = outline.getSprite().getVertices();
		verts[SpriteBatch.X1] += outline.getSprite().getWidth() / 1.75f;
		verts[SpriteBatch.X2] += outline.getSprite().getWidth() / 1.75f;
		verts[SpriteBatch.Y1] -= outline.getSprite().getWidth() / 1.75f;
		verts[SpriteBatch.Y2] -= outline.getSprite().getWidth() / 1.75f;
		
		float outlineW = verts[SpriteBatch.X2] - verts[SpriteBatch.X4];
		float outlineH = verts[SpriteBatch.Y1] - verts[SpriteBatch.Y4];
		outline.getSprite().setPosition(
				Gdx.graphics.getWidth() / 2 - outlineW / 2 - verts[SpriteBatch.X4], 
				Gdx.graphics.getHeight() / 2 - outlineH / 2 - verts[SpriteBatch.Y4]);
		
		lavaSpill = new LavaSpill(process.getAtlas());
		lavaSpill.setMaxWidth(outline.getSprite().getWidth() / 3);
		lavaSpill.setHeight(Gdx.graphics.getHeight() / 2);
		lavaSpill.getLavaActor().getSprite().setPosition(
				verts[SpriteBatch.X4] + (verts[SpriteBatch.X1] - verts[SpriteBatch.X4]) / 2,
				verts[SpriteBatch.Y4] + outlineH / 2);
		
		boilingLava = new SpriteActor(SpriteAnimation.create(process.getAtlas(), "single-animations/lava"));
		boilingLava.copyVerts(outline.getSprite());
		verts = boilingLava.getSprite().getVertices();
		verts[SpriteBatch.X2] = verts[SpriteBatch.X1];
		verts[SpriteBatch.X3] = verts[SpriteBatch.X4];
		
		// set up rectActor
		rect = new SpriteActor(new Sprite(process.getAtlas().findRegion("single-objects/blank")));
		rect.copyVerts(outline.getSprite());
		rect.setVisible(false);
		
		// set up chain
		chain = new Chain2D(process.getAtlas());
		chain.setPosition(
				Gdx.graphics.getWidth() - chain.getWidth() * 2,
				Gdx.graphics.getHeight() - chain.getHeight() / 2);
		chain.addListener(new DefaultActorListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				chainHold = true;
				return true;
			}

			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				chainHold = false;
			}
		});

		// set up smoke particleEffectActor
		ParticleEffectActor smokeParticleEffectActor = new ParticleEffectActor(
				smokeEffect);
		smokeParticleEffectActor.setInitialPosition(Gdx.graphics.getWidth() / 2,
				outline.getY());

		// set up sparks particleEffectActor
		ParticleEffectActor sparkParticleEffectActor = new ParticleEffectActor(
				sparkEffect);
		
		// set up switch2d. Make it invisible at first
		switch2d = new Switch2D(process.getAtlas());
		switch2d.setPosition(0, Gdx.graphics.getHeight() / 2 - switch2d.getHeight()
				/ 2);
		switch2d.setVisible(false);
		switch2d.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// change the drawable of switch2d
				switch2d.triggerSwitch(false);

				if (smokeEmitted == false) {
					// emit smoke
					smokeEffect.start();

					Material material = weapon.getMaterial();
					
					// make the rect visible
					rect.getSprite().setColor(
							material.getRedColorValue(),
							material.getGreenColorValue(),
							material.getBlueColorValue(),
							1);
					
					rect.setVisible(true);

					// make the nextButton available
					nextButton.setVisible(true);

					// make sure this code runs only once
					smokeEmitted = true;
				}
			}
		});
		
		// set up the nextButton
		nextButton = new TextButton("Next", process.getSkin());
		nextButton.setPosition(Gdx.graphics.getWidth() - nextButton.getWidth(), 0);
		nextButton.setVisible(false);
		nextButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// proceed to the next screen
				startCompleteSequence();
			}
		});
		
		process.getStage().addListener(new DefaultActorListener() {

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
				/*
				 * proceed if chain is being held down, if y is lesser than
				 * the current chain2d.y and if there's some chain out of the
				 * screen
				 */
				if (chainHold == true
						&& y < chain.getY()
						&& chain.getY() + chain.getHeight() > Gdx.graphics.getHeight()) {
					
					// distance since last frame
					float lastFrameDistance = -(y - chain.getY());

					// update the chains position
					chain.setY(y);

					// calculate the current boilingLava height depending on the
					// percent change
					float percentChange = lastFrameDistance * 100 / chainInitialDistance;
					float addToLava = percentChange * outline.getSprite().getHeight() / 100f;
					
					float[] verts = boilingLava.getSprite().getVertices();
					verts[SpriteBatch.X2] += addToLava;
					verts[SpriteBatch.X3] += addToLava;
					
					lavaSpill.updateLava(percentChange);
					
					// add + 1 to boilingLava2d.getY() to bypass the calculation error
					if (verts[SpriteBatch.X2] >= outline.getSprite().getVertices()[SpriteBatch.X2] - 1) {
						switch2d.setVisible(true);
						
						// correct boilingLava height
						verts[SpriteBatch.X2] = outline.getSprite().getVertices()[SpriteBatch.X2];
						verts[SpriteBatch.X3] = outline.getSprite().getVertices()[SpriteBatch.X3];
						
						// correct chain.y position
						chain.setY(Gdx.graphics.getHeight() - chain.getHeight());
					}
				}
			}
		});

		// calculate the distance
		chainInitialDistance = chain.getY()
				- (Gdx.graphics.getHeight() - chain.getHeight());
		
		// add actors to the stage
		group.addActor(boilingLava);
		group.addActor(rect);
		group.addActor(outline);
		group.addActor(chain);
		group.addActor(switch2d);
		group.addActor(lavaSpill.getLavaActor());
		group.addActor(smokeParticleEffectActor);
		group.addActor(sparkParticleEffectActor);
		group.addActor(nextButton);
		
		origChainPos = new Vector2();
		origChainPos.x = chain.getX();
		origChainPos.y = chain.getY();
	}

	@Override
	public void update() {
		if (firstTime) {
			outline.getSprite().setRegion(((TextureRegionDrawable)weapon.getBgOutline().getDrawable()).getRegion());
			outline.setVisible(true);
			firstTime = false;
		}
		
		lavaSpill.update();
		
		// update sparkEffect position when playing to cling to boilingLava2d
		if (sparkEffect.isComplete() == false)
			sparkEffect.setPosition(
					boilingLava.getSprite().getX() + boilingLava.getSprite().getWidth() / 2,
					boilingLava.getSprite().getY());
	}
	
	@Override
	public void reset() {
		super.reset();
		
		float[] verts = boilingLava.getSprite().getVertices();
		verts[SpriteBatch.X2] = verts[SpriteBatch.X1];
		verts[SpriteBatch.X3] = verts[SpriteBatch.X4];
		
		switch2d.setVisible(false);
		switch2d.triggerSwitch(true);
		smokeEmitted = false;
		
		rect.setVisible(false);
		lavaSpill.reset();
		chain.setPosition(
				origChainPos.x, 
				origChainPos.y);
	}
}
