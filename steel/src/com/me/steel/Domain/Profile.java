package com.me.steel.Domain;

import java.util.HashMap;
import java.util.Map;

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
import com.me.steel.Utils.TextUtils;

/**
 * The player's profile.
 * This class is used to store the game progress, and is persisted to the file
 * system when the game exists.
 */
public class Profile implements Serializable {
	
	private int currentLevelId;
	private int credits;
	private Map<Integer, Integer> highScores;
	private Weapon weapon;

	public Profile() {
		credits = 1000;
		highScores = new HashMap<Integer, Integer>();
		
		weapon = new Weapon();
		weapon.install(Material.COPPER_INGOT);
		weapon.install(Outline.CRUDE_BROADSWORD);
		weapon.install(Handle.ROUGH_HANDLE);
		weapon.install(Rune.ATTACK_POWER_RUNE);
	}

	/**
	 * Retrieves the ID of the next playable level.
	 */
	public int getCurrentLevelId() {
		return currentLevelId;
	}

	/**
	 * Retrieves the high scores for each level (Level-ID -> High score).
	 */
	public Map<Integer, Integer> getHighScores() {
		return highScores;
	}

	/**
	 * Gets the current high score for the given level.
	 */
	public int getHighScore(int levelId) {
		if (highScores == null)
			return 0;
		Integer highScore = highScores.get(levelId);
		return (highScore == null ? 0 : highScore);
	}

	/**
	 * Notifies the score on the given level. Returns <code>true</code> if its a
	 * high score.
	 */
	public boolean notifyScore(int levelId, int score) {
		if (score > getHighScore(levelId)) {
			highScores.put(levelId, score);
			return true;
		}
		return false;
	}
	
	/**
	 * Retrieves the amount of credits the player has.
	 */
	public int getCredits() {
		return credits;
	}
	
	/**
	 * Retrieves the amount of credits as text.
	 */
	public String getCreditsAsText() {
		return TextUtils.creditStyle(credits);
	}

	/**
	 * Retrieves the current weapon configuration.
	 */
	public Weapon getWeapon() {
		return weapon;
	}

	/**
	 * Checks whether the given item can be bought.
	 */
	public boolean canBuy(Item item) {
		if (weapon.contains(item)) {
			return false;
		}
		// item.getPrice()
		if (500 > credits) {
			return false;
		}
		return true;
	}

	/**
	 * Buys the given item.
	 */
	public boolean buy(Item item) {
		if (canBuy(item)) {
			Gdx.app.log(Steel.LOG, "Buying item: " + item);
			weapon.install(item);
			credits -= 1000; // item.getPrice();
			Gdx.app.log(Steel.LOG, "Credits available: " + credits);
			return true;
		} else {
			Gdx.app.log(Steel.LOG, "No credits to buy item: " + item);
			return false;
		}
	}

	// Serializable implementation

	@SuppressWarnings("unchecked")
	@Override
	public void read(Json json, JsonValue jsonData) {
		// read the some basic properties
		currentLevelId = json.readValue("currentLevelId", Integer.class,
				jsonData);
		credits = json.readValue("credits", Integer.class, jsonData);
		
		// libgdx handles the keys of JSON formatted HashMaps as Strings, but we
		// want it to be an integer instead (levelId)
		Map<String, Integer> highScores = json.readValue("highScores",
				HashMap.class, Integer.class, jsonData);
		for (String levelIdAsString : highScores.keySet()) {
			int levelId = Integer.valueOf(levelIdAsString);
			Integer highScore = highScores.get(levelIdAsString);
			this.highScores.put(levelId, highScore);
		}

		// finally, read the ship
		weapon = json.readValue("weapon", Weapon.class, jsonData);
	}

	@Override
	public void write(Json json) {
		json.writeValue("currentLevelId", currentLevelId);
		json.writeValue("credits", credits);
		json.writeValue("highScores", highScores);
		json.writeValue("weapon", weapon);
	}
}
