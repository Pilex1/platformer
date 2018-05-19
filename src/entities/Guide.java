package entities;

import static main.MainApplet.P;

import java.util.ArrayList;

import core.Fonts;
import main.EntityManager;
import processing.core.PConstants;
import processing.core.PVector;
import util.Color;
import util.Rectangle;

public class Guide extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected ArrayList<String> conversations;
	private int conversationId = -1;

	public Color color;

	public Guide(PVector pos, ArrayList<String> conversations) {
		super(new Rectangle(pos, new PVector(20, 40)));
		this.conversations = conversations;
		color = Color.LightViolet.a(128);
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
		//P.game.fill(color);
		P.game.fill(Color.LightGreen.a(128));
		P.game.rect(hitbox, P.getCamera());

		if (isTalking()) {

			float w = 350;
			float h = 120;
			float margin = 10;

			float x = 0;
			float y = 0;

			PVector playerpos = EntityManager.getPlayer().getHitbox().getCenter();
			if (playerpos.x <= getHitbox().getX1()) {
				// render text on left
				x = hitbox.getCenterX() - P.getCamera().x - 20 - w;
			} else {
				// render text on right
				x = hitbox.getCenterX() - P.getCamera().x + 20;
			}
			y = hitbox.getY1() - P.getCamera().y - 20 - h;

			P.game.fill(Color.DarkGrey.a(192));
			P.game.stroke(Color.Grey.a(192));
			P.game.roundedRect(x, y, w, h, 5);
			if (playerpos.x <= getHitbox().getX1()) {
				P.game.circle(x + w, y + h + 10, 20);
			} else {
				P.game.circle(x, y + h + 10, 20);
			}

			// conversation
			String text = conversations.get(conversationId);
			P.game.fill(Color.White.Transparent());
			P.game.textSize(24);
			P.game.textAlign(PConstants.CENTER, PConstants.CENTER);
			P.game.text(text, x + margin, y + margin, w - 2 * margin - x, h - 2 * margin-y);

			// npc name
			P.game.textAlign(PConstants.LEFT, PConstants.BOTTOM);
			P.game.textSize(24);
			P.game.fill(Color.White.Transparent());
			P.game.text("Guide", x, y);
		} else {
			P.game.textAlign(PConstants.CENTER, PConstants.BOTTOM);
			P.game.fill(Color.White.Transparent());
			P.game.textFont(Fonts.LatoLight, 24);
		 P.game.text("Guide", hitbox.getCenterX() - P.getCamera().x, hitbox.getY1() - P.getCamera().y -
			 10);
		}
	}

}
