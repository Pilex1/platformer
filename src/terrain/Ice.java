package terrain;

import main.Images;
import processing.core.PVector;

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
	public void onUpdate() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRender() {
		renderImage(Images.Ice);
	}

}
