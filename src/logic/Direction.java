package logic;

public enum Direction {
	UP, RIGHT, DOWN, LEFT;

	public Direction opposite() {
		return Direction.values()[(ordinal() + 2) % 4];
	}
	
	public Direction rotateClockwise() {
		return Direction.values()[(ordinal() + 1) % 4];
	}
	
	public Direction rotateCounterClockwise() {
		return Direction.values()[(ordinal() + 3) % 4];
	}
}
