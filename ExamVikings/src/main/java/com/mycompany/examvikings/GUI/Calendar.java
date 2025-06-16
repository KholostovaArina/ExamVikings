package com.mycompany.examvikings.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;

public class Calendar extends JDialog {
    private static int currentYear = 800; // Текущий год, увеличивается после успешного похода

    private boolean confirmed = false;
    private final int days;
    private LocalDate selectedStartDate;

    public Calendar(JFrame parent, int days) {
        super(parent, "Выбор даты начала похода", true);
        this.days = days;
        this.selectedStartDate = null;

        setSize(400, 300);
        setLocationRelativeTo(parent);

        JLabel yearLabel = new JLabel("Сейчас " + currentYear + "год");
        yearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(yearLabel, BorderLayout.NORTH);

        JPanel datePanel = new JPanel(new GridLayout(3, 2, 5, 5));
        JComboBox<Integer> dayBox = new JComboBox<>();
        for (int i = 1; i <= 31; i++) {
            dayBox.addItem(i);
        }

        JComboBox<String> monthBox = new JComboBox<>(new String[]{
                "мая", "июня", "июля", "августа", "сентября"
        });

        JButton confirmButton = new JButton("Подтвердить");
        JButton cancelButton = new JButton("Отмена");

        datePanel.add(new JLabel("День:"));
        datePanel.add(dayBox);
        datePanel.add(new JLabel("Месяц:"));
        datePanel.add(monthBox);

        confirmButton.addActionListener(e -> {
            int day = (Integer) dayBox.getSelectedItem();
            String monthStr = (String) monthBox.getSelectedItem();

            Month startMonth = switch (monthStr) {
                case "мая" -> Month.MAY;
                case "июня" -> Month.JUNE;
                case "июля" -> Month.JULY;
                case "августа" -> Month.AUGUST;
                case "сентября" -> Month.SEPTEMBER;
                default -> null;
            };

            if (startMonth == null) return;

            try {
                selectedStartDate = LocalDate.of(currentYear, startMonth, day);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Неверная дата.");
                return;
            }

            // Проверяем: начало похода не ранее 6 мая
            LocalDate earliestDate = LocalDate.of(currentYear, Month.MAY, 6);
            if (selectedStartDate.isBefore(earliestDate)) {
                JOptionPane.showMessageDialog(this,
                        "Поход нельзя начать до 6 мая.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Проверяем: конец похода должен быть до или включая 12 сентября
            LocalDate endDate = selectedStartDate.plusDays(days);
            LocalDate latestDate = LocalDate.of(currentYear, Month.SEPTEMBER, 12);

            if (endDate.isAfter(latestDate)) {
                JOptionPane.showMessageDialog(this,
                        "Поход завершится после 12 сентября. Невозможно организовать.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            confirmed = true;
            dispose();
        });

        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        add(datePanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public LocalDate getSelectedStartDate() {
        return selectedStartDate;
    }

    public LocalDate getEndDate() {
        return selectedStartDate != null ? selectedStartDate.plusDays(days) : null;
    }

    public static class DateRangeResult {

        public final LocalDate startDate;
        public final LocalDate endDate;

        public DateRangeResult(LocalDate start, LocalDate end) {
            this.startDate = start;
            this.endDate = end;
        }

        @Override
        public String toString() {
            return String.format("%d %s — %d %s %d",
                    startDate.getDayOfMonth(), getMonthName(startDate.getMonthValue()),
                    endDate.getDayOfMonth(), getMonthName(endDate.getMonthValue()), endDate.getYear());
        }
    }

    public static DateRangeResult calculateDateRange(int days) {
        Calendar dialog = new Calendar((JFrame) null, days);
        dialog.setVisible(true);

        if (!dialog.isConfirmed()) {
            return null;
        }

        LocalDate startDate = dialog.getSelectedStartDate();
        LocalDate endDate = startDate.plusDays(days);
        currentYear++; // Увеличиваем год после успешного похода

        return new DateRangeResult(startDate, endDate);
    }

    public static String getMonthName(int monthValue) {
        return switch (monthValue) {
            case 5 ->
                "мая";
            case 6 ->
                "июня";
            case 7 ->
                "июля";
            case 8 ->
                "августа";
            case 9 ->
                "сентября";
            default ->
                "";
        };
    }

    /**
     * Статический метод получения текущего года
     */
    public static int getCurrentYear() {
        return currentYear;
    }

}
