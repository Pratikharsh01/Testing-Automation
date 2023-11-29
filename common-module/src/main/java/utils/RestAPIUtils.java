package utils;

import java.util.HashMap;

//import io.restassured.RestAssured;
import base.BaseAPITest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;


public class RestAPIUtils {
    private static ThreadLocal<RequestSpecification> requests = new ThreadLocal<>();

    public static void setup(String baseURL) {
        RequestSpecification request = null;
        request = RestAssured.given();
        request.baseUri(baseURL);
        requests.set(request);
    }
    public static RequestSpecification getRequest()
    {
        return requests.get();
    }
    public static void clearRequest()
    {
        requests.remove();
    }

    public static Response postAPI(String path, String body, HashMap<String, String> header, String baseURL) {
        setup(baseURL);
        RequestSpecification request = getRequest();
        Response res = null;
        if (body != null)
            request.body(body);
        for (String key : header.keySet()) {
            request.header(key, header.get(key));
        }
        res = request.post(path);
        return res;
    }

    public static Response putAPI(String path, String body, HashMap<String, String> header, String baseURL) {
        setup(baseURL);
        RequestSpecification request = getRequest();
        Response res = null;
        if (body != null)
            request.body(body);
        for (String key : header.keySet()) {
            request.header(key, header.get(key));
        }
        res = request.put(path);
        return res;
    }

    public static Response getAPI(String path, String body, HashMap<String, String> header, String baseURL) {
        setup(baseURL);
        RequestSpecification request = getRequest();
        Response res = null;
        if (body != null)
            request.body(body);
        for (String key : header.keySet()) {
            request.header(key, header.get(key));
        }
        res = request.get(path);
        return res;
    }

}
