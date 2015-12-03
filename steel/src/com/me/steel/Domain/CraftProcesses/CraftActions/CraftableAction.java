package com.me.steel.Domain.CraftProcesses.CraftActions;

public interface CraftableAction {
	public void update();
	public void reset();
	public boolean isComplete();
	/** Adds the action actors group to the provided stage */
	public void addToStage();
	/** Removes the action actors group from the provided stage */
	public void removeFromStage();
}
