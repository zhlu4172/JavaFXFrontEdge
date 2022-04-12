package au.edu.sydney.soft3202.task2.SceneController;


import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.*;

import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * @author Emma LU
 * @create 2022-04-01-1:27 pm
 */
public class DefaultController implements Initializable, Clickable{

    private String parameters;

    @FXML
    private Button registerButton;
    @FXML
    private Button LoginButton;
    @FXML
    private Label state;

    @Override
    public void buttonActionController(ActionEvent event) throws Exception {

        initialize(null,null);
        System.out.println(parameters + "hi");
        setState(parameters);
        String button = event.getSource().toString();
        System.out.println(button);
        switch (button){
            case "Button[id=register, styleClass=button]'Register'":
                FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("/AllPages/Register.fxml"));
                Parent registerRoot = registerLoader.load();
                RegisterController registerController = registerLoader.getController();
                registerController.setState(parameters);
                Scene registerScene = new Scene(registerRoot);
                Stage registerWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                registerWindow.setScene(registerScene);
                registerWindow.show();
                break;
            case "Button[id=login, styleClass=button]'Login'":
                FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/AllPages/Login.fxml"));
                Parent loginRoot = loginLoader.load();
                LoginController loginController = loginLoader.getController();
                loginController.setState(parameters);
                Scene loginScene = new Scene(loginRoot);
                Stage loginWindow = (Stage) (((Node) event.getSource()).getScene().getWindow());
                loginWindow.setScene(loginScene);
                loginWindow.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parameters = SpaceTraderApp.parameters;
    }

    public void setState(String state){
        String new_state = this.state.getText() + state;
        this.state.setText(new_state);
    }


}
