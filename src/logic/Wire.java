package logic;

import java.io.Serializable;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashSet;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Color;
import util.Vector2i;

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

	@Override
	public void neighbouringUpdate() {
		updateNetwork();
		updateConnections();
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
		if (connections[Direction.UP.ordinal()]) {
			P.game.rect(cx - radius, cy - TerrainManager.TILE_SIZE / 2, 2 * radius, offset, P.getCamera());
		}
		if (connections[Direction.DOWN.ordinal()]) {
			P.game.rect(cx - radius, cy + radius, 2 * radius, offset, P.getCamera());
		}
		if (connections[Direction.LEFT.ordinal()]) {
			P.game.rect(cx - TerrainManager.TILE_SIZE / 2, cy - radius, offset, 2 * radius, P.getCamera());
		}
		if (connections[Direction.RIGHT.ordinal()]) {
			P.game.rect(cx + radius, cy - radius, offset, 2 * radius, P.getCamera());
		}

	}
}
