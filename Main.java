package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {


	private Controller controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
		GridPane rootGridPane = loader.load();
		controller = loader.getController();
		controller.createPlayground();
		Pane menuPane = (Pane) rootGridPane.getChildren().get(0);

		MenuBar menuBar = createMenu();
		menuPane.getChildren().add(menuBar);
		menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

		Scene scene = new Scene(rootGridPane);

		primaryStage.setScene(scene);
		primaryStage.setTitle("Connect Four");
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	private MenuBar createMenu() {
		//menu
		Menu fileMenu = new Menu("File");
		MenuItem newgameMenu = new MenuItem("New Game");
		newgameMenu.setOnAction(event ->controller.resetGame());
		MenuItem resetgameMenu = new MenuItem("Reset Game");
		resetgameMenu.setOnAction(event -> {
			controller.resetGame();
		});
		SeparatorMenuItem sp = new SeparatorMenuItem();
		MenuItem exitgamemenu = new MenuItem("Exit Game");
		exitgamemenu.setOnAction(event -> {
			exitgame();
			Platform.exit();
			System.exit(0);
		});


		fileMenu.getItems().addAll(newgameMenu, resetgameMenu, sp, exitgamemenu);

		Menu helpmenu = new Menu("Help");
		MenuItem aboutapp = new MenuItem("About Connect 4");
		aboutapp.setOnAction(event -> {
			aboutc4();

		});
		SeparatorMenuItem s = new SeparatorMenuItem();
		MenuItem aboutme = new MenuItem("About Me");
        aboutme.setOnAction(event -> { aboutMe();} );
		helpmenu.getItems().addAll(aboutapp, s, aboutme);

		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, helpmenu);
		return menuBar;
	}

    private void aboutMe() {
        Alert alertt = new Alert(Alert.AlertType.INFORMATION);
        alertt.setTitle("About The Developer");
        alertt.setHeaderText("Deep Gandhi");
        alertt.setContentText("I love making games");
        alertt.show();
    }

    private void aboutc4() {
		Alert alertt = new Alert(Alert.AlertType.INFORMATION);
		alertt.setTitle("About Connect Four");
		alertt.setHeaderText("How To Play?");
		alertt.setContentText("Connect Four is a two-player connection game in " +
                "which the players first choose a color and then take turns" +
                " dropping colored discs from the top into a seven-column," +
                " six-row vertically suspended grid. The pieces fall straight down," +
                " occupying the next available space within the column." +
                " The objective of the game is to be the first to form a horizontal," +
                " vertical, or diagonal line of four of one's own discs. " +
                "Connect Four is a solved game. " +
                "The first player can always win by playing the right moves.");
	    alertt.show();
	}

	private void exitgame() {
	}

	private void resetgame() {
		
	}

	public static void main(String[] args) {
		launch(args);
	}
}
