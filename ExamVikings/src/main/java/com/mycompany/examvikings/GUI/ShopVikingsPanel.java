package com.mycompany.examvikings.GUI;

import Entity.Inventory;
import Entity.NewVikings;
import Entity.Viking;
import Design.Design;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ShopVikingsPanel {

    private static final int MAIN_IMAGE_SIZE = 220, ARROW_SIZE = 50;
    private static List<Viking> vikingChoices;
    private static int currentIndex = 0;
    private static JLabel mainImageLabel, nameLabel, silverLabel;
    private static JTextPane infoPane;

    public static JPanel create() {
        vikingChoices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            vikingChoices.add(NewVikings.createNewViking());
        }
        currentIndex = 0;

        // --- TOP: Серебро пользователя ---
        silverLabel = new JLabel("Серебро: " + Inventory.getSilver(), SwingConstants.RIGHT);
        silverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        silverLabel.setForeground(new Color(30, 53, 102));
        silverLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 10));

        // --- ЦЕНТР: стрелки, фото, инфо ---
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.setOpaque(false);

        // Фотография
        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainImageLabel.setBackground(new Color(235, 242, 250));
        mainImageLabel.setOpaque(true);
        mainImageLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 2, true));
        mainImageLabel.setPreferredSize(new Dimension(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE));

        // Стрелки
        JButton prevBtn = navBtn(Design.getLeftImage(), () -> move(-1));
        JButton nextBtn = navBtn(Design.getRightImage(), () -> move(+1));

        JPanel navPhotoPanel = new JPanel();
        navPhotoPanel.setLayout(new BoxLayout(navPhotoPanel, BoxLayout.X_AXIS));
        navPhotoPanel.setOpaque(false);
        navPhotoPanel.add(Box.createHorizontalGlue());
        navPhotoPanel.add(prevBtn);
        navPhotoPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPhotoPanel.add(mainImageLabel);
        navPhotoPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        navPhotoPanel.add(nextBtn);
        navPhotoPanel.add(Box.createHorizontalGlue());

        // Инфо о викинге (справа)
        nameLabel = new JLabel("", SwingConstants.LEFT);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(35, 50, 120));

        infoPane = new JTextPane();
        infoPane.setEditable(false);
        infoPane.setContentType("text/html");
        infoPane.setBackground(new Color(235, 242, 250));
        infoPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        infoPane.setBorder(null);

        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(235, 242, 250));
        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(14));
        infoPanel.add(infoPane);
        infoPanel.add(Box.createVerticalGlue());
        infoPanel.setPreferredSize(new Dimension(210, MAIN_IMAGE_SIZE + 40));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(navPhotoPanel);
        centerPanel.add(Box.createHorizontalStrut(16));
        centerPanel.add(infoPanel);

        mainContent.add(centerPanel, BorderLayout.CENTER);

        // --- Кнопка КУПИТЬ ---
        JButton btnBuy = new JButton("Купить викинга");
        btnBuy.setFont(new Font("Arial", Font.BOLD, 15));
        btnBuy.setBackground(new Color(130, 189, 120));
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFocusPainted(false);
        btnBuy.setBorder(BorderFactory.createEmptyBorder(9, 24, 9, 24));
        btnBuy.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Реализация покупки
        btnBuy.addActionListener((ActionEvent e) -> {
            Viking selected = vikingChoices.get(currentIndex);
            if (Inventory.getSilver() >= 200) {
                Inventory.setSilver(Inventory.getSilver() - 200);
                JOptionPane.showMessageDialog(null,
                        String.format("Вы купили викинга %s за 200 серебра!", selected.getName()));
//                NewVikings.addBoughtViking(selected); // Предполагается статический метод
                vikingChoices.remove(selected);
                if (vikingChoices.isEmpty()) {
                    for (int i = 0; i < 4; i++) {
                        vikingChoices.add(NewVikings.createNewViking());
                    }
                }
                updateDisplay();
                updateSilverLabel();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Недостаточно серебра! Нужно 200.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- Общий контейнер ---
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(220, 225, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 22, 16, 22));
        panel.add(silverLabel);
        panel.add(mainContent);
        panel.add(Box.createVerticalStrut(24));
        panel.add(btnBuy);

        // Первый показ
        updateDisplay();

        return panel;
    }

    private static void move(int d) {
        currentIndex = (currentIndex + d + vikingChoices.size()) % vikingChoices.size();
        updateDisplay();
    }

    private static JButton navBtn(Image image, Runnable action) {
        ImageIcon icon = (image != null)
                ? new ImageIcon(image.getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH))
                : placeholderIcon(ARROW_SIZE, "←→");
        JButton btn = new JButton(icon);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private static void updateDisplay() {
        if (vikingChoices == null || vikingChoices.isEmpty()) return;
        Viking v = vikingChoices.get(currentIndex);

        // Фото
        ImageIcon icon;
        try {
            icon = new ImageIcon(v.getPhotoPath());
            Image img = icon.getImage().getScaledInstance(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
            icon = new ImageIcon(img);
        } catch (Exception ex) {
            icon = placeholderIcon(MAIN_IMAGE_SIZE, "V");
        }
        mainImageLabel.setIcon(icon);

        // Имя и инфо
        nameLabel.setText("<html><b>" + v.getName() + "</b></html>");
        infoPane.setText(
                String.format(
                        "<html><div style='font-size:12pt; color:#253270; padding:7px'>" +
                                "Пол: %s<br>" +
                                "Клан: %s<br>" +
                                "Возраст: %d лет<br>" +
                                "Коэфф. активности: %.1f<br>" +
                                "ID: %d" +
                                "</div></html>",
                        v.getGender(), v.getClan(), v.getAge(), v.getActivityCoefficient(), v.getId()
                )
        );
    }

    private static void updateSilverLabel() {
        silverLabel.setText("Серебро: " + Inventory.getSilver());
    }

    private static ImageIcon placeholderIcon(int size, String txt) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(new Color(230, 236, 247));
        g.fillOval(0, 0, size, size);
        g.setColor(new Color(130, 130, 180));
        g.drawOval(0, 0, size - 1, size - 1);
        g.setFont(new Font("Arial", Font.BOLD, size / 2));
        FontMetrics f = g.getFontMetrics();
        int x = (size - f.stringWidth(txt)) / 2, y = (size - f.getHeight()) / 2 + f.getAscent();
        g.setColor(new Color(60, 60, 80));
        g.drawString(txt, x, y);
        g.dispose();
        return new ImageIcon(img);
    }
}