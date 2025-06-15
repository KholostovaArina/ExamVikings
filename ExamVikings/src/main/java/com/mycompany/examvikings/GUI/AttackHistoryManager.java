package com.mycompany.examvikings.GUI;

import Entity.Report;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttackHistoryManager {
    private static final AttackHistoryManager instance = new AttackHistoryManager();
    private final List<AttackHistoryEntry> historyEntries = new ArrayList<>();

    private AttackHistoryManager() {}

    public static AttackHistoryManager getInstance() {
        return instance;
    }

    // Добавляем отчёт вместе с датой
    public void addAttack(Report.ReportData data, LocalDate startDate, int days) {
        historyEntries.add(new AttackHistoryEntry(data, startDate, days));
    }

    // Получаем все записи
    public List<AttackHistoryEntry> getHistory() {
        return new ArrayList<>(historyEntries);
    }

    // Получаем конкретный отчет
    public Report.ReportData getAttack(int index) {
        if (index >= 0 && index < historyEntries.size()) {
            return historyEntries.get(index).getReportData();
        }
        return null;
    }

    // Очистка (опционально)
    public void clear() {
        historyEntries.clear();
    }
}