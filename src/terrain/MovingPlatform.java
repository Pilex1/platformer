package terrain;

import entities.Entity;
import main.TerrainManager;
import processing.core.*;

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
		this.start = start;
		this.end = end;
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
		moveTo(hitbox.getX1() + dx, hitbox.getY1() + dy);
		// update any entities standing on top
		Entity[] entitiesOn = getEntitiesOn();
		for (Entity e : entitiesOn) {
			e.moveRight(dx);
			e.moveDown(dy);
		}

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

	@Override
	public void onCollisionLeft(Entity e) {
		super.onCollisionLeft(e);
		
		// tries to push the entity to the left
		// if the entity gets stuck in a block as a result,
		// then undo the push and instead make the platform
		// reverse direction

		e.getHitbox().incrX(dx);
		if (TerrainManager.getCollisions(e).size() > 0) {
			e.getHitbox().decrX(dx);
			dx *= -1;
			onUpdate();
		}

	}

	@Override
	public void onCollisionRight(Entity e) {
		super.onCollisionRight(e);

		e.getHitbox().incrX(dx);
		if (TerrainManager.getCollisions(e).size() > 0) {
			e.getHitbox().decrX(dx);
			dx *= -1;
			onUpdate();
		}
	}

	@Override
	public void onCollisionDown(Entity e) {
		super.onCollisionDown(e);
		// moveTo(hitbox.getX1(), e.getHitbox().getY1() - hitbox.getY1());
		// dy *= -1;
	}

	@Override
	public void onCollisionUp(Entity e) {
		super.onCollisionUp(e);
		// moveTo(hitbox.getX1(), e.getHitbox().getY2());
		// dy *= -1;
	}

}
