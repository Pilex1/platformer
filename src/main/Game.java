package main;

import core.DynamicGridLayout;
import core.GameCanvas;
import core.Layout;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import util.Images;

import static main.MainApplet.P;

import components.Button;

public class Game extends GameCanvas {

	// private SoundFile satie;

	private PImage background;

	public Game() {

		super();

		loadTitleScreen();
		loadGame();
		loadPauseScreen();

	}

	private void loadTitleScreen() {
		DynamicGridLayout layout = new DynamicGridLayout();
		layout.setMaxWidth(400);
		layout.addComponent(new Button("Play", () -> {
			System.out.println(0);
			gameState = GameState.Game;
		}), 0, 0);
		layout.addComponent(new Button("Instructions", () -> {
		}), 0, 1);
		layout.addComponent(new Button("Exit", () -> {
			P.exit();
		}), 0, 2);
		titleScreen = layout;
	}

	private void loadGame() {
		// satie = new SoundFile(Processing, "Satie.mp3");
		// satie.loop();

		Images.load();
		TerrainManager.loadPlatforms();
		EntityManager.loadEntities();

		background = Images.background;
		background.resize(P.width, P.height);
	}

	private void loadPauseScreen() {

	}

	@Override
	protected void onUpdate(PVector pos, PVector size) {
		float width, height, x, y;
		switch (gameState) {
		case Game:
			TerrainManager.update();
			EntityManager.update();
			break;
		case Paused:
			width = Math.min(pauseScreenOverlay.getMaxWidth(), P.width);
			height = Math.min(pauseScreenOverlay.getMaxHeight(), P.height);
			x = (P.width - width) / 2;
			y = (P.height - height) / 2;
			pauseScreenOverlay.update(new PVector(x, y), new PVector(width, height));
			break;
		case TitleScreen:
			width = Math.min(titleScreen.getMaxWidth(), P.width);
			height = Math.min(titleScreen.getMaxHeight(), P.height);
			x = (P.width - width) / 2;
			y = (P.height - height) / 2;
			titleScreen.update(new PVector(x, y), new PVector(width, height));
			break;
		default:
			break;

		}
		requestGraphicalUpdate();
	}

	@Override
	protected void onRender(PVector pos, PVector size) {
		float width, height, x, y;
		switch (gameState) {
		case Game:
			P.image(background, 0, 0);
			TerrainManager.render();
			EntityManager.render();
			break;
		case Paused:
			width = Math.min(pauseScreenOverlay.getMaxWidth(), P.width);
			height = Math.min(pauseScreenOverlay.getMaxHeight(), P.height);
			x = (P.width - width) / 2;
			y = (P.height - height) / 2;
			pauseScreenOverlay.render(new PVector(x, y), new PVector(width, height));
			break;
		case TitleScreen:
			width = Math.min(titleScreen.getMaxWidth(), P.width);
			height = Math.min(titleScreen.getMaxHeight(), P.height);
			x = (P.width - width) / 2;
			y = (P.height - height) / 2;
			titleScreen.render(new PVector(x, y), new PVector(width, height));
			break;
		default:
			break;

		}
	}

	@Override
	public void onMousePress(MouseEvent event) {
		switch (gameState) {
		case Game:
			EntityManager.getPlayer().onMousePress(event.getButton());
			break;
		case Paused:
			pauseScreenOverlay.onMousePress(event);
			break;
		case TitleScreen:
			titleScreen.onMousePress(event);
			break;
		default:
			break;
		}
	}

	@Override
	public void onMouseRelease(MouseEvent event) {
		switch (gameState) {
		case Game:
			EntityManager.getPlayer().onMouseRelease(event.getButton());
			break;
		case Paused:
			pauseScreenOverlay.onMouseRelease(event);
			break;
		case TitleScreen:
			titleScreen.onMouseRelease(event);
			break;
		default:
			break;

		}

	}

	@Override
	public void onKeyType(char key) {
		switch (gameState) {
		case Game:
			EntityManager.getPlayer().onKeyPress(key);
			break;
		case Paused:
			pauseScreenOverlay.onKeyType(key);
			break;
		case TitleScreen:
			titleScreen.onKeyType(key);
			break;
		default:
			break;

		}
	}

	@Override
	public void onScroll(MouseEvent event) {
		switch (gameState) {
		case Game:
			EntityManager.getPlayer().onScroll(event.getCount());
			break;
		case Paused:
			pauseScreenOverlay.onScroll(event);
			break;
		case TitleScreen:
			titleScreen.onScroll(event);
			break;
		default:
			break;

		}
	}

}
