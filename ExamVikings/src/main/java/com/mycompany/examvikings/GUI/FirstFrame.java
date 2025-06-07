package com.mycompany.examvikings.GUI;

import Design.Design;
import javax.swing.*;
import java.awt.*;

public class FirstFrame extends JFrame {
    public FirstFrame() {
        setTitle("Пророчество Викингов");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Панель с фоном-картинкой
        JPanel panel = Design.createPanelWithPhoto(Design.getFirstImage());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        // Отступ сверху
        panel.add(Box.createVerticalStrut(80));

        // Заголовок по центру
        JLabel titleLabel = new JLabel("Пророчество Викингов", SwingConstants.CENTER);
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(230, 102, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        // Отступ между заголовком и подзаголовком
        panel.setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
        panel.add(Box.createVerticalStrut(80));

        // Подзаголовок справа
        JPanel subPanel = new JPanel(new BorderLayout());
        subPanel.setOpaque(false); // фон не закрывает
        JLabel subtitleLabel = new JLabel("created by Arina", SwingConstants.RIGHT);
        subtitleLabel.setFont(Design.getBaseFont().deriveFont(28f));
        subPanel.add(subtitleLabel, BorderLayout.EAST);
        subPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(subPanel);

        // Ещё отступ до кнопки
        panel.add(Box.createVerticalStrut(70));

        // Кнопка по центру
        Design.CustomButton startButton = new Design.CustomButton("Начать");
        startButton.setFont(Design.getBaseFont());
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(250, 100));
        startButton.addActionListener(e -> {
            dispose();
            new LoreFrame();
        });
        panel.add(startButton);

        // Пружина - чтобы всё было по центру, вне зависимости от размера
        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        setVisible(true);
    }
}