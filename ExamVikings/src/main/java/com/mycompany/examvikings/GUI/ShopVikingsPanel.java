package com.mycompany.examvikings.GUI;

import Entity.Inventory;
import Entity.NewVikings;
import Entity.Viking;
import Design.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ShopVikingsPanel {

    private static final int MAIN_IMAGE_SIZE = 220, ARROW_SIZE = 50;
    private List<Viking> vikingChoices;
    private int currentIndex = 0;
    private JLabel silverLabel, mainImageLabel, nameLabel;
    private JTextPane infoPane;

    /**
     * Создаёт панель "Магазин викингов".
     * Лейбл серебра передаётся снаружи (чтобы быть общим для магазина).
     */
    public JPanel create(JLabel sharedSilverLabel) {
        this.silverLabel = sharedSilverLabel;

        vikingChoices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            vikingChoices.add(NewVikings.createNewViking());
        }
        currentIndex = 0;

        // == Верхний лейбл серебра (общий для магазина) ==
        silverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        silverLabel.setForeground(new Color(30, 53, 102));
        silverLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 10));
        silverLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // == ЛЕВО: фото и стрелки ==
        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainImageLabel.setBackground(new Color(235, 242, 250));
        mainImageLabel.setOpaque(true);
        mainImageLabel.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 200), 2, true));
        mainImageLabel.setPreferredSize(new Dimension(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE));

        JButton prevBtn = navBtn(Design.getLeftImage(), () -> move(-1));
        JButton nextBtn = navBtn(Design.getRightImage(), () -> move(1));
        JPanel arrowsPanel = new JPanel();
        arrowsPanel.setOpaque(false);
        arrowsPanel.setLayout(new BoxLayout(arrowsPanel, BoxLayout.X_AXIS));
        arrowsPanel.add(Box.createHorizontalGlue());
        arrowsPanel.add(prevBtn);
        arrowsPanel.add(Box.createRigidArea(new Dimension(12, 0)));
        arrowsPanel.add(nextBtn);
        arrowsPanel.add(Box.createHorizontalGlue());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(mainImageLabel);
        leftPanel.add(Box.createVerticalStrut(18));
        leftPanel.add(arrowsPanel);

        // == ПРАВО: инфа, кнопка ==
        nameLabel = new JLabel();
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(35, 50, 120));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        infoPane = new JTextPane();
        infoPane.setEditable(false);
        infoPane.setContentType("text/html");
        infoPane.setBackground(new Color(235, 242, 250));
        infoPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        infoPane.setBorder(null);
        infoPane.setFont(new Font("Arial", Font.PLAIN, 14));
        infoPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoPane.setPreferredSize(new Dimension(210, 100));

        Design.CustomButton btnBuy = new Design.CustomButton("Купить викинга");
        btnBuy.setBackground(new Color(130, 189, 120));
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFocusPainted(false);
        btnBuy.setBorder(BorderFactory.createEmptyBorder(9, 24, 9, 24));
        btnBuy.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Покупка ---
        btnBuy.addActionListener((ActionEvent e) -> {
            Viking selected = vikingChoices.get(currentIndex);
            if (Inventory.getSilver() >= 200) {
                Inventory.setSilver(Inventory.getSilver() - 200);
                refreshSilver();
                vikingChoices.remove(selected);
                if (vikingChoices.isEmpty()) {
                    for (int i = 0; i < 4; i++) {
                        vikingChoices.add(NewVikings.createNewViking());
                    }
                    currentIndex = 0;
                } else if (currentIndex >= vikingChoices.size()) {
                    currentIndex = 0;
                }
                updateDisplay();
                // !!! Optionally: ShopFrame.refreshAllSilver(); если тебе надо обновить и на другой панели
            } else {
                JOptionPane.showMessageDialog(null,
                        "Недостаточно серебра! Нужно 200.",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.add(nameLabel);
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(infoPane);
        rightPanel.add(Box.createVerticalStrut(24));
        rightPanel.add(btnBuy);
        rightPanel.add(Box.createVerticalGlue());

        // == Объединяем слева-справа ==

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(Box.createHorizontalStrut(20));
        centerPanel.add(leftPanel);
        centerPanel.add(Box.createHorizontalStrut(32));
        centerPanel.add(rightPanel);
        centerPanel.add(Box.createHorizontalGlue());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(220, 225, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 22, 16, 22));

        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(silverLabel);
        mainPanel.add(Box.createVerticalStrut(12));
        mainPanel.add(centerPanel);
        mainPanel.add(Box.createVerticalGlue());

        updateDisplay();
        return mainPanel;
    }

    private void move(int d) {
        if (vikingChoices.size() == 0) return;
        currentIndex = (currentIndex + d + vikingChoices.size()) % vikingChoices.size();
        updateDisplay();
    }

    private void updateDisplay() {
        if (vikingChoices == null || vikingChoices.isEmpty()) {
            mainImageLabel.setIcon(null);
            nameLabel.setText("Викингов больше нет!");
            infoPane.setText("");
            return;
        }
        Viking v = vikingChoices.get(currentIndex);
        // Фото
        try {
            ImageIcon icon = new ImageIcon(v.getPhotoPath());
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
                mainImageLabel.setIcon(new ImageIcon(img));
            } else {
                mainImageLabel.setIcon(null);
            }
        } catch (Exception e) {
            mainImageLabel.setIcon(null);
        }
        nameLabel.setText("<html><b>" + v.getName() + "</b></html>");
        infoPane.setText(String.format(
                "<html><div style='font-size:12pt; color:#253270; padding:7px'>"
                        + "Пол: %s<br>"
                        + "Клан: %s<br>"
                        + "Возраст: %d лет<br>"
                        + "Коэфф. активности: %.1f<br>"
                        + "ID: %d"
                        + "</div></html>",
                v.getGender(), v.getClan(), v.getAge(), v.getActivityCoefficient(), v.getId())
        );
    }

    private JButton navBtn(Image image, Runnable action) {
        JButton btn = new JButton();
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        if (image != null)
            btn.setIcon(new ImageIcon(image.getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH)));
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public void refreshSilver() {
        if (silverLabel != null) {
            silverLabel.setText("Серебро: " + Inventory.getSilver());
        }
    }
}