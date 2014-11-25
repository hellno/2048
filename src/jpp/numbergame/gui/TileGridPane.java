package jpp.numbergame.gui;

import java.util.List;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jpp.numbergame.Coordinate2D;
import jpp.numbergame.Move;
import jpp.numbergame.Tile;

public class TileGridPane extends Pane {

	private int width;
	private int height;
	private Tile[][] tList;
	private DoubleBinding db2;

	public TileGridPane(int width, int height) {
		this.width = width;
		this.height = height;
		Tile[][] tList =  new Tile[this.width][this.height];
		final DoubleBinding dbw = tileWidthBinding();
		final DoubleBinding dbh = tileHeightBinding();
		for (int i = 0; i < this.width; i++) {
			Line ln = new Line();
			final int b = i;
			ln.setFill(Color.GRAY);
			ln.strokeWidthProperty().bind(dbw);
			ln.setStartY(0);
			ln.endYProperty().bind(this.heightProperty());
			db2 = new DoubleBinding() {
				@Override
				protected double computeValue() {
					return dbw.get() * b;
				}
			};
			ln.startXProperty().bind(db2);
			ln.endXProperty().bind(db2);
			this.getChildren().add(ln);
		}
		for (int i = 0; i < this.height; i++) {
			Line ln = new Line();
			final int b = i;
			ln.setFill(Color.GRAY);
			ln.strokeWidthProperty().bind(dbw);
			ln.setStartX(0);
			ln.endXProperty().bind(this.widthProperty());
			db2 = new DoubleBinding() {
				@Override
				protected double computeValue() {
					return dbh.get() * b;
				}
			};
			ln.startYProperty().bind(db2);
			ln.endYProperty().bind(db2);
			this.getChildren().add(ln);
		}
	}

	public void addRectangle(Tile tile) {
		this.addRectangle(tile.getValue(), tile.getCoordinate().getX(), tile
				.getCoordinate().getY());
	}

	public void addRectangle(int value, int x, int y) {
		if (this.tList[x][y] != null) {
			throw new IllegalArgumentException();
		}
		NumberRectangle ner = new NumberRectangle(x, y, value);
		this.getChildren().add(ner);
		Tile t = new Tile(new Coordinate2D(x, y), value);
		this.tList[x][y] = t;
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
		int x = move.getFrom().getX();
		int y = move.getFrom().getY();
		Coordinate2D dest = move.getTo();
		Tile t = this.tList[x][y];
		this.tList[x][y] = null;
		this.tList[dest.getX()][dest.getY()] = t;
	}
}
