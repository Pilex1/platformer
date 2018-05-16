package terrain;

import entities.*;
import processing.core.*;

// invisible until the player jumps and hits the block
public class InvisiblePlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean activated = false;

	public InvisiblePlatform(PVector pos) {
		super(pos);
		solid = false;
	}

	@Override
	public void onCollisionUp(Entity e) {
		activated = true;
		solid = true;
		super.onCollisionUp(e);
	}

	@Override
	public void onRender() {
		if (activated)
			super.onRender();
	}

	@Override
	public void reset() {
		activated = false;
		solid = false;
	}

}
