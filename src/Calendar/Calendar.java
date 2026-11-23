package Calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calendar {
    // Bound by the GUI designer form (if present)
    private JLabel lMonth;
    private JButton btnNext;
    private JPanel pnlMain;
    private JButton btnBack;

    private static final String[] HEADERS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final Color WEEKDAY_BG = new Color(245, 245, 245);
    private static final Color WEEKEND_BG = new Color(255, 232, 200);

    private double basePrice = 100.0;
    private YearMonth currentMonth;
    private final List<Day> days = new ArrayList<>();
    private final JPanel pnlDays = new JPanel(new GridLayout(0, 7, 4, 4));
    private final JPanel rootPanel = new JPanel(new BorderLayout());

    public Calendar() {
        this(100.0);
    }

    public Calendar(double basePrice) {
        this.basePrice = basePrice;
        this.currentMonth = YearMonth.now();

        // Fallback components if not bound by the .form designer
        if (pnlMain == null) {
            pnlMain = new JPanel(new BorderLayout());
        }
        if (lMonth == null) {
            lMonth = new JLabel("", SwingConstants.CENTER);
            pnlMain.add(lMonth, BorderLayout.WEST);
        }
        if (btnNext == null) {
            btnNext = new JButton("Next");
            pnlMain.add(btnNext, BorderLayout.EAST);
        }

        // Top header bar (pnlMain) + dynamic calendar grid (pnlDays) — NO outer border
        rootPanel.add(pnlMain, BorderLayout.NORTH);
        rootPanel.add(pnlDays, BorderLayout.CENTER);

        btnNext.addActionListener(e -> showMonth(currentMonth.plusMonths(1)));
        if (btnBack != null) {
            btnBack.addActionListener(e -> showMonth(currentMonth.minusMonths(1)));
        }

        showMonth(currentMonth);
    }

    /** Returns the full calendar panel so it can be embedded inside another JFrame or split layout. */
    public JPanel getRootPanel() {
        return rootPanel;
    }

    /** Updates the nightly base price and refreshes the displayed month. */
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
        showMonth(currentMonth);
    }

    /** Builds a Day for every date in the month and redraws the days panel. */
    public void showMonth(YearMonth month) {
        currentMonth = month;
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
        for (Day day : days) {
            JLabel cell = new JLabel(String.format(
                    "<html><center><b>%d</b><br><small>%.2f</small></center></html>",
                    day.getDayOfMonth(), day.getPrice(basePrice)), SwingConstants.CENTER);
            cell.setOpaque(true);
            cell.setBackground(day.isWeekend() ? WEEKEND_BG : WEEKDAY_BG);
            cell.setToolTipText(day.toString());
            if (day.getDate().equals(today)) {
                cell.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
            }
            pnlDays.add(cell);
        }

        pnlDays.revalidate();
        pnlDays.repaint();
    }
}