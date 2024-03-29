package logic;

import java.io.Serializable;
import main.TerrainManager;
import processing.core.PVector;
import util.Color;
import static main.MainApplet.P;

public class Wire extends Connection implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Color inactiveColor = Color.Red;
	protected static final Color activeColor = Color.Purple;
	protected static final float radius = 4;

	public Wire(PVector pos) {
		super(pos);
		solid = false;
	}

	public void updateNetwork() {
		updateNetwork(true, true);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onRender() {
		float cx = hitbox.getCenterX();
		float cy = hitbox.getCenterY();
		P.game.fill(active ? activeColor : inactiveColor);
		P.game.noStroke();
		P.game.rect(cx - radius, cy - radius, 2 * radius, 2 * radius, P.getCamera());
		float offset = TerrainManager.TILE_SIZE / 2 - radius;
		if (hasConnection(Direction.UP)) {
			P.game.rect(cx - radius, cy - TerrainManager.TILE_SIZE / 2, 2 * radius, offset, P.getCamera());
		}
		if (hasConnection(Direction.DOWN)) {
			P.game.rect(cx - radius, cy + radius, 2 * radius, offset, P.getCamera());
		}
		if (hasConnection(Direction.LEFT)) {
			P.game.rect(cx - TerrainManager.TILE_SIZE / 2, cy - radius, offset, 2 * radius, P.getCamera());
		}
		if (hasConnection(Direction.RIGHT)) {
			P.game.rect(cx + radius, cy - radius, offset, 2 * radius, P.getCamera());
		}

	}
}
