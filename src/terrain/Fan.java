package terrain;

import entities.Player;
import logic.Drain;
import logic.LogicTile;
import main.EntityManager;
import main.Images;
import main.TerrainManager;
import processing.core.PVector;

public class Fan extends Drain {

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
		super.onUpdate();
		float range = 8 * TerrainManager.TILE_SIZE;
		float force = 1;
		Player player = EntityManager.getPlayer();
		PVector delta = player.getPos().sub(hitbox.topLeft());
		if (delta.mag() > range) return;
		// the actual rotation is one 90 degree turn clockwise than specified, due to
		// how the actual PNG file is drawn
		 System.out.println(delta);
	//	System.out.println(player.getPos());
		switch (rotation) {
		case UP:
			if (delta.x < 0)
				return;
			player.increaseVelx(force);
			//System.out.println(0);
			break;
		case DOWN:
			if (delta.x > 0)
				return;
			player.increaseVelx(-force);
			break;
		case RIGHT:
			if (delta.y < 0)
				return;
			player.increaseVely(force);
			break;

		case LEFT:
			if (delta.y > 0)
				return;
			player.increaseVely(-force);
			break;
		}
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.FanOn : Images.FanOff);
		super.onRender();
	}

}
