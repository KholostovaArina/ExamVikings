package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import EntityManager.Vikings;
import com.mycompany.examvikings.GUI.HomeScreen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame {
    private static JPanel cardPanel;
    private static CardLayout cardLayout;
    private static final String[] CARD_NAMES = {"DRAKKAR", "SATELLITE", "ROUTE"};

    public static void createMainFrame() {
        JFrame frame = new JFrame("Система планировщика набегов викингов");
        
        frame.getContentPane().setBackground(new Color(45,56,61));
        
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        // Главная горизонтальная разбивка: слева — mainLeftPanel, справа — rightPanel
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createMainLeftPanel(),
                SelectionPanel.createRightPanel());
        mainSplitPane.setResizeWeight(0.7);
        mainSplitPane.setDividerLocation(0.7);
        mainSplitPane.setOpaque(false);

        frame.add(mainSplitPane);
        frame.setVisible(true);
    }

    // Создание левой части (панель с контентом + кнопки "Назад"/"Далее")
    private static Component createMainLeftPanel() {
        JPanel mainLeftPanel = new JPanel(new BorderLayout());

        // Панель для карточек
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(DrakkarPanel.create(), "DRAKKAR");
        cardPanel.add(SatelitePanel.create(Vikings.getAllVikings()), "SATELLITE");
        cardPanel.add(RoutePanel.create(), "ROUTE");
        cardLayout.show(cardPanel, "DRAKKAR");
        
        // Нижняя панель с кнопками "Назад" и "Далее"
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Design.CustomButton buttonBack = new Design.CustomButton("Назад");
        Design.CustomButton buttonNext = new Design.CustomButton("Далее"); 
        
        updateNavigationButtons(buttonBack, buttonNext, "DRAKKAR");

        buttonBack.addActionListener(getNavigationAction(buttonBack, buttonNext, false));
        buttonNext.addActionListener(getNavigationAction(buttonBack, buttonNext, true));

        bottomLeftPanel.add(buttonBack);
        bottomLeftPanel.add(buttonNext);
        bottomLeftPanel.setOpaque(false);
        Design.setFontForAllComponents(bottomLeftPanel, Color.white);

        // Добавляем всё в основную левую панель
        mainLeftPanel.add(cardPanel, BorderLayout.CENTER);
        mainLeftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        mainLeftPanel.setOpaque(false);
        
        Design.makeAllNonOpaque(mainLeftPanel);

        return mainLeftPanel;
    }

    // Обработчик для кнопок "Назад" и "Далее"
    private static ActionListener getNavigationAction(JButton backBtn, JButton nextBtn, boolean isNext) {
        return (ActionEvent e) -> {
            int currentIndex = getCurrentCardIndex();
            if (isNext && currentIndex < CARD_NAMES.length - 1) {
                cardLayout.show(cardPanel, CARD_NAMES[currentIndex + 1]);
            } else if (!isNext && currentIndex > 0) {
                cardLayout.show(cardPanel, CARD_NAMES[currentIndex - 1]);
            }
            updateNavigationButtons(backBtn, nextBtn, CARD_NAMES[getCurrentCardIndex()]);
        };
    }

    // Получаем индекс текущей активной карты
    private static int getCurrentCardIndex() {
        for (int i = 0; i < CARD_NAMES.length; i++) {
            Component component = cardPanel.getComponent(i);
            if (component.isVisible()) {
                return i;
            }
        }
        return 0;
    }

    // Обновление состояния кнопок "Назад" и "Далее"
    private static void updateNavigationButtons(JButton backBtn, JButton nextBtn, String currentCard) {
        switch (currentCard) {
            case "DRAKKAR" -> {
                backBtn.setEnabled(false);
                nextBtn.setEnabled(true);
            }
            case "SATELLITE" -> {
                backBtn.setEnabled(true);
                nextBtn.setEnabled(true);
            }
            case "ROUTE" -> {
                backBtn.setEnabled(true);
                nextBtn.setEnabled(false);
            }
            default -> {
                backBtn.setEnabled(true);
                nextBtn.setEnabled(true);
            }
        }
    }
}