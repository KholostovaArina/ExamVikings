package com.mycompany.examvikings.GUI;

import Entity.Inventory;
import Entity.NewVikings;
import Entity.Viking;
import Design.Design;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ShopVikingsPanel {

    private static final int MAIN_IMAGE_SIZE = 400, ARROW_SIZE = 70;
    private List<Viking> vikingChoices;
    private int currentIndex = 0;
    private JLabel silverLabel, mainImageLabel, nameLabel;
    private JPanel infoPanel; // Теперь будет инициализирована

    public JPanel create(JLabel sharedSilverLabel) {
        this.silverLabel = sharedSilverLabel;

        vikingChoices = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            vikingChoices.add(NewVikings.createNewViking());
        }
        currentIndex = 0;

        // == Верхний лейбл серебра ==
        silverLabel.setFont(new Font("Arial", Font.BOLD, 16));
        silverLabel.setForeground(new Color(30, 53, 102));
        silverLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 12, 10));
        silverLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // == ЛЕВО: фото и стрелки ==
        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setVerticalAlignment(SwingConstants.CENTER);
        mainImageLabel.setOpaque(true);
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
        nameLabel.setFont(Design.getBigFont());
        nameLabel.setForeground(new Color(45, 56, 61));
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Инициализация infoPanel
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setOpaque(false);

        Design.CustomButton btnBuy = new Design.CustomButton("Купить викинга");
        btnBuy.setForeground(Color.WHITE);
        btnBuy.setFont(Design.getBaseFont().deriveFont(24f));
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
            } else {
                JOptionPane.showMessageDialog(null,
                        "Недостаточно серебра! Нужно 200.",
                        "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        });

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setOpaque(false);
        rightPanel.add(Box.createVerticalStrut(50));
        rightPanel.add(nameLabel);
        rightPanel.add(Box.createVerticalStrut(50));
        rightPanel.add(infoPanel); // Используем инициализированный infoPanel
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
        if (vikingChoices.isEmpty()) {
            return;
        }
        currentIndex = (currentIndex + d + vikingChoices.size()) % vikingChoices.size();
        updateDisplay();
    }

    private void updateDisplay() {
        if (vikingChoices == null || vikingChoices.isEmpty()) {
            mainImageLabel.setIcon(null);
            nameLabel.setText("Викингов больше нет!");
            infoPanel.removeAll();
            infoPanel.revalidate();
            infoPanel.repaint();
            return;
        }

        Viking v = vikingChoices.get(currentIndex);

        // Обновляем имя
        nameLabel.setText(v.getName());

        // Обновляем информацию
        infoPanel.removeAll(); // Очищаем старые данные
        JLabel genderLabel = new JLabel("Пол: " + v.getGender());
        JLabel clanLabel = new JLabel("Клан: " + v.getClan());
        JLabel ageLabel = new JLabel("Возраст: " + v.getAge() + " лет");
        JLabel activityLabel = new JLabel(String.format("Коэфф. активности: %.1f", v.getActivityCoefficient()));
        JLabel idLabel = new JLabel("ID: " + v.getId());

        for (JLabel label : new JLabel[]{genderLabel, clanLabel, ageLabel, activityLabel, idLabel}) {
            label.setFont(Design.getBaseFont().deriveFont(22f));
            label.setForeground(new Color(45, 56, 61));
            label.setBorder(BorderFactory.createEmptyBorder(7, 7, 7, 7)); // padding
            infoPanel.add(label);
        }
        infoPanel.revalidate();
        infoPanel.repaint();

        // Загружаем фото
        mainImageLabel.setIcon(null); // Очистка предыдущего изображения
        mainImageLabel.setText(""); // Очистка текста
        String photoPath = v.getPhotoPath();

        if (photoPath == null || photoPath.isEmpty()) {
            System.err.println("[ERROR] Путь к фото пустой");
            mainImageLabel.setText("Путь к фото не указан");
            return;
        }

        // Нормализуем путь
        if (!photoPath.startsWith("/")) {
            photoPath = "/" + photoPath;
        }

        URL imgUrl = ShopVikingsPanel.class.getResource(photoPath);

        if (imgUrl == null) {
            // Пробуем через контекстный загрузчик
            imgUrl = Thread.currentThread().getContextClassLoader().getResource(photoPath.substring(1));
        }

        if (imgUrl != null) {
            try {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE, Image.SCALE_SMOOTH);
                mainImageLabel.setIcon(new ImageIcon(img));
            } catch (Exception ex) {
                System.err.println("[ERROR] Не удалось загрузить изображение: " + ex.getMessage());
                mainImageLabel.setText("Ошибка загрузки изображения");
                ex.printStackTrace();
            }
        } else {
            System.err.println("[ERROR] Фото не найдено по пути: " + photoPath);
            mainImageLabel.setText("Фото не найдено");
        }
    }

    private JButton navBtn(Image image, Runnable action) {
        JButton btn = new JButton();
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        if (image != null) {
            btn.setIcon(new ImageIcon(image.getScaledInstance(ARROW_SIZE, ARROW_SIZE, Image.SCALE_SMOOTH)));
        }
        btn.addActionListener(e -> action.run());
        return btn;
    }

    public void refreshSilver() {
        if (silverLabel != null) {
            silverLabel.setText("Серебро: " + Inventory.getSilver());
        }
    }
}