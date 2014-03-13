package com.rain.utils.android.robocop.model;

/**
 * Created with IntelliJ IDEA.
 * User: dustin
 * Date: 1/16/14
 * Time: 5:07 PM
 */
public class StringUtils {

    public static String getPrivateVariableName(String input) {
        input = convertToCamelCase(input);
        String firstChar = input.substring(0, 1);
        String remaining = input.substring(1, input.length());
        return "m" + firstChar.toUpperCase() + remaining;
    }

    public static String convertToCamelCase(String input) {
        String[] parts = input.split("_");
        String camelCaseString = "";
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            camelCaseString = camelCaseString + ((i == 0) ? part.toLowerCase() : toProperCase(part));
//            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    public static String convertToTitleCase(String input) {
        String[] parts = input.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    public static String getUnderscoreNameString(String input) {
        String regex = "([a-z])([A-Z])";
        String replacement = "$1_$2";
        return input.replaceAll(regex, replacement);
    }

    public static String getConstantString(String input) {
        String constant = StringUtils.getUnderscoreNameString(input).toUpperCase();
        return constant;
    }

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
