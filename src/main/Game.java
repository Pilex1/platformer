package main;

import core.GameCanvas;
import processing.core.*;
import util.Images;

import static main.MainApplet.P;

public class Game extends GameCanvas {

	// private SoundFile satie;
	
	private PImage background;
	
	public Game() {

		super();
		
	
		
		// satie = new SoundFile(Processing, "Satie.mp3");
		// satie.loop();

		Images.load();
		TerrainManager.loadPlatforms();
		EntityManager.loadEntities();

		
		background = Images.background;
		background.resize(P.width, P.height);
		
		/*
		 * float sx = 1100;
		 * 
		 * currentFloor = Floor; addPlatform(new Platform(new PVector(0, Floor), new
		 * PVector(sx, 100)));
		 * 
		 * x += sx; y = Manager.Floor;
		 * 
		 * gen1(); addPlatform(new Platform(new PVector(x, y), new PVector(sx, 100))); x
		 * += sx;
		 * 
		 * gen2(); addPlatform(new Platform(new PVector(x, y), new PVector(sx, 100))); x
		 * += sx;
		 */

	}

	@Override
	protected void onRender(PVector pos, PVector size) {
		P.image(background, 0, 0);
		TerrainManager.render();
		EntityManager.render();
	}

	@Override
	protected void onUpdate(PVector pos, PVector size) {
		TerrainManager.update();
		EntityManager.update();
		requestGraphicalUpdate();
	}
	
}
