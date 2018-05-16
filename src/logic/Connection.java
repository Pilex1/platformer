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
	private boolean connections[] = new boolean[4];

	protected Connection(PVector pos) {
		super(pos);
	}

	@Override
	public void onLoad() {
		updateAround();
	}
	
	@Override
	protected void updateAround() {
		updateNetwork();
		super.updateAround();
	}

	/**
	 * does not take into account the rotation of the tile to properly take into
	 * account rotation, do hasConnection(dir.add(rotation));
	 * 
	 * @param dir
	 * @return
	 */
	protected boolean hasConnection(Direction dir) {
		return connections[dir.ordinal()];
	}

	protected void setConnection(Direction dir, boolean b) {
		connections[dir.ordinal()] = b;
	}

	protected void updateConnections() {
		for (Direction dir : Direction.values()) {
			setConnection(dir, false);
			Tile t = TerrainManager.getTileRelative(this, dir);
			if (t instanceof LogicTile) {
				LogicTile l = (LogicTile) t;
				if (l.canHaveConnection(dir.opposite().rotateAntiClockwise(l.getRotation()))) {
					Direction connectionDir = dir.opposite();
					connectionDir = connectionDir.rotateAntiClockwise(l.getRotation());
					if (l.canHaveConnection(connectionDir)) {
						setConnection(dir, true);
					}
				}
			}
		}
	}

	@Override
	public void onUpdate() {
	}

	private class PortalVertical extends Wire {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private PortalVertical(Vector2i id) {
			super(new PVector(id.x * TerrainManager.TILE_SIZE, id.y * TerrainManager.TILE_SIZE));
		}

		@Override
		protected void updateConnections() {
			super.updateConnections();
			setConnection(Direction.LEFT, false);
			setConnection(Direction.RIGHT, false);
		}
		
		@Override
		public int hashCode() {
			return pos.hashCode();
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PortalIntoTheThirdDimension) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) obj;
				return p.pos.equals(pos);
			}
			if (obj instanceof PortalVertical) {
				PortalVertical p = (PortalVertical) obj;
				return p.pos.equals(pos);
			}
			return false;
		}

	}

	private class PortalHorizontal extends Wire {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private PortalHorizontal(Vector2i id) {
			super(new PVector(id.x * TerrainManager.TILE_SIZE, id.y * TerrainManager.TILE_SIZE));
		}

		@Override
		protected void updateConnections() {
			super.updateConnections();
			setConnection(Direction.UP, false);
			setConnection(Direction.DOWN, false);
		}

		@Override
		public int hashCode() {
			return pos.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PortalIntoTheThirdDimension) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) obj;
				return p.pos.equals(pos);
			}
			if (obj instanceof PortalHorizontal) {
				PortalHorizontal p = (PortalHorizontal) obj;
				return p.pos.equals(pos);
			}
			return false;
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
			allConnections.add(this);
		} else {
			search.add(this);
		}
		allConnections.add(this);

		while (search.size() > 0) {

			for (Connection c : search.toArray(new Connection[0])) {
				
				System.out.println(c);

				/**
				 * contains the neighbouring tiles to the block
				 */
				ArrayList<SimpleEntry<Direction, LogicTile>> list = new ArrayList<>();

				// populates the list with neighbouring tiles
				// note for portals, only the tiles in the given direction are added

				c.updateConnections();

				for (Direction dir : Direction.values()) {
					if (c.hasConnection(dir)) {
						LogicTile logic = (LogicTile) TerrainManager.getTileRelative(c, dir);
						if (!allConnections.contains(logic)) {
							SimpleEntry<Direction, LogicTile> entry = new SimpleEntry<>(dir, logic);
							list.add(entry);
						}

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
						
						if (up instanceof PortalIntoTheThirdDimension) {
							up = new PortalVertical(up.getTileId());
						}
						if (down instanceof PortalIntoTheThirdDimension) {
							down = new PortalVertical(down.getTileId());
						}
						if (left instanceof PortalIntoTheThirdDimension) {
							left = new PortalHorizontal(left.getTileId());
						}
						if (right instanceof PortalIntoTheThirdDimension) {
							right = new PortalHorizontal(right.getTileId());
						}

						PortalHorizontal ph = new PortalHorizontal(id_t);
						PortalVertical pv = new PortalVertical(id_t);
						boolean added=false;
						if ((left instanceof LogicTile && allConnections.contains(left))
								|| (right instanceof LogicTile && allConnections.contains(right))) {
							search.add(ph);
							allConnections.add(ph);
							added=true;
						}
						if ((up instanceof LogicTile && allConnections.contains(up))
								|| (down instanceof LogicTile && allConnections.contains(down))) {
							search.add(pv);
							allConnections.add(pv);
							added=true;
						}
						if (added) {
							allConnections.add((Connection)t);
						}
					} else if (t instanceof Connection) {
						search.add((Connection) t);
						allConnections.add((Connection) t);

					} else if (t instanceof Emitter) {
						Direction emitterDirection = dir.opposite();
						emitterDirection = emitterDirection.rotateAntiClockwise(t.getRotation());
						if (((Emitter) t).outputSignal(emitterDirection)) {
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

		updateConnections();

	}

}
