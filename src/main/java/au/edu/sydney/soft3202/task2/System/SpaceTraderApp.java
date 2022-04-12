package au.edu.sydney.soft3202.task2.System;
import au.edu.sydney.soft3202.task2.MiniDB.MarketPlaceParser;
import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.SceneController.DefaultController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Emma LU
 * @create 2022-04-01-1:24 pm
 */
public class SpaceTraderApp extends Application{

    public static String parameters;
    public static String username;
    public static String token;
    public static UserParser userParser;
    public static MarketPlaceParser marketPlaceParser;
    public static String flightId;
    @Override
    public void start(Stage primaryStage) throws Exception {
        userParser = new UserParser(null,null);
        marketPlaceParser = new MarketPlaceParser();
        FXMLLoader defaultPage = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
        Parent root = defaultPage.load();
        DefaultController defaultController = defaultPage.getController();
        defaultController.setState(parameters);
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setTitle("SpaceTrader");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args){
        try{
            if (args.length == 1){
                parameters = args[0];
            }else{
                parameters = "online";
            }
            launch();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
