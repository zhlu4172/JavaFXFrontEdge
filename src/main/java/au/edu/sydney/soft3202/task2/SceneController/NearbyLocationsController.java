package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.MarketPlaceParser;
import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
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
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

/**
 * @author Emma LU
 * @create 2022-04-06-2:09 pm
 */
public class NearbyLocationsController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private Label nearbyLocations;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(SpaceTraderApp.username, SpaceTraderApp.token);
        MarketPlaceParser marketPlaceParser = new MarketPlaceParser();
        switch (button){
            case "Button[id=back, styleClass=button]'Back'":
            case "Button[id=home, styleClass=button]'Home'":
                if (parameters.equals("online")){
                    try{
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

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setNearbyLocations(JsonObject locations){
        JsonArray locationsArray = locations.get("locations").getAsJsonArray();
        String addingDetails = "";
        for (int i = 0; i < locationsArray.size(); i++){
            JsonObject gettingLocation = locationsArray.get(i).getAsJsonObject();
            String symbol = gettingLocation.get("symbol").getAsString();
            String type = gettingLocation.get("type").getAsString();
            String name = gettingLocation.get("name").getAsString();
            Long xPos = gettingLocation.get("x").getAsLong();
            Long yPos = gettingLocation.get("y").getAsLong();
            Boolean allowsConstruction = gettingLocation.get("allowsConstruction").getAsBoolean();
            JsonArray traits = gettingLocation.get("traits").getAsJsonArray();
            System.out.println(traits);
            String traitsStr = "";
            for (int j = 0; j < traits.size(); j++){
                if (j == traits.size() - 1){
                    traitsStr += traits.get(j);
                }else{
                    traitsStr = traitsStr + traits.get(j) + ", ";
                }
            }
            String addingDetail = "Symbol is   " + symbol + "\nType is    " + type + "\nName is    " + name +
                    "\nX position is   " + xPos + "\nY position is   " + yPos +
                    "\nAllow Construction is   " + allowsConstruction + "\nTraits are    " + traitsStr + "\n\n";
            addingDetails += addingDetail;
        }
        this.nearbyLocations.setText(addingDetails);
    }

    public void setNearbyLocationsOffline(){
        this.nearbyLocations.setText("Symbol is OE-KO\n" +
                "Type is PLANET\n" +
                "Name is Koria\n" +
                "X position is -49\n" +
                "Y position is\n" +
                "Allow Construction is false\n" +
                "Traits are \"SOME NATURAL_CHEMICALS\" \"SOME_RARE_METAL_ORES\"\n" +
                "Symbol is OE-UC\n" +
                "Type is PLANET\n" +
                "Name is Ucarro\n" +
                "X position is -17\n" +
                "Y position is -72\n" +
                "Allow Construction is false\n" +
                "Traits are \"SOME METAL ORES\" \"ARABLE LAND\"");
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
