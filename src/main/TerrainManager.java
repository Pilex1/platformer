package main;

import static main.MainApplet.P;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import entities.Entity;
import processing.core.PVector;
import terrain.Checkpoint;
import terrain.Chunk;
import terrain.GuidePlatform;
import terrain.GuideRemovalPlatform;
import terrain.Platform;
import util.Rectangle;

public class TerrainManager {

	public static float Floor = 10000;

	private static Chunk[] chunks;

	private static String filePath = "terrain.plex";

	public static void loadPlatforms() {
		chunks = new Chunk[2000];
		for (int i = 0; i < chunks.length; i++) {
			chunks[i] = new Chunk(i);
		}

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
			Object o = in.readObject();
			while (o != null) {
				Platform p = (Platform) o;
				p.onLoad();
				addPlatform(p);
				o = in.readObject();
			}
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
		}
	}

	public static void savePlatforms() {
		try {
			File f = new File(filePath);
			f.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			for (Platform p : getAllPlatforms()) {
				out.writeObject(p);
			}
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	public static void update() {
		for (Platform p : getActivePlatforms()) {
			p.onUpdate();
		}
	}

	public static void render() {
		for (Platform p : getActivePlatforms()) {
			p.onRender();
		}
	}

	public static void addPlatform(Platform p) {
		float left = p.getLeftBoundary();
		float right = p.getRightBoundary();
		int idLeft = (int) (left / Chunk.Width);
		int idRight = (int) (right / Chunk.Width);
		for (int i = idLeft; i <= idRight; i++) {
			chunks[i].addPlatform(p);
		}
	}

	public static void removePlatform(Platform p) {
		for (Chunk c : chunks) {
			c.removePlatform(p);
		}
	}

	public static Platform[] getAllPlatforms() {
		HashSet<Platform> platforms = new HashSet<>();
		for (Chunk chunk : chunks) {
			platforms.addAll(chunk.getPlatforms());
		}
		return platforms.toArray(new Platform[0]);
	}

	public static Platform[] getActivePlatforms() {
		ArrayList<Chunk> chunks = getActiveChunks();
		HashSet<Platform> platforms = new HashSet<>();
		for (Chunk c : chunks) {
			platforms.addAll(c.getPlatforms());
		}
		return platforms.toArray(new Platform[0]);
	}

	public static Chunk[] getAllChunks() {
		return chunks;
	}

	public static Chunk getChunk(int id) {
		return chunks[id];
	}

	public static int getPlayerChunk() {
		return (int) (EntityManager.getPlayer().getHitbox().getCenterX() / Chunk.Width) + 1;
	}

	public static int getChunkRadius() {
		return (int) (P.width / 2 / Chunk.Width) + 2;
	}

	public static ArrayList<Chunk> getActiveChunks() {
		int chunkCur = getPlayerChunk();
		int chunkRadius = getChunkRadius();
		int chunkMin = chunkCur - chunkRadius;
		chunkMin = Math.max(0, chunkMin);
		int chunkMax = chunkCur + chunkRadius;
		chunkMax = Math.min(chunks.length - 1, chunkMax);
		ArrayList<Chunk> chunks = new ArrayList<>();
		for (int i = chunkMin; i <= chunkMax; i++) {
			chunks.add(TerrainManager.chunks[i]);
		}
		return chunks;
	}

	// solid true - finds collision with solid blocks
	// solid false - finds collision with non-solid blocks
	public static ArrayList<Platform> getCollisions(Rectangle r, boolean solid) {
		ArrayList<Platform> colliding = new ArrayList<>();
		for (Chunk chunk : getActiveChunks()) {
			for (Platform platform : chunk.getPlatforms()) {
				if (platform.getHitbox().isIntersecting(r) && platform.isSolid()) {
					if (solid && platform.isSolid()) {
						colliding.add(platform);
					} else if (!solid && !platform.isSolid()) {
						colliding.add(platform);
					}
				}
			}
		}
		return colliding;
	}

	public static ArrayList<Platform> getCollisions(Entity e, boolean solid) {
		return getCollisions(e.getHitbox(), solid);
	}

	public static void resetBlocks() {
		for (Chunk c : chunks) {
			c.resetBlocks();
		}
	}

	public static Checkpoint getActiveCheckpoint() {
		for (Checkpoint c : getAllCheckpoints()) {
			if (c.isChecked())
				return c;
		}
		return null;
	}

	public static ArrayList<Checkpoint> getAllCheckpoints() {
		ArrayList<Checkpoint> checkpoints = new ArrayList<>();
		for (Platform p : getAllPlatforms()) {
			if (p instanceof Checkpoint)
				checkpoints.add((Checkpoint) p);
		}
		return checkpoints;
	}
}
