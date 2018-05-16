package terrain;

import java.io.Serializable;

import entities.*;
import main.Images;
import processing.core.*;

public class HBounce extends Platform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength = 35f;
	private float vBounceStrength = -8f;

	public HBounce(PVector pos) {
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
