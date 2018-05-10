package main;

import static main.MainApplet.P;

import components.Button;
import core.DynamicGridLayout;
import core.GameCanvas;
import core.LayoutList;
import processing.core.PImage;
import processing.core.PVector;
import processing.event.MouseEvent;

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

		LayoutList layoutList = new LayoutList();

		DynamicGridLayout home = new DynamicGridLayout();
		DynamicGridLayout levelSelect = new DynamicGridLayout();
		DynamicGridLayout instructions = new DynamicGridLayout();

		{
			home.setMaxWidth(400);
			home.addComponent(new Button("Play", () -> {
				layoutList.setActiveLayout(levelSelect);
			}), 0, 0);
			home.addComponent(new Button("Instructions", () -> {
				layoutList.setActiveLayout(instructions);
			}), 0, 1);
			home.addComponent(new Button("Exit", () -> {
				P.exit();
			}), 0, 2);
		}

		{
			levelSelect.addComponentToCol(new Button("Level 1", () -> {
				LevelManager.setActiveLevel(0);
				gameState = GameState.Game;
				layoutList.setActiveLayout(home);
			}), 0);
			levelSelect.addComponentToCol(new Button("Level 2", () -> {
			}), 0);
			levelSelect.addComponentToCol(new Button("Level 3", () -> {
			}), 0);
			levelSelect.addComponentToCol(new Button("Back", () -> {
				layoutList.setActiveLayout(home);
			}), 0);
		}

		{

			instructions.addComponent(new Button("Back", () -> {
				layoutList.setActiveLayout(home);
			}), 0, 0);
		}

		layoutList.addLayouts(home, levelSelect, instructions);
		titleScreen = layoutList;
	}

	private void loadGame() {
		// satie = new SoundFile(Processing, "Satie.mp3");
		// satie.loop();

		Images.load();
		LevelManager.loadAllLevels();

		background = Images.Background;
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
			P.game.image(background, 0, 0);
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
