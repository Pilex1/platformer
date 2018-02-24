package terrain;

import static main.MainApplet.*;

import processing.core.*;
import util.*;

public class Checkpoint extends Platform {

	public static final String Id = "C";

	public static float stickHeight = 120;
	public static float stickWidth = 20;
	public static float flagHeight = 60;
	public static float flagWidth = 60;
	public static float offset = 20;

	private boolean checked = false;

	public Checkpoint(PVector pos) {
		super(pos);
		hitbox.setWidth(flagWidth);
		hitbox.setHeight(flagHeight + stickHeight - offset);
		solid = false;
	}

	@Override
	public String getId() {
		return Id;
	}

	public boolean isChecked() {
		return checked;
	}

	@Override
	public void onRender() {
		P.game.stroke(Color.Black);
		P.game.fill(20, 20, 30);
		P.rect(hitbox.getX1() - P.getCamera().x, hitbox.getY2() - stickHeight - P.getCamera().y, stickWidth, stickHeight);

		if (checked) {
			P.game.fill(Color.MediumGreen);
		} else {
			P.game.fill(Color.Red);
		}
		P.triangle(hitbox.getX1() - P.getCamera().x, hitbox.getY1() - P.getCamera().y, hitbox.getX1() + flagWidth - P.getCamera().x,
				hitbox.getY1() + flagHeight / 2 - P.getCamera().y, hitbox.getX1() - P.getCamera().x,
				hitbox.getY1() + flagHeight - P.getCamera().y);
	}

	@Override
	public void onUpdate() {
		if (isIntersecting(P.game.getPlayer())) {
			for (Checkpoint c : P.game.getAllCheckpoints()) {
				c.checked = false;
			}
			checked = true;
		}
	}
}
