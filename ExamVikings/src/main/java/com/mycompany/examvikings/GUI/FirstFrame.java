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

        // Создаем панель с картинкой
        JPanel panel = Design.createPanelWithPhoto(Design.getFirstImage());
        panel.setLayout(null); // Абсолютное позиционирование

        // Заголовок
        JLabel titleLabel = new JLabel("Пророчество Викингов", SwingConstants.CENTER);
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(230, 102, 24));
        titleLabel.setBounds(50, 100, 700, 100);
        panel.add(titleLabel);

        // Подзаголовок справа
        JLabel subtitleLabel = new JLabel("created by Arina", SwingConstants.RIGHT);
        subtitleLabel.setFont(Design.getBaseFont());
        subtitleLabel.setBounds(450, 220, 300, 50);
        panel.add(subtitleLabel);

        // Кастомная кнопка с эффектами
        Design.CustomButton startButton = new Design.CustomButton("Начать");
        startButton.setFont(Design.getBaseFont());
        startButton.setForeground(Color.WHITE);
        startButton.setBounds(300, 350, 200, 50);
        startButton.addActionListener(e -> {
            dispose();
            LoreFrame lf = new LoreFrame();
        });

        panel.add(startButton);

        setContentPane(panel);
        setVisible(true);
    }
}