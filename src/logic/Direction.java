package logic;

public enum Direction {
	UP, RIGHT, DOWN, LEFT;

	public Direction opposite() {
		return Direction.values()[(ordinal() + 2) % 4];
	}
	
	public Direction rotateClockwise() {
		return rotateClockwise(RIGHT);
	}
	
	public Direction rotateAntiClockwise() {
		return rotateAntiClockwise(RIGHT);
	}
	
	public Direction rotateClockwise(Direction dir) {
		return Direction.values()[(ordinal()+dir.ordinal())%4];
	}
	
	public Direction rotateAntiClockwise(Direction dir) {
		return Direction.values()[(ordinal()-dir.ordinal()+4)%4];
	}
	
}
