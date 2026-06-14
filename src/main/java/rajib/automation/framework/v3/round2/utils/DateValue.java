package rajib.automation.framework.v3.round2.utils;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateValue {
    public final int day, month, year;

    public DateValue(int day, int month, int year) { this.day = day; this.month = month; this.year = year; }

    public static DateValue parse(String input) {
        // For "10 May 2026" format
        String[] parts = input.split(" ");
        int day = Integer.parseInt(parts[0]);
        int year = Integer.parseInt(parts[2]);
        int month = Month.valueOf(parts[1].toUpperCase()).getValue(); // Month enum: JANUARY=1
        return new DateValue(day, month, year);
    }

    public String toAriaLabel() {
        // e.g. "Choose Sunday, May 10th, 2026"
        // NOTE: calendrical logic (e.g., find the weekday) is optional
        // Use a contains() selector or match only on day, month and year for simplicity
        return String.format("%d %s %d", day, Month.of(month).getDisplayName(TextStyle.FULL, Locale.ENGLISH), year);
    }
}