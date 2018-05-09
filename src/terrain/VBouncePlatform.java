package terrain;


import entities.*;
import main.Images;
import processing.core.*;
import util.*;

public class VBouncePlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength = 1.05f;
	private float vBounceStrength = -1.5f;
	private float maxVel = -20f;

	public VBouncePlatform(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onCollisionUp(Entity e) {
		if (e.getVely() >= 0)
			return;

		float vy = e.getVely();
		vy *= vBounceStrength;
		e.setVely(vy);

		float vx = e.getVelx();
		vx *= hBounceStrength;
		e.setVelx(vx);
	}

	@Override
	public void onCollisionDown(Entity e) {
		if (e.getVely() <= 0)
			return;

		float vy = e.getVely();
		vy *= vBounceStrength;
		vy = Math.max(vy, maxVel);
		e.setVely(vy);

		float vx = e.getVelx();
		vx *= hBounceStrength;
		e.setVelx(vx);
	}
	
	@Override
	public void onRender() {
		renderImage(Images.VBounce);
	}

}
