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
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.w3c.dom.Text;

/**
 * @author Emma LU
 * @create 2022-04-03-8:44 pm
 */
public class PurchaseNewShipController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private String shipSymbol;
    private String plantSymbol;

    @FXML
    private Label state;

    @FXML
    private TextField ship;
    @FXML
    private TextField plant;
    @FXML
    private ChoiceBox systemChoices;
    @FXML
    private ChoiceBox shipChoice;
    @FXML
    private ChoiceBox locationChoice;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button) {
            case "Button[id=confirm, styleClass=button]'Confirm'":
                if (parameters.equals("offline")){
                    FXMLLoader purchaseShipSuccessfulLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseShipSuccessfully.fxml"));
                    Parent purchaseShipSuccessfulRoot = purchaseShipSuccessfulLoader.load();
                    PurchaseShipSuccessfullyController purchaseShipSuccessfullyController = purchaseShipSuccessfulLoader.getController();

                    purchaseShipSuccessfullyController.setState(parameters);
//                purchaseShipSuccessfullyController.setDetails();
                    Scene purchaseShipSuccessfulScene = new Scene(purchaseShipSuccessfulRoot);
                    Stage purchaseShipSuccessfulStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    purchaseShipSuccessfulStage.setScene(purchaseShipSuccessfulScene);
                    purchaseShipSuccessfulStage.show();
//                    getPlantSymbol();
//                    getShipSymbol();
//                    UserParser userParser = new UserParser(Game.username, Game.token);
//                    String purchaseShipSuccessfully = userParser.purchaseShip(plantSymbol, shipSymbol);
//                    if (purchaseShipSuccessfully.equals("yes")){
//                        FXMLLoader purchaseShipSuccessfulLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseShipSuccessfully.fxml"));
//                        Parent purchaseShipSuccessfulRoot = purchaseShipSuccessfulLoader.load();
//                        PurchaseShipSuccessfullyController purchaseShipSuccessfullyController = purchaseShipSuccessfulLoader.getController();
//
//                        purchaseShipSuccessfullyController.setState(parameters);
////                purchaseShipSuccessfullyController.setDetails();
//                        Scene purchaseShipSuccessfulScene = new Scene(purchaseShipSuccessfulRoot);
//                        Stage purchaseShipSuccessfulStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
//                        purchaseShipSuccessfulStage.setScene(purchaseShipSuccessfulScene);
//                        purchaseShipSuccessfulStage.show();
//                    }else{
//                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
//                        Parent errorPageRoot = errorPageLoader.load();
//                        ErrorPageController errorPageController = errorPageLoader.getController();
//                        errorPageController.setState(parameters);
//                        errorPageController.setErrormessage(purchaseShipSuccessfully);
////                purchaseShipSuccessfullyController.setDetails();
//                        Scene errorPageScene = new Scene(errorPageRoot);
//                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
//                        errorPageStage.setScene(errorPageScene);
//                        errorPageStage.show();
//                    }
                }else{
                    String purchaseSuccessOrNot = checkPurchaseSuccessful(purchaseShip());
                    if (purchaseSuccessOrNot.equals("yes")){
                        FXMLLoader purchaseShipSuccessfulLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseShipSuccessfully.fxml"));
                        Parent purchaseShipSuccessfulRoot = purchaseShipSuccessfulLoader.load();
                        PurchaseShipSuccessfullyController purchaseShipSuccessfullyController = purchaseShipSuccessfulLoader.getController();

                        purchaseShipSuccessfullyController.setState(parameters);
//                purchaseShipSuccessfullyController.setDetails();
                        Scene purchaseShipSuccessfulScene = new Scene(purchaseShipSuccessfulRoot);
                        Stage purchaseShipSuccessfulStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        purchaseShipSuccessfulStage.setScene(purchaseShipSuccessfulScene);
                        purchaseShipSuccessfulStage.show();
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(purchaseSuccessOrNot);
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
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
            case "Button[id=back, styleClass=button]'Back'":
                FXMLLoader availableShipLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableShips.fxml"));
                Parent availableShipRoot = availableShipLoader.load();
                AvailableShipController availableShipController = availableShipLoader.getController();
                availableShipController.setState(parameters);
                UserParser userParser2 = new UserParser(Game.username,Game.token);
                availableShipController.setAvailableShipDetails(userParser2);
                Scene availableShipsScene = new Scene(availableShipRoot);
                Stage availableShipsStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                availableShipsStage.setScene(availableShipsScene);
                availableShipsStage.show();
            case "Button[id=choose1, styleClass=button]'Choose'":
                setLocationChoices(getLocations());
                break;
            case "Button[id=choose2, styleClass=button]'Choose'":
                setShipChoice(getAvailableShips());
                break;
            case "Button[id=info, styleClass=button]'Info'":
                System.out.println("Hi");
                if (parameters.equals("online")){
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
                    if (statusCode == 200){
                        FXMLLoader accountInfoLoader = new FXMLLoader(getClass().getResource("/AllPages/AccountInfo.fxml"));
                        Parent accountInfoRoot = accountInfoLoader.load();
                        AccountInfoController accountInfoController = accountInfoLoader.getController();
                        accountInfoController.setState(parameters);
                        accountInfoController.setDetails(userPost);
                        Scene accountInfoScene = new Scene(accountInfoRoot);
                        Stage accountInfoStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        accountInfoStage.setScene(accountInfoScene);
                        accountInfoStage.show();
                    }
                }else if(parameters.equals("offline")){
                    FXMLLoader accountInfoLoader = new FXMLLoader(getClass().getResource("/AllPages/AccountInfo.fxml"));
                    Parent accountInfoRoot = accountInfoLoader.load();
                    AccountInfoController accountInfoController = accountInfoLoader.getController();
                    accountInfoController.setState(parameters);
                    accountInfoController.setDetailsOffline(readFakeInfoFile() );
                    Scene accountInfoScene = new Scene(accountInfoRoot);
                    Stage accountInfoStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    accountInfoStage.setScene(accountInfoScene);
                    accountInfoStage.show();
                }
                break;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parameters = Game.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public void getShipSymbol(){
        shipSymbol = ship.getText();
    }

    public void getPlantSymbol(){
        plantSymbol = plant.getText();
    }

    public void setLocationChoices(JsonObject jsonObject){
        JsonArray locationsArray = jsonObject.get("locations").getAsJsonArray();
        if (locationsArray == null || locationsArray.size() == 0){
            return;
        }
        ArrayList<String> gettingSymbolArray = new ArrayList<>();
        for (int i = 0; i < locationsArray.size(); i++){
            JsonObject location = locationsArray.get(i).getAsJsonObject();
            String gettingSymbol = location.get("symbol").getAsString();
            if (!gettingSymbolArray.contains(gettingSymbol)){
                gettingSymbolArray.add(gettingSymbol);
            }
        }
        for (int i = 0; i < gettingSymbolArray.size(); i++){
            locationChoice.getItems().add(gettingSymbolArray.get(i));
        }
    }

    public void setSystemChoices(){
        systemChoices.getItems().add("OE");
    }

    public JsonObject getLocations(){
        try{
            String addingToken = "token=" + Game.token;
            String systemStr = systemChoices.getValue().toString();
            String uri = "https://api.spacetraders.io/systems/" + systemStr + "/locations?" + addingToken;
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            return userPost;
        }catch(Exception e){
            return null;
        }
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setShipChoice(JsonObject ships){
        JsonArray shipsArray = ships.get("ships").getAsJsonArray();
        if (shipsArray == null || shipsArray.size() == 0){
            return;
        }else{
            ArrayList<String> shipTypes = new ArrayList<>();
            for (int i = 0; i < shipsArray.size(); i++){
                JsonObject shipObject = shipsArray.get(i).getAsJsonObject();
                String types = shipObject.get("type").getAsString();
                if (!shipTypes.contains(types)){
                    shipTypes.add(types);
                }
            }

            for (int i = 0; i < shipTypes.size(); i++){
                shipChoice.getItems().add(shipTypes.get(i));
            }
        }
    }


    public JsonObject getAvailableShips(){
        try{
            String addingToken = "token=" + Game.token;
            String uri = "https://api.spacetraders.io/types/ships?" + addingToken;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            return userPost;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public HttpResponse<String> purchaseShip(){
        try{
            String addingToken = "token=" + Game.token;
            String addingLocation = "&location=" + locationChoice.getValue().toString();
            String addingType = "&type=" + shipChoice.getValue().toString();
            String uri = "https://api.spacetraders.io/my/ships?" + addingToken + addingLocation + addingType;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            return response;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String checkPurchaseSuccessful(HttpResponse<String> response){
        int statusCode = response.statusCode();
        String responseBody = response.body();
        JsonObject userPost = getUserPost(responseBody);
        if (statusCode != 201){
            return userPost.get("error").getAsJsonObject().get("message").getAsString();
        }else{
            return "yes";
        }
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
