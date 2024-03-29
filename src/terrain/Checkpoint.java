package terrain;

import main.EntityManager;
import main.Images;
import main.TerrainManager;
import processing.core.*;

public class Checkpoint extends Tile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private boolean checked;

	public Checkpoint(PVector pos) {
		super(pos);
		solid = false;
	}

	public boolean isChecked() {
		return checked;
	}

	@Override
	public void onRender() {
		renderImage(checked ? Images.CheckpointOn : Images.CheckpointOff, new PVector(0, 50));
	}

	@Override
	public void onUpdate() {
		// if the player is on this checkpoint, then we deactivate all other checkpoints
		// and activate this checkpoint
		if (isIntersecting(EntityManager.getPlayer())) {
			for (Checkpoint c : TerrainManager.getAllCheckpoints()) {
				c.checked = false;
			}
			checked = true;
		}
	}

	@Override
	public void onLoad() {
		checked = false;
	}

	@Override
	public void reset() {
		checked = false;
	}
}
