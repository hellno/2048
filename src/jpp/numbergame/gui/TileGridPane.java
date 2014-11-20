package jpp.numbergame.gui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import jpp.numbergame.Tile;

public class TileGridPane extends Pane {

	private int width;
	private int height;
	private Tile[][] tList;
	private DoubleBinding db;
	private DoubleBinding db2;

	public TileGridPane(int width, int height) {
		this.width = width;
		this.height = height;		
		Tile[][] tList = new Tile[this.width][this.height];
		final ReadOnlyDoubleProperty widthProp = this.widthProperty();
		db = new DoubleBinding() {
			@Override
			protected double computeValue() {
				return (widthProp.doubleValue() / 400) + 1;
			}
		};
		for (int i = 0; i < this.width; i++) {
			Line ln = new Line();
			ln.setFill(Color.GRAY);
			ln.strokeWidthProperty().bind(db);
			ln.setStartY(0);
			ln.endYProperty().bind(this.heightProperty());
			db2 = new DoubleBinding() {
				
				@Override
				protected double computeValue() {
					return 0;
				}
			};
		}
	}

}
