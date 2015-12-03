package com.me.steel.Domain.CraftProcesses;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftActions.DeliverAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.DropPartAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.FinishAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.MaterialSelectAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.MeltAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.OutlineSelectAction;
import com.me.steel.Domain.CraftProcesses.CraftActions.SmeltAction;
import com.me.steel.Domain.Entities.Weapons.CraftableWeapon;
import com.me.steel.Domain.Entities.Weapons.StaffEntity;

public class WizardProcess extends CraftProcess {

	private StaffEntity staff;
	
	public WizardProcess(Stage stage, Skin skin, TextureAtlas atlas, List<Customer> orderList) {
		super(stage, skin, atlas, orderList);
		
		type = "staff";
		tierLevel = "1";
		
		staff = new StaffEntity();
		
		craftActionList.add(new OutlineSelectAction(this));
		craftActionList.add(new MaterialSelectAction(this));
		craftActionList.add(new MeltAction(this));
		craftActionList.add(new SmeltAction(this));
		craftActionList.add(new DropPartAction(this));
		craftActionList.add(new FinishAction(this));
		craftActionList.add(new DeliverAction(this));
	}

	@Override
	public CraftableWeapon getWeapon() {
		return staff;
	}

}
