package entities;

import main.TerrainManager;
import processing.core.*;
import util.*;

public class Guide extends Npc {

	// 400 pixels to the left of the checkpoint

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Guide() {
		super(new PVector(TerrainManager.CENTER_X + 5 * TerrainManager.TILE_SIZE, TerrainManager.CENTER_Y), "Guide");
		color = Color.LightPurple;
		conversations.add("ABC");
		conversations.add("123");
	}

}
