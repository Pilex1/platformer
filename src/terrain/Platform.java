package terrain;

import static main.MainApplet.*;

import java.io.*;

import entities.*;
import processing.core.PVector;
import util.*;

public class Platform implements Serializable {

	public static final String Id = "P";

	protected Color strokeColor;
	protected Color fillColor;

	protected float friction = 0.85f;
	protected Rectangle hitbox;
	protected boolean solid = true;

	public Platform(PVector pos) {
		hitbox = new Rectangle(pos, new PVector(50, 50));
		fillColor = Color.DarkBlue;
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

	public void onRender() {
		if (strokeColor == null) {
			P.game.stroke(fillColor);
		} else {
			P.game.stroke(strokeColor);
		}
		P.game.fill(fillColor);
		P.game.rect(hitbox, P.getCamera());
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

	public String getId() {
		return Id;
	}

	public String saveString() {
		return getId() + " " + hitbox.getX1() + " " + hitbox.getY1();
	}

	public static Platform loadString(String s) {
		String[] arr = s.split(" ");
		PVector pos = new PVector(Float.parseFloat(arr[1]), Float.parseFloat(arr[2]));
		if (arr[0].equals(Platform.Id))
			return new Platform(pos);
		if (arr[0].equals(Checkpoint.Id))
			return new Checkpoint(pos);
		if (arr[0].equals(VBouncePlatform.Id))
			return new VBouncePlatform(pos);
		if (arr[0].equals(InvisiblePlatform.Id))
			return new InvisiblePlatform(pos);
		if (arr[0].equals(PhantomPlatform.Id))
			return new PhantomPlatform(pos);
		if (arr[0].equals(HBouncePlatform.Id))
			return new HBouncePlatform(pos);
		return null;
	}
}
