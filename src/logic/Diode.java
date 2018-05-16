package logic;

import main.Images;
import processing.core.PVector;

public class Diode extends Emitter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Diode(PVector pos) {
		super(pos);
		allowRotations=true;
	}

	@Override
	protected boolean canHaveConnection(Direction dir) {
		if (dir==Direction.LEFT||dir==Direction.RIGHT) return true;
		return false;
	}
	
	@Override
	protected boolean outputSignal(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.UP || dir == Direction.DOWN) {
			return false;
		}
		return active;
	}

	@Override
	public void onUpdate() {
		boolean newActive = false;
		for (Connection c : getAdjacentConnections(Direction.LEFT)) {
			if (c.active) {
				newActive=true;
			}
		}
		if (newActive!=active) {
			active=newActive;
			updateAround();
		}
	}

	@Override
	public void onRender() {
		renderImage(active?Images.DiodeOn:Images.DiodeOff);
	}

}
