package Calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

/**
 * A single calendar day with a price multiplier.
 * Weekdays have a multiplier of 1.0, weekends (Sat/Sun) have 1.1.
 */
public class Day {
    public static final double WEEKDAY_MULTIPLIER = 1.0;
    public static final double WEEKEND_MULTIPLIER = 1.1;

    private final LocalDate date;
    private final double priceMultiplier;

    public Day(LocalDate date) {
        this.date = date;
        this.priceMultiplier = isWeekend() ? WEEKEND_MULTIPLIER : WEEKDAY_MULTIPLIER;
    }

    public LocalDate getDate() {
        return date;
    }

    public int getDayOfMonth() {
        return date.getDayOfMonth();
    }

    public double getPriceMultiplier() {
        return priceMultiplier;
    }

    public boolean isWeekend() {
        DayOfWeek d = date.getDayOfWeek();
        return d == DayOfWeek.SATURDAY || d == DayOfWeek.SUNDAY;
    }

    public double getPrice(double basePrice) {
        return basePrice * priceMultiplier;
    }

    public String getDayName() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    @Override
    public String toString() {
        return getDayName() + ", " + date + " (x" + priceMultiplier + ")";
    }
}
