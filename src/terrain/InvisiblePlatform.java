package terrain;

import entities.*;
import processing.core.*;

// invisible until the player jumps and hits the block
public class InvisiblePlatform extends Platform {

	public static final String Id = "I";

	private boolean activated = false;

	public InvisiblePlatform(PVector pos) {
		super(pos);
		solid = false;
	}

	@Override
	public String getId() {
		return Id;
	}

	@Override
	public void onCollisionUp(Entity e) {
		activated = true;
		solid = true;
		super.onCollisionUp(e);
	}

	@Override
	public void onRender(PVector pos, PVector size) {
		if (activated)
			super.onRender(pos, size);
	}

	public void reset() {
		activated = false;
		solid = false;
	}

}
