package com.me.steel.Domain.CraftProcesses;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftActions.CraftableAction;
import com.me.steel.Domain.Entities.SmeltTimer;

public abstract class CraftProcess implements CraftableProcess {
	
	protected List<CraftableAction> craftActionList;
	private List<Customer> orderList;
	// the entity to receive the weapon
	private Customer customer;
	private int index;
	private SmeltTimer smeltTimer;
	private TextButton backButton;
	private boolean complete;
	
	protected Stage stage;
	protected Skin skin;
	protected TextureAtlas atlas;
	protected ParticleEffect smokeEffect;
	protected ParticleEffect sparkEffect;
	
	protected String type;
	protected String tierLevel;
	
	public CraftProcess(Stage stage, Skin skin, TextureAtlas atlas, List<Customer> orderList) {
		// initialize resources
		this.stage = stage;
		this.skin = skin;
		this.atlas = atlas;
		this.orderList = orderList;
		smokeEffect = new ParticleEffect();
		smokeEffect.load(Gdx.files.internal("particles/smoke.p"),
				Gdx.files.internal("particles"));
		sparkEffect = new ParticleEffect();
		sparkEffect.load(Gdx.files.internal("particles/sparks.p"),
				Gdx.files.internal("particles"));
		
		// define smelt timer
		smeltTimer = new SmeltTimer(atlas);
		
		// add actions
		craftActionList = new ArrayList<CraftableAction>();
	}
	
	@Override
	public void update(float delta) {
		if (!complete) {
			craftActionList.get(index).update();
				
			if (craftActionList.get(index).isComplete()) {
				if (index + 1 < craftActionList.size()) {
					index++;
					craftActionList.get(index).addToStage();
				}
				else complete = true;
			}
		}
	}
	
	@Override
	public void reset() {
		Iterator<CraftableAction> iterator = craftActionList.iterator();
		while (iterator.hasNext()) {
			CraftableAction action = (CraftableAction) iterator.next();
			action.reset();
		}
		
		index = 0;
		complete = false;
	}
	
	@Override
	public void addActionGroup() {
		craftActionList.get(index).addToStage();
	}
	
	@Override
	public void removeAcionGroup() {
		craftActionList.get(index).removeFromStage();
	}
	
	@Override
	public void dispose() {
		smokeEffect.dispose();
		sparkEffect.dispose();
	}
	
	@Override
	public Stage getStage() {
		return stage;
	}
	
	@Override
	public Skin getSkin() {
		return skin;
	}
	
	@Override
	public TextureAtlas getAtlas() {
		return atlas;
	}
	
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public String getTierLevel() {
		return tierLevel;
	}
	
	@Override
	public ParticleEffect getSmokeEffect() {
		return smokeEffect;
	}
	
	@Override
	public ParticleEffect getSparkEffect() {
		return sparkEffect;
	}
	
	@Override
	public SmeltTimer getSmeltTimer() {
		return smeltTimer;
	}
	
	@Override
	public CraftableAction getCurrentAction() {
		return craftActionList.get(index);
	}
	
	@Override
	public boolean isComplete() {
		return complete;
	}
	
	@Override
	public List<Customer> getOrderList() {
		return orderList;
	}
	
	@Override
	public Customer getCustomer() {
		return customer;
	}
	
	@Override
	public TextButton getBackButton() {
		return backButton;
	}
	
	@Override
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	@Override
	public void setBackButton(TextButton backButton) {
		this.backButton = backButton;
	}
}
