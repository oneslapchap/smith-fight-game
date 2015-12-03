package com.me.steel.Screens.GameScreens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Screens.Scene2d.GrindWheel2D;
import com.me.steel.Screens.Scene2d.Outline2D;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.ParticleEffectActor;
import com.me.steel.Utils.Stats;

public class GrindScreen extends AbstractScreen {

	private Stats stats;
	private Outline wep;
	private Material material;
	private final Slider grindSlider;

	private float grindSliderSpeed;
	private boolean start;
	private float scale;
	private Outline2D outline2d;
	private Label multiLabel;
	private int timesToGrind;
	private int timesGrinded;
	private TextButton nextButton;
	private Button rightButton;
	private Button leftButton;
	private ParticleEffect slimSparkEffect;
	private ParticleEffectActor slimSparEffectActor;

	public GrindScreen(Steel game, Stats stats, Outline wep, Material material) {
		super(game);

		this.stats = stats;
		this.wep = wep;
		this.material = material;
		grindSlider = new Slider(0f, 1f, 0.001f, false, getSkin());
	}

	@Override
	public void show() {
		super.show();

		// how many times to grind the outline
		timesToGrind = 5;

		// grindSliders knob movement speed
		grindSliderSpeed = 0.25f;

		// keeps track of whether to start spinning the wheel
		start = false;

		// set up the scale
		scale = 2f;

		// set up grindSlider
		grindSlider.setWidth(MENU_VIEWPORT_WIDTH / 2);
		grindSlider.setPosition(
				MENU_VIEWPORT_WIDTH / 2 - grindSlider.getWidth() / 2,
				MENU_VIEWPORT_HEIGHT - grindSlider.getHeight());
		grindSlider.setTouchable(Touchable.disabled);
		grindSlider.setAnimateDuration(1);

		// set up the grinding wheel
		final GrindWheel2D grindWheel2d = new GrindWheel2D(getAtlas());
		grindWheel2d.setPosition(
				MENU_VIEWPORT_WIDTH / 2 - grindWheel2d.getWidth() / 2,
				MENU_VIEWPORT_HEIGHT / 2 - grindWheel2d.getHeight() / 2);

		// set up right screen button
		rightButton = new Button(getSkin());
		rightButton.setSize(MENU_VIEWPORT_WIDTH / 2, MENU_VIEWPORT_HEIGHT);
		rightButton.setPosition(MENU_VIEWPORT_WIDTH / 2, 0);
		rightButton.setColor(0f, 0f, 0f, 0f);
		rightButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				outline2d.setX(outline2d.getX() + outline2d.getHeight()
						/ timesToGrind);

				// update the times grinded count
				timesGrinded++;

				if (slimSparkEffect.isComplete())
					slimSparkEffect.start();
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				// begin the simulation
				start = true;

				// restart the grindSlider
				grindSlider.setValue(0f);

				return true;
			}
		});

		// set up left screen button
		leftButton = new Button(getSkin());
		leftButton.setSize(MENU_VIEWPORT_WIDTH / 2, MENU_VIEWPORT_HEIGHT);
		leftButton.setPosition(0, 0);
		leftButton.setColor(0f, 0f, 0f, 0f);
		// set it unavailable at first
		leftButton.setVisible(false);
		leftButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);

				outline2d.setX(outline2d.getX() - outline2d.getHeight()
						/ timesToGrind);
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {

				// begin the simulation
				start = true;

				return true;
			}
		});

		// set up the outline
		outline2d = new Outline2D(getAtlas(), wep, false);
		outline2d.setSize(outline2d.getWidth() * scale, outline2d.getHeight()
				* scale);
		outline2d.setInitialPosition(
				grindWheel2d.getX() - outline2d.getHeight(),
				MENU_VIEWPORT_HEIGHT / 2);
		outline2d
				.setColor(material.getRedColorValue(),
						material.getGreenColorValue(),
						material.getBlueColorValue(), 1f);
		// set the initial rotation
		outline2d.setRotation(-90);

		// set up slim spark effect
		slimSparkEffect = new ParticleEffect();
		slimSparkEffect.load(Gdx.files.internal("particles/slim-sparks.p"),
				Gdx.files.internal("particles"));
		slimSparEffectActor = new ParticleEffectActor(slimSparkEffect);
		// set the position relative to the outline2d
		slimSparEffectActor.setInitialPosition(MENU_VIEWPORT_WIDTH / 2,
				outline2d.getY() - outline2d.getWidth() / 2);

		// set up multiLabel
		multiLabel = new Label("", getSkin());
		multiLabel.setPosition(grindSlider.getX(), grindSlider.getY()
				- multiLabel.getHeight());

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
				game.setScreen(new PowerSourceScreen(game, stats, wep, material));
			}
		});

		// add actors to the stage
		stage.addActor(grindWheel2d);
		stage.addActor(slimSparEffectActor);
		stage.addActor(outline2d);
		stage.addActor(rightButton);
		stage.addActor(leftButton);
		stage.addActor(multiLabel);
		stage.addActor(grindSlider);
		stage.addActor(nextButton);
	}

	@Override
	public void render(float delta) {
		// update
		if (start == true) {
			float value = grindSlider.getValue();
			updateSlider(value, delta);
			updateMultiplier(value, delta);

			// make nextButton visible when the outline has been grinded
			if (timesGrinded == timesToGrind) {
				nextButton.setVisible(true);

				// make rightButton and leftButton unavailable
				rightButton.setTouchable(Touchable.disabled);
				leftButton.setTouchable(Touchable.disabled);

				// reset the slider
				grindSlider.setValue(0f);

				// reset label
				multiLabel.setText("");

				slimSparEffectActor.setVisible(false);
				
				// make sure this code isn't accessed repeatedly
				start = false;
			}
		}

		super.render(delta);
	}

	private void updateMultiplier(float value, float delta) {
		if (value < 0.5f) {
			multiLabel.setText("Ok");

			// increase the stats
			stats.setAttackPower(stats.getAttackPower() + 1 * delta);
			stats.setMagicPower(stats.getMagicPower() + 1 * delta);
			stats.setDefensePower(stats.getDefensePower() + 1 * delta);
		} else if (value > 0.5f && value < 0.7f) {
			multiLabel.setText("Good");

			// increase the stats
			stats.setAttackPower(stats.getAttackPower() + 2 * delta);
			stats.setMagicPower(stats.getMagicPower() + 2 * delta);
			stats.setDefensePower(stats.getDefensePower() + 2 * delta);
		} else if (value > 0.7f && value < 0.9f) {
			multiLabel.setText("Perfect");

			// increase the stats
			stats.setAttackPower(stats.getAttackPower() + 3 * delta);
			stats.setMagicPower(stats.getMagicPower() + 3 * delta);
			stats.setDefensePower(stats.getDefensePower() + 3 * delta);
		} else {
			multiLabel.setText("Bad");

			// decrease the stats
			float attackPower = stats.getAttackPower();
			float magicPower = stats.getMagicPower();
			float defensePower = stats.getDefensePower();

			// don't let the stats fall below zero
			if (attackPower > 0 && magicPower > 0 && defensePower > 0) {
				stats.setAttackPower(attackPower - 10 * delta);
				stats.setMagicPower(magicPower - 10 * delta);
				stats.setDefensePower(defensePower - 10 * delta);
			}
		}
	}

	private void updateSlider(float value, float delta) {
		grindSlider.setValue(value + grindSliderSpeed * delta);
	}

	@Override
	public void dispose() {
		super.dispose();
		// dispose of slimSparkEffect
		slimSparkEffect.dispose();
	}
}
