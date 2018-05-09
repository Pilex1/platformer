package logic;

import java.io.Serializable;

import entities.Entity;
import main.Images;
import processing.core.PVector;

public class Sensor extends Emitter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int prevEntityCount;

	public Sensor(PVector pos) {
		super(pos);
	}

	@Override
	public void onUpdate() {
		Entity[] entities = getEntitiesOn();
		if (prevEntityCount == 0 && entities.length > 0) {
			active = !active;
			updateAdjacent();
		}
		prevEntityCount = entities.length;
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.SensorOn : Images.SensorOff);
	}

	@Override
	protected boolean outputSignal(Direction dir) {
		return active;
	}

	@Override
	public void neighbouringUpdate() {

	}

}
