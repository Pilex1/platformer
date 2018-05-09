package terrain;

import java.io.*;

import main.Images;
import processing.core.PVector;
import util.*;

public class Platform extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Platform(PVector pos) {
		super(pos);
	}

	@Override
	public void reset() {
	}

	@Override
	public void onUpdate() {
	}
	
	@Override
	public void onRender() {
		renderImage(Images.Platform);
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterRemove() {
		// TODO Auto-generated method stub
		
	}


}
