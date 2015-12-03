package com.me.steel.Domain.Entities.TweenAnimations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Enums.Skill;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.TweenAnimation.Animation;

public interface FightableEntity {
	
	public void update();
	public void draw(SpriteBatch batch);
	public Animation getAnimation();
	public boolean isReadyToAttack();
	public boolean isSkillInProgress();
	public boolean isDead();
	public float getBasePosX();
	public float getBasePosY();
	public int getHealth();
	public float getMana();
	public float getAttackPower();
	public float getMagicPower();
	public float getDefensePower();
	public CraftableWeapon getWeapon();
	public Sequence getSequence();
	public Image getPivot();
	public Table getSkillTable();
	public void setTarget(FightableEntity target);
	public void setBasePosition(float x, float y);
	public void setHealth(int health);
	public void setMana(float mana);
	public void setAttackPower(float attackPower);
	public void setMagicPower(float magicPower);
	public void setDefensePower(float defensePower);
	public void addSequence(Sequence sequence);
	/** Adds the weapons group to the entities group */
	public void addWeapon();
	public void addSkill(final Skill skill, Skin skin, TextureAtlas textureAtlas);
	public void skillsToFront();
	/** Adds a predefined default attack sequence.
	 * It is assumed that the target is set before calling this function.
	 */
	public void addAttackSequence();
}
