package terrain;

import processing.core.*;
import util.*;
import static main.MainApplet.P;

public class GuidePlatform extends Tile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GuidePlatform(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onRender() {
		P.game.strokeWeight(0);
		P.game.fill(Color.LightGrey.Transparent());
		P.game.rect(hitbox, P.getCamera());
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterRemove() {
		// TODO Auto-generated method stub
		
	}
}
