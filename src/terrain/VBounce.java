package terrain;


import entities.*;
import main.Images;
import processing.core.*;

/**
 * bounces entities vertically
 * @author pilex
 *
 */
public class VBounce extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float hBounceStrength = 1.2f;
	private float vBounceStrength = -1.2f;

	public VBounce(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		allowRepeatedJumps=false;
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
