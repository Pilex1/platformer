package terrain;

import static main.MainApplet.P;

import processing.core.*;
import util.*;

public class GuideRemovalPlatform extends Platform {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuideRemovalPlatform(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onRender() {
		P.game.strokeWeight(1);
		P.game.stroke(Color.Red);
		P.game.fill(Color.Transparent);
		P.game.rect(hitbox, P.getCamera());
	}
}
