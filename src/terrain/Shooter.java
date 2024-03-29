package terrain;

import processing.core.PVector;
import util.MathUtil;

import static main.MainApplet.P;

import entities.ShooterProjectile;
import logic.Drain;
import main.EntityManager;
import main.Images;
import main.TerrainManager;

/**
 * periodically shoots projectiles which pushes the player around
 * @author pilex
 *
 */
public class Shooter extends Drain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * cooldown until the shooter can shoot again
	 */
	protected int cooldown;

	public Shooter(PVector pos) {
		super(pos);
	}

	@Override
	public void onLoad() {
		super.onLoad();
		resetCooldown();
	}

	protected void resetCooldown() {
		cooldown = 60 + P.R.nextInt(10);
		// cooldown=2;
	}

	protected void shoot() {
		float speed = 8;
		PVector delta = EntityManager.getPlayer().getHitbox().topLeft().sub(getHitbox().topLeft());
		float theta = (float) Math.atan2(delta.y, delta.x);
		theta += MathUtil.randFloat(P.R, -0.1f, 0.1f);
		// offset that the projectile is spawned relative to the center of the tile
		// note that we can't just spawn the projectile directly in the center of where
		// the tile is
		// because then it would get stuck in the tile
		// consider a circle of diameter 25 sqrt2 centered around the current tile
		// we spawn the projectile at angle theta around the circumference of the circle
		PVector pos = new PVector((float) (25 * 2 * Math.cos(theta)), (float) (25 * 2 * Math.sin(theta)));
		pos = pos.add(hitbox.getCenter());
		PVector vel = new PVector((float) (speed * Math.cos(theta)), (float) (speed * Math.sin(theta)));
		ShooterProjectile proj = new ShooterProjectile(pos, vel);
		EntityManager.addEntity(proj);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (cooldown > 0) {
			cooldown--;
		}

		// we shoot a projectile when the tile is active and the cooldown reaches 0
		// and when the distance between this tile and the player is within a 2 - 8 tile range
		// then we reset the cooldown
		float rangeMin = 2 * TerrainManager.TILE_SIZE;
		float rangeMax = 8 * TerrainManager.TILE_SIZE;
		if (active && cooldown == 0) {
			float dist = EntityManager.distBetween(hitbox, EntityManager.getPlayer().getHitbox());
			if (dist >= rangeMin && dist <= rangeMax) {
				shoot();
				resetCooldown();
			}
		}
	}

	@Override
	public void onRender() {
		renderImage(active ? Images.ShooterOn : Images.ShooterOff);
		super.onRender();
	}

}
