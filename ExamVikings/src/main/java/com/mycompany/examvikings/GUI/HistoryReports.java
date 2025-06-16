package com.mycompany.examvikings.GUI;

import Entity.*;
import EntityManager.HistoryReportsManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class HistoryReports extends JFrame {

    private JList<String> reportList;
    private final DefaultListModel<String> listModel;
    private final JLabel emptyLabel;
    private JPanel contentPanel = new JPanel();

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

        // Панель с CardLayout
        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(scrollPane, "scroll");
        contentPanel.add(emptyLabel, "empty");

        // Добавляем contentPanel в окно
        add(contentPanel, BorderLayout.CENTER);

        // Инициализируем отображение
        updateContent();

        // ----- Вот здесь добавлен MouseListener -----
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

        // Кнопку "Открыть отчет" убрал — она больше не нужна

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });
    }

    private void updateContent() {
        if (reportList.getModel().getSize() == 0) {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "empty");
        } else {
            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "scroll");
        }
    }

    private void loadHistory() {
        listModel.clear();

        List<Report.ReportData> reports = HistoryReportsManager.getInstance().getHistory();
        for (int i = 0; i < reports.size(); i++) {
            listModel.addElement("Отчет №" + reports.get(i).reportId);
        }

        updateContent();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            loadHistory(); // Обновляем историю при открытии окна
        }
        super.setVisible(visible);
    }
}
