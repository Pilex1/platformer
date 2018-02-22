package terrain;

import java.util.*;

import entities.*;

public class Chunk {

	public static float Width = 400;

	private HashSet<Platform> platforms = new HashSet<>();
	private HashSet<Entity> entities = new HashSet<>();
	private int id;

	public Chunk(int id) {
		this.id = id;
	}

	public void resetBlocks() {
		for (Platform p : platforms) {
			if (p instanceof InvisiblePlatform) {
				InvisiblePlatform i = (InvisiblePlatform) p;
				i.reset();
			} else if (p instanceof PhantomPlatform) {
				PhantomPlatform ph = (PhantomPlatform) p;
				ph.reset();
			}
		}
	}

	public HashSet<Entity> getEntities() {
		return entities;
	}

	public HashSet<Platform> getPlatforms() {
		return platforms;
	}

	public void removeEntity(Entity e) {
		entities.remove(e);
	}

	public void removePlatform(Platform p) {
		platforms.remove(p);
	}

	public void addEntity(Entity e) {
		entities.add(e);
	}

	public void addPlatform(Platform p) {
		platforms.add(p);
	}

	public float getX1() {
		return id * Width;
	}

	public float getX2() {
		return (id + 1) * Width;
	}

}
