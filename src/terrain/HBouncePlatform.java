package terrain;

import java.io.Serializable;

import entities.*;
import processing.core.*;
import util.*;

public class HBouncePlatform extends Platform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength = 50f;
	private float vBounceStrength = -8f;

	public HBouncePlatform(PVector pos) {
		super(pos);
	}

	@Override
	public void onCollisionLeft(Entity e) {
		if (e.getVelx() >= 0)
			return;
		e.setVelx(hBounceStrength);
		e.setVely(vBounceStrength);
	}

	@Override
	public void onCollisionRight(Entity e) {
		if (e.getVelx() <= 0)
			return;
		e.setVelx(-hBounceStrength);
		e.setVely(vBounceStrength);
	}
	
	@Override
	public void onRender() {
		renderImage(Images.HBounce);
	}

}
