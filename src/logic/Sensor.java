package logic;

import java.io.Serializable;

import entities.Entity;
import processing.core.PVector;
import util.Images;

public class Sensor extends Emitter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected boolean state = false;

	private int prevEntityCount;

	public Sensor(PVector pos) {
		super(pos);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		Entity[] entities = getEntitiesOn();
		if (prevEntityCount == 0 && entities.length > 0) {
			toggleState();
		}
		prevEntityCount = entities.length;
	}

	@Override
	public void onRender() {
		renderImage(state ? Images.SensorOn : Images.SensorOff);
	}

	@Override
	protected boolean outputSignal() {
		return state;
	}

	public void setState(boolean b) {
		if (b == state)
			return;
		state = b;
		getNeighbouringWires().forEach(w->w.updateNetwork());
	}

	public void toggleState() {
		setState(!state);
	}

	public boolean isActive() {
		return state;
	}

	@Override
	public void reset() {
		state = false;
	}

	@Override
	public void neighbouringUpdate() {
		
	}

}
