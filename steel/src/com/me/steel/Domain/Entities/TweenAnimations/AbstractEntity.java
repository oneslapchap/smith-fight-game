package com.me.steel.Domain.Entities.TweenAnimations;

import java.util.Iterator;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.steel.Utils.DebugBox;
import com.me.steel.Utils.ParallaxCamera;
import com.me.steel.Utils.Sequence;
import com.me.steel.Utils.TweenAnimation.Animation;

public abstract class AbstractEntity {
	
	protected Animation animation;
	private Group group;
	private Sequence sequence;
	
	public AbstractEntity(String xmlPath, TextureAtlas textureAtlas) {
		animation = new Animation(xmlPath, textureAtlas);
		group = animation.getGroup();
	}
	
	public void drawBodyPartDebug(ParallaxCamera camera) {
		DebugBox.setColor(Color.RED);
		
		Iterator<Image> imageIter = animation.getImageList().iterator();
		while (imageIter.hasNext()) {
			Image image = (Image) imageIter.next();
			
			DebugBox.drawDebug(
					animation.getGroup().getX() + image.getX(),
					animation.getGroup().getY() + image.getY(), 
					image.getWidth(), 
					image.getHeight(), 
					camera);
		}
	}
	
	public void drawDebug(ParallaxCamera camera) {
		DebugBox.setColor(Color.GREEN);
		DebugBox.drawDebug(
				animation.getGroup().getX(), 
				animation.getGroup().getY(), 
				animation.getGroup().getWidth(), 
				animation.getGroup().getHeight(), 
				camera);
	}
	
	public void draw(SpriteBatch batch) {
		group.draw(batch, 1f);
	}
	
	public void update() {
		if (sequence != null)
			sequence.update();
		
		animation.update();
	}
	
	public Animation getAnimation() {
		return animation;
	}
	
	public void addSequence(Sequence sequence) {
		if (this.sequence == null)
			this.sequence = new Sequence();
		
		this.sequence.removeAllElements();
		this.sequence = sequence;
	}
}
