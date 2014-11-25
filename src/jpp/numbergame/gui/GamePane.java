package jpp.numbergame.gui;

import java.util.List;

import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jpp.numbergame.Move;
import jpp.numbergame.Tile;

public class GamePane extends StackPane {
	private TileGridPane canvas;
	private Text message;
	private FadeTransition ft;

	public GamePane(int width, int height) {
		canvas = new TileGridPane(width, height);
		message = new Text();
		message.setOpacity(0d);
		message.setFill(Color.DARKRED);
		message.setFont(new Font(20d));
		DropShadow ds = new DropShadow();
		ds.setOffsetX(4.0f);
		ds.setColor(Color.WHITE);
		message.setEffect(ds);
		ChangeListener ch = new ChangeListener() {

			public void changed(ObservableValue arg0, Object arg1, Object arg2) {
				// TODO Auto-generated method stub

			}
		};
		ft = new FadeTransition(Duration.millis(1000), message);
		ft.fromValueProperty().set(1d);
		ft.toValueProperty().set(0d);
		canvas.boundsInLocalProperty().addListener(ch);
		message.boundsInLocalProperty().addListener(ch);
	}

	public void addTile(Tile t) {
		canvas.addRectangle(t);
	}

	public void moveTiles(List<Move> moves) {
		canvas.moveRectangles(moves);
	}

	public void reset() {
		this.canvas.getChildren().clear();
		this.fadeOutMessage();
	}

	public void showFadingMessage(String text) {
		this.showMessage(text);
		this.fadeOutMessage();
	}

	public void showMessage(String text) {
		ft.stop();
		message.setText(text);
		message.setOpacity(1d);
	}

	public void fadeOutMessage() {
		ft.play();
	}
}
