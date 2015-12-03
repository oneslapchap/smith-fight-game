package com.me.steel.Domain.Enums;


public enum Rune implements Item {
	
	ATTACK_POWER_RUNE("Attack power rune", 10, 10, 10),
	MAGIC_POWER_RUNE("Magic power rune", 20, 20, 20),
	DEFENSE_POWER_RUNE("Defense rune", 30, 30, 30);

	private final String name;
	private final float attackPower;
	private final float magicPower;
	private final float defensePower;
	
	private Rune(String name, float attackPower, float magicPower, float defensePower) {
		this.name = name;
		this.attackPower = attackPower;
		this.magicPower = magicPower;
		this.defensePower = defensePower;
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
}
