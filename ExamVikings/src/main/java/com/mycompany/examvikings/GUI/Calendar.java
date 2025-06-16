package com.mycompany.examvikings.GUI;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.Month;

/**
 * Класс, представляющий диалоговое окно выбора даты начала похода.
 * Позволяет пользователю выбрать стартовую дату и проверяет,
 * чтобы поход укладывался в допустимый временной диапазон.
 */
public class Calendar extends JDialog {

    /**
     * Текущий год, который увеличивается после успешного похода.
     * Используется для определения возможных дат набегов.
     */
    private static int currentYear = 800;

    /**
     * Флаг, указывающий, была ли подтверждена дата пользователем.
     */
    private boolean confirmed = false;

    /**
     * Продолжительность похода в днях.
     */
    private final int days;

    /**
     * Выбранная пользователем дата начала похода.
     */
    private LocalDate selectedStartDate;

    /**
     * Создаёт новое диалоговое окно для выбора даты начала похода.
     *
     * @param parent родительское окно
     * @param days продолжительность похода в днях
     */
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

    /**
     * Возвращает флаг подтверждения даты.
     *
     * @return true, если дата была подтверждена, иначе false
     */
    public boolean isConfirmed() {
        return confirmed;
    }

    /**
     * Возвращает выбранную пользователем дату начала похода.
     *
     * @return объект {@link LocalDate} с выбранной датой
     */
    public LocalDate getSelectedStartDate() {
        return selectedStartDate;
    }

    /**
     * Возвращает рассчитанную дату окончания похода на основе выбранной даты старта.
     *
     * @return объект {@link LocalDate} с датой окончания похода
     */
    public LocalDate getEndDate() {
        return selectedStartDate != null ? selectedStartDate.plusDays(days) : null;
    }

    /**
     * Вложенный класс, представляющий диапазон дат похода.
     * Содержит даты начала и окончания похода.
     */
    public static class DateRangeResult {

        /**
         * Дата начала похода.
         */
        public final LocalDate startDate;

        /**
         * Дата окончания похода.
         */
        public final LocalDate endDate;

        /**
         * Создаёт новый объект с указанием дат начала и окончания.
         *
         * @param start дата начала
         * @param end дата окончания
         */
        public DateRangeResult(LocalDate start, LocalDate end) {
            this.startDate = start;
            this.endDate = end;
        }

        /**
         * Возвращает строковое представление диапазона дат.
         *
         * @return строка вида "1 мая — 12 сентября 800"
         */
        @Override
        public String toString() {
            return String.format("%d %s — %d %s %d",
                    startDate.getDayOfMonth(), getMonthName(startDate.getMonthValue()),
                    endDate.getDayOfMonth(), getMonthName(endDate.getMonthValue()), endDate.getYear());
        }
    }

    /**
     * Открывает диалоговое окно и возвращает выбранный диапазон дат.
     *
     * @param days продолжительность похода в днях
     * @return объект {@link DateRangeResult} с датами начала и окончания похода
     */
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

    /**
     * Возвращает название месяца на русском языке по его номеру.
     *
     * @param monthValue номер месяца (1-12)
     * @return строка с названием месяца
     */
    public static String getMonthName(int monthValue) {
        return switch (monthValue) {
            case 5 -> "мая";
            case 6 -> "июня";
            case 7 -> "июля";
            case 8 -> "августа";
            case 9 -> "сентября";
            default -> "";
        };
    }

    /**
     * Возвращает текущий год, используемый в приложении.
     *
     * @return текущий год
     */
    public static int getCurrentYear() {
        return currentYear;
    }
}