package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

/**
 * @author Emma LU
 * @create 2022-04-01-1:03 pm
 */
public class LoginController implements Clickable, Initializable {
    private String newState;
    public String usernameStr;
    private String tokenStr;
    private String parameters;

    @FXML
    private Label userMessage;

    @FXML
    private TextField username;

    @FXML
    private TextField token;

    @FXML
    private Label state;


    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button){
            case"Button[id=cancel, styleClass=button]'Cancel'":
            case"Button[id=home, styleClass=button]'Home'":
            case"Button[id=back, styleClass=button]'Back'":
                FXMLLoader defaultLoader = new FXMLLoader(getClass().getResource("/AllPages/default.fxml"));
                Parent defaultRoot = defaultLoader.load();
                DefaultController defaultController = defaultLoader.getController();
                defaultController.setState(parameters);
                Scene defaultScene = new Scene(defaultRoot);
                Stage defaultStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                defaultStage.setScene(defaultScene);
                defaultStage.show();
                break;
            case "Button[id=confirm, styleClass=button]'Confirm'":
                setToken();
                UserParser userParser = new UserParser(usernameStr,tokenStr);
                if (parameters.equals("offline")){
//                    if (userParser.login(usernameStr,tokenStr)){
                    if(!token.getText().isEmpty()){
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                        Parent loginSuccessRoot = loader.load();
                        LoginSuccessController loginSuccessController = loader.getController();
//                        loginSuccessController.setGreeting(usernameStr);
                        loginSuccessController.setState(parameters);
                        Scene loginSuccessScene = new Scene(loginSuccessRoot);
                        Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        loginSuccessStage.setScene(loginSuccessScene);
                        loginSuccessStage.show();
                    }else{
                        FXMLLoader errorLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorRoot = errorLoader.load();
                        ErrorPageController errorPageController = errorLoader.getController();
                        errorPageController.setErrormessage("The token should not be empty!");
                        Scene errorScene = new Scene(errorRoot);
                        Stage errorStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorStage.setScene(errorScene);
                        errorStage.show();
                    }
                }else if(parameters.equals("online")){
                    try{
                        String addingToken = "token=" + tokenStr;
                        SpaceTraderApp.token = tokenStr;
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
                            String gettingUsername = userPost.get("user").getAsJsonObject().get("username").getAsString();
                            SpaceTraderApp.username = gettingUsername;
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                            Parent loginSuccessRoot = loader.load();
                            LoginSuccessController loginSuccessController = loader.getController();
                            loginSuccessController.setUsername(gettingUsername);
                            loginSuccessController.setToken(tokenStr);
                            usernameStr = gettingUsername;
                            setToken();
                            loginSuccessController.setGreeting(gettingUsername);
                            loginSuccessController.setState(parameters);
                            Scene loginSuccessScene = new Scene(loginSuccessRoot);
                            Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                            loginSuccessStage.setScene(loginSuccessScene);
                            loginSuccessStage.show();
                            break;
                        }else{
                            FXMLLoader ErrorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                            Parent ErrorPageRoot = ErrorPageLoader.load();
                            ErrorPageController errorPageController = ErrorPageLoader.getController();
                            String errorMessage = userPost.get("error").getAsJsonObject().get("message").getAsString();
                            errorPageController.setErrormessage(errorMessage);
                            errorPageController.setState(parameters);
                            Scene errorPageScene = new Scene(ErrorPageRoot);
                            Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                            errorPageStage.setScene(errorPageScene);
                            errorPageStage.show();
                            break;
                        }


                    }catch(Exception exception){
                        exception.printStackTrace();
                    }
                }

                break;


        }
    }

    public void setUsername(){
        if (!username.getText().isEmpty()){
            usernameStr = username.getText();
            SpaceTraderApp.username = usernameStr;
        }
    }

    public void setToken(){
        if (!token.getText().isEmpty()){
            tokenStr = token.getText();
            SpaceTraderApp.token = tokenStr;
        }
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.parameters = SpaceTraderApp.parameters;
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }
}
