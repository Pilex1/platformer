package terrain;

import entities.Player;
import logic.LogicTile;
import main.EntityManager;
import processing.core.PVector;

public class Fan extends LogicTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Fan(PVector pos) {
		super(pos);
		allowRotations = true;
	}

	@Override
	public void onUpdate() {
		float force = 5;
		Player player = EntityManager.getPlayer();
		PVector delta = player.getPos().sub(hitbox.topLeft());
		// the actual rotation is one 90 degree turn clockwise than specified, due to
		// how the actual PNG file is drawn
		switch (rotation) {
		case DOWN:
			if (delta.x > 0)
				return;
			player.getHitbox().incrX(-force);
			break;
		case LEFT:
			if (delta.y > 0)
				return;
			player.getHitbox().incrY(-force);
			break;
		case RIGHT:
			if (delta.y < 0)
				return;
			player.getHitbox().incrY(force);
			break;
		case UP:
			if (delta.x < 0)
				return;
			player.getHitbox().incrX(force);
			break;

		}
	}

	@Override
	public void onRender() {
		// TODO Auto-generated method stub

	}

}
