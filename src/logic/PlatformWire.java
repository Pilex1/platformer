package logic;

import main.Images;
import processing.core.PVector;

/**
 * a platform with a wire in it
 * @author pilex
 *
 */
public class PlatformWire extends Wire {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlatformWire(PVector pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		solid = true;
	}
	
	@Override
	public void onRender() {
		renderImage(Images.Platform);
		super.onRender();
	}

}
