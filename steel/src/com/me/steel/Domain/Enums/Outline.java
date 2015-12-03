package com.me.steel.Domain.Enums;


public enum Outline implements Item {
	
	// tier1
	CRUDE_BROADSWORD("sword", "1", "Crude broadsword", 10, 10, 10, Skill.SKILL_FIREBALL),
	CRUDE_CLEAVESWORD("sword", "1", "Crude cleavesword", 20, 20, 20, Skill.ATTACKER_MELEE),
	CRUDE_SAWBLADE("sword", "1", "Crude sawblade", 30, 30, 30, Skill.SKILL_FIREBALL),
	CRUDE_SCIMITAR("sword", "1", "Crude scimitar", 40, 40, 40, Skill.SKILL_FIREBALL),
	SIMPLE_STAFF("staff", "1", "Simple staff", 10, 10, 10, Skill.SKILL_FIREBALL),
	OCTOPUS_STAFF("staff", "1", "Octopus staff", 20, 20, 20, Skill.SKILL_FIREBALL),
	// tier2
	WICKED_SCIMITAR("sword", "2", "Wicked scrimitar", 50, 50, 50, Skill.SKILL_FIREBALL),
	GUST("sword", "2", "Gust", 60, 60, 60, Skill.SKILL_FIREBALL);

	// used when finding regions
	private final String itemType;
	private final String tierLevel;
	
	private final String name;
	private final float attackPower;
	private final float magicPower;
	private final float defensePower;
	private final Skill skill;
	
	private Outline(String itemType, String tierLevel, String name,
			float attackPower, float magicPower, float defensePower, Skill skill) {
		this.itemType = itemType;
		this.tierLevel = tierLevel;
		
		this.name = name;
		this.attackPower = attackPower;
		this.magicPower = magicPower;
		this.defensePower = defensePower;
		this.skill = skill;
	}
	
	/** Returns the item's type (sword, staff etc.) */
	public String getType() {
		return itemType;
	}
	
	/** Returns item's tier level */
	public String getTierlevel() {
		return tierLevel;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		return name().replaceAll( "_", "-" ).toLowerCase();
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
