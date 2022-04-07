package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
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
 * @create 2022-04-03-5:18 pm
 */
public class ActiveLoansController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private Long credits;
    private String dueDate;
    private String id;
    private Long repaymentAmount;
    private String status;
    private String type;
    private String token;

    @FXML
    private Label state;
    @FXML
    private Label activeLoans;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username,Game.token);
        switch (button){
            case"Button[id=back, styleClass=button]'Back'":
                FXMLLoader availableLoanLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableLoans.fxml"));
                Parent availableLoanRoot = availableLoanLoader.load();
                AvailableLoanController availableLoanController = availableLoanLoader.getController();
                availableLoanController.setState(parameters);
//                UserParser userParser = new UserParser(Game.username,Game.token);
                if (parameters.equals("offline")){
                    availableLoanController.setAvailableLoans(userParser.getAvailableLoans());
                }else if(parameters.equals("online")){
                    String addingToken = "token=" + Game.token;
                    String uri = "https://api.spacetraders.io/types/loans?" + addingToken;
                    System.out.println(uri);
                    HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                            .GET()
                            .build();
                    HttpClient client = HttpClient.newBuilder().build();
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    String responseBody = response.body();
                    JsonObject userPost = getUserPost(responseBody);
                    System.out.println(userPost);
                    availableLoanController.setAvailableLoansOnline(userPost);
                }

                Scene availableLoanScene = new Scene(availableLoanRoot);
                Stage availableLoanStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                availableLoanStage.setScene(availableLoanScene);
                availableLoanStage.show();
                break;
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
            case "Button[id=info, styleClass=button]'Info'":
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

    public JSONObject hasActiveLoans(){
        JSONObject returnLoan = null;
        try{
            FileReader fileReader = new FileReader("src/main/resources/Loans/" + Game.username + "_loans.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject loans = (JSONObject)  jsonParser.parse(fileReader);
            returnLoan = loans;
        } catch(IOException | ParseException e){
//            return false;
        }
        return returnLoan;
    }

    public void getActiveLoans(){
        JSONObject loans = hasActiveLoans();
        if (loans == null){
            activeLoans.setText("There is no active loans currently.");
        }else{
            credits = (Long) loans.get("credits");

            JSONObject loan = (JSONObject) loans.get("loans");
            dueDate = (String) loan.get("due");
            id = (String) loan.get("id");
            repaymentAmount = (Long) loan.get("repaymentAmount");
            type = (String) loan.get("type");
            String newDetails = "Your credits are: " + credits + ".\nYour due date is: " + dueDate
                    + ".\nYour loan id is: " + id + ".\nYour repayment amount is: " + repaymentAmount
                    + ".\nYour loan type is: " + type;
            activeLoans.setText(newDetails);
        }
    }

    public void setActiveLoansOnline(){
        JsonObject userActiveLoans = getLoanString();
        JsonArray loansArray = userActiveLoans.get("loans").getAsJsonArray();
        System.out.println(loansArray + "hihi");
        String addingStr = "";
        if (loansArray.size() == 0){
            addingStr += "You don't have any loans due currently.";
        }else{
            for (int i = 0; i < loansArray.size(); i++){
                JsonObject loan = (JsonObject) loansArray.get(i);
                dueDate = loan.get("due").getAsString();
                id = loan.get("id").getAsString();
                repaymentAmount = loan.get("repaymentAmount").getAsLong();
                type = loan.get("type").getAsString();
                String newDetails = "Your due date is: " + dueDate
                        + ".\nYour loan id is: " + id + ".\nYour repayment amount is: " + repaymentAmount
                        + ".\nYour loan type is: " + type;
                addingStr += newDetails;
            }
        }
        activeLoans.setText(addingStr);
    }

    public void setToken(String token){
        this.token = token;
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public JsonObject getLoanString(){
        try{
            String addingToken = "token=" + Game.token;
            String uri = "https://api.spacetraders.io/my/loans?" + addingToken;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(statusCode);
            System.out.println(responseBody);
            return userPost;

        }catch(Exception e){
            e.printStackTrace();
            return null;
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
