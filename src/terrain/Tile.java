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
	 * if false, entities will fall through the tile as if it wasn't there
	 */
	protected boolean solid = true;

	protected float friction = 0.8f;

	protected PVector pos;
	protected Rectangle hitbox;

	protected Direction rotation = Direction.UP;
	protected boolean canRotate;

	protected Tile(PVector pos) {
		this.pos = pos;
		hitbox = new Rectangle(pos, new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE));
	}
	
	public Direction getRotation( ) {
		return rotation;
	}

	public void rotate() {
		if (!canRotate)
			return;
		rotation = rotation.rotateClockwise();
	}

	/**
	 * this is called when deserialising a Tile for extra initialisation
	 * functionality e.g. when loading a Wire tile, its neighbouring connections
	 * must be calculated
	 */
	public abstract void onLoad();

	public abstract void onUpdate();

	public abstract void onRender();

	public abstract void afterRemove();

	public abstract void reset();

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

	public void onCollisionUp(Entity e) {
		if (!solid)
			return;
		e.setVely(0);
	}

	public void onCollisionDown(Entity e) {
		if (!solid)
			return;
		e.setVely(0);
	}

	public void onCollisionLeft(Entity e) {
		if (!solid)
			return;
		e.setVelx(0);
	}

	public void onCollisionRight(Entity e) {
		if (!solid)
			return;
		e.setVelx(0);
	}

	public void moveTo(float newX, float newY) {
		TerrainManager.removePlatform(this);
		hitbox.setPos(new PVector(newX, newY));
		TerrainManager.addTile(this);
	}

	public void moveTo(PVector p) {
		moveTo(p.x, p.y);
	}

	public Entity[] getEntitiesOn() {
		ArrayList<Entity> entities = new ArrayList<>();
		for (Entity e : EntityManager.getAllEntities()) {
			if (e.getTilesStandingOn().contains(this)) {
				entities.add(e);
			}
		}
		return entities.toArray(new Entity[0]);
	}

	protected void renderImage(PImage img) {
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

		P.game.image(img, hitbox.topLeft(), rotationAmt, P.getCamera());
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
