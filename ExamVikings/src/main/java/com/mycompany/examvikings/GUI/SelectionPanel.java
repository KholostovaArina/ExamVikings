package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.*;
import java.awt.*;
import java.util.List;
import java.util.Set;
import javax.swing.*;

public class SelectionPanel {
    private static JLabel drakkarLabel;
    private static JLabel satelliteLabel;
    public static JLabel routeLabel;

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
        bottomPanel.add(buttonReady);

        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        return rightPanel;
    }

    public static void setSelectedSatellites(Set<Viking> vikings) {
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
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар:<br>" + drakkar.getName() + "</div></html>");
        } else {
            drakkarLabel.setText("<html><div style='text-align:left;'>Драккар: не выбран</div></html>");
        }
    }

    public static void updateRouteLabel(List<String> route) {
        if (route.isEmpty()) {
            routeLabel.setText("<html><div style='text-align:left;'>Маршрут: не выбран</div></html>");
        } else {
            StringBuilder sb = new StringBuilder("<html><div style='text-align:left;'>Маршрут:<ul style='margin:0; padding-left:20px;'>");
            for (String city : route) {
                sb.append("<li>").append(city).append("</li>");
            }
            sb.append("</ul></div></html>");
            routeLabel.setText(sb.toString());
        }
    }
}