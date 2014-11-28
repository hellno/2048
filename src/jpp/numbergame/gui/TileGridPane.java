package jpp.numbergame.gui;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import jpp.numbergame.*;

public class TileGridPane extends Pane {

	private int width;
	private int height;

	private NumberRectangle[][] tiles;

	public TileGridPane(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new NumberRectangle[width][height];
		
		DoubleBinding strokeWidth = widthProperty().divide(400).add(1);
		
		for(int x = 1; x < width; x++) {
			Line gridline = new Line();
			gridline.setStroke(Color.GRAY);
			gridline.strokeWidthProperty().bind(strokeWidth);

			gridline.setStartY(0);
			gridline.endYProperty().bind(heightProperty());
			
			DoubleBinding xbind = tileWidthBinding().multiply(x);
			gridline.startXProperty().bind(xbind);
			gridline.endXProperty().bind(xbind);
			getChildren().add(gridline);
		}

		for(int y = 1; y < width; y++) {
			Line gridline = new Line();
			gridline.setStroke(Color.GRAY);
			gridline.strokeWidthProperty().bind(widthProperty().divide(400).add(1));

			gridline.setStartX(0);
			gridline.endXProperty().bind(widthProperty());
			
			DoubleBinding ybind = tileHeightBinding().multiply(y);
			gridline.startYProperty().bind(ybind);
			gridline.endYProperty().bind(ybind);
			getChildren().add(gridline);
		}
	}

	public void addRectangle(Tile t) {
		addRectangle(t.getValue(), t.getCoordinate().getX(), t.getCoordinate().getY());
	}

	public void addRectangle(int v, int x, int y) {
		if (tiles[x][y] != null) {
			throw new IllegalArgumentException(String.format(
					"Tile at (%d,%d) already exists", x, y));
		}

		NumberRectangle rect = new NumberRectangle(x, y, v);

		rect.rectWidthProperty().bind(tileWidthBinding());
		rect.rectHeightProperty().bind(tileHeightBinding());

		rect.layoutXProperty().bind(tileWidthBinding().multiply(rect.xProperty()));
		rect.layoutYProperty().bind(tileHeightBinding().multiply(rect.yProperty()));

		getChildren().add(rect);
		tiles[x][y] = rect;
	}

	public void moveRectangles(List<Move> moves) {
		for (Move move : moves) {
			moveRectangle(move);
		}
	}

	public void moveRectangle(Move move) {
		NumberRectangle movedRect = tiles[move.getFrom().getX()][move.getFrom().getY()];
		movedRect.moveTo(move.getTo().getX(), move.getTo().getY());

		if (move.isMerge()) {
			//move tiles around
			NumberRectangle tempRect = tiles[move.getTo().getX()][move.getTo().getY()];
			FadeTransition fadeTransition = new FadeTransition(Duration.millis(150), tempRect);
			fadeTransition.setFromValue(1d);
			fadeTransition.setToValue(0d);
			fadeTransition.play();
			getChildren().remove(tempRect);
			
			movedRect.setValue(move.getNewValue());
		}
		tiles[move.getTo().getX()][move.getTo().getY()] = movedRect;
		tiles[move.getFrom().getX()][move.getFrom().getY()] = null;
	}

	public DoubleBinding tileWidthBinding() {
		return widthProperty().divide(width);
	}

	public DoubleBinding tileHeightBinding() {
		return heightProperty().divide(height);
	}

	public void reset() {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[x].length; y++) {
				getChildren().remove(tiles[x][y]);
				tiles[x][y] = null;
			}
		}
	}
}
