package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.Satelite;
import com.mycompany.examvikings.SateliteStorage;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SatelitePanel {
    private static final int THUMBNAIL_SIZE = 50; // Квадратные миниатюры 50x50
    private static final int MAIN_PHOTO_SIZE = 250;
    private static final int COLUMNS = 5; // 5 колонок
    private static final double SPLIT_RATIO = 0.7;
    
    private static JLabel nameLabel;
    private static JLabel infoLabel;
    private static JLabel photoLabel;

    public static JSplitPane create(SateliteStorage storage) {
        initializeComponents();
        return createSplitPane(storage);
    }

    private static void initializeComponents() {
        nameLabel = createNameLabel();
        infoLabel = createInfoLabel();
        photoLabel = createPhotoLabel();
    }

    private static JSplitPane createSplitPane(SateliteStorage storage) {
        JPanel leftPanel = createThumbnailsPanel(storage);
        JPanel rightPanel = createDetailsPanel();

        JSplitPane splitPane = new JSplitPane(
            JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(leftPanel), 
            rightPanel
        );
        
        configureSplitPane(splitPane);
        return splitPane;
    }

    private static JPanel createThumbnailsPanel(SateliteStorage storage) {
        // Используем GridLayout с 5 колонками и автоматическим количеством строк
        JPanel gridPanel = new JPanel(new GridLayout(0, COLUMNS, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(new Color(245, 245, 245));

        // Добавляем миниатюры
        for (Satelite satelite : storage.getAllVikings()) {
            gridPanel.add(createThumbnailButton(satelite));
        }

        // Обертка с заголовком
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(new Color(245, 245, 245));
        
        JLabel title = new JLabel("Спутники", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(new Color(30, 30, 30));
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        
        wrapper.add(title, BorderLayout.NORTH);
        wrapper.add(gridPanel, BorderLayout.CENTER);
        
        return wrapper;
    }

    private static JButton createThumbnailButton(Satelite satelite) {
        JButton button = new JButton();
        // Фиксируем размер кнопки для квадратных миниатюр
        button.setPreferredSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        button.setMinimumSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        button.setMaximumSize(new Dimension(THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        
        button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        ImageIcon icon = new ImageIcon(satelite.getPhotoMiniPath());
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            button.setIcon(resizeIcon(icon, THUMBNAIL_SIZE, THUMBNAIL_SIZE));
        } else {
            button.setText("");
            button.setIcon(new ImageIcon(createPlaceholderImage()));
        }

        button.addActionListener(e -> updateSatelliteInfo(satelite));
        addHoverEffect(button);

        return button;
    }

    // Создаем прозрачное изображение-заглушку
    private static Image createPlaceholderImage() {
        BufferedImage img = new BufferedImage(
            THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
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

    // Остальные методы остаются без изменений
    private static JPanel createDetailsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(photoLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane infoScroll = new JScrollPane(infoLabel);
        infoScroll.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        infoScroll.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, 180));
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
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 180, 180), 2),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        label.setPreferredSize(new Dimension(MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE));
        label.setBackground(new Color(248, 253, 248));
        label.setOpaque(true);
        label.setForeground(new Color(180, 160, 160));
        label.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        label.setText("<html><div style='text-align:center;'>Фото<br>недоступно</div></html>");
        return label;
    }

    private static JLabel createInfoLabel() {
        JLabel label = new JLabel(getDefaultInfo(), SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(50, 50, 50));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private static void configureSplitPane(JSplitPane splitPane) {
        splitPane.setResizeWeight(SPLIT_RATIO);
        splitPane.setDividerLocation(SPLIT_RATIO);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setBackground(new Color(210, 210, 210));
    }

    private static void updateSatelliteInfo(Satelite satelite) {
        nameLabel.setText(satelite.getName());

        ImageIcon icon = new ImageIcon(satelite.getPhotoPath());
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            photoLabel.setIcon(resizeIcon(icon, MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE));
            photoLabel.setText("");
        } else {
            photoLabel.setIcon(null);
            photoLabel.setText("<html><div style='text-align:center;'>Фото<br>недоступно</div></html>");
        }

        infoLabel.setText(String.format(
            "<html><b>Пол:</b> %s<br><b>Род:</b> %s<br>" +
            "<b>Возраст:</b> %d лет<br><b>Коэффициент активности:</b> %.2f</html>",
            satelite.getGender(), satelite.getClan(),
            satelite.getAge(), satelite.getActivityCoefficient()
        ));
    }

    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static void addHoverEffect(JButton button) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(230, 230, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(null);
            }
        });
    }

    private static String getDefaultInfo() {
        return "<html><div style='text-align:center; padding:20px;'>" +
               "Нажмите на миниатюру,<br>чтобы увидеть информацию о спутнике</div></html>";
    }
}