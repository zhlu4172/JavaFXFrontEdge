package au.edu.sydney.soft3202.task2.SceneController;

import au.edu.sydney.soft3202.task2.MiniDB.MarketPlaceParser;
import au.edu.sydney.soft3202.task2.MiniDB.UserParser;
import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.javafx.scene.PerspectiveCameraHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * @author Emma LU
 * @create 2022-04-04-8:01 pm
 */
public class ViewMarketPlaceByLocationController implements Clickable, Initializable {
    private String parameters;
    private String newState;
    private String location;

    @FXML
    private Label state;
    @FXML
    private Label marketplaces;
    @FXML
    private TextField goodInput;
    @FXML
    private TextField quantityInput;
    @FXML
    private TextField shipIdInput;
    @FXML
    private ChoiceBox goodChoices;
    @FXML
    private ChoiceBox ShipIdChoices;
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        initialize(null,null);
        setState(parameters);
        String button = event.getSource().toString();
        UserParser userParser = new UserParser(Game.username, Game.token);
        MarketPlaceParser marketPlaceParser = new MarketPlaceParser();
        switch (button){
            case"Button[id=back, styleClass=button]'Back'":
                if (parameters.equals("offline")) {
                    FXMLLoader marketplaceLoader = new FXMLLoader(getClass().getResource("/AllPages/MarketPlace.fxml"));
                    Parent marketPlaceRoot = marketplaceLoader.load();
                    MarketPlaceController marketPlaceController = marketplaceLoader.getController();
                    marketPlaceController.setState(parameters);
                    Scene marketplaceScene = new Scene(marketPlaceRoot);
                    Stage marketplaceStage = (Stage) (((Node) event.getSource()).getScene().getWindow());
                    marketplaceStage.setScene(marketplaceScene);
                    marketplaceStage.show();
                }else if(parameters.equals("online")){
                    FXMLLoader marketplaceOnlineLoader = new FXMLLoader(getClass().getResource("/AllPages/MarketPlaceOnline.fxml"));
                    Parent marketPlaceOnlineRoot = marketplaceOnlineLoader.load();
                    MarketPlaceController marketPlaceOnlineController = marketplaceOnlineLoader.getController();
                    marketPlaceOnlineController.setState(parameters);
                    marketPlaceOnlineController.setLocationChoices(marketPlaceOnlineController.getLocations());
                    Scene marketplaceOnlineScene = new Scene(marketPlaceOnlineRoot);
                    Stage marketplaceOnlineStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    marketplaceOnlineStage.setScene(marketplaceOnlineScene);
                    marketplaceOnlineStage.show();
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
            case "Button[id=purchase, styleClass=button]'Purchase'":
                if(parameters.equals("offline")){
                    userParser.purchaseGood(Game.username,goodInput.getText(), quantityInput.getText(),shipIdInput.getText(),location,marketPlaceParser);
                    FXMLLoader purchaseGoodSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseGoodsSuccess.fxml"));
                    Parent purchaseGoodSuccessRoot = purchaseGoodSuccessLoader.load();
                    PurchaseGoodsSuccessController purchaseGoodsSuccessController = purchaseGoodSuccessLoader.getController();
                    purchaseGoodsSuccessController.setState(parameters);
                    Scene purchaseGoodSuccessScene = new Scene(purchaseGoodSuccessRoot);
                    Stage purchaseGoodSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                    purchaseGoodSuccessStage.setScene(purchaseGoodSuccessScene);
                    purchaseGoodSuccessStage.show();
                }else if(parameters.equals("online")){
                    if(purchaseGood().equals("yes")){
                        FXMLLoader purchaseGoodSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/PurchaseGoodsSuccess.fxml"));
                        Parent purchaseGoodSuccessRoot = purchaseGoodSuccessLoader.load();
                        PurchaseGoodsSuccessController purchaseGoodsSuccessController = purchaseGoodSuccessLoader.getController();
                        purchaseGoodsSuccessController.setState(parameters);
                        Scene purchaseGoodSuccessScene = new Scene(purchaseGoodSuccessRoot);
                        Stage purchaseGoodSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        purchaseGoodSuccessStage.setScene(purchaseGoodSuccessScene);
                        purchaseGoodSuccessStage.show();
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(purchaseGood());
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
                    }
                }
                break;
            case "Button[id=sell, styleClass=button]'Sell'":
                if (parameters.equals("online")){
                    if(sellGood().equals("yes")){
                        FXMLLoader sellGoodSuccessLoader = new FXMLLoader(getClass().getResource("/AllPages/SellGoodsSuccess.fxml"));
                        Parent sellGoodSuccessRoot = sellGoodSuccessLoader.load();
                        SellGoodsSuccessController sellGoodsSuccessController = sellGoodSuccessLoader.getController();
                        sellGoodsSuccessController.setState(parameters);
                        Scene sellGoodSuccessScene = new Scene(sellGoodSuccessRoot);
                        Stage sellGoodSuccessStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        sellGoodSuccessStage.setScene(sellGoodSuccessScene);
                        sellGoodSuccessStage.show();
                    }else{
                        FXMLLoader errorPageLoader = new FXMLLoader(getClass().getResource("/AllPages/ErrorPage.fxml"));
                        Parent errorPageRoot = errorPageLoader.load();
                        ErrorPageController errorPageController = errorPageLoader.getController();
                        errorPageController.setState(parameters);
                        errorPageController.setErrormessage(sellGood());
//                purchaseShipSuccessfullyController.setDetails();
                        Scene errorPageScene = new Scene(errorPageRoot);
                        Stage errorPageStage = (Stage)(((Node) event.getSource()).getScene().getWindow());
                        errorPageStage.setScene(errorPageScene);
                        errorPageStage.show();
                    }
                }
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

    public void setState(String state){
        newState = this.state.getText() + state;
        this.state.setText(newState);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parameters = Game.parameters;
    }

    public void setMarketPlacesText(MarketPlaceParser marketPlaceParser, String location){
        JSONArray marketplacesArray = marketPlaceParser.getMarketplaces(location);
        String marketplacesDetails = "\n";
        for (int i = 0; i < marketplacesArray.size(); i++){
            JSONObject shipObject = (JSONObject) marketplacesArray.get(0);
            marketplacesDetails = marketplacesDetails + "The " + (i+1) + "th Marketplace: \n\n";
            marketplacesDetails = marketplacesDetails + "Price Per Unit: " + shipObject.get("pricePerUnit") + "\n";
            marketplacesDetails = marketplacesDetails + "Purchase Price Per Unit: " + shipObject.get("purchasePricePerUnit") + "\n";
            marketplacesDetails = marketplacesDetails + "Available Quantity: " + shipObject.get("quantityAvailable") + "\n";
            marketplacesDetails = marketplacesDetails + "Sell Price Per Unit: " + shipObject.get("sellPricePerUnit") + "\n";
            marketplacesDetails = marketplacesDetails + "Spread: " + shipObject.get("spread") + "\n";
            marketplacesDetails = marketplacesDetails + "Goods Symbol: " + shipObject.get("symbol") + "\n";
            marketplacesDetails = marketplacesDetails + "Volumn Per Unit: " + shipObject.get("volumePerUnit") + "\n\n\n";
        }
        this.marketplaces.setText(marketplacesDetails);
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setMarketplacesTextOnline(HttpResponse<String> response){
        String responseBody = response.body();
        JsonObject userPost = getUserPost(responseBody);
        try{
            String addingDetails = "";
            JsonArray marketplace = userPost.get("marketplace").getAsJsonArray();
            for (int i = 0; i < marketplace.size(); i++){
                JsonObject one = (JsonObject) marketplace.get(i);
                String symbol = one.get("symbol").getAsString();
                goodChoices.getItems().add(symbol);
                Long volumePerUnit = one.get("volumePerUnit").getAsLong();
                Long pricePerUnit = one.get("pricePerUnit").getAsLong();
                Long spread = one.get("spread").getAsLong();
                Long purchasePricePerUnit = one.get("purchasePricePerUnit").getAsLong();
                Long sellPricePerUnit = one.get("sellPricePerUnit").getAsLong();
                Long quantityAvailable = one.get("quantityAvailable").getAsLong();
                String addingDetail = "Good symbol is " + symbol + "\nVolume per unit is " + volumePerUnit +
                        "\nPrice per unit is " + pricePerUnit + "\nSpread is " + spread +
                        "\nPurchase Price Per unit is " + purchasePricePerUnit +
                        "\nSell Price Per unit is " + sellPricePerUnit +
                        "\nQuantity Available is " + quantityAvailable + "\n\n";
                addingDetails += addingDetail;
            }
            this.marketplaces.setText(addingDetails);
        }catch (Exception e){
            JsonObject one = userPost.get("marketplace").getAsJsonObject();
            String symbol = one.get("symbol").getAsString();
            Long volumePerUnit = one.get("volumePerUnit").getAsLong();
            Long pricePerUnit = one.get("pricePerUnit").getAsLong();
            Long spread = one.get("spread").getAsLong();
            Long purchasePricePerUnit = one.get("purchasePricePerUnit").getAsLong();
            Long sellPricePerUnit = one.get("sellPricePerUnit").getAsLong();
            Long quantityAvailable = one.get("quantityAvailable").getAsLong();
            String addingDetail = "Good symbol is " + symbol + "\nVolume per unit is " + volumePerUnit +
                    "\nPrice per unit is " + pricePerUnit + "\nSpread is " + spread +
                    "\nPurchase Price Per unit is " + purchasePricePerUnit +
                    "\nSell Price Per unit is " + sellPricePerUnit +
                    "\nQuantity Available is " + quantityAvailable + "\n\n";
            this.marketplaces.setText(addingDetail);
        }
    }

    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public void setShipChoices(){
        try{
            String addingToken = "token=" + Game.token;
            String uri = "https://api.spacetraders.io/my/ships?" + addingToken;
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            int statusCode = response.statusCode();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            JsonArray shipArray = userPost.get("ships").getAsJsonArray();
            for (int i = 0; i < shipArray.size(); i++){
                JsonObject ship = shipArray.get(i).getAsJsonObject();
                ShipIdChoices.getItems().add(ship.get("id").getAsString());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String purchaseGood(){
        try{
            String uri = "https://api.spacetraders.io/my/purchase-orders?token=" + Game.token + "&shipId=" + ShipIdChoices.getValue()
                    + "&good=" + goodChoices.getValue() + "&quantity=" + quantityInput.getText();
            System.out.println(uri + "hi");
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(responseBody);
            System.out.println(statusCode);
            if (statusCode == 201){
                return "yes";
            }else{
                return userPost.get("error").getAsJsonObject().get("message").getAsString();
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String sellGood(){
        try{
            String uri = "https://api.spacetraders.io/my/sell-orders?token=" + Game.token + "&shipId=" + ShipIdChoices.getValue()
                    + "&good=" + goodChoices.getValue() + "&quantity=" + quantityInput.getText();
            System.out.println(uri + "hi");
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(responseBody);
            System.out.println(statusCode);
            if (statusCode == 201){
                return "yes";
            }else{
                return userPost.get("error").getAsJsonObject().get("message").getAsString();
            }
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
