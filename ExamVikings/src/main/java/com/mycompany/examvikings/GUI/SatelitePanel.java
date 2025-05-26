
package com.mycompany.examvikings.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;


public class SatelitePanel {
    public static JSplitPane create() {
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(new JLabel("Список спутников", SwingConstants.CENTER), BorderLayout.CENTER);

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        rightPanel.add(new JLabel("Выбранный спутник:"));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JLabel satelliteLabel = new JLabel("Один-1");
        satelliteLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rightPanel.add(satelliteLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        rightPanel.add(new JLabel("Характеристики:"));
        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        JPanel specsPanel = new JPanel();
        specsPanel.setLayout(new BoxLayout(specsPanel, BoxLayout.Y_AXIS));
        specsPanel.add(new JLabel("• Разрешение: 1.5 м/пиксель"));
        specsPanel.add(new JLabel("• Частота съёмки: 2 раза/день"));
        specsPanel.add(new JLabel("• Покрытие: глобальное"));
        rightPanel.add(specsPanel);
        
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.8);
        return splitPane;
    }
}
