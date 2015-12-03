package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Screens.Scene2d.BoilingLava2D;
import com.me.steel.Screens.Scene2d.Chain2D;
import com.me.steel.Screens.Scene2d.Flare2D;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Screens.Scene2d.Switch2D;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.ParticleEffectActor;
import com.me.steel.Utils.RectActor;
import com.me.steel.Utils.Stats;

public class SmeltScreen extends AbstractScreen {

	private Stats stats;
	private Outline wep;
	private Material material;
	private Outline2D outline2d;
	private BoilingLava2D boilingLava2d;
	private Chain2D chain2d;
	private Switch2D switch2d;
	private Flare2D flare2d;
	private Image background;
	private ParticleEffect smokeEffect;
	private ParticleEffect sparkEffect;
	private ShapeRenderer shapeRenderer;
	private TextButton nextButton;
	private Slider slider;

	// keeps track whether the is being held or not
	private boolean chainHold;

	// make sure the smoke only emits once
	private boolean smokeEmitted;

	// keeps track when chain2d and boilingLava2d reaches their destination
	private boolean destinationReached;
	
	// make sure the flare is activated only once
	private boolean flareActivated;
	
	// initial distance between boilingLava2d.y and outline2d.y
	private float lavaInitialDistance;
	// initial distance between chain2d.y and it's final destination (when it's
	// fully pulled out on the screen)
	private float chainInitialDistance;

	// sliders value decrease speed
	private float sliderDecreaseSpeed;
	
	private float flare2dYSpeed;
	
	public SmeltScreen(Steel game, Stats stats, Outline wep, Material material) {
		super(game);
		this.stats = stats;
		this.wep = wep;
		this.material = material;
	}

	@Override
	public void show() {
		super.show();
		
		sliderDecreaseSpeed = 0.1f;
		
		flare2dYSpeed = 500f;
		
		// set up background
		background = new Image(getAtlas().findRegion("backgrounds/smelt-screen-background"));
		
		// set up slider
		// indicates how much can the user pull the chain
		slider = new Slider(0f, 1f, 0.001f, false, getSkin());
		slider.setWidth(MENU_VIEWPORT_WIDTH / 2);
		slider.setPosition(MENU_VIEWPORT_WIDTH / 2 - slider.getWidth() / 2,
				MENU_VIEWPORT_HEIGHT - slider.getHeight());
		slider.setTouchable(Touchable.disabled);
		slider.setAnimateDuration(0.1f);

		// set up outline2d
		outline2d = new Outline2D(getAtlas(), wep, true);
		outline2d.setInitialPosition(
				MENU_VIEWPORT_WIDTH / 2 - outline2d.getWidth() / 2,
				MENU_VIEWPORT_HEIGHT / 2 - outline2d.getHeight() / 2);
		outline2d
				.setOrigin(outline2d.getWidth() / 2, outline2d.getHeight() / 2);
		outline2d.setRotation(180);

		// create BoilingLava2D via a factory method
		boilingLava2d = BoilingLava2D.create(super.getAtlas());
		// set initial position of the lava
		//boilingLava2d.setInitialPosition(MENU_VIEWPORT_WIDTH / 2
				//- boilingLava2d.getWidth() / 2, MENU_VIEWPORT_HEIGHT);

		// set up chain 2d
		chain2d = new Chain2D(getAtlas());
		chain2d.setInitialPosition(
				MENU_VIEWPORT_WIDTH - chain2d.getWidth() * 2,
				MENU_VIEWPORT_HEIGHT - chain2d.getHeight() / 2);
		chain2d.addListener(new DefaultActorListener() {
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

		// set up rectActor
		shapeRenderer = new ShapeRenderer();
		//final RectActor rectActor = new RectActor(shapeRenderer,
			//	material.getRedColorValue(), material.getGreenColorValue(),
			//	material.getBlueColorValue());
		//rectActor.setSize(outline2d.getWidth(), outline2d.getHeight());
		//rectActor.setPosition(outline2d.getX(), outline2d.getY());
		//rectActor.setVisible(false);

		// set up smoke particleEffectActor
		smokeEffect = new ParticleEffect();
		smokeEffect.load(Gdx.files.internal("particles/smoke.p"),
				Gdx.files.internal("particles"));
		ParticleEffectActor smokeParticleEffectActor = new ParticleEffectActor(
				smokeEffect);
		smokeParticleEffectActor.setInitialPosition(MENU_VIEWPORT_WIDTH / 2,
				outline2d.getY());

		// set up sparks particleEffectActor
		sparkEffect = new ParticleEffect();
		sparkEffect.load(Gdx.files.internal("particles/sparks.p"),
				Gdx.files.internal("particles"));
		ParticleEffectActor sparkParticleEffectActor = new ParticleEffectActor(
				sparkEffect);
		
		// set up switch2d. Make it invisible at first
		switch2d = new Switch2D(getAtlas());
		switch2d.setPosition(0, MENU_VIEWPORT_HEIGHT / 2 - switch2d.getHeight()
				/ 2);
		switch2d.setVisible(false);
		switch2d.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// change the drawable of switch2d
				//switch2d.setSwitchDown();

				if (smokeEmitted == false) {
					// emit smoke
					smokeEffect.start();

					// make the rectActor visible
				//	rectActor.setVisible(true);

					// make the nextButton available
					nextButton.setVisible(true);

					// make sure this code happens only once
					smokeEmitted = true;
				}
			}
		});

		// set up flare2d
		flare2d = new Flare2D(getAtlas());
		flare2d.setInitialPosition(MENU_VIEWPORT_WIDTH / 2 - flare2d.getWidth()
				/ 2, -flare2d.getHeight());
		
		// set up the nextButton
		nextButton = new TextButton("Next", getSkin());
		nextButton.setPosition(MENU_VIEWPORT_WIDTH - nextButton.getWidth(), 0);
		nextButton.setVisible(false);
		nextButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				// proceed to the next screen
				game.setScreen(new GrindScreen(game, stats, wep, material));
			}
		});
		
		stage.addListener(new DefaultActorListener() {

			@Override
			public void touchDragged(InputEvent event, float x, float y,
					int pointer) {
				super.touchDragged(event, x, y, pointer);
				/*
				 * proceed if chain2d is being held down, if y is lesser than
				 * the current chain2d.y and if there's some chain2d out of the
				 * screen
				 */
				if (chainHold == true
						&& y < chain2d.getY()
						&& chain2d.getY() + chain2d.getHeight() > MENU_VIEWPORT_HEIGHT) {

					// distance since last frame
					float lastFrameDistance = -(y - chain2d.getY());

					// update slider's value depending on lastFrameDistance
					slider.setValue(slider.getValue() + lastFrameDistance
							/ 100f);

					// if the slider reached it's peak, add a penalty
					if (slider.getValue() >= 1f) {

						// decrease the stats
						float attackPower = stats.getAttackPower();
						float magicPower = stats.getMagicPower();
						float defensePower = stats.getDefensePower();

						// don't let the stats fall below zero
						if (attackPower > 0 && magicPower > 0
								&& defensePower > 0) {
							// reduce the stats depending on lastFrameDistance
							stats.setAttackPower(attackPower
									- (lastFrameDistance));
							stats.setMagicPower(magicPower
									- (lastFrameDistance));
							stats.setDefensePower(defensePower
									- (lastFrameDistance));
						}

						// only emit sparks if the previous emition ended
						if (sparkEffect.isComplete())
							sparkEffect.start();
					}

					// update the chains position
					chain2d.setY(y);

					// calculate the current distance between chain2d.y and it's
					// destination
					float currentChainDistance = chain2d.getY()
							- (MENU_VIEWPORT_HEIGHT - chain2d.getHeight());
					// calculate the percent chain2d.y has until it reaches its
					// destination
					float chainPercent = (currentChainDistance * 100f)
							/ chainInitialDistance;

					// calculate the current boilingLava2d.y depending on the
					// chain2d percent
					float currentLavaDistance = (chainPercent * lavaInitialDistance) / 100f;
					// set boilingLava2d.y accordingly
					//boilingLava2d.setY(outline2d.getY() - currentLavaDistance);
				}
			}
		});

		// calculate the distances
	//	lavaInitialDistance = outline2d.getY() - boilingLava2d.getY();
		chainInitialDistance = chain2d.getY()
				- (MENU_VIEWPORT_HEIGHT - chain2d.getHeight());

		// add actors to the stage
		//stage.addActor(boilingLava2d);
		//stage.addActor(rectActor);
		stage.addActor(flare2d);
		stage.addActor(background);
		stage.addActor(outline2d);
		stage.addActor(chain2d);
		stage.addActor(switch2d);
		stage.addActor(smokeParticleEffectActor);
		stage.addActor(sparkParticleEffectActor);
		stage.addActor(nextButton);
		stage.addActor(slider);
	}

	@Override
	public void render(float delta) {

		// continue until the destination is reached
		if (destinationReached == false) {

			// add + 1 to boilingLava2d.getY() to bypass the calculation error
			//if (boilingLava2d.getY() - 1 <= outline2d.getY()) {
				// make switch2d visible
				switch2d.setVisible(true);

				// correct boilingLava2d.y position
				//boilingLava2d.setY(outline2d.getY());

				// correct chain2d.y position
				chain2d.setY(MENU_VIEWPORT_HEIGHT - chain2d.getHeight());

				// set true, to make sure this if isn't reentered for no reason
				destinationReached = true;
			}
	//	}

		// update sparkEffect position when playing to cling to boilingLava2d
		//if (sparkEffect.isComplete() == false)
		//	sparkEffect.setPosition(
				//	boilingLava2d.getX() + boilingLava2d.getWidth() / 2,
				//	boilingLava2d.getY());

		// keep decreasing the slider's value
		float value = slider.getValue();
		if (value >= 0) {
			slider.setValue(value - sliderDecreaseSpeed * delta);
		}

		// proceed only when the smoke emitted clears
		if (smokeEmitted == true && smokeEffect.isComplete() && flareActivated == false) {
			
			// begin moving flare2d
			flare2d.setVelocity(0, flare2dYSpeed);
			
			flareActivated = true;
		}
		
		super.render(delta);
	}

	@Override
	public void dispose() {
		super.dispose();
		// dispose of the smoke effect
		smokeEffect.dispose();
		// dispose of the spark effect
		sparkEffect.dispose();
		// dispose of shapeRenderer
		shapeRenderer.dispose();
	}
}
