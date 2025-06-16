package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.GUI.Prediction.MainFrame;
import Design.Design;
import EntityManager.Cities;
import javax.swing.*;
import java.awt.*;

public class HomeScreen extends JFrame {
    public HomeScreen() {
        setTitle("Пророчество Викингов");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Панель с фоном-картинкой
        JPanel panel = Design.createPanelWithPhoto(Design.getFirstImage());
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);

        panel.add(Box.createVerticalStrut(80));

        JLabel titleLabel = new JLabel("Пророчество Викингов", SwingConstants.CENTER);
        titleLabel.setFont(Design.getBigFont());
        titleLabel.setForeground(new Color(45, 56, 61));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);

        panel.add(Box.createVerticalStrut(70));

        Design.CustomButton historyReportButton = new Design.CustomButton("История предсказаний");
        historyReportButton.setFont(Design.getBaseFont().deriveFont(24f));
        historyReportButton.addActionListener(e -> {
            new HistoryReports().setVisible(true);
            setVisible(false);
        });
        historyReportButton.setForeground(Color.WHITE);
        historyReportButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyReportButton.setMaximumSize(new Dimension(400, 100));
        
        Design.CustomButton historyButton = new Design.CustomButton("История набегов");
        historyButton.setFont(Design.getBaseFont().deriveFont(24f));
        historyButton.addActionListener(e -> {
            new HistoryAttacks().setVisible(true);
            setVisible(false);
        });
        historyButton.setForeground(Color.WHITE);
        historyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        historyButton.setMaximumSize(new Dimension(400, 100));

        Design.CustomButton predictButton = new Design.CustomButton("Предсказать набег");
        predictButton.setFont(Design.getBaseFont().deriveFont(24f));
        predictButton.setForeground(Color.WHITE);
        predictButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        predictButton.setMaximumSize(new Dimension(400, 100));
        predictButton.addActionListener(e ->{
            Cities.loadCitiesFromDB();
            dispose();
            MainFrame.createMainFrame();
        });
        
        Design.CustomButton shopButton = new Design.CustomButton("Пойти на рынок");
        shopButton.addActionListener(e ->{
            dispose();
            ShopFrame.createShopFrame();
        });
        shopButton.setFont(Design.getBaseFont().deriveFont(24f));
        shopButton.setForeground(Color.WHITE);
        shopButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        shopButton.setMaximumSize(new Dimension(400, 100));

        panel.add(historyButton);
        panel.add(Box.createVerticalStrut(70));
        panel.add(historyReportButton);
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
