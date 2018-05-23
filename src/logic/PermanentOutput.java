package logic;

import main.Images;
import processing.core.PVector;

public class PermanentOutput extends Emitter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PermanentOutput(PVector pos) {
		super(pos);
		active=true;
	}

	@Override
	protected boolean outputSignal(Direction dir) {
		return true;
	}

	@Override
	public void onUpdate() {
		updateAround();
	}

	@Override
	public void onRender() {
		renderImage(Images.SensorOn);
	}

}
