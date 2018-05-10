package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import entities.Entity;
import logic.Direction;
import processing.core.PVector;
import terrain.Checkpoint;
import terrain.Tile;
import util.Rectangle;
import util.Vector2i;

public class TerrainManager {

	/**
	 *  size in pixels that the tiles will be rendered onto the screen
	 */
	public static final int TILE_SIZE = 50;

	/**
	 * number of tiles in the x direction in a level
	 */
	public static final int TILES_X = 256;
	/**
	 * number of tiles in the y direction in a level
	 */
	public static final int TILES_Y = 256;

	/**
	 * x coordinate of the very center of the level
	 */
	public static final float CENTER_X = TILES_X / 2 * TILE_SIZE;
	/**
	 * y coordinate of the very center of the level
	 */
	public static final float CENTER_Y = TILES_Y / 2 * TILE_SIZE;

	public static final int UPDATE_RADIUS = Applet.WIDTH / 2 / TILE_SIZE + 16;

	private static Tile[][] tiles = new Tile[TILES_X][TILES_Y];

	private static HashSet<Tile> allTiles = new HashSet<>();
	private static HashSet<Checkpoint> allCheckpoints = new HashSet<>();

	public static void reset() {
		tiles = new Tile[TILES_X][TILES_Y];
	}

	public static void addTile(Tile t) {
		addTile(t, false);
	}

	public static void addTile(Tile t, boolean overwrite) {
		int x = (int) t.getHitbox().getX1() / TILE_SIZE;
		int y = (int) t.getHitbox().getY1() / TILE_SIZE;
		if (tiles[x][y] == null) {
			tiles[x][y] = t;
			allTiles.add(t);
			t.onLoad();
		} else {
			if (overwrite) {
				allTiles.remove(tiles[x][y]);
				tiles[x][y] = t;
				allTiles.add(t);
				t.onLoad();
			}
		}
	}

	public static void removePlatform(Tile t) {
		int x = (int) t.getHitbox().getX1() / TILE_SIZE;
		int y = (int) t.getHitbox().getY1() / TILE_SIZE;
		if (tiles[x][y] == null)
			return;
		allTiles.remove(tiles[x][y]);
		tiles[x][y] = null;
		t.afterRemove();
	}

	public static void update() {
		for (Tile p : getActiveTiles()) {
			p.onUpdate();
		}
	}

	public static void render() {
		for (Tile p : getActiveTiles()) {
			p.onRender();
		}
	}

	public static Tile getTileRelative(Tile t, Direction d) {
		Vector2i v = t.getTileId();
		switch (d) {
		case UP:
			return getTileById(v.x, v.y - 1);
		case DOWN:
			return getTileById(v.x, v.y + 1);
		case LEFT:
			return getTileById(v.x - 1, v.y);
		case RIGHT:
			return getTileById(v.x + 1, v.y);
		}
		return null;
	}

	public static Tile getTileById(Vector2i v) {
		return getTileById(v.x, v.y);
	}

	public static Tile getTileById(int x, int y) {
		return tiles[x][y];
	}

	public static Tile getTileAt(float x, float y) {
		return tiles[(int) x / TILE_SIZE][(int) y / TILE_SIZE];
	}

	public static Tile getTileAt(PVector v) {
		return getTileAt(v.x, v.y);
	}

	public static HashSet<Tile> getAllTiles() {
		return allTiles;
	}

	public static ArrayList<Tile> getActiveTiles() {

		ArrayList<Tile> l = new ArrayList<>();

		int px = (int) EntityManager.getPlayer().getHitbox().getCenterX() / TILE_SIZE;
		int py = (int) EntityManager.getPlayer().getHitbox().getCenterY() / TILE_SIZE;

		int x1 = Math.max(0, px - UPDATE_RADIUS);
		int x2 = Math.min(tiles.length - 1, px + UPDATE_RADIUS);
		int y1 = Math.max(0, py - UPDATE_RADIUS);
		int y2 = Math.min(tiles[0].length - 1, py + UPDATE_RADIUS);

		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				if (tiles[i][j] != null) {
					l.add(tiles[i][j]);
				}
			}
		}
		return l;
	}

	public static Checkpoint getActiveCheckpoint() {
		for (Checkpoint c : allCheckpoints) {
			if (c.isChecked())
				return c;
		}
		return null;
	}

	public static HashSet<Checkpoint> getAllCheckpoints() {
		return allCheckpoints;
	}

	/**
	 * 
	 * finds all collisions between the given rectangle and the terrain
	 * 
	 * @param r
	 * @param solid
	 *            if true, finds all collisions with solid tiles if false, finds all
	 *            collisions with non-solid tiles
	 * @return
	 */
	public static ArrayList<Tile> getCollisions(Rectangle r, boolean solid) {
		ArrayList<Tile> colliding = new ArrayList<>();

		int x1 = (int) (r.getX1() / TILE_SIZE);
		int x2 = (int) (r.getX2() / TILE_SIZE + 0.5);
		int y1 = (int) (r.getY1() / TILE_SIZE);
		int y2 = (int) (r.getY2() / TILE_SIZE + 0.5);

		x1 = Math.max(0, x1);
		x2 = Math.min(tiles.length - 1, x2);
		y1 = Math.max(0, y1);
		y2 = Math.min(tiles[0].length - 1, y2);

		for (int i = x1; i <= x2; i++) {
			for (int j = y1; j <= y2; j++) {
				Tile t = tiles[i][j];
				if (t == null)
					continue;
				if (t.getHitbox().isIntersecting(r)) {
					if (solid == t.isSolid()) {
						colliding.add(t);
					}
				}
			}
		}

		return colliding;
	}

	public static ArrayList<Tile> getCollisions(Entity e, boolean solid) {
		return getCollisions(e.getHitbox(), solid);
	}

	public static void resetBlocks() {
		for (Tile t : allTiles) {
			t.reset();
		}
	}

}
