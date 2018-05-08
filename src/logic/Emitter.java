package logic;

import java.io.Serializable;

import processing.core.PVector;

/**
 * represents a LogicTile that emits a signal
 * @author pilex
 *
 */
public abstract class Emitter extends LogicTile implements Serializable{
	 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected Emitter(PVector pos) {
		super(pos);
	}
	
	protected abstract boolean outputSignal();

}
