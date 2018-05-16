package terrain;

import entities.*;
import main.Images;
import processing.core.*;

// appears to be a regular block, then disappears when standing on it or jumping and hitting it
public class Phantom extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean activated = false;

	public Phantom(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onLoad() {
		reset();
	}
	@Override
	public void onCollisionUp(Entity e) {
		activated = true;
		solid = false;
	}

	@Override
	public void onCollisionDown(Entity e) {
		activated = true;
		solid = false;
	}
	
	@Override
	public void onStanding(Entity e) {
		System.out.println(0);
		activated=true;
		solid=false;
	}

	@Override
	public void onRender() {
		if (!activated) {
			renderImage(Images.Platform);
		}
	}

	@Override
	public void reset() {
		activated = false;
		solid = true;
	}

}
