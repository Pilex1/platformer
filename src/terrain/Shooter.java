package terrain;

import processing.core.PVector;
import util.MathUtil;

import static main.MainApplet.P;

import entities.ShooterProjectile;
import logic.Drain;
import main.EntityManager;
import main.Images;
import main.TerrainManager;

public class Shooter extends Drain {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
		PVector center = new PVector((float) (25 * 2 * Math.cos(theta)), (float) (25 * 2 * Math.sin(theta)));
		center = center.add(hitbox.getCenter());
		PVector pos = center.add(new PVector(-10, -10));
		PVector vel = new PVector((float) (speed * Math.cos(theta)), (float) (speed * Math.sin(theta)));
		// vel = new PVector(0,0);
		ShooterProjectile proj = new ShooterProjectile(center, vel);
		EntityManager.addEntity(proj);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (cooldown > 0) {
			cooldown--;
		}

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
