package terrain;

import entities.*;
import processing.core.*;

// appears to be a regular block, then disappears when standing on it or jumping and hitting it
public class PhantomPlatform extends Platform {

	public static final String Id = "PH";

	private boolean activated = false;

	public PhantomPlatform(PVector pos) {
		super(pos);
	}

	@Override
	public String getId() {
		return Id;
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
	public void onRender(PVector pos, PVector size) {
		if (!activated)
			super.onRender(pos, size);
	}

	public void reset() {
		activated = false;
	}

}
