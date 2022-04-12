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
 * @create 2022-04-05-3:22 am
 */
public class AccountInfoController implements Clickable, Initializable {
    private String parameters;

    private String newState;

    @FXML
    private Label state;
    @FXML
    private Label details;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        switch (button) {
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
            case "Button[id=back, styleClass=button]'Back'":
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
                    loginSuccessController.setState(parameters);
                    Scene loginSuccessScene = new Scene(loginSuccessRoot);
                    Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    loginSuccessStage.setScene(loginSuccessScene);
                    loginSuccessStage.show();
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
        parameters = SpaceTraderApp.parameters;
    }

    public void setDetails(JsonObject jsonObject){
        String gettingUsername = jsonObject.get("user").getAsJsonObject().get("username").getAsString();
        String gettingShipCount = jsonObject.get("user").getAsJsonObject().get("shipCount").getAsString();
        String gettingJoinTime = jsonObject.get("user").getAsJsonObject().get("joinedAt").getAsString();
        String gettingCredits = jsonObject.get("user").getAsJsonObject().get("credits").getAsString();
        String settingString = "";
        settingString = "Username:   " + gettingUsername + "\n";
        settingString = settingString + "Your Ship count:   " + gettingShipCount + "\n";
        settingString = settingString + "Your Joining Time:   " + gettingJoinTime + "\n";
        settingString = settingString + "Your Current Credits:   " + gettingCredits + "\n";
        details.setText(settingString);
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setDetailsOffline(String readingString){
        this.details.setText(readingString);
    }
}
