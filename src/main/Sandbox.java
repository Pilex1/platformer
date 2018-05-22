package main;

import processing.core.PConstants;
import processing.core.PVector;
import terrain.Checkpoint;
import terrain.Fan;
import terrain.Ice;
import terrain.Interface;
import terrain.InterfacePlatform;
import terrain.HBounce;
import terrain.Invisible;
import terrain.LevelFlag;
import terrain.Phantom;
import terrain.Platform;
import terrain.Shooter;
import terrain.Tile;
import terrain.VBounce;
import util.Color;
import util.Rectangle;
import util.StringUtil;

import static main.MainApplet.P;

import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import core.Fonts;
import entities.Entity;
import entities.Guide;
import logic.AndGate;
import logic.Diode;
import logic.Inverter;
import logic.PermanentOutput;
import logic.PortalIntoTheThirdDimension;
import logic.Sensor;
import logic.Wire;

public class Sandbox {

	private static enum Action {
		None, Tile, Size, Remove, Guide
	}

	private static Class<?>[] platformTypes = new Class<?>[] { Platform.class, VBounce.class, HBounce.class, Ice.class,
			Invisible.class, Phantom.class, Shooter.class, Checkpoint.class, LevelFlag.class, Wire.class,
			PortalIntoTheThirdDimension.class, AndGate.class, Inverter.class, Diode.class, Sensor.class, PermanentOutput.class,
			Interface.class, InterfacePlatform.class, Fan.class, };
	private static int currentPlatform = 0;

	private static PVector pos1 = null;

	private static GuidePlatform tile1 = new GuidePlatform(new PVector());
	private static GuideRemovalPlatform removal = new GuideRemovalPlatform(new PVector());
	private static GuideGuide guide = new GuideGuide(new PVector());

	private static Action currentAction = Action.None;

	private static int frameRate = -1;

	public static void update() {
		if (!P.game.debug)
			return;
		// System.out.println(currentAction);
		PVector mousePos = getMousePos();
		if (currentAction == Action.Tile) {
			pos1 = blockify(mousePos, TerrainManager.TILE_SIZE);
			tile1.getHitbox().setPos(pos1);
			tile1.getHitbox().setSize(new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
		} else if (currentAction == Action.Size) {
			PVector offset = PVector.sub(mousePos, pos1);
			if (offset.x <= 0) {
				// mouse to left of guide
				tile1.getHitbox().setX1(pos1.x + TerrainManager.TILE_SIZE);
			} else {
				tile1.getHitbox().setX1(pos1.x);
			}
			if (offset.y <= 0) {
				// mouse above guide
				tile1.getHitbox().setY1(pos1.y + TerrainManager.TILE_SIZE);
			} else {
				tile1.getHitbox().setY1(pos1.y);
			}

			PVector newOffset = PVector.sub(mousePos, tile1.getHitbox().topLeft());
			tile1.getHitbox().setSize(blockifyCeil(newOffset, TerrainManager.TILE_SIZE));
		} else if (currentAction == Action.Remove) {
			Tile selectedTile = getSelectedTile();
			Entity selectedEntity = getSelectedEntity();
			if (selectedEntity == EntityManager.getPlayer()) {
				selectedEntity = null;
			}
			if (selectedTile != null) {
				removal.getHitbox().setPos(selectedTile.getHitbox().topLeft());
				removal.getHitbox().setSize(selectedTile.getHitbox().getSize());
				if (P.mousePressed && P.mouseButton == PConstants.LEFT) {
					TerrainManager.removeTile(selectedTile);
				}
			} else if (selectedEntity != null) {
				removal.getHitbox().setPos(selectedEntity.getHitbox().topLeft());
				removal.getHitbox().setSize(selectedEntity.getHitbox().getSize());
				if (P.mousePressed && P.mouseButton == PConstants.LEFT) {
					EntityManager.removeEntity(selectedEntity);
				}
			} else {
				removal.getHitbox().setSize(new PVector(0, 0));
			}
		} else if (currentAction == Action.Guide) {
			Tile selectedTile = getSelectedTile();
			if (selectedTile == null) {
				pos1 = blockify(mousePos, TerrainManager.TILE_SIZE);
				pos1.y += TerrainManager.TILE_SIZE - 40;
				guide.getHitbox().setPos(pos1);
			}
		}
	}

	public static void render() {
		if (!P.game.debug)
			return;

		P.game.fill(Color.White);
		P.game.textFont(Fonts.LatoLight, 32);
		P.game.textAlign(PConstants.LEFT, PConstants.TOP);
		String debug = "";
		if (P.frameCount % 120 == 0 || frameRate == -1) {
			frameRate = (int) P.frameRate;
		}
		debug += "FPS: " + frameRate + "\n";
		debug += "Pos: " + StringUtil.beautify(EntityManager.getPlayer().getHitbox().getCenter()) + "\n";
		debug += "Vel: " + StringUtil.beautify(EntityManager.getPlayer().getVel()) + "\n";
		debug += "Acc: " + StringUtil.beautify(EntityManager.getPlayer().getAccel()) + "\n";
		debug += "Mouse: " + StringUtil.beautify(getMousePos()) + "\n";
		debug += "Action: " + currentAction + "\n";
		if (currentAction == Action.Tile || currentAction == Action.Size) {
			debug += "Tile: " + StringUtil.beautify(tile1.getHitbox().topLeft()) + "\n";
			debug += "Tile Size: " + (StringUtil.beautify(tile1.getHitbox().getSize())) + "\n";
		} else if (currentAction == Action.Remove) {
			debug += "Remove: " + (!removal.getHitbox().getSize().equals(new PVector(0, 0))
					? StringUtil.beautify(removal.getHitbox().topLeft())
					: "") + "\n";

		}
		P.game.text(debug, 0, 0);

		if (currentAction == Action.Tile) {
			P.game.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			P.game.text(platformTypes[currentPlatform].getCanonicalName(), P.width / 2, P.height);
		}

		if (currentAction == Action.Tile || currentAction == Action.Size) {
			tile1.onRender();
		}

		if (currentAction == Action.Remove) {
			removal.onRender();
		}
		if (currentAction == Action.Guide) {
			guide.onRender();
		}

	}

	public static Tile getSelectedTile() {
		ArrayList<Tile> tiles = TerrainManager.getActiveTiles();
		Tile selected = null;
		for (Tile t : tiles) {
			PVector mousePos = getMousePos();
			if (t.getHitbox().inside(mousePos)) {
				selected = t;
				break;
			}
		}
		return selected;
	}

	public static Entity getSelectedEntity() {
		HashSet<Entity> entities = EntityManager.getAllEntities();
		Entity selected = null;
		for (Entity t : entities) {
			PVector mousePos = getMousePos();
			if (t.getHitbox().inside(mousePos)) {
				selected = t;
				break;
			}
		}
		return selected;
	}

	private static PVector getMousePos() {
		float mx = P.mouseX + EntityManager.getPlayer().getHitbox().getCenterX() - P.width / 2;
		float my = P.mouseY + EntityManager.getPlayer().getHitbox().getCenterY() - P.height / 2;
		return new PVector(mx, my);
	}

	private static PVector blockify(PVector v, int x) {
		return new PVector((int) (v.x / x) * x, (int) (v.y / x) * x);
	}

	private static PVector blockifyCeil(PVector v, float s) {
		float x = 0;
		if (v.x < 0) {
			x = (float) Math.min(-s, Math.floor(v.x / s) * s);
		} else {
			x = (float) Math.max(s, Math.ceil(v.x / s) * s);
		}
		float y = 0;
		if (v.y < 0) {
			y = (float) Math.min(-s, Math.floor(v.y / s) * s);
		} else {
			y = (float) Math.max(s, Math.ceil(v.y / s) * s);
		}
		return new PVector(x, y);
	}

	// java reflection!!!
	private static Tile createTile(Class<?> c, PVector pos) {
		try {
			return (Tile) c.getConstructor(PVector.class).newInstance(pos);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void onMousePress(int mouseButton) {
		if (currentAction == Action.Tile && pos1 != null) {
			if (mouseButton == PConstants.LEFT) {
				if (TerrainManager.getTileAt(pos1) == null) {
					// if we left click an empty tile, start a drag operation
					// staring from the selected tile
					// unless we're placing down a Checkpoint,
					// in which case dragging is disabled and we just place the checkpoint

					if (platformTypes[currentPlatform] == Checkpoint.class) {
						PVector pos = pos1.copy();
						TerrainManager.addTile(createTile(Checkpoint.class, pos));
						pos1 = null;
					} else {
						currentAction = Action.Size;
					}
				} else {
					// left clicking a tile that has something there already
					// rotate the tile
					TerrainManager.getTileAt(pos1).rotate();
				}

			} else if (mouseButton == PConstants.RIGHT) {
				// if we right click on a tile, then we start a drag operation for removing
				// tiles
				// not implemented for now (maybe do it sometime later)
			}

		} else if (currentAction == Action.Guide) {
			PVector guidePos = pos1.copy();
			// bottom left of the tile
			guidePos.y += TerrainManager.TILE_SIZE;
			// calculate top left pos of guide
			guidePos.y -= 40;

			System.out.println("Enter guide conversation (newline to end):");

			ArrayList<String> conversations = new ArrayList<>();
			String curLine = P.scanner.nextLine();
			while (!curLine.equals("")) {
				conversations.add(curLine);
				curLine = P.scanner.nextLine();
			}

			Guide g = new Guide(guidePos, conversations);
			EntityManager.addEntity(g);

			System.out.println("Guide added");
		}
	}

	public static void onMouseRelease(int mouseButton) {
		if (currentAction == Action.Size && pos1 != null) {
			if (platformTypes[currentPlatform] != Checkpoint.class) {
				Rectangle rect = new Rectangle(tile1.getHitbox().topLeft(), tile1.getHitbox().getSize()).regularise();
				for (int i = 0; i < rect.getWidth() / TerrainManager.TILE_SIZE; i++) {
					for (int j = 0; j < rect.getHeight() / TerrainManager.TILE_SIZE; j++) {
						PVector pos = rect.topLeft().copy();
						pos.x += i * TerrainManager.TILE_SIZE;
						pos.y += j * TerrainManager.TILE_SIZE;
						Tile t = createTile(platformTypes[currentPlatform], pos);
						TerrainManager.addTile(t);
					}
				}
				pos1 = null;
				currentAction = Action.Tile;
			}
		}
	}

	public static void onKeyPress(char key) {
		if (!P.game.debug)
			return;
		if (key == KeyEvent.VK_ESCAPE) {
			currentAction = Action.None;
			P.game.allowPausing(true);
		}
		if (key == 'f') {
			EntityManager.getPlayer().toggleFlying();
		}
		if (key == 'e') {
			currentAction = Action.Tile;
			P.game.allowPausing(false);
		}
		if (key == 'r') {
			currentAction = Action.Remove;
			P.game.allowPausing(false);
		}
		if (key == 'g') {
			currentAction = Action.Guide;
			P.game.allowPausing(false);
		}
	}

	public static void onKeyRelease(char key) {

	}

	public static void onScroll(int scrollAmt) {
		if (scrollAmt < 0) {
			currentPlatform = (currentPlatform - 1 + platformTypes.length) % platformTypes.length;
		} else if (scrollAmt > 0) {
			currentPlatform = (currentPlatform + 1) % platformTypes.length;
		}
	}

	private static class GuidePlatform extends Tile {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private GuidePlatform(PVector pos) {
			super(pos);
		}

		@Override
		public void onRender() {
			P.game.strokeWeight(0);
			P.game.fill(Color.LightGrey.Transparent());
			P.game.rect(hitbox, P.getCamera());
		}

		@Override
		public void onUpdate() {
		}
	}

	private static class GuideRemovalPlatform extends Tile {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private GuideRemovalPlatform(PVector pos) {
			super(pos);
		}

		@Override
		public void onRender() {
			P.game.strokeWeight(1);
			P.game.stroke(Color.Red);
			P.game.fill(Color.Transparent);
			P.game.rect(hitbox, P.getCamera());
		}

		@Override
		public void onUpdate() {
		}
	}

	private static class GuideGuide extends Tile {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private GuideGuide(PVector pos) {
			super(pos);
			hitbox.setSize(new PVector(20, 40));
		}

		@Override
		public void onRender() {
			P.game.strokeWeight(1);
			P.game.stroke(Color.LightGreen);
			P.game.fill(Color.Transparent);
			P.game.rect(hitbox, P.getCamera());
		}

		@Override
		public void onUpdate() {
		}
	}

}
