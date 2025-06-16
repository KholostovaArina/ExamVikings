package com.mycompany.examvikings.GUI;

import EntityManager.AttackHistoryManager;
import Entity.*;
import EntityManager.Vikings;
import com.mycompany.examvikings.GUI.Prediction.SatelitePanel;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Класс, представляющий окно отчёта о походе викингов.
 * Отображает маршрут, результаты атак, добычу и итоги набега.
 */
public class ReportFrame extends JFrame {

    /**
     * Текущее количество участвующих викингов.
     */
    private int vikingCount = 0;

    /**
     * Список имён викингов, участвующих в походе.
     */
    private final List<String> currentVikingNames = new ArrayList<>();

    /**
     * Флаг, определяющий, доступна ли кнопка "Поехать в поход".
     */
    private boolean enableGoButton = true;

    /**
     * Создаёт пустое окно отчёта.
     */
    public ReportFrame() {
        setTitle("Отчёт о походе");
        setSize(900, 700);
        setLocationRelativeTo(null);
    }

    /**
     * Показывает отчёт для нового похода на основе выбранных викингов, городов и корабля.
     *
     * @param vikings множество участвующих викингов
     * @param cityList список посещённых городов
     * @param drakkar используемый драккар
     */
    public void showReport(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        this.vikingCount = vikings.size();
        this.currentVikingNames.clear();
        for (Viking v : vikings) {
            this.currentVikingNames.add(v.getName());
        }

        Report.ReportData data = Report.generateReportData(vikings, cityList, drakkar);
        showThisReport(data);
    }

    /**
     * Отображает отчёт о набеге из истории.
     *
     * @param data данные отчёта
     * @param enableGoButton флаг доступности кнопки "Поехать в поход"
     */
    public void showReport(Report.ReportData data, boolean enableGoButton) {
        this.currentVikingNames.clear();
        this.enableGoButton = enableGoButton;
        showThisReport(data);
    }

    /**
     * Отображает исторический отчёт (по умолчанию с активированной кнопкой).
     *
     * @param data данные отчёта
     */
    public void showReport(Report.ReportData data) {
        showReport(data, true); // по умолчанию из истории — кнопка видна
    }

    /**
     * Универсальный метод отображения отчёта.
     *
     * @param data данные отчёта
     */
    private void showThisReport(Report.ReportData data) {
        setTitle("Отчёт о походе №" + data.reportId);
        setSize(900, 700);
        setLocationRelativeTo(null);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("============= Отчёт о походе №" + data.reportId + " =============");
        lblTitle.setFont(lblTitle.getFont().deriveFont(Font.BOLD, 16));
        lblTitle.setForeground(Color.BLUE);
        content.add(lblTitle);
        content.add(Box.createVerticalStrut(12));

        if (!currentVikingNames.isEmpty()) {
            content.add(getBoldLabel("Участники похода:"));
            for (String name : currentVikingNames) {
                content.add(new JLabel("   - " + name));
            }
            content.add(Box.createVerticalStrut(10));
        }

        StringBuilder routeSb = new StringBuilder("Маршрут: ");
        for (Report.ReportData.CityVisit visit : data.visits) {
            routeSb.append(visit.city.getName()).append(" → ");
        }
        routeSb.append(data.startCity != null ? data.startCity.getName() : "");
        content.add(new JLabel(routeSb.toString()));
        content.add(Box.createVerticalStrut(10));

        double currentFood = 0;
        for (Report.ReportData.CityVisit visit : data.visits) {
            content.add(getBoldLabel(String.format("Город: %s (%s)", visit.city.getName(), visit.city.getCityType())));
            content.add(new JLabel(String.format("Время в пути: %.2f часов (~%.2f дней)", visit.time, visit.time / 24)));

            if (visit.success) {
                content.add(getColoredLabel("Атака успешна!", new Color(0, 153, 0)));
                content.add(new JLabel("Добыча:"));
                if (!visit.loots.isEmpty()) {
                    Map<String, Integer> map = new LinkedHashMap<>();
                    for (Loot loot : visit.loots) {
                        map.merge(loot.getName(), 1, Integer::sum);
                    }
                    for (var entry : map.entrySet()) {
                        content.add(new JLabel("   - " + entry.getKey() + " x" + entry.getValue()));
                    }
                }
                content.add(new JLabel(String.format("Рабов взято: %d", visit.newSlaves)));
            } else {
                content.add(getColoredLabel("Атака провалена.", Color.GRAY));
            }

            if (currentFood > 0 && visit.remainingFood <= 0) {
                content.add(getColoredLabel("⚠️ Викинги начали есть из добычи", Color.RED));
            }
            currentFood = visit.remainingFood;
            content.add(Box.createVerticalStrut(8));
        }

        JLabel lblSummary = new JLabel("------------ ИТОГИ ------------");
        lblSummary.setFont(lblSummary.getFont().deriveFont(Font.BOLD, 15));
        lblSummary.setForeground(Color.BLUE);
        content.add(lblSummary);

        double leftovers = 0;
        int totalPeople = vikingCount + data.totalSlaves;

        if (!data.visits.isEmpty()) {
            leftovers = data.visits.get(data.visits.size() - 1).remainingFood;
        }

        double possibleDays = (totalPeople > 0) ? leftovers / (totalPeople * 0.1) : 0;

        if (data.possible) {
            content.add(getColoredLabel("✅ Поход осуществим!", new Color(0, 153, 0)));
            content.add(new JLabel(String.format("Общее время: %.2f часов (~%.2f дней)", data.totalTime, data.totalTime / 24)));
            content.add(new JLabel(String.format("Осталось еды: %.2f мер (~%.2f дней на %d чел.)",
                    leftovers, possibleDays, totalPeople)));
            content.add(new JLabel(String.format("Захвачено рабов: %d", data.totalSlaves)));
            content.add(new JLabel("Собрано лута:"));
            for (String line : data.lootList) {
                content.add(new JLabel("   - " + line));
            }
        } else {
            content.add(getColoredLabel("⚠️ Поход НЕосуществим:", Color.RED));
            if (data.foodRanOut) {
                content.add(getColoredLabel("- Закончились припасы", Color.RED));
            } else if (data.tooLong) {
                content.add(getColoredLabel("- Общее время превышает 60 дней", Color.RED));
            } else if (data.noSuccess) {
                content.add(getColoredLabel("- Ни одна атака не удалась", Color.RED));
            }
        }

        content.add(Box.createVerticalStrut(8));
        JLabel lblEnd = new JLabel("=================================");
        lblEnd.setFont(lblEnd.getFont().deriveFont(Font.BOLD, 14));
        lblEnd.setForeground(Color.BLUE);
        content.add(lblEnd);

        JScrollPane scrollPane = new JScrollPane(content);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        getContentPane().removeAll();
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JButton btnGo = new JButton("Поехать в поход");
        btnGo.setEnabled(data.possible && enableGoButton);

        btnGo.addActionListener(e -> {
            if (data.possible) {
                int days = (int) Math.round(data.totalTime / 24);
                Calendar.DateRangeResult result = Calendar.calculateDateRange(days);
                if (result == null) {
                    JOptionPane.showMessageDialog(this, "Поход отменён или невозможен.");
                    return;
                }

                takeFoodFromInventoryAndAddLeftovers(data);
                addSlavesAndLoot(data);
                Vikings.increaseVikingsAge();
                SatelitePanel.refreshVikingsList();
                JOptionPane.showMessageDialog(this, "Добыча успешно добавлена в инвентарь!");
                AttackHistoryManager.getInstance().addAttack(data, result.startDate, days);
            } else {
                JOptionPane.showMessageDialog(this, "Поход неосуществим!");
            }
        });

        JPanel panelButtons = new JPanel();
        if (enableGoButton) {
            panelButtons.add(btnGo);
        }
        add(panelButtons, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        currentVikingNames.clear();
    }

    /**
     * Рассчитывает, какое количество еды нужно забрать из инвентаря,
     * и обновляет запасы с учётом остатков после похода.
     *
     * @param data данные отчёта о походе
     */
    private void takeFoodFromInventoryAndAddLeftovers(Report.ReportData data) {
        double leftovers = 0;
        if (!data.visits.isEmpty()) {
            leftovers = data.visits.get(data.visits.size() - 1).remainingFood;
        }

        double reserve = 2 * vikingCount;
        Inventory.setFood(Inventory.getFood() - reserve + leftovers);
    }

    /**
     * Добавляет захваченных рабов и собранную добычу в инвентарь.
     *
     * @param data данные отчёта
     */
    private void addSlavesAndLoot(Report.ReportData data) {
        Inventory.setSlaves(Inventory.getSlaves() + data.totalSlaves);
        for (Report.ReportData.CityVisit visit : data.visits) {
            if (visit.success && visit.loots != null) {
                for (Loot loot : visit.loots) {
                    Inventory.addProduct(loot);
                }
            }
        }
    }

    /**
     * Создаёт JLabel с указанным цветом текста.
     *
     * @param text текст метки
     * @param color цвет текста
     * @return настроенный {@link JLabel}
     */
    private JLabel getColoredLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));
        return label;
    }

    /**
     * Создаёт JLabel с жирным стилем текста.
     *
     * @param text текст метки
     * @return настроенный {@link JLabel}
     */
    private JLabel getBoldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 14f));
        return label;
    }
}