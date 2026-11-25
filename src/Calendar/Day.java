package Calendar;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Map;

/**
 * A single calendar day with a price multiplier.
 *  - Weekdays:  x1.0
 *  - Weekends (Sat/Sun): x1.1
 *  - Holidays (Christmas, New Year): x1.5 (replaces the weekend multiplier, does not stack)
 */
public class Day {
    public static final double WEEKDAY_MULTIPLIER = 1.0;
    public static final double WEEKEND_MULTIPLIER = 1.1;
    public static final double HOLIDAY_MULTIPLIER = 1.5;

    /** Fixed-date holidays (same every year). Add more entries here to extend. */
    private static final Map<MonthDay, String> HOLIDAYS = Map.of(
            MonthDay.of(12, 25), "Christmas Day",
            MonthDay.of(1, 1), "New Year's Day"
    );

    private final LocalDate date;
    private final double priceMultiplier;

    public Day(LocalDate date) {
        this.date = date;
        if (isHoliday()) {
            this.priceMultiplier = HOLIDAY_MULTIPLIER;
        } else if (isWeekend()) {
            this.priceMultiplier = WEEKEND_MULTIPLIER;
        } else {
            this.priceMultiplier = WEEKDAY_MULTIPLIER;
        }
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

    public boolean isHoliday() {
        return HOLIDAYS.containsKey(MonthDay.from(date));
    }

    /** Name of the holiday, or null if this day is not a holiday. */
    public String getHolidayName() {
        return HOLIDAYS.get(MonthDay.from(date));
    }

    public double getPrice(double basePrice) {
        return basePrice * priceMultiplier;
    }

    public String getDayName() {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

    @Override
    public String toString() {
        String holiday = isHoliday() ? " - " + getHolidayName() : "";
        return getDayName() + ", " + date + holiday + " (x" + priceMultiplier + ")";
    }
}
