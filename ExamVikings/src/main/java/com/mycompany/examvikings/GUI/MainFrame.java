package com.mycompany.examvikings.GUI;

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
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        // Главная горизонтальная разбивка: слева — mainLeftPanel, справа — rightPanel
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createMainLeftPanel(), createRightPanel());
        mainSplitPane.setResizeWeight(0.7);
        mainSplitPane.setDividerLocation(0.7);

        frame.add(mainSplitPane);
        frame.setVisible(true);
    }

    // Создание левой части (панель с контентом + кнопки "Назад"/"Далее")
    private static Component createMainLeftPanel() {
        JPanel mainLeftPanel = new JPanel(new BorderLayout());

        // Панель для карточек
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.add(DrakkarPanel.create(), "DRAKKAR");
        cardPanel.add(SatelitePanel.create(), "SATELLITE");
        cardPanel.add(RoutePanel.create(), "ROUTE");
        cardLayout.show(cardPanel, "DRAKKAR");

        // Нижняя панель с кнопками "Назад" и "Далее"
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton buttonBack = new JButton("Назад");
        JButton buttonNext = new JButton("Далее");

        updateNavigationButtons(buttonBack, buttonNext, "DRAKKAR");

        buttonBack.addActionListener(getNavigationAction(buttonBack, buttonNext, false));
        buttonNext.addActionListener(getNavigationAction(buttonBack, buttonNext, true));

        bottomLeftPanel.add(buttonBack);
        bottomLeftPanel.add(buttonNext);

        // Добавляем всё в основную левую панель
        mainLeftPanel.add(cardPanel, BorderLayout.CENTER);
        mainLeftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);

        return mainLeftPanel;
    }

    // Создание правой части (панель с информацией + кнопка "Готово" снизу)
    private static Component createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Верхняя часть — отображение информации
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        infoPanel.add(new JLabel("Выбранный элемент:"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        infoPanel.add(new JLabel("Драккар: Морской волк"));
        infoPanel.add(new JLabel("Спутник: Odin-1"));
        infoPanel.add(new JLabel("Маршрут: Viking Route"));

        // Нижняя часть — кнопка "Готово"
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonReady = new JButton("Готово");
        bottomRightPanel.add(buttonReady);

        // Сборка правой панели
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomRightPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    // Обработчик для кнопок "Назад" и "Далее"
    private static ActionListener getNavigationAction(JButton backBtn, JButton nextBtn, boolean isNext) {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int currentIndex = getCurrentCardIndex();
                if (isNext && currentIndex < CARD_NAMES.length - 1) {
                    cardLayout.show(cardPanel, CARD_NAMES[currentIndex + 1]);
                } else if (!isNext && currentIndex > 0) {
                    cardLayout.show(cardPanel, CARD_NAMES[currentIndex - 1]);
                }
                updateNavigationButtons(backBtn, nextBtn, CARD_NAMES[getCurrentCardIndex()]);
            }
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