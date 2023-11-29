package utils;

public class EnvironmentUtil {
    public static String countryCode;

    public static String rps_order_id;

    public static void updateData(String country) {
        if (country.equalsIgnoreCase("KE")) {
            countryCode = "ke";
        }
        if (country.equalsIgnoreCase("at") || country.equalsIgnoreCase("IN")) {
            countryCode = "in";
        }
    }

}
