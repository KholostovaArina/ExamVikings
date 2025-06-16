package com.mycompany.examvikings.GUI;

import Design.Design;
import Entity.Inventory;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Класс, представляющий окно магазина.
 * Позволяет игроку покупать новых викингов или продавать добычу и рабов.
 */
public class ShopFrame {

    /**
     * Общая метка для отображения текущего количества серебра.
     * Используется во всех панелях магазина.
     */
    private static final JLabel sharedSilverLabel = new JLabel("Серебро: " + Inventory.getSilver(), SwingConstants.RIGHT);

    /**
     * Создаёт и отображает окно магазина с двумя вкладками:
     * - покупка викингов
     * - продажа инвентаря
     */
    public static void createShopFrame() {
        JFrame frame = new JFrame("Магазин Викингов");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // на весь экран

        // При закрытии окна возвращаемся на главный экран
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                new HomeScreen();
            }
        });

        // Основная панель с карточками
        JPanel mainPanel = new JPanel(new BorderLayout());
        CardLayout cardLayout = new CardLayout();
        JPanel centerPanel = new JPanel(cardLayout);

        // Создаем обе панели
        ShopVikingsPanel shopVikingsPanel = new ShopVikingsPanel();
        SellInventoryPanel sellInventoryPanel = new SellInventoryPanel();

        // Получаем UI-компоненты
        JPanel buyPanel = shopVikingsPanel.create(sharedSilverLabel);
        JPanel sellPanel = sellInventoryPanel.create(sharedSilverLabel);

        // Добавляем вкладки
        centerPanel.add(buyPanel, "BUY");
        centerPanel.add(sellPanel, "SELL");

        // Панель с кнопками переключения
        JPanel buttonsPanel = new JPanel();
        Design.CustomButton buyButton = new Design.CustomButton("Купить викинга");
        Design.CustomButton sellButton = new Design.CustomButton("Продать инвентарь");

        // Слушатели для навигации между вкладками
        buyButton.addActionListener(e -> cardLayout.show(centerPanel, "BUY"));
        sellButton.addActionListener(e -> cardLayout.show(centerPanel, "SELL"));

        buttonsPanel.add(buyButton);
        buttonsPanel.add(sellButton);

        // Компоновка интерфейса
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Обновляет значение метки серебра во всех частях магазина.
     */
    public static void refreshAllSilver() {
        sharedSilverLabel.setText("Серебро: " + Inventory.getSilver());
    }
}