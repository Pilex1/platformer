package terrain;

import main.Images;
import processing.core.PVector;

public class InterfacePlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InterfacePlatform(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onRender() {
		if (solid) {
			renderImage(Images.Platform);
		}
	}

}