package com.me.steel.Domain.Entities.TweenAnimations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Skill;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.TweenAnimation.Animation;

public class SkillEntity extends AbstractEntity {
	
	// possible skill behavior types
	public static final int GET_CLOSE 				= 1;
	public static final int CAST 					= 2;
	public static final int CAST_APPEAR_AT_TARGET 	= 3;
	
	// current behavior type
	private int behaviour;
	private Skill skill;
	private boolean skillInProgress;
	
	private FightableEntity target;
	private FightableEntity caster;
	// caster and target animations for convenience
	private Animation casterAnim;
	private Animation targetAnim;
	
	private Sequence sequence;
	
	public SkillEntity(Skill skill, TextureAtlas textureAtlas, FightableEntity caster) {
		super("skeleton/xml-skeletons/skills/" + skill.getSkeletonXmlName(), textureAtlas);
		
		this.skill = skill;
		this.caster = caster;
		casterAnim = caster.getAnimation();
		animation.setVisible(false);
		setBehaviour(skill.getSkillType());
		
		sequence = new Sequence();
	}
	
	public void setBehaviour(int behaviour) {
		this.behaviour = behaviour;
	}
	
	@Override
	public void update() {
		super.update();
		
		if (skillInProgress == true && caster.getSequence().endReached() == true && target.getSequence().endReached() == true) {
			sequence.update();
			if (sequence.hasEnded() == true) {
				skillInProgress = false;
				Gdx.app.log("", "DONE");
			}	
		}
	}
	
	public void performSkill() {
		if (skillInProgress == false) {
			
			skillInProgress = true;
			sequence.start(true);
			
			Gdx.app.log(Steel.LOG, "Performing skill");
		}
	}
	
	public void addSkillSequence() {
		targetAnim = target.getAnimation();
		sequence.removeAllElements();
		
		switch (behaviour) {
		case GET_CLOSE:
			
			sequence.setTarget(target);
			sequence.setCaster(caster);
			
			sequence.setVisible(false, false, animation);
			sequence.setScale(2, animation);
			sequence.positionToTarget(targetAnim.getWidth() / 2 - animation.getWidth(), 0, false, animation);
			sequence.setMovement(Animation.MOVEMENT_WALK, -1, 1, false, casterAnim);
			sequence.moveToTarget(1, false, casterAnim);
			sequence.setMovement(Animation.MOVEMENT_ATTACK, 1, 0, true, casterAnim);
			sequence.setVisible(true, true, animation);
			sequence.setMovement(0, 1, 1, false, animation);
			sequence.setHealth(skill.getDamage(), true);
			sequence.setVisible(false, true, animation);
			sequence.setMovement(Animation.MOVEMENT_WALK, -1, 1, true, casterAnim);
			sequence.returnToBase(1, false, casterAnim);
			sequence.setMovement(Animation.MOVEMENT_IDLE, 1, 1, true, casterAnim);
			sequence.setScale(0.5f, animation);
			
			break;
		}
	}
	
	public void setTarget(FightableEntity target) {
		this.target = target;
		sequence.setTarget(target);
	}
	
	public boolean inProgress() {
		return skillInProgress;
	}
	
	public Sequence getSequence() {
		return sequence;
	}
}
