package logic;

import java.io.Serializable;

import main.Images;
import processing.core.PVector;

/**
 * inputs on two horizontal sides must be on then vertical outputs turn on
 * 
 * @author pilex
 *
 */
public class AndGate extends Emitter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected boolean activeLeft, activeRight;

	public AndGate(PVector pos) {
		super(pos);
		canRotate=true;
	}

	@Override
	public void onRender() {
		if (active) {
			renderImage(Images.AndOn);
		} else if (activeLeft) {
			renderImage(Images.AndLeft);
		} else if (activeRight) {
			renderImage(Images.AndRight);
		} else {
			renderImage(Images.AndOff);
		}
	}

	@Override
	public void onUpdate() {
		int count = 0;
		for (Connection c : getAdjacentConnections(Direction.LEFT)) {
			if (c.active) {
				count++;
				activeLeft=true;
			} else {
				activeLeft=false;
			}
		}
		for (Connection c : getAdjacentConnections(Direction.RIGHT)) {
			if (c.active) {
				count++;
				activeRight=true;
			} else {
				activeRight=false;
			}
		}
		boolean newActive = count==2;
		if (newActive!=active) {
			active = newActive;
			getAdjacentConnections().forEach(c->c.updateNetwork());
		}
	}

	@Override
	protected boolean outputSignal(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return false;
		}
		return active;
	}

}
