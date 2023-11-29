package utils;

import org.json.simple.JSONObject;

import java.util.Map;

import static utils.JsonUtils.readJson;
import static utils.JsonUtils.readJsonFromFile;

public class ConfigUtil {
    public static String loginURL;


    public static void updateData(String country) {
        String countryConfigLocation= System.getProperty("CountryJson");
        JSONObject countryData = (JSONObject) readJsonFromFile(countryConfigLocation).get(country.toLowerCase());
        loginURL = countryData.get("loginURL").toString();
    }
}
