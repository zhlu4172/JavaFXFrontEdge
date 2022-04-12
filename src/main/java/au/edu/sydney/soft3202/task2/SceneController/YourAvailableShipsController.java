package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.SpaceTraderApp;
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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

/**
 * @author Emma LU
 * @create 2022-04-04-6:31 pm
 */
public class YourAvailableShipsController implements Clickable, Initializable {
    private String parameters;
    private String newState;

    @FXML
    private Label state;
    @FXML
    private Label ships;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        String button = event.getSource().toString();
        switch (button) {
            case "Button[id=back, styleClass=button]'Back'":
                if (parameters.equals("offline")){
                    FXMLLoader availableShipLoader = new FXMLLoader(getClass().getResource("/AllPages/AvailableShips.fxml"));
                    Parent availableShipRoot = availableShipLoader.load();
                    AvailableShipController availableShipController = availableShipLoader.getController();
                    availableShipController.setState(parameters);
                    Scene availableShipsScene = new Scene(availableShipRoot);
                    Stage availableShipsStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    availableShipsStage.setScene(availableShipsScene);
                    availableShipsStage.show();
                }else if(parameters.equals("online")){
                    FXMLLoader selectShipClassLoader = new FXMLLoader(getClass().getResource("/AllPages/SelectShipClass.fxml"));
                    Parent selectShipClassRoot = selectShipClassLoader.load();
                    SelectAvailableShipClassController selectAvailableShipClassController  = selectShipClassLoader.getController();
                    selectAvailableShipClassController.setState(parameters);
                    selectAvailableShipClassController.setChoices();
                    Scene selectShipClassScene = new Scene(selectShipClassRoot);
                    Stage selectShipClassStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    selectShipClassStage.setScene(selectShipClassScene);
                    selectShipClassStage.show();
                }
                break;
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
            case "Button[id=info, styleClass=button]'Info'":
                System.out.println("Hi");
                if (parameters.equals("online")){
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
        parameters = SpaceTraderApp.parameters;
    }

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }

    public void setMyAvailableShips(UserParser user) throws IOException, ParseException {
        JSONArray shipList = user.getMyAvailableShips(SpaceTraderApp.username);
        String newShipsDetails = "\n";
        if (shipList == null){
            this.ships.setText("You do not have any ships currently.\n");
            return;
        }
        for (int i = 0; i < shipList.size(); i++){
            JSONObject shipObject = (JSONObject) shipList.get(i);
            newShipsDetails = newShipsDetails + "The " + (i+1) + "th Ship: \n\n";
            newShipsDetails = newShipsDetails + "ID: " + shipObject.get("ID") + "\n";
            newShipsDetails = newShipsDetails + "Class: " + shipObject.get("class") + "\n";
            newShipsDetails = newShipsDetails + "Manufacturer: " + shipObject.get("manufacturer") + "\n";
            newShipsDetails = newShipsDetails + "MaxCargo: " + shipObject.get("maxCargo") + "\n";
            newShipsDetails = newShipsDetails + "Plating: " + shipObject.get("plating") + "\n";
            newShipsDetails = newShipsDetails + "Speed: " + shipObject.get("speed") + "\n";
            newShipsDetails = newShipsDetails + "Type: " + shipObject.get("type") + "\n";
            newShipsDetails = newShipsDetails + "Weapons: " + shipObject.get("weapons") + "\n\n\n";
        }
        this.ships.setText(newShipsDetails);
    }

    public String setYourAvailableShipsOnline(HttpResponse<String> response){

        if (response != null){
            System.out.println("hihi");
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            String addingDetails = "";
            JsonArray shipsArray = userPost.get("ships").getAsJsonArray();
            for (int i = 0; i < shipsArray.size(); i++){
                JsonObject gettingShip = shipsArray.get(i).getAsJsonObject();
                String gettingId = gettingShip.get("id").getAsString();
                String gettingLocation = gettingShip.get("location").getAsString();
                Long gettingXPos = gettingShip.get("x").getAsLong();
                Long gettingYPos = gettingShip.get("y").getAsLong();
                String gettingCargo = "";
                JsonArray cargoArray = gettingShip.get("cargo").getAsJsonArray();
                if (cargoArray == null || cargoArray.size() == 0){
                    gettingCargo = "empty";
                }else{
                    for (int j = 0; j < cargoArray.size(); j++){
                        JsonObject cargo = cargoArray.get(j).getAsJsonObject();
                        String gettingGood = cargo.get("good").getAsString();
                        Long gettingQuantity = cargo.get("quantity").getAsLong();
                        Long gettingTotalVolume = cargo.get("totalVolume").getAsLong();
                        String addingCargo = "Good is: " + gettingGood + "\nQuantity is: " + gettingQuantity +
                                "\nTotal Volume is: " + gettingTotalVolume;
                        gettingCargo += addingCargo;
                    }
                }
                Long spaceAvailable = gettingShip.get("spaceAvailable").getAsLong();
                String gettingType = gettingShip.get("type").getAsString();
                String gettingClass = gettingShip.get("class").getAsString();
                Long gettingMaxCargo = gettingShip.get("maxCargo").getAsLong();
                Long gettingLoadingSpeed = gettingShip.get("loadingSpeed").getAsLong();
                Long gettingSpeed = gettingShip.get("speed").getAsLong();
                String gettingManufacturer = gettingShip.get("manufacturer").getAsString();
                Long gettingPlating = gettingShip.get("plating").getAsLong();
                Long gettingWeapons = gettingShip.get("weapons").getAsLong();
                String addingDetail = "Your Ship ID: " + gettingId + "\nYour Ship Location: " + gettingLocation +
                        "\nYour Ship X position: " + gettingXPos + "\nYour ship Y position: " + gettingYPos +
                        "\nYour Cargo is: " + gettingCargo + "\nYour Ship space Available is: " + spaceAvailable +
                        "\nYour ship type is: " + gettingType + "\nYou ship class is: " + gettingClass +
                        "\nYour ship's Max Cargo is: " + gettingMaxCargo + "\nYour Ship's Loading speed is: " + gettingLoadingSpeed +
                        "\nYour ship's speed is: " + gettingSpeed + "\nYour ship's manufacturer is: " + gettingManufacturer +
                        "\nYour ship's plating is: " + gettingPlating + "\nYour ship's weapon number: " + gettingWeapons + "\n\n";
                addingDetails += addingDetail;
            }
            System.out.println(addingDetails);
            this.ships.setText(addingDetails);
            return "yes";
        }
        return null;
    }



    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setShipsOffline(){
        this.ships.setText("Ship type is: ZA-MK-I\n" +
                "Ship class is: MK-ll\n" +
                "The maximum Cargo is: 100\n" +
                "Loading Speed is: 100\n" +
                "Speed is: 2\n" +
                "Manufacturer is: Zetra\n" +
                "Plating is: 5\n" +
                "Weapons Number is: 5");
    }

    public String readFakeInfoFile(){
//        String reading_string = "";
//        try {
//            File myObj = new File("src/main/resources/UserListJson/info.txt");
//            Scanner myReader = new Scanner(myObj);
//            while (myReader.hasNextLine()) {
//                String data = myReader.nextLine() + "\n";
//                reading_string += data;
//            }
//            myReader.close();
//        } catch (FileNotFoundException e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
//        }
        String returnString = "Username: offline user\n" +
                "Your Ship count: 0\n" +
                "Your Joining Time: 2022-04-05T04:15:28.472Z\n" +
                "Your Current Credits: 200000";
        return returnString;
    }
}
