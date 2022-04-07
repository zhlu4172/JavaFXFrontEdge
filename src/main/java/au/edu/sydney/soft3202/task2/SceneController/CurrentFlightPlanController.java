package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.MarketPlaceParser;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-06-4:03 pm
 */
public class CurrentFlightPlanController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private Label current;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username, Game.token);
        MarketPlaceParser marketPlaceParser = new MarketPlaceParser();
        switch (button) {
            case "Button[id=back, styleClass=button]'Back'":
                FXMLLoader createFlightPlanLoader = new FXMLLoader(getClass().getResource("/AllPages/CreateNewFlightPlan.fxml"));
                Parent createFlightRoot = createFlightPlanLoader.load();
                CreateFlightPlanController createFlightPlanController = createFlightPlanLoader.getController();
                createFlightPlanController.setState(parameters);
                if(parameters.equals("online")){
                    createFlightPlanController.setLocationChoices(createFlightPlanController.getLocations());
                    createFlightPlanController.setShipChoices();
                }
                Scene createFlightScene = new Scene(createFlightRoot);
                Stage createFlightStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                createFlightStage.setScene(createFlightScene);
                createFlightStage.show();
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

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setCurrent(List<JsonObject> jsonObjectList){
        String addingDetails = "";
        for (int i = 0; i < jsonObjectList.size(); i++){
            JsonObject jsonObject = jsonObjectList.get(i);
            System.out.println(jsonObject + "\n-------------");
            JsonObject flightPlan = jsonObject.get("flightPlan").getAsJsonObject();
            String arrivesAt = flightPlan.get("arrivesAt").getAsString();
            String createdAt = flightPlan.get("createdAt").getAsString();
            String departure = flightPlan.get("departure").getAsString();
            String destination = flightPlan.get("destination").getAsString();
            Long distance = flightPlan.get("distance").getAsLong();
            Long fuelConsumed = flightPlan.get("fuelConsumed").getAsLong();
            Long fuelRemaining = flightPlan.get("fuelRemaining").getAsLong();
            String id = flightPlan.get("id").getAsString();
            String shipId = flightPlan.get("shipId").getAsString();
            String terminatedAt = "";
            if (flightPlan.get("terminated") == null){
                terminatedAt = "not decided";
            }else{
                terminatedAt = flightPlan.get("terminatedAt").getAsString();
            }
            Long timeRemainingInSeconds = flightPlan.get("timeRemainingInSeconds").getAsLong();
            String addingDetail = "Your flight arrives at " + arrivesAt + "\nYour flight created at " + createdAt +
                    "\nYour flight's departure is " + departure + "\nYour flight's destination is " + destination +
                    "\nYour flight's distance is " + distance + "\nThe fuel your flight consumes is " + fuelConsumed +
                    "\nYour remaining fuel is " + fuelRemaining + "\nYour flight id is " + id +
                    "\nYour flight's ship id is " + shipId + "\nYour Flight termination is " + terminatedAt +
                    "\nYour flight time remaining in seconds is " + timeRemainingInSeconds + "\n\n";
            addingDetails += addingDetail;
        }

        this.current.setText(addingDetails);
    }

    public List<String> getFlightPlanId(){
        List<String> idList = new ArrayList<>();
        try{
            String addingToken = "?token=" + Game.token;
            String uri = "https://api.spacetraders.io/systems/OE/flight-plans" + addingToken ;
            System.out.println(uri);
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            JsonArray flightPlans = userPost.get("flightPlans").getAsJsonArray();
            if (flightPlans.size() == 0){
                return idList;
            }
            for (int i = 0; i < flightPlans.size(); i++){
                JsonObject flightPlan = flightPlans.get(i).getAsJsonObject();
                String username = flightPlan.get("username").getAsString();
                if (username.equals(Game.username) && !idList.contains(flightPlan.get("id").getAsString())){
                    idList.add(flightPlan.get("id").getAsString());
                }
            }
            System.out.println(idList);
            return idList;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void setCurrentOffline(){
        this.current.setText("Your flight arrives at 2022-04-07T07:48:00.346Z\n" +
                "Your flight created at 2022-04-07T07:46:45.349Z\n" +
                "Your flight's departure is OE-PM\n" +
                "Your flight's destination is OE-KO\n" +
                "Your flight's distance is 30\n" +
                "The fuel your flight consumes is 7\n" +
                "Your remaining fuel is 6\n" +
                "Your flight id is ch1op492e146831715s6jknorhvo\n" +
                "Your flight's ship id is cl1m00oiy64925115s6mr Glohn\n" +
                "Your Flight termination is not decided\n" +
                "Your flight time remaining in seconds is 69");
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
