package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.MarketPlaceParser;
import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-06-2:54 pm
 */
public class CreateFlightPlanController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private String id;

    @FXML
    private Label state;
    @FXML
    private ChoiceBox shipChoices;
    @FXML
    private ChoiceBox destinationChoices;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username, Game.token);
        MarketPlaceParser marketPlaceParser = new MarketPlaceParser();
        switch (button){
            case "Button[id=confirm, styleClass=button]'Confirm'":
                if(parameters.equals("online")){
                    HttpResponse<String> response = createFlightPlan();
                    if (checkResponse(response).equals("yes")){
                        FXMLLoader createFlightPlanSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/CreateFlightPlanSuccess.fxml"));
                        Parent createFlightPlanSuccessRoot = createFlightPlanSuccessLoader.load();
                        CreateFlightPlanSuccessfulController createFlightPlanSuccessfulController = createFlightPlanSuccessLoader.getController();
                        createFlightPlanSuccessfulController.setState(parameters);
                        setCurrentFlightId(response);
                        Scene createFlightScene = new Scene(createFlightPlanSuccessRoot);
                        Stage createFlightStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        createFlightStage.setScene(createFlightScene);
                        createFlightStage.show();
                    }else {
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(checkResponse(createFlightPlan()));
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
                    }

                }else if(parameters.equals("offline")){
                    FXMLLoader createFlightPlanSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/CreateFlightPlanSuccess.fxml"));
                    Parent createFlightPlanSuccessRoot = createFlightPlanSuccessLoader.load();
                    CreateFlightPlanSuccessfulController createFlightPlanSuccessfulController = createFlightPlanSuccessLoader.getController();
                    createFlightPlanSuccessfulController.setState(parameters);
                    Scene createFlightScene = new Scene(createFlightPlanSuccessRoot);
                    Stage createFlightStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    createFlightStage.setScene(createFlightScene);
                    createFlightStage.show();
                }
                break;
            case "Button[id=current, styleClass=button]'Current Flight Plan'":
                FXMLLoader CurrentFlightPlanLoader = new FXMLLoader(getClass().getResource("/AllPages/CurrentFlightPlan.fxml"));
                Parent currentFlightPlanRoot = CurrentFlightPlanLoader.load();
                CurrentFlightPlanController currentFlightPlanController = CurrentFlightPlanLoader.getController();
                currentFlightPlanController.setState(parameters);
                if (parameters.equals("online")){
                    currentFlightPlanController.getFlightPlanId();
                    currentFlightPlanController.setCurrent(getCurrentFlightInfo(currentFlightPlanController.getFlightPlanId()));
                }else if(parameters.equals("offline")){
                    currentFlightPlanController.setCurrentOffline();
                }
                Scene currentFlightPlanScene = new Scene(currentFlightPlanRoot);
                Stage currentFlightPlanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                currentFlightPlanStage.setScene(currentFlightPlanScene);
                currentFlightPlanStage.show();
                break;
            case "Button[id=back, styleClass=button]'Back'":
            case "Button[id=cancel, styleClass=button]'Cancel'":
                if (parameters.equals("online")){
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
                }else if(parameters.equals("offline")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                    Parent loginSuccessRoot = loader.load();
                    LoginSuccessController loginSuccessController = loader.getController();
//                        loginSuccessController.setGreeting(usernameStr);
                    loginSuccessController.setState(parameters);
                    Scene loginSuccessScene = new Scene(loginSuccessRoot);
                    Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    loginSuccessStage.setScene(loginSuccessScene);
                    loginSuccessStage.show();
                }
            break;
        case "Button[id=home, styleClass=button]'Home'":
            FXMLLoader defaultLoader = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
            Parent defaultRoot = defaultLoader.load();
            DefaultController defaultController = defaultLoader.getController();
            defaultController.setState(parameters);
            Scene defaultScene = new Scene(defaultRoot);
            Stage defaultStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
            defaultStage.setScene(defaultScene);
            defaultStage.show();
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
        this.parameters = Game.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setShipChoices(){
        try{
            String addingToken = "token=" + Game.token;
            String uri = "https://api.spacetraders.io/my/ships?" + addingToken;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            JsonArray shipArray = userPost.get("ships").getAsJsonArray();
            for (int i = 0; i < shipArray.size(); i++){
                JsonObject ship = shipArray.get(i).getAsJsonObject();
                shipChoices.getItems().add(ship.get("id").getAsString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
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
            destinationChoices.getItems().add(gettingSymbolArray.get(i));
        }
    }


    public JsonObject getLocations(){
        try{
            String addingToken = "token=" + Game.token;
            String systemStr = "OE";
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

    public HttpResponse<String> createFlightPlan(){
        try{
            String addingToken = "token=" + Game.token;
            String addingShipId = "&shipId=" + shipChoices.getValue().toString();
            String destination = "&destination=" + destinationChoices.getValue().toString();
            String uri = "https://api.spacetraders.io/my/flight-plans?" + addingToken + addingShipId + destination;
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            return response;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String checkResponse(HttpResponse<String> response){
        int statusCode = response.statusCode();
        String responseBody = response.body();
        JsonObject userPost = getUserPost(responseBody);
        if (statusCode != 201){
            return userPost.get("error").getAsJsonObject().get("message").getAsString();
        }else{
            return "yes";
        }
    }

    public void setCurrentFlightId(HttpResponse<String> response){
        String responseBody = response.body();
        JsonObject userPost = getUserPost(responseBody);
        JsonObject flightPlan = userPost.get("flightPlan").getAsJsonObject();

        id = flightPlan.get("id").getAsString();
        Game.flightId = id;
        System.out.println(id + "-----------");
    }

    public List<JsonObject> getCurrentFlightInfo(List<String> flightIds){
        List<JsonObject> jsonObjectList = new ArrayList<>();
        try{
            for (int i = 0; i < flightIds.size(); i++){
                String addingToken = "?token=" + Game.token;
                String uri = "https://api.spacetraders.io/my/flight-plans/" + flightIds.get(i) + addingToken ;
                System.out.println(uri);
                HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                        .GET()
                        .build();
                HttpClient client = HttpClient.newBuilder().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                int statusCode = response.statusCode();
                JsonObject userPost = getUserPost(responseBody);
                jsonObjectList.add(userPost);
                System.out.println(userPost);
            }

            return jsonObjectList;
        }catch(Exception e){
            e.printStackTrace();
            return null;
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
