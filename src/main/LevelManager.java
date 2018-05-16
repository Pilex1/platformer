package main;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import entities.Entity;
import entities.Player;
import terrain.Tile;

public class LevelManager {

	// level files are stored in the format
	// level#.plex where # represents a number indicating the level id
	// e.g. level0.plex represents level 0
	public static final String fileName = "level";
	public static final String fileExt = ".plex";

	private static ArrayList<ArrayList<Object>> levels = new ArrayList<>();

	private static int currentLevel = -1;

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
			i++;
		}
	}

	public static void saveCurrentLevel() {
		if (currentLevel == -1)
			return;
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
				if (e instanceof Player)
					continue;
				out.writeObject(e);
				list.add(e);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * saves the current level, then sets the given active level
	 * 
	 * @param level
	 */
	public static void setActiveLevel(int level) {
		if (currentLevel == level)
			return;
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
			} else if (obj instanceof Entity) {
				Entity e = (Entity) obj;
				EntityManager.addEntity(e);
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