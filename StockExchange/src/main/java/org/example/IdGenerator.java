package org.example;

import java.util.UUID;

public class IdGenerator {
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        int hyphenIndex = uuidStr.indexOf('-');
        return hyphenIndex != -1 ? uuidStr.substring(0, hyphenIndex) : uuidStr;
    }
}
