package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import EntityManager.Cities;
import Entity.City;
import Entity.Drakkar;
import Entity.Viking;
import com.mycompany.examvikings.GUI.ReportFrame;
import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * Класс, представляющий правую панель основного окна планирования набега.
 * Отображает выбранные элементы: драккар, викинги, маршрут.
 * Также содержит кнопку "Готово" для перехода к отчёту.
 */
public class SelectionPanel {
    
    /**
     * Метка для отображения текущего выбранного драккара.
     */
    private static JLabel drakkarLabel;

    /**
     * Метка для отображения текущих выбранных викингов.
     */
    private static JLabel satelliteLabel;

    /**
     * Метка для отображения текущего маршрута.
     */
    public static JLabel routeLabel;

    /**
     * Список городов в порядке их следования по маршруту.
     */
    public static List<City> orderedCities = new ArrayList<>();

    /**
     * Текущий выбранный драккар.
     */
    public static Drakkar selectedDrakkar;

    /**
     * Множество викингов, выбранных для участия в набеге.
     */
    public static Set<Viking> selectedVikings = new HashSet<>();

    /**
     * Создаёт и возвращает правую панель с информацией о выбранных элементах:
     * драккар, спутники (викинги), маршрут и кнопка "Готово".
     *
     * @return {@link JScrollPane}, содержащий настроенную правую панель
     */
    public static Component createRightPanel() {
        JScrollPane scrollRightPanel = new JScrollPane();
        scrollRightPanel.setOpaque(false);
        JPanel rightPanel = Design.createPanelWithPhoto(Design.getRightPanelImage());

        // Панель информации
        JPanel infoPanel = new JPanel();
        infoPanel.setOpaque(false);
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(70, 25, 20, 40));

        // Заголовок
        JLabel mainLabel = new JLabel("Выбранный элемент:");
        mainLabel.setFont(Design.getBaseFont().deriveFont(14f));
        infoPanel.add(mainLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Инициализация меток
        drakkarLabel = new JLabel("Драккар: не выбран");
        drakkarLabel.setFont(Design.getBaseFont().deriveFont(14f));

        satelliteLabel = new JLabel("Спутник: не выбран");
        satelliteLabel.setFont(Design.getBaseFont().deriveFont(14f));

        routeLabel = new JLabel("Маршрут: не выбран");
        routeLabel.setFont(Design.getBaseFont().deriveFont(14f));

        // Добавление меток
        infoPanel.add(drakkarLabel);
        infoPanel.add(satelliteLabel);
        infoPanel.add(routeLabel);

        // Панель с кнопкой "Готово"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setOpaque(false);

        Design.CustomButton buttonReady = new Design.CustomButton("Готово");
        buttonReady.setForeground(Color.WHITE);
        buttonReady.setFont(Design.getBaseFont().deriveFont(18f));
        buttonReady.addActionListener(e -> {
            if (selectedDrakkar == null) {
                JOptionPane.showMessageDialog(null, "⚠️ Не выбран драккар!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedVikings.isEmpty()) {
                JOptionPane.showMessageDialog(null, "⚠️ Не выбраны викинги!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (orderedCities.size() < 2) {
                JOptionPane.showMessageDialog(null, "⚠️ Не выбраны города!", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (selectedVikings.size() > selectedDrakkar.getCrewCapacity()) {
                JOptionPane.showMessageDialog(
                        null,
                        "⚠️ Невозможно вместить всех викингов в драккар\nмаксимум " + selectedDrakkar.getCrewCapacity() + " викингов",
                        "Ошибка",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            ReportFrame rf = new ReportFrame();
            rf.showReport(selectedVikings, orderedCities, selectedDrakkar);
        });

        bottomPanel.add(buttonReady);

        // Компоновка интерфейса
        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        scrollRightPanel.setViewportView(rightPanel);

        return scrollRightPanel;
    }

    /**
     * Обновляет информацию о выбранных викингах.
     *
     * @param vikings множество викингов
     */
    public static void setSelectedSatellites(Set<Viking> vikings) {
        selectedVikings = vikings;
        if (vikings == null || vikings.isEmpty()) {
            satelliteLabel.setText("<html><div style='text-align:left;'>Спутники: не выбраны</div></html>");
        } else {
            StringBuilder sb = new StringBuilder("<html><div style='text-align:left;'>Спутники:<ul style='margin:0; padding-left:20px;'>");
            for (Viking v : vikings) {
                sb.append("<li>").append(v.getName()).append("</li>");
            }
            sb.append("</ul></div></html>");
            satelliteLabel.setText(sb.toString());
        }
    }

    /**
     * Обновляет информацию о выбранном драккаре.
     *
     * @param drakkar объект {@link Drakkar}, выбранный пользователем
     */
    public static void setSelectedDrakkar(Drakkar drakkar) {
        if (drakkar != null) {
            selectedDrakkar = drakkar;
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар:<br>" + drakkar.getName() + "</div></html>");
        } else {
            selectedDrakkar = null;
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар: не выбран</div></html>");
        }
    }

    /**
     * Обновляет текстовое представление маршрута и строит список городов на основе названий.
     *
     * @param route список строк с названиями городов в порядке посещения
     */
    public static void updateRouteLabel(List<String> route) {
        if (route == null || route.isEmpty()) {
            routeLabel.setText("<html><div style='text-align:left;'>Маршрут: не выбран</div></html>");
        } else {
            StringBuilder sb = new StringBuilder("<html><div style='text-align:left;'>Маршрут:<ul style='margin:0; padding-left:20px;'>");
            for (String city : route) {
                sb.append("<li>").append(city).append("</li>");
            }
            sb.append("</ul></div></html>");
            routeLabel.setText(sb.toString());
        }

        orderedCities = buildOrderedCities(route);  // обновляем список городов
    }

    /**
     * Преобразует список имён городов в список объектов {@link City} в том же порядке.
     *
     * @param route список строк с названиями городов
     * @return список объектов {@link City}
     */
    private static List<City> buildOrderedCities(List<String> route) {
        List<City> list = new ArrayList<>();
        if (route == null) {
            return list;
        }

        List<City> allCities = Cities.getCities();
        for (String cityName : route) {
            for (City city : allCities) {
                if (city.getName().equals(cityName)) {
                    list.add(city);
                    break;
                }
            }
        }
        return list;
    }
}