package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
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
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-03-2:30 pm
 */
public class ClaimNewLoanController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private String gettingType;

    @FXML
    private Label state;
    @FXML
    private TextField type;

    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
//        setState(parameters);
        String button = event.getSource().toString();
        switch (button){
            case"Button[id=confirm, styleClass=button]'Confirm'":
                if (parameters.equals("offline")){
                    FXMLLoader claimLoanSuccessfulLoader = new FXMLLoader(getClass().getResource("/AllPages/ClaimLoanSuccess.fxml"));
                    Parent claimLoanSuccessfulRoot = claimLoanSuccessfulLoader.load();
                    ClaimLoanSuccessfulController claimLoanSuccessfulController = claimLoanSuccessfulLoader.getController();
                    UserParser userParser = new UserParser(Game.username,Game.token);
//                    userParser.activateLoan(Game.username);
//                    userParser.updateCreditsJson(Game.username, (long)200000);
                    claimLoanSuccessfulController.setState(parameters);
                    claimLoanSuccessfulController.setDetails();
                    Scene claimLoanSuccessfulScene = new Scene(claimLoanSuccessfulRoot);
                    Stage claimLoanSuccessfulStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    claimLoanSuccessfulStage.setScene(claimLoanSuccessfulScene);
                    claimLoanSuccessfulStage.show();
                }else if(parameters.equals("online")){
                    setGettingType();
                    HttpResponse<String> response = claimNewLoan();
                    if (checkStatusCode(response).equals("yes")){
                        FXMLLoader claimLoanSuccessfulLoader = new FXMLLoader(getClass().getResource("/AllPages/ClaimLoanSuccess.fxml"));
                        Parent claimLoanSuccessfulRoot = claimLoanSuccessfulLoader.load();
                        ClaimLoanSuccessfulController claimLoanSuccessfulController = claimLoanSuccessfulLoader.getController();
                        String responseBody = response.body();
                        JsonObject userPost = getUserPost(responseBody);
                        claimLoanSuccessfulController.setDetailsOnline(userPost);
                        System.out.println(gettingType);
                        Scene claimLoanSuccessfulScene = new Scene(claimLoanSuccessfulRoot);
                        Stage claimLoanSuccessfulStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        claimLoanSuccessfulStage.setScene(claimLoanSuccessfulScene);
                        claimLoanSuccessfulStage.show();
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(checkStatusCode(response));
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
                    }
                }
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
            case"Button[id=back, styleClass=button]'Back'":
                FXMLLoader availableLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableLoans.fxml"));
                Parent availableLoanRoot = availableLoanLoader.load();
                AvailableLoanController availableLoanController = availableLoanLoader.getController();
                availableLoanController.setState(parameters);
                System.out.println(Game.token);
                UserParser userParser2 = new UserParser(Game.username,Game.token);
                availableLoanController.setAvailableLoans(userParser2.getAvailableLoans());
                Scene availableLoanScene = new Scene(availableLoanRoot);
                Stage availableLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                availableLoanStage.setScene(availableLoanScene);
                availableLoanStage.show();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.parameters = Game.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public void setGettingType(){
        gettingType = type.getText();
    }

    public HttpResponse<String> claimNewLoan() throws IOException, InterruptedException, URISyntaxException {
        String uri = "https://api.spacetraders.io/my/loans?token=" + Game.token + "&type=" + gettingType;
        System.out.println(uri + "hi");
        HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpClient client = HttpClient.newBuilder().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        int statusCode = response.statusCode();
        String responseBody = response.body();
        System.out.println(responseBody);
        System.out.println(statusCode);
        JsonObject userPost = getUserPost(responseBody);
        System.out.println("UserPost: -------" + userPost);
        return response;

    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public String checkStatusCode(HttpResponse<String> response){
        try{
            int statusCode = response.statusCode();
            String responseBody = response.body();
            JsonObject userPost = getUserPost(responseBody);
            if (statusCode != 201){
                return userPost.get("error").getAsJsonObject().get("message").getAsString();
            }else{
                return "yes";
            }
        }catch(Exception e){
            return "no";
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
