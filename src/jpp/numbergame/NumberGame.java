package jpp.numbergame;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represents a game of 2048.
 */
public class NumberGame {
	public static final Random rand = new Random();
	public static final double PROBABILITY_FOUR = 0.1;

	private RotatableIntArray2D values;
	private int height, width;
	private int points = 0;

	/**
	 * Create a new game with given board size.
	 *
	 * @param width
	 *            number of horizontal tiles
	 * @param height
	 *            number of vertical tiles
	 */
	public NumberGame(int width, int height) {
		if(width < 1 || height < 1) throw new IllegalArgumentException();
		this.values = new RotatableIntArray2D(width, height);
		this.width = width;
		this.height = height;
	}

	/**
	 * Create a new game with given board size.
	 *
	 * @param width
	 *            number of horizontal tiles
	 * @param height
	 *            number of vertical tiles
	 */
	public NumberGame(int width, int height, int initialFields) {
		this(width, height);
		if(initialFields < 0 || initialFields > width * height) {
			throw new IllegalArgumentException("Invalid number of initial tiles");
		}
		for (int i = 0; i < initialFields; i++)
			addRandomTile();
	}

	/**
	 * Get the tile value at given coordinate
	 *
	 * @param c
	 *            coordinate of tile
	 * @return value of tile or 0, if no tile is at that coordinate
	 */
	public int get(Coordinate2D c) {
		return get(c.getX(), c.getY());
	}

	/**
	 * Get the tile value at given coordinate
	 *
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return value of tile or 0, if no tile is at that coordinate
	 */
	public int get(int x, int y) {
		if(x >= width || y >= height || x < 0 || y < 0) {
			throw new IndexOutOfBoundsException();
		}
		return values.get(x, y);
	}

	/**
	 * Check if there are empty fields on the board.
	 *
	 * @return true, if any field does not contain a tile.
	 */
	public boolean isFull() {
		for (int val : values) {
			if (val == 0)
				return false;
		}
		return true;
	}

	/**
	 * Return current number of points.
	 *
	 * Points are the total numb
	 *
	 * @return
	 */
	public int getPoints() {
		return points;
	}

	/**
	 * Move tiles in the given direction.
	 *
	 * @param dir
	 *            direction
	 * @return list of tile moves that resulted from the action
	 */
	public List<Move> move(Direction dir) {
		Coordinate2D limit = rotateAndGetLimits(dir);

		HashSet<Coordinate2D> merged = new HashSet<>();

		LinkedList<Move> moves = new LinkedList<>();

		// first line can not move and may be ignored
		for (int x = 1; x < limit.getX(); x++) {
			for (int y = 0; y < limit.getY(); y++) {
				Move m = move(x, y, merged);
				if (m != null) {
					moves.add(m);
				}
			}
		}
		values.rot0();
		return moves;
	}

	private Move move(int x, int y, HashSet<Coordinate2D> merged) {
		int cur = values.get(x, y);
		if (cur == 0)
			return null;
		int nx = x;

		// move along empty fields
		while (nx - 1 > -1 && values.get(nx - 1, y) == 0) {
			nx--;
		}
		values.set(x, y, 0);
		values.set(nx, y, cur);

		// check if merge is possible
		if (nx > 0 && !merged.contains(new Coordinate2D(nx - 1, y)) && values.get(nx - 1, y) == values.get(nx, y)) {
			int newValue = 2 * cur;
			values.set(nx, y, 0);
			values.set(nx - 1, y, newValue);
			merged.add(new Coordinate2D(nx - 1, y));
			points += newValue;
			return new Move(values.getUnrotated(x, y), values.getUnrotated(nx - 1, y), cur, newValue);
		} else if (nx < x) {
			return new Move(values.getUnrotated(x, y), values.getUnrotated(nx, y), cur);
		}
		return null;
	}

	/**
	 * Check if a move is possible in the given direction
	 *
	 * @param dir
	 *            direction for move
	 * @return true, if a move in that direction would result in tile moves
	 */
	public boolean canMove(Direction dir) {
		Coordinate2D limit = rotateAndGetLimits(dir);

		for (int x = 1; x < limit.getX(); x++) {
			for (int y = 0; y < limit.getY(); y++) {
				int cur = values.get(x, y);
				if (cur == 0)
					continue;
				if (values.get(x - 1, y) == 0 || values.get(x - 1, y) == cur)
					return true;
			}
		}
		return false;
	}

	/**
	 * Check if any move is possible.
	 *
	 * @return true, if a move is possible. If false is returned, the player
	 *         looses the game.
	 */
	public boolean canMove() {
		return canMove(Direction.LEFT) || canMove(Direction.RIGHT) || canMove(Direction.UP) || canMove(Direction.DOWN);
	}

	private Coordinate2D rotateAndGetLimits(Direction dir) {
		switch (dir) {
		case DOWN:
			values.rot90();
			return new Coordinate2D(height, width);
		case UP:
			values.rot270();
			return new Coordinate2D(height, width);
		case LEFT:
			values.rot0();
			return new Coordinate2D(width, height);
		case RIGHT:
			values.rot180();
			return new Coordinate2D(width, height);
		default:
			throw new RuntimeException("BUG: invalid direction");
		}
	}

	/**
	 * Add a tile at a random free location.
	 *
	 * The tile starts with a random value of 2 (90%) or 4 (10%).
	 *
	 * @return inserted Tile coordinates and value
	 * @throws TileExistsException
	 *             if all fields are occupied
	 */
	public Tile addRandomTile() throws TileExistsException {
		if (isFull())
			throw new TileExistsException("All fields contain tiles");
		int x, y;
		do {
			x = rand.nextInt(width);
			y = rand.nextInt(height);
		} while (values.get(x, y) != 0);

		int value = rand.nextDouble() < PROBABILITY_FOUR ? 4 : 2;
		return addTile(x, y, value);
	}

	/**
	 * Add a tile.
	 *
	 * @param x
	 *            x coordinate of the tile
	 * @param y
	 *            y coordinate of the tile
	 * @param value
	 *            initial tile value
	 * @return the tile definition
	 * @throws TileExistsException
	 *             if the field at the given coordinates is occupied
	 */
	public Tile addTile(int x, int y, int value) throws TileExistsException {
		if (values.get(x, y) > 0) {
			throw new TileExistsException(x, y);
		}
		values.set(x, y, value);
		return new Tile(new Coordinate2D(x, y), value);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		StringBuilder lineBuilder = new StringBuilder();
		for (int i = 0; i < width; i++) {
			lineBuilder.append("+----");
		}
		lineBuilder.append("+\n");
		String line = lineBuilder.toString();
		for (int y = 0; y < height; y++) {
			sb.append(line);
			for (int x = 0; x < width; x++) {
				int value = get(x, y);
				sb.append("|");
				sb.append(getStringForValue(value, 4));
			}
			sb.append("|\n");
		}
		sb.append(line);
		return sb.toString();
	}

	private static final char[] SUFFIXES = { 'k', 'M', 'G', 'T', 'P' };
	private static final int SUFFIX_BASE = 1024;

	private static String getStringForValue(int value, int field_width) {
		if (value == 0) {
			return String.format("%" + field_width + "s", "");
		}
		int suffix_index = -1;
		while (value >= SUFFIX_BASE) {
			value /= SUFFIX_BASE;
			++suffix_index;
		}
		return String.format("%" + (field_width - 1) + "d%s", value, suffix_index == -1 ? " " : SUFFIXES[suffix_index]);
	}
}
