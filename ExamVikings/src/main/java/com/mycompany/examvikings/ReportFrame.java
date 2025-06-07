package com.mycompany.examvikings;

import static com.mycompany.examvikings.Report.food;
import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;

public class ReportFrame extends JFrame {

    public ReportFrame() {
        setTitle("Отчёт");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void showReport(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        Report.ReportData data = Report.generateReportData(vikings, cityList, drakkar);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        StringBuilder sb = new StringBuilder();

        sb.append("============= Отчёт о походе №").append(data.reportId).append(" =============\n\n");

        sb.append("Маршрут: ");
        for (City city : cityList) {
            sb.append(city.getName()).append(" → ");
        }
        sb.append(data.startCity.getName()).append("\n\n");

        double currentFood = food; // Чтобы отслеживать уменьшение еды

        for (Report.ReportData.CityVisit visit : data.visits) {
            sb.append(String.format("Город: %s (%s)\n", visit.city.getName(), visit.city.getCityType()));
            sb.append(String.format("Время в пути: %.2f часов (~%.2f дней)\n", visit.time, visit.time / 24));

            if (visit.success) {
                sb.append("Атака успешна!\n");

                // Список лута по городу
                sb.append("Добыча:\n");
                visit.loots.stream()
                        .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()))
                        .forEach((name, count) -> sb.append("   - ").append(name).append(" x").append(count).append("\n"));

                // Рабы только этого города
                int slavesThisCity = visit.newSlaves;
                sb.append(String.format("Рабов взято: %d\n\n", slavesThisCity));
            } else {
                sb.append("Атака провалена.\n\n");
            }

            // Проверяем, ели ли из лута
            if (currentFood > 0 && food <= 0) {
                sb.append("⚠ Викинги начали есть из добычи\n\n");
            }
            currentFood = food;
        }

        sb.append("------------- ИТОГИ -------------\n");

        if (data.possible) {
            sb.append("✅ Поход осуществим!\n");
            sb.append(String.format("Общее время: %.2f часов (~%.2f дней)\n", data.totalTime, data.totalTime / 24));
            sb.append(String.format("Осталось еды: %.2f мер (~%.2f дней)\n", food, food * 10));
            sb.append(String.format("Захвачено рабов: %d\n", data.totalSlaves));
            sb.append("Собрано лута:\n");
            for (String loot : data.lootList) {
                sb.append("   - ").append(loot).append("\n");
            }
        } else {
            sb.append("⚠ Поход НЕосуществим:\n");
            if (data.tooLong) {
                sb.append("- Общее время превышает 60 дней\n");
            }
            if (data.foodRanOut) {
                sb.append("- Закончились припасы\n");
            }
            if (data.noSuccess) {
                sb.append("- Ни одна атака не удалась\n");
            }
        }

        sb.append("=================================\n");

        textArea.setText(sb.toString());

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}
