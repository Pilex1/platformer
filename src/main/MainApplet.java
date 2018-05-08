package main;

import core.GraphicsComponent;
import processing.core.PVector;

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
		LevelManager.saveCurrentLevel();
	}

	public PVector getCamera() {
		return EntityManager.getPlayer().getPosTopLeft();
	}

}
