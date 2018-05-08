package main;

import processing.core.PConstants;
import processing.core.PVector;
import terrain.Checkpoint;
import terrain.GuidePlatform;
import terrain.GuideRemovalPlatform;
import terrain.HBouncePlatform;
import terrain.InvisiblePlatform;
import terrain.MovingPlatform;
import terrain.PhantomPlatform;
import terrain.Platform;
import terrain.Tile;
import terrain.VBouncePlatform;
import util.Color;
import util.Rectangle;
import util.StringUtil;

import static main.MainApplet.P;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import core.Fonts;
import logic.AndGate;
import logic.PortalIntoTheThirdDimension;
import logic.Sensor;
import logic.Wire;

public class Sandbox {

	private static enum Action {
		None, Tile1, Size1, Tile2, Removing
	}

	private static Class<?>[] platformTypes = new Class<?>[] { Platform.class, VBouncePlatform.class,
			HBouncePlatform.class, InvisiblePlatform.class, PhantomPlatform.class, /*MovingPlatform.class,*/
			Checkpoint.class, Wire.class ,Sensor.class, AndGate.class, PortalIntoTheThirdDimension.class};
	private static int currentPlatform = 0;

	private static PVector pos1 = null;
	private static PVector size1 = null;
	private static PVector pos2 = null;

	private static GuidePlatform tile1 = new GuidePlatform(new PVector());
	private static GuidePlatform tile2 = new GuidePlatform(new PVector());
	private static GuideRemovalPlatform removal = new GuideRemovalPlatform(new PVector());

	public static boolean enabled = true;
	private static Action currentAction = Action.None;

	public static void update() {
		// System.out.println(currentAction);
		PVector mousePos = getMousePos();
		if (currentAction == Action.Tile1) {
			pos1 = blockify(mousePos, TerrainManager.TILE_SIZE);
			tile1.getHitbox().setPos(pos1);
			tile1.getHitbox().setSize(new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
		} else if (currentAction == Action.Size1) {
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
		} else if (currentAction == Action.Removing) {
			Tile selected = getSelectedTile();
			if (selected == null) {
				removal.getHitbox().setSize(new PVector(0, 0));
			} else {
				removal.getHitbox().setPos(selected.getHitbox().topLeft());
				removal.getHitbox().setSize(selected.getHitbox().getSize());
			}
			if (P.mousePressed && P.mouseButton == PConstants.LEFT && selected != null) {
				TerrainManager.removePlatform(selected);
			}
		} else if (currentAction == Action.Tile2) {
			pos2 = blockify(mousePos, TerrainManager.TILE_SIZE);
			tile2.getHitbox().setPos(pos2);
			tile2.getHitbox().setSize(new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
		}
	}

	public static void render() {
		if (!enabled)
			return;

		P.game.fill(Color.White);
		P.game.textFont(Fonts.LatoLight, 32);
		P.game.textAlign(PConstants.LEFT, PConstants.TOP);
		String debug = "";
		debug += "FPS: " + P.frameRate + "\n";
		debug += "Pos: " + StringUtil.beautify(EntityManager.getPlayer().getHitbox().getCenter()) + "\n";
		debug += "Vel: " + StringUtil.beautify(EntityManager.getPlayer().getVel()) + "\n";
		debug += "Mouse: " + StringUtil.beautify(getMousePos()) + "\n";
		if (currentAction == Action.Tile1 || currentAction == Action.Size1 || currentAction == Action.Tile2) {
			debug += "Tile 1: " + (pos1 != null ? StringUtil.beautify(tile1.getHitbox().topLeft()) : "") + "\n";
			debug += "Tile 1 Size: " + (size1 != null ? StringUtil.beautify(tile1.getHitbox().getSize()) : "") + "\n";
			debug += "Tile 2: " + (pos2 != null ? StringUtil.beautify(pos2) : "") + "\n";
		} else

		if (currentAction == Action.Removing) {
			debug += "Remove: " + (!removal.getHitbox().getSize().equals(new PVector(0, 0))
					? StringUtil.beautify(removal.getHitbox().topLeft())
					: "") + "\n";

		}
		P.game.text(debug, 0, 0);

		if (currentAction == Action.Tile1 || currentAction == Action.Tile2) {
			P.game.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			P.game.text(platformTypes[currentPlatform].getCanonicalName(), P.width / 2, P.height);
		}

		if (currentAction == Action.Tile1 || currentAction == Action.Size1) {
			tile1.onRender();
		}
		if (currentAction == Action.Tile2) {
			tile1.onRender();
			tile2.onRender();
		}

		if (currentAction == Action.Removing) {
			removal.onRender();
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
		if (currentAction == Action.Tile1 && pos1 != null) {
			if (platformTypes[currentPlatform] == Checkpoint.class) {
				PVector pos = pos1.copy();
				pos.y += TerrainManager.TILE_SIZE
						- (Checkpoint.stickHeight + Checkpoint.flagHeight - Checkpoint.offset);
				TerrainManager.addTile(createTile(Checkpoint.class, pos));
				pos1 = null;
			} else if (platformTypes[currentPlatform] == MovingPlatform.class) {
				size1 = null;
				tile1.getHitbox().setPos(pos1);
				tile1.getHitbox().setSize(new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
				currentAction = Action.Tile2;
			} else {
				currentAction = Action.Size1;
			}
		} else if (currentAction == Action.Tile2 && pos2 != null) {
			if (platformTypes[currentPlatform] == MovingPlatform.class) {
				float speed = 1;
				MovingPlatform p = new MovingPlatform(pos1, pos2, speed);
				EntityManager.addEntity(p);
				pos2 = null;
				pos1 = null;
				size1 = null;
				currentAction = Action.Tile1;
			}
		}
	}

	public static void onMouseRelease(int mouseButton) {
		if (currentAction == Action.Size1 && pos1 != null) {
			if (platformTypes[currentPlatform] != Checkpoint.class
					&& platformTypes[currentPlatform] != MovingPlatform.class) {
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
				size1 = null;
				currentAction = Action.Tile1;
			}
		}
	}

	public static void onKeyPress(char key) {
		if (!enabled)
			return;
		if (P.keyEscape) {
			if (currentAction == Action.Tile1 && size1 != null) {
				size1 = null;
			} else {
				currentAction = Action.None;
			}
		}
		if (key == 'f') {
			EntityManager.getPlayer().toggleFlying();
		}
		if (key == 'e') {
			currentAction = Action.Tile1;
		}
		if (key == 'r') {
			currentAction = Action.Removing;
		}
	}

	public static void onScroll(int scrollAmt) {
		if (scrollAmt < 0) {
			currentPlatform = (currentPlatform - 1 + platformTypes.length) % platformTypes.length;
		} else if (scrollAmt > 0) {
			currentPlatform = (currentPlatform + 1) % platformTypes.length;
		}
	}

}
