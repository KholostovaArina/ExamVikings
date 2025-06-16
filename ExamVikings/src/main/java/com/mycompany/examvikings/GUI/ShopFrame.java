package com.mycompany.examvikings.GUI;

import Entity.Inventory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ShopFrame {

    private static JLabel sharedSilverLabel = new JLabel("Серебро: " + Inventory.getSilver(), SwingConstants.RIGHT);

    public static void createShopFrame() {
        JFrame frame = new JFrame("Магазин Викингов");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new HomeScreen();
            }
        });

        JPanel mainPanel = new JPanel(new BorderLayout());
        CardLayout cardLayout = new CardLayout();
        JPanel centerPanel = new JPanel(cardLayout);

        ShopVikingsPanel shopVikingsPanel = new ShopVikingsPanel();
        SellInventoryPanel sellInventoryPanel = new SellInventoryPanel();

        JPanel buyPanel = shopVikingsPanel.create(sharedSilverLabel);
        JPanel sellPanel = sellInventoryPanel.create(sharedSilverLabel);

        centerPanel.add(buyPanel, "BUY");
        centerPanel.add(sellPanel, "SELL");

        JPanel buttonsPanel = new JPanel();
        JButton buyButton = new JButton("Купить викинга");
        JButton sellButton = new JButton("Продать инвентарь");

        buyButton.addActionListener(e -> cardLayout.show(centerPanel, "BUY"));
        sellButton.addActionListener(e -> cardLayout.show(centerPanel, "SELL"));

        buttonsPanel.add(buyButton);
        buttonsPanel.add(sellButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void refreshAllSilver() {
        sharedSilverLabel.setText("Серебро: " + Inventory.getSilver());
    }
}