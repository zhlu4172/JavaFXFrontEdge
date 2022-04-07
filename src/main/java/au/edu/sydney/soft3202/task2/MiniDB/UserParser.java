package au.edu.sydney.soft3202.task2.MiniDB;

import au.edu.sydney.soft3202.task2.System.Game;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import wholepackage.UserPost;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import java.io.*;

/**
 * @author Emma LU
 * @create 2022-04-01-10:35 pm
 */
public class UserParser {
    public String username;
    public String token;
    public int id;
    public UserParser(String username, String token){
        this.username = username;
        this.token = token;
    }

    public void createNewUser(String username) throws IOException {
        File newUser = new File("src/main/resources/UserListJson/" + username + ".json");
        FileWriter fileWriter = new FileWriter(newUser);
        fileWriter.write(initiateJsonFile(username));
        fileWriter.close();
//        printWriter.close();
    }

    public boolean login(String username, String token) throws Exception{
        try{
            JsonParser jsonParser = new JsonParser();
            FileReader fileReader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
            JsonObject user = (JsonObject) jsonParser.parse(fileReader);
            this.token = getRightToken(user.get("token").toString());
            System.out.println(token);
            System.out.println(this.token);
            if (this.token.equals(token)){
                System.out.println("hihi");
                return true;
            }else{
                return false;
            }
        }catch(FileNotFoundException e){

        }
        return false;
    }

    public String initiateJsonFile(String username){
        String fileContents = "{\n" +
                "  \"credits\": 0,\n" +
                "  \"joinedAt\": \"2021-05-13T02:29:41.741Z\",\n" +
                "  \"shipCount\": 0,\n" +
                "  \"structureCount\": 0,\n" +
                "  \"username\": \"" + username +"\",\n" +
                "  \"token\": \"666\"\n" +
                "}";
        this.token = "666";
        return fileContents;
    }

    public String getToken(){
        return token;
    }

    public String getUsername(){
        return username;
    }

    public String getRightToken(String token){
        int length = token.length();
        String newToken = "";
        for (int i = 0; i < length; i++){
            if (i != 0 && i != length - 1){
                newToken += token.charAt(i);
            }
        }
        return newToken;
    }

    public JSONObject getAvailableLoans() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/Loans/loans.json");
        JSONObject loans = (JSONObject)  jsonParser.parse(reader);
        JSONArray loanArray = (JSONArray) loans.get("loans");
        JSONObject loan = (JSONObject) loanArray.get(0);
        return loan;
    }

    public void activateLoan(String username) throws IOException, ParseException {
        initiateLoanJson();
    }

    public void initiateLoanJson() throws IOException, ParseException {
        FileWriter fileWriter = new FileWriter("src/main/resources/Loans/" + username + "_loans.json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String adding = "{\n" +
                "  \"credits\": 200000,\n" +
                "  \"loans\": \n" +
                "    {\n" +
                "      \"due\": \"2021-05-15T02:32:43.269Z\",\n" +
                "      \"id\": \"ckoma153c0060zbnzquw2xa29\",\n" +
                "      \"repaymentAmount\": 280000,\n" +
                "      \"status\": \"CURRENT\",\n" +
                "      \"type\": \"STARTUP\"\n" +
                "    }\n" +
                "}";
        printWriter.write(adding);
        printWriter.close();
        fileWriter.close();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        FileReader fileReader = new FileReader("src/main/resources/Loans/" + username + "_loans.json");
        JSONParser jsonParser = new JSONParser();
        JSONObject loans = (JSONObject)  jsonParser.parse(fileReader);
        JSONObject loan = (JSONObject) loans.get("loans");
        String dueDate = String.valueOf(timestamp);
        String duedate = loan.get("due").toString();
//        JSONObject loanDueDate = (JSONObject) loan.get("due");
        System.out.println(dueDate);
        loan.remove("due");
        loan.put("due",dueDate);
        System.out.println(loan.get("due"));
        overWriteJsonFile(dueDate,null,null,null,null);
    }

    public void overWriteJsonFile(String due, String id, String repaymentAmount, String status, String type) throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/Loans/" + username + "_loans.json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        String adding = "{\n" +
                "  \"credits\": 200000,\n" +
                "  \"loans\": \n" +
                "    {\n" +
                "      \"due\": \"" + due + "\",\n" +
                "      \"id\": \"ckoma153c0060zbnzquw2xa29\",\n" +
                "      \"repaymentAmount\": 280000,\n" +
                "      \"status\": \"CURRENT\",\n" +
                "      \"type\": \"STARTUP\"\n" +
                "    }\n" +
                "}";
        printWriter.write(adding);
        printWriter.close();
        fileWriter.close();
    }

    public JSONObject getAvailableShips() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/Ships/ships.json");
        JSONObject ships = (JSONObject)  jsonParser.parse(reader);
        return ships;
    }

    public void updateCreditsJson(String username, Long credits) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
        JSONObject user = (JSONObject)  jsonParser.parse(reader);
        Long oldCredits = (Long) user.get("credits");
        user.remove("credits");
        user.put("credits", oldCredits + credits);
        String new_writing = user.toString();
        FileWriter fileWriter = new FileWriter("src/main/resources/UserListJson/" + username + ".json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write(new_writing);
        printWriter.close();
        fileWriter.close();
    }

    public String purchaseShip(String plantStr, String shipStr) throws IOException, ParseException {
        JSONObject ships = getAvailableShips();
        JSONArray shipArray = (JSONArray) ships.get("shipListings");
        if (plantStr.isEmpty()){
            return "You Location should not be empty!";
        }
        if (shipStr.isEmpty()){
            return "You ship should not be empty!";
        }
        boolean hasMatching = false;
        for (int i = 0; i < shipArray.size(); i++){
            JSONObject ship = (JSONObject) shipArray.get(i);
            System.out.println(ship);
            String shipType = (String) ship.get("type");
            JSONArray locations = (JSONArray) ship.get("purchaseLocations");
            Long index = Long.valueOf(-1);
            for (int j = 0; j < locations.size(); j++){
                if (((JSONObject)locations.get(j)).get("location").toString().equals(plantStr)){
                    index = (Long) ((JSONObject)locations.get(j)).get("price");
                    System.out.println(index);
                    if(!checkCredits(Game.username, index)){
                        System.out.println("bought failed!");
                        return "You don't have enough credits!";
                    }
                }
            }
            if (index < 0) {
                return "No matching ships!";
            }
            if (shipType.equals(shipStr)){
                System.out.println(index);
                updateCreditsJson(Game.username, -index);
                updateShipsBought(ship);
                hasMatching = true;
                return "yes";
//                String adding = "";

            }
        }
        if (!hasMatching){
            return "No Matching ships!";
        }
        return "no";

    }

    public void updateShipsBought(JSONObject input) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
        JSONObject user = (JSONObject)  jsonParser.parse(reader);

        Long shipCount = (Long) user.get("shipCount");
        shipCount += 1;
        input.put("ID",shipCount);
        user.remove("shipCount");
        user.put("shipCount", shipCount);
        input.remove("purchaseLocations");
        if(user.get("ships") == null){
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(input);
            user.put("ships", jsonArray);
            System.out.println(user);
        }else{
            JSONArray jsonArray = (JSONArray) user.get("ships");
            jsonArray.add(input);
            JSONArray newJsonArray = jsonArray;
            user.remove(jsonArray);
            user.put("ships", newJsonArray);
        }
        FileWriter fileWriter = new FileWriter("src/main/resources/UserListJson/" + username + ".json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write(user.toString());
        printWriter.close();
        fileWriter.close();
    }

    public boolean checkCredits(String username, Long price) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
        JSONObject user = (JSONObject)  jsonParser.parse(reader);
        Long credits = (Long) user.get("credits");
        if (credits < price){
            return false;
        }else{
            return true;
        }
    }

    public JSONArray getMyAvailableShips(String username) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
        JSONObject user = (JSONObject)  jsonParser.parse(reader);
        JSONArray shipList = (JSONArray) user.get("ships");
        return shipList;
    }

    public String purchaseGood(String username, String good, String quantity, String shipId, String location, MarketPlaceParser marketPlaceParser) throws IOException, ParseException {
        JSONArray goodsArray = marketPlaceParser.getMarketplaces(location);
        if (goodsArray == null){
            return "No things to buy!";
        }
        if(!checkHasShip(username, shipId)){
            return "You do not have the matching ship!";
        }
        boolean canFind = false;
        for (int i = 0; i < goodsArray.size(); i++){
            JSONObject goodDetail = (JSONObject) goodsArray.get(i);
            String goodSymbol = (String) goodDetail.get("symbol");
            Long gettingQuantity = (Long)goodDetail.get("quantityAvailable");
            Long sellPricePerUnit = (Long) goodDetail.get("sellPricePerUnit");
            Long totalBuyingPrice = sellPricePerUnit * Long.parseLong(quantity);
            Long havingQuantity = Long.parseLong(quantity);
            if (!checkCredits(username, -totalBuyingPrice)){
                return "Do not have enough credits to buy";
            }
            if (goodSymbol.equals(good)){
                canFind = true;
                if (!checkQuantity(gettingQuantity, havingQuantity)){
                    return "Do not have enough goods for you to buy!";
                }else{
                    addingGoodsToJsonFile(username, shipId, quantity,goodDetail, good);
                    return "yes";
                }
            }
        }
        if (!canFind){
            return "No matching good to buy in this place.";
        }
        return "no";
    }

    public boolean checkHasShip(String username, String shipId) throws IOException, ParseException {
        Long id = Long.parseLong(shipId);
        JSONArray shipsArray = getMyAvailableShips(username);
        if (shipsArray == null || shipsArray.isEmpty()){
            return false;
        }
        for (int i = 0; i < shipsArray.size(); i++){
            JSONObject ship = (JSONObject) shipsArray.get(i);
            Long gettingId = (Long) ship.get("ID");
            if (gettingId == id){
                return true;
            }
        }
        return false;
    }

    public boolean checkQuantity(Long havingQuantity, Long needingQuantity){
        if(havingQuantity < needingQuantity){
            return false;
        }else{
            return true;
        }
    }


    public void addingGoodsToJsonFile(String username, String shipId, String quantity, JSONObject goods, String goodStr) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("src/main/resources/UserListJson/" + username + ".json");
        JSONObject user = (JSONObject)  jsonParser.parse(reader);
        Long volumePerUnit = (Long) goods.get("volumePerUnit");
        Long sellPricePerUnit = (Long) goods.get("sellPricePerUnit");
        Long totalVolume = volumePerUnit * Long.parseLong(quantity);
//        Long totalBuyingPrice = sellPricePerUnit * Long.parseLong(quantity);
        goods.put("total", totalVolume);
        goods.put("quantity", quantity);
        goods.remove("volumePerUnit");
        goods.remove("sellPricePerUnit");
        goods.remove("spread");
        goods.remove("quantityAvailable");
        if(user.get("orders") == null){
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(goods);
            user.put("order", jsonArray);
        }else{
            JSONArray jsonArray = (JSONArray) user.get("orders");
            jsonArray.add(goods);
            JSONArray newJsonArray = jsonArray;
            user.remove(jsonArray);
            user.put("orders", newJsonArray);
        }

        JSONArray ships = (JSONArray)user.get("ships");
        for (int i = 0; i < ships.size(); i++){
            JSONObject ship = (JSONObject) ships.get(i);
            Long id = (Long) ship.get("ID");
            if (id == Long.parseLong(shipId)){
                JSONObject addingObject = new JSONObject();
                addingObject.put("good", goodStr);
                addingObject.put("quantity",Long.parseLong(quantity));
                addingObject.put("totalVolume", totalVolume);
                if(user.get("cargo") == null){
                    JSONArray jsonArray = new JSONArray();
                    jsonArray.add(addingObject);
                    ship.put("cargo", jsonArray);
                }else{
                    JSONArray jsonArray = (JSONArray) user.get("cargo");
                    jsonArray.add(addingObject);
                    JSONArray newJsonArray = jsonArray;
                    user.remove(jsonArray);
                    user.put("cargo", newJsonArray);
                }
            }
        }
        System.out.println(user);

        FileWriter fileWriter = new FileWriter("src/main/resources/UserListJson/" + username + ".json");
        PrintWriter printWriter = new PrintWriter(fileWriter);
        printWriter.write(user.toString());
        printWriter.close();
        fileWriter.close();
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setToken(String token){
        this.token = token;
    }














}
