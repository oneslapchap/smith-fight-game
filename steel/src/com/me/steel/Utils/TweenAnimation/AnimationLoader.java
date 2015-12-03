package com.me.steel.Utils.TweenAnimation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

public class AnimationLoader {
	
	public List<AnimationData> loadAnimation(String path) throws IOException {
		
		// initialize some variables
		
		List<AnimationData> animationDataList = new ArrayList<AnimationData>();
		
		FileHandle fileHandle = Gdx.files.internal(path);
		
		// load an armature and get the offset values if there is any
		ArmatureLoader armatureLoader = new ArmatureLoader();
		List<Vector2> offsets = armatureLoader.loadArmatureOffsets(fileHandle);
		
		// read the xml and parse its data
		XmlReader xmlReader = new XmlReader();
		XmlReader.Element xmlElement = xmlReader.parse(fileHandle);
		
		Iterator<XmlReader.Element> animationsIterator = xmlElement.getChildrenByName("animations").iterator();
		while (animationsIterator.hasNext()) {
			XmlReader.Element animationsElement = (XmlReader.Element) animationsIterator.next();
			
			Iterator<XmlReader.Element> animationIterator = animationsElement.getChildrenByName("animation").iterator();
			while (animationIterator.hasNext()) {
				XmlReader.Element animationElement = (XmlReader.Element) animationIterator.next();
				
				List<Movement> movementList = new ArrayList<Movement>();
				
				Iterator<XmlReader.Element> movIterator = animationElement.getChildrenByName("mov").iterator();
				while (movIterator.hasNext()) {
					XmlReader.Element movElement = (XmlReader.Element) movIterator.next();
					
					List<BodyPart> bodyPartList = new ArrayList<BodyPart>();
					
					int i = 0;
					Iterator<XmlReader.Element> bIterator = movElement.getChildrenByName("b").iterator();
					while (bIterator.hasNext()) {
						XmlReader.Element bElement = (XmlReader.Element) bIterator.next();
						
						List<Frame> framesList = new ArrayList<Frame>();
						
						Iterator<XmlReader.Element> fIterator = bElement.getChildrenByName("f").iterator();
						while (fIterator.hasNext()) {
							XmlReader.Element fElement = (XmlReader.Element) fIterator.next();
							
							// get the frame attributes
							String x = fElement.getAttribute("x");
							String y = fElement.getAttribute("y");
							String kX = fElement.getAttribute("kX");
							String kY = fElement.getAttribute("kY");
							String pX = fElement.getAttribute("pX");
							String pY = fElement.getAttribute("pY");
							String dr = fElement.getAttribute("dr");
							
							// write the attributes to framesList and add the offsets received earlier
							framesList.add(new Frame(
									Float.parseFloat(x),
									// add negative values on the y axis since the
									// y axis in the game is pointing upwards
									-Float.parseFloat(y), 
									-Float.parseFloat(kX), 
									Float.parseFloat(kY), 
									Float.parseFloat(pX) + offsets.get(i).x, 
									Float.parseFloat(pY) + offsets.get(i).y,
									Float.parseFloat(dr)));
						}
						
						bodyPartList.add(new BodyPart(bElement.getAttribute("name"), framesList));
						
						i++;
					}
				
					movementList.add(new Movement(movElement.getAttribute("name"), bodyPartList));
				}
				
				animationDataList.add(new AnimationData(animationElement.getAttribute("name"), movementList));
			}
		}
		
		// find the minY and minY values of the armature's first frame
		float minY = 
				animationDataList.get(0)
				.getMovements().get(0)
				.getBodyParts().get(0)
				.getFrames().get(0).getY()
				- animationDataList.get(0)
				.getMovements().get(0)
				.getBodyParts().get(0)
				.getFrames().get(0)
				.getPivotY();
		float minX = animationDataList.get(0)
				.getMovements().get(0)
				.getBodyParts().get(0)
				.getFrames().get(0).getX()
				- animationDataList.get(0)
				.getMovements().get(0)
				.getBodyParts().get(0)
				.getFrames().get(0)
				.getPivotX();
		
		// set the position according to the first movement's minX and minY
		Iterator<AnimationData> animationDataIter = animationDataList.iterator();
		while (animationDataIter.hasNext()) {
			AnimationData animationData = (AnimationData) animationDataIter.next();
			
			Movement firstMovement = animationData.getMovements().get(0);
				
			Iterator<BodyPart> bodyPartIter = firstMovement.getBodyParts().iterator();
			while (bodyPartIter.hasNext()) {
				BodyPart bodyPart = (BodyPart) bodyPartIter.next();

				if (minY > bodyPart.getFrames().get(0).getY() - bodyPart.getFrames().get(0).getPivotY())
					minY = bodyPart.getFrames().get(0).getY() - bodyPart.getFrames().get(0).getPivotY();
					
				if (minX > bodyPart.getFrames().get(0).getX() - bodyPart.getFrames().get(0).getPivotX())
					minX = bodyPart.getFrames().get(0).getX() - bodyPart.getFrames().get(0).getPivotX();
			}
		}
		
		if (minY < 0)
			minY = (-minY);
		
		if (minX < 0)
			minX = (-minX);
		
		// set all the parts according to the pivots part X and Y
		float distanceToX = minX;
		float distanceToY = minY;
		
		animationDataIter = animationDataList.iterator();
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
						
						// set the distances in order to calculate the frame's world coords
						frame.setDistanceToX(distanceToX);
						frame.setDistanceToY(distanceToY);
					}
				}
			}
		}
		
		return animationDataList;
	}
}