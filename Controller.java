package sample;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable {

	private static final int COLUMNS = 7;
	private static final int ROWS = 6;
	private static final int CIRCLE_DIAMETER = 80;
	private static final String disc1 = "#24303E";
	private static final String disc2 = "#4CAA88";

	private static String player_one = "Player One";
	private static String player_two = "Player Two";

	private boolean isPlayerOneturn = true;
	private Disc[][] insertedDiscsArray= new Disc[ROWS][COLUMNS];

	@FXML
	public GridPane rootGridPane;

	@FXML
	public Pane insertedDiscsPane;

	public Pane menuPane;

	public Label player;

	@FXML
	public Label turn;

	@FXML
	public TextField player1;

	@FXML
	public TextField player2;

	@FXML
	public Button btnname;

	private boolean isallowedtoinsert=true;
	public void createPlayground() {
		Shape rectangle = creategrid();
		rootGridPane.add(rectangle, 0, 1);
		 List<Rectangle> rectangleList = createClickcol();
		for (Rectangle rect:rectangleList) {
			rootGridPane.add(rect,0,1);
		}
		btnname.setOnAction(event -> {
			player_one= player1.getText() + "'s";
			player_two=player2.getText() + "'s";
	});
	}
	private Shape creategrid() {
		Shape rectangle = new Rectangle((COLUMNS + 1) * CIRCLE_DIAMETER, (ROWS + 1) * CIRCLE_DIAMETER);

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLUMNS; col++) {
				Circle cirle = new Circle();
				cirle.setRadius(CIRCLE_DIAMETER / 2);
				cirle.setCenterX(CIRCLE_DIAMETER / 2);
				cirle.setCenterY(CIRCLE_DIAMETER / 2);
                cirle.setSmooth(true);
				cirle.setTranslateX(col * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER / 4));
				cirle.setTranslateY(row * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER / 4));
				rectangle = Shape.subtract(rectangle, cirle);
			}
		}
		rectangle.setFill(Color.WHITE);
		return rectangle;
	}
	private List<Rectangle> createClickcol() {



		List<Rectangle> rectList = new ArrayList<>();
		for (int col = 0; col < COLUMNS; col++) {
			Rectangle rect = new Rectangle((CIRCLE_DIAMETER + 5), (ROWS + 1) * CIRCLE_DIAMETER);
			rect.setFill(Color.TRANSPARENT);
			rect.setTranslateX(col * (CIRCLE_DIAMETER + 5) + (CIRCLE_DIAMETER / 4));

			rect.setOnMouseEntered(event -> rect.setFill(Color.valueOf("#eeeeee26")));
			rect.setOnMouseExited(event -> rect.setFill(Color.TRANSPARENT));
			final int column = col;
			rect.setOnMouseClicked(event -> {
				if (isallowedtoinsert) {
					isallowedtoinsert= false;
					insertDisc(new Disc(isPlayerOneturn), column);
				}});
			rectList.add(rect);
		}
		return rectList;
	}

	private void insertDisc(Disc disc, int column) {
		int row =ROWS-1;
		while(row>=0) {
			if (insertedDiscsArray[row][column] == null)
				break;
			row--;
		}if (row<0)
			return;
		insertedDiscsArray[row][column] = disc;
		insertedDiscsPane.getChildren().add(disc);
        disc.setTranslateX( (column * (CIRCLE_DIAMETER+5))+CIRCLE_DIAMETER/4 );
		TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.5),disc);
		translateTransition.setToY(((row)*(CIRCLE_DIAMETER+5))+ CIRCLE_DIAMETER/4);
		int finalRow = row;
		translateTransition.setOnFinished(event -> {
			isallowedtoinsert = true;
			if(gameEnded(finalRow, column)){
       gameOver();
       return;
			}

			isPlayerOneturn= !isPlayerOneturn;
			player.setText(isPlayerOneturn? player_one:player_two);
		});

		translateTransition.play();

	}

	private boolean gameEnded(int row , int column){
	List<Point2D> verticalpoints=IntStream.rangeClosed(row -3,row +3)
			.mapToObj(r ->new Point2D(r,column))
			.collect(Collectors.toList());
		List<Point2D> horizontalpoints=IntStream.rangeClosed(column -3,column +3)
				.mapToObj(col ->new javafx.geometry.Point2D(row ,col))
				.collect(Collectors.toList());

		Point2D startpoint1 = new Point2D(row-3,column+3);
		List<Point2D> diagonal1points = IntStream.rangeClosed(0,6)
				.mapToObj(i ->  startpoint1.add(i,-i))
				.collect(Collectors.toList());
		Point2D startpoint2 = new Point2D(row-3,column-3);
		List<Point2D> diagonal2points = IntStream.rangeClosed(0,6)
				.mapToObj(i ->  startpoint2.add(i,i))
				.collect(Collectors.toList());

        boolean isEnded = checkCombinations(verticalpoints)
		        || checkCombinations(horizontalpoints)
		        || checkCombinations(diagonal1points)
		        || checkCombinations(diagonal2points);
		return isEnded;
	}

	private boolean checkCombinations(List<Point2D> points) {
		int chain =0;
		for (Point2D point:points) {
			int rowIndexForArray = (int) point.getX();
			int colIndexForArray = (int) point.getY();
			Disc disc= getDiscifPresent(rowIndexForArray,colIndexForArray);
           if (disc != null && disc.isplayerOneMove == isPlayerOneturn){
           	chain++;
           	if (chain == 4) return true;
           }else chain=0;
		}
		return false;
	}

    private Disc getDiscifPresent (int row,int column){

		if(row>=ROWS || row<0 || column>=COLUMNS || column<0 ){
	      return null;
		}
      return insertedDiscsArray[row][column];
	}

    private void gameOver() {
	    String winner = isPlayerOneturn ? player1.getText() : player2.getText();
	    System.out.println("Winner is :" + winner);
	    Alert alert = new Alert(Alert.AlertType.INFORMATION);
	    alert.setTitle("Connect 4");
	    alert.setHeaderText("Winner is : " + winner);
	    alert.setContentText("Do you want to play again?");
	    ButtonType yesbtn = new ButtonType("Yes");
	    ButtonType nobtn = new ButtonType("No,Exit");
	    alert.getButtonTypes().setAll(yesbtn, nobtn);
        Platform.runLater(()->{
		        Optional < ButtonType > btnclicked = alert.showAndWait();
	    if (btnclicked.isPresent() && btnclicked.get() == yesbtn) {
		    resetGame();
	    } else {
		    Platform.exit();
		    System.exit(0);
	    }
        });


	}

	public void resetGame() {
     insertedDiscsPane.getChildren().clear();
     for (int row =0; row<insertedDiscsArray.length; row++){
     	for (int column =0; column < insertedDiscsArray[row].length;column++){
             insertedDiscsArray[row][column] = null;
        }
     }
     isPlayerOneturn=true;
     createPlayground();
     player.setText(player_one);
	 player_one = "Player One";
	 player_two = "Player Two";
	}




	private static class Disc extends Circle{
		private final boolean isplayerOneMove;
		public Disc(boolean isplayerOneMove){
			this.isplayerOneMove=isplayerOneMove;
			setRadius((CIRCLE_DIAMETER/2));
			setFill(isplayerOneMove? Color.valueOf(disc1):Color.valueOf(disc2));
			setCenterX(CIRCLE_DIAMETER/2);
			setCenterY(CIRCLE_DIAMETER/2);
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
