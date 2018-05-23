package terrain;

import entities.*;
import processing.core.*;

// invisible until the player jumps and hits the block
public class Invisible extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean activated = false;

	public Invisible(PVector pos) {
		super(pos);
		solid = false;
	}

	@Override
	public void onLoad() {
		super.onLoad();
		reset();
	}

	@Override
	public void onCollisionUp(Entity e) {
		// we don't want the tile to activate if the player is standing INSIDE it
		if (Math.abs(e.getHitbox().getY1() - getHitbox().getY2()) > Entity.EPSILON)
			return;

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
