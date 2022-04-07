package wholepackage;

/**
 * @author Emma LU
 * @create 2022-03-29-10:02 pm
 */


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class mainpage extends Application{

    public static void main(String[] args){
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea textArea = new TextArea();
        textArea.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button button = new Button("Register");
        button.setLayoutX(350);
        button.setLayoutY(300);


        //create a horizontal box
        HBox hbox = new HBox();
        hbox.setLayoutX(200);
        hbox.setLayoutY(200);
        Label label = new Label("Username");
        label.setFont(new Font(20));
        label.setAlignment(Pos.CENTER_LEFT);
        label.setPrefSize(100,50);
        TextField username = new TextField();
        username.setPrefSize(200,50);
        username.setEditable(true);
        username.setPromptText("Please create your username");
        username.setAlignment(Pos.CENTER_LEFT);
        hbox.getChildren().addAll(label,username);

        //create another horizontal box
        HBox hbox2 = new HBox();
        hbox2.setLayoutX(200);
        hbox2.setLayoutY(250);
        Label label2 = new Label("Password");
        label2.setFont(new Font(20));
        label2.setAlignment(Pos.CENTER_LEFT);
        label2.setPrefSize(100,50);
        TextField password = new TextField();
        password.setPrefSize(200,50);
        password.setEditable(true);
        password.setPromptText("Please create your username");
        password.setAlignment(Pos.CENTER_LEFT);
        hbox2.getChildren().addAll(label2,password);

        Pane pane = new Pane(button, hbox, hbox2);
        pane.setStyle("-fx-background-color: transparent;");
        pane.setPrefSize(200,160);
        Scene scene = new Scene(pane);
        scene.setFill(Color.web("#81c483"));
        primaryStage.setScene(scene);
        primaryStage.setTitle("JavaFX App");
        this.setPrimaryStageFeature(750,650, primaryStage);
        primaryStage.show();

    }

    public void setPrimaryStageFeature(double width, double height, Stage stage){
        stage.setHeight(height);
        stage.setWidth(width);
    }
}
