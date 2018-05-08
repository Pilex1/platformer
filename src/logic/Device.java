package logic;

import java.io.Serializable;

import processing.core.PVector;

/**
 * any logic tile that requires a signal to run
 *
 */
public abstract class Device extends LogicTile implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Device(PVector pos) {
		super(pos);
	}

}
