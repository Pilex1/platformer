package logic;

import main.Images;
import processing.core.PVector;

public class Inverter extends Emitter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Inverter(PVector pos) {
		super(pos);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean outputSignal(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.UP || dir == Direction.DOWN) {
			return false;
		}
		return !active;
	}

	@Override
	public void onUpdate() {
		boolean newActive = false;
		for (Connection c : getAdjacentConnections(Direction.LEFT)) {
			if (c.active) {
				newActive = true;
			}
		}
		if (newActive != active) {
			active = newActive;
			getAdjacentConnections().forEach(c -> c.updateNetwork());
		}
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.InverterOn : Images.InverterOff);
	}

}
