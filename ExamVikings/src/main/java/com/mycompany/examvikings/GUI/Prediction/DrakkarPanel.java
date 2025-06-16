package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import Entity.Drakkar;
import Reading.DrakkarLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

public class DrakkarPanel {
    private static final int MAIN_IMAGE_SIZE = 350, ARROW_SIZE = 50;

    private static List<Drakkar> drakkars;
    private static int currentIndex = 0;
    private static JLabel mainImageLabel, nameLabel;
    private static JTextPane descriptionPane;
    
    public static JButton reloadButton;
    public static JSplitPane splitPane;

    public static JSplitPane create() {
        drakkars = DrakkarLoader.loadDrakkars();
        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, createImagePanel(), createInfoPanel()
        );
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        splitPane.setOpaque(false);
        Design.makeAllNonOpaque(splitPane);
        Design.setFontForAllComponents(splitPane, Color.WHITE);
        updateDrakkarDisplay();   
        return splitPane;
    }

    private static JPanel createImagePanel() {
        // Картинка с мягкой обводкой/фоном
        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.setOpaque(false);

        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setOpaque(false);
        mainImageLabel.setPreferredSize(new Dimension(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE));
        imageWrapper.add(mainImageLabel);

        // Используем готовые Image из Design!
        JButton prev = navBtn(Design.getLeftImage(), () -> moveDrakkar(-1));
        JButton next = navBtn(Design.getRightImage(), () -> moveDrakkar(1));

        JPanel navPanel = new JPanel();
        navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
        navPanel.setOpaque(false);
        navPanel.add(Box.createHorizontalGlue());
        navPanel.add(prev);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(imageWrapper);
        navPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPanel.add(next);
        navPanel.add(Box.createHorizontalGlue());

        // Кнопка под драккаром
        Design.CustomButton selectButton = new Design.CustomButton("Выбрать этот драккар");
        selectButton.setFont(Design.getBaseFont().deriveFont(24f));
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.addActionListener(e -> {
            if (drakkars != null && !drakkars.isEmpty()) {
                SelectionPanel.setSelectedDrakkar(drakkars.get(currentIndex));
            }
        });
        

        // обновление
        Design.CustomButton reloadButton = new Design.CustomButton("Обновить");
        reloadButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        reloadButton.addActionListener(e -> {
            drakkars = DrakkarLoader.loadDrakkars();
            if (drakkars == null || drakkars.isEmpty()) {
                currentIndex = 0;
            } else if (currentIndex >= drakkars.size()) {
                currentIndex = 0;
            }
            updateDrakkarDisplay();
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.add(Box.createVerticalGlue());
        panel.add(navPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(selectButton);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(reloadButton);
        panel.add(Box.createVerticalGlue());
        panel.setOpaque(false);
        Design.setFontForAllComponents(panel, Color.WHITE);
        return panel;
    }

    // Теперь принимаем Image (или ImageIcon, если тебе так удобнее)
    private static JButton navBtn(Image image, Runnable action) {
        ImageIcon icon = (image != null)
                ? new ImageIcon(image.getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH))
                : placeholderIcon(ARROW_SIZE, "←→");
        JButton btn = new JButton(icon);
        btn.setContentAreaFilled(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private static JPanel createInfoPanel() {
        nameLabel = new JLabel("", SwingConstants.CENTER);
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setFont(Design.getBigFont().deriveFont(20f));
                

        descriptionPane = new JTextPane();
        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        descriptionPane.setBorder(null);
        descriptionPane.setOpaque(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(32, 24, 32, 24));
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(descriptionPane);
        panel.add(Box.createVerticalGlue());
        panel.setOpaque(false);
        return panel;
    }

    private static void moveDrakkar(int delta) {
        currentIndex = (currentIndex + delta + drakkars.size()) % drakkars.size();
        updateDrakkarDisplay();
    }

   private static void updateDrakkarDisplay() {
    if (drakkars == null || drakkars.isEmpty()) {
        mainImageLabel.setIcon(null);
        nameLabel.setText("Нет драккаров");
        descriptionPane.setText("<html><div style='font-size:16pt; color:white;'>Файл drakkars.yml пуст или не найден.</div></html>");
        return;
    }
    Drakkar d = drakkars.get(currentIndex);
    Image image = d.getImage();
    mainImageLabel.setIcon(image != null
            ? new ImageIcon(image.getScaledInstance(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE, Image.SCALE_SMOOTH))
            : placeholderIcon(MAIN_IMAGE_SIZE, "Драккар " + d.getId()));
    nameLabel.setText(d.getName());
    nameLabel.setFont(Design.getBigFont().deriveFont(20f)); // Берём нужный шрифт
    nameLabel.setForeground(new Color(200, 220, 255)); // бледно-голубой
    
    descriptionPane.setText(String.format(
            "<html><div style='font-size:16pt; color:white; padding-top:10px'>" +
                    "<b>ID:</b> %s<br>" +
                    "<b>Тип:</b> %s<br>" +
                    "<b>Вместимость:</b> %d воинов<br>" +
                    "<b>Гребцы:</b> %d пар<br>" +
                    "<b>Грузоподъемность:</b> %d тонн<br>" +
                    "<b>Вес:</b> %.1f тонн" +
                    "</div></html>",
            d.getId(),
            getDrakkarType(d.getCrewCapacity()),
            d.getCrewCapacity(),
            d.getRowersPairs(),
            d.getCargoCapacity(),
            d.getWeight()
    ));
}

    private static String getDrakkarType(int capacity) {
        if (capacity >= 50) return "Боевой драккар (большой)";
        if (capacity >= 30) return "Боевой драккар";
        if (capacity >= 20) return "Торговый драккар";
        return "Разведывательный драккар";
    }

    private static ImageIcon placeholderIcon(int size, String txt) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(230, 236, 247));
        g.fillOval(0, 0, size, size); // CIRCLE!
        g.setColor(new Color(130, 130, 180));
        g.drawOval(0, 0, size - 1, size - 1);
        g.setFont(new Font("Arial", Font.PLAIN, size / 10));
        FontMetrics f = g.getFontMetrics();
        int x = (size - f.stringWidth(txt)) / 2, y = (size - f.getHeight()) / 2 + f.getAscent();
        g.setColor(new Color(60, 60, 80));
        g.drawString(txt, x, y);
        g.dispose();
        return new ImageIcon(img);
    }
}