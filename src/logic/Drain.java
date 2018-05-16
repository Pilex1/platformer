package logic;

import processing.core.PVector;

/**
 * represents a logic tile that requires power to activate
 * @author pilex
 *
 */
public abstract class Drain extends LogicTile{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Drain(PVector pos) {
		super(pos);
	}
	
	@Override
	public void onUpdate() {
		active = false;
		getAdjacentConnections().forEach(c -> {
			if (c.isActive()) {
				active = true;
			}
		});
	}

}
