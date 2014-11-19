package jpp.numbergame;

public final class Tile {
	private final Coordinate2D coordinate;
	private final int value;

	public Tile(Coordinate2D coord, int value) {
		if(value < 1) {
			throw new IllegalArgumentException();
		}
		this.coordinate = coord;
		this.value = value;
	}

	/**
	 * @return the coordinate
	 */
	public Coordinate2D getCoordinate() {
		return coordinate;
	}

	/**
	 * @return the value
	 */
	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((coordinate == null) ? 0 : coordinate.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		if (value != other.value)
			return false;
		return true;
	}
}