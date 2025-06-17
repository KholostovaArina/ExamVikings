package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import EntityManager.Vikings;
import com.mycompany.examvikings.GUI.HomeScreen;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Класс, представляющий основное окно планирования набега.
 * Содержит три этапа: выбор драккара, выбор викингов и построение маршрута.
 */
public class MainFrame {
    
    /**
     * Панель, содержащая карточки для разных этапов планирования.
     */
    private static JPanel cardPanel;

    /**
     * Менеджер отображения карточек (позволяет переключаться между ними).
     */
    private static CardLayout cardLayout;

    /**
     * Массив имён карточек, соответствующих этапам планирования.
     */
    private static final String[] CARD_NAMES = {"DRAKKAR", "SATELLITE", "ROUTE"};
    
    /**
     * Конструктор по умолчанию.
     * Используется только для создания графического интерфеса.
     */
    public MainFrame() {
        // конструктор
    }

    /**
     * Создаёт и настраивает главное окно приложения для планирования набега.
     * Устанавливает макет, панели и обработчики событий.
     */
    public static void createMainFrame() {
        JFrame frame = new JFrame("Система планировщика набегов викингов");
        frame.getContentPane().setBackground(new Color(45, 56, 61));
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // При закрытии возвращаемся на главный экран
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                new HomeScreen();
            }
        });

        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);

        // Горизонтальное разделение: слева — основная панель, справа — детали
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createMainLeftPanel(),
                SelectionPanel.createRightPanel());
        mainSplitPane.setResizeWeight(0.7);
        mainSplitPane.setDividerLocation(0.7);
        mainSplitPane.setOpaque(false);

        frame.add(mainSplitPane);
        frame.setVisible(true);
    }

    /**
     * Создаёт левую часть интерфейса с карточками (для переключения между этапами)
     * и кнопками "Назад" и "Далее".
     *
     * @return панель с контентом слева
     */
    private static Component createMainLeftPanel() {
        JPanel mainLeftPanel = new JPanel(new BorderLayout());

        // Панель с этапами выбора
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);
        cardPanel.add(DrakkarPanel.create(), "DRAKKAR");
        cardPanel.add(SatelitePanel.create(Vikings.getAllVikings()), "SATELLITE");
        cardPanel.add(RoutePanel.create(), "ROUTE");
        cardLayout.show(cardPanel, "DRAKKAR");

        // Панель с кнопками навигации
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        Design.CustomButton buttonBack = new Design.CustomButton("Назад");
        Design.CustomButton buttonNext = new Design.CustomButton("Далее");

        updateNavigationButtons(buttonBack, buttonNext, "DRAKKAR");

        buttonBack.addActionListener(getNavigationAction(buttonBack, buttonNext, false));
        buttonNext.addActionListener(getNavigationAction(buttonBack, buttonNext, true));

        bottomLeftPanel.add(buttonBack);
        bottomLeftPanel.add(buttonNext);
        bottomLeftPanel.setOpaque(false);
        Design.setFontForAllComponents(bottomLeftPanel, Color.WHITE);

        // Добавляем элементы в основную панель
        mainLeftPanel.add(cardPanel, BorderLayout.CENTER);
        mainLeftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        mainLeftPanel.setOpaque(false);

        Design.makeAllNonOpaque(mainLeftPanel);

        return mainLeftPanel;
    }

    /**
     * Возвращает слушатель события для кнопок "Назад" и "Далее".
     * Обеспечивает переход между этапами планирования.
     *
     * @param backBtn кнопка "Назад"
     * @param nextBtn кнопка "Далее"
     * @param isNext флаг направления перехода (true — к следующему этапу)
     * @return {@link ActionListener}, реагирующий на нажатие
     */
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

    /**
     * Определяет индекс текущей активной карточки.
     *
     * @return индекс активной карточки
     */
    private static int getCurrentCardIndex() {
        for (int i = 0; i < CARD_NAMES.length; i++) {
            Component component = cardPanel.getComponent(i);
            if (component.isVisible()) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Обновляет доступность кнопок "Назад" и "Далее" в зависимости от текущего этапа.
     *
     * @param backBtn кнопка "Назад"
     * @param nextBtn кнопка "Далее"
     * @param currentCard имя текущей карточки
     */
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