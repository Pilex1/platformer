package entities;

import static main.MainApplet.*;

import core.Fonts;
import main.EntityManager;
import main.TerrainManager;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;
import terrain.*;
import util.*;

public class Player extends Npc {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private float npcRange = 300;
	private boolean flying = false;

	private PVector defaultSpawn;

	private boolean tutorialMove = false;
	private boolean tutorialTalk = false;

	private int deaths = 0;

	private PImage img;

	public Player() {
		super(new PVector(150, TerrainManager.Floor), "");
		// super(new PVector(5600, game.Floor), "");

		defaultSpawn = getHitbox().topLeft().copy();
		color = Color.White;

		img = Images.player;
	}

	@Override
	protected void onUpdate() {
		handleInputs();
		checkRespawning();
		Npc npc = EntityManager.getClosestNpc(this);
		if (npc != null && npc.getDistanceTo(this) > npcRange) {
			leaveAllTalking();
		}
		Sandbox.update();
		
	//	System.out.println(EntityManager.getPlayer().getPlatformsStandingOn().size());
	}

	@Override
	public void onRender() {

		P.game.transparency(128);
		P.game.image(img, hitbox.topLeft(), P.getCamera());

		P.game.fill(Color.White);
		P.game.textFont(Fonts.LatoLight, 32);
		P.game.textAlign(PConstants.LEFT, PConstants.TOP);
		if (tutorialMove == false) {
			P.game.fill(Color.White);
			P.game.textFont(Fonts.LatoLight, 32);
			P.game.textAlign(PConstants.CENTER, PConstants.TOP);

			P.game.text("Press W, A, S, D to move around.", P.width / 2, 20);
		} else if (tutorialTalk == false) {
			P.game.fill(Color.White);
			P.game.textFont(Fonts.LatoLight, 32);
			P.game.textAlign(PConstants.CENTER, PConstants.TOP);

			P.game.text("Press ENTER when near an NPC to interact.", P.width / 2, 20);
		}

		P.game.fill(Color.White);
		P.game.textFont(Fonts.LatoLight, 24);
		P.game.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
		P.game.text("Deaths: " + deaths, P.width - 10, P.height - 10);
		
		Sandbox.render();
	}

	public PVector getPosTopLeft() {
		return new PVector(getHitbox().getCenterX() - P.width / 2, getHitbox().getCenterY() - P.height / 2);
	}

	private void handleInputs() {
		useGravity = !flying;
		if (P.keys['w']) {
			tutorialMove = true;
			if (flying) {
				flyUp();
			} else {
				jump();
			}
		}
		if (P.keys['s']) {
			tutorialMove = true;
			if (flying) {
				flyDown();
			}
		}
		if (P.keys['a']) {
			tutorialMove = true;
			if (flying) {
				flyLeft();
			} else {
				strafeLeft();
			}
		}
		if (P.keys['d']) {
			tutorialMove = true;
			if (flying) {
				flyRight();
			} else {
				strafeRight();
			}
		}

	}

	private void checkRespawning() {
		// respawning
		if (hitbox.getCenterY() >= 12000) {
			Checkpoint c = TerrainManager.getActiveCheckpoint();
			if (c == null) {
				hitbox.setPos(defaultSpawn);
			} else {
				teleportToCheckpoint(c);
			}
			setVel(new PVector(0, 0));
			TerrainManager.resetBlocks();
			deaths++;
		}
	}

	public void leaveAllTalking() {
		for (Npc npc : EntityManager.getAllNpcs()) {
			npc.leaveTalking();
		}
	}

	public void onMouseRelease(int mouseButton) {
		Sandbox.onMouseRelease(mouseButton);
	}

	public void onMousePress(int mouseButton) {
		Sandbox.onMousePress(mouseButton);
	}

	public void teleportToCheckpoint(Checkpoint c) {
		setVel(new PVector(0, 0));
		hitbox.setX1(c.getHitbox().getX1());
		hitbox.setY2(c.getHitbox().getY2());
	}

	public void toggleFlying() {
		flying = !flying;
	}

	public void onKeyPress(char key) {
		if (P.keyEnter) {
			Npc npc = EntityManager.getClosestNpc(this);
			if (npc != null) {
				if (npc.getDistanceTo(this) <= npcRange) {
					npc.talk();
					tutorialTalk = true;
				}
			}
		}
		Sandbox.onKeyPress(key);
	}

	public void onScroll(int scrollAmt) {
		Sandbox.onScroll(scrollAmt);
	}
}
