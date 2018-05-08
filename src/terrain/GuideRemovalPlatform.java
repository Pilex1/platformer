package terrain;

import static main.MainApplet.P;

import processing.core.*;
import util.*;

public class GuideRemovalPlatform extends Tile {

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
