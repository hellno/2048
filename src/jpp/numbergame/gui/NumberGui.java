package jpp.numbergame.gui;

import java.util.List;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jpp.numbergame.*;

public class NumberGui extends Application {

    private NumberGame game;
    private GamePane gamePane;
    private Label pointsValue;
    private boolean gameIsLost = false;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage){
		primaryStage.setTitle("2048");
		
		gamePane = new GamePane(4, 4);
		
		BorderPane rootPane = new BorderPane();
		HBox topBar = new HBox();
		topBar.setStyle("-fx-background-color: palegoldenrod; -fx-font-size: 1.5em");
		
		Label pointsLabel = new Label("Points:");
		pointsValue = new Label("0");
		pointsValue.setStyle( "-fx-font-weight: bold; -fx-text-fill: darkred;");
		
		topBar.getChildren().add(pointsLabel);
		topBar.getChildren().add(pointsValue);
		
		rootPane.setTop(topBar);
		rootPane.setCenter(gamePane);
		
		primaryStage.setScene(new Scene(rootPane, 300, 300));
		primaryStage.show();
		primaryStage.setMinHeight(300);
		primaryStage.setMinWidth(300);
		
		//game stuff
		game = new NumberGame(4, 4);
		
		gamePane.addTile(game.addRandomTile());
		gamePane.addTile(game.addRandomTile());
		
		//listening to key events
		primaryStage.addEventFilter(KeyEvent.ANY, new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent ev) {
				if (ev.getEventType() != KeyEvent.KEY_PRESSED)
					return;

				switch (ev.getCode()) {
				case UP:
				case W:
					handleDirectionKey(Direction.UP);
					break;
				case DOWN:
				case S:
					handleDirectionKey(Direction.DOWN);
					break;
				case LEFT:
				case A:
					handleDirectionKey(Direction.LEFT);
					break;
				case RIGHT:
				case D:
					handleDirectionKey(Direction.RIGHT);
					break;
				case F5:
				case SPACE:
				case ENTER:
					restartGame();
					break;
				default:
				}
				ev.consume();
			}
		});

	}

	private void handleDirectionKey(Direction dir){
		if (gameIsLost)
			return;

		System.out.println("handling dir key " + dir);
		
		List<Move> moves = game.move(dir);

		if (!moves.isEmpty()) {
			gamePane.moveTiles(moves);
			pointsValue.setText(Integer.toString(game.getPoints()));
			Tile tile = game.addRandomTile();
			gamePane.addTile(tile);
		} else {
			gamePane.showFadingMessage("Move not possible");
		}
		if (!game.canMove()) {
			gameIsLost = true;
			gamePane.showMessage("Sorry, no more moves possible\nF5 to restart");
		}
		
		System.out.println("Nr. of tiles " + gamePane.getNumberOfTiles());
	}
	
	private void restartGame(){
		game = new NumberGame(300, 300);
		pointsValue.setText("0");
		gamePane.reset();
		
		gamePane.addTile(game.addRandomTile());
		gamePane.addTile(game.addRandomTile());
		
		gameIsLost = false;
	}
	
}
