package terrain;

import entities.*;
import processing.core.*;
import util.*;

public class HBouncePlatform extends Platform {

	public static final String Id = "H";

	private float hBounceStrength = 70f;
	private float vBounceStrength = -8f;
	private float maxVel = -15f;

	public HBouncePlatform(PVector pos) {
		super(pos);
		fillColor = Color.LightGreen;
	}

	@Override
	public String getId() {
		return Id;
	}

	@Override
	public void onCollisionLeft(Entity e) {
		if (e.getVelx() >= 0)
			return;
		e.setVelx(hBounceStrength);
		e.setVely(vBounceStrength);
	}

	@Override
	public void onCollisionRight(Entity e) {
		if (e.getVelx() <= 0)
			return;
		e.setVelx(-hBounceStrength);
		e.setVely(vBounceStrength);
	}

}
