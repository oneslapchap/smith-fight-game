package com.me.steel.Domain.Enums;


public enum Material implements Item {
	COPPER_INGOT("Copper ingot", 238, 180, 34, 10, 10, 10, -30), 
	STEEL_INGOT("Steel ingot", 169, 169, 169, 20, 20, 20, -120),
	DIAMOND_INGOT("Diamong ingot", 0, 178, 238, 30, 30, 30, -210),
	DEMONIC_INGOT("Demonic ingot", 176, 23, 31, 40, 40, 40, -300);

	private final String name;
	private final int red, green, blue;
	private final float attackPower;
	private final float magicPower;
	private final float defensePower;
	private final int meltPoint;

	private Material(String name, int red, int green, int blue,
			float attackPower, float magicPower, float defensePower, int meltPoint) {
		this.name = name;
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.attackPower = attackPower;
		this.magicPower = magicPower;
		this.defensePower = defensePower;
		this.meltPoint = meltPoint;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSimpleName() {
		return name().replaceAll("_", "-").toLowerCase();
	}

	/** returns red color value as float 0f - 1f */
	public float getRedColorValue() {
		return red / 255f;
	}

	/** returns green color value as float 0f - 1f */
	public float getGreenColorValue() {
		return green / 255f;
	}

	/** returns blue color value as float 0f - 1f */
	public float getBlueColorValue() {
		return blue / 255f;
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
	
	public int getMeltPoint() {
		return meltPoint;
	}
}
