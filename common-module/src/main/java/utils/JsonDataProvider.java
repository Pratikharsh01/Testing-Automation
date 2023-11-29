package utils;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class JsonDataProvider {

    @DataProvider(name = "json-data-provider")
    public Object[][] provideJsonData() {
        JSONParser jsonParser = new JSONParser();
        Object[][] data = null;

        try (FileReader reader = new FileReader("src/test/resources/jsonFiles/Countryconfig.json")) {

            Object obj = jsonParser.parse(reader);
            JSONObject jsonData = (JSONObject) obj;


            data = new Object[jsonData.size()][2];
            int index = 0;


            for (Object key : jsonData.keySet()) {
                String keyValue = (String) key;
                JSONObject innerData = (JSONObject) jsonData.get(keyValue);
                Map<String, Object> innerMap = new HashMap<>();
                for (Object innerKey : innerData.keySet()) {
                    String innerKeyString = (String) innerKey;
                    Object innerValue = innerData.get(innerKeyString);
                    innerMap.put(innerKeyString, innerValue);
                }
                data[index][0] = keyValue;
                data[index][1] = innerMap;
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

}

