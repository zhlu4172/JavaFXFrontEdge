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
 * @create 2022-04-01-12:56 pm
 */
public class RegisterController implements Initializable, Clickable{
    private String parameters;

    private String newState;

    private String token;
    private UserParser user;

    @FXML
    private TextField username;

    @FXML
    private Label state;

    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        switch (button){
            case"Button[id=cancel, styleClass=button]'Cancel'":
            case "Button[id=back, styleClass=button]'Back'":
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

            case"Button[id=confirm, styleClass=button]'Confirm'":
                if (parameters.equals("offline")){
                    if (!username.getText().isEmpty()){
                        try{
                            UserParser userParser = new UserParser(username.getText(), null);
                            user = userParser;
//                            userParser.createNewUser(getUsername());
                            token = userParser.getToken();
                            FXMLLoader registerSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/RegisterSuccess.fxml"));
                            Parent registerSuccessRoot = registerSuccessLoader.load();
                            RegisterSuccessController registerSuccessController = registerSuccessLoader.getController();
                            registerSuccessController.setUser(user);
                            registerSuccessController.setToken(user);
                            registerSuccessController.setGreeting(user);
                            Scene registerSuccessScene = new Scene(registerSuccessRoot);
                            Stage registerSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                            registerSuccessStage.setScene(registerSuccessScene);
                            registerSuccessStage.show();
                            break;
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                    }
                }else if(parameters.equals("online")){
                    try{
                        String uri = "https://api.spacetraders.io/users/" + username.getText() + "/claim";
                        HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                                .POST(HttpRequest.BodyPublishers.noBody())
                                .build();

                        HttpClient client = HttpClient.newBuilder().build();

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                        int statusCode = response.statusCode();
                        String responseBody = response.body();
                        System.out.println(responseBody);
                        JsonObject userPost = getUserPost(responseBody);
                        if (statusCode == 201){
                            FXMLLoader registerSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/RegisterSuccess.fxml"));
                            Parent registerSuccessRoot = registerSuccessLoader.load();
                            RegisterSuccessController registerSuccessController = registerSuccessLoader.getController();
                            registerSuccessController.setTokenOnline(userPost.get("token").getAsString());
                            String gettingUsername = userPost.get("user").getAsJsonObject().get("username").getAsString();
                            registerSuccessController.setUserOnline(gettingUsername);
                            registerSuccessController.setGreetingOnline(gettingUsername);
                            Scene registerSuccessScene = new Scene(registerSuccessRoot);
                            Stage registerSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                            registerSuccessStage.setScene(registerSuccessScene);
                            registerSuccessStage.show();
                        }else {
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
                        }

                        System.out.println("Response status code was: " + response.statusCode());
                        System.out.println("Response headers were: " + response.headers());
                        System.out.println("Response body was:\n" + response.body());
                        break;
                    }catch (Exception exception){
                        exception.printStackTrace();
                    }

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

    public String getUsername(){
        return username.getText();
    }


    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }
}
