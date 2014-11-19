package jpp.numbergame;

/**
 * Class representing a tile move.
 *
 * Objects of this class are immutable and their values are accessible with final fields.
 */
public class Move {
	/**
	 * Coordinate of tile before move
	 */
	private final Coordinate2D from;

	/**
	 * Coordinate of tile after move
	 */
	private final Coordinate2D to;

	/**
	 * Value of tile before move.
	 */
	private final int oldValue;
	
	/**
	 * Value of tile after move.
	 */
	private final int newValue;

	/**
	 * Create a new Move.
	 *
	 * @param from source coordinate
	 * @param to target coordinate
	 * @param newValue value of tile after move
	 */
	public Move(Coordinate2D from, Coordinate2D to, int oldValue, int newValue) {
		if(oldValue < 1 || newValue < 1) throw new IllegalArgumentException();
		this.from = from;
		this.to = to;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * Create a new non-merge Move.
	 *
	 * @param from source coordinate
	 * @param to target coordinate
	 */
	public Move(Coordinate2D from, Coordinate2D to, int value) {
		this(from, to, value, value);
	}

	/**
	 * Check if this move is a merge move (i.e. newValue is greater than 0)
	 *
	 * @return true, if the move caused a tile merge
	 */
	public boolean isMerge() {
		return getNewValue() != getOldValue();
	}

	@Override
	public String toString() {
		return String.format("%s -> %s%s", getFrom().toString(), getTo().toString(), isMerge() ? " (M)" : "");
	}

	/**
	 * @return the from
	 */
	public Coordinate2D getFrom() {
		return from;
	}

	/**
	 * @return the to
	 */
	public Coordinate2D getTo() {
		return to;
	}

	/**
	 * @return the newValue
	 */
	public int getNewValue() {
		return newValue;
	}

	/**
	 * @return the oldValue
	 */
	public int getOldValue() {
		return oldValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result + newValue;
		result = prime * result + oldValue;
		result = prime * result + ((to == null) ? 0 : to.hashCode());
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
		Move other = (Move) obj;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (newValue != other.newValue)
			return false;
		if (oldValue != other.oldValue)
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		return true;
	};
}
