package terrain;

import java.io.Serializable;

import entities.*;
import main.Images;
import processing.core.*;

/**
 * a block that bounces an entity horizontally (and a tiny bit vertically)
 * @author pilex
 *
 */
public class HBounce extends Platform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength;
	private float vBounceStrength;

	public HBounce(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		hBounceStrength=28f;
		vBounceStrength = -8f;
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
