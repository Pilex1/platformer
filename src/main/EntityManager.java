package main;

import java.util.ArrayList;
import java.util.HashSet;

import entities.Entity;
import entities.Npc;
import entities.Player;
import processing.core.PVector;
import terrain.Platform;
import util.Rectangle;

public class EntityManager {

	private static Player player;

	private static HashSet<Entity> entities = new HashSet<>();
	
	public static void initialisePlayer() {
		player = new Player();
		if (player.inAir()) {
			PVector pos = player.getHitbox().bottomLeft();
			TerrainManager.addTile(new Platform(pos));
		}

		entities.add(player);
	}
	
	public static void reset() {
		entities.clear();
	}

	public static void update() {
		for (Entity e : entities.toArray(new Entity[0])) {
			if (e == player)
				continue;
			e.update();
		}
		player.update();
	}

	public static void render() {
		for (Entity e : entities) {
			if (e == player)
				continue;
			e.onRender();
		}
		// by rendering the player last, this ensures that the player will always remain visible
		// even if there are multiple entities overlapping the player
		player.onRender();
	}

	public static void addEntity(Entity e) {
		entities.add(e);
		e.onLoad();
	}
	
	public static void removeEntity(Entity e) {
		entities.remove(e);
	}

	public static Npc getClosestNpc(Entity entity) {
		float minDist = Float.MAX_VALUE;
		Npc closestNpc = null;
		for (Entity e : entities) {
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
		return closestNpc;
	}

	public static ArrayList<Npc> getAllNpcs() {
		ArrayList<Npc> npcs = new ArrayList<>();
		for (Entity e : entities) {
			if (e instanceof Npc) {
				npcs.add((Npc) e);
			}
		}
		return npcs;
	}

	public static Player getPlayer() {
		return player;
	}

	/**
	 * gets all the entities in the level<br>
	 * <b>NB</b> this includes the player
	 * @return
	 */
	public static HashSet<Entity> getAllEntities() {
		return entities;
	}
	
	public static ArrayList<Entity> getCollisions(Entity search) {
		ArrayList<Entity> list = new ArrayList<>();
		for (Entity e : entities) {
			if (e==search) continue;
			if (e.getHitbox().isIntersecting(search.getHitbox(), true)) {
				list.add(e);
			}
		}
		return list;
	}
	
	public static float distBetween(Entity a, Entity b) {
		return distBetween(a.getHitbox(),b.getHitbox());
	}
	
	public static float distBetween(Rectangle a, Rectangle b) {
		return a.topLeft().dist(b.topLeft());
	}

}
