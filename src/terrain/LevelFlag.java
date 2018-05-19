package terrain;

import static main.MainApplet.P;

import core.GameCanvas.GameState;
import main.EntityManager;
import main.Images;
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
		P.game.setGameState(GameState.NextLevel);
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
