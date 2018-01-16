package com.rs.core.utils;

/**
 * Class for modifying text
 *
 * @author FuzzyAvacado
 */
public final class TextUtils {

    public static final String GREEN = "<col=00FF00>";
    public static final String RED = "<col=FF0000>";
    public static final String BLUE = "<col=1589FF>";
    public static final String END_COLOR = "</col>";

    public static String modifyCharsForXMas(String string) {
        String newString = "", currentColor = "";
        char[] chars = string.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == ' ') {
                newString += " ";
                continue;
            }
            currentColor = i % 2 == 0 ? RED : GREEN;
            newString += currentColor + chars[i];// + END_COLOR;
        }
        return newString;
    }

    public static String modifyStringForXMas(String s) {
        String[] words = s.split(" ");
        String newString = "", currentColor = "";
        for (int i = 0; i < words.length; i++) {
            currentColor = i % 2 == 0 ? RED : GREEN;
            newString += currentColor + words[i] + " ";
        }
        return newString;
    }

    public static String colorizeString(String string, String colorCode) {
        return colorCode + string + END_COLOR;
    }

    public static String formatStringUpper(String string) {
        String[] words = string.split(" ");
        String newString = "";
        for (String s : words) {
            s.substring(1).toUpperCase();
            newString += (s + " ");
        }
        return newString;
    }

    public static String upperCase(String word) {
        return word.length() > 0 ? Character.toUpperCase(word.charAt(0)) + word.substring(1) : null;
    }
}
