package terrain;

import processing.core.*;

public class MovingPlatform extends Platform {

	private PVector start;
	private PVector end;
	private float speed;
	private float dx;
	private float dy;

	public MovingPlatform(PVector start, PVector end, float speed) {
		super(start);
		this.start = start;
		this.end = end;
		this.speed = speed;

		float m = (end.y - start.y) / (end.x - start.x);
		dx = (float) (speed / Math.sqrt(m * m + 1));
		dy = (float) (speed * m / Math.sqrt(m * m + 1));
	}

	@Override
	public void onUpdate() {
		hitbox.incrX(dx);
		hitbox.incrY(dy);
		if (hitbox.getX1() >= end.x) {
			dx *= -1;
			dy *= -1;
			hitbox.setPos(end);
		}
		if (hitbox.getX1() <= start.x) {
			dx *= -1;
			dy *= -1;
			hitbox.setPos(start);
		}
	}

}
