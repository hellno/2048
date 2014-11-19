package jpp.numbergame;

public class TileExistsException extends RuntimeException {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public TileExistsException() {
		super();
	}

	public TileExistsException(String message) {
		super(message);
	}

	public TileExistsException(int x, int y) {
		super(String.format("Tile at (%d,%d) already exists", x, y));
	}
}
