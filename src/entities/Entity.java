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

	public static final float epsilon = 0.1f;

	/**
	 * acceleration due to gravity
	 */
	public static final float gravity = 0.8f;
	public static final float verticalDrag = 0.0008f;

	public static final float horizontalDrag = 0.2f;

	/*
	 * vertical acceleration is calculated as follows: a = g - k * v^2 where g is
	 * acceleration due to gravity, and k is the drag constant terminal velocity is
	 * then given by v = sqrt(g/k) (found by setting a = 0)
	 * 
	 * IDK HOW TO CALCULATE JUMP HEIGHT AND TIME!!!
	 * 
	 * HOW DO U FIND AN EQUATION IN TERMS OF TIME?
	 */

	/*
	 * horizontal acceleration is calculated as follows: a = -f * v where f is the
	 * friction constant of the current block terminal velocity is given by v = s/f,
	 * where s is the strafing acceleration (a = -f * v + s, and let a = 0)
	 */

	protected float strafingAcceleration = 1.5f;
	protected float jumpingAcceleration = 16.5f;

	protected Rectangle hitbox;

	/**
	 * set this to false if flying
	 */
	protected boolean calculatePhysics = true;
	protected PVector flyingSpeed = new PVector(8f, 8f);

	protected PVector vel = new PVector(0, 0);
	protected PVector acceleration = new PVector(0, 0);

	public Color color;

	/**
	 * number of frames since the entity was last in the air
	 */
	private int lastInAir;

	private boolean moveLeft, moveRight;

	protected Entity(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public float getStrafingAccel() {
		return strafingAcceleration;
	}

	public float getJumpAccel() {
		return jumpingAcceleration;
	}

	// called when deserialising
	public void onLoad() {

	}

	public final void update() {

		if (calculatePhysics) {
			// vel.x = vel.x * getCurrentFriction() * horizontalDrag;

			acceleration.x = -Math.signum(vel.x) * getCurrentFriction() * Math.abs(vel.x);
			if (moveLeft) {
				acceleration.x -= strafingAcceleration;
			}
			if (moveRight) {
				acceleration.x += strafingAcceleration;
			}
			moveLeft = moveRight = false;

			if (inAir()) {
				acceleration.y = gravity - Math.signum(vel.y) * verticalDrag * vel.y * vel.y;
			} else {
				acceleration.y = 0;
			}

			vel.x += acceleration.x;
			vel.y += acceleration.y;

			// System.out.println(acceleration.x);

		} else {
			// vel.y = 0;
		}

		moveRight(vel.x);
		moveDown(vel.y);

		getTilesStandingOn(true).forEach(t -> t.onStanding(this));

		if (!inAir()) {
			lastInAir++;
		} else {
			lastInAir = 0;
		}

		onUpdate();

	}

	public final void moveLeft(float x) {
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

	public final void moveRight(float x) {
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

	public final void moveUp(float y) {
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

	public final void moveDown(float y) {
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

	public final void strafeLeft() {
		for (Tile t : getTilesStandingOn(true)) {
			if (!t.allowMovement)
				return;
		}
		moveLeft = true;
		// acceleration.x -=strafingAcceleration;
	}

	public final void strafeRight() {
		for (Tile t : getTilesStandingOn(true)) {
			if (!t.allowMovement)
				return;
		}
		moveRight = true;
		// vel.x += strafingAcceleration;
		// acceleration.x += strafingAcceleration;
	}

	public final void jump() {
		if (inAir())
			return;
		ArrayList<Tile> tiles = getTilesStandingOn(true);
		for (Tile t : tiles) {
			if (!t.allowRepeatedJumps && lastInAir <= 1)
				return;
			if (!t.allowJumps)
				return;
		}

		vel.y -= jumpingAcceleration;
	}

	public final void flyUp() {
		vel.y = -flyingSpeed.y;
	}

	public final void flyDown() {
		vel.y = flyingSpeed.y;
	}

	public final void flyLeft() {
		vel.x = -flyingSpeed.x;
	}

	public final void flyRight() {
		vel.x = flyingSpeed.x;
	}

	public final boolean inAir() {
		return getTilesStandingOn(true).size() == 0;
	}

	/**
	 * gets the platforms which the entity is standing on<br>
	 * if solid is true, only gets solid platforms that the entity is standing on if
	 * solid is false, only gets non-solid platforms the entity is standing on
	 * 
	 * @return
	 */
	public ArrayList<Tile> getTilesStandingOn(boolean solid) {
		ArrayList<Tile> colliding = new ArrayList<>();
		ArrayList<Tile> tiles = TerrainManager.getActiveTiles();
		for (Tile t : tiles) {
			if (getHitbox().getY2() == t.getHitbox().getY1() && getHitbox().getX2() > t.getHitbox().getX1()
					&& getHitbox().getX1() < t.getHitbox().getX2()) {
				if (t.isSolid() == solid) {
					colliding.add(t);
				}
			}
		}
		return colliding;
	}

	/**
	 * gets the friction value of the tile that the entity is currently standing on
	 * <br>
	 * if the entity is not standing on anything, then it returns horizontal drag
	 */
	public float getCurrentFriction() {
		ArrayList<Tile> platforms = getTilesStandingOn(true);
		if (platforms.size() == 0)
			return horizontalDrag;
		float min = Float.MAX_VALUE;
		for (Tile p : platforms) {
			min = Math.min(min, p.getFriction());
		}
		return min;
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

	/**
	 * returns the position of the top left corner of the player hitbox
	 * 
	 * @return
	 */
	public PVector getPos() {
		return new PVector(getHitbox().getCenterX() - P.width / 2, getHitbox().getCenterY() - P.height / 2);
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

	public PVector getAccel() {
		return acceleration;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " " + hitbox.topLeft();
	}

	public void onCollisionLeft(Tile t) {
		vel.x = 0;
	}

	public void onCollisionRight(Tile t) {
		vel.x = 0;
	}

	public void onCollisionUp(Tile t) {
		vel.y = 0;
	}

	/**
	 * called when an entity is moving downwards and collides with this block
	 * 
	 * @param e
	 */
	public void onCollisionDown(Tile t) {
		vel.y = 0;
	}

}
