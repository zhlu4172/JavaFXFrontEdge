package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
import com.google.gson.Gson;
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
 * @create 2022-04-04-11:42 pm
 */
public class PurchaseGoodsSuccessController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    @FXML
    private Label state;
    @FXML
    private Label details;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button) {
            case "Button[id=back, styleClass=button]'Back'":
                FXMLLoader marketplaceOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/MarketPlaceOnline.fxml"));
                Parent marketPlaceOnlineRoot = marketplaceOnlineLoader.load();
                MarketPlaceController marketPlaceOnlineController = marketplaceOnlineLoader.getController();
                marketPlaceOnlineController.setState(parameters);
                if (parameters.equals("online")){
                    marketPlaceOnlineController.setLocationChoices(marketPlaceOnlineController.getLocations());
                }
                Scene marketplaceOnlineScene = new Scene(marketPlaceOnlineRoot);
                Stage marketplaceOnlineStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                marketplaceOnlineStage.setScene(marketplaceOnlineScene);
                marketplaceOnlineStage.show();
                break;
            case "Button[id=home, styleClass=button]'Home'":
                if(parameters.equals("online")){
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
                }else{
                    FXMLLoader defaultLoader = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
                    Parent defaultRoot = defaultLoader.load();
                    DefaultController defaultController = defaultLoader.getController();
                    defaultController.setState(parameters);
                    Scene defaultScene = new Scene(defaultRoot);
                    Stage defaultStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    defaultStage.setScene(defaultScene);
                    defaultStage.show();
                }

                break;
            case "Button[id=info, styleClass=button]'Info'":
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
        parameters = SpaceTraderApp.parameters;
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
