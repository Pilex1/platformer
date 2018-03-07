package terrain;

import entities.*;
import processing.core.*;

// appears to be a regular block, then disappears when standing on it or jumping and hitting it
public class PhantomPlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean activated = false;

	public PhantomPlatform(PVector pos) {
		super(pos);
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
	public void onRender() {
		if (!activated)
			super.onRender();
	}

	@Override
	public void reset() {
		activated = false;
	}

}
