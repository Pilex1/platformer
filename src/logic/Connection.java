package logic;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.AbstractMap.SimpleEntry;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Vector2i;

public abstract class Connection extends LogicTile {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * indicates whether the wire should be connected to the corresponding direction
	 * i.e. there is a wire or some other logic component
	 */
	protected boolean connections[] = new boolean[4];

	protected Connection(PVector pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}

	protected void updateConnections() {
		for (Direction dir : Direction.values()) {
			connections[dir.ordinal()] = TerrainManager.getTileRelative(this, dir) instanceof LogicTile;
		}
	}
	
	@Override
	public void onUpdate() {
	}

	private class PortalVertical extends Wire {

		private PortalVertical(Vector2i id) {
			super(new PVector(id.x * TerrainManager.TILE_SIZE, id.y * TerrainManager.TILE_SIZE));
		}

		@Override
		protected void updateConnections() {
			super.updateConnections();
			connections[Direction.LEFT.ordinal()] = connections[Direction.RIGHT.ordinal()] = false;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof PortalVertical))
				return false;
			PortalVertical p = (PortalVertical) obj;
			return p.pos.equals(pos);
		}

	}

	private class PortalHorizontal extends Wire {

		private PortalHorizontal(Vector2i id) {
			super(new PVector(id.x * TerrainManager.TILE_SIZE, id.y * TerrainManager.TILE_SIZE));
		}

		@Override
		protected void updateConnections() {
			super.updateConnections();
			connections[Direction.UP.ordinal()] = connections[Direction.DOWN.ordinal()] = false;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof PortalHorizontal))
				return false;
			PortalHorizontal p = (PortalHorizontal) obj;
			return p.pos.equals(pos);
		}

	}

	protected abstract void updateNetwork();

	protected void updateNetwork(boolean horizontal, boolean vertical) {
		boolean state = false;
		/**
		 * stores all the wires that we have already traversed, so they are not
		 * traversed again
		 */
		HashSet<Connection> allConnections = new HashSet<>();

		/**
		 * the current wires being traversed
		 */
		HashSet<Connection> search = new HashSet<>();

		if (this instanceof PortalIntoTheThirdDimension) {
			if (horizontal) {
				allConnections.add(new PortalHorizontal(getTileId()));
				search.add(new PortalHorizontal(getTileId()));
			} else if (vertical) {
				allConnections.add(new PortalVertical(getTileId()));
				search.add(new PortalVertical(getTileId()));
			}
		} else {
			allConnections.add(this);
			search.add(this);
		}
		/*
		 * if (horizontal) { Tile left = TerrainManager.getTileById(thisId.x - 1,
		 * thisId.y); Tile right = TerrainManager.getTileById(thisId.x + 1, thisId.y);
		 * 
		 * }
		 * 
		 * if (o == Orientation.Horizontal || o == Orientation.Both) { // if on the left
		 * or the right there is a component, add both
		 * 
		 * if ((left instanceof LogicTile) || (right instanceof LogicTile)) {
		 * 
		 * if (((LogicTile) t).isIntermediate() && ((LogicTile) t).isActive()) {
		 * connectedToIntermediate = true; state = true; } else if (t instanceof Wire &&
		 * !allConnections.contains(t)) { search.add((Wire) t);
		 * allConnections.add((Wire) t); } else if (t instanceof Emitter) { if
		 * (((Emitter) t).outputSignal()) { connectedToActiveEmitter = true; state =
		 * true; } } } }
		 * 
		 * if (o == Orientation.Vertical || o == Orientation.Both) { // if on the top or
		 * bottom there is a component, add both Tile up =
		 * TerrainManager.getTileById(thisId.x, thisId.y - 1); Tile down =
		 * TerrainManager.getTileById(thisId.x, thisId.y + 1); if ((up instanceof
		 * LogicTile) || (down instanceof LogicTile)) { search.add(up);
		 * search.add(down); } }
		 */

		while (search.size() > 0) {

			for (Connection c : search.toArray(new Connection[0])) {

				/**
				 * contains the neighbouring tiles to the block
				 */
				ArrayList<SimpleEntry<Direction, LogicTile>> list = new ArrayList<>();

				// populates the list with neighbouring tiles
				// note for portals, only the tiles in the given direction are added
				
				c.updateConnections();

				for (Direction dir : Direction.values()) {
					if (c.connections[dir.ordinal()]) {
						SimpleEntry<Direction, LogicTile> entry = new SimpleEntry<>(dir,
								(LogicTile) TerrainManager.getTileRelative(c, dir));
						list.add(entry);
					}
				}

				for (SimpleEntry<Direction, LogicTile> entry : list) {

					Direction dir = entry.getKey();
					LogicTile t = entry.getValue();

					// convert portals into horizontal and vertical components
					// a horizontal portal is only added if there is at least one logic tile
					// on the left or the right of the portal connected to the network
					// similarly for vertical portals
					if (t instanceof PortalIntoTheThirdDimension) {
						Vector2i id_t = t.getTileId();

						Tile up = TerrainManager.getTileById(id_t.x, id_t.y - 1);
						Tile down = TerrainManager.getTileById(id_t.x, id_t.y + 1);
						Tile left = TerrainManager.getTileById(id_t.x - 1, id_t.y);
						Tile right = TerrainManager.getTileById(id_t.x + 1, id_t.y);

						PortalHorizontal ph = new PortalHorizontal(id_t);
						PortalVertical pv = new PortalVertical(id_t);
						if ((left instanceof LogicTile && allConnections.contains(left))
								|| (right instanceof LogicTile && allConnections.contains(right))) {
							if (!allConnections.contains(ph)) {
								search.add(ph);
								allConnections.add(ph);
							}
						}
						if ((up instanceof LogicTile && allConnections.contains(up))
								|| (down instanceof LogicTile && allConnections.contains(down))) {
							if (!allConnections.contains(pv)) {
								search.add(pv);
								allConnections.add(pv);
							}
						}
					} else if (t instanceof Connection) {
						if (!allConnections.contains(t)) {
							search.add((Connection) t);
							allConnections.add((Connection) t);
						}

					} else if (t instanceof Emitter) {
						if (((Emitter) t).outputSignal(dir.opposite())) {
							state = true;
						}
					}
				}

				search.remove(c);
			}

		}

		for (Connection w : allConnections) {
			if (w instanceof PortalHorizontal) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) TerrainManager.getTileById(w.getTileId());
				p.hActive = state;
			} else if (w instanceof PortalVertical) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) TerrainManager.getTileById(w.getTileId());
				p.vActive = state;
			}
			w.active = state;
		}

	}

}
