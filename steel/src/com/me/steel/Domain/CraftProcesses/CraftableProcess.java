package com.me.steel.Domain.CraftProcesses;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftActions.CraftableAction;
import com.me.steel.Domain.Entities.SmeltTimer;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;

public interface CraftableProcess {
	/** Adds the current action actors to the provided stage. */
	public void addActionGroup();
	/** Removes the current action actors from the provided stage. */
	public void removeAcionGroup();
	public void update(float delta);
	public void dispose();
	/** Resets all the actions of the process */
	public void reset();
	public Stage getStage();
	public Skin getSkin();
	public TextureAtlas getAtlas();
	public String getType();
	public String getTierLevel();
	public ParticleEffect getSmokeEffect();
	public ParticleEffect getSparkEffect();
	public CraftableWeapon getWeapon();
	public SmeltTimer getSmeltTimer();
	public CraftableAction getCurrentAction();
	public boolean isComplete();
	public List<Customer> getOrderList();
	/** The entity selected from the oderList that shall receive the weapon */
	public Customer getCustomer();
	public TextButton getBackButton();
	/** Set the entity that shall receive the weapon being crafted */
	public void setCustomer(Customer customer);
	public void setBackButton(TextButton backButton);
}
