package com.mycompany.examvikings;

import static com.mycompany.examvikings.Report.food;
import javax.swing.*;
import java.awt.*;
import java.util.stream.Collectors;
import java.util.Set;
import java.util.List;
import javax.swing.text.*;

public class ReportFrame extends JFrame {

    public ReportFrame() {
        setTitle("Отчёт о походе");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public void showReport(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        Report.ReportData data = Report.generateReportData(vikings, cityList, drakkar);

        JTextPane textArea = new JTextPane();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        StyledDocument doc = textArea.getStyledDocument();

        try {
            Style defaultStyle = doc.addStyle("default", null);
            StyleConstants.setFontFamily(defaultStyle, "Monospaced");
            StyleConstants.setFontSize(defaultStyle, 14);

            Style titleStyle = doc.addStyle("title", null);
            StyleConstants.setBold(titleStyle, true);
            StyleConstants.setForeground(titleStyle, Color.BLUE);

            Style successStyle = doc.addStyle("success", null);
            StyleConstants.setForeground(successStyle, new Color(0, 150, 0));

            Style warningStyle = doc.addStyle("warning", null);
            StyleConstants.setForeground(warningStyle, Color.RED);

            Style neutralStyle = doc.addStyle("neutral", null);
            StyleConstants.setForeground(neutralStyle, Color.BLACK);

            // Заголовок
            doc.insertString(doc.getLength(), "============= Отчёт о походе №" + data.reportId + " =============\n\n", titleStyle);

            // Маршрут
            doc.insertString(doc.getLength(), "Маршрут: ", neutralStyle);
            for (City city : cityList) {
                doc.insertString(doc.getLength(), city.getName() + " → ", neutralStyle);
            }
            doc.insertString(doc.getLength(), data.startCity.getName() + "\n\n", neutralStyle);

            double currentFood = food;

            // Визиты по городам
            for (Report.ReportData.CityVisit visit : data.visits) {
                doc.insertString(doc.getLength(), String.format("Город: %s (%s)\n", visit.city.getName(), visit.city.getCityType()), neutralStyle);
                doc.insertString(doc.getLength(), String.format("Время в пути: %.2f часов (~%.2f дней)\n", visit.time, visit.time / 24), neutralStyle);

                if (visit.success) {
                    doc.insertString(doc.getLength(), "Атака успешна!\n", successStyle);
                    doc.insertString(doc.getLength(), "Добыча: \n",  neutralStyle);
                    visit.loots.stream()
                            .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()))
                            .forEach((name, count) -> {
                                try {
                                    doc.insertString(doc.getLength(), String.format("   - %s x%d\n", name, count), neutralStyle);
                                } catch (Exception ignored) {}
                            });
                    if (visit.newSlaves > 0) {
                        doc.insertString(doc.getLength(), String.format("Рабов взято: %d\n\n", visit.newSlaves), neutralStyle);
                    } else {
                        doc.insertString(doc.getLength(), "\n", neutralStyle);
                    }
                } else {
                    doc.insertString(doc.getLength(), "Атака провалена.\n\n", neutralStyle);
                }

                if (currentFood > 0 && visit.remainingFood <= 0) {
                    doc.insertString(doc.getLength(), "⚠ Викинги начали есть из добычи\n\n", warningStyle);
                }
                currentFood = visit.remainingFood;
            }

            doc.insertString(doc.getLength(), "------------- ИТОГИ -------------\n", titleStyle);

            if (data.possible) {
                doc.insertString(doc.getLength(), "✅ Поход осуществим!\n", successStyle);
                doc.insertString(doc.getLength(), String.format("Общее время: %.2f часов (~%.2f дней)\n", data.totalTime, data.totalTime / 24), neutralStyle);
                double remainingFood = Math.max(0, food);
                doc.insertString(doc.getLength(), String.format("Осталось еды: %.2f мер (~%.2f дней)\n", remainingFood, remainingFood * 10), neutralStyle);
                doc.insertString(doc.getLength(), String.format("Захвачено рабов: %d\n", data.totalSlaves), neutralStyle);
                doc.insertString(doc.getLength(), "Собрано лута:\n", neutralStyle);
                for (String loot : data.lootList) {
                    doc.insertString(doc.getLength(), "   - " + loot + "\n", neutralStyle);
                }
            } else {
                doc.insertString(doc.getLength(), "⚠ Поход НЕосуществим:\n", warningStyle);

                if (data.foodRanOut) {
                    doc.insertString(doc.getLength(), "- Закончились припасы\n", warningStyle);
                } else {
                    if (data.tooLong) {
                        doc.insertString(doc.getLength(), "- Общее время превышает 60 дней\n", warningStyle);
                    } else {
                        if (data.noSuccess) {
                            doc.insertString(doc.getLength(), "- Ни одна атака не удалась\n", warningStyle);
                        }
                    }
                }
            }

            doc.insertString(doc.getLength(), "=================================\n", titleStyle);

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
        setVisible(true);
    }
}