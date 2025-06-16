package com.mycompany.examvikings.GUI;

import Design.Design;
import javax.swing.*;
import java.awt.*;

/**
 * Главное стартовое окно приложения "Пророчество Викингов".
 * Отображает заголовок, подзаголовок и кнопку для начала работы.
 */
public class FirstFrame extends JFrame {

    /**
     * Создаёт и настраивает главное окно приложения.
     * Устанавливает фоновое изображение, заголовок и кнопку "Начать".
     */
    public FirstFrame() {
        setTitle("Пророчество Викингов");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = Design.createPanelWithPhoto(Design.getFirstImage());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        panel.add(Box.createVerticalStrut(80));

        JLabel titleLabel = new JLabel("Пророчество Викингов", SwingConstants.CENTER);
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(45, 56, 61));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.setBorder(BorderFactory.createEmptyBorder(70, 70, 70, 70));
        panel.add(Box.createVerticalStrut(80));

        JPanel subPanel = new JPanel(new BorderLayout());
        subPanel.setOpaque(false);

        JLabel subtitleLabel = new JLabel("created by Arina", SwingConstants.RIGHT);
        subtitleLabel.setFont(Design.getBaseFont().deriveFont(28f));
        subPanel.add(subtitleLabel, BorderLayout.EAST);
        subPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        panel.add(subPanel);

        panel.add(Box.createVerticalStrut(70));

        Design.CustomButton startButton = new Design.CustomButton("Начать");
        startButton.setFont(Design.getBaseFont().deriveFont(24f));
        startButton.setForeground(Color.WHITE);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setMaximumSize(new Dimension(250, 100));
        startButton.addActionListener(e -> {
            dispose();
            new LoreFrame();
        });
        panel.add(startButton);

        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        setVisible(true);
    }
}