package com.me.steel.Domain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.me.steel.Steel;
import com.me.steel.Domain.Enums.Handle;
import com.me.steel.Domain.Enums.Item;
import com.me.steel.Domain.Enums.Material;
import com.me.steel.Domain.Enums.Outline;
import com.me.steel.Domain.Enums.Rune;
import com.me.steel.Utils.Stats;

public class Weapon implements Serializable {

	private Material material;
	private Outline outline;
	private Handle handle;
	private Rune rune;
	private Stats stats;
	
	public Weapon() {
		stats = new Stats();
	}
	
	public void install(Item item) {
		Gdx.app.log(Steel.LOG, "Installing item: " + item);
		if (item instanceof Material) {
			material = (Material) item;
		} else if (item instanceof Outline) {
			outline = (Outline) item;
		} else if (item instanceof Handle) {
			handle = (Handle) item;
		} else if (item instanceof Rune) {
			rune = (Rune) item;
		} else {
			throw new IllegalArgumentException("Unknown item: " + item);
		}
	}
	
	public boolean contains(Item item) {
		if (item == null)
			return false;
		return (item.equals(material) || item.equals(outline) || item
				.equals(handle) || item.equals(rune));
	}
	
	public Material getMaterial() {
		return material;
	}
	
	public Outline getOutline() {
		return outline;
	}
	
	public Handle getHandle() {
		return handle;
	}
	
	public Rune getRune() {
		return rune;
	}
	
	public Stats getStats() {
		return stats;
	}
	
	public void setStats(Stats stats) {
		this.stats = stats;
	}
	
	@Override
	public void write(Json json) {
		json.writeValue("material", material.name());
		json.writeValue("outline", outline.name());
		json.writeValue("handle", handle.name());
		json.writeValue("rune", rune.name());
		
		json.writeValue("attackPower", stats.getAttackPower());
		json.writeValue("magicPower", stats.getMagicPower());
		json.writeValue("defensePower", stats.getDefensePower());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		material = Material.valueOf(json.readValue("material", String.class, jsonData));
		outline = Outline.valueOf(json.readValue("outline", String.class, jsonData));
		handle = Handle.valueOf(json.readValue("handle", String.class, jsonData));
		rune = Rune.valueOf(json.readValue("rune", String.class, jsonData));
		
		stats.setAttackPower(json.readValue("attackPower", Float.class, jsonData));
		stats.setMagicPower(json.readValue("magicPower", Float.class, jsonData));
		stats.setDefensePower(json.readValue("defensePower", Float.class, jsonData));
	}
}
