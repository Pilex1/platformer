package main;

import static main.MainApplet.*;

import java.io.*;
import java.util.*;

import core.Canvas;
import entities.*;
import processing.core.*;
//import processing.sound.*;
import terrain.*;

public class Game extends Canvas {

	public static float Floor = 10000;

	private Chunk[] chunks;
	private GuidePlatform guide = new GuidePlatform(new PVector());
	private GuideRemovalPlatform guideRemoval = new GuideRemovalPlatform(new PVector());

	private String file = "terrain.plex";

	private float currentFloor;
	private float x;
	private float y;

	private Player player;

	// private SoundFile satie;

	public Game() {

		super(P.width, P.height);

		// satie = new SoundFile(Processing, "Satie.mp3");
		// satie.loop();

		chunks = new Chunk[2000];
		for (int i = 0; i < chunks.length; i++) {
			chunks[i] = new Chunk(i);
		}

		player = new Player();
		addEntity(new Guide());
//		addEntity(new Guide2());
//		addEntity(new Guide3());
//		addEntity(new Guide4());
//		addEntity(new Guide5());
//		addEntity(new Guide6());
//		addEntity(new Mystery());

		loadPlatforms();

		/*
		 * float sx = 1100;
		 * 
		 * currentFloor = Floor; addPlatform(new Platform(new PVector(0, Floor), new
		 * PVector(sx, 100)));
		 * 
		 * x += sx; y = Manager.Floor;
		 * 
		 * gen1(); addPlatform(new Platform(new PVector(x, y), new PVector(sx, 100))); x
		 * += sx;
		 * 
		 * gen2(); addPlatform(new Platform(new PVector(x, y), new PVector(sx, 100))); x
		 * += sx;
		 */

	}

	@Override
	protected void onRender(PVector pos, PVector size) {
		beginDraw();
		background(120,150,210);
		for (Platform p : getActivePlatforms()) {
			p.onRender();
		}
		if (player.isEditing())
			guide.onRender();
		if (player.isRemoving())
			guideRemoval.onRender();
		for (Entity e : getActiveEntities()) {
			e.onRender();
		}
		renderBuffer(pos, size);
		endDraw();
	}

	@Override
	protected void onUpdate(PVector pos, PVector size) {
		for (Platform p : getActivePlatforms()) {
			p.onUpdate();
		}
		for (Entity e : getActiveEntities()) {
			e.update();
		}
		requestGraphicalUpdate();
	}

	public void resetBlocks() {
		for (Chunk c : chunks) {
			c.resetBlocks();
		}
	}

	private void loadPlatforms() {
		try {
			BufferedReader reader = P.createReader(file);
			String line = null;
			while ((line = reader.readLine()) != null) {
				addPlatform(Platform.loadString(line));
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void savePlatforms() {
		PrintWriter writer = P.createWriter(file);
		for (Platform p : getAllPlatforms()) {
			writer.println(p.saveString());
		}
		writer.close();
	}

	public Npc[] getAllNpcs() {
		HashSet<Npc> npcs = new HashSet<>();
		for (Chunk chunk : chunks) {
			for (Entity e : chunk.getEntities()) {
				if (e instanceof Npc) {
					npcs.add((Npc) e);
				}
			}
		}
		return npcs.toArray(new Npc[0]);
	}

	// not including the player
	public Entity[] getAllEntities() {
		HashSet<Entity> entities = new HashSet<>();
		for (Chunk chunk : chunks) {
			entities.addAll(chunk.getEntities());
		}
		return entities.toArray(new Entity[0]);
	}

	public void removePlatform(Platform p) {
		for (Chunk c : chunks) {
			c.removePlatform(p);
		}
	}

	public Platform[] getAllPlatforms() {
		HashSet<Platform> platforms = new HashSet<>();
		for (Chunk chunk : chunks) {
			platforms.addAll(chunk.getPlatforms());
		}
		return platforms.toArray(new Platform[0]);
	}

	public Player getPlayer() {
		return player;
	}

	// entity must not move
	public void addEntity(Entity e) {
		float left = e.getHitbox().getX1();
		float right = e.getHitbox().getX2();
		int idLeft = (int) (left / Chunk.Width);
		int idRight = (int) (right / Chunk.Width);
		for (int i = idLeft; i <= idRight; i++) {
			chunks[i].addEntity(e);
		}
	}

	public PVector getGuidePos() {
		return guide.getHitbox().getPos();
	}

	public PVector getGuideSize() {
		return guide.getHitbox().getSize();
	}

	public PVector getGuideRemovalPos() {
		return guideRemoval.getHitbox().getPos();
	}

	public PVector getGuideRemovalSize() {
		return guideRemoval.getHitbox().getSize();
	}

	public void setGuidePos(PVector pos) {
		guide.getHitbox().setPos(pos);
	}

	public void setGuideSize(PVector size) {
		guide.getHitbox().setSize(size);
	}

	public void setGuideRemovalPos(PVector pos) {
		guideRemoval.getHitbox().setPos(pos);
	}

	public void setGuideRemovalSize(PVector size) {
		guideRemoval.getHitbox().setSize(size);
	}

	public void addPlatform(Platform p) {
		float left = p.getLeftBoundary();
		float right = p.getRightBoundary();
		int idLeft = (int) (left / Chunk.Width);
		int idRight = (int) (right / Chunk.Width);
		for (int i = idLeft; i <= idRight; i++) {
			chunks[i].addPlatform(p);
		}
	}

	public Npc getClosestNpc(Entity entity) {
		float minDist = Float.MAX_VALUE;
		Npc closestNpc = null;
		for (Chunk chunk : getActiveChunks()) {
			for (Entity e : chunk.getEntities()) {
				if (e == entity)
					continue;
				if (!(e instanceof Npc))
					continue;
				float dist = entity.getDistanceTo(e);
				if (dist < minDist) {
					minDist = dist;
					closestNpc = (Npc) e;
				}
			}
		}
		return closestNpc;
	}

	public Entity[] getActiveEntities() {
		ArrayList<Chunk> chunks = getActiveChunks();
		HashSet<Entity> entities = new HashSet<>();
		for (Chunk c : chunks) {
			entities.addAll(c.getEntities());
		}
		entities.add(player);
		return entities.toArray(new Entity[0]);
	}

	public Platform[] getActivePlatforms() {
		ArrayList<Chunk> chunks = getActiveChunks();
		HashSet<Platform> platforms = new HashSet<>();
		for (Chunk c : chunks) {
			platforms.addAll(c.getPlatforms());
		}
		return platforms.toArray(new Platform[0]);
	}

	public int getChunk() {
		return (int) (player.getHitbox().getCenterX() / Chunk.Width) + 1;
	}

	public int getChunkRadius() {
		return (int) (P.width / 2 / Chunk.Width) + 2;
	}

	public ArrayList<Chunk> getActiveChunks() {
		int chunkCur = getChunk();
		int chunkRadius = getChunkRadius();
		int chunkMin = chunkCur - chunkRadius;
		chunkMin = Math.max(0, chunkMin);
		int chunkMax = chunkCur + chunkRadius;
		chunkMax = Math.min(chunks.length - 1, chunkMax);
		ArrayList<Chunk> chunks = new ArrayList<>();
		for (int i = chunkMin; i <= chunkMax; i++) {
			chunks.add(this.chunks[i]);
		}
		return chunks;
	}

	public ArrayList<Platform> getCollidingNotSolidPlatforms(Entity e) {
		ArrayList<Platform> colliding = new ArrayList<>();
		for (Chunk chunk : getActiveChunks()) {
			for (Platform platform : chunk.getPlatforms()) {
				if (platform.isIntersecting(e) && !platform.isSolid()) {
					colliding.add(platform);
				}
			}
		}
		return colliding;
	}

	public ArrayList<Platform> getCollidingPlatforms(Entity e) {
		ArrayList<Platform> colliding = new ArrayList<>();
		for (Chunk chunk : getActiveChunks()) {
			for (Platform platform : chunk.getPlatforms()) {
				if (platform.isIntersecting(e) && platform.isSolid()) {
					colliding.add(platform);
				}
			}
		}
		return colliding;
	}

	public Checkpoint getActiveCheckpoint() {
		for (Checkpoint c : getAllCheckpoints()) {
			if (c.isChecked())
				return c;
		}
		return null;
	}

	public ArrayList<Checkpoint> getAllCheckpoints() {
		ArrayList<Checkpoint> checkpoints = new ArrayList<>();
		for (Platform p : getAllPlatforms()) {
			if (p instanceof Checkpoint)
				checkpoints.add((Checkpoint) p);
		}
		return checkpoints;
	}

	public Checkpoint getNextCheckpoint(Entity e) {
		Checkpoint next = null;
		float ex = e.getHitbox().getX1();
		for (Checkpoint c : getAllCheckpoints()) {
			float x = c.getHitbox().getX1();
			if (x <= ex)
				continue;
			if (next == null) {
				next = c;
			} else {
				float cx = next.getHitbox().getX1();
				if (x < cx) {
					next = c;
				}
			}
		}
		return next;
	}

	public Checkpoint getPreviousCheckpoint(Entity e) {
		Checkpoint next = null;
		float ex = e.getHitbox().getX1();
		for (Checkpoint c : getAllCheckpoints()) {
			float x = c.getHitbox().getX1();
			if (x >= ex)
				continue;
			if (next == null) {
				next = c;
			} else {
				float cx = next.getHitbox().getX1();
				if (x > cx) {
					next = c;
				}
			}
		}
		return next;
	}

	@Override
	protected void onResize(float newWidth, float newHeight) {
		// System.err.println("Don't resize the canvas!");
	}

}
