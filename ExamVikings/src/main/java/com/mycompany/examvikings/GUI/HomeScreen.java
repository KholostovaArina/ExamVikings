package com.mycompany.examvikings.GUI;

import Design.Design;
import EntityManager.Cities;
import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JFrame {
    public HomeScreen() {
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

        // Ещё отступ до кнопки
        panel.add(Box.createVerticalStrut(70));

        // Кнопка по центру
        Design.CustomButton historyButton = new Design.CustomButton("История предсказаний");
        historyButton.setFont(Design.getBaseFont());
        historyButton.setForeground(Color.WHITE);
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyButton.setMaximumSize(new Dimension(400, 100));

        Design.CustomButton predictButton = new Design.CustomButton("Предсказать набег");
        predictButton.setFont(Design.getBaseFont());
        predictButton.setForeground(Color.WHITE);
        predictButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        predictButton.setMaximumSize(new Dimension(400, 200));
        predictButton.addActionListener(e ->{
            Cities.loadCitiesFromDB();
            dispose();
            MainFrame.createMainFrame();
        });
        
        Design.CustomButton shopButton = new Design.CustomButton("Пойти на рынок");
        shopButton.setFont(Design.getBaseFont());
        shopButton.setForeground(Color.WHITE);
        shopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shopButton.setMaximumSize(new Dimension(400, 100));

        panel.add(historyButton);
        panel.add(Box.createVerticalStrut(70));
        panel.add(predictButton);
        panel.add(Box.createVerticalStrut(70));
        panel.add(shopButton);

        // Пружина - чтобы всё было по центру, вне зависимости от размера
        panel.add(Box.createVerticalGlue());

        setContentPane(panel);
        setVisible(true);
    }
}
