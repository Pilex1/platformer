package terrain;

import java.io.*;

import main.Images;
import processing.core.PVector;

/**
 * a good ol' regular tile
 * @author pilex
 *
 */
public class Platform extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Platform(PVector pos) {
		super(pos);
	}

	@Override
	public void onUpdate() {
	}
	
	@Override
	public void onRender() {
		renderImage(Images.Platform);
	}

}
