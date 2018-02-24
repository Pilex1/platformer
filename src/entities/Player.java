package entities;

import static main.MainApplet.*;

import core.Fonts;
import main.*;
import processing.core.PConstants;
import processing.core.PVector;
import terrain.*;
import util.*;

public class Player extends Npc {

	private enum PlatformId {
		Regular, VBounce, HBounce, Invisible, Phantom, Checkpoint;

		PlatformId incr() {
			int id = ordinal();
			id++;
			if (id >= PlatformId.values().length) {
				id = 0;
			}
			return PlatformId.values()[id];
		}

		PlatformId decr() {
			int id = ordinal();
			id--;
			if (id < 0) {
				id = PlatformId.values().length - 1;
			}
			return PlatformId.values()[id];
		}
	};

	private enum Editing {
		Playing, EditPos, EditSize, Removing
	}

	private float npcRange = 300;
	private boolean flying = false;
	private Editing editing = Editing.Playing;
	private PlatformId platformId = PlatformId.Regular;

	private PVector defaultSpawn;

	private boolean tutorialMove = false;
	private boolean tutorialTalk = false;

	private int deaths = 0;

	public Player() {
		super(new PVector(150, Game.Floor), "");
		// super(new PVector(5600, game.Floor), "");

		defaultSpawn = getHitbox().getPos().copy();
		color = Color.White;
	}

	public boolean isRemoving() {
		return editing == Editing.Removing;
	}

	public boolean isEditing() {
		return editing == Editing.EditPos || editing == Editing.EditSize;
	}

	public PVector getPosTopLeft() {
		return new PVector(getHitbox().getCenterX() - P.width / 2,
				getHitbox().getCenterY() - P.height / 2);
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
			Checkpoint c = P.game.getActiveCheckpoint();
			if (c == null) {
				hitbox.setPos(defaultSpawn);
			} else {
				teleportToCheckpoint(c);
			}
			setVel(new PVector(0, 0));
			P.game.resetBlocks();
			deaths++;
		}
	}

	public PVector getMousePos() {
		float mx = P.mouseX + hitbox.getCenterX() - P.width / 2;
		float my = P.mouseY + hitbox.getCenterY() - P.height / 2;
		return new PVector(mx, my);
	}

	private void renderDebug() {
		P.game.fill(Color.White);
		P.game.textFont(Fonts.TwCenMT, 32);
		P.game.textAlign(PConstants.LEFT, PConstants.TOP);

		String debug = "";
		debug += "P: " + StringUtil.beautifyString(hitbox.getCenter()) + "\n";
		debug += "MP: " + StringUtil.beautifyString(getMousePos()) + "\n";
		debug += "V: " + StringUtil.beautifyString(vel) + "\n";
		if (isEditing()) {
			debug += "EP: " + StringUtil.beautifyString(P.game.getGuidePos()) + "\n";
			debug += "ES: " + StringUtil.beautifyString(P.game.getGuideSize()) + "\n";
		} else if (isRemoving()) {
			debug += "RP: " + StringUtil.beautifyString(P.game.getGuideRemovalPos()) + "\n";
			debug += "RS: " + StringUtil.beautifyString(P.game.getGuideRemovalSize()) + "\n";
		}
		P.game.text(debug, 0, 0);
	}

	private PVector blockify(PVector v, int x) {
		return new PVector((int) (v.x / x) * x, (int) (v.y / x) * x);
	}

	private void handleEditing() {
		P.game.fill(Color.White);
		P.game.textFont(Fonts.TwCenMT, 32);
		P.game.textAlign(PConstants.CENTER, PConstants.BOTTOM);
		P.game.text(platformId.name(), P.width / 2, P.height);

		PVector mousePos = getMousePos();
		if (editing == Editing.EditPos) {
			P.game.setGuidePos(blockify(mousePos, 10));
		} else if (editing == Editing.EditSize) {
			PVector offset = PVector.sub(mousePos, P.game.getGuidePos());
			if (offset.x >= 0)
				offset.x = Math.max(offset.x, 50);
			else
				offset.x = Math.min(offset.x, -50);
			if (offset.y >= 0)
				offset.y = Math.max(offset.y, 50);
			else
				offset.y = Math.min(offset.y, -50);
			P.game.setGuideSize(blockify(offset, 50));
		}
	}

	public Platform getSelectedPlatform() {
		Platform[] platforms = P.game.getActivePlatforms();
		Platform selected = null;
		for (Platform p : platforms) {
			PVector mousePos = getMousePos();
			if (p.getHitbox().inside(mousePos)) {
				selected = p;
				break;
			}
		}
		return selected;
	}

	private void handleRemoving() {
		Platform selected = getSelectedPlatform();
		if (selected == null) {
			P.game.setGuideRemovalSize(new PVector());
			P.game.setGuideRemovalPos(new PVector());
			return;
		}
		P.game.setGuideRemovalPos(selected.getHitbox().getPos());
		P.game.setGuideRemovalSize(selected.getHitbox().getSize());
	}

	public void leaveAllTalking() {
		for (Npc npc : P.game.getAllNpcs()) {
			npc.leaveTalking();
		}
	}

	@Override
	protected void onUpdate() {
		handleInputs();
		checkRespawning();
		Npc npc = P.game.getClosestNpc(this);
		if (npc != null && npc.getDistanceTo(this) > npcRange) {
			leaveAllTalking();
		}
	}
	
	@Override
	public void onRender() {
		super.onRender();
		if (P.debug) {
			if (isEditing()) {
				handleEditing();
			} else if (isRemoving()) {
				handleRemoving();
			}
			renderDebug();
		}

		if (tutorialMove == false) {
			P.game.fill(Color.White);
			P.game.textFont(Fonts.TwCenMT, 32);
			P.game.textAlign(PConstants.CENTER, PConstants.TOP);

			P.game.text("Press W, A, S, D to move around.", P.width / 2, 20);
		} else if (tutorialTalk == false) {
			P.game.fill(Color.White);
			P.game.textFont(Fonts.TwCenMT, 32);
			P.game.textAlign(PConstants.CENTER, PConstants.TOP);

			P.game.text("Press ENTER when near an NPC to interact.", P.width / 2, 20);
		}

		P.game.fill(Color.White);
		P.game.textFont(Fonts.TwCenMT, 24);
		P.game.textAlign(PConstants.RIGHT, PConstants.BOTTOM);
		P.game.text("Deaths: " + deaths, P.width - 10, P.height - 10);
	}

	private void setEditing(Editing editing) {
		if (isEditing()) {
			P.game.setGuideSize(new PVector(50, 50));
		}
		this.editing = editing;
	}

	public void updateOnMousePressed(int mouseButton) {
		if (editing == Editing.EditPos) {
			editing = Editing.EditSize;
			switch (platformId) {
			case Checkpoint:
				PVector pos = P.game.getGuidePos();
				float y = pos.y;
				y += 50;
				y -= Checkpoint.stickHeight + Checkpoint.flagHeight - Checkpoint.offset;
				float x = pos.x;
				P.game.addPlatform(new Checkpoint(new PVector(x, y)));
				setEditing(Editing.EditPos);
			default:
				break;
			}
		} else if (editing == Editing.EditSize) {
			PVector guidePos = P.game.getGuidePos();
			PVector guideSize = P.game.getGuideSize();
			Rectangle rect = new Rectangle(guidePos, guideSize).regularise();
			for (int i = 0; i < rect.getWidth() / 50; i++) {
				for (int j = 0; j < rect.getHeight() / 50; j++) {
					PVector pos = rect.getPos().copy();
					pos.x += i * 50;
					pos.y += j * 50;
					switch (platformId) {
					case Regular:
						P.game.addPlatform(new Platform(pos));
						break;
					case VBounce:
						P.game.addPlatform(new VBouncePlatform(pos));
						break;
					case Checkpoint:
						break;
					case Invisible:
						P.game.addPlatform(new InvisiblePlatform(pos));
						break;
					case Phantom:
						P.game.addPlatform(new PhantomPlatform(pos));
						break;
					case HBounce:
						P.game.addPlatform(new HBouncePlatform(pos));
						break;
					default:
						break;
					}
				}
			}
			editing = Editing.EditPos;
			P.game.setGuideSize(new PVector(50, 50));
		} else if (editing == Editing.Removing) {
			Platform selected = getSelectedPlatform();
			if (selected != null) {
				P.game.removePlatform(selected);
			}
		}
	}

	public void teleportToCheckpoint(Checkpoint c) {
		setVel(new PVector(0, 0));
		hitbox.setX1(c.getHitbox().getX1());
		hitbox.setY2(c.getHitbox().getY2());
	}

	public void updateOnKeyTyped(int key) {
		if (P.keyEnter) {
			Npc npc = P.game.getClosestNpc(this);
			if (npc != null) {
				if (npc.getDistanceTo(this) <= npcRange) {
					npc.talk();
					tutorialTalk = true;
				}
			}
		}
		if (P.debug) {
			if (P.keyEscape) {
				setEditing(Editing.Playing);
			}
			if (key == 'f') {
				flying = !flying;
			}
			if (key == 'e') {
				setEditing(Editing.EditPos);
			}
			if (key == 'r') {
				setEditing(Editing.Removing);
			}
			if (key == 'j') {
				platformId = platformId.decr();
			}
			if (key == 'l') {
				platformId = platformId.incr();
			}
			if (key == 'u') {
				Checkpoint c = P.game.getPreviousCheckpoint(this);
				if (c != null) {
					teleportToCheckpoint(c);
					;
				}
			}
			if (key == 'o') {
				Checkpoint c = P.game.getNextCheckpoint(this);
				if (c != null) {
					teleportToCheckpoint(c);
				}
			}
		}
	}
}
