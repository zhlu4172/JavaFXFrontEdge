package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-02-7:57 pm
 */
public class LoginSuccessController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private String token;
    private String username;
    @FXML
    private Label greeting;
    @FXML
    private Label state;
    @FXML
    private Button info;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username,Game.token);
        System.out.println(username);
        switch (button){
            case"Button[id=back, styleClass=button]'Back'":
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/AllPages/Login.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginController = loginLoader.getController();
                loginController.setState(parameters);
                Scene loginScene = new Scene(loginRoot);
                Stage loginWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                loginWindow.setScene(loginScene);
                loginWindow.show();
                break;
            case"Button[id=home, styleClass=button]'Home'":
                FXMLLoader defaultLoader = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
                Parent defaultRoot = defaultLoader.load();
                DefaultController defaultController = defaultLoader.getController();
                defaultController.setState(parameters);
                Scene defaultScene = new Scene(defaultRoot);
                Stage defaultStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                defaultStage.setScene(defaultScene);
                defaultStage.show();
                break;
            case"Button[id=loans, styleClass=button]'Loans'":
                FXMLLoader availableLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableLoans.fxml"));
                Parent availableLoanRoot = availableLoanLoader.load();
                AvailableLoanController availableLoanController = availableLoanLoader.getController();
                availableLoanController.setState(parameters);
                availableLoanController.setToken(token);
//                UserParser userParser = new UserParser(Game.username,Game.token);
                if (parameters.equals("offline")){
                    availableLoanController.setAvailableLoans(userParser.getAvailableLoans());
                }else if(parameters.equals("online")){
                    String addingToken = "token=" + Game.token;
                    String uri = "https://api.spacetraders.io/types/loans?" + addingToken;
                    HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                            .GET()
                            .build();
                    HttpClient client = HttpClient.newBuilder().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String responseBody = response.body();
                    JsonObject userPost = getUserPost(responseBody);
                    System.out.println(userPost);
                    availableLoanController.setAvailableLoansOnline(userPost);
                }

                Scene availableLoanScene = new Scene(availableLoanRoot);
                Stage availableLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                availableLoanStage.setScene(availableLoanScene);
                availableLoanStage.show();
                break;
            case"Button[id=ship, styleClass=button]'Ship'":
                if(parameters.equals("offline")){
                    FXMLLoader selectShipClassLoader = new FXMLLoader(getClass().getResource("/AllPages/SelectShipClass.fxml"));
                    Parent selectShipClassRoot = selectShipClassLoader.load();
                    SelectAvailableShipClassController selectAvailableShipClassController  = selectShipClassLoader.getController();
                    selectAvailableShipClassController.setState(parameters);
                    selectAvailableShipClassController.setChoicesOffline();
                    Scene selectShipClassScene = new Scene(selectShipClassRoot);
                    Stage selectShipClassStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    selectShipClassStage.setScene(selectShipClassScene);
                    selectShipClassStage.show();
                }else if(parameters.equals("online")){
                    FXMLLoader selectShipClassLoader = new FXMLLoader(getClass().getResource("/AllPages/SelectShipClass.fxml"));
                    Parent selectShipClassRoot = selectShipClassLoader.load();
                    SelectAvailableShipClassController selectAvailableShipClassController  = selectShipClassLoader.getController();
                    selectAvailableShipClassController.setState(parameters);
                    selectAvailableShipClassController.setChoices();
                    Scene selectShipClassScene = new Scene(selectShipClassRoot);
                    Stage selectShipClassStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    selectShipClassStage.setScene(selectShipClassScene);
                    selectShipClassStage.show();
                }
                break;
            case"Button[id=marketplace, styleClass=button]'MarketPlace'":
                if(parameters.equals("offline")){
                    FXMLLoader marketplaceOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/MarketPlaceOnline.fxml"));
                    Parent marketPlaceOnlineRoot = marketplaceOnlineLoader.load();
                    MarketPlaceController marketPlaceOnlineController = marketplaceOnlineLoader.getController();
                    marketPlaceOnlineController.setState(parameters);
                    Scene marketplaceOnlineScene = new Scene(marketPlaceOnlineRoot);
                    Stage marketplaceOnlineStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    marketplaceOnlineStage.setScene(marketplaceOnlineScene);
                    marketplaceOnlineStage.show();
                }else if(parameters.equals("online")){
                    FXMLLoader marketplaceOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/MarketPlaceOnline.fxml"));
                    Parent marketPlaceOnlineRoot = marketplaceOnlineLoader.load();
                    MarketPlaceController marketPlaceOnlineController = marketplaceOnlineLoader.getController();
                    marketPlaceOnlineController.setState(parameters);
                    marketPlaceOnlineController.setLocationChoices(marketPlaceOnlineController.getLocations());
                    Scene marketplaceOnlineScene = new Scene(marketPlaceOnlineRoot);
                    Stage marketplaceOnlineStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    marketplaceOnlineStage.setScene(marketplaceOnlineScene);
                    marketplaceOnlineStage.show();
                }

                break;
            case "Button[id=info, styleClass=button]'Info'":
                System.out.println("Hi");
                if (parameters.equals("online")){
                    String addingToken = "token=" + token;
                    userParser.setToken(token);
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
            case "Button[id=locations, styleClass=button]'Locations'":
                if(parameters.equals("online")){
                    String addingToken = "token=" + token;
                    String addingType = "&type=PLANET";
                    userParser.setToken(token);
                    String uri = "https://api.spacetraders.io/systems/OE/locations?" + addingToken + addingType;
                    System.out.println(uri);
                    HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                            .GET()
                            .build();

                    HttpClient client = HttpClient.newBuilder().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String responseBody = response.body();
                    int statusCode = response.statusCode();
                    JsonObject userPost = getUserPost(responseBody);
                    System.out.println(userPost);

                    FXMLLoader nearbyLocationsLoader = new FXMLLoader(getClass().getResource("/AllPages/NearbyLocations.fxml"));
                    Parent nearbyLocationsRoot = nearbyLocationsLoader.load();
                    NearbyLocationsController nearbyLocationsController = nearbyLocationsLoader.getController();
                    nearbyLocationsController.setState(parameters);
                    nearbyLocationsController.setNearbyLocations(userPost);
                    Scene nearbyLocationsScene = new Scene(nearbyLocationsRoot);
                    Stage nearbyLocationsStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    nearbyLocationsStage.setScene(nearbyLocationsScene);
                    nearbyLocationsStage.show();
                }else if(parameters.equals("offline")){
                    FXMLLoader nearbyLocationsLoader = new FXMLLoader(getClass().getResource("/AllPages/NearbyLocations.fxml"));
                    Parent nearbyLocationsRoot = nearbyLocationsLoader.load();
                    NearbyLocationsController nearbyLocationsController = nearbyLocationsLoader.getController();
                    nearbyLocationsController.setState(parameters);
                    nearbyLocationsController.setNearbyLocationsOffline();
                    Scene nearbyLocationsScene = new Scene(nearbyLocationsRoot);
                    Stage nearbyLocationsStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    nearbyLocationsStage.setScene(nearbyLocationsScene);
                    nearbyLocationsStage.show();
                }
                break;
            case "Button[id=flightplan, styleClass=button]'Flight Plan'":
                FXMLLoader createFlightPlanLoader = new FXMLLoader(getClass().getResource("/AllPages/CreateNewFlightPlan.fxml"));
                Parent createFlightRoot = createFlightPlanLoader.load();
                CreateFlightPlanController createFlightPlanController = createFlightPlanLoader.getController();
                createFlightPlanController.setState(parameters);
                if (parameters.equals("online")){
                    createFlightPlanController.setLocationChoices(createFlightPlanController.getLocations());
                    createFlightPlanController.setShipChoices();
                }
                Scene createFlightScene = new Scene(createFlightRoot);
                Stage createFlightStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                createFlightStage.setScene(createFlightScene);
                createFlightStage.show();
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




    public void setGreeting(String username){
        String newGreeting = greeting.getText() + ", " + username;
        greeting.setText(newGreeting);
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
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
