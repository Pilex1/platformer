package logic;

import static main.MainApplet.P;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Color;

/**
 * represents a logic tile that requires power to activate
 * 
 * @author pilex
 *
 */
public abstract class Drain extends LogicTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final float radius = 4;
	protected static final float iconRadius = 8f;
	protected static final Color inactiveColor = Color.Red;
	protected static final Color activeColor = Color.Purple;

	protected boolean[] activeSides;

	public Drain(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onLoad() {
		super.onLoad();
		activeSides = new boolean[4];
	}

	@Override
	public void onUpdate() {
		active = false;
		for (Direction dir : Direction.values()) {
			activeSides[dir.ordinal()] = false;
			Tile t = TerrainManager.getTileRelative(this, dir.rotateClockwise(rotation));
			if (t instanceof Connection && ((Connection) t).active) {
				activeSides[dir.ordinal()] = true;
				active = true;
			}
		}
	}

	@Override
	public void onRender() {
		float cx = hitbox.getCenterX();
		float cy = hitbox.getCenterY();
		P.game.noStroke();
		float offset = TerrainManager.TILE_SIZE / 2 - radius;

		P.game.fill(activeSides[Direction.UP.rotateAntiClockwise(rotation).ordinal()] ? activeColor : inactiveColor);
		P.game.rect(cx - radius, cy - TerrainManager.TILE_SIZE / 2, 2 * radius, offset - iconRadius, P.getCamera());
		P.game.fill(activeSides[Direction.DOWN.rotateAntiClockwise(rotation).ordinal()] ?  activeColor : inactiveColor);
		P.game.rect(cx - radius, cy + radius+ iconRadius, 2 * radius, offset-iconRadius, P.getCamera());
		
		P.game.fill(activeSides[Direction.LEFT.rotateAntiClockwise(rotation).ordinal()] ? activeColor : inactiveColor);
		P.game.rect(cx - TerrainManager.TILE_SIZE / 2, cy - radius, offset - iconRadius, 2 * radius, P.getCamera());
		P.game.fill(activeSides[Direction.RIGHT.rotateAntiClockwise(rotation).ordinal()] ?  activeColor : inactiveColor);
		P.game.rect(cx + radius + iconRadius, cy - radius, offset-iconRadius, 2 * radius, P.getCamera());
	}
}
