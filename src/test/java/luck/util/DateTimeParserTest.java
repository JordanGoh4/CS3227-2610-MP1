package luck.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

/** Tests supported date/time input and output formats. */
class DateTimeParserTest {
    @Test
    void parse_slashSeparatedDate_dateReturned() {
        assertEquals(LocalDateTime.of(2026, 8, 25, 0, 0),
                DateTimeParser.parse("25/08/2026"));
    }

    @Test
    void parse_dateAndTimeWithColon_dateTimeReturned() {
        assertEquals(LocalDateTime.of(2026, 8, 25, 14, 30),
                DateTimeParser.parse("25/08/2026 14:30"));
    }

    @Test
    void parse_unsupportedDateFormat_nullReturned() {
        assertNull(DateTimeParser.parse("25-08-2026"));
    }

    @Test
    void formatForDisplay_dateTime_formattedTextReturned() {
        assertEquals("Aug 25 2026, 2:30 PM",
                DateTimeParser.formatForDisplay(LocalDateTime.of(2026, 8, 25, 14, 30)));
    }
}
