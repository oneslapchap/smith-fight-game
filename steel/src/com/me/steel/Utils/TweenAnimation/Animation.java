package com.me.steel.Utils.TweenAnimation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Animation {
	
	public static final int MOVEMENT_IDLE = 0;
	public static final int MOVEMENT_WALK = 1;
	public static final int MOVEMENT_ATTACK = 2;
	public static final int MOVEMENT_DIE = 3;
	
	private Group group;
	private List<Image> imageList;
	private List<AnimationData> animationDataList;
	private TweenManager manager;
	private Vector2 initialPos;
	
	// groups initial dimensions
	private float initialW;
	private float initialH;
	
	private int currentAnimation;
	private int currentMovement;
	private int currentFrame;
	private int maxFrames;
	
	private int loopFor;
	private int loopCount;
	
	private boolean animationStarted;
	
	public Animation(String xmlPath, TextureAtlas textureAtlas) {
		
		manager = new TweenManager();
		initialPos = new Vector2();
		
		// load the animation data
		AnimationLoader animationLoader = new AnimationLoader();
		try {
			animationDataList = animationLoader.loadAnimation(xmlPath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		group = new Group();
		imageList = new ArrayList<Image>();
		
		// load spriteList and set it to an initial position
		Iterator<BodyPart> bodyPartIterator = animationDataList.get(0).getMovements().get(0).getBodyParts().iterator();
		while (bodyPartIterator.hasNext()) {
			BodyPart bodyPart = (BodyPart) bodyPartIterator.next();
			
			TextureRegion bodyPartRegion = new TextureRegion(textureAtlas.findRegion(
					animationDataList.get(0).getName()
					+ "/parts-" 
					+ bodyPart.getName()));
			
			Image image = new Image(bodyPartRegion);
			image.setPosition(bodyPart.getFrames().get(0).getWorldX(), bodyPart.getFrames().get(0).getWorldY());
			
			image.setOrigin(bodyPart.getFrames().get(0).getPivotX(), bodyPart.getFrames().get(0).getPivotY());
			image.rotate(bodyPart.getFrames().get(0).getRotation());
			
			imageList.add(image);
			group.addActor(image);
		}
		
		// calculate the width and height of the armature
		float minX = imageList.get(0).getX();
		float maxX = imageList.get(0).getX();
		float minY = imageList.get(0).getY();
		float maxY = imageList.get(0).getY();
		
		Iterator<Image> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			Image image = (Image) iterator.next();
			
			if (minX > image.getX())
				minX = image.getX();
			
			if (minY > image.getY())
				minY = image.getY();
			
			if (maxX < image.getX() + image.getWidth())
				maxX = image.getX() + image.getWidth();
			
			if (maxY < image.getY() + image.getHeight())
				maxY = image.getY() + image.getHeight();
		}
		
		initialW = maxX - minX;
		initialH = maxY - minY;
		
		group.setWidth(initialW);
		group.setHeight(initialH);
	}
	
	public void update() {
		manager.update(Gdx.graphics.getDeltaTime());
		
		if (loopCount < loopFor || loopFor == -1) {
			// don't play the animation if there's no frames to play
			if (maxFrames > 0)
				playAnimation();
		}
	}
	
	public void setPosition(float x, float y) {
		group.setPosition(x, y);
	}
	
	public void setMovement(int currentMovement, int loopFor) {
		this.currentMovement = currentMovement;
		maxFrames = animationDataList
				.get(currentAnimation)
				.getMovements()
				.get(currentMovement)
				.getBodyParts().get(0)
				.getFrames().size();
		
		this.loopFor = loopFor;
		// reset the loop counter
		loopCount = 0;
		currentFrame = 0;
		animationStarted = true;
	}
	
	private void playAnimation() {
		if (manager.getRunningTweensCount() == 0) {
			
			if (currentFrame >= maxFrames) {
				currentFrame = 0;
				loopCount++;
			}
			
			Iterator<BodyPart> bodyPartIterator = 
					animationDataList.get(currentAnimation)
					.getMovements().get(currentMovement)
					.getBodyParts().iterator();
			
			int i = 0;
			while (bodyPartIterator.hasNext()) {
				BodyPart bodyPart = (BodyPart) bodyPartIterator.next();
				
				if (currentFrame < bodyPart.getFrames().size()) {

					Tween.to(imageList.get(i), ImageAccessor.POS_XY, bodyPart.getFrames().get(currentFrame).getDuration())
					.target(bodyPart.getFrames().get(currentFrame).getWorldX(), bodyPart.getFrames().get(currentFrame).getWorldY())
					.start(manager);
						
					imageList.get(i).setOrigin(bodyPart.getFrames().get(currentFrame).getPivotX(), bodyPart.getFrames().get(currentFrame).getPivotY());
					Tween.to(imageList.get(i), ImageAccessor.ROTATION, bodyPart.getFrames().get(currentFrame).getDuration())
					.target(bodyPart.getFrames().get(currentFrame).getRotation())
					.start(manager);
				}
				
				i++;
			}
			
			currentFrame++;
		}
	}
	
	public void setColor(float red, float green, float blue) {
		Iterator<Image> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			Image image = (Image) iterator.next();
			image.setColor(red, green, blue, 1f);
		}
	}
	
	public void setScale(float scale) {
		group.setScale(scale);
		
		group.setWidth(initialW * scale);
		group.setHeight(initialH * scale);
	}
	
	public void flipAnimation(boolean flipX, boolean flipY) {
		Iterator<Image> iterator = imageList.iterator();
		while (iterator.hasNext()) {
			Image image = (Image) iterator.next();
			((TextureRegionDrawable)image.getDrawable()).getRegion().flip(flipX, flipY);
		}
		
		Iterator<AnimationData> animationDataIter = animationDataList.iterator();
		while (animationDataIter.hasNext()) {
			AnimationData animationData = (AnimationData) animationDataIter.next();
			
			Iterator<Movement> movementIter = animationData.getMovements().iterator();
			while (movementIter.hasNext()) {
				Movement movement = (Movement) movementIter.next();
				
				Iterator<BodyPart> bodyPartIter = movement.getBodyParts().iterator();
				while (bodyPartIter.hasNext()) {
					BodyPart bodyPart = (BodyPart) bodyPartIter.next();
					
					Iterator<Frame> frameIter = bodyPart.getFrames().iterator();
					while (frameIter.hasNext()) {
						Frame frame = (Frame) frameIter.next();
						
						if (flipX == true) {
							frame.setX(-frame.getX());
							frame.setRotation(-frame.getRotation());
						}
						
						if (flipY == true) {
							frame.setY(-frame.getY());
							frame.setRotation(-frame.getRotation());
						}
					}
				}
			}
		}
	}
	
	public void setVisible(boolean visible) {
		Iterator<Image> iterator = getImageList().iterator();
		while (iterator.hasNext()) {
			Image image = (Image) iterator.next();
			image.setVisible(visible);
		}
	}
	
	public void stop() {
		loopFor = 0;
		maxFrames = 0;
		animationStarted = false;
	}
	
	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public float getX() {
		return group.getX();
	}
	
	public float getY() {
		return group.getY();
	}
	
	public int getLoopFor() {
		return loopFor;
	}
	
	/** only updated on the setPosition() */
	public float getInitialX() {
		return initialPos.x;
	}
	
	/** only updated on the setPosition() */
	public float getInitialY() {
		return initialPos.y;
	}
	
	public float getWidth() {
		return group.getWidth();
	}
	
	public float getHeight() {
		return group.getHeight();
	}
	
	public List<Image> getImageList() {
		return imageList;
	}
	
	public int getMaxFrames() {
		return maxFrames;
	}
	
	public int getTotalMovements() {
		return animationDataList.get(0).getMovements().size();
	}
	
	public int getCurrentFrame() {
		return currentFrame;
	}
	
	public int getLoopCount() {
		return loopCount;
	}
	
	public int getCurrentMovement() {
		return currentMovement;
	}
	
	public boolean isStarted() {
		return animationStarted;
	}
	
	public int getRunningTweensCount(){
		return manager.getRunningTweensCount();
	}
	
	public Group getGroup() {
		return group;
	}
	
	public List<AnimationData> getAnimationDataList() {
		return animationDataList;
	}
}
