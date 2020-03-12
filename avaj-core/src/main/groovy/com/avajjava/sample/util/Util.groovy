package com.avajjava.sample.util

import groovy.json.JsonOutput
import jaemisseo.man.AnsiMan

class Util {



    /***************************************************************************
     *
     * Hightlight for JSON
     *
     ***************************************************************************/
    static String syntaxHighlightForHTML(def object){
        return syntaxHighlight(
                object,
                '<span class="key">', "</span>",
                '<span class="number">', "</span>",
                '<span class="string">', "</span>",
                '<span class="boolean">', "</span>",
                '<span class="null">', "</span>",
        )
    }

    static String syntaxHighlightForConsole(def object){
        return syntaxHighlight(
                object,
                AnsiMan.ANSI_WHITE, AnsiMan.ANSI_RESET,
                AnsiMan.ANSI_CYAN, AnsiMan.ANSI_RESET,
                AnsiMan.ANSI_PURPLE, AnsiMan.ANSI_RESET,
                AnsiMan.ANSI_GREEN, AnsiMan.ANSI_RESET,
                AnsiMan.ANSI_RED, AnsiMan.ANSI_RESET,
        )
    }

    static String syntaxHighlight(def object, String preForKey, String surForKey, String preForNumber, String surForNumber, String preForString, String surForString, String preForBoolean, String surForBoolean, String preForNull, String surForNull){
        String json = (object instanceof String) ? object : JsonOutput.toJson(object)
        json = json.replace(/&/, '&amp;').replace(/</, '&lt;').replace(/>/, '&gt;')
        json = json.replaceAll(/("([\\]u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/, { match ->
            String chk = match[0]
            if (chk ==~ /^".*/){
                if (chk ==~ /.*:$/){
                    return " " + preForKey + unicodeConvert(chk) + surForKey
                }else{
                    return preForString + unicodeConvert(chk) + surForString
                }
            }else if (chk ==~ /true|false/){
                return preForBoolean + chk + surForBoolean
            }else if (chk ==~ /null/){
                return preForNull + chk + surForNull
            }
            return preForNumber + chk + surForNumber
        })
        return json
    }

    static String unicodeConvert(String str) {
        StringBuilder sb = new StringBuilder();
        char ch;
        int len = str.length();
        for (int i = 0; i < len; i++) {
            ch = str.charAt(i);
            if (ch == '\\' && str.charAt(i+1) == 'u') {
                sb.append((char) Integer.parseInt(str.substring(i+2, i+6), 16));
                i+=5;
                continue;
            }
            sb.append(ch);
        }
        return sb.toString();
    }



    /***************************************************************************
     *
     * RANDOM
     *
     ***************************************************************************/
    static Object pickObjectRandom(List someList){
        int size = someList.size()
        int index = Math.floor(Math.random() * size);
        return someList[index];
    }

    static Object pickObjectRandom(Map someMap){
        List keyList = someMap.keySet().toList()
        int size = keyList.size()
        int index = Math.floor(Math.random() * size);
        Long key = keyList[index];
        return someMap[key];
    }




    /***************************************************************************
     *
     * - REF: https://stackoverflow.com/questions/955110/similarity-string-comparison-in-java
     * Calculates the similarity (a number within 0 and 1) between two strings.
     *
     ***************************************************************************/
    public static double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
        /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public static int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

    public static Object pickObjectHighSimilarity(String standardToCompare, List<Object> targetObjectToCompareList, List<String> targetObjectAttributeNameList){
        double highSimilarity = 0.0;
        Object targetObjectWithHighSimilarity = null;
        targetObjectToCompareList.each{
            String targetToCompare = targetObjectAttributeNameList.collect{ String attributeName -> it[attributeName] }.join('')
            double targetValue = similarity(standardToCompare, targetToCompare)
            if (highSimilarity < targetValue){
                highSimilarity = targetValue
                targetObjectWithHighSimilarity = it
            }
        }
        return targetObjectWithHighSimilarity
    }




    static Boolean generateBoolean(String value){
        switch(value?.toUpperCase()?.trim()){
            case {["1",  "Y", "TRUE", "T", "OK", "YES"].contains(it)}:
                return true
                break
            case {["0",  "N", "FALSE", "F", "NO"].contains(it)}:
                return false
                break
        }
        return null
    }

}
