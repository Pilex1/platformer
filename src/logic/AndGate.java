package logic;

import java.io.Serializable;

import processing.core.PVector;
import util.Images;

public class AndGate extends LogicTile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AndGate(PVector pos) {
		super(pos);
	}

	@Override
	public void neighbouringUpdate() {
	}
	
	@Override
	protected boolean isIntermediate() {
		return true;
	}

	private void changeState(boolean s) {
		// System.out.println(0);
		active = s;
		getNeighbouringWires().forEach(w -> {
			w.active = active;
			w.updateNetwork();
		});
	}

	@Override
	public void onUpdate() {
		int count = 0;
		for (Wire w : getNeighbouringWires()) {
			if (w.connectedToActiveEmitter) {
				count++;
			}
		}
 
		//System.out.println(count);
		boolean newState = count >= 2;
		if (active != newState) {
			changeState(newState);
		}
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.AndOn : Images.AndOff);
	}

	public void reset() {
		// TODO Auto-generated method stub

	}

}
