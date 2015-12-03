package com.me.steel.Domain.CraftProcesses.CraftActions;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Utils.DefaultActorListener;

public class FinishAction extends CraftAction {
	
	private CraftableWeapon weapon;
	private boolean orderFound;
	
	public FinishAction(final CraftableProcess process) {
		super(process.getStage());
		
		weapon = process.getWeapon();
		
		// set up the scrap button
		TextButton scrapButton = new TextButton("Scrap", process.getSkin());
		scrapButton.setY(Gdx.graphics.getHeight() - scrapButton.getHeight());
		scrapButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		final Label noSuchOrderLabel = new Label("This weapon was not ordered", process.getSkin());
		noSuchOrderLabel.setX(Gdx.graphics.getWidth() - noSuchOrderLabel.getWidth());
		noSuchOrderLabel.setVisible(false);
		
		// set up the create button
		TextButton createButton = new TextButton("Create", process.getSkin());
		createButton.setPosition(Gdx.graphics.getWidth() - createButton.getWidth(),
				Gdx.graphics.getHeight() - createButton.getHeight());
		createButton.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				
				Iterator<Customer> iterator = process.getOrderList().iterator();
				while (iterator.hasNext()) {
					Customer customer = (Customer) iterator.next();
					
					if (process.getWeapon().getClass() == customer.getEntity().getWeapon().getClass()) {
						orderFound = true;
						
						copyWeapon(process.getWeapon(), customer.getEntity().getWeapon());
						customer.getEntity().addWeapon();
						customer.getEntity().getWeapon().setPivot(customer.getEntity().getPivot());
						
						process.setCustomer(customer);
						iterator.remove();
						startCompleteSequence();
						
						break;
					}
				}
				
				if (!orderFound) noSuchOrderLabel.setVisible(true);
			}
		});
		
		// set up the stat labels
		Label attackLabel = new Label("Attack power: " + Float.toString(20), process.getSkin());
		Label magicLabel = new Label("Magic power: " + Float.toString(30), process.getSkin());
		Label defenseLabel = new Label("Defense power: " + Float.toString(40), process.getSkin());
		
		// set up a table for the labels
		Table statsTable = new Table();
		statsTable.setPosition(100, Gdx.graphics.getHeight() - statsTable.getHeight() - 100);
		
		statsTable.add(attackLabel).row();
		statsTable.add(magicLabel).row();
		statsTable.add(defenseLabel);
		
		// add the actors to the scene
		group.addActor(weapon.getGroup());
		group.addActor(scrapButton);
		group.addActor(createButton);
		group.addActor(statsTable);
		group.addActor(noSuchOrderLabel);
	}
	
	@Override
	public void update() {
		if (firstTime) {			
			weapon.buildWeapon();
			weapon.getGroup().setPosition(
					Gdx.graphics.getWidth() / 2 - weapon.getGroup().getWidth() / 2, 
					Gdx.graphics.getHeight() / 2 - weapon.getGroup().getHeight() / 2);
			
			firstTime = false;
		}
	}
	
	private void copyWeapon(CraftableWeapon src, CraftableWeapon dst) {		
		dst.setMaterial(src.getMaterial());
		dst.setOutline(src.getOutline());
		
		int length = src.getParts().length;
		for (int i = 0; i < length; i++)
			dst.getParts()[i].setPartImage(src.getParts()[i].getPartImage());
	}
}
