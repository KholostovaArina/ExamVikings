package com.mycompany.examvikings.GUI;

import EntityManager.AttackHistoryEntry;
import EntityManager.AttackHistoryManager;
import Entity.Report;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Класс, представляющий окно истории набегов.
 * Отображает список всех прошедших набегов и позволяет просмотреть отчёт о каждом из них.
 */
public class HistoryAttacks extends JFrame {

    /**
     * Список строкового представления набегов для отображения пользователю.
     */
    private final JList<String> attackList;

    /**
     * Модель списка для хранения записей о набегах.
     */
    private final DefaultListModel<String> listModel;

    /**
     * Сообщение, отображаемое, если история набегов пуста.
     */
    private final JLabel emptyLabel;

    /**
     * Панель с {@link CardLayout}, которая переключается между списком и сообщением "Набегов пока нет".
     */
    private final JPanel contentPanel;

    /**
     * Создаёт новое окно истории набегов.
     * Настраивает интерфейс, подключает обработчики событий и отображает историю.
     */
    public HistoryAttacks() {
        setTitle("История набегов");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        attackList = new JList<>(listModel);
        attackList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scrollPane = new JScrollPane(attackList);
        emptyLabel = new JLabel("<html><center>Набегов пока нет</center></html>", SwingConstants.CENTER);
        emptyLabel.setFont(emptyLabel.getFont().deriveFont(Font.ITALIC, 16f));

        contentPanel = new JPanel();
        CardLayout layout = new CardLayout();
        contentPanel.setLayout(layout);
        contentPanel.add(scrollPane, "attacks");
        contentPanel.add(emptyLabel, "empty");

        add(contentPanel, BorderLayout.CENTER);

        // Обработка двойного клика — показываем отчет по выбранному набегу
        attackList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !attackList.isSelectionEmpty()) {
                    int index = attackList.getSelectedIndex();
                    Report.ReportData data = AttackHistoryManager.getInstance().getAttack(index);
                    if (data != null) {
                        ReportFrame frame = new ReportFrame();
                        frame.showReport(data, false); // открываем существующий отчет
                    }
                }
            }
        });

        // При закрытии окна возвращаемся на главный экран
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });
    }

    /**
     * Обновляет содержимое панели: показывает либо список набегов, либо сообщение об их отсутствии.
     */
    private void updateContent() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        if (listModel.isEmpty()) {
            cl.show(contentPanel, "empty");
        } else {
            cl.show(contentPanel, "attacks");
        }
    }

    /**
     * Загружает историю набегов из менеджера и обновляет отображение.
     */
    private void loadAttacks() {
        listModel.clear();

        List<AttackHistoryEntry> attacks = AttackHistoryManager.getInstance().getHistory();
        for (AttackHistoryEntry entry : attacks) {
            listModel.addElement(entry.toString());
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
            loadAttacks(); // обновляем список при открытии
        }
        super.setVisible(visible);
    }
}