package fr.uge.game.element;

/**
 * Record representing coordinate with x and y values.
 */
public record Coordinate(int x, int y) {
	
	/**
	 * Returns the x-coordinate.
	 * 
	 * @return the x value
	 */
	public int x() {
		return x;
	}
	
	/**
	 * Returns the y-coordinate.
	 * 
	 * @return the y value
	 */
	public int y() {
		return y;
	}
}
