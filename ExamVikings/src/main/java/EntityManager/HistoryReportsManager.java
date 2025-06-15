package EntityManager;

import Entity.Report;
import java.util.ArrayList;
import java.util.List;

public class HistoryReportsManager {
    private static final HistoryReportsManager instance = new HistoryReportsManager();
    private final List<Report.ReportData> history = new ArrayList<>();

    private HistoryReportsManager() {}

    public static HistoryReportsManager getInstance() {
        return instance;
    }

    public void addReport(Report.ReportData report) {
        history.add(report);
    }

    public List<Report.ReportData> getHistory() {
        return history;
    }

    public Report.ReportData getReport(int index) {
        if (index >= 0 && index < history.size()) {
            return history.get(index);
        }
        return null;
    }
}