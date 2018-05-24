package entities;

import static main.MainApplet.*;

import core.Fonts;
import main.EntityManager;
import main.Images;
import main.Sandbox;
import main.TerrainManager;
import processing.core.PConstants;
import processing.core.PVector;
import terrain.*;
import util.*;

public class Player extends Entity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * the distance the player needs to be within to interact with the guide
	 */
	private float guideRange = 8 * TerrainManager.TILE_SIZE;

	private PVector defaultSpawn;

	private int deaths = 0;
	
	/**
	 * stores the number of frames elapsed since the player has reached >= 90% terminal velocity
	 * the player automatically respawns when this reaches a certain amount
	 */
	private int c;

	public Player() {
		super(new Rectangle(new PVector(), new PVector(20,40)));
		hitbox.setPos(new PVector(TerrainManager.CENTER_X, TerrainManager.CENTER_Y - hitbox.getHeight()));

		defaultSpawn = hitbox.topLeft().copy();
		serializable =false;
	}
	
	public int getDeaths() {
		return deaths;
	}

	@Override
	protected void onUpdate() {
		if (flying) {
			vel.x = vel.y = 0;
		}
		handleInputs();
		
		if (vel.y >= 0.9 * getTerminalVelocity()) {
			c++;
		} else {
			c = 0;
		}
		// if the player has been at >= 0.9 * terminal velocity for more than 1 second, we force a respawn
		if (c >= 1 * 60) {
		teleportToCheckpoint(TerrainManager.getActiveCheckpoint());
		}
		
		// if the player is at the bottom of the world, we also respawn
		if (hitbox.getCenterY() >= TerrainManager.TILE_SIZE * TerrainManager.TILES_Y) {
			teleportToCheckpoint(TerrainManager.getActiveCheckpoint());
		}
		Sandbox.update();

		// System.out.println(EntityManager.getPlayer().getPlatformsStandingOn().size());
	}

	@Override
	public void onRender() {
		P.game.transparency(192);
		P.game.image(Images.Player, hitbox.topLeft(), P.getCamera());

		P.game.fill(Color.White);
		P.game.textFont(Fonts.LatoLight, 24);
		P.game.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
		P.game.text("Deaths: " + deaths, P.width - 10, P.height - 10);

		Sandbox.render();
	}

	
	private void handleInputs() {
		if (P.keys['w']) {
			if (flying) {
				flyUp();
			} else {
				jump();
			}
		}
		if (P.keys['s']) {
			if (flying) {
				flyDown();
			}
		}
		if (P.keys['a']) {
			if (flying) {
				flyLeft();
			} else {
				strafeLeft();
			}
		}
		if (P.keys['d']) {
			if (flying) {
				flyRight();
			} else {
				strafeRight();
			}
		}
	}

	/**
	 * stops any active conversations with the guide
	 */
	public void leaveAllTalking() {
		for (Guide npc : EntityManager.getGuides()) {
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
		Guide npc = EntityManager.getClosestGuide(this);
		if (npc != null && npc.getDistanceTo(this) > guideRange) {
			leaveAllTalking();
		}
		if (c == null) {
			// teleport to spawn
			hitbox.setPos(defaultSpawn);
		} else {
			hitbox.setX1(c.getHitbox().getX1());
			hitbox.setY2(c.getHitbox().getY2());
		}
		setVel(new PVector(0, 0));
		deaths++;
		TerrainManager.resetBlocks();
		if (c!=null) {
			c.onUpdate();
		}
		
	}

	public void toggleFlying() {
		flying = !flying;
		calculatePhysics = !flying;
		vel.x = vel.y = 0;
		acceleration.x = acceleration.y = 0;
	}

	public void onKeyPress(char key) {
		if (P.keyEnter) {
			// interact with the guide on enter
			Guide npc = EntityManager.getClosestGuide(this);
			if (npc != null) {
				if (npc.getDistanceTo(this) <= guideRange) {
					npc.talk();
				}
			}
		}
		Sandbox.onKeyPress(key);
	}

	public void onScroll(int scrollAmt) {
		Sandbox.onScroll(scrollAmt);
	}
}
