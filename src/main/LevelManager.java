package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import entities.Entity;
import terrain.Tile;

public class LevelManager {

	// level files are stored in the format
	// level#.plex where # represents a number indicating the level id
	// e.g. level0.plex represents level 0
	public static final String fileName = "level";
	public static final String fileExt = ".plex";

	/**
	 * stores all the data in each level
	 */
	private static ArrayList<ArrayList<Object>> levels = new ArrayList<>();
	/**
	 * stores whether a given level is unlocked or not
	 */
	private static ArrayList<Boolean> unlocked = new ArrayList<>();
	
	private static int currentLevel = -1;
	
	/**
	 * loads all the level data from the files
	 */
	public static void loadAllLevels() {
		int i = 0;
		while (new File(fileName + i + fileExt).exists()) {
			ArrayList<Object> list = new ArrayList<>();
			try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName + i + fileExt))) {
				Object o = in.readObject();
				while (o != null) {
					list.add(o);
					o = in.readObject();
				}
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
			levels.add(list);
			if (i == 0 || MainApplet.debug) {
				unlocked.add(true);
			} else {
				unlocked.add(false);
			}
			i++;
		}
	}

	public static void saveCurrentLevel() {
		// if there is no active level we do nothing
		if (currentLevel == -1)
			return;
		// if we are not in debug mode we do nothing
		// the levels shouldn't require saving
		if (!MainApplet.debug)return;
		try {
			ArrayList<Object> list = levels.get(currentLevel);
			list.clear();

			File file = new File(fileName + currentLevel + fileExt);
			file.createNewFile();
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
			for (Tile t : TerrainManager.getAllTiles()) {
				out.writeObject(t);
				list.add(t);
			}
			for (Entity e : EntityManager.getAllEntities()) {
				if (e.isSerializable()) {
					out.writeObject(e);
					list.add(e);
				}
			}
			
			out.writeObject(null);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void unlockLevel(int level) {
		if (level < 0 || level>= unlocked.size()) return;
		unlocked.set(level, true);
	}
	
	public static boolean isUnlocked(int level) {
		return unlocked.get(level);
	}

	/**
	 * if the input level is locked then we do nothing
	 * otherwise we save the current level, then initialise the new level
	 * 
	 * @param level
	 */
	public static void setActiveLevel(int level) {
		if (!unlocked.get(level)) return;
		saveCurrentLevel();
		TerrainManager.reset();
		EntityManager.reset();
		EntityManager.initialisePlayer();
		currentLevel = level;

		ArrayList<Object> data = levels.get(level);
		for (Object obj : data) {
			if (obj instanceof Tile) {
				Tile t = (Tile) obj;
				TerrainManager.addTile(t);
				t.reset();
				t.onLoad();
			} else if (obj instanceof Entity) {
				Entity e = (Entity) obj;
				EntityManager.addEntity(e);
				e.onLoad();
			} else {
				throw new RuntimeException();
			}
		}
	}

	public static void createNewLevel() {
		saveCurrentLevel();
		TerrainManager.reset();
		EntityManager.reset();
		EntityManager.initialisePlayer();
		currentLevel++;

		levels.add(new ArrayList<>());
	}

	public static int getNumberOfLevels() {
		return levels.size();
	}

	public static int getCurrentLevel() {
		return currentLevel;
	}

}
