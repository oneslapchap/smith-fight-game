package com.me.steel.Utils.TweenAnimation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.XmlReader;

public class ArmatureLoader {
	
	private List<Vector2> offsets;
	
	/** read the pivot offsets of the armature */
	public List<Vector2> loadArmatureOffsets(FileHandle fileHandle) throws IOException {

		offsets = new ArrayList<Vector2>();
		
		// read xml and parse its data
		XmlReader xmlReader = new XmlReader();
		XmlReader.Element xmlElement = xmlReader.parse(fileHandle);
		
		Iterator<XmlReader.Element> armaturesIterator = xmlElement.getChildrenByName("armatures").iterator();
		while (armaturesIterator.hasNext()) {
			XmlReader.Element armaturesElement = (XmlReader.Element) armaturesIterator.next();
			
			Iterator<XmlReader.Element> armatureIterator = armaturesElement.getChildrenByName("armature").iterator();
			while (armatureIterator.hasNext()) {
				XmlReader.Element armatureElement = (XmlReader.Element) armatureIterator.next();
				
				Iterator<XmlReader.Element> bIterator = armatureElement.getChildrenByName("b").iterator();
				while (bIterator.hasNext()) {
					XmlReader.Element bElement = (XmlReader.Element) bIterator.next();
					
					Iterator<XmlReader.Element> dIterator = bElement.getChildrenByName("d").iterator();
					while (dIterator.hasNext()) {
						XmlReader.Element dElement = (XmlReader.Element) dIterator.next();
						
						String pX = dElement.getAttribute("pX");
						String pY = dElement.getAttribute("pY");
						
						offsets.add(new Vector2(
								Float.parseFloat(pX),
								Float.parseFloat(pY)));
					}
				}
			}
		}
		
		return offsets;
	}
}