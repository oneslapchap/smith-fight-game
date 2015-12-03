package com.me.steel.Utils.TweenAnimation;

import aurelienribon.tweenengine.TweenAccessor;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteAccessor implements TweenAccessor<Sprite> {
    public static final int SKEW_X2X3 = 1;
    public static final int SKEW_X1X4 = 2;
    public static final int POS_XY = 3;
 
    @Override
    public int getValues(Sprite target, int tweenType, float[] returnValues) {
        switch (tweenType) {
            case SKEW_X2X3:
                float[] vs = target.getVertices();
                returnValues[0] = vs[SpriteBatch.X2] - target.getX();
                returnValues[1] = vs[SpriteBatch.X3] - target.getX() - target.getWidth();
                return 2;
                
            case SKEW_X1X4:
                float[] vs1 = target.getVertices();
                returnValues[0] = vs1[SpriteBatch.X1] - target.getX();
                returnValues[1] = vs1[SpriteBatch.X4] - target.getX() - target.getWidth();
                return 2;
                
            case POS_XY:
				returnValues[0] = target.getX();
				returnValues[1] = target.getY();
				return 2;
        }
 
        assert false;
        return -1;
    }
 
    @Override
    public void setValues(Sprite target, int tweenType, float[] newValues) {
        switch (tweenType) {
            case SKEW_X2X3:
                float x2 = target.getX();
                float x3 = x2 + target.getWidth();
                float[] vs = target.getVertices();
                vs[SpriteBatch.X2] = x2 + newValues[0];
                vs[SpriteBatch.X3] = x3 + newValues[1];
                break;
                
            case SKEW_X1X4:
                float x1 = target.getX();
                float x4 = x1 + target.getWidth();
                float[] vs1 = target.getVertices();
                vs1[SpriteBatch.X1] = x1 + newValues[0];
                vs1[SpriteBatch.X4] = x4 + newValues[1];
                break;
                
            case POS_XY: target.setPosition(newValues[0], newValues[1]); break;
        }
    }
}
