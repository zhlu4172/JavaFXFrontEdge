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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

/**
 * @author Emma LU
 * @create 2022-04-04-7:37 pm
 */
public class MarketPlaceController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private TextField plant;
    @FXML
    private ChoiceBox choices;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username, Game.token);
        MarketPlaceParser marketPlaceParser = new MarketPlaceParser();
        switch (button){
            case"Button[id=confirm, styleClass=button]'Confirm'":
                if (parameters.equals("offline")){
                    FXMLLoader viewMarketPlaceByLocationLoader = new FXMLLoader(getClass().getResource("/AllPages/ViewMarketPlacesByLocation.fxml"));
                    Parent viewMarketPlaceByLocationRoot = viewMarketPlaceByLocationLoader.load();
                    ViewMarketPlaceByLocationController viewMarketPlaceByLocation = viewMarketPlaceByLocationLoader.getController();
                    viewMarketPlaceByLocation.setState(parameters);
                    viewMarketPlaceByLocation.setLocation(plant.getText());
                    viewMarketPlaceByLocation.setMarketPlacesText(marketPlaceParser,getLocation());
                    Scene viewMarketPlaceByLocationScene = new Scene(viewMarketPlaceByLocationRoot);
                    Stage viewMarketPlaceByLocationStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    viewMarketPlaceByLocationStage.setScene(viewMarketPlaceByLocationScene);
                    viewMarketPlaceByLocationStage.show();
                }else if(parameters.equals("online")){
                    if (checkStatusCode(getMarketplaceAsForLocation()).equals("yes")){
                        FXMLLoader viewMarketPlaceByLocationLoader = new FXMLLoader(getClass().getResource("/AllPages/ViewMarketPlacesByLocationOnline.fxml"));
                        Parent viewMarketPlaceByLocationRoot = viewMarketPlaceByLocationLoader.load();
                        ViewMarketPlaceByLocationController viewMarketPlaceByLocation = viewMarketPlaceByLocationLoader.getController();
                        viewMarketPlaceByLocation.setState(parameters);
                        viewMarketPlaceByLocation.setMarketplacesTextOnline(getMarketplaceAsForLocation());
                        viewMarketPlaceByLocation.setShipChoices();

                        Scene viewMarketPlaceByLocationScene = new Scene(viewMarketPlaceByLocationRoot);
                        Stage viewMarketPlaceByLocationStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        viewMarketPlaceByLocationStage.setScene(viewMarketPlaceByLocationScene);
                        viewMarketPlaceByLocationStage.show();
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(checkStatusCode(getMarketplaceAsForLocation()));
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

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parameters = Game.parameters;
    }

    public String getLocation(){
        return plant.getText();
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
            choices.getItems().add(gettingSymbolArray.get(i));
        }
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public HttpResponse<String> getMarketplaceAsForLocation(){
        try{
            try{
                String addingLocation = choices.getValue().toString();
                String uri = "https://api.spacetraders.io/locations/" + addingLocation + "/marketplace?token=" + Game.token;
                System.out.println(uri);
                HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                        .GET()
                        .build();
                HttpClient client = HttpClient.newBuilder().build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                String responseBody = response.body();
                int statusCode = response.statusCode();
                JsonObject userPost = getUserPost(responseBody);
                System.out.println(responseBody);
                System.out.println(statusCode);
                return response;

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
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
