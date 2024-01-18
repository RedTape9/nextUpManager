package io.github.redtape9.nextupmanager.backend.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeFormatter {
    public static String getFormattedDateTime() {

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Datum und Uhrzeit im ISO-8601-Format formatieren und zurückgeben
        return now.format(formatter);
    }
}
