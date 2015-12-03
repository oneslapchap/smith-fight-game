package com.me.steel.Utils.TweenAnimation;

import aurelienribon.tweenengine.TweenAccessor;

import com.me.steel.Utils.ParallaxCamera;

public class CameraAccessor implements TweenAccessor<ParallaxCamera> {
	
	public static final int POS_XY = 1;

	@Override
	public int getValues(ParallaxCamera target, int tweenType, float[] returnValues) {
		switch (tweenType) {
			case POS_XY:
				returnValues[0] = target.position.x;
				returnValues[1] = target.position.y;
				return 2;

			default: assert false; return -1;
		}
	}

	@Override
	public void setValues(ParallaxCamera target, int tweenType, float[] newValues) {
		switch (tweenType) {
			case POS_XY:
				target.position.x = newValues[0];
				target.position.y = newValues[1];
			break;

			default: assert false;
		}
	}
}
