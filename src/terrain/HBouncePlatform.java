package terrain;

import entities.*;
import processing.core.*;
import util.*;

public class HBouncePlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength = 70f;
	private float vBounceStrength = -8f;
	private float maxVel = -15f;

	public HBouncePlatform(PVector pos) {
		super(pos);
	}

	@Override
	public void onLoad() {
		img = Images.platform_hbounce;
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

}
