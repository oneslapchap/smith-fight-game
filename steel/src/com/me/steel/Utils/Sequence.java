package com.me.steel.Utils;

import java.util.ArrayList;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.equations.Linear;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.me.steel.Domain.Entities.TweenAnimations.EnemyEntity;
import com.me.steel.Domain.Entities.TweenAnimations.FightableEntity;
import com.me.steel.Utils.TweenAnimation.Animation;
import com.me.steel.Utils.TweenAnimation.GroupAccessor;

public class Sequence {

	private static final int SET_VISIBLE 		= 1;
	private static final int SET_MOVEMENT 		= 2;
	private static final int SET_POSITION 		= 3;
	private static final int SET_SCALE 			= 4;
	private static final int SET_HEALTH 		= 5;
	private static final int MOVE_TO 			= 6;
	private static final int MOVE_TO_TARGET 	= 7;
	private static final int MOVE_TO_BASE		= 8;
	private static final int POSITION_TO_TARGET = 9;
	private static final int FLIP 				= 10;
	
	private List<Struct> sequenceList;
	private int index;
	private boolean start;
	private boolean repeat;
	private boolean reached;
	private boolean ended;
	private TweenManager manager;
	
	// to avoid dynamic allocation, references are used
	private FightableEntity caster;
	private FightableEntity target;
	
	// convenience variables
	Struct struct;
	Struct prevStruct;
	
	public Sequence() {
		sequenceList = new ArrayList<Struct>();
		manager = new TweenManager();
	}
	
	public void update() {
		if (index >= sequenceList.size()) {
			index = 0;
			reached = true;
			if (repeat != true) start = false;
		}
		
		manager.update(Gdx.graphics.getDeltaTime());
		
		if (reached == true) {
			struct = sequenceList.get(sequenceList.size() - 1);
			if (struct.animation.getRunningTweensCount() == 0 && manager.getRunningTweensCount() == 0) {
				ended = true;
			}
		}
		
		if (start == true) {
			// get the current element
			struct = sequenceList.get(index);
			
			// check if there is a previous element
			if (index - 1 != -1) {		
				// get the previous element
				prevStruct = sequenceList.get(index - 1);
				inspectCommand();		
			}
			else {		
				// get the last element from the list
				prevStruct = sequenceList.get(sequenceList.size() - 1);
				inspectCommand();
			}
		}
	}
	
	private void inspectCommand() {
		if (struct.waitToFinish == true && prevStruct.animation != null) {
			if (prevStruct.command == MOVE_TO) {
				if (prevStruct.animation.getX() == prevStruct.x && prevStruct.animation.getY() == prevStruct.y)
					executeCommand();
			}
			else if (prevStruct.command == MOVE_TO_TARGET) {
				if (prevStruct.animation.getX() + ((target instanceof EnemyEntity) ? prevStruct.animation.getWidth() : -target.getAnimation().getWidth()) == target.getBasePosX() &&
						prevStruct.animation.getY() == target.getBasePosY()) 
					executeCommand();
			}
			else if (prevStruct.command == MOVE_TO_BASE) {
				if (prevStruct.animation.getX() == caster.getBasePosX() && prevStruct.animation.getY() == caster.getBasePosY())
					executeCommand();
			}
			else {
				if (prevStruct.animation.getLoopCount() >= prevStruct.animation.getLoopFor() 
						&& prevStruct.animation.getRunningTweensCount() == 0)
					executeCommand();
			}
		}
		else 
			executeCommand();
	}
	
	private void executeCommand() {	
		switch (struct.command) {
		case SET_VISIBLE:
			if (struct.animation != null)
				struct.animation.setVisible(struct.visible);
			else
				struct.button.setVisible(struct.visible);
			break;
		case SET_MOVEMENT:
			struct.animation.setMovement(struct.movement, struct.loopFor);
			struct.animation.setCurrentFrame(struct.startFrame);
			break;
		case SET_POSITION:
			struct.animation.setPosition(struct.x, struct.y);
			break;
		case SET_SCALE:
			struct.animation.setScale(struct.scale);
			break;
		case SET_HEALTH:
			target.setHealth(target.getHealth() - struct.damage);
			break;
		case MOVE_TO:
			Tween.to(struct.animation.getGroup(), GroupAccessor.POS_XY, struct.duration)
			.ease(Linear.INOUT)
			.target(struct.x, struct.y)
			.start(manager);
			break;
		case MOVE_TO_TARGET:
			Tween.to(struct.animation.getGroup(), GroupAccessor.POS_XY, struct.duration)
			.ease(Linear.INOUT)
			.target(target.getBasePosX() + ((target instanceof EnemyEntity) ? -struct.animation.getWidth() : target.getAnimation().getWidth()), 
					target.getBasePosY())
			.start(manager);
			break;
		case MOVE_TO_BASE:
			Tween.to(struct.animation.getGroup(), GroupAccessor.POS_XY, struct.duration)
			.ease(Linear.INOUT)
			.target(caster.getBasePosX(), caster.getBasePosY())
			.start(manager);
			break;
		case POSITION_TO_TARGET:
			struct.animation.setPosition(target.getBasePosX() + struct.x, target.getBasePosY() + struct.y);
			break;
		case FLIP:
			struct.animation.flipAnimation(true, false);
			break;
		}
		
		index++;
	}
	
	public void start(boolean start) {
		this.start = start;
		reached = false;
		ended = false;
	}
	
	public void repeat() {
		repeat = true;
	}
	
	public boolean endReached() {
		return reached;
	}
	
	public boolean hasEnded() {
		return ended;
	}
	
	public boolean isStarted() {
		return start;
	}
	
	public void startFromLast() {
		index = sequenceList.size();
	}
	
	public void removeAllElements() {
		sequenceList.clear();
	}
	
	public void moveTo(float x, float y, float duration, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = MOVE_TO;
		struct.x = x;
		struct.y = y;
		struct.duration = duration;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	/** Move the animation to the currently set target */
	public void moveToTarget(float duration, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = MOVE_TO_TARGET;
		struct.duration = duration;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	/** Move the currently set caster back to his base position */
	public void returnToBase(float duration, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = MOVE_TO_BASE;
		struct.duration = duration;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	/** Sets the health of the currently set target. */
	public void setHealth(int damage, boolean waitToFinish) {
		Struct struct = new Struct();
		struct.command = SET_HEALTH;
		struct.damage = damage;
		struct.waitToFinish = waitToFinish;
		sequenceList.add(struct);
	}
	
	public void positionToTarget(float x, float y, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = POSITION_TO_TARGET;
		struct.x = x;
		struct.y = y;
		struct.animation = animation;
		struct.waitToFinish = waitToFinish;
		sequenceList.add(struct);
	}
	
	public void setPosition(float x, float y, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = SET_POSITION;
		struct.x = x;
		struct.y = y;
		struct.animation = animation;
		struct.waitToFinish = waitToFinish;
		sequenceList.add(struct);
	}
	
	public void setMovement(int movement, int loopFor, int startFrame, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = SET_MOVEMENT;
		struct.movement = movement;
		struct.loopFor = loopFor;
		struct.startFrame = startFrame;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	public void setVisible(boolean visible, boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = SET_VISIBLE;
		struct.visible = visible;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	public void setVisible(boolean visible, boolean waitToFinish, TextButton button) {
		Struct struct = new Struct();
		struct.command = SET_VISIBLE;
		struct.visible = visible;
		struct.waitToFinish = waitToFinish;
		struct.button = button;
		sequenceList.add(struct);
	}
	
	public void setScale(float scale, Animation animation) {
		Struct struct = new Struct();
		struct.command = SET_SCALE;
		struct.animation = animation;
		struct.scale = scale;
		sequenceList.add(struct);
	}
	
	public void flip(boolean waitToFinish, Animation animation) {
		Struct struct = new Struct();
		struct.command = FLIP;
		struct.waitToFinish = waitToFinish;
		struct.animation = animation;
		sequenceList.add(struct);
	}
	
	/** 
	 * Set the current target, which must be set in order to use commands
	 * that involve another entity, such as:
	 * SET_HEALTH (of the target)
	 * MOVE_TO_TARGET etc.
	 */
	public void setTarget(FightableEntity target) {
		this.target = target;
	}
	
	public void setCaster(FightableEntity caster) {
		this.caster = caster;
	}
	
	/** A helper struct, to hold the variables */
	private class Struct {
		public int command;
		public boolean visible;
		public boolean waitToFinish;
		public float x;
		public float y;
		public float duration;
		public int movement;
		public int startFrame;
		public int loopFor;
		public Animation animation;
		public TextButton button;
		public int damage;
		public float scale;
	}
}
