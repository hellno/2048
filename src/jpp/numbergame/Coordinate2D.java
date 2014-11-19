package jpp.numbergame;

/**
 * Two-dimensional coordinate
 * Coordinate2D has only immutable objects and its values are accessible as final fields.
 */
public class Coordinate2D {
	/**
	 * x component of the coordinate
	 */
	private final int x;

	/**
	 * y component of the coordinate
	 */
	private final int y;
	
	/**
	 * Create a new coordinate.
	 * The resulting coordinate is immutable.
	 *
	 * @param x x component
	 * @param y y component
	 */
	public Coordinate2D(int x, int y) {
		if(x < 0 || y < 0) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Coordinate2D)) {
			return false;
		}

		Coordinate2D o = (Coordinate2D) other;
		return getX() == o.getX() && getY() == o.getY();
	}

	@Override
	public int hashCode() {
		return getX() << 16 ^ getY();
	}

	@Override
	public String toString() {
		return String.format("(%d,%d)", getX(), getY());
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
