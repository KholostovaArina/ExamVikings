package com.mycompany.examvikings.GUI;

import Design.Design;
import Entity.Drakkar;
import Reading.DrakkarLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.imageio.ImageIO;

public class DrakkarPanel {
    private static final int MAIN_IMAGE_SIZE = 250, ARROW_SIZE = 50;

    private static List<Drakkar> drakkars;
    private static int currentIndex = 0;
    private static JLabel mainImageLabel, nameLabel;
    private static JTextPane descriptionPane;

    public static JSplitPane create() {
        drakkars = DrakkarLoader.loadDrakkars();
        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, createImagePanel(), createInfoPanel()
        );
        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        updateDrakkarDisplay();
        return splitPane;
    }

    private static JPanel createImagePanel() {
        // Картинка с мягкой обводкой/фоном
        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.setOpaque(false);

        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setOpaque(true);
        mainImageLabel.setBackground(new Color(235,242,250));
        mainImageLabel.setBorder(BorderFactory.createLineBorder(new Color(180,180,200), 2, true));
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
        JButton selectButton = new JButton("Выбрать этот драккар");
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.addActionListener(e -> {
            if (drakkars != null && !drakkars.isEmpty()) {
                SelectionPanel.setSelectedDrakkar(drakkars.get(currentIndex));
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 225, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.add(Box.createVerticalGlue());
        panel.add(navPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(selectButton);
        panel.add(Box.createVerticalGlue());
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
        nameLabel.setFont(new Font("Arial", Font.BOLD, 20));
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setForeground(new Color(30, 53, 102));

        descriptionPane = new JTextPane();
        descriptionPane.setEditable(false);
        descriptionPane.setContentType("text/html");
        descriptionPane.setBackground(new Color(235,242,250));
        descriptionPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        descriptionPane.setBorder(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(235,242,250));
        panel.setBorder(BorderFactory.createEmptyBorder(32, 24, 32, 24));
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 24)));
        panel.add(descriptionPane);
        panel.add(Box.createVerticalGlue());
        return panel;
    }

    private static void moveDrakkar(int delta) {
        currentIndex = (currentIndex + delta + drakkars.size()) % drakkars.size();
        updateDrakkarDisplay();
    }

    private static void updateDrakkarDisplay() {
        if (drakkars == null || drakkars.isEmpty()) return;
        Drakkar d = drakkars.get(currentIndex);
        Image image = d.getImage();
        mainImageLabel.setIcon(image != null
            ? new ImageIcon(image.getScaledInstance(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE, Image.SCALE_SMOOTH))
            : placeholderIcon(MAIN_IMAGE_SIZE, "Драккар " + d.getId()));
        nameLabel.setText(d.getName());
        descriptionPane.setText(String.format(
                "<html><div style='font-size:12pt; color:#2a2a2a; padding-top:10px'>" +
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

    // Грузить картинки ресурсов больше не нужно для стрелок,
    // но оставим для других случаев
    private static ImageIcon loadImageIcon(String path, int size) {
        try {
            if (path == null || path.isEmpty()) return null;
            var is = DrakkarPanel.class.getResourceAsStream(path.startsWith("/") ? path : "/" + path);
            if (is == null) return null;
            Image img = ImageIO.read(is);
            return img != null ? new ImageIcon(img.getScaledInstance(size, size, Image.SCALE_SMOOTH)) : null;
        } catch (Exception e) {
            return null;
        }
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