package com.josephgibis.springcodegenerator.util;

import java.util.ArrayList;
import java.util.List;

public class StringFormatter {

    // (this assumes PascalCase)
    public static String makeCamelCase(final String input) {
        return input.substring(0, 1).toLowerCase() + input.substring(1);
    }

    // (this assumes camelCase)
    public static String makePascalCase(final String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }


    // (camelCase & PascalCase both work here)
    public static String makeSnakeCase(final String input) {
        List<String> words = getWordListFromPascalCase(input);
        StringBuilder sb = new StringBuilder();
        for(String word : words) {
            sb.append(word.toLowerCase());

            sb.append("_");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String makePlural(final String input) {
        return input.charAt(input.length() - 1) == 's' ? input : input + "s";
    }


    private static List<String> getWordListFromPascalCase(final String input) {
        StringBuilder sb = new StringBuilder();
        List<String> words = new ArrayList<>();
        for(char c : input.toCharArray()) {
            if(Character.isUpperCase(c)){
                if(!sb.isEmpty()) {
                    words.add(sb.toString());
                    sb.setLength(0);
                }
            }
            sb.append(c);
        }
        words.add(sb.toString());

        return words;
    }

}
