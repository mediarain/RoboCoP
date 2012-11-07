package de.mdsdacp.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class stores the dependencies between the different database entries.
 * The ecore stores the over direction, but the reverse direction is required. 
 * 
 * @author Frederik Goetz
 */
public class StoredReferences {
    private static HashMap<String, List<String>> references = new HashMap<String, List<String>>();

    public static void putReference(String key, String value) {
        if (references.containsKey(key)) {
            references.get(key).add(value);
        } else {
            List<String> list = new ArrayList<String>();
            list.add(value);
            references.put(key, list);
        }
    }

    public static List<String> getReferences(String key) {
        return references.get(key);
    }

    public static void clearReferences() {
        references.clear();
    }

}
