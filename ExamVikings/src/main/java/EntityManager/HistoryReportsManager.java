package EntityManager;

import Entity.Report;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для управления историей отчётов о набегах.
 * Предоставляет методы для добавления, получения и хранения отчётов.
 */
public class HistoryReportsManager {
    
    /**
     * Единственный экземпляр класса (Singleton).
     */
    private static final HistoryReportsManager instance = new HistoryReportsManager();

    /**
     * Список всех отчётов, собранных за время работы приложения.
     */
    private final List<Report.ReportData> history = new ArrayList<>();

    /**
     * Приватный конструктор для предотвращения создания дополнительных экземпляров.
     */
    private HistoryReportsManager() {}

    /**
     * Возвращает единственный экземпляр класса.
     *
     * @return единственный экземпляр {@link HistoryReportsManager}
     */
    public static HistoryReportsManager getInstance() {
        return instance;
    }

    /**
     * Добавляет новый отчёт в историю.
     *
     * @param report данные отчёта о походе
     */
    public void addReport(Report.ReportData report) {
        history.add(report);
    }

    /**
     * Возвращает список всех отчётов.
     *
     * @return список объектов {@link Report.ReportData}
     */
    public List<Report.ReportData> getHistory() {
        return history;
    }

    /**
     * Возвращает отчёт по его индексу в списке.
     *
     * @param index индекс отчёта в списке
     * @return данные отчёта ({@link Report.ReportData}) или null, если индекс неверен
     */
    public Report.ReportData getReport(int index) {
        if (index >= 0 && index < history.size()) {
            return history.get(index);
        }
        return null;
    }
}