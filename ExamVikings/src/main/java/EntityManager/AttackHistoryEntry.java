package EntityManager;

import Entity.Report;
import com.mycompany.examvikings.GUI.Calendar;
import java.time.LocalDate;

/**
 * Класс, представляющий запись о прошлом набеге викингов.
 * Содержит ссылку на данные отчёта и информацию о временных рамках похода.
 */
public class AttackHistoryEntry {
    
    /**
     * Данные отчёта о набеге.
     */
    private final Report.ReportData reportData;

    /**
     * Дата начала похода.
     */
    private final LocalDate startDate;

    /**
     * Общая продолжительность похода в днях.
     */
    private final int days;

    /**
     * Создаёт новый экземпляр записи истории набега.
     *
     * @param reportData данные отчёта о набеге
     * @param startDate дата начала похода
     * @param days продолжительность похода в днях
     */
    public AttackHistoryEntry(Report.ReportData reportData, LocalDate startDate, int days) {
        this.reportData = reportData;
        this.startDate = startDate;
        this.days = days;
    }

    /**
     * Возвращает данные отчёта о набеге.
     *
     * @return объект {@link Report.ReportData}
     */
    public Report.ReportData getReportData() {
        return reportData;
    }

    /**
     * Формирует строковое представление временного диапазона похода.
     * Например: "5 мая — 20 августа 823 года".
     *
     * @return строка с датами начала и окончания похода
     */
    public String getDateRangeString() {
        LocalDate endDate = startDate.plusDays(days);
        return "%d %s — %d %s %d года".formatted(
                startDate.getDayOfMonth(), Calendar.getMonthName(startDate.getMonthValue()),
                endDate.getDayOfMonth(), Calendar.getMonthName(endDate.getMonthValue()), endDate.getYear()
        );
    }

    /**
     * Возвращает строковое представление объекта для отображения в списке.
     * Используется, например, в {@link JList} для отображения истории набегов.
     *
     * @return строка вида "Набег от 5 мая — 20 августа 823 года"
     */
    @Override
    public String toString() {
        return "Набег от " + getDateRangeString();
    }
}