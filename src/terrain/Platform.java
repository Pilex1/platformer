package terrain;

import static main.MainApplet.*;

import java.io.*;
import java.util.ArrayList;

import entities.*;
import main.EntityManager;
import main.TerrainManager;
import processing.core.PImage;
import processing.core.PVector;
import util.*;

public class Platform implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected transient PImage img;

	protected float friction = 0.8f;
	protected Rectangle hitbox;
	protected boolean solid = true;

	public Platform(PVector pos) {
		hitbox = new Rectangle(pos, new PVector(50, 50));
		onLoad();
	}

	public Platform(String[] arr) {
		this(new PVector(Float.parseFloat(arr[0]), Float.parseFloat(arr[1])));
	}

	// called when deserialising
	public void onLoad() {
		img = Images.platform;
	}

	public void onRender() {
		P.game.transparency(128);
		P.game.image(img, hitbox.topLeft(), P.getCamera());
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

	public float getLeftBoundary() {
		return hitbox.getX1();
	}

	public float getRightBoundary() {
		return hitbox.getX2();
	}

	public void onUpdate() {
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

	public void reset() {

	}

	public void moveTo(float newX, float newY) {
		TerrainManager.removePlatform(this);
		hitbox.setPos(new PVector(newX, newY));
		TerrainManager.addPlatform(this);
	}

	public Entity[] getEntitiesOn() {
		ArrayList<Entity> entities = new ArrayList<>();
		for (Entity e : EntityManager.getActiveEntities(true)) {
			if (e.getPlatformsStandingOn().contains(this)) {
				entities.add(e);
			}
		}
		return entities.toArray(new Entity[0]);
	}
}
