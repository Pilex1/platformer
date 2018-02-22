package terrain;

import entities.*;
import processing.core.*;
import util.*;

public class VBouncePlatform extends Platform {

	public static final String Id = "V";

	private float hBounceStrength = 1.05f;
	private float vBounceStrength = -1.5f;
	private float maxVel = -20f;

	public VBouncePlatform(PVector pos) {
		super(pos);
		fillColor = Color.Purple;
	}

	@Override
	public String getId() {
		return Id;
	}

	@Override
	public void onCollisionUp(Entity e) {
		if (e.getVely() >= 0)
			return;

		float vy = e.getVely();
		vy *= vBounceStrength;
		e.setVely(vy);

		float vx = e.getVelx();
		vx *= hBounceStrength;
		e.setVelx(vx);
	}

	@Override
	public void onCollisionDown(Entity e) {
		if (e.getVely() <= 0)
			return;

		float vy = e.getVely();
		vy *= vBounceStrength;
		vy = Math.max(vy, maxVel);
		e.setVely(vy);

		float vx = e.getVelx();
		vx *= hBounceStrength;
		e.setVelx(vx);
	}

}
