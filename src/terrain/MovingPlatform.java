package terrain;

import entities.Entity;
import main.EntityManager;
import main.TerrainManager;
import processing.core.*;
import util.Rectangle;

public class MovingPlatform extends Platform {

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
		super(start);
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

	}

	public MovingPlatform(String[] arr) {
		this(new PVector(Float.parseFloat(arr[0]), Float.parseFloat(arr[1])),
				new PVector(Float.parseFloat(arr[2]), Float.parseFloat(arr[3])), Float.parseFloat(arr[4]));

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
		// otherwise we leave the entities as they are, and the platform waits

		Entity[] activeEntities = EntityManager.getActiveEntities(true);
		boolean intersecting = false;
		Rectangle newHitbox = getHitbox().copy();
		newHitbox.incrX(dx);
		newHitbox.incrY(dy);
		for (Entity e : activeEntities) {
			Rectangle entityHitbox = e.getHitbox().copy();
			if (newHitbox.isIntersecting(entityHitbox)) {
				if (dx > 0) {
					entityHitbox.setX1(newHitbox.getX2());
				} else {
					entityHitbox.setX2(newHitbox.getX1());
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

			// move any entities standing on top in the direction that the platform is
			// moving
			Entity[] entitiesOn = getEntitiesOn();
			for (Entity e : entitiesOn) {
				e.moveRight(dx);
				e.moveDown(dy);
			}

			for (Entity e : activeEntities) {
				Rectangle entityHitbox = e.getHitbox();
				if (newHitbox.isIntersecting(entityHitbox)) {
					if (dx > 0) {
						entityHitbox.setX1(newHitbox.getX2());
					} else {
						entityHitbox.setX2(newHitbox.getX1());
					}
					System.out.println("pushing entity");
				}
			}

			moveTo(newHitbox.topLeft());

		}

		
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
	}

}
