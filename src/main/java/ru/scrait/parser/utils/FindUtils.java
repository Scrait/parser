package ru.scrait.parser.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FindUtils {

    public static Set<String> getFieldFromArray(JSONArray array, String field) {
        final Set<String> set = new HashSet<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject j = array.getJSONObject(i);
            set.add(j.getString(field));
        }
        return set;
    }

    public static String getKeyFromValue(Map<String, String> map, String value) {
        String key = null;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            final String entryValue = entry.getValue();
            if (entryValue.substring(entryValue.indexOf(":") + 2).equals(value)) {
                key = entry.getKey();
                break;
            }
        }
        return key;
    }

    public static int randomNumber(int min, int max) {
        return (int) (min + (double) (max - min) * Math.random());
    }

}
