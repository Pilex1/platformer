package terrain;

import main.Images;
import processing.core.PVector;

/**
 * a frictionless tile that disables all movement
 * @author pilex
 *
 */
public class Ice extends Tile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Ice(PVector pos) {
		super(pos);
		friction = 0;
		allowMovement=false;
		allowJumps=false;
	}
	
	@Override
	public void onLoad() {
		friction = 0;
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		renderImage(Images.Ice);
	}

}
