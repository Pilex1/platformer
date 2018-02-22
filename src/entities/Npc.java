package entities;

import static main.MainApplet.*;

import java.util.*;

import core.Fonts;
import processing.core.PConstants;
import processing.core.PVector;
import terrain.*;
import util.*;

public class Npc extends Entity {

	protected String name;
	protected ArrayList<String> conversations = new ArrayList<>();
	private int conversationId = -1;

	public Npc(PVector pos, String name) {
		super(new Rectangle(pos, new PVector(20, 40)));
		this.name = name;
	}

	public boolean isTalking() {
		return conversationId != -1;
	}

	public void leaveTalking() {
		conversationId = -1;
	}

	public void talk() {
		conversationId++;
		if (conversationId >= conversations.size())
			conversationId = -1;
	}

	@Override
	public void onRender() {
		P.game.strokeWeight(0);
		P.game.fill(color);
		P.game.rect(hitbox, P.getCamera());

		if (isTalking()) {

			float w = 350;
			float h = 120;
			float margin = 10;

			float x = 0;
			float y = 0;

			PVector playerpos = P.game.getPlayer().getHitbox().getCenter();
			if (playerpos.x <= getHitbox().getX1()) {
				// render text on left
				x = hitbox.getCenterX() - P.getCamera().x - 20 - w;
			} else {
				// render text on right
				x = hitbox.getCenterX() - P.getCamera().x + 20;
			}
			y = hitbox.getY1() - P.getCamera().y - 20 - h;

			P.game.fill(Color.White.Transparent());
			P.game.stroke(Color.Black.Transparent());
			P.game.rect(x, y, w, h, 5);
			if (playerpos.x <= getHitbox().getX1()) {
				P.ellipse(x + w, y + h + 10, 20, 20);
			} else {
				P.ellipse(x, y + h + 10, 20, 20);
			}

			// conversation
			String text = conversations.get(conversationId);
			P.fill(Color.Black.Transparent().val);
			P.textSize(24);
			P.textAlign(PConstants.CENTER, PConstants.CENTER);
			P.text(text, x + margin, y + margin, w - 2 * margin, h - 2 * margin);

			// npc name
			P.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			P.textSize(24);
			P.fill(Color.White.Transparent().val);
			P.text(name, x, y);
		} else {
			P.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			P.fill(Color.White.Transparent().val);
			P.textFont(Fonts.TwCenMT, 24);
			P.text(name, hitbox.getCenterX() - camera.x, hitbox.getY1() - camera.y - 10);
		}
	}

}
