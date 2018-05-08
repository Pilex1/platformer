package entities;

import static main.MainApplet.P;

import java.io.Serializable;
import java.util.*;

import main.TerrainManager;
import processing.core.*;
import terrain.*;
import util.*;

public abstract class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static float epsilon = 0.1f;

	private static float gravityDy = 0.8f;
	private static float airResis = 0.8f;

	protected Rectangle hitbox;

	protected boolean useGravity = true;
	// the maximum velocity at which the entity can move through moving by
	// itself
	// note this does not include bounce blocks
	protected PVector maxVel = new PVector(15, 20);
	protected PVector vel = new PVector(0, 0);
	protected PVector acceleration = new PVector(1.75f, 15);

	public Color color;

	protected Entity(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	// called when deserialising
	public void onLoad() {

	}

	public final void update() {
		vel.x *= getCurrentFriction();
		if (useGravity) {
			vel.y += gravityDy;
		} else {
			vel.y = 0;
		}
		vel = MathUtil.clampAbsolute(vel, maxVel);

		moveRight(vel.x);
		moveDown(vel.y);

		onUpdate();
	}

	public void moveLeft(float x) {
		if (x < 0) {
			moveRight(-x);
			return;
		}
		while (x > 0) {
			if (x > epsilon) {
				moveLeftDx(epsilon);
			} else {
				moveLeftDx(x);
			}
			x -= epsilon;
		}
	}

	public void moveRight(float x) {
		if (x < 0) {
			moveLeft(-x);
			return;
		}
		while (x > 0) {
			if (x > epsilon) {
				moveRightDx(epsilon);
			} else {
				moveRightDx(x);
			}
			x -= epsilon;
		}
	}

	public void moveUp(float y) {
		if (y < 0) {
			moveDown(-y);
			return;
		}
		while (y > 0) {
			if (y > epsilon) {
				jumpDy(epsilon);
			} else {
				jumpDy(y);
			}
			y -= epsilon;
		}
	}

	public void moveDown(float y) {
		if (y < 0) {
			moveUp(-y);
			return;
		}
		while (y > 0) {
			if (y > epsilon) {
				fallDy(epsilon);
			} else {
				fallDy(y);
			}
			y -= epsilon;
		}
	}

	public void strafeLeft() {
		if (vel.x <= -maxVel.x)
			return;
		vel.x -= acceleration.x;
		vel.x = Math.max(vel.x, -maxVel.x);
	}

	public void strafeRight() {
		if (vel.x >= maxVel.x)
			return;
		vel.x += acceleration.x;
		vel.x = Math.min(vel.x, maxVel.x);
	}

	public void jump() {
		if (inAir())
			return;
		vel.y -= acceleration.y;
		vel.y = Math.max(vel.y, -maxVel.y);
	}

	public void flyUp() {
		vel.y = -maxVel.y;
	}

	public void flyDown() {
		vel.y = maxVel.y;
	}

	public void flyLeft() {
		vel.x = -maxVel.x;
	}

	public void flyRight() {
		vel.x = maxVel.x;
	}

	public boolean inAir() {
		return getTilesStandingOn().size() == 0;
	}

	/**
	 * gets the platforms which the entity is standing on<br>
	 * 
	 * @return
	 */
	public ArrayList<Tile> getTilesStandingOn() {
		ArrayList<Tile> colliding = new ArrayList<>();
		ArrayList<Tile> tiles = TerrainManager.getActiveTiles();
		for (Tile t : tiles) {
			if (getHitbox().getY2() == t.getHitbox().getY1() && getHitbox().getX2() > t.getHitbox().getX1()
					&& getHitbox().getX1() < t.getHitbox().getX2()) {
				colliding.add(t);
			}
		}
		return colliding;
	}

	/**
	 * gets the friction value of the tile that the entity is currently standing on
	 * <br>
	 * returns the value of airResistance if the entity is not standing on anything
	 */
	public float getCurrentFriction() {
		ArrayList<Tile> platforms = getTilesStandingOn();
		if (platforms.size() == 0)
			return airResis;
		return platforms.get(0).getFriction();
	}

	/**
	 * moves up by a small amount
	 * 
	 * @param dy
	 */
	private void jumpDy(float dy) {
		hitbox.decrY(dy);
		ArrayList<Tile> colliding = TerrainManager.getCollisions(this, true);
		// find the bottom-most edge out of all the colliding platforms
		float topMost = Float.MIN_VALUE;
		for (Tile platform : colliding) {
			topMost = Math.max(topMost, platform.getHitbox().getY2());
		}
		if (topMost != Float.MIN_VALUE) {
			hitbox.setY1(topMost);
		}
		for (Tile p : colliding) {
			p.onCollisionUp(this);
		}

		ArrayList<Tile> collidingNotSolid = TerrainManager.getCollisions(this, false);
		for (Tile p : collidingNotSolid) {
			p.onCollisionUp(this);
		}
	}

	/**
	 * moves down by a small amount
	 * 
	 * @param dy
	 */
	private void fallDy(float dy) {
		hitbox.incrY(dy);
		ArrayList<Tile> colliding = TerrainManager.getCollisions(this, true);
		// find the top-most edge out of all the colliding platforms
		float topMost = Float.MAX_VALUE;
		for (Tile p : colliding) {
			topMost = Math.min(topMost, p.getHitbox().getY1());
		}
		if (topMost != Float.MAX_VALUE) {
			hitbox.setY2(topMost);
		}
		for (Tile platform : colliding) {
			platform.onCollisionDown(this);
		}

		ArrayList<Tile> collidingNotSolid = TerrainManager.getCollisions(this, false);
		for (Tile p : collidingNotSolid) {
			p.onCollisionDown(this);
		}
	}

	/**
	 * moves left by a small amount
	 * 
	 * @param dx
	 */
	private void moveLeftDx(float dx) {
		hitbox.decrX(dx);
		ArrayList<Tile> colliding = TerrainManager.getCollisions(this, true);
		// find the right-most edge out of all the colliding platforms
		float rightMost = Float.MIN_VALUE;
		for (Tile platform : colliding) {
			rightMost = Math.max(rightMost, platform.getHitbox().getX2());
		}
		if (rightMost != Float.MIN_VALUE) {
			hitbox.setX1(rightMost);
		}
		for (Tile p : colliding) {
			p.onCollisionLeft(this);
		}

		ArrayList<Tile> collidingNotSolid = TerrainManager.getCollisions(this, false);
		for (Tile p : collidingNotSolid) {
			p.onCollisionLeft(this);
		}
	}

	/**
	 * moves right by a small amount
	 * 
	 * @param dx
	 */
	private void moveRightDx(float dx) {
		hitbox.incrX(dx);
		ArrayList<Tile> colliding = TerrainManager.getCollisions(this, true);
		// find the left-most edge out of all the colliding platforms
		float leftMost = Float.MAX_VALUE;
		for (Tile p : colliding) {
			leftMost = Math.min(leftMost, p.getHitbox().getX1());
		}
		if (leftMost != Float.MAX_VALUE) {
			hitbox.setX2(leftMost);
		}
		for (Tile platform : colliding) {
			platform.onCollisionRight(this);
		}

		ArrayList<Tile> collidingNotSolid = TerrainManager.getCollisions(this, false);
		for (Tile p : collidingNotSolid) {
			p.onCollisionRight(this);
		}
	}

	public boolean isIntersecting(Entity e) {
		return hitbox.isIntersecting(e.hitbox);
	}

	public float getDistanceTo(Entity other) {
		return PVector.dist(hitbox.getCenter(), other.hitbox.getCenter());
	}
	
	public PVector getVel() {
		return vel;
	}

	public float getVely() {
		return vel.y;
	}

	public float getVelx() {
		return vel.x;
	}

	public void setVel(PVector vel) {
		this.vel = vel.copy();
	}

	public void setVely(float y) {
		vel.y = y;
	}

	public void setVelx(float x) {
		vel.x = x;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public abstract void onRender();

	protected void onUpdate() {
	}
	
	protected void renderImage(PImage img) {
		P.game.transparency(128);
		P.game.image(img, hitbox.topLeft(), P.getCamera());
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + hitbox.topLeft();
	}

}
