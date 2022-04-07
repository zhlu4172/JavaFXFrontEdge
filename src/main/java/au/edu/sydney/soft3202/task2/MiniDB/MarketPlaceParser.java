package au.edu.sydney.soft3202.task2.MiniDB;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.xml.stream.FactoryConfigurationError;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Emma LU
 * @create 2022-04-04-8:11 pm
 */
public class MarketPlaceParser {
    public JSONArray getMarketplaces(String location){
        try{
            JSONParser jsonParser = new JSONParser();
            FileReader reader = new FileReader("src/main/resources/Plants/" + location + ".json");
            JSONObject plant = (JSONObject)  jsonParser.parse(reader);
            JSONArray marketplaces = (JSONArray) plant.get("marketplace");
            return marketplaces;
        } catch (IOException | ParseException e){
            return null;
        }
    }
}
