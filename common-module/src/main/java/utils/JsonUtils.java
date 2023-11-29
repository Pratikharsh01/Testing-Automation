package utils;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtils {

    public static Object readJson(String jsonString, String path) {
        Object returnObj = "";
        try {
            Object document = Configuration.defaultConfiguration().jsonProvider().parse(jsonString);
            returnObj = JsonPath.read(document, path);
        } catch (PathNotFoundException pnfe) {
            pnfe.printStackTrace();
        }
        return returnObj;
    }

    public static Object readJson(Response response, String path) {
        return readJson(response.asPrettyString(), path);
    }

    public static Map<String, Object> readJsonFromFile(String fileLocation) {
        JSONParser jsonParser = new JSONParser();
        Map<String, Object> data = new HashMap<>();

        try {
            FileReader reader = new FileReader(fileLocation);
            Object obj = jsonParser.parse(reader);
            JSONObject jsonData = (JSONObject) obj;
            data = (HashMap) jsonData;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static int getIndexOfMatchingAttribute(String jsonString, String path, String key, String value) {
        int returnID = -1;
        List<LinkedHashMap<String, Object>> dataList = (List<LinkedHashMap<String, Object>>) readJson(jsonString, path);

        List<HashMap<String, Object>> reducedList = dataList.stream().filter(hashmap -> ((hashmap.get(key)).toString().equalsIgnoreCase(value)))
                .collect(Collectors.toList());
        returnID = dataList.indexOf(reducedList.get(0));
        return returnID;
    }

    public static void main(String[] args) {
        JSONObject orderData = (JSONObject) readJsonFromFile("src/main/java/resources/order/Order.json").get("ph");
        Map<String, JSONObject> freeDelivery = (Map) readJson(orderData.toString(), "$.freeDelivery");
        Map<String, JSONObject> paidDelivery = (Map) readJson(orderData.toString(), "$.paidDelivery");
        Map<String, JSONObject> discountedItem = (Map) readJson(orderData.toString(), "$.discountedItem");
        System.out.println(freeDelivery.get("checkoutPayload"));
    }
}
