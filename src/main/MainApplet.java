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
	public void keyTyped(KeyEvent event) {
		EntityManager.getPlayer().onKeyPress(event.getKey());
	}

	@Override
	public void mousePressed(MouseEvent event) {
		EntityManager.getPlayer().onMousePress(event.getButton());
	}

	@Override
	public void mouseReleased(MouseEvent event) {
		EntityManager.getPlayer().onMouseRelease(event.getButton());
	}

	@Override
	public void mouseWheel(MouseEvent event) {
		EntityManager.getPlayer().onScroll(event.getCount());
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
