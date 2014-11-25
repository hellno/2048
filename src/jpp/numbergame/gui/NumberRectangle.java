package jpp.numbergame.gui;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;

public class NumberRectangle extends StackPane {
	private javafx.scene.shape.Rectangle rec = new javafx.scene.shape.Rectangle();
	private javafx.beans.property.IntegerProperty inital;
	private javafx.scene.text.Text text;
	private javafx.beans.property.DoubleProperty xProperty;
	private javafx.beans.property.DoubleProperty yProperty;
	private javafx.beans.property.DoubleProperty width;
	private javafx.beans.property.DoubleProperty height;
	private Timeline timeline;
	private DoubleProperty recWidth;
	private DoubleProperty recHeight;

	public NumberRectangle(int x, int y, int initialValue) {
		this.xProperty.set(x);
		this.yProperty.set(y);
		this.inital.set(initialValue);
		Insets ins = new Insets(5, 5, 5, 5);
		this.setPadding(ins);
		this.rec.setArcHeight(10);
		this.rec.setArcWidth(10);
		this.text.setBoundsType(TextBoundsType.VISUAL);
		this.text.setFill(Color.WHITE);
		this.text.setFont(Font.font("Verdana", 20.0));
		InnerShadow is = new InnerShadow();
		is.setOffsetX(4.0f);
		is.setOffsetY(4.0f);
		is.setColor(Color.BLACK);
		this.text.setEffect(is);
		this.inital.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> o,
					Number oldVal, Number newVal) {
				text.setText(newVal.toString());
			}
		});
		this.inital.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0,
					Number oldVal, Number newVal) {
				FillTransition ft = new FillTransition(Duration.millis(150),
						rec);
				double interpol = Math.log(newVal.doubleValue())
						/ Math.log(2048);
				if (interpol > 1) {
					interpol = 1;
				}
				ft.setToValue(Color.PALEGOLDENROD.interpolate(
						Color.PALEGOLDENROD, interpol));
			}
		});
		this.text.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0,
					String oldVal, String newVal) {
				double scale = rec.boundsInLocalProperty().get().getWidth()
						/ text.boundsInLocalProperty().get().getWidth();
				text.setX(text.getX() * scale);
				text.setY(text.getY() * scale);
			}
		});
		this.rec.boundsInLocalProperty().addListener(
				new ChangeListener<Bounds>() {
					@Override
					public void changed(ObservableValue<? extends Bounds> arg0,
							Bounds oldVal, Bounds newVal) {
						Bounds tb = text.getBoundsInLocal();
						double scale = newVal.getWidth() / tb.getWidth();
						xProperty.set(xProperty.doubleValue() * scale);
						yProperty.set(yProperty.doubleValue() * scale);
					}
				});
		this.rec.setHeight(height.doubleValue() - 2 * 5);
		this.rec.setWidth(width.doubleValue() - 2 * 5);
	}

	public void moveTo(int x, int y) {
		List<KeyFrame> kflist = new ArrayList<KeyFrame>();
		KeyValue xk = new KeyValue(this.xProperty, x);
		KeyValue yk = new KeyValue(this.yProperty, y);
		KeyFrame kf = new KeyFrame(Duration.millis(150), xk, yk);
		for (KeyFrame kft : timeline.getKeyFrames()) {
			kflist.add(kft);
		}
		kflist.clear();
		timeline.getKeyFrames().add(kf);
		timeline.playFromStart();
	}

	public DoubleProperty rectWidthProperty() {
		recWidth.set(rec.getWidth());
		return recWidth;

	}

	public DoubleProperty rectHeightProperty() {
		recHeight.set(rec.getHeight());
		return recHeight;
	}

	public int getValue() {
		return inital.get();
	}

	public double getY() {
		return yProperty.doubleValue();

	}

	public double getX() {
		return xProperty.doubleValue();
	}

	public void setY(double newY) {
		this.yProperty.set(newY);
	}

	public void setX(double newX) {
		this.xProperty.set(newX);
	}

	public DoubleProperty yProperty() {
		return yProperty;
	}

	public DoubleProperty xProperty() {
		return xProperty;
	}
}
