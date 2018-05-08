package logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Color;
import util.Vector2i;

import static main.MainApplet.P;

public class Wire extends LogicTile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static final Color inactiveColor = Color.Red;
	protected static final Color activeColor = Color.Purple;
	protected static final float radius = 5;

	/**
	 * indicates whether the wire should be connected to the corresponding direction
	 * i.e. there is a wire or some other logic component
	 */
	protected boolean connectUp, connectRight, connectDown, connectLeft;

	/**
	 * stores if the wire is connected to a signal emitter this normally corresponds
	 * to the state being active however, a wire may be active and not be connected
	 * to an emitter e.g. if it is connected to an active AND gate
	 */
	protected boolean connectedToActiveEmitter;

	/**
	 * if the wire is connected to something like an AND gate that transmits a
	 * signal, but doesn't actually emit a signal
	 */
	protected boolean connectedToIntermediate;

	public Wire(PVector pos) {
		super(pos);
		solid = false;
	}

	@Override
	public void neighbouringUpdate() {
		updateNetwork();
		updateConnections();
	}

	private class PortalVertical extends Wire {

		private PortalVertical(Vector2i id) {
			super(new PVector(id.x * TerrainManager.TILE_SIZE, id.y * TerrainManager.TILE_SIZE));
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
		public boolean equals(Object obj) {
			if (!(obj instanceof PortalHorizontal))
				return false;
			PortalHorizontal p = (PortalHorizontal) obj;
			return p.pos.equals(pos);
		}

	}

	public void updateNetwork() {
		updateNetwork(true, true);
	}

	protected void updateNetwork(boolean horizontal, boolean vertical) {

		connectedToActiveEmitter = false;
		connectedToIntermediate = false;

		boolean state = false;
		/**
		 * stores all the wires that we have already traversed, so they are not
		 * traversed again
		 */
		HashSet<Wire> allConnections = new HashSet<>();

		/**
		 * the current wires being traversed
		 */
		HashSet<Wire> search = new HashSet<>();

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

			for (Wire w : search.toArray(new Wire[0])) {

				Vector2i id = w.getTileId();
				/**
				 * contains the neighbouring tiles to the block
				 */
				ArrayList<Tile> list = new ArrayList<>();

				// populates the list with neighbouring tiles
				// note for portals, only the tiles in the given direction are added
				
				if (w instanceof PortalHorizontal) {
					list.add(TerrainManager.getTileById(id.x - 1, id.y));
					list.add(TerrainManager.getTileById(id.x + 1, id.y));
				} else if (w instanceof PortalVertical) {
					list.add(TerrainManager.getTileById(id.x, id.y - 1));
					list.add(TerrainManager.getTileById(id.x, id.y + 1));
				} else {
					assert !(w instanceof PortalIntoTheThirdDimension);
					list.add(TerrainManager.getTileById(id.x, id.y - 1));
					list.add(TerrainManager.getTileById(id.x, id.y + 1));
					list.add(TerrainManager.getTileById(id.x - 1, id.y));
					list.add(TerrainManager.getTileById(id.x + 1, id.y));
				}

				for (Tile t : list) {
					if (!(t instanceof LogicTile))
						continue;

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
					} else if (((LogicTile) t).isIntermediate() && ((LogicTile) t).isActive()) {
						connectedToIntermediate = true;
						state = true;
					} else if (t instanceof Wire && !allConnections.contains(t)) {
						search.add((Wire) t);
						allConnections.add((Wire) t);
					} else if (t instanceof Emitter) {
						if (((Emitter) t).outputSignal()) {
							connectedToActiveEmitter = true;
							state = true;
						}
					}
				}

				search.remove(w);
			}

		}

		for (Wire w : allConnections) {
			if (w instanceof PortalHorizontal) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) TerrainManager.getTileById(w.getTileId());
				p.hActive = state;
			} else if (w instanceof PortalVertical) {
				PortalIntoTheThirdDimension p = (PortalIntoTheThirdDimension) TerrainManager.getTileById(w.getTileId());
				p.vActive = state;
			}
			w.active = state;
			w.connectedToIntermediate = connectedToIntermediate;
			w.connectedToActiveEmitter = connectedToActiveEmitter;

		}

	}

	public void updateConnections() {
		int x = (int) pos.x / TerrainManager.TILE_SIZE;
		int y = (int) pos.y / TerrainManager.TILE_SIZE;

		connectUp = TerrainManager.getTileById(x, y - 1) instanceof LogicTile;
		connectRight = TerrainManager.getTileById(x + 1, y) instanceof LogicTile;
		connectDown = TerrainManager.getTileById(x, y + 1) instanceof LogicTile;
		connectLeft = TerrainManager.getTileById(x - 1, y) instanceof LogicTile;
	}

	@Override
	public void reset() {
	}

	@Override
	public void onRender() {
		float cx = hitbox.getCenterX();
		float cy = hitbox.getCenterY();
		P.game.fill(active ? activeColor : inactiveColor);
		P.game.noStroke();
		P.game.rect(cx - radius, cy - radius, 2 * radius, 2 * radius, P.getCamera());
		float offset = TerrainManager.TILE_SIZE / 2 - radius;
		if (connectUp) {
			P.game.rect(cx - radius, cy - TerrainManager.TILE_SIZE / 2, 2 * radius, offset, P.getCamera());
		}
		if (connectRight) {
			P.game.rect(cx + radius, cy - radius, offset, 2 * radius, P.getCamera());
		}
		if (connectDown) {
			P.game.rect(cx - radius, cy + radius, 2 * radius, offset, P.getCamera());
		}
		if (connectLeft) {
			P.game.rect(cx - TerrainManager.TILE_SIZE / 2, cy - radius, offset, 2 * radius, P.getCamera());
		}
	}

	@Override
	public void afterRemove() {
		super.afterRemove();
		ArrayList<LogicTile> list = getNeighbouringLogicTiles();
		for (LogicTile logic : list) {

		}
	}
}
