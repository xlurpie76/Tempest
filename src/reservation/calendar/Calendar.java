package reservation.calendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.DoubleSupplier;

public class Calendar {
    // Bound by the GUI designer form (if present)
    private JLabel lMonth;
    private JButton btnNext;
    private JPanel pnlMain;
    private JButton btnBack;

    private static final String[] HEADERS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final Color WEEKDAY_BG = new Color(245, 245, 245);
    private static final Color WEEKEND_BG = new Color(255, 232, 200);
    private static final Color HOLIDAY_BG = new Color(255, 205, 205);
    private static final Color PAST_BG = new Color(225, 225, 225);
    private static final Color PAST_FG = new Color(160, 160, 160);
    private static final Color HOVER_BG = new Color(220, 235, 255);
    private static final Color ENDPOINT_BG = new Color(120, 170, 245); // start / end day
    private static final Color RANGE_BG = new Color(200, 222, 252);    // days in between

    /**
     * Supplies the base price every time the calendar draws. Connect your own variable here
     * with setBasePriceSupplier(...), then call refresh() whenever that variable changes.
     */
    private DoubleSupplier basePriceSupplier = () -> 100.0;

    private YearMonth currentMonth;
    private final YearMonth minMonth = YearMonth.now(); // can't navigate before this
    private final List<Day> days = new ArrayList<>();
    private final JPanel pnlDays = new JPanel(new GridLayout(0, 7, 4, 4));
    private final JPanel rootPanel = new JPanel(new BorderLayout());

    // Range selection
    private LocalDate startDate;
    private LocalDate endDate;
    private int numberOfDays; // inclusive count of selected days (0 until a full range is picked)
    private BiConsumer<LocalDate, LocalDate> onRangeSelected;

    public Calendar() {
        this(100.0);
    }

    public Calendar(double basePrice) {
        this(() -> basePrice);
    }

    public Calendar(DoubleSupplier basePriceSupplier) {
        this.basePriceSupplier = basePriceSupplier;
        this.currentMonth = YearMonth.now();

        // Fallback components if not bound by the .form designer
        if (pnlMain == null) {
            pnlMain = new JPanel(new BorderLayout());
        }
        if (lMonth == null) {
            lMonth = new JLabel("", SwingConstants.CENTER);
            pnlMain.add(lMonth, BorderLayout.CENTER);
        }
        if (btnBack == null) {
            btnBack = new JButton("Back");
            pnlMain.add(btnBack, BorderLayout.WEST);
        }
        if (btnNext == null) {
            btnNext = new JButton("Next");
            pnlMain.add(btnNext, BorderLayout.EAST);
        }

        rootPanel.add(pnlMain, BorderLayout.NORTH);
        rootPanel.add(pnlDays, BorderLayout.CENTER);

        btnNext.addActionListener(e -> showMonth(currentMonth.plusMonths(1)));
        btnBack.addActionListener(e -> showMonth(currentMonth.minusMonths(1)));

        showMonth(currentMonth);
    }

    private Runnable onSelectionChanged;

    public void setOnSelectionChanged(Runnable r) {
        this.onSelectionChanged = r;
    }

    private void fireChanged() {
        if (onSelectionChanged != null) onSelectionChanged.run();
    }

    /** Returns the full calendar panel so it can be embedded inside another JFrame or split layout. */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    // ------------------------------------------------------------------ pricing

    /** Sets a fixed base price and refreshes the display. */
    public void setBasePrice(double basePrice) {
        this.basePriceSupplier = () -> basePrice;
        refresh();
        fireChanged();
    }

    /**
     * Connects a live base price, e.g. cal.setBasePriceSupplier(() -> room.getPricePerNight()).
     * It is read each time the calendar redraws, so call refresh() after your variable changes.
     */
    public void setBasePriceSupplier(DoubleSupplier supplier) {
        this.basePriceSupplier = supplier;
        refresh();
        fireChanged();
    }

    /** Current base price as supplied by the connected variable. */
    public double getBasePrice() {
        return basePriceSupplier.getAsDouble();
    }

    /** Redraws the current month so prices pick up the latest base price. */
    public void refresh() {
        showMonth(currentMonth);
    }

    /**
     * Total price of the selected range: sum of every selected day's price
     * (weekend and holiday multipliers included). Returns 0 until a full range is picked.
     */
    public double getTotalPrice() {
        if (startDate == null || endDate == null) return 0.0;
        double base = getBasePrice();
        double total = 0.0;
        for (LocalDate d = startDate; !d.isAfter(endDate); d = d.plusDays(1)) {
            total += new Day(d).getPrice(base);
        }
        return total;
    }

    // ------------------------------------------------------------------ selection

    /** Called once both a start and an end day have been picked. */
    public void setOnRangeSelected(BiConsumer<LocalDate, LocalDate> listener) {
        this.onRangeSelected = listener;
    }

    /** First selected day, or null. */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** Last selected day, or null if only a start day is picked so far. */
    public LocalDate getEndDate() {
        return endDate;
    }

    /** Number of selected days, counting both the start and end day. 0 until a full range is picked. */
    public int getNumberOfDays() {
        return numberOfDays;
    }

    /** Clears the current selection. */
    public void clearSelection() {
        startDate = null;
        endDate = null;
        numberOfDays = 0;
        showMonth(currentMonth);
    }

    // ------------------------------------------------------------------ drawing

    /** Builds a Day for every date in the month and redraws the days panel. */
    public void showMonth(YearMonth month) {
        if (month.isBefore(minMonth)) {
            month = minMonth; // never go before the current month
        }
        currentMonth = month;
        btnBack.setEnabled(currentMonth.isAfter(minMonth));

        days.clear();
        for (int d = 1; d <= month.lengthOfMonth(); d++) {
            days.add(new Day(month.atDay(d)));
        }

        lMonth.setText(month.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                + " " + month.getYear());

        pnlDays.removeAll();
        for (String h : HEADERS) {
            JLabel l = new JLabel(h, SwingConstants.CENTER);
            l.setFont(l.getFont().deriveFont(Font.BOLD));
            pnlDays.add(l);
        }

        // Sunday-first offset: Sunday = 0 ... Saturday = 6
        int offset = month.atDay(1).getDayOfWeek().getValue() % 7;
        for (int i = 0; i < offset; i++) {
            pnlDays.add(new JLabel());
        }

        LocalDate today = LocalDate.now();
        double base = getBasePrice(); // read once per redraw
        for (Day day : days) {
            pnlDays.add(createCell(day, today, base));
        }

        pnlDays.revalidate();
        pnlDays.repaint();
    }

    /** Creates one day cell: grayed out and inert if in the past, clickable otherwise. */
    private JLabel createCell(Day day, LocalDate today, double base) {
        LocalDate date = day.getDate();
        boolean isPast = date.isBefore(today);

        JLabel cell = new JLabel(String.format(
                "<html><center><b>%d</b><br><small>%.2f</small></center></html>",
                day.getDayOfMonth(), day.getPrice(base)), SwingConstants.CENTER);
        cell.setOpaque(true);

        if (isPast) {
            cell.setBackground(PAST_BG);
            cell.setForeground(PAST_FG);
            cell.setToolTipText("Unavailable");
            return cell; // no listeners -> not clickable
        }

        final Color normalBg = backgroundFor(day);
        cell.setBackground(normalBg);
        cell.setToolTipText(day.toString());
        cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!isInSelection(date)) {
                    cell.setBackground(HOVER_BG);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                cell.setBackground(normalBg);
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                handleClick(date);
            }
        });

        return cell;
    }

    /**
     * Click logic:
     *  - nothing picked, or a full range already picked  -> this day becomes the new start
     *  - start picked, clicked day is after the start    -> it becomes the end (range selected)
     *  - start picked, clicked day is on/before the start -> restart with this day as the start
     */
    private void handleClick(LocalDate date) {
        if (startDate == null || endDate != null || !date.isAfter(startDate)) {
            startDate = date;
            endDate = null;
            numberOfDays = 0;
            showMonth(currentMonth);
            fireChanged();
        } else {
            endDate = date;
            numberOfDays = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
            showMonth(currentMonth);
            if (onRangeSelected != null) {
                onRangeSelected.accept(startDate, endDate);
            }
            fireChanged();
        }
    }

    private boolean isInSelection(LocalDate date) {
        if (startDate == null) return false;
        if (endDate == null) return date.equals(startDate);
        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

    private Color backgroundFor(Day day) {
        LocalDate date = day.getDate();
        if (date.equals(startDate) || date.equals(endDate)) {
            return ENDPOINT_BG;
        }
        if (isInSelection(date)) {
            return RANGE_BG;
        }
        if (day.isHoliday()) {
            return HOLIDAY_BG;
        }
        return day.isWeekend() ? WEEKEND_BG : WEEKDAY_BG;
    }
}
