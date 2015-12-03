package com.me.steel.Domain.Entities.TweenAnimations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Skill;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.Timer;
import com.me.steel.Utils.TweenAnimation.Animation;

public class EnemyEntity extends AbstractEntity implements FightableEntity {
	
	private List<SkillEntity> skillEntityList;
	private CraftableWeapon craftableWeapon;
	
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
	
	public EnemyEntity(String xmlPath, TextureAtlas textureAtlas, Skin skin, float red, float green, float blue) {
		super(xmlPath, textureAtlas);
		
		basePosition = new Vector2();
		
		skillEntityList = new ArrayList<SkillEntity>();
		animation.setColor(red, green, blue);
		
		sequence = new Sequence();
		timer = new Timer();
	}
	
	@Override
	public void update() {
		super.update();
		
		if (craftableWeapon != null)
			craftableWeapon.update();
		
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
	
	private void checkForAttack() {
		if (sequence.endReached() == true) {
			if (timer.isStarted() == false) {
				timer.start();
				readyToAttack = false;
			}
			
			if (timer.getTicks() / 5000 >= 1 && target.isReadyToAttack() == false) {
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
		
		if (craftableWeapon != null)
			craftableWeapon.drawDebug(animation.getGroup(), camera);
	}
	
	@Override
	public void addSkill(Skill skill, Skin skin, TextureAtlas textureAtlas) {
		skillEntityList.add(new SkillEntity(skill, textureAtlas, this));
	}
	
	@Override
	public void setTarget(FightableEntity target) {
		this.target = target;	
	}
	
	@Override
	public Table getSkillTable() {
		return null;
	}
	
	@Override
	public void addAttackSequence() {
		Iterator<SkillEntity> iterator = skillEntityList.iterator();
		while (iterator.hasNext()) {
			SkillEntity skillEntity = (SkillEntity) iterator.next();
			skillEntity.setTarget(target);
		}
		
		sequence.setTarget(target);
		sequence.removeAllElements();
		sequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, false, animation);
		sequence.moveToTarget(1, false, animation);
		sequence.setMovement(Animation.MOVEMENT_ATTACK, 1, 0, true, animation);
		sequence.setHealth(10, true);
		sequence.setMovement(Animation.MOVEMENT_WALK, -1, 0, true, animation);
		sequence.moveTo(basePosition.x, basePosition.y, 1, false, animation);
		sequence.setMovement(Animation.MOVEMENT_IDLE, -1, 0, true, animation);
		sequence.startFromLast();
	}
	
	@Override
	public void addWeapon() {}
	
	@Override
	public Image getPivot() {
		return null;
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
	public CraftableWeapon getWeapon() {
		return craftableWeapon;
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
