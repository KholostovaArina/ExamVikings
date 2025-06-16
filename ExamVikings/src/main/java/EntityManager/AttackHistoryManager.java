package EntityManager;

import Entity.Report;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, реализующий шаблон Singleton для управления историей набегов.
 * Предоставляет методы для добавления, получения и очистки записей о прошедших походах.
 */
public class AttackHistoryManager {
    
    /**
     * Единственный экземпляр класса (Singleton).
     */
    private static final AttackHistoryManager instance = new AttackHistoryManager();

    /**
     * Список записей об атаках/походах.
     */
    private final List<AttackHistoryEntry> historyEntries = new ArrayList<>();

    /**
     * Приватный конструктор для предотвращения создания дополнительных экземпляров.
     */
    private AttackHistoryManager() {}

    /**
     * Возвращает единственный экземпляр класса.
     *
     * @return единственный экземпляр {@link AttackHistoryManager}
     */
    public static AttackHistoryManager getInstance() {
        return instance;
    }

    /**
     * Добавляет новую запись о походе в историю.
     *
     * @param data данные отчёта о походе
     * @param startDate дата начала похода
     * @param days продолжительность похода в днях
     */
    public void addAttack(Report.ReportData data, LocalDate startDate, int days) {
        historyEntries.add(new AttackHistoryEntry(data, startDate, days));
    }

    /**
     * Возвращает копию списка всех записей истории набегов.
     *
     * @return список объектов {@link AttackHistoryEntry}
     */
    public List<AttackHistoryEntry> getHistory() {
        return new ArrayList<>(historyEntries);
    }

    /**
     * Возвращает данные отчёта о конкретном набеге по индексу.
     *
     * @param index индекс записи в списке
     * @return данные отчёта ({@link Report.ReportData}) или null, если индекс неверен
     */
    public Report.ReportData getAttack(int index) {
        if (index >= 0 && index < historyEntries.size()) {
            return historyEntries.get(index).getReportData();
        }
        return null;
    }

    /**
     * Очищает историю набегов (удаляет все записи).
     * Может использоваться при сбросе данных.
     */
    public void clear() {
        historyEntries.clear();
    }
}