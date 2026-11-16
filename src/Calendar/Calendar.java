package Calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Calendar extends JFrame {
    // same fields as the original (bound by the GUI designer form)
    private JLabel lMonth;
    private JButton btnNext;
    private JPanel pnlMain;
    private JButton btnBack;

    private static final String[] HEADERS = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final Color WEEKDAY_BG = new Color(245, 245, 245);
    private static final Color WEEKEND_BG = new Color(255, 232, 200);

    private static final Color NON_CURRENT_BG = new Color(80,80,80);
    private static final double BASE_PRICE = 100.0;

    private YearMonth currentMonth;
    private final List<Day> days = new ArrayList<>();
    private final JPanel pnlDays = new JPanel(new GridLayout(0, 7, 4, 4));

    public Calendar() {
        currentMonth = YearMonth.now();

        // If the form designer didn't create the components, fall back to plain ones
        if (pnlMain == null) pnlMain = new JPanel(new BorderLayout());
        if (lMonth == null) {
            lMonth = new JLabel("", SwingConstants.CENTER);
            pnlMain.add(lMonth, BorderLayout.NORTH);
        }
        if (btnNext == null) {
            btnNext = new JButton("Next");
            pnlMain.add(btnNext, BorderLayout.SOUTH);
        }

        // pnlMain is left untouched (its designer layout stays as-is);
        // the dynamic days panel goes underneath it in a wrapper panel
        JPanel root = new JPanel(new BorderLayout());
        root.add(pnlMain, BorderLayout.NORTH);
        root.add(pnlDays, BorderLayout.CENTER);
        setContentPane(root);

        btnNext.addActionListener(e -> showMonth(currentMonth.plusMonths(1)));
        showMonth(currentMonth);
    }

    /** Builds a Day for every date in the month and redraws the days panel. */
    private void showMonth(YearMonth month) {
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
                    day.getDayOfMonth(), day.getPrice(BASE_PRICE)), SwingConstants.CENTER);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calendar app = new Calendar(); // Instantiate
            app.setSize(400, 300);
            app.setDefaultCloseOperation(EXIT_ON_CLOSE);
            app.setVisible(true);
        });
    }
}
