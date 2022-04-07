package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author Emma LU
 * @create 2022-04-03-1:43 pm
 */
public class AvailableLoanController implements Clickable, Initializable {
    private String amount;
    private boolean collateralRequired;
    private Long rate;
    private Long termInDays;
    private String type;
    private String parameters;
    private String newState;
    private String token;
    @FXML
    private Label state;
    @FXML
    private Label availableloans;

    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
//        setState(parameters);
        String button = event.getSource().toString();
        switch (button){
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
                if (parameters.equals("offline")){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                    Parent loginSuccessRoot = loader.load();
                    LoginSuccessController loginSuccessController = loader.getController();
                    loginSuccessController.setGreeting(Game.username);
                    loginSuccessController.setState(parameters);
                    Scene loginSuccessScene = new Scene(loginSuccessRoot);
                    Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    loginSuccessStage.setScene(loginSuccessScene);
                    loginSuccessStage.show();
                }else if(parameters.equals("online")){
                    try{
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
                        String gettingUsername = userPost.get("user").getAsJsonObject().get("username").getAsString();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/AllPages/LoginSuccessfulMainPage.fxml"));
                        Parent loginSuccessRoot = loader.load();
                        LoginSuccessController loginSuccessController = loader.getController();
                        loginSuccessController.setUsername(gettingUsername);
                        loginSuccessController.setToken(Game.token);
                        loginSuccessController.setGreeting(gettingUsername);
                        loginSuccessController.setState(parameters);
                        Scene loginSuccessScene = new Scene(loginSuccessRoot);
                        Stage loginSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        loginSuccessStage.setScene(loginSuccessScene);
                        loginSuccessStage.show();
                    }catch(Exception exception){
                        exception.printStackTrace();
                    }
                }

                break;
            case"Button[id=claimNewLoan, styleClass=button]'Claim New Loans'":
                FXMLLoader newLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/ClaimNewLoan.fxml"));
                Parent claimNewLoanRoot = newLoanLoader.load();
                ClaimNewLoanController claimNewLoanController = newLoanLoader.getController();
                claimNewLoanController.setState(parameters);
                Scene claimNewLoanScene = new Scene(claimNewLoanRoot);
                Stage claimNewLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                claimNewLoanStage.setScene(claimNewLoanScene);
                claimNewLoanStage.show();
                break;
            case"Button[id=activeloans, styleClass=button]'View Active Loans'":
                FXMLLoader activeLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/ActiveLoans.fxml"));
                Parent activeLoanRoot = activeLoanLoader.load();
                ActiveLoansController activeLoansController = activeLoanLoader.getController();
                activeLoansController.setState(parameters);
                if (parameters.equals("offline")){
                    activeLoansController.getActiveLoans();
                }else if(parameters.equals("online")){
                    activeLoansController.setActiveLoansOnline();
                }
                Scene activeLoanScene = new Scene(activeLoanRoot);
                Stage activeLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                activeLoanStage.setScene(activeLoanScene);
                activeLoanStage.show();
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

    public void setAvailableLoans(JSONObject loans) throws IOException, ParseException {
        amount = loans.get("amount").toString();
        collateralRequired = (boolean) loans.get("collateralRequired");
        rate = (Long)loans.get("rate");
        termInDays = (Long)loans.get("termInDays");
        type = (String)loans.get("type");
        String newStr = "Your available loan amount is " + amount + ".\nYour required collateral is "
                + collateralRequired + ".\nYour loan rate is " + rate + ".\nYour term in days is "
                + termInDays + ".\nYour loan type is " + type + ".\n";
        availableloans.setText(newStr);
    }

    public void setAvailableLoansOnline(JsonObject loans) throws IOException, ParseException {
        JsonArray loanArray = loans.get("loans").getAsJsonArray();
        String adding = "";
        for (int i = 0; i < loanArray.size(); i++){
            amount = ((JsonObject)loanArray.get(i)).get("amount").getAsString();
            collateralRequired =  ((JsonObject)loanArray.get(i)).get("collateralRequired").getAsBoolean();
            rate =  ((JsonObject)loanArray.get(i)).get("rate").getAsLong();
            termInDays =  ((JsonObject)loanArray.get(i)).get("termInDays").getAsLong();
            type =  ((JsonObject)loanArray.get(i)).get("type").getAsString();
            String newStr = "Your available loan amount is " + amount + ".\nYour required collateral is "
                    + collateralRequired + ".\nYour loan rate is " + rate + ".\nYour term in days is "
                    + termInDays + ".\nYour loan type is " + type + ".\n";
            adding += newStr;
        }

        availableloans.setText(adding);
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return this.token;
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
