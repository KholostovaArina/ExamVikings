package com.mycompany.examvikings.GUI;

import Entity.Report;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class HistoryAttacks extends JFrame {
    private final JList<String> attackList;
    private final DefaultListModel<String> listModel;
    private final JLabel emptyLabel;
    private final JPanel contentPanel;

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

        // Двойной клик — показываем отчет
        attackList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && !attackList.isSelectionEmpty()) {
                    int index = attackList.getSelectedIndex();
                    Report.ReportData data = AttackHistoryManager.getInstance().getAttack(index);
                    if (data != null) {
                        ReportFrame frame = new ReportFrame();
                        frame.showReport(data); // открываем существующий отчет
                    }
                }
            }
        });

        // Кнопка закрытия
        JButton closeButton = new JButton("Закрыть");
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });
    }

    private void updateContent() {
        CardLayout cl = (CardLayout) contentPanel.getLayout();
        if (listModel.isEmpty()) {
            cl.show(contentPanel, "empty");
        } else {
            cl.show(contentPanel, "attacks");
        }
    }

    private void loadAttacks() {
        listModel.clear();

        List<AttackHistoryEntry> attacks = AttackHistoryManager.getInstance().getHistory();
        for (AttackHistoryEntry entry : attacks) {
            listModel.addElement(entry.toString());
        }

        updateContent();
    }

    @Override
    public void setVisible(boolean visible) {
        if (visible) {
            loadAttacks(); // обновляем список при открытии
        }
        super.setVisible(visible);
    }
}