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

	protected boolean active;

	protected LogicTile(PVector pos) {
		super(pos);
	}

	public boolean isActive() {
		return active;
	}


	@Override
	public void onLoad() {
		neighbouringUpdate();
		getAdjacentConnections().forEach(w -> {
			w.neighbouringUpdate();
		});
	}

	/**
	 * this gets called when a neighbouring logic tile is added/removed
	 */
	public void neighbouringUpdate() {
		
	}
	
	protected void updateAdjacent() {
		getAdjacentConnections().forEach(w -> w.updateNetwork());
	}

	@Override
	public void afterRemove() {
		updateAdjacent();
	}
	
	@Override
	public void reset() {
	}

	protected ArrayList<Connection> getAdjacentConnections() {
		return getAdjacentConnections(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
	}

	protected ArrayList<Connection> getAdjacentConnections(Direction... directions) {
		Vector2i v = getTileId();
		ArrayList<Connection> list = new ArrayList<>();

		Tile up = TerrainManager.getTileById(v.x, v.y - 1);
		Tile right = TerrainManager.getTileById(v.x + 1, v.y);
		Tile down = TerrainManager.getTileById(v.x, v.y + 1);
		Tile left = TerrainManager.getTileById(v.x - 1, v.y);

		for (Direction dir : directions) {
			switch (dir) {
			case UP:
				if (up instanceof Connection) {
					list.add((Connection) up);
				}
				break;
			case DOWN:
				if (down instanceof Connection) {
					list.add((Connection) down);
				}
				break;
			case LEFT:
				if (left instanceof Connection) {
					list.add((Connection) left);
				}
				break;
			case RIGHT:
				if (right instanceof Connection) {
					list.add((Connection) right);
				}
				break;
			}
		}
		return list;
	}
	
	protected ArrayList<LogicTile> getAdjacentLogics(){
		return getAdjacentLogics(Direction.UP,Direction.DOWN,Direction.LEFT,Direction.RIGHT);
	}

	protected ArrayList<LogicTile> getAdjacentLogics(Direction... directions) {
		Vector2i v = getTileId();
		ArrayList<LogicTile> list = new ArrayList<>();

		Tile up = TerrainManager.getTileById(v.x, v.y - 1);
		Tile right = TerrainManager.getTileById(v.x + 1, v.y);
		Tile down = TerrainManager.getTileById(v.x, v.y + 1);
		Tile left = TerrainManager.getTileById(v.x - 1, v.y);

		for (Direction dir : directions) {
			switch (dir) {
			case UP:
				if (up instanceof LogicTile) {
					list.add((LogicTile) up);
				}
				break;
			case DOWN:
				if (down instanceof LogicTile) {
					list.add((LogicTile) down);
				}
				break;
			case LEFT:
				if (left instanceof LogicTile) {
					list.add((LogicTile) left);
				}
				break;
			case RIGHT:
				if (right instanceof LogicTile) {
					list.add((LogicTile) right);
				}
				break;
			}
		}
		return list;
	}

}
