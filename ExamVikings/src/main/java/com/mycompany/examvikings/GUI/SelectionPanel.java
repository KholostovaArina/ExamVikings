package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import javax.swing.*;

public class SelectionPanel {
    private static JLabel drakkarLabel;
    private static JLabel satelliteLabel;
    public static JLabel routeLabel;
    public static List<City> orderedCities = new ArrayList<>();
    public static Drakkar selectedDrakkar;
    public static Set<Viking> selectedVikings;

    public static Component createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        infoPanel.add(new JLabel("Выбранный элемент:"));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        drakkarLabel = new JLabel("Драккар: не выбран");
        satelliteLabel = new JLabel("Спутник: не выбран");
        routeLabel = new JLabel("Маршрут: не выбран");

        infoPanel.add(drakkarLabel);
        infoPanel.add(satelliteLabel);
        infoPanel.add(routeLabel);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonReady = new JButton("Готово");
        buttonReady.addActionListener(e -> {
           ReportFrame rf =  new ReportFrame();
           rf.showReport(
                selectedVikings,
                orderedCities,
                selectedDrakkar
            );
        });
        bottomPanel.add(buttonReady);

        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

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

    public static void setSelectedDrakkar(Drakkar drakkar) {
        if (drakkar != null) {
            selectedDrakkar = drakkar;
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар:<br>" + drakkar.getName() + "</div></html>");
        } else {
            selectedDrakkar = null;
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар: не выбран</div></html>");
        }
    }

    // В этот метод передается маршрут в виде List<String> (имена городов в порядке следования)
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
        orderedCities = buildOrderedCities(route);  // теперь вернет лист
    }

    // Вспомогательный метод: превращает List<String> в LinkedHashSet<City> в том же порядке (без повторов)
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
                    break; // нашли нужный город — добавляем и выходим из внутреннего цикла
                }
            }
        }
        return list;
    }

}
