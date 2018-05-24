package terrain;

import entities.Entity;
import logic.Drain;
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
		if (!active)
			return;
		for (Entity e : EntityManager.getAllEntities()) {
			float range = 8 * TerrainManager.TILE_SIZE;
			float force = 1;
			PVector delta = e.getPos().sub(hitbox.topLeft());
			if (delta.mag() > range)
				continue;
			// the actual rotation is one 90 degree turn clockwise than specified, due to
			// how the actual PNG file is drawn
			// System.out.println(delta);
			
			//System.out.println(e);

			
			switch (rotation) {
			case UP:
				if (delta.x < 0)
					continue;
				e.increaseVelx(force);
				break;
			case DOWN:
				if (delta.x > 0)
					continue;
				e.increaseVelx(-force);
				break;
			case RIGHT:
				if (delta.y < 0)
					continue;
				e.increaseVely(force);
				break;

			case LEFT:
				if (delta.y > 0)
					continue;
				e.increaseVely(-force);
				break;
			}
		}
		
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.FanOn : Images.FanOff);
		super.onRender();
	}

}
