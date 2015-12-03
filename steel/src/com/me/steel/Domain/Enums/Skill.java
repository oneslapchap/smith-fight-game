package com.me.steel.Domain.Enums;

import com.me.steel.Domain.Entities.TweenAnimations.SkillEntity;

public enum Skill {
	
	ATTACKER_MELEE("Attacker melee", "blast-skeleton.xml", 10, SkillEntity.GET_CLOSE), 
	DEFENDER_MELEE("Defender melee", null, 20, SkillEntity.GET_CLOSE),
	CASTER_MELEE("Caster melee", null, 30, SkillEntity.GET_CLOSE),
	SKILL_FIREBALL("Skill fireball", "fireball-skeleton.xml", 40, SkillEntity.GET_CLOSE),
	SKILL_ICE_PRISON("Skill ice prison", "ice-prison-skeleton.xml", 50, SkillEntity.CAST_APPEAR_AT_TARGET);

	private final String name;
	private final String xmlPath;
	private final int damage;
	private final int skillType;
	
	private Skill(String name, String xmlPath, int damage, int skillType) {
		this.name = name;
		this.xmlPath = xmlPath;
		this.damage = damage;
		this.skillType = skillType;
	}

	public String getName() {
		return name;
	}

	public String getSimpleName() {
		return name().replaceAll("_", "-").toLowerCase();
	}
	
	public int getDamage() {
		return damage;
	}
	
	public String getSkeletonXmlName() {
		return xmlPath;
	}
	
	public int getSkillType() {
		return skillType;
	}
}
