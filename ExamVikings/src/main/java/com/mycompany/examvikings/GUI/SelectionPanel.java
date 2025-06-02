package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.*;
import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class SelectionPanel {
    private static JLabel drakkarLabel;
    private static JLabel satelliteLabel;
    private static JLabel routeLabel;
    
    public static Component createRightPanel() {
        JPanel rightPanel = new JPanel(new BorderLayout());

        // Верхняя часть — отображение информации
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

        // Нижняя часть — кнопка "Готово"
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton buttonReady = new JButton("Готово");
        bottomPanel.add(buttonReady);

        rightPanel.add(infoPanel, BorderLayout.CENTER);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        return rightPanel;
    }
    
    public static void setSelectedSatellites(Set<Viking> vikings) {
        if (vikings.isEmpty()) {
            satelliteLabel.setText("Спутники:не выбраны");
        } else {
            StringBuilder sb = new StringBuilder("Спутники: ");
            for (Viking v : vikings) {
                if (sb.length() > 10) sb.append(", ");
                sb.append(v.getName());
            }
            satelliteLabel.setText(sb.toString());
        }
    }
    
    public static void setSelectedDrakkar(Drakkar drakkar) {
        if (drakkar != null) {
            drakkarLabel.setText("Драккар:" + drakkar.getName());
        } else {
            drakkarLabel.setText("Драккар: не выбран");
        }
    }
}
