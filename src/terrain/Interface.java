package terrain;

import logic.Drain;
import logic.LogicTile;
import main.Images;
import main.TerrainManager;
import processing.core.PVector;

/**
 * a logic-activated block that can place/remove special interface platform
 * blocks
 * 
 * @author pilex
 *
 */
public class Interface extends Drain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Interface(PVector pos) {
		super(pos);
		allowRotations=true;
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int range = 12;
		for (int i = 1; i < range; i++) {
			Tile t = TerrainManager.getTileRelative(this, rotation.rotateClockwise(), i);
			if (t instanceof InterfacePlatform) {
				InterfacePlatform plat = (InterfacePlatform) t;
				plat.solid = active;
				
			}
		}

	}

	@Override
	public void onRender() {
		renderImage(active?Images.InterfaceOn:Images.InterfaceOff);
		super.onRender();
	}

}
