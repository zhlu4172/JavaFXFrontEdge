package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.System.Game;
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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

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

/**
 * @author Emma LU
 * @create 2022-04-03-4:32 pm
 */
public class ClaimLoanSuccessfulController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private Long credits;
    private String dueDate;
    private String id;
    private Long repaymentAmount;
    private String status;
    private String type;
    @FXML
    private Label state;

    @FXML
    private Label details;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button) {
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
                FXMLLoader newLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/ClaimNewLoan.fxml"));
                Parent claimNewLoanRoot = newLoanLoader.load();
                ClaimNewLoanController claimNewLoanController = newLoanLoader.getController();
                claimNewLoanController.setState(parameters);
                Scene claimNewLoanScene = new Scene(claimNewLoanRoot);
                Stage claimNewLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                claimNewLoanStage.setScene(claimNewLoanScene);
                claimNewLoanStage.show();
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
                }
                else if(parameters.equals("offline")){
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

    public void setDetails() throws IOException, ParseException {
        FileReader fileReader = new FileReader("src/main/resources/Loans/" + Game.username + "_loans.json");
        JSONParser jsonParser = new JSONParser();
        JSONObject loans = (JSONObject)  jsonParser.parse(fileReader);
        credits = (Long) loans.get("credits");

        JSONObject loan = (JSONObject) loans.get("loans");
        dueDate = (String) loan.get("due");
        id = (String) loan.get("id");
        repaymentAmount = (Long) loan.get("repaymentAmount");
        type = (String) loan.get("type");
        String newDetails = "Your credits are: " + credits + ".\nYour due date is: " + dueDate
                + ".\nYour loan id is: " + id + ".\nYour repayment amount is: " + repaymentAmount
                + ".\nYour loan type is: " + type;
        details.setText(newDetails);
    }

    public void setDetailsOnline(JsonObject gettingDetails){
        credits = gettingDetails.get("credits").getAsLong();
        JsonObject loansObject = gettingDetails.get("loan").getAsJsonObject();
        id = loansObject.get("id").getAsString();
        dueDate = loansObject.get("due").getAsString();
        repaymentAmount = loansObject.get("repaymentAmount").getAsLong();
        type = loansObject.get("type").getAsString();
        status = loansObject.get("status").getAsString();
        String newDetails = "Your credits are: " + credits + ".\nYour due date is: " + dueDate
                + ".\nYour loan id is: " + id + ".\nYour repayment amount is: " + repaymentAmount
                + ".\nYour loan type is: " + type;
        details.setText(newDetails);
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
