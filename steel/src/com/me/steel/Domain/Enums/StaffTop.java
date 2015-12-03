package com.me.steel.Domain.Enums;

public enum StaffTop implements Item {
	
	ORB("Orb", 10, 10, 10, Skill.SKILL_ICE_PRISON), 
	SKULL("Skull", 20, 20, 20, Skill.SKILL_ICE_PRISON);

	private final String name;
	private final float attackPower;
	private final float magicPower;
	private final float defensePower;
	private final Skill skill;

	private StaffTop(String name, float attackPower, float magicPower, float defensePower, Skill skill) {
		this.name = name;
		this.attackPower = attackPower;
		this.magicPower = magicPower;
		this.defensePower = defensePower;
		this.skill = skill;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		return name().replaceAll("_", "-").toLowerCase();
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
	
	public Skill getSkill() {
		return skill;
	}
}
