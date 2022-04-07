package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.security.auth.callback.ChoiceCallback;
import java.util.List;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-03-6:19 pm
 */
public class AvailableShipController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private Label ships;

    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button) {
            case "Button[id=cancel, styleClass=button]'Cancel'":
            case "Button[id=back, styleClass=button]'Back'":
                if (parameters.equals("offline")){
                    FXMLLoader accountInfoLoader = new FXMLLoader(getClass().getResource("/AllPages/AccountInfo.fxml"));
                    Parent accountInfoRoot = accountInfoLoader.load();
                    AccountInfoController accountInfoController = accountInfoLoader.getController();
                    accountInfoController.setState(parameters);
                    accountInfoController.setDetailsOffline(readFakeInfoFile() );
                    Scene accountInfoScene = new Scene(accountInfoRoot);
                    Stage accountInfoStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    accountInfoStage.setScene(accountInfoScene);
                    accountInfoStage.show();

                }else if(parameters.equals("online")){
                    System.out.println("hi");
                    try{
                        String addingToken = "token=" + Game.token;
                        String uri = "https://api.spacetraders.io/my/account?" + addingToken;
                        HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                                .GET()
                                .build();

                        HttpClient client = HttpClient.newBuilder().build();
                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        String responseBody = response.body();
                        int statusCode = response.statusCode();
                        JsonObject userPost = getUserPost(responseBody);
                        String gettingUsername = userPost.get("user").getAsJsonObject().get("username").getAsString();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                        Parent loginSuccessRoot = loader.load();
                        LoginSuccessController loginSuccessController = loader.getController();
                        loginSuccessController.setUsername(gettingUsername);
                        loginSuccessController.setToken(Game.token);
                        loginSuccessController.setGreeting(gettingUsername);
                        loginSuccessController.setState(parameters);
                        Scene loginSuccessScene = new Scene(loginSuccessRoot);
                        Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        loginSuccessStage.setScene(loginSuccessScene);
                        loginSuccessStage.show();
                    }catch(Exception exception){
                        exception.printStackTrace();
                    }
                }

                break;
            case "Button[id=home, styleClass=button]'Home'":
                FXMLLoader defaultLoader = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
                Parent defaultRoot = defaultLoader.load();
                DefaultController defaultController = defaultLoader.getController();
                defaultController.setState(parameters);
                Scene defaultScene = new Scene(defaultRoot);
                Stage defaultStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                defaultStage.setScene(defaultScene);
                defaultStage.show();
                break;
            case "Button[id=purchaseNewShips, styleClass=button]'Purchase a new ship'":
                if(parameters.equals("offline")){
                    FXMLLoader purchaseShipOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseNewShipOnline.fxml"));
                    Parent purchaseShipOnlineRoot = purchaseShipOnlineLoader.load();
                    PurchaseNewShipController purchaseNewShipController = purchaseShipOnlineLoader.getController();
                    purchaseNewShipController.setState(parameters);
                    Scene purchaseShipScene = new Scene(purchaseShipOnlineRoot);
                    Stage purchaseShipStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    purchaseShipStage.setScene(purchaseShipScene);
                    purchaseShipStage.show();
                }else if(parameters.equals("online")){
                    FXMLLoader purchaseShipOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseNewShipOnline.fxml"));
                    Parent purchaseShipOnlineRoot = purchaseShipOnlineLoader.load();
                    PurchaseNewShipController purchaseNewShipController = purchaseShipOnlineLoader.getController();
                    purchaseNewShipController.setState(parameters);
                    purchaseNewShipController.setSystemChoices();
                    Scene purchaseShipScene = new Scene(purchaseShipOnlineRoot);
                    Stage purchaseShipStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    purchaseShipStage.setScene(purchaseShipScene);
                    purchaseShipStage.show();
                }

                break;
            case "Button[id=myShips, styleClass=button]'View my ships'":

                if (parameters.equals("offline")){
                    FXMLLoader myShipLoader = new FXMLLoader(getClass().getResource("/AllPages/YourAvailableShips.fxml"));
                    Parent myShipRoot = myShipLoader.load();
                    YourAvailableShipsController yourAvailableShipsController = myShipLoader.getController();
                    yourAvailableShipsController.setState(parameters);
                    UserParser userParser = new UserParser(Game.username, Game.token);
                    yourAvailableShipsController.setMyAvailableShips(userParser);
                    Scene myShipScene = new Scene(myShipRoot);
                    Stage myShipStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    myShipStage.setScene(myShipScene);
                    myShipStage.show();
                }else if(parameters.equals("online")){
                    if (checkStatusCode(getAvailableShipsFromAPI()).equals("yes")){
                        FXMLLoader myShipLoader = new FXMLLoader(getClass().getResource("/AllPages/YourAvailableShips.fxml"));
                        Parent myShipRoot = myShipLoader.load();
                        YourAvailableShipsController yourAvailableShipsController = myShipLoader.getController();
                        yourAvailableShipsController.setState(parameters);
                        yourAvailableShipsController.setYourAvailableShipsOnline(getAvailableShipsFromAPI());
                        Scene myShipScene = new Scene(myShipRoot);
                        Stage myShipStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        myShipStage.setScene(myShipScene);
                        myShipStage.show();
                        break;
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(checkStatusCode(getAvailableShipsFromAPI()));
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
                        break;
                    }
                }
                break;

        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.parameters = Game.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public void setAvailableShipDetails(UserParser user) throws IOException, ParseException {
        JSONObject ships = user.getAvailableShips();
        String newShipsDetails = "\n";
        JSONArray shipArray = (JSONArray) ships.get("shipListings");
        for (int i = 0; i < shipArray.size(); i++){
            JSONObject shipObject = (JSONObject) shipArray.get(i);
            newShipsDetails = newShipsDetails + "The " + (i+1) + "th Ship: \n\n";
            newShipsDetails = newShipsDetails + "ID: " + shipObject.get("ID") + "\n";
            newShipsDetails = newShipsDetails + "Class: " + shipObject.get("class") + "\n";
            newShipsDetails = newShipsDetails + "Manufacturer: " + shipObject.get("manufacturer") + "\n";
            newShipsDetails = newShipsDetails + "MaxCargo: " + shipObject.get("maxCargo") + "\n";
            newShipsDetails = newShipsDetails + "Plating: " + shipObject.get("plating") + "\n";
            newShipsDetails = newShipsDetails + "Speed: " + shipObject.get("speed") + "\n";
            newShipsDetails = newShipsDetails + "Type: " + shipObject.get("type") + "\n";
            newShipsDetails = newShipsDetails + "Weapons: " + shipObject.get("weapons") + "\n\n\n";
        }
        this.ships.setText(newShipsDetails);
    }

    public void setAvailableShipDetailsOnline(JsonObject ships){
        JsonArray shipsArray = ships.get("ships").getAsJsonArray();
        if (shipsArray == null || shipsArray.size() == 0){
            this.ships.setText("No available ships");
        }else{
            String shipDetails = "";
            for (int i = 0; i < shipsArray.size(); i++){
                JsonObject shipObject = shipsArray.get(i).getAsJsonObject();
                String types = shipObject.get("type").getAsString();
                String shipClass = shipObject.get("class").getAsString();
                Long maxCargo = shipObject.get("maxCargo").getAsLong();
                Long loadingSpeed = shipObject.get("loadingSpeed").getAsLong();
                Long speed = shipObject.get("speed").getAsLong();
                String manufacturer = shipObject.get("manufacturer").getAsString();
                Long plating = shipObject.get("plating").getAsLong();
                Long weapons = shipObject.get("weapons").getAsLong();
                String shipDetail = "Ship type is: " + types + "\nShip class is: " + shipClass +
                        "\nThe maximum Cargo is: " + maxCargo + "\nLoading Speed is: " + loadingSpeed +
                        "\nSpeed is: " + speed + "\nManufacturer is: " + manufacturer +
                        "\nPlating is: " + plating + "\nWeapons Number is: " + weapons + "\n\n";
                shipDetails += shipDetail;
            }
            this.ships.setText(shipDetails);
        }
    }

    public HttpResponse<String> getAvailableShipsFromAPI(){
        try{
            String addingToken = "token=" + Game.token;
            String uri = "https://api.spacetraders.io/my/ships?" + addingToken;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response;
        }catch(Exception e){
            return null;
        }
    }

    public String checkStatusCode(HttpResponse<String> response){
        int statusCode = response.statusCode();
        String responseBody = response.body();
        JsonObject userPost = getUserPost(responseBody);
        if (statusCode != 200){
            return userPost.get("error").getAsJsonObject().get("message").getAsString();
        }else{
            return "yes";
        }

    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setAvailableShipsOffline(String str){
        this.ships.setText(str);
    }

    public String readFakeInfoFile(){
        String reading_string = "";
        try {
            File myObj = new File("src/main/resources/UserListJson/info.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine() + "\n";
                reading_string += data;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return reading_string;
    }


}
