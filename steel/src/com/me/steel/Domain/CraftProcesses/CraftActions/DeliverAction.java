package com.me.steel.Domain.CraftProcesses.CraftActions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Customer;
import com.me.steel.Domain.CraftProcesses.CraftableProcess;
import com.me.steel.Utils.DefaultActorListener;
import com.me.steel.Utils.Timer;
import com.me.steel.Utils.TweenAnimation.Animation;

public class DeliverAction extends CraftAction {

	private CraftableProcess process;
	private Customer customer;
	private Timer timer;
	
	public DeliverAction(CraftableProcess process) {
		super(process.getStage());
		this.process = process;
		
		timer = new Timer();
		
		TextButton button = new TextButton("Continue", process.getSkin());
		button.setY(Gdx.graphics.getHeight() - button.getHeight());
		button.addListener(new DefaultActorListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y,
					int pointer, int button) {
				super.touchUp(event, x, y, pointer, button);
				customer.setDelivered(true);
				customer.getEntity().getAnimation().getGroup().remove();
				startCompleteSequence();
			}
		});
		
		group.addActor(button);
	}
	
	@Override
	public void update() {
		if (firstTime) {
			process.getBackButton().setVisible(false);
			customer = process.getCustomer();
			Animation animation = customer.getEntity().getAnimation();
			Group animGroup = animation.getGroup();
			
			// build the entities weapon
			customer.getEntity().getWeapon().buildWeapon();
			customer.getEntity().getWeapon().scaleToEntity(animGroup.getHeight());
			animation.setScale(2);
			group.addActor(animGroup);
			animGroup.setPosition(
					Gdx.graphics.getWidth() / 2 - animGroup.getWidth(),
					-animGroup.getHeight() / 2.5f);
			
			timer.start();
			firstTime = false;
		}
	}
}
