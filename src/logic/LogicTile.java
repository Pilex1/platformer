package logic;

import java.io.Serializable;
import java.util.ArrayList;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;

public abstract class LogicTile extends Tile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected boolean active;

	public LogicTile(PVector pos) {
		super(pos);
	}
	
	public boolean isActive() {
		return active;
	}
	
	protected boolean canHaveConnection(Direction dir) {
		return true;
	}

	@Override
	public void rotate() {
		super.rotate();
		if (allowRotations) {
			updateAround();
		}
	}


	@Override
	public void onLoad() {
		updateAround();
	}

	protected void updateAround() {
		if (this instanceof Connection) {
			((Connection)this).updateNetwork();
		}
		getAdjacentConnections().forEach(w -> w.updateNetwork());
	}

	@Override
	public void afterRemove() {
		getAdjacentConnections().forEach(w -> w.updateNetwork());
	}
	
	@Override
	public void reset() {
	}

	protected ArrayList<Connection> getAdjacentConnections() {
		return getAdjacentConnections(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
	}

	protected ArrayList<Connection> getAdjacentConnections(Direction... directions) {
		ArrayList<Connection> list = new ArrayList<>();
		for (Direction dir : directions) {
			Tile t = TerrainManager.getTileRelative(this, dir.rotateClockwise(rotation));
			if (t instanceof Connection) {
				list.add((Connection)t);
			}
		}
		return list;
	}
	
	protected ArrayList<LogicTile> getAdjacentLogics(){
		return getAdjacentLogics(Direction.UP,Direction.DOWN,Direction.LEFT,Direction.RIGHT);
	}

	protected ArrayList<LogicTile> getAdjacentLogics(Direction... directions) {
		ArrayList<LogicTile> list = new ArrayList<>();
		for (Direction dir : directions) {
			Tile t = TerrainManager.getTileRelative(this, dir.rotateClockwise(rotation));
			if (t instanceof LogicTile) {
				list.add((LogicTile)t);
			}
		}
		return list;
	}

}
