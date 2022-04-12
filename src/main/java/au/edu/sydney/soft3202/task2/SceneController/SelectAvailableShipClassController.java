package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author Emma LU
 * @create 2022-04-05-2:31 pm
 */
public class SelectAvailableShipClassController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private ChoiceBox choices;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(SpaceTraderApp.username, SpaceTraderApp.token);
        switch (button){
            case"Button[id=confirm, styleClass=button]'Confirm'":
                if (parameters.equals("online")){
                    FXMLLoader availableShipLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableShips.fxml"));
                    Parent availableShipRoot = availableShipLoader.load();
                    AvailableShipController availableShipController = availableShipLoader.getController();
                    availableShipController.setState(parameters);
                    if (choices.getValue().toString().equals("All Ships")){
                        availableShipController.setAvailableShipDetailsOnline(getAvailableShips());
                    }else{
                        availableShipController.setAvailableShipDetailsOnline(getAvailableShipsAsForClass(choices.getValue().toString()));
                    }
                    Scene availableLoanScene = new Scene(availableShipRoot);
                    Stage availableLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    availableLoanStage.setScene(availableLoanScene);
                    availableLoanStage.show();
                }else if(parameters.equals("offline")){
                    FXMLLoader availableShipLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableShips.fxml"));
                    Parent availableShipRoot = availableShipLoader.load();
                    AvailableShipController availableShipController = availableShipLoader.getController();
                    availableShipController.setState(parameters);
                    availableShipController.setAvailableShipsOffline(readFakeInfoFile());
                    Scene availableLoanScene = new Scene(availableShipRoot);
                    Stage availableLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    availableLoanStage.setScene(availableLoanScene);
                    availableLoanStage.show();
                }
                break;
            case"Button[id=purchase, styleClass=button]'Purchase Your Ship'":
                FXMLLoader purchaseShipOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseNewShipOnline.fxml"));
                Parent purchaseShipOnlineRoot = purchaseShipOnlineLoader.load();
                PurchaseNewShipController purchaseNewShipController = purchaseShipOnlineLoader.getController();
                purchaseNewShipController.setState(parameters);
                purchaseNewShipController.setSystemChoices();
                Scene purchaseShipScene = new Scene(purchaseShipOnlineRoot);
                Stage purchaseShipStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                purchaseShipStage.setScene(purchaseShipScene);
                purchaseShipStage.show();
                break;
            case"Button[id=back, styleClass=button]'Back'":
            case"Button[id=cancel, styleClass=button]'Cancel'":
                if(parameters.equals("offline")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                    Parent loginSuccessRoot = loader.load();
                    LoginSuccessController loginSuccessController = loader.getController();
//                        loginSuccessController.setGreeting(usernameStr);
                    loginSuccessController.setState(parameters);
                    Scene loginSuccessScene = new Scene(loginSuccessRoot);
                    Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    loginSuccessStage.setScene(loginSuccessScene);
                    loginSuccessStage.show();
                }else if(parameters.equals("online")) {
                    try {
                        String addingToken = "token=" + SpaceTraderApp.token;
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
                        loginSuccessController.setToken(SpaceTraderApp.token);
                        loginSuccessController.setGreeting(gettingUsername);
                        loginSuccessController.setState(parameters);
                        Scene loginSuccessScene = new Scene(loginSuccessRoot);
                        Stage loginSuccessStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                        loginSuccessStage.setScene(loginSuccessScene);
                        loginSuccessStage.show();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                break;

            case "Button[id=info, styleClass=button]'Info'":
                System.out.println("Hi");
                if (parameters.equals("online")){
                    String addingToken = "token=" + SpaceTraderApp.token;
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
            case"Button[id=yourShips, styleClass=button]'View Your Ships'":
                if (parameters.equals("offline")){
                    FXMLLoader myShipLoader = new FXMLLoader(getClass().getResource("/AllPages/YourAvailableShips.fxml"));
                    Parent myShipRoot = myShipLoader.load();
                    YourAvailableShipsController yourAvailableShipsController = myShipLoader.getController();
                    yourAvailableShipsController.setState(parameters);
                    yourAvailableShipsController.setShipsOffline();
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
        this.parameters = SpaceTraderApp.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public void setChoices(){
        JsonObject ships = getAvailableShips();
        JsonArray shipsArray = ships.get("ships").getAsJsonArray();
        if (shipsArray == null || shipsArray.size() == 0){
            return;
        }
        ArrayList<String> classArray = new ArrayList<>();
        for (int i = 0; i < shipsArray.size(); i++){
            JsonObject ship = shipsArray.get(i).getAsJsonObject();
            String type = ship.get("class").getAsString();
            if (!classArray.contains(type)){
                classArray.add(type);
            }
        }
        for (int i = 0; i < classArray.size(); i++){
            choices.getItems().add(classArray.get(i));
        }
        choices.getItems().add("All Ships");
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public JsonObject getAvailableShips(){
        try{
            String addingToken = "token=" + SpaceTraderApp.token;
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

    public JsonObject getAvailableShipsAsForClass(String shipClass){
        try{
            String addingToken = "token=" + SpaceTraderApp.token;
            String uri = "https://api.spacetraders.io/types/ships?" + addingToken + "&class=" + shipClass;
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
            return userPost;
        }catch(Exception e){
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

    public HttpResponse<String> getAvailableShipsFromAPI(){
        try{
            String addingToken = "token=" + SpaceTraderApp.token;
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

    public void setChoicesOffline(){
        choices.getItems().add("All Ships");
    }

    public String readFakeInfoFile(){
//        String reading_string = "";
//        try {
//            File myObj = new File("src/main/resources/UserListJson/info.txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine() + "\n";
//                reading_string += data;
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
        String returnString = "Username: offline user\n" +
                "Your Ship count: 0\n" +
                "Your Joining Time: 2022-04-05T04:15:28.472Z\n" +
                "Your Current Credits: 200000";
        return returnString;
    }


}
