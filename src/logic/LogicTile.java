package logic;

import java.io.Serializable;
import java.util.ArrayList;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Vector2i;

public abstract class LogicTile extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean active = false;

	protected LogicTile(PVector pos) {
		super(pos);
	}

	public boolean isActive() {
		return active;
	}
	
	/**
	 * used to indicate that a logic tile does not emit a signal
	 * however lets a signal pass through depending on certain conditions
	 * e.g. AND gate
	 * @return
	 */
	protected boolean isIntermediate() {
		return false;
	}

	@Override
	public void onLoad() {
		neighbouringUpdate();
		getNeighbouringWires().forEach(w -> {
			w.neighbouringUpdate();
		});
	}
	
	/**
	 * this gets called when a neighbouring logic tile is added/removed
	 */
	public abstract void neighbouringUpdate();

	@Override
	public void onUpdate() {
	}

	@Override
	public void afterRemove() {
		getNeighbouringLogicTiles().forEach(logic -> {
			logic.neighbouringUpdate();
		});
	}

	protected ArrayList<Wire> getNeighbouringWires() {
		Vector2i v = getTileId();
		ArrayList<Wire> list = new ArrayList<>();

		Tile up = TerrainManager.getTileById(v.x, v.y - 1);
		Tile right = TerrainManager.getTileById(v.x + 1, v.y);
		Tile down = TerrainManager.getTileById(v.x, v.y + 1);
		Tile left = TerrainManager.getTileById(v.x - 1, v.y);

		if (up instanceof Wire) {
			list.add((Wire) up);
		}
		if (right instanceof Wire) {
			list.add((Wire) right);
		}
		if (down instanceof Wire) {
			list.add((Wire) down);
		}
		if (left instanceof Wire) {
			list.add((Wire) left);
		}
		return list;
	}

	protected ArrayList<LogicTile> getNeighbouringLogicTiles() {
		Vector2i v = getTileId();
		ArrayList<LogicTile> list = new ArrayList<>();

		Tile up = TerrainManager.getTileById(v.x, v.y - 1);
		Tile right = TerrainManager.getTileById(v.x + 1, v.y);
		Tile down = TerrainManager.getTileById(v.x, v.y + 1);
		Tile left = TerrainManager.getTileById(v.x - 1, v.y);

		if (up instanceof LogicTile) {
			list.add((LogicTile) up);
		}
		if (right instanceof LogicTile) {
			list.add((LogicTile) right);
		}
		if (down instanceof LogicTile) {
			list.add((LogicTile) down);
		}
		if (left instanceof LogicTile) {
			list.add((LogicTile) left);
		}
		return list;
	}

}
