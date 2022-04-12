package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;

/**
 * @author Emma LU
 * @create 2022-04-01-11:38 pm
 */
public class RegisterSuccessController implements Clickable, Initializable {

    private String parameters;
    @FXML
    private Label userMessage;
    @FXML
    private Label greeting;
    @FXML
    private Label token;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch(button){
            case"Button[id=back, styleClass=button]'Back'":
                System.out.println("hi");
                FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("/AllPages/Register.fxml"));
                Parent registerRoot = registerLoader.load();
                RegisterController registerController = registerLoader.getController();
                registerController.setState(parameters);
                Scene registerScene = new Scene(registerRoot);
                Stage registerStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                registerStage.setScene(registerScene);
                registerStage.show();
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
            case"Button[id=login, styleClass=button]'Login'":
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/AllPages/Login.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginController = loginLoader.getController();
                loginController.setState(parameters);
                Scene loginScene = new Scene(loginRoot);
                Stage loginWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                loginWindow.setScene(loginScene);
                loginWindow.show();
            case "Button[id=copy, styleClass=button]'Copy'":
                copyToClipboard();
                break;

        }

    }

    public void setUser(UserParser user){
        if (user != null && user.getUsername() != null){
            this.userMessage.setText(user.getUsername());
        }
    }

    public void setUserOnline(String username){
        this.userMessage.setText(username);
    }

    public void setToken(UserParser user){
        if (user != null){
            String newToken = this.token.getText() + " " + "666";
            this.token.setText(newToken);
        }
    }

    public void setTokenOnline(String token){
        String newToken = this.token.getText() + " " + token;
        this.token.setText(newToken);
    }

    public void setGreeting(UserParser user){
        if (user != null){
            String newGreeting = this.greeting.getText() + " " + user.getUsername();
            this.greeting.setText(newGreeting);
        }
    }

    public void setGreetingOnline(String greeting){
        String newGreeting = this.greeting.getText() + " " + greeting;
        this.greeting.setText(newGreeting);
    }

    public void copyToClipboard(){
        String totalString = this.token.getText();
        String newString = totalString.replaceAll("Your token is ", "");
        StringSelection stringSelection = new StringSelection(newString);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.parameters = SpaceTraderApp.parameters;
    }
}
