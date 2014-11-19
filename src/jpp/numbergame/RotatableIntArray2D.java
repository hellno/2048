package jpp.numbergame;

import java.util.Iterator;

/**
 * 2d array of integer values with rotatable indexing. The array represents a
 * rectangle of integers with a cartesian coordinate system. It can be rotated
 * from its starting position, transforming coordinates on calling getters and
 * setters.
 *
 *
 */
public class RotatableIntArray2D implements Iterable<Integer> {
	/**
	 * Rotation in degrees clockwise.
	 */
	public enum RotationMode {
		/** No rotation */
		ROTATION_0,
		/** Rotation by 90 degrees clockwise */
		ROTATION_90,
		/** Rotation by 180 degrees */
		ROTATION_180,
		/** Rotation by 270 degrees clockwise */
		ROTATION_270
	}

	private RotationMode rot = RotationMode.ROTATION_0;

	private int[][] values;
	private int width, height;

	/**
	 * Create a new instance with fixed size.
	 *
	 * @param width
	 *            width in unrotated state
	 * @param height
	 *            height in unrotated state
	 */
	public RotatableIntArray2D(int width, int height) {
		this.width = width;
		this.height = height;
		this.values = new int[width][height];
	}

	/**
	 * Return the value at given coordinates at current rotation.
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return value at (x,y) in the rotated coordinate system
	 */
	public int get(int x, int y) {
		OutOfBoundsRotation(x,y);
		Coordinate2D c = getUnrotated(x, y);
		return values[c.getX()][c.getY()];
	}

	/**
	 * Change the value at given coordinates at current rotation.
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @param value
	 *            new value
	 */
	public void set(int x, int y, int value) {
		OutOfBoundsRotation(x,y);
		Coordinate2D c = getUnrotated(x, y);
		values[c.getX()][c.getY()] = value;
	}

	/**
	 * Return the value at given coordinates at current rotation.
	 *
	 * @param c
	 *            coordinate
	 * @return value at <code>c</code> in rotated coordinate system
	 */
	public int get(Coordinate2D c) {
		return get(c.getX(), c.getY());
	}

	/**
	 * Change the value at given coordinates at current rotation.
	 *
	 * @param c
	 *            coordinate
	 * @param value
	 *            new value
	 */
	public void set(Coordinate2D c, int value) {
		set(c.getX(), c.getY(), value);
	}

	/**
	 * Change to unrotated state
	 */
	public void rot0() {
		rot = RotationMode.ROTATION_0;
	}

	/**
	 * Change rotation to 90 degrees clockwise
	 */
	public void rot90() {
		rot = RotationMode.ROTATION_90;
	}

	/**
	 * Change rotation to 180 degrees
	 */
	public void rot180() {
		rot = RotationMode.ROTATION_180;
	}

	/**
	 * Change rotation to 270 degrees clockwise
	 */
	public void rot270() {
		rot = RotationMode.ROTATION_270;
	}

	/**
	 * Get the coordinate in the unrotated array corresponding to the given
	 * coordinate in the current rotation.
	 *
	 * @param c
	 *            rotated coordinate
	 * @return corresponding coordinate to (x,y) in unrotated version
	 */
	public Coordinate2D getUnrotated(Coordinate2D c) {
		return getUnrotated(c.getX(), c.getY());
	}

	/**
	 * Get the coordinate in the unrotated array corresponding to the given
	 * coordinate in the current rotation.
	 *
	 * @param x
	 *            rotated x coordinate
	 * @param y
	 *            rotated y coordinate
	 * @return corresponding coordinate to (x,y) in unrotated version
	 */
	public Coordinate2D getUnrotated(int x, int y) {
		switch (rot) {
		case ROTATION_0:
			return new Coordinate2D(x, y);
		case ROTATION_90:
			return new Coordinate2D(y, height - 1 - x);
		case ROTATION_180:
			return new Coordinate2D(width - 1 - x, height - 1 - y);
		case ROTATION_270:
			return new Coordinate2D(width - 1 - y, x);
		default:
			throw new RuntimeException("BUG: invalid enum value");
		}
	}

	/**
	 * Get an iterator working linewise (along x axis in rotated coordinate
	 * system)
	 *
	 * @return linewise iterator
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new Iterator<Integer>() {
			int x = 0;
			int y = 0;

			@Override
			public boolean hasNext() {
				return x < width && y < height;
			}

			@Override
			public Integer next() {
				Integer v = get(x, y);
				x++;
				if (x >= width) {
					x = 0;
					y++;
				}
				return v;
			}

		};
	}

	@Override
	public String toString() {
		int xlim = width, ylim = height;
		if (rot == RotationMode.ROTATION_90 || rot == RotationMode.ROTATION_270) {
			xlim = height;
			ylim = width;
		}

		StringBuilder sb = new StringBuilder();

		for (int x = 0; x < xlim; x++) {
			int y;
			for (y = 0; y < ylim - 1; y++) {
				sb.append(get(x, y)).append(", ");
			}
			sb.append(get(x, y)).append("\n");
		}
		return sb.toString();
	}

	@SuppressWarnings("unused")
	private void OutOfBounds(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			throw new IndexOutOfBoundsException(
					String.format(
							"2D index out of bounds. Field size: %d x %d. Requested index [%d,%d]",
							width, height, x, y));
		}
	}

	private void OutOfBoundsRotation(int x, int y) {
		Coordinate2D c = getUnrotated(x, y);
		if (c.getX() < 0 || c.getY() < 0 || c.getX() >= width || c.getY() >= height) {
			throw new IndexOutOfBoundsException(
					String.format(
							"2D index out of bounds. Field size: %d x %d. Requested index [%d,%d], after rotation [%d,%d]",
							width, height, x, y, c.getX(), c.getY()));
		}
	}
}
