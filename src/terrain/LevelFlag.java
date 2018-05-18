package terrain;

import static main.MainApplet.P;

import core.GameCanvas.GameState;
import main.EntityManager;
import main.Images;
import main.LevelManager;
import processing.core.PVector;

/**
 * indicates the end of the level</br>
 * when the player reaches here, they move onto the next level</br>
 * (or the end game screen if all levels are finished)
 * 
 * @author pilex
 *
 */
public class LevelFlag extends Tile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LevelFlag(PVector pos) {
		super(pos);
		solid = false;
	}

	private void nextLevel() {
		int level = LevelManager.getCurrentLevel();
		if (level == LevelManager.getNumberOfLevels() - 1) {
			// you've finished the last level!
			// congrations!
			P.game.setGameState(GameState.End);
		} else {
			// move onto next level
			level++;
			LevelManager.setActiveLevel(level);
		}
	}

	@Override
	public void onUpdate() {
		if (isIntersecting(EntityManager.getPlayer())) {
			nextLevel();
		}
	}

	@Override
	public void onRender() {
		renderImage(Images.LevelFlag, new PVector(0, 50));
	}

}
