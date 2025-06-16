package com.mycompany.examvikings.GUI;

import Entity.*;
import EntityManager.HistoryReportsManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * Класс, представляющий окно истории отчётов о набегах.
 * Отображает список всех сохранённых отчётов и позволяет просмотреть детали по двойному клику.
 */
public class HistoryReports extends JFrame {

    /**
     * Список строкового представления отчётов для отображения пользователю.
     */
    private JList<String> reportList;

    /**
     * Модель списка для хранения записей об отчётах.
     */
    private final DefaultListModel<String> listModel;

    /**
     * Сообщение, отображаемое, если история отчётов пуста.
     */
    private final JLabel emptyLabel;

    /**
     * Панель с {@link CardLayout}, которая переключается между списком и сообщением "Отчётов пока нет".
     */
    private JPanel contentPanel = new JPanel();

    /**
     * Создаёт новое окно истории отчётов.
     * Настраивает интерфейс, подключает обработчики событий и отображает историю.
     */
    public HistoryReports() {
        setTitle("История отчетов");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        reportList = new JList<>(listModel);
        reportList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(reportList);

        emptyLabel = new JLabel("Отчётов пока нет", SwingConstants.CENTER);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 16));

        // Настройка контентной панели с CardLayout
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(scrollPane, "scroll");
        contentPanel.add(emptyLabel, "empty");

        add(contentPanel, BorderLayout.CENTER);

        // Обновляем состояние панели
        updateContent();

        // Обработка двойного клика — показываем выбранный отчёт
        reportList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !reportList.isSelectionEmpty()) {
                    int index = reportList.getSelectedIndex();
                    if (index != -1) {
                        Report.ReportData selectedReport = HistoryReportsManager.getInstance().getReport(index);
                        if (selectedReport != null) {
                            ReportFrame frame = new ReportFrame();
                            frame.showReport(selectedReport);
                        }
                    }
                }
            }
        });

        // При закрытии окна возвращаемся на главный экран
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new HomeScreen();
            }
        });
    }

    /**
     * Обновляет содержимое панели: показывает либо список отчётов, либо сообщение об их отсутствии.
     */
    private void updateContent() {
        if (reportList.getModel().getSize() == 0) {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "empty");
        } else {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "scroll");
        }
    }

    /**
     * Загружает историю отчётов из менеджера и обновляет отображение.
     */
    private void loadHistory() {
        listModel.clear();

        List<Report.ReportData> reports = HistoryReportsManager.getInstance().getHistory();
        for (int i = 0; i < reports.size(); i++) {
            listModel.addElement("Отчет №" + reports.get(i).reportId);
        }

        updateContent();
    }

    /**
     * Переопределённый метод, который загружает историю перед отображением окна.
     *
     * @param visible флаг видимости окна
     */
    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            loadHistory(); // обновляем список при открытии
        }
        super.setVisible(visible);
    }
}