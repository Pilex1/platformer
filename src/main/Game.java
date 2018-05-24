package main;

import static main.MainApplet.P;

import java.util.ArrayList;

import components.Button;
import components.Label;
import core.DynamicGridLayout;
import core.Fonts;
import core.GameCanvas;
import core.Layout;
import core.LayoutList;
import processing.core.PVector;
import processing.event.MouseEvent;
import util.Color;
import util.EdgeTuple;

public class Game extends GameCanvas {

	private ArrayList<Button> levelButtons;

	private Label label_level;
	private Label label_info;

	private Label instructions1, instructions2, instructions3, controls;

	@Override
	protected Layout initNextLevelScreen() {
		DynamicGridLayout nextLevelScreen = new DynamicGridLayout();

		PVector maxBtnSize = new PVector(700, 120);
		EdgeTuple btnPadding = new EdgeTuple(10);
		PVector maxHeadingSize = new PVector(700, 100);
		int headingTextSize = 36;

		DynamicGridLayout main = new DynamicGridLayout();
		label_level = new Label("");
		label_level.textSize = headingTextSize;
		label_level.setMaxSize(maxHeadingSize);
		main.addComponentToCol(label_level, 0);
		label_info = new Label("");
		main.addComponentToCol(label_info, 0);

		DynamicGridLayout next = new DynamicGridLayout();
		next.addComponent(new Button("Continue", () -> {
			if (LevelManager.getCurrentLevel() == LevelManager.getNumberOfLevels() - 1) {
				// the last level has been completed
				// move to end screen
				setGameState(GameState.End);
			} else {
				// move onto the next level
				LevelManager.unlockLevel(LevelManager.getCurrentLevel() + 1);
				LevelManager.setActiveLevel(LevelManager.getCurrentLevel() + 1);
				setGameState(GameState.Game);
			}
		}), 0, 0);
		next.setMaxSize(maxBtnSize);

		nextLevelScreen.addComponent(main, 0, 0);
		nextLevelScreen.addComponent(next, 0, 1);

		nextLevelScreen.padding = new EdgeTuple(20);

		nextLevelScreen.getAllComponents().forEach(gc -> {
			if (gc instanceof Button) {
				Button b = (Button) gc;
				b.setMaxSize(maxBtnSize);
				b.setPadding(btnPadding);
			}
		});

		return nextLevelScreen;
	}

	@Override
	protected void loadGame() {
		// satie = new SoundFile(Processing, "Satie.mp3");
		// satie.loop();

		Images.load();
		LevelManager.loadAllLevels();

		instructions1 = new Label(
				"The game is split into multiple levels, which progressively increase in difficulty. Levels must be completed in order before the next level is unlocked.",
				20);
		instructions2 = new Label(
				"Each level contains a variety of obstacles that you will need to overcome. To complete a level, you must navigate to the level marker, indicated by a blue flag.",
				20);
		instructions3 = new Label(
				"A helpful Guide will accompany you through each level, offering helpful tips and advice.", 20);
		controls = new Label("W A S D : Movement\nEsc : Pause game\nEnter: Talk to Guide", 20);
	}

	@Override
	protected Layout initTitleScreen() {
		LayoutList layoutList = new LayoutList();

		DynamicGridLayout home = new DynamicGridLayout();
		DynamicGridLayout levelSelect = new DynamicGridLayout();
		DynamicGridLayout instructions = new DynamicGridLayout();

		PVector maxBtnSize = new PVector(700, 120);
		EdgeTuple btnPadding = new EdgeTuple(10);
		PVector maxHeadingSize = new PVector(700, 100);
		int headingTextSize = 36;
		{

			DynamicGridLayout home_main = new DynamicGridLayout();
			Label title = new Label("お誕生日おめでとうございます!");
			title.setFont(Fonts.Japanese);
			title.textSize = headingTextSize;
			title.setMaxSize(maxHeadingSize);
			home_main.addComponentToCol(title, 0);
			home_main.addComponentToCol(new Button("Play", () -> {
				layoutList.setActiveLayout(levelSelect);
			}), 0);
			home_main.addComponentToCol(new Button("Instructions", () -> {
				layoutList.setActiveLayout(instructions);
			}), 0);

			DynamicGridLayout home_exit = new DynamicGridLayout();
			home_exit.addComponent(new Button("Exit", () -> {
				P.exit();
			}), 0, 0);
			home_exit.setMaxSize(maxBtnSize);

			home.addComponent(home_main, 0, 0);
			home.addComponent(home_exit, 0, 1);
		}

		{
			DynamicGridLayout levelSelect_main = new DynamicGridLayout();
			Label title = new Label("Level Selection");
			title.textSize = headingTextSize;
			title.setMaxSize(maxHeadingSize);
			levelSelect_main.addComponentToCol(title, 0);
			levelButtons = new ArrayList<Button>();
			for (int i = 0; i < LevelManager.getNumberOfLevels(); i++) {
				final int level = i;
				Button btn = new Button("Level " + (i + 1), () -> {
					LevelManager.setActiveLevel(level);
					setGameState(GameState.Game);
					layoutList.setActiveLayout(home);
				});
				levelSelect_main.addComponentToCol(btn, 0);
				levelButtons.add(btn);
			}

			DynamicGridLayout levelSelect_back = new DynamicGridLayout();
			levelSelect_back.addComponentToCol(new Button("Back", () -> {
				layoutList.setActiveLayout(home);
			}), 0);
			levelSelect_back.setMaxSize(maxBtnSize);

			levelSelect.addComponent(levelSelect_main, 0, 0);
			levelSelect.addComponent(levelSelect_back, 0, 1);
		}

		{
			DynamicGridLayout instructions_main = new DynamicGridLayout();
			Label title1 = new Label("Instructions");
			title1.textSize = headingTextSize;
			title1.setMaxSize(maxHeadingSize);
			instructions_main.addComponentToCol(title1, 0);
			instructions_main.addComponentToCol(instructions1, 0);
			instructions_main.addComponentToCol(instructions2, 0);
			instructions_main.addComponentToCol(instructions3, 0);
			Label title2 = new Label("Controls");
			title2.textSize = headingTextSize;
			title2.setMaxSize(maxHeadingSize);
			instructions_main.addComponentToCol(title2, 0);
			instructions_main.addComponentToCol(controls, 0);
			DynamicGridLayout instructions_back = new DynamicGridLayout();
			instructions_back.addComponent(new Button("Back", () -> {
				layoutList.setActiveLayout(home);
			}), 0, 0);
			instructions_back.setMaxSize(maxBtnSize);

			instructions.addComponent(instructions_main, 0, 0);
			instructions.addComponent(instructions_back, 0, 1);
		}

		layoutList.addLayouts(home, levelSelect, instructions);
		layoutList.getLayouts().forEach(l -> l.padding = new EdgeTuple(20));

		layoutList.getAllComponents().forEach(gc -> {
			if (gc instanceof Button) {
				Button b = (Button) gc;
				b.setMaxSize(maxBtnSize);
				b.setPadding(btnPadding);
			}
		});

		return layoutList;
	}

	@Override
	protected Layout initPauseScreen() {
		LayoutList layoutList = new LayoutList();
		DynamicGridLayout home = new DynamicGridLayout();
		DynamicGridLayout instructions = new DynamicGridLayout();

		PVector maxBtnSize = new PVector(700, 120);
		EdgeTuple btnPadding = new EdgeTuple(10);
		PVector maxHeadingSize = new PVector(700, 100);
		int headingTextSize = 36;
		{

			DynamicGridLayout home_main = new DynamicGridLayout();
			Label title = new Label("Paused");
			title.textSize = headingTextSize;
			title.setMaxSize(maxHeadingSize);
			home_main.addComponentToCol(title, 0);
			home_main.addComponentToCol(new Button("Return to game", () -> {
				P.game.setGameState(GameState.Game);
			}), 0);
			home_main.addComponentToCol(new Button("Return to last checkpoint", () -> {
				EntityManager.getPlayer().teleportToCheckpoint(TerrainManager.getActiveCheckpoint());
				P.game.setGameState(GameState.Game);
			}), 0);
			home_main.addComponentToCol(new Button("Instructions", () -> {
				layoutList.setActiveLayout(instructions);
			}), 0);

			DynamicGridLayout home_exit = new DynamicGridLayout();
			home_exit.addComponent(new Button("Return to title screen", () -> {
				LevelManager.saveCurrentLevel();
				P.game.setGameState(GameState.Title);
			}), 0, 0);
			home_exit.setMaxSize(maxBtnSize);

			home.addComponent(home_main, 0, 0);
			home.addComponent(home_exit, 0, 1);

		}

		{
			DynamicGridLayout instructions_main = new DynamicGridLayout();
			Label title1 = new Label("Instructions");
			title1.textSize = headingTextSize;
			title1.setMaxSize(maxHeadingSize);
			instructions_main.addComponentToCol(title1, 0);
			instructions_main.addComponentToCol(instructions1, 0);
			instructions_main.addComponentToCol(instructions2, 0);
			instructions_main.addComponentToCol(instructions3, 0);
			Label title2 = new Label("Controls");
			title2.textSize = headingTextSize;
			title2.setMaxSize(maxHeadingSize);
			instructions_main.addComponentToCol(title2, 0);
			instructions_main.addComponentToCol(controls, 0);
			DynamicGridLayout instructions_back = new DynamicGridLayout();
			instructions_back.addComponent(new Button("Back", () -> {
				layoutList.setActiveLayout(home);
			}), 0, 0);
			instructions_back.setMaxSize(maxBtnSize);

			instructions.addComponent(instructions_main, 0, 0);
			instructions.addComponent(instructions_back, 0, 1);
		}

		layoutList.addLayouts(home, instructions);
		layoutList.getLayouts().forEach(l -> l.padding = new EdgeTuple(20));

		layoutList.getAllComponents().forEach(gc -> {
			if (gc instanceof Button) {
				Button b = (Button) gc;
				b.setMaxSize(maxBtnSize);
				b.setPadding(btnPadding);
			}
		});
		layoutList.setBackgroundColorRecursive(new Color(0, 0, 0));

		return layoutList;
	}

	@Override
	protected Layout initEndScreen() {
		DynamicGridLayout end = new DynamicGridLayout();

		PVector maxBtnSize = new PVector(700, 120);
		EdgeTuple btnPadding = new EdgeTuple(10);
		PVector maxHeadingSize = new PVector(700, 100);
		int headingTextSize = 36;

		DynamicGridLayout home_main = new DynamicGridLayout();
		Label title = new Label("おめでとう!");
		title.textSize = headingTextSize;
		title.setMaxSize(maxHeadingSize);
		title.setFont(Fonts.Japanese);
		home_main.addComponentToCol(title, 0);
		home_main.addComponentToCol(new Label(
				"If the Guide really didn't have any control over what he said, would he really be able to have that final conversation with you? The Guide is the real evil mastermind behind everything. After all it's him that designed all the levels and lead you through them right?!"),
				0);
		home_main.addComponentToCol(
				new Label("You have completed all the levels and finished the game.\nThanks for playing!"), 0);

		DynamicGridLayout home_exit = new DynamicGridLayout();
		home_exit.addComponent(new Button("Return to title screen", () -> {
			LevelManager.saveCurrentLevel();
			P.game.setGameState(GameState.Title);
		}), 0, 0);
		home_exit.setMaxSize(maxBtnSize);

		end.addComponent(home_main, 0, 0);
		end.addComponent(home_exit, 0, 1);

		end.padding = new EdgeTuple(20);

		end.getAllComponents().forEach(gc -> {
			if (gc instanceof Button) {
				Button b = (Button) gc;
				b.setMaxSize(maxBtnSize);
				b.setPadding(btnPadding);
			}
		});

		return end;
	}

	@Override
	protected void updateGame() {
		P.game.image(Images.Background, new PVector(0, 0));
		TerrainManager.update();
		EntityManager.update();

		// autosave just in case the game crashes!
		// if (P.frameCount % (60 * 60 * 1) == 0) {
		// LevelManager.saveCurrentLevel();
		// System.out.println("autosaved");
		// }
	}

	@Override
	protected void renderGame() {
		TerrainManager.render();
		EntityManager.render();
	}

	@Override
	protected void onMousePressGame(MouseEvent event) {
		EntityManager.getPlayer().onMousePress(event.getButton());
	}

	@Override
	protected void onMouseReleaseGame(MouseEvent event) {
		EntityManager.getPlayer().onMouseRelease(event.getButton());
	}

	@Override
	protected void onKeyPressGame(char key) {
		EntityManager.getPlayer().onKeyPress(key);
	}

	@Override
	protected void onScrollGame(MouseEvent event) {
		EntityManager.getPlayer().onScroll(event.getCount());
	}

	@Override
	protected void onGameStateChange(GameState state) {
		if (state == GameState.Title) {
			for (int i = 0; i < levelButtons.size(); i++) {
				Button btn = levelButtons.get(i);
				btn.disabled = !LevelManager.isUnlocked(i);
			}
		}
		if (state == GameState.NextLevel) {
			label_level.setText("Level " + (LevelManager.getCurrentLevel() + 1) + " complete!");
			label_info.setText("\nDeaths: " + EntityManager.getPlayer().getDeaths());
		}
	}
}
