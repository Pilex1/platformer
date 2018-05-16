package terrain;

import java.util.ArrayList;
import java.util.HashSet;

import entities.Entity;
import main.EntityManager;
import main.Images;
import main.TerrainManager;
import processing.core.*;
import util.Rectangle;


/**
 * LET'S JUST NOT USE THIS
 * I SPENT LIKE WEEKS WORKING ON THIS AND IT STILL DOESN'T EVEN WORK
 * @author pilex
 *
 */
public class MovingPlatform extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PVector start;
	private PVector end;
	private float speed;
	private float dx;
	private float dy;

	public MovingPlatform(PVector start, PVector end, float speed) {
		super(new Rectangle(start, new PVector(TerrainManager.TILE_SIZE, TerrainManager.TILE_SIZE)));
		
		if (end.x < start.x) {
			this.start = end;
			this.end = start;
		} else {
			this.end = end;
			this.start = start;
		}
		this.speed = speed;

		float theta = (float) Math.atan2(end.y - start.y, end.x - start.x);

		dx = speed * (float) Math.cos(theta);
		dy = speed * (float) Math.sin(theta);

		useGravity = false;

	}

	@Override
	public String toString() {
		return start.x + " " + start.y + " " + end.x + " " + end.y + " " + speed;
	}

	@Override
	public void onUpdate() {

		// try to see if we can push entities standing next to the platform
		// if after being pushed, none of the entities are stuck in a tile
		// then we actually push all the entities
		// otherwise the platform waits for the
		// entities to move

		HashSet<Entity> entities = EntityManager.getAllEntities();

		boolean intersecting = false;
		Rectangle platformHitbox = getHitbox().copy();
		platformHitbox.incrX(dx);
		platformHitbox.incrY(dy);
		for (Entity e : entities) {
			Rectangle entityHitbox = e.getHitbox().copy();
			if (platformHitbox.isIntersecting(entityHitbox)) {

				if (dx > 0) {
					if (entityHitbox.getX1() > platformHitbox.getX1()) {
						// platform is moving to the right and entity is to the right of platform //
						// platform
						entityHitbox.setX1(platformHitbox.getX2());
					} else {
						// platform is moving to the right and entity is to the left of platform
						entityHitbox.setX2(platformHitbox.getX1());
					}
				} else {
					if (entityHitbox.getX1() > platformHitbox.getX1()) {
						// platform is moving to the left and entity is to the right of platform //
						// platform
						entityHitbox.setX1(platformHitbox.getX2());
					} else {
						// platform is moving to the left and entity is to the left of platform //
						// platform
						entityHitbox.setX2(platformHitbox.getX1());

					}
				}

				if (TerrainManager.getCollisions(entityHitbox, true).size() > 0) {
					intersecting = true;
					break;
				}
			}
		}
		if (intersecting) {
			// if we push an entity, it will get stuck, so don't do anything
			// wait until the entity moves
		} else {
			// we can push entities without them getting stuck

			// if the platform reaches its boundary, then reverse its direction
			Rectangle hitbox = getHitbox();
			if (hitbox.getX1() >= end.x) {
				dx *= -1;
				dy *= -1;
				hitbox.setPos(end);
			}
			if (hitbox.getX1() <= start.x) {
				dx *= -1;
				dy *= -1;
				hitbox.setPos(start);
			}

				// move any entities standing on top in the direction that the platform is
			// moving
			ArrayList<Entity> entitiesOn = new ArrayList<>();
			for (Entity e : EntityManager.getAllEntities()) {
				if (e == this)
					continue;
				if (e.getHitbox().getX2() > getHitbox().getX1() && e.getHitbox().getX1() < getHitbox().getX2()) {
					if (e.getVely() > 0 && e.getHitbox().getY2() >= getHitbox().getY1()
							&& e.getHitbox().getY1() <= getHitbox().getY2()) {
						entitiesOn.add(e);
					}
				}
			}

			for (Entity e : entitiesOn) {
				e.getHitbox().setY2(getHitbox().getY1());
				e.setVely(0);

				e.moveRight(dx);
				e.moveDown(dy);
			}

			for (Entity e : entities) {
				Rectangle entityHitbox = e.getHitbox();
				if (platformHitbox.isIntersecting(entityHitbox, false)) {

					if (dx > 0) {
						if (entityHitbox.getX1() > platformHitbox.getX1()) {
							// platform is moving to the right and entity is to the right of platform //
							// platform
							entityHitbox.setX1(platformHitbox.getX2());
						} else {
							// platform is moving to the right and entity is to the left of platform
							entityHitbox.setX2(platformHitbox.getX1());
						}
					} else {
						if (entityHitbox.getX1() > platformHitbox.getX1()) {
							// platform is moving to the left and entity is to the right of platform //
							// platform
							entityHitbox.setX1(platformHitbox.getX2());
						} else {
							// platform is moving to the left and entity is to the left of platform //
							// platform
							entityHitbox.setX2(platformHitbox.getX1());

						}
					}

				}
			}

			hitbox.incrX(dx);
			hitbox.incrY(dy);
		}

	}

	@Override
	public void onRender() {
		renderImage(Images.Platform);
	}

}
