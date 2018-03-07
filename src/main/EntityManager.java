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
import entities.Npc;
import entities.Player;
import entities.Sandbox;
import terrain.Chunk;
import terrain.Platform;

public class EntityManager {
	
	private static Player player;

	private static String filePath = "entities.plex";
	
	public static void update() {
		for (Entity e : getActiveEntities(true)) {
			e.update();
		}
	}
	
	public static void render() {
		for (Entity e : getActiveEntities(true)) {
			e.onRender();
		}
	}
	
	public static void loadEntities() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath));
			Object o = in.readObject();
			while (o != null) {
				Entity e = (Entity) o;
				e.onLoad();
				addEntity(e);
				o = in.readObject();
			}
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println(e);
		}
		
		player = new Player();
		//Guide guide = new Guide();
		//addEntity(guide);
		
		if (player.inAir()) {
			TerrainManager.addPlatform(new Platform(player.getHitbox().bottomLeft()));
		}
		//if (guide.inAir()) {
		//	TerrainManager.addPlatform(new Platform(guide.getHitbox().bottomLeft()));
		//}
	}
	
	public static void saveEntities() {
		try {
			File f = new File(filePath);
			f.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
			for (Entity e : getAllEntities(false)) {
				out.writeObject(e);
			}
			out.close();
		} catch (IOException e) {
			System.err.println(e);
		}
	}

	// entity must not move
	public static void addEntity(Entity e) {
		float left = e.getHitbox().getX1();
		float right = e.getHitbox().getX2();
		int idLeft = (int) (left / Chunk.Width);
		int idRight = (int) (right / Chunk.Width);
		for (int i = idLeft; i <= idRight; i++) {
			TerrainManager.getChunk(i).addEntity(e);
		}
	}
	
	public static Npc getClosestNpc(Entity entity) {
		float minDist = Float.MAX_VALUE;
		Npc closestNpc = null;
		for (Chunk chunk : TerrainManager.getActiveChunks()) {
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

	public static Entity[] getActiveEntities(boolean includePlayer) {
		ArrayList<Chunk> chunks = TerrainManager.getActiveChunks();
		HashSet<Entity> entities = new HashSet<>();
		for (Chunk c : chunks) {
			entities.addAll(c.getEntities());
		}
		if (includePlayer) {
			entities.add(player);
		}
		return entities.toArray(new Entity[0]);
	}
	
	public static Entity[] getAllEntities(boolean includePlayer) {
		HashSet<Entity> entities = new HashSet<>();
		for (Chunk chunk : TerrainManager.getAllChunks()) {
			entities.addAll(chunk.getEntities());
		}
		if (includePlayer) {
			entities.add(player);
		}
		return entities.toArray(new Entity[0]);
	}
	
	public static Npc[] getAllNpcs() {
		HashSet<Npc> npcs = new HashSet<>();
		for (Chunk chunk : TerrainManager.getAllChunks()) {
			for (Entity e : chunk.getEntities()) {
				if (e instanceof Npc) {
					npcs.add((Npc) e);
				}
			}
		}
		return npcs.toArray(new Npc[0]);
	}


	public static Player getPlayer() {
		return player;
	}

}
