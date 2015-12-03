package com.me.steel.Screens.GameScreens;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Steel;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.CraftProcesses.WarriorProcess;
import com.me.steel.Domain.CraftProcesses.WizardProcess;
import com.me.steel.Domain.CraftProcesses.CraftActions.MeltAction;
import com.me.steel.Domain.Entities.SmeltPot;
import com.me.steel.Domain.Entities.SmeltTimer;
import com.me.steel.Domain.Entities.TweenAnimations.FightableEntity;
import com.me.steel.Domain.Entities.TweenAnimations.WarriorEntity;
import com.me.steel.Domain.Entities.TweenAnimations.WizardEntity;
import com.me.steel.Screens.AbstractScreen;
import com.me.steel.Services.SoundManager.TyrianSound;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.Timer;
import com.me.steel.Utils.TweenAnimation.Animation;
import com.me.steel.Utils.TweenAnimation.GroupAccessor;
import com.me.steel.Utils.TweenAnimation.ImageAccessor;

public class CampScreen extends AbstractScreen {
	
	private Group group;
	private TweenManager manager;
	private TextButton backButton;
	private TextButton[] craftButton;
	private TextButton[] returnButton;
	private Timer timer;
	
	// disposable resources
	private TextureAtlas skeletonAtlas;
	// every process available
	private List<CraftableProcess> availableProcess;
	
	// customer index
	private int customerI;
	// process currently inside index
	private int processI;
	
	// reference to the process selected at each pot
	private CraftableProcess[] craftProcess;
	private List<Customer> orderList;
	private SmeltPot[] pots;
	private boolean inProcess;
	
	// entities
	private Animation smith;
	private Customer[] customers;
	// references to the entities inside the customers
	// to be sent to the next screen
	private FightableEntity[] heroes;
	
	private int customersLeft;
	
	public CampScreen(Steel game) {
		super(game);
	}

	@Override
	public void show() {
		super.show();
		
		Tween.registerAccessor(Image.class, new ImageAccessor());
		Tween.registerAccessor(Group.class, new GroupAccessor());

		Gdx.gl.glClearColor(31 / 255f, 23 / 255f, 0 / 255f, 1);
		
		skeletonAtlas = new TextureAtlas(Gdx.files.internal("skeleton/image-atlases/skeleton-pages.atlas"));
		
		group = new Group();
		manager = new TweenManager();
		orderList = new ArrayList<Customer>();
		timer = new Timer();
		
		// add the background
		Image background = new Image(getAtlas().findRegion("backgrounds/camp-screen-background"));
		background.setSize(background.getWidth() * 2, background.getHeight() * 2);
		group.addActor(background);
		
		addEntities();
		addUI();
		
		// add the actors to the stage
		group.addActor(smith.getGroup());
		smith.getGroup().toBack();
		
		for (SmeltPot pot : pots) {
			pot.getGroup().setVisible(true);
			group.addActor(pot.getGroup());
			pot.getGroup().toBack();
		}
		for (Customer customer : customers) 
			group.addActor(customer.getEntity().getAnimation().getGroup());
		
		stage.addActor(group);
		stage.addActor(backButton);
		
		timer.start();
	}
	
	@Override
	public void render(float delta) {
		// only updated when out of process
		if (!inProcess) {
			manager.update(delta);
			smith.update();
		}
		
		if (customerI < customers.length) {
			if (timer.getTicks() > 1000) {	
				customers[customerI].setReadyToOder(true);
				customerI++;
				timer.start();
			}
		}
		
		for (int i = 0; i < customers.length; i++) {
			if (!customers[i].gotDelivery()) {
				if (customers[i].isReadyToOrder()) {
					if (i != 0) {
						if (customers[i-1].hasOrdered())
							customers[i].getEntity().update();
					}
					else
						customers[i].getEntity().update();
				}
			}
		}
		
		// update the processes
		for (int i = 0; i < craftProcess.length; i++) {
			if (craftProcess[i] != null) {
				craftProcess[i].update(delta);
				
				if (craftProcess[i].getSmeltTimer().isStarted())
					pots[i].getSmeltTimer().setRotation(craftProcess[i].getSmeltTimer().getRotation());
				
				if (craftProcess[i].isComplete()) {
					craftProcess[i].reset();
					swapToCamp();
					returnButton[i].setVisible(false);
					craftButton[i].setVisible(true);
					
					// change the screen if there are no customers left
					customersLeft--;
					if (customersLeft <= 0) 
						game.setScreen(new BattleScreen(game, getAtlas(), skeletonAtlas, heroes));
				}
			}
		}
		
		super.render(delta);
	}
	
	@Override
	public void dispose() {
		// both atlas'es are disposes in the battle screen
		super.dispose();
		for (CraftableProcess process : availableProcess) process.dispose();
	}
	
	private void addUI() {
		// the back button
		backButton = new TextButton("Back", getSkin());
		backButton.setVisible(false);
		backButton.setY(Gdx.graphics.getHeight() / 2 - backButton.getHeight() / 2);
		backButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				swapToCamp();
						
				if (craftProcess[processI].getCurrentAction() instanceof MeltAction)
					pots[processI].getSmeltTimer().setMeltPoint(craftProcess[processI].getSmeltTimer().getMeltPoint());
			}
		});

		final int potCount = 3;
		final int processPerPot = 2;
		craftProcess = new CraftableProcess[potCount];
		craftButton = new TextButton[potCount];
		returnButton = new TextButton[potCount];
		availableProcess = new ArrayList<CraftableProcess>();
		pots = new SmeltPot[potCount];
				
		// add the pots
		float x = Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / potCount;
		
		for (int i = 0; i < potCount; i++) {
			pots[i] = new SmeltPot(getAtlas(), new SmeltTimer(getAtlas()));
			pots[i].getHatch().setVisible(true);
			pots[i].getGroup().setScale(0.65f);
			pots[i].getGroup().setPosition(x + pots[i].getGroup().getWidth() / 2, Gdx.graphics.getHeight() / 1.85f);
					
			final int temp = i;
					
			// add a table
			final Table table = new Table();
			table.setPosition(x, pots[i].getGroup().getY());
			table.setVisible(false);
			
			// add the craft button
			craftButton[i] = new TextButton("Craft", getSkin());
			craftButton[i].setPosition(
					pots[i].getGroup().getX() - craftButton[i].getWidth(), 
					pots[i].getGroup().getY() + pots[i].getGroup().getHeight() * pots[i].getGroup().getScaleX());
			craftButton[i].addListener(new DefaultActorListener() {
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					table.setVisible(!table.isVisible());
				}
			});
			
			// add the return button
			returnButton[i] = new TextButton("Return", getSkin());
			returnButton[i].setVisible(false);
			returnButton[i].setPosition(
					pots[i].getGroup().getX(), 
					pots[i].getGroup().getY() + pots[i].getGroup().getHeight() * pots[i].getGroup().getScaleX());
			returnButton[i].addListener(new DefaultActorListener() {
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					// swap to the already assigned process
					swapToProcess(temp, table);
				}
			});
			
			for (int j = 0; j < processPerPot; j++) {
				
				// populate the available craft process list
				final CraftableProcess process;
				switch (j) {
				case 0: process = new WarriorProcess(stage, getSkin(), getAtlas(), orderList); break;
				case 1: process = new WizardProcess(stage, getSkin(), getAtlas(), orderList); break;
				default: process = new WarriorProcess(stage, getSkin(), getAtlas(), orderList); break;
				}
				process.setBackButton(backButton);
				availableProcess.add(process);
				
				// start process button
				final TextButton tableButton = new TextButton("Button", getSkin());
				tableButton.addListener(new DefaultActorListener() {
					@Override
					public void touchUp(InputEvent event, float x, float y,
							int pointer, int button) {
						super.touchUp(event, x, y, pointer, button);
						// assign a new process
						craftProcess[temp] = process;
						
						returnButton[temp].setVisible(true);
						craftButton[temp].setVisible(false);
						swapToProcess(temp, table);
					}
				});
								
				table.add(tableButton);
				table.row();
				table.setSize(tableButton.getWidth(), table.getHeight() + tableButton.getHeight());
			}
					
			group.addActor(craftButton[i]);
			group.addActor(returnButton[i]);
			group.addActor(table);
					
			x -= Gdx.graphics.getWidth() / potCount;
		}
	}
	
	private void swapToProcess(int index, Table table) {
		group.remove();
		craftProcess[index].addActionGroup();
		
		stage.addActor(backButton);
		processI = index;
		inProcess = true;
		
		backButton.setVisible(true);
		table.setVisible(false);
	}
	
	private void swapToCamp() {
		inProcess = false;
		craftProcess[processI].removeAcionGroup();
		stage.addActor(group);
		backButton.remove();
	}
	
	private void addEntities() {
		// define the smith
		smith = new Animation("skeleton/xml-skeletons/entities/smith-skeleton.xml", skeletonAtlas);
		smith.setScale(.75f);
		smith.setPosition(MENU_VIEWPORT_WIDTH / 2 - smith.getWidth() / 2, MENU_VIEWPORT_HEIGHT / 1.9f);
		smith.setMovement(Animation.MOVEMENT_IDLE, -1);
			
		// define the heroes
		Random random = new Random();
		String prefix = "skeleton/xml-skeletons/entities/";
		String[] heroTypes = {"warrior", "wizard"};
		float x = Gdx.graphics.getWidth() / 2;
		
		customers = new Customer[random.nextInt(2)+1];
		heroes = new FightableEntity[customers.length];
		customersLeft = customers.length;
		
		for (int i = 0; i < customers.length; i++) {
			final String heroType = heroTypes[random.nextInt(customers.length)];
					
			final FightableEntity entity;
			if (heroType.equals("warrior"))
				entity = new WarriorEntity(prefix + heroType + "-skeleton.xml", skeletonAtlas, getSkin(), 1, 1, 1);
			else
				entity = new WizardEntity(prefix + heroType + "-skeleton.xml", skeletonAtlas, getSkin(), 1, 1, 1);
			
			customers[i] = new Customer();
			customers[i].setEntity(entity);
			heroes[i] = entity;
			Animation animation = entity.getAnimation();
			entity.getAnimation().setPosition(-animation.getGroup().getWidth() * 2, 0);
					
			final int temp = i;
			// ordered sequence definition
			final Sequence orderSequence = new Sequence();;
			orderSequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, false, animation);
			orderSequence.moveTo(
					animation.getGroup().getWidth(),
					-animation.getGroup().getHeight(),
					1, false, animation);
			orderSequence.setPosition(x, -animation.getGroup().getHeight() * 2, true, animation);
			orderSequence.flip(false, animation);
			orderSequence.moveTo(x, 0, 2, false, animation);
			orderSequence.setMovement(Animation.MOVEMENT_IDLE, -1, 0, true, animation);
			orderSequence.start(true);
			
			x += entity.getAnimation().getWidth();
			
			// hero order button
			final TextButton orderButton = new TextButton("Order", getSkin());
			orderButton.setSize(100, 50);
			orderButton.setPosition(
					entity.getAnimation().getGroup().getWidth() / 2 - orderButton.getWidth() / 2, 
					entity.getAnimation().getGroup().getHeight() + orderButton.getHeight() / 2);
			orderButton.setVisible(false);
			orderButton.addListener(new DefaultActorListener() {
				@Override
				public void touchUp(InputEvent event, float x, float y,
						int pointer, int button) {
					super.touchUp(event, x, y, pointer, button);
					game.getSoundManager().play(TyrianSound.CLICK);
					orderList.add(customers[temp]);
					customers[temp].setOrdered(true);
					orderButton.setVisible(false);
					entity.addSequence(orderSequence);
				}
			});
					
			entity.getAnimation().getGroup().addActor(orderButton);
					
			// approach sequence definition
			Sequence sequence = new Sequence();
			sequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, false, entity.getAnimation());
			sequence.moveTo(entity.getAnimation().getGroup().getWidth(), 0, 1, false, entity.getAnimation());
			sequence.setVisible(true, true, orderButton);
			sequence.setMovement(Animation.MOVEMENT_IDLE, -1, 0, true, entity.getAnimation());
			sequence.start(true);
			entity.addSequence(sequence);
		}
	}
}
