package jpp.numbergame.gui;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import jpp.numbergame.Coordinate2D;
import jpp.numbergame.Move;
import jpp.numbergame.Tile;

public class TileGridPane extends Pane {

	private int width;
	private int height;
	private NumberRectangle[][] tList;
	private DoubleBinding db2;

	public TileGridPane(int width, int height) {
		
		this.width = width;
		this.height = height;
		
		tList = new NumberRectangle[width][height];
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

	public void addRectangle(Tile tile) {
		this.addRectangle(tile.getValue(), tile.getCoordinate().getX(), tile
				.getCoordinate().getY());
	}

	public void addRectangle(int value, int x, int y) {
		
		if (this.tList[x][y] != null) {
			throw new IllegalArgumentException(String.format(
					"tile %d,%d already exists", x, y));
		}
		
		NumberRectangle ner = new NumberRectangle(x, y, value);
		
		this.getChildren().add(ner);
		this.tList[x][y] = ner;
		
		
	}

	public DoubleBinding tileWidthBinding() {
		final ReadOnlyDoubleProperty widthProp = this.widthProperty();
		final int xvalue = this.width;
		DoubleBinding db = new DoubleBinding() {
			@Override
			protected double computeValue() {
				return widthProp.doubleValue() / xvalue;
			}
		};
		return db;
	}

	public DoubleBinding tileHeightBinding() {
		final ReadOnlyDoubleProperty heightProp = this.heightProperty();
		final int yvalue = this.height;
		DoubleBinding db = new DoubleBinding() {
			@Override
			protected double computeValue() {
				return heightProp.doubleValue() / yvalue;
			}
		};
		return db;
	}

	public void moveRectangles(List<Move> moves) {
		for (Move m : moves) {
			moveRectangle(m);
		}
	}

	public void moveRectangle(Move move) {
		
		NumberRectangle movedRect = tList[move.getFrom().getX()][move.getFrom().getY()];
		movedRect.moveTo(move.getTo().getX(), move.getTo().getY());

		if (move.isMerge()) {
			NumberRectangle oldRect = tList[move.getTo().getX()][move.getTo().getY()];
			FadeTransition ft = new FadeTransition(Duration.millis(150), oldRect);
			ft.setFromValue(1d);
			ft.setToValue(0d);
			ft.play();
			getChildren().remove(oldRect);
			movedRect.setValue(move.getNewValue());
			
			System.out.println("merging two tiles");
		}
		
		tList[move.getTo().getX()][move.getTo().getY()] = movedRect;
		tList[move.getFrom().getX()][move.getFrom().getY()] = null;
	}
	
	public void reset() {
		for(int x = 0; x < tList.length; x++) {
			for(int y = 0; y < tList[x].length; y++) {
				getChildren().remove(tList[x][y]);
				tList[x][y] = null;
			}
		}
	}
}
