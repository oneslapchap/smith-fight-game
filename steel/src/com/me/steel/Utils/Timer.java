package com.me.steel.Utils;

public class Timer {
	private long startTicks;
	private long pausedTicks;
	
	private boolean paused;
	private boolean started;
	
	public Timer() {
		startTicks = 0;
		pausedTicks = 0;
		paused = false;
		started = false;
	}
	
	public void start() {
		started = true;
		paused = false;
		startTicks = System.currentTimeMillis();
	}
	
	public void stop() {
		started = false;
		paused = false;
	}
	
	public void pause() {
		if (started == true && paused == false) {
			paused = true;
			pausedTicks = System.currentTimeMillis() - startTicks;
		}
	}
	
	public void unpause() {
		if (paused == true) {
			paused = false;
			startTicks = System.currentTimeMillis() - pausedTicks;
			pausedTicks = 0;
		}
	}
	
	public long getTicks() {
		if (started == true) {
			if (paused == true)
				return pausedTicks;
			else
				return System.currentTimeMillis() - startTicks;
		}
		
		return 0;
	}
	
	public boolean isStarted() {
		return started;
	}
	
	public boolean isPaused() {
		return paused;
	}
}
