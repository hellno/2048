package jpp.numbergame.gui;

import java.util.List;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;


public class NumberRectangle extends StackPane {
	
	private Rectangle rect = new Rectangle();
	private IntegerProperty rectValue = new SimpleIntegerProperty();
	private Text text = new Text();

	private DoubleProperty width = new SimpleDoubleProperty();
	private DoubleProperty height = new SimpleDoubleProperty();

	private Timeline timeline = new Timeline();
	
	private DoubleProperty x = new SimpleDoubleProperty();
	private DoubleProperty y = new SimpleDoubleProperty();

	public static final DropShadow shadow = new DropShadow();
	
	static {
		shadow.setOffsetY(4.0f);
		shadow.setColor(Color.BLACK);
	}

	public NumberRectangle(int x, int y, int initialValue) {

		setPadding(new Insets(5, 5, 5, 5));
		rect.setArcWidth(10);
		rect.setArcHeight(10);

		text.setBoundsType(TextBoundsType.VISUAL);
		text.setFill(Color.WHITE);
		text.setFont(new Font(20.0));
		text.setEffect(shadow);
	
		ChangeListener<Number> ch1 = new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				text.setText(newValue.toString());
			}
		};
		rectValue.addListener(ch1);

		ChangeListener<Number> ch2 = new ChangeListener<Number>() {
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				FillTransition ft = new FillTransition(Duration.millis(150), rect);
				ft.setToValue(calcColor(newValue.doubleValue()));
				ft.play();
			}
		};
		
		rectValue.addListener(ch2);

		ChangeListener<Bounds> ch3 = new ChangeListener<Bounds>() {
			public void changed(ObservableValue<? extends Bounds> target, Bounds oldValue, Bounds newValue) {
				Bounds tb = text.getBoundsInLocal();
				double scalex = newValue.getWidth() / tb.getWidth();
				text.setScaleX(scalex);
				text.setScaleY(scalex);
			}
		};
		
		rect.boundsInLocalProperty().addListener(ch3);
		
		ChangeListener<String> ch4 = new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> target, String oldValue, String newValue) {
				Bounds rb = rect.getBoundsInLocal();
				Bounds tb = text.getBoundsInLocal();
				double scalex = rb.getWidth() / tb.getWidth();
				text.setScaleX(scalex);
				text.setScaleY(scalex);
			}
		};
		
		text.textProperty().addListener(ch4);

		rectValue.set(initialValue);
		this.x.set(x);
		this.y.set(y);
		
		rect.widthProperty().bind(width.subtract(5 * 2));
		rect.heightProperty().bind(height.subtract(5 * 2));

		getChildren().addAll(new Node[] { rect, text });
	}

	public void moveTo(int x, int y) {
		final KeyValue keyValueX = new KeyValue(this.xProperty(), x);
		final KeyValue keyValueY = new KeyValue(this.yProperty(), y);
		final KeyFrame keyFrame = new KeyFrame(Duration.millis(150), keyValueX, keyValueY);

		List<KeyFrame> frames = timeline.getKeyFrames();
		frames.clear();
		frames.add(keyFrame);
		timeline.playFromStart();
	}

	public DoubleProperty rectWidthProperty() {
		return width;
	}

	public DoubleProperty rectHeightProperty() {
		return height;
	}

	private Color calcColor(double value) {
		double ratio = Math.log(value) / Math.log(2048);
		return Color.PALEGOLDENROD.interpolate(Color.DARKRED, ratio > 1d ? 1d : ratio);
	}

	public int getValue() {
		return rectValue.get();
	}

	public void setValue(int newValue) {
		rectValue.set(newValue);
	}

	public IntegerProperty valueProperty() {
		return rectValue;
	}
	
	public double getX() {
		return x.get();
	}

	public void setX(double newX) {
		x.set(newX);
	}

	public DoubleProperty xProperty() {
		return x;
	}

	public double getY() {
		return y.get();
	}

	public void setY(double newY) {
		y.set(newY);
	}

	public DoubleProperty yProperty() {
		return y;
	}

}
