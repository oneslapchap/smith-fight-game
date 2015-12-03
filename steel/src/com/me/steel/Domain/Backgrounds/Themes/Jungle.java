package com.me.steel.Domain.Backgrounds.Themes;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.me.steel.Steel;
import com.me.steel.Domain.Backgrounds.Objects.Cloud;
import com.me.steel.Domain.Backgrounds.Objects.Leaf;
import com.me.steel.Domain.Backgrounds.Objects.Mountain;
import com.me.steel.Domain.Backgrounds.Objects.Tree;
import com.me.steel.Domain.Backgrounds.Objects.Vine;
import com.me.steel.Domain.Entities.TweenAnimations.EnemyEntity;
import com.me.steel.Domain.Entities.TweenAnimations.FightableEntity;
import com.me.steel.Domain.Enums.Skill;
import com.me.steel.Utils.OrthoCamController;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.TweenAnimation.Animation;
import com.me.steel.Utils.TweenAnimation.CameraAccessor;
import com.me.steel.Utils.TweenAnimation.GroupAccessor;
import com.me.steel.Utils.TweenAnimation.ImageAccessor;
import com.me.steel.Utils.TweenAnimation.SpriteAccessor;

public class Jungle {
	
	private ParallaxCamera camera;
	private OrthoCamController controller;
	private Texture fgTexture;
	private Sprite fgSprite;
	private Mountain[] mountain;
	private Cloud[] cloud;
	private Leaf[] leaf;
	private Tree[] tree;
	private Vine[] vine;
	
	private Random random;
	
	private TextureAtlas skeletonAtlas;
	private TextureAtlas defaultAtlas;
	private Skin skin;
	private Stage stage;
	private SpriteBatch batch;
	
	private FightableEntity[] heroes;
	private FightableEntity[] enemies;
	
	private TweenManager manager;
	
	private Slider enemySlider;
	
	private int currentEnemy;
	private int currentLeaf;
	private int maxLeafs;
	private int currentVine;
	private int maxVines;
	private boolean basePosSet;
	
	private float prevCamPos;
	
	// convenience variables
	private float fgSpriteW;
	private float halfScreenW;
	private float halfScreenH;
	
	public Jungle(Steel game, TextureAtlas defaultAtlas, TextureAtlas skeletonAtlas, FightableEntity[] heroes) {
		
		Tween.registerAccessor(Image.class, new ImageAccessor());
		Tween.registerAccessor(Group.class, new GroupAccessor());
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Tween.registerAccessor(ParallaxCamera.class, new CameraAccessor());
		
		this.skeletonAtlas = skeletonAtlas;
		this.defaultAtlas = defaultAtlas;
		this.heroes = heroes;
		
		manager = new TweenManager();
		
		// define the camera
		camera = new ParallaxCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		controller = new OrthoCamController(camera);
		//Gdx.input.setInputProcessor(controller);
		
		random = new Random();
		Gdx.gl.glClearColor(0.4f, 0.7f, 1f, 1f);
		
		halfScreenW = Gdx.graphics.getWidth() / 2;
		halfScreenH = Gdx.graphics.getHeight() / 2;
		
		initResources();
		addEntities();
		addThemeObjects();
		addUI();
		
		for (FightableEntity entity : heroes) entity.skillsToFront();
		for (FightableEntity entity : enemies) entity.skillsToFront();
	}
	
	public void update(float delta) {
		
		for (FightableEntity entity : heroes) entity.update();
		enemies[currentEnemy].update();
		
		if (enemies[currentEnemy].isDead()) {
			if (currentEnemy + 1 < enemies.length) {
				if (!basePosSet) {
					
					for (FightableEntity entity : heroes) {
						entity.setBasePosition(
								enemies[currentEnemy + 1].getAnimation().getX() - entity.getAnimation().getWidth() * 4,
								entity.getBasePosY());
						
						entity.getAnimation().setMovement(Animation.MOVEMENT_WALK, -1);
						
						Tween.to(entity.getAnimation().getGroup(), GroupAccessor.POS_XY, 3)
						.target(entity.getBasePosX(), entity.getBasePosY())
						.start(manager);
					}
					
					Tween.to(camera, CameraAccessor.POS_XY, 2)
					.target(heroes[0].getBasePosX() - 100 + halfScreenW, camera.position.y)
					.start(manager);
					
					basePosSet = true;
				}
				
				if (enemies[currentEnemy].getBasePosX() + enemies[currentEnemy].getAnimation().getWidth() * 1.5f < camera.position.x - halfScreenW)
					currentEnemy++;
			}
		}
		
		if (basePosSet) {
			manager.update(delta);
			fgSprite.setPosition(camera.position.x - halfScreenW, camera.position.y - halfScreenH);
			
			if (leaf[currentLeaf].getX() + leaf[currentLeaf].getWidth() * 1.5f < camera.position.x - halfScreenW * 2) {
				if (currentLeaf + 1 < leaf.length)
					currentLeaf++;
			}
			
			if (vine[currentVine].getX() + vine[currentVine].getWidth() * 1.5f < camera.position.x - halfScreenW * 2) {
				if (currentVine + 1 < vine.length)
					currentVine++;
			}
			
			fgSprite.setU(fgSprite.getU() + calculateChange());
			fgSprite.setU2(fgSprite.getU()+1);
			prevCamPos = camera.position.x;
			
			if (manager.getRunningTweensCount() == 0) {	
				for (FightableEntity entity : heroes) entity.setTarget(enemies[currentEnemy]);
				enemies[currentEnemy].setTarget(heroes[0]);
				basePosSet = false;
			}
		}
		
		enemySlider.setValue(enemies[currentEnemy].getHealth());
		
		stage.act(delta);
		
		for (int i = 0; i < cloud.length; i++)
			cloud[i].update();
		
		for (int i = currentLeaf; i < currentLeaf + maxLeafs; i++) {
			if (i < leaf.length) 
				leaf[i].update();
			else break;
		}
		
		for (int i = currentVine; i < currentVine + maxVines; i++) {
			if (i < vine.length)
				vine[i].update();
			else break;
		}
	}
	
	public void draw() {
		
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// background layer
		batch.setProjectionMatrix(camera.calculateParallaxMatrix(0, 1));
		batch.begin();
		
		for (int i = 0; i < cloud.length; i++)
			cloud[i].draw(batch);
		
		for (int i = 0; i < mountain.length; i++)
			mountain[i].draw(batch);
		
		batch.end();
		
		// mid'ground layer
		batch.setProjectionMatrix(camera.calculateParallaxMatrix(0.5f, 1));
		batch.begin();
		for (int i = currentLeaf; i < currentLeaf + maxLeafs; i++) {
			if (i < tree.length) {
				tree[i].draw(batch);
				leaf[i].draw(batch);
			}
			else break;
		}
			
		batch.end();
		
		// foreground layer
		batch.setProjectionMatrix(camera.calculateParallaxMatrix(1f, 1));
		batch.begin();
		
		fgSprite.draw(batch);
		
		for (int i = currentVine; i < currentVine + maxVines; i++) {
			if (i < vine.length)
				vine[i].draw(batch);
			else break;
		}
		
		enemies[currentEnemy].draw(batch);
		for (FightableEntity entity : heroes) entity.draw(batch);
		
		stage.draw();
		
		batch.end();
		
		// make sure the batch has ended
		//attacker.drawDebug(camera);
		//attacker.drawBodyPartDebug(camera);
	}
	
	public void dispose() {
		fgTexture.dispose();
		skeletonAtlas.dispose();
		defaultAtlas.dispose();
		skin.dispose();
		stage.dispose();
		batch.dispose();
	}
	
	private float calculateChange() {		
		return (camera.position.x - prevCamPos) / fgSpriteW;
	}
	
	private void initResources() {
		// define the foreground texture
		fgTexture = new Texture(Gdx.files.internal("foregrounds/jungle-foreground.png"));
		fgTexture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		fgSprite = new Sprite(fgTexture);
		fgSprite.setPosition(-fgSprite.getRegionWidth() / 2, -halfScreenH);
		fgSpriteW = fgSprite.getWidth();

		skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		batch = new SpriteBatch();
	}
	
	private void addThemeObjects() {
		maxLeafs = 5;
		maxVines = 5;
		
		mountain = new Mountain[random.nextInt(4-2)+2];
		for (int i = 0; i < mountain.length; i++) {
			float x = random.nextInt(Gdx.graphics.getWidth());
			
			mountain[i] = new Mountain(defaultAtlas);
			mountain[i].getSprite().flip(random.nextBoolean(), false);
			mountain[i].getSprite().setPosition(x - halfScreenW, 100 - halfScreenH);
		}
		
		cloud = new Cloud[random.nextInt(5-2)+2];
		for (int i = 0; i < cloud.length; i++) {
			cloud[i] = new Cloud(defaultAtlas);
		}
		
		tree = new Tree[(random.nextInt(6-3)+3) * enemies.length];
		
		for (int i = 0, x = random.nextInt(Gdx.graphics.getWidth()); i < tree.length; i++, x += random.nextInt(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / maxLeafs) + Gdx.graphics.getWidth() / maxLeafs)  {
			
			tree[i] = new Tree(defaultAtlas);
			tree[i].getSprite().setPosition(x - halfScreenW, random.nextInt(25) - halfScreenH);
		}
		
		leaf = new Leaf[tree.length];
		for (int i = 0; i < leaf.length; i++) {
			leaf[i] = new Leaf(defaultAtlas);
			leaf[i].setPosition(tree[i].getSprite().getX(), halfScreenH - leaf[i].getHeight() / 1.5f);
		}
		
		vine = new Vine[(random.nextInt(6-3)+3) * enemies.length];
		for (int i = 0, x = random.nextInt(Gdx.graphics.getWidth()); i < vine.length; i++, x += random.nextInt(Gdx.graphics.getWidth() - Gdx.graphics.getWidth() / maxVines) + Gdx.graphics.getWidth() / maxVines) {
			
			vine[i] = new Vine(defaultAtlas);
			vine[i].setPosition(x - halfScreenW, halfScreenH - vine[i].getHeight() / 1.5f);
		}
	}
	
	private void addEntities() {
						
		// create some entities 
		float scale = 1f;
		String prefix = "skeleton/xml-skeletons/entities/";
						
		// set up the main character
		for (FightableEntity entity : heroes) {
			entity.getAnimation().setScale(0.8f);
			entity.getAnimation().setPosition(
					150 - halfScreenW - (random.nextInt(100-50)+50),
					50 - halfScreenH + random.nextInt(50-15)+15);
			entity.setHealth(200);
			entity.getAnimation().flipAnimation(true, false);
			
			// add skills
			entity.addSkill(Skill.ATTACKER_MELEE, skin, skeletonAtlas);
		}
		
		// sort the heroes depending on the y
		for (int i = 0; i < heroes.length-1; i++) {
			for (int j = i+1; j < heroes.length; j++) {
				FightableEntity temp;
				if (heroes[i].getAnimation().getY() < heroes[j].getAnimation().getY()) {
					temp = heroes[i];
					heroes[i] = heroes[j];
					heroes[j] = temp;
				}
			}
		}
		
		// add enemies
		enemies = new FightableEntity[2];
		enemies[0] = new EnemyEntity(prefix + "spitter-skeleton.xml", skeletonAtlas, skin, 1f, 1f, 1f);
		enemies[0].getAnimation().setScale(scale);
		enemies[0].getAnimation().setMovement(0, -1);
		enemies[0].getAnimation().setPosition(Gdx.graphics.getWidth() - enemies[0].getAnimation().getWidth() * 1.25f - halfScreenW, 50 - halfScreenH);
		enemies[0].setHealth(200);
						
		enemies[1] = new EnemyEntity(prefix + "droid-skeleton.xml", skeletonAtlas, skin, 1f, 1f, 1f);
		enemies[1].getAnimation().setScale(scale);
		enemies[1].getAnimation().setMovement(0, -1);
		enemies[1].getAnimation().setPosition(enemies[0].getAnimation().getX() + enemies[0].getAnimation().getWidth() * 1.5f + Gdx.graphics.getWidth(), 50 - halfScreenH);
		enemies[1].setHealth(200);
		
		for (FightableEntity entity : heroes )
			entity.setBasePosition(
					entity.getAnimation().getX(), 
					entity.getAnimation().getY());
		
		enemies[0].setBasePosition(enemies[0].getAnimation().getX(), enemies[0].getAnimation().getY());
		enemies[1].setBasePosition(enemies[1].getAnimation().getX(), enemies[1].getAnimation().getY());
		
		// add the enemy as the target for our hero and vice versa
		for (FightableEntity entity : heroes) {
			entity.setTarget(enemies[currentEnemy]);
			entity.addAttackSequence();
		}
		
		for (FightableEntity entity : enemies) {
			entity.setTarget(heroes[0]);
			entity.addAttackSequence();
		}
	}
	
	private void addUI() {
		enemySlider = new Slider(0, enemies[currentEnemy].getHealth(), 1, false, skin);
		enemySlider.setWidth(halfScreenW - 100);
		enemySlider.setPosition(halfScreenW + 100, 0);
		enemySlider.setTouchable(Touchable.disabled);
		enemySlider.setAnimateDuration(1);
		enemySlider.setValue(enemies[currentEnemy].getHealth());
				
		stage.addActor(enemySlider);
		for (FightableEntity entity : heroes)
			stage.addActor(entity.getSkillTable());
		
		Gdx.input.setInputProcessor(stage);
	}
}