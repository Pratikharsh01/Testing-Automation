package utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;

import java.security.SecureRandom;
import java.util.Random;

public class RandomUtils {
    static char[] SYMBOLS = "^$*.[]{}()?-\"!@#%&/\\,><':;|_~`".toCharArray();
    static char[] LOWERCASE = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    static char[] UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    static char[] NUMBERS = "0123456789".toCharArray();
    static char[] ALL_CHARS =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789^$*.[]{}()?-\"!@#%&/\\,><':;|_~`"
                    .toCharArray();
    static Random rand = new SecureRandom();


    //For generating Random Email
    public static String generateRandomEmail(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
        String email = "";
        String temp = RandomStringUtils.random(length, allowedChars);
        email = temp.substring(0, temp.length() - 9) + "@yopmail.com";
        return email;
    }

    public static String generateMaildrop(int length) {
        String allowedChars = "abcdefghijklmnopqrstuvwxyz" + "1234567890";
        String email = "";
        String temp = RandomStringUtils.random(length, allowedChars);
        email = temp.substring(0, temp.length() - 9) + "@maildrop.cc";
        return email;
    }

    public static long getNumber(int noOfDigit) {
        long numberToReturn = 0;

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append(random.nextInt(9) + 1);

        // rest of 11 digits
        for (int i = 0; i < noOfDigit - 1; i++) {
            sb.append(random.nextInt(10));
        }

        numberToReturn = Long.valueOf(sb.toString()).longValue();
        return numberToReturn;
    }

    public static String getPassword(int length) {

        assert length >= 4;
        char[] password = new char[length];

        // get the requirements out of the way
        password[0] = LOWERCASE[rand.nextInt(LOWERCASE.length)];
        password[1] = UPPERCASE[rand.nextInt(UPPERCASE.length)];
        password[2] = NUMBERS[rand.nextInt(NUMBERS.length)];
        password[3] = SYMBOLS[rand.nextInt(SYMBOLS.length)];

        // populate rest of the password with random chars
        for (int i = 4; i < length; i++) {
            password[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        // shuffle it up
        for (int i = 0; i < password.length; i++) {
            int randomPosition = rand.nextInt(password.length);
            char temp = password[i];
            password[i] = password[randomPosition];
            password[randomPosition] = temp;
        }
        return new String(password);
    }

    public static String generateRandomString(int length) {

        char[] randomString = new char[length];

        // populate random String with random chars
        for (int i = 0; i < length; i++) {
            randomString[i] = ALL_CHARS[rand.nextInt(ALL_CHARS.length)];
        }

        // shuffle it up
        for (int i = 0; i < randomString.length; i++) {
            int randomPosition = rand.nextInt(randomString.length);
            char temp = randomString[i];
            randomString[i] = randomString[randomPosition];
            randomString[randomPosition] = temp;
        }
        return new String(randomString);
    }

    public static int getRandomNumber(int length) {
        return rand.nextInt(length);
    }

    public static long generateRandomNumber(int length) {

        char[] randomNumber = new char[length];

        // populate random String with random chars
        for (int i = 0; i < length; i++) {
            randomNumber[i] = NUMBERS[rand.nextInt(NUMBERS.length)];
        }

        // shuffle it up
        for (int i = 0; i < randomNumber.length; i++) {
            int randomPosition = rand.nextInt(randomNumber.length);
            char temp = randomNumber[i];
            randomNumber[i] = randomNumber[randomPosition];
            randomNumber[randomPosition] = temp;
        }
        return new Long(String.valueOf(randomNumber));
    }


}
