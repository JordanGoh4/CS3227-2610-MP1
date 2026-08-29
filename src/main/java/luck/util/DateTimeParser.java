package luck.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

public class DateTimeParser {
    private static final List<DateTimeFormatter> INPUT_FORMATTERS = Arrays.asList(
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ISO_LOCAL_DATE,
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy HH:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );

    private static final DateTimeFormatter DISPLAY_FORMATTER = DateTimeFormatter.ofPattern("MMM dd yyyy");
    private static final DateTimeFormatter DISPLAY_WITH_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");

    public static LocalDateTime parse(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        String trimmed = value.trim();
        for (DateTimeFormatter formatter : INPUT_FORMATTERS) {
            try {
                return LocalDateTime.parse(trimmed, formatter);
            } catch (DateTimeParseException ignored) {
                // Try the next pattern.
            }

            try {
                LocalDate date = LocalDate.parse(trimmed, formatter);
                return date.atStartOfDay();
            } catch (DateTimeParseException ignored) {
                // Try the next pattern.
            }
        }

        String[] parts = trimmed.split("\\s+");
        if (parts.length == 2) {
            try {
                LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ofPattern("d/M/yyyy"));
                LocalTime time = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HHmm"));
                return LocalDateTime.of(date, time);
            } catch (DateTimeParseException ignored) {
                // Try alternative format.
            }
        }

        return null;
    }

    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        if (dateTime.getHour() == 0 && dateTime.getMinute() == 0 && dateTime.getSecond() == 0) {
            return dateTime.format(DISPLAY_FORMATTER);
        }

        return dateTime.format(DISPLAY_WITH_TIME_FORMATTER);
    }

    public static String toStorageString(LocalDateTime dateTime) {
        return dateTime == null ? "" : dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
