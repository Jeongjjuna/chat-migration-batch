package jeongjihun.chatmigrationbatch.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateGenerator {
    private DateGenerator() {
    }
    public static LocalDateTime convert(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");
        return LocalDateTime.parse(date, formatter);
    }
}
