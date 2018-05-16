package terrain;

import static main.MainApplet.P;

import java.io.Serializable;
import java.util.ArrayList;

import entities.Entity;
import logic.Direction;
import main.EntityManager;
import main.TerrainManager;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import util.Rectangle;
import util.Vector2i;

public abstract class Tile implements Serializable {

	/**
	 * 
	 */
	protected static final long serialVersionUID = 1L;

	/**
	 * if false, entities will fall through the tile as if it wasn't there
	 */
	protected boolean solid = true;

	/**
	 * higher values of friction correspond with a lower maximum velocity</br>
	 * having a friction of 1 means that you won't be able to move having a
	 * friction</br>
	 * of 0 means you will move indefinitely (think of those old school nintendo
	 * games with the ice puzzles!)
	 */
	protected float friction = Entity.horizontalDrag * 1.05f;

	protected PVector pos;
	protected Rectangle hitbox;

	protected Direction rotation = Direction.UP;
	protected boolean allowRotations;

	/**
	 * if entities can jump on this block
	 */
	public boolean allowJumps = true;

	/**
	 * if entites can make repeated jumps on this block</br>
	 * e.g. true for a vbounceplatform, as we do not want entities to be able to
	 * bounce higher by jumping when it rebounds
	 */
	public boolean allowRepeatedJumps = true;

	/**
	 * whether entities can move around on this tile
	 */
	public boolean allowMovement = true;

	public Tile(PVector pos) {
		this.pos = pos;
		hitbox = new Rectangle(pos, new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
	}

	public Direction getRotation() {
		return rotation;
	}

	public void rotate() {
		if (!allowRotations)
			return;
		rotation = rotation.rotateClockwise();
	}

	/**
	 * this is called when deserialising a Tile for extra initialisation
	 * functionality e.g. when loading a Wire tile, its neighbouring connections
	 * must be calculated
	 */
	public void onLoad() {
	}

	public abstract void onUpdate();

	public abstract void onRender();

	public void afterRemove() {
	}

	public void reset() {
	}

	public boolean isSolid() {
		return solid;
	}

	public boolean isIntersecting(Entity e) {
		return hitbox.isIntersecting(e.getHitbox());
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public float getFriction() {
		return friction;
	}

	/**
	 * called when an entity is moving upwards, and collides with this block
	 * 
	 * @param e
	 */
	public void onCollisionUp(Entity e) {
		if (!solid)
			return;
		e.onCollisionUp(this);
	}

	/**
	 * called when an entity is moving downwards and collides with this block
	 * 
	 * @param e
	 */
	public void onCollisionDown(Entity e) {
		if (!solid)
			return;
		e.onCollisionDown(this);
	}

	/**
	 * called when an entity is moving leftwards and collides with this block
	 * 
	 * @param e
	 */
	public void onCollisionLeft(Entity e) {
		if (!solid)
			return;
		e.onCollisionLeft(this);
	}

	/**
	 * called when an entity is moving rightwards and collides with this block
	 * 
	 * @param e
	 */
	public void onCollisionRight(Entity e) {
		if (!solid)
			return;
		e.onCollisionRight(this);
	}
	
	/**
	 * called when an entity is standing on the block
	 * @param e
	 */
	public void onStanding(Entity e) {
		
	}

	public Entity[] getEntitiesOn() {
		ArrayList<Entity> entities = new ArrayList<>();
		for (Entity e : EntityManager.getAllEntities()) {
			if (e.getTilesStandingOn(true).contains(this)) {
				entities.add(e);
			}
		}
		return entities.toArray(new Entity[0]);
	}

	protected void renderImage(PImage img) {
		renderImage(img, new PVector());
	}
	
	protected void renderImage(PImage img, PVector offset) {
		P.game.transparency(256);
		float rotationAmt = 0;
		switch (rotation) {
		case UP:
			rotationAmt = 0;
			break;
		case LEFT:
			rotationAmt = -PConstants.HALF_PI;
			break;
		case DOWN:
			rotationAmt = -PConstants.PI;
			break;
		case RIGHT:
			rotationAmt = -3 * PConstants.HALF_PI;
			break;
		}

		P.game.image(img, hitbox.topLeft().sub(offset), rotationAmt, P.getCamera());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + pos.x + "," + pos.y;
	}

	public Vector2i getTileId() {
		int x = (int) hitbox.getX1() / TerrainManager.TILE_SIZE;
		int y = (int) hitbox.getY1() / TerrainManager.TILE_SIZE;
		return new Vector2i(x, y);
	}

}
