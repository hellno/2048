package jpp.numbergame.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jpp.numbergame.Direction;
import jpp.numbergame.NumberGame;

public class NumberGui extends Application {

    private NumberGame game;
    private GamePane gamePane;
    private Label pointsValue;
    private boolean gameIsLost = false;
	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public void start(Stage primaryStage){
		/*
		 *  Add a new Eventfilter to the primary stage that works on KeyEvent.ANY and passes to a new EventHandler<KeyEvent that implements the following functionality in its handle(KeyEvent ev) function:
		        if the event type is not KeyEvent.KEY_PRESSED
		        else get the keycode and handle the cases in the following way
		            UP: call handleDirectionKey(Direction.UP);
		            DOWN: call handleDirectionKey(Direction.DOWN);
		            LEFT: call handleDirectionKey(Direction.LEFT);
		            RIGHT: call handleDirectionKey(Direction.RIGHT);
		            F5: restart the game
		        after the handling consume the event
		 */
		
		//java fx shit
		primaryStage.setTitle("2048");
		
		gamePane = new GamePane(4, 4);
		
		BorderPane rootPane = new BorderPane();
		HBox topBar = new HBox();
		topBar.setStyle("-fx-background-color: palegoldenrod; -fx-font-size: 1.5em");
		
		Label pointsLabel = new Label("Points:");
		Label pointsValueLabel = new Label("0");
		pointsValueLabel.setStyle( "-fx-font-weight: bold; -fx-text-fill: darkred;");
		topBar.getChildren().add(pointsLabel);
		topBar.getChildren().add(pointsValueLabel);
		
		rootPane.setTop(topBar);
		rootPane.setCenter(gamePane);
		
		primaryStage.setScene(new Scene(rootPane, 300, 300));
		primaryStage.setMinHeight(300);
		primaryStage.setMinWidth(300);
		primaryStage.show();
		
		System.out.println("showing scene " + primaryStage);
		
		//game stuff
		game = new NumberGame(4, 4);
		game.addRandomTile();
		game.addRandomTile();
		
	}

	private void handleDirectionKey(Direction dir){
		
	}
	
	private void restartGame(){
		
	}
	
}
