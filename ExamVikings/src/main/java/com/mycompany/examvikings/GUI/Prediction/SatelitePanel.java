package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import Entity.Viking;
import EntityManager.Vikings;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

public class SatelitePanel {
    private static final int THUMBNAIL_SIZE = 100;
    private static final int MAIN_PHOTO_SIZE = 250;
    private static final int COLUMNS = 5;
    private static final double SPLIT_RATIO = 0.7;

    private static JLabel nameLabel;
    private static JPanel infoPanel;
    private static JLabel genderInfoLabel;
    private static JLabel clanInfoLabel;
    private static JLabel ageInfoLabel;
    private static JLabel activityInfoLabel;
    private static JLabel infoHintLabel; // Для стартовой подсказки
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
        nameLabel.setFont(Design.getBigFont().deriveFont(24f));
        nameLabel.setForeground(new Color(200, 220, 255));
        photoLabel = createPhotoLabel();
        createInfoPanel(); // только конфигурируем, сам infoPanel используется дальше
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
        
        JLabel title = new JLabel("Спутники - выбрано: " + selectedVikings.size() , SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        title.setFont(Design.getBigFont().deriveFont(24f));
        title.setForeground(new Color(200, 220, 255));
        
        thumbnailsWrapperPanel.add(title, BorderLayout.NORTH);
        thumbnailsWrapperPanel.add(gridPanel, BorderLayout.CENTER);
        
        return thumbnailsWrapperPanel;
    }

    private static JButton createThumbnailButton(Viking v) {
        
  String photoPath = v.getPhotoPath();
   System.out.println("photoPath = " + photoPath);
   

        
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        button.setToolTipText(v.getName());
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Загружаем иконку через ресурс
        String photoMiniPath = v.getPhotoMiniPath(); // Например, "/викинги/викинг1.png"
        URL imgUrl = SatelitePanel.class.getResource(photoMiniPath);

        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
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
        title.setText("Спутники - выбрано: " + selectedVikings.size() );
    }

    private static Image createPlaceholderImage() {
        BufferedImage img = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawRect(0, 0, THUMBNAIL_SIZE-1, THUMBNAIL_SIZE-1);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.drawString("Фото", 10, THUMBNAIL_SIZE / 2);
        g2d.dispose();
        return img;
    }

    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(photoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane infoScroll = new JScrollPane(createInfoPanel());
        infoScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        infoScroll.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, 150));
        infoScroll.getViewport().setBackground(infoPanel.getBackground());

        panel.add(infoScroll);

        return panel;
    }

    private static JLabel createNameLabel() {
        JLabel label = new JLabel("Выберите спутника", SwingConstants.CENTER);
        label.setForeground(Color.white);
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

    private static JPanel createInfoPanel() {
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(70, 80, 90));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        genderInfoLabel = createDataLabel();
        clanInfoLabel = createDataLabel();
        ageInfoLabel = createDataLabel();
        activityInfoLabel = createDataLabel();

        infoHintLabel = new JLabel("Нажмите на миниатюру спутника, чтобы увидеть подробную информацию");
        infoHintLabel.setForeground(Color.WHITE);
        infoHintLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        infoHintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // По-умолчанию — только подсказка
        infoPanel.add(infoHintLabel);
        infoPanel.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, 150));

        return infoPanel;
    }

    // Утилитарный метод для инфо-лейблов:
    private static JLabel createDataLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // немного места между строками
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

        // Загружаем фото через ресурс пакета
        String photoPath = viking.getPhotoPath(); // Например, "/викинги/викинг1.png"
        URL imgUrl = SatelitePanel.class.getResource(photoPath);

        if (imgUrl != null) {
            ImageIcon icon = new ImageIcon(imgUrl);
            photoLabel.setIcon(resizeIcon(icon, MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE));
            photoLabel.setText("");
        } else {
            photoLabel.setIcon(null);
            photoLabel.setText("Фото недоступно");
            photoLabel.setForeground(Color.white);
            photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
            photoLabel.setVerticalAlignment(SwingConstants.CENTER);
        }


        // обновляем остальные поля, как у вас
        infoPanel.removeAll();
        genderInfoLabel.setText("Пол: " + viking.getGender());
        clanInfoLabel.setText("Род: " + viking.getClan());
        ageInfoLabel.setText("Возраст: " + viking.getAge() + " лет");
        activityInfoLabel.setText("Коэффициент активности: " + String.format("%.2f", viking.getActivityCoefficient()));

        infoPanel.add(genderInfoLabel);
        infoPanel.add(clanInfoLabel);
        infoPanel.add(ageInfoLabel);
        infoPanel.add(activityInfoLabel);

        Design.setFontForAllComponents(infoPanel, Color.white);

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static void refreshVikingsList() {
        allVikings = Vikings.getAllVikings();
        // убираем старых из выбранных
        selectedVikings.removeIf(v -> v.getAge() >= 55);

        // Очищаем grid (панель миниатюр)
        JPanel gridPanel = (JPanel) thumbnailsWrapperPanel.getComponent(1); // т.к. gridPanel всегда в CENTER
        gridPanel.removeAll();

        for (Viking v : allVikings) {
            // только молодых
            if (v.getAge() < 55) {
                gridPanel.add(createThumbnailButton(v));
            }
        }
        // Обновить label над фото и сетку
        updateThumbnailsTitle();

        gridPanel.revalidate();
        gridPanel.repaint();
        thumbnailsWrapperPanel.revalidate();
        thumbnailsWrapperPanel.repaint();
    }
}