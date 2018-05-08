package logic;

import static main.MainApplet.P;

import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Vector2i;

/**
 * a wire that connects horizontal components and vertical components separately
 * @author pilex
 *
 */
public class PortalIntoTheThirdDimension extends Wire {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int gap = 5;
	
	protected boolean hActive, vActive;

	public PortalIntoTheThirdDimension(PVector pos) {
		super(pos);
	}
	
	 @Override
	public void updateNetwork() {
		super.updateNetwork(true,false);
		super.updateNetwork(false, true);
	}
	
	@Override
	protected boolean isIntermediate() {
		return true;
	}
	
	@Override
	public void onRender() {
		float cx = hitbox.getCenterX();
		float cy = hitbox.getCenterY();
		P.game.fill(hActive ? activeColor : inactiveColor);
		P.game.noStroke();
		P.game.rect(cx - radius, cy - radius, 2 * radius, 2 * radius, P.getCamera());
		float offset = TerrainManager.TILE_SIZE / 2 - radius;
		if (connectUp) {
			P.game.fill(vActive?activeColor:inactiveColor);
			P.game.rect(cx - radius, cy - TerrainManager.TILE_SIZE / 2, 2 * radius, offset-gap, P.getCamera());
		}
		if (connectRight) {
			P.game.fill(hActive?activeColor:inactiveColor);
			P.game.rect(cx + radius, cy - radius, offset, 2 * radius, P.getCamera());
		}
		if (connectDown) {
			P.game.fill(vActive?activeColor:inactiveColor);
			P.game.rect(cx - radius, cy + radius + gap, 2 * radius, offset-gap, P.getCamera());
		}
		if (connectLeft) {
			P.game.fill(hActive?activeColor:inactiveColor);
			P.game.rect(cx - TerrainManager.TILE_SIZE / 2, cy - radius, offset, 2 * radius, P.getCamera());
		}
	}

}
