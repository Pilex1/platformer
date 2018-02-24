package main;

import core.StaticGridLayout;
import processing.core.PVector;

public class MainApplet extends Applet {

	public static MainApplet P;

	public Game game;
	public boolean debug = true;

	@Override
	public void setup() {
		super.setup();
		P = this;
		game = new Game();
		
		StaticGridLayout layout = new StaticGridLayout(1, 1);
		layout.addComponent(game);
		frame.layout = layout;
	}

	// background(120, 150, 210);

	@Override
	public void keyTyped() {
		game.getPlayer().updateOnKeyTyped(key);
	}

	@Override
	public void mousePressed() {
		game.getPlayer().updateOnMousePressed(mouseButton);
	}

	@Override
	public void dispose() {
		super.dispose();
		game.savePlatforms();
	}
	
	public PVector getCamera() {
		return game.getPlayer().getPosTopLeft();
	}

}
