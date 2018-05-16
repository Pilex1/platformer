package terrain;

import logic.Drain;
import logic.LogicTile;
import main.Images;
import main.TerrainManager;
import processing.core.PVector;

/**
 * a logic-activated block than can place/remove special interface platform
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
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		int range = 8;
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
		// TODO Auto-generated method stub

	}

	public class InterfacePlatform extends Platform {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public InterfacePlatform(PVector pos) {
			super(pos);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onRender() {
			if (solid) {
				renderImage(Images.Platform);
			}
		}

	}

}
