package com.me.steel.Domain.Entities.TweenAnimations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Skill;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.Timer;
import com.me.steel.Utils.TweenAnimation.Animation;
import com.me.steel.Utils.TweenAnimation.BodyPart;

public abstract class HeroEntity extends AbstractEntity implements FightableEntity {
	
	// instantiated in a sub class with a specific weapon type
	protected CraftableWeapon craftableWeapon;
	
	private List<SkillEntity> skillEntityList;
	private Table skillTable;
	
	private int health;
	private float mana;
	private float attackPower;
	private float magicPower;
	private float defensePower;
	
	private Vector2 basePosition;

	private Sequence sequence;
	private FightableEntity target;
	private boolean skillInProgress;
	private boolean readyToAttack;
	private boolean dead;
	private Timer timer;
	private Image pivot;
	private float waitFor;
	private Random random;
	
	public HeroEntity(String xmlPath, TextureAtlas textureAtlas, Skin skin, float red, float green, float blue) {
		super(xmlPath, textureAtlas);
		
		basePosition = new Vector2();
		
		skillEntityList = new ArrayList<SkillEntity>();
		animation.setColor(red, green, blue);
		
		skillTable = new Table();
		skillTable.left().top();
		skillTable.debug();
		
		sequence = new Sequence();
		timer = new Timer();
		random = new Random();
		
		// find the "hand" body part of the skeleton if there is one
		int i = 0;
		Iterator<BodyPart> iterator = animation.getAnimationDataList().get(0).getMovements().get(0).getBodyParts().iterator();
		while (iterator.hasNext()) {
			BodyPart bodyPart = (BodyPart) iterator.next();
			if (bodyPart.getName().equals("hand")) {
				pivot = animation.getImageList().get(i);
				break;
			}
			i++;
		}
		
		// in case the body part is not found
		if (pivot == null)
			pivot = animation.getImageList().get(0);
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		Iterator<SkillEntity> iterator = skillEntityList.iterator();
		while (iterator.hasNext()) {
			SkillEntity skillEntity = (SkillEntity) iterator.next();
			skillEntity.draw(batch);
		}
	}
	
	@Override
	public void update() {
		super.update();
		
		// reset the indicator
		skillInProgress = false;
		
		// update skills
		Iterator<SkillEntity> iterator = skillEntityList.iterator();
		while (iterator.hasNext()) {
			SkillEntity skillEntity = (SkillEntity) iterator.next();
			skillEntity.update();
			
			if (skillEntity.inProgress() == true)
				skillInProgress = true;
		}
		
		if (target != null) {
			if (dead == false && target.isDead() == false) {
				if (skillInProgress == false && target.isSkillInProgress() == false)
					checkForAttack();
				sequence.update();
				
				if (health <= 0) {
					animation.setMovement(Animation.MOVEMENT_DIE, 1);
					dead = true;
				}
			}
		}
		
		craftableWeapon.update();
	}
	
	private void checkForAttack() {
		if (sequence.endReached() == true) {
			if (timer.isStarted() == false) {
				waitFor = random.nextInt(2000-500)+500;
				timer.start();
				readyToAttack = false;
			}
			
			if (timer.getTicks() >= waitFor && target.isReadyToAttack() == false) {
				timer.stop();
				readyToAttack = true;
				sequence.start(true);
			}
		}
	}
	
	@Override
	public void drawDebug(ParallaxCamera camera) {
		super.drawDebug(camera);
		
		Iterator<SkillEntity> iterator = skillEntityList.iterator();
		while (iterator.hasNext()) {
			SkillEntity skillEntity = (SkillEntity) iterator.next();
			
			if (skillEntity.getAnimation().getImageList().get(0).isVisible() == true)
				skillEntity.drawDebug(camera);
		}
	}
	
	@Override
	public void addSkill(final Skill skill, Skin skin, TextureAtlas textureAtlas) {
		
		final SkillEntity skillEntity = new SkillEntity(skill, textureAtlas, this);
		skillEntityList.add(skillEntity);
		
		TextButton textButton = new TextButton(skill.getSimpleName(), skin);
		skillTable.add(textButton).size(75f);
		skillTable.setPosition(0, Gdx.graphics.getHeight());
		textButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
					skillEntity.performSkill();
			}
		});
	}
	
	@Override
	public void addAttackSequence() {		
		
		for (SkillEntity skill : skillEntityList)
			skill.addSkillSequence();
		
		sequence.setTarget(target);
		sequence.setCaster(this);
		
		sequence.removeAllElements();
		sequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, false, animation);
		sequence.moveToTarget(1, false, animation);
		sequence.setMovement(Animation.MOVEMENT_ATTACK, 1, 0, true, animation);
		sequence.setHealth(30, true);
		sequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, true, animation);
		sequence.returnToBase(1, false, animation);
		sequence.setMovement(Animation.MOVEMENT_IDLE, -1, 0, true, animation);
		sequence.startFromLast();
	}
	
	@Override
	public void setTarget(FightableEntity target) {
		this.target = target;
		sequence.setTarget(target);
		for (SkillEntity skill : skillEntityList) skill.setTarget(target);
	}
	
	@Override
	public Table getSkillTable() {
		return skillTable;
	}
	
	@Override
	public Image getPivot() {
		return pivot;
	}
	
	@Override
	public void addSequence(Sequence sequence) {
		super.addSequence(sequence);
	}
	
	@Override
	public float getBasePosX() {
		return basePosition.x;
	}
	
	@Override
	public float getBasePosY() {
		return basePosition.y;
	}
	
	@Override
	public boolean isDead() {
		return dead;
	}
	
	@Override
	public boolean isSkillInProgress() {
		return skillInProgress;
	}
	
	@Override
	public boolean isReadyToAttack() {
		return readyToAttack;
	}
	
	@Override
	public Sequence getSequence() {
		return sequence;
	}
	
	@Override
	public int getHealth() {
		return health;
	}

	@Override
	public float getMana() {
		return mana;
	}

	@Override
	public float getAttackPower() {
		return attackPower;
	}

	@Override
	public float getMagicPower() {
		return magicPower;
	}

	@Override
	public float getDefensePower() {
		return defensePower;
	}
	
	@Override
	public CraftableWeapon getWeapon() {
		return craftableWeapon;
	}
	
	@Override
	public void addWeapon() {
		craftableWeapon.addToGroup(animation.getGroup());
	}
	
	@Override
	public void setBasePosition(float x, float y) {
		basePosition.x = x;
		basePosition.y = y;
	}
	
	@Override
	public void setHealth(int health) {
		this.health = health;
	}

	@Override
	public void setMana(float mana) {
		this.mana = mana;
	}

	@Override
	public void setAttackPower(float attackPower) {
		this.attackPower = attackPower;
	}

	@Override
	public void setMagicPower(float magicPower) {
		this.magicPower = magicPower;
	}

	@Override
	public void setDefensePower(float defensePower) {
		this.defensePower = defensePower;
	}
	
	@Override
	public void skillsToFront() {
		Iterator<SkillEntity> iterator = skillEntityList.iterator();
		while (iterator.hasNext()) {
			SkillEntity skillEntity = (SkillEntity) iterator.next();
			
			skillEntity.getAnimation().getGroup().toFront();
		}
	}
}
