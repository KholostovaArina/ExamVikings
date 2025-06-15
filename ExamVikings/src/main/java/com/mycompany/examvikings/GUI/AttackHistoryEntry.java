package com.mycompany.examvikings.GUI;

import Entity.Report;
import java.time.LocalDate;

public class AttackHistoryEntry {
    private final Report.ReportData reportData;
    private final LocalDate startDate;
    private final int days;

    public AttackHistoryEntry(Report.ReportData reportData, LocalDate startDate, int days) {
        this.reportData = reportData;
        this.startDate = startDate;
        this.days = days;
    }

    public Report.ReportData getReportData() {
        return reportData;
    }

    public String getDateRangeString() {
        LocalDate endDate = startDate.plusDays(days);
        return "%d %s — %d %s %d года".formatted(
                startDate.getDayOfMonth(), Calendar.getMonthName(startDate.getMonthValue()),
                endDate.getDayOfMonth(), Calendar.getMonthName(endDate.getMonthValue()), endDate.getYear()
        );
    }

    @Override
    public String toString() {
        return "Набег от " + getDateRangeString();
    }
}