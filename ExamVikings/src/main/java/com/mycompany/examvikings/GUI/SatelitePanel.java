package com.mycompany.examvikings.GUI;

import Entity.Viking;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class SatelitePanel {
    private static final int THUMBNAIL_SIZE = 50;
    private static final int MAIN_PHOTO_SIZE = 250;
    private static final int COLUMNS = 5;
    private static final double SPLIT_RATIO = 0.7;
    
    private static JLabel nameLabel;
    private static JLabel infoLabel;
    private static JLabel photoLabel;
    
    private static Set<Viking> selectedVikings = new HashSet<>();
    private static JPanel thumbnailsPanel;
    private static JPanel thumbnailsWrapperPanel; // Новое поле для обёртки
    private static List<Viking> allVikings;
    private static Viking currentlyDisplayedViking;

    public static JSplitPane create(List<Viking> vikings) {
        allVikings = vikings;
        initializeComponents();
        return createSplitPane(vikings);
    }

    private static void initializeComponents() {
        nameLabel = createNameLabel();
        infoLabel = createInfoLabel();
        photoLabel = createPhotoLabel();
    }

    private static JSplitPane createSplitPane(List<Viking> vikings) {
        thumbnailsPanel = createThumbnailsPanel(vikings);
        JPanel rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(thumbnailsPanel), 
            rightPanel
        );
        
        configureSplitPane(splitPane);
        return splitPane;
    }

    private static JPanel createThumbnailsPanel(List<Viking> vikings) {
        JPanel gridPanel = new JPanel(new GridLayout(0, COLUMNS, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(new Color(245, 245, 245));

        for (Viking v : vikings) {
            gridPanel.add(createThumbnailButton(v));
        }

        thumbnailsWrapperPanel = new JPanel(new BorderLayout());
        thumbnailsWrapperPanel.setBackground(new Color(245, 245, 245));
        
        JLabel title = new JLabel("Спутники (выбрано: " + selectedVikings.size() + ")", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 30, 30));
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        thumbnailsWrapperPanel.add(title, BorderLayout.NORTH);
        thumbnailsWrapperPanel.add(gridPanel, BorderLayout.CENTER);
        
        return thumbnailsWrapperPanel;
    }

    private static JButton createThumbnailButton(Viking v) {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        button.setToolTipText(v.getName());
        
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Загрузка изображения
        ImageIcon icon = new ImageIcon(v.getPhotoMiniPath());
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            button.setIcon(resizeIcon(icon, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        } else {
            button.setIcon(new ImageIcon(createPlaceholderImage()));
        }

        updateButtonAppearance(button, v);

        button.addActionListener(e -> {
            toggleVikingSelection(v);
            currentlyDisplayedViking = v;
            updateSatelliteInfo(v);
            updateThumbnailsTitle();
        });

        // Эффект при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButtonAppearance(button, v);
            }
        });

        return button;
    }

    private static void toggleVikingSelection(Viking v) {
        if (selectedVikings.contains(v)) {
            selectedVikings.remove(v);
        } else {
            selectedVikings.add(v);
        }
        SelectionPanel.setSelectedSatellites(new HashSet<>(selectedVikings));
    }

    private static void updateButtonAppearance(JButton button, Viking v) {
        if (selectedVikings.contains(v)) {
            button.setBackground(new Color(220, 240, 255));
            button.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 255), 2));
        } else {
            button.setBackground(new Color(240, 240, 240));
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
    }

    private static void updateThumbnailsTitle() {
        JLabel title = (JLabel)((BorderLayout)thumbnailsWrapperPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        title.setText("Спутники (выбрано: " + selectedVikings.size() + ")");
    }

    private static Image createPlaceholderImage() {
        BufferedImage img = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawRect(0, 0, THUMBNAIL_SIZE-1, THUMBNAIL_SIZE-1);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.drawString("Фото", 10, THUMBNAIL_SIZE/2);
        g2d.dispose();
        return img;
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Основные компоненты
        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(photoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Панель с информацией
        JScrollPane infoScroll = new JScrollPane(infoLabel);
        infoScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        infoScroll.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, 150));
        panel.add(infoScroll);

        return panel;
    }

    private static JLabel createNameLabel() {
        JLabel label = new JLabel("Выберите спутника", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(40, 50, 50));
        return label;
    }

    private static JLabel createPhotoLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE));
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        label.setBackground(new Color(248, 248, 255));
        label.setOpaque(true);
        return label;
    }

    private static JLabel createInfoLabel() {
        JLabel label = new JLabel(getDefaultInfo(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private static void configureSplitPane(JSplitPane splitPane) {
        splitPane.setResizeWeight(SPLIT_RATIO);
        splitPane.setDividerLocation(SPLIT_RATIO);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
    }

    private static void updateSatelliteInfo(Viking viking) {
        nameLabel.setText(viking.getName());

        // Обновление фото
        ImageIcon icon = new ImageIcon(viking.getPhotoPath());
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            photoLabel.setIcon(resizeIcon(icon, MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE));
            photoLabel.setText("");
        } else {
            photoLabel.setIcon(null);
            photoLabel.setText("<html><div style='text-align:center;'>Фото<br>недоступно</div></html>");
        }

        // Обновление информации
        infoLabel.setText(String.format(
            "<html><div style='padding:10px; text-align:left;'>" +
            "<b>Имя:</b> %s<br>" +
            "<b>Пол:</b> %s<br>" +
            "<b>Род:</b> %s<br>" +
            "<b>Возраст:</b> %d лет<br>" +
            "<b>Коэффициент активности:</b> %.2f</div></html>",
            viking.getName(),
            viking.getGender(),
            viking.getClan(),
            viking.getAge(),
            viking.getActivityCoefficient()
        ));
    }

    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static String getDefaultInfo() {
        return "<html><div style='text-align:center; padding:20px;'>" +
               "Нажмите на миниатюру спутника,<br>чтобы увидеть подробную информацию</div></html>";
    }
}