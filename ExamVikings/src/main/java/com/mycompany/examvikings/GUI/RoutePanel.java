package com.mycompany.examvikings.GUI;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class RoutePanel {

    public static JPanel create() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Карта маршрута", SwingConstants.CENTER), BorderLayout.CENTER);
        return leftPanel;
    }
}
