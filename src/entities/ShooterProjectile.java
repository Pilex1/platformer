package entities;

import java.util.ArrayList;

import main.EntityManager;
import main.Images;
import main.TerrainManager;
import processing.core.PVector;
import terrain.Tile;
import util.Rectangle;

/**
 * the shooter tile shoots shooterprojectiles which push entities around
 * @author pilex
 *
 */
public class ShooterProjectile extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int life;

	public ShooterProjectile(PVector pos, PVector vel) {
		super(new Rectangle(pos, new PVector(20, 20)));
		this.vel = vel;
		calculatePhysics = false;
		life = 120;
		serializable=false;
	}
	
	@Override
	public void onCollisionRight(Tile t) {
		EntityManager.removeEntity(this);
	}
	
	@Override
	public void onCollisionLeft(Tile t) {
		EntityManager.removeEntity(this);
	}
	
	@Override
	public void onCollisionUp(Tile t) {
		EntityManager.removeEntity(this);
	}
	
	@Override
	public void onCollisionDown(Tile t) {
		EntityManager.removeEntity(this);
	}

	@Override
	protected void onUpdate() {
		// if it colides with terrain we remove it
		if (TerrainManager.getCollisions(this, true, true).size() > 0) {
			EntityManager.removeEntity(this);
		}
		
		
		ArrayList<Entity> collisions = EntityManager.getCollisions(this);
		for (Entity e : collisions) {
			if (e instanceof ShooterProjectile) continue;
			// if it collides with an entity, we push that entity, before removing this projectile
			float factor = 2.5f;
			e.vel.x += factor * vel.x;
			e.vel.y += factor * vel.y;
			EntityManager.removeEntity(this);
		}
		
		life--;
		if (life==0) {
			EntityManager.removeEntity(this);
		}
	}

	@Override
	public void onRender() {
		renderImage(Images.ShooterProjectile);
	}

}
