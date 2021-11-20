package com.javaica.avp.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

public class JsonNodeAnswerComparator implements Comparator<JsonNode> {

    @Override
    public int compare(JsonNode o1, JsonNode o2) {
        if (o1 != null && o2 == null || o1 == null && o2 != null)
            return 1;
        if (o1 == null) // then o2 == null
            return 0;
        if (o1.equals(o2))
            return 0;
        if (o1.isTextual() && o2.isTextual() && o1.asText().equalsIgnoreCase(o2.asText()))
            return 0;
        if (o1.isArray() && o2.isArray())
            return compareArrays(o1, o2);
        if (o1.isObject() && o2.isObject())
            return compareObjects(o1, o2);
        return 1;
    }

    private int compareArrays(JsonNode o1, JsonNode o2) {
        Iterator<JsonNode> elements1 = o1.elements();
        Iterator<JsonNode> elements2 = o2.elements();
        while (elements1.hasNext() && elements2.hasNext()) {
            JsonNode node1 = elements1.next();
            JsonNode node2 = elements2.next();
            if (compare(node1, node2) != 0)
                return 1;
        }
        if (elements1.hasNext() || elements2.hasNext())
            return 1;
        return 0;
    }

    private int compareObjects(JsonNode o1, JsonNode o2) {
        Iterator<Map.Entry<String, JsonNode>> fields = o1.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> nodeEntry = fields.next();
            if (!o2.has(nodeEntry.getKey()))
                return 1;
            if (compare(o2.get(nodeEntry.getKey()), nodeEntry.getValue()) != 0)
                return 1;
        }

        Iterator<String> o2Names = o2.fieldNames();
        while (o2Names.hasNext()) {
            if (!o1.has(o2Names.next()))
                return 1;
        }
        return 0;
    }
}
