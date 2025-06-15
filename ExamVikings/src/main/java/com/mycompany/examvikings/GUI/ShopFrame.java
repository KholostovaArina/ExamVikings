package com.mycompany.examvikings.GUI;

import Entity.*;
import javax.swing.*;
import java.awt.*;

public class ShopFrame {

    public static void createShopFrame() {
        JFrame frame = new JFrame("Shop");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });

        // Главная панель с BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // PANELS FOR CARDLAYOUT
        CardLayout cardLayout = new CardLayout();
        JPanel centerPanel = new JPanel(cardLayout);

        // Панель 1 — серая
        JPanel buyVikingPanel = createBuyVikingPanel();

        // Панель 2 — белая
        JPanel sellInventoryPanel = (new SellInventoryPanel()).create();

        // Добавляем панели с именами
        centerPanel.add(buyVikingPanel, "BUY");
        centerPanel.add(sellInventoryPanel, "SELL");

        // Панель кнопок снизу
        JPanel buttonsPanel = new JPanel();
        JButton buyButton = new JButton("Купить викинга");
        JButton sellButton = new JButton("Продать инвентарь");

        buttonsPanel.add(buyButton);
        buttonsPanel.add(sellButton);

        // Обработчики кнопок
        buyButton.addActionListener(e -> cardLayout.show(centerPanel, "BUY"));
        sellButton.addActionListener(e -> cardLayout.show(centerPanel, "SELL"));

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(false);
        frame.setVisible(true);
    }

    private static JPanel createBuyVikingPanel() {
        JPanel buyVikingPanel = new JPanel(new BorderLayout());
        buyVikingPanel.setBackground(Color.LIGHT_GRAY);
        JLabel silverLabel1 = new JLabel("Серебро : " + Inventory.getSilver());
        silverLabel1.setHorizontalAlignment(SwingConstants.RIGHT);
        buyVikingPanel.add(silverLabel1, BorderLayout.NORTH);

        return buyVikingPanel;
    }
}
