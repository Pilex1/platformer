package main;

import core.GraphicsComponent;
import core.Layout;
import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class MainApplet extends Applet {

	public static MainApplet P;

	public Game game;

	@Override
	public void setup() {
		super.setup();
		P = this;
	}
	
	@Override
	protected GraphicsComponent mainComponent() {
		game = new Game();
		return game;
	}

	@Override
	public void dispose() {
		super.dispose();
		TerrainManager.savePlatforms();
		EntityManager.saveEntities();
	}

	public PVector getCamera() {
		return EntityManager.getPlayer().getPosTopLeft();
	}

}
