package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.Satelite;
import com.mycompany.examvikings.SateliteStorage;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class SatelitePanel {

    private static final JLabel nameLabel = new JLabel("Имя", SwingConstants.CENTER);
    private static final JLabel infoLabel = new JLabel();
    private static final JLabel photoLabel = new JLabel();

    public static JSplitPane create(SateliteStorage storage) {
        // Левая панель с мини-фотками
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 10));
        leftPanel.setBackground(new Color(245, 245, 245));

        JLabel listLabel = new JLabel("Спутники", SwingConstants.CENTER);
        listLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        listLabel.setForeground(new Color(30, 30, 30));
        leftPanel.add(listLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel miniPhotosPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        miniPhotosPanel.setBackground(new Color(245, 245, 245));

        for (Satelite satelite : storage.getAllVikings()) {
            addMiniPhoto(miniPhotosPanel, satelite);
        }

        JScrollPane scrollPane = new JScrollPane(miniPhotosPanel);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        leftPanel.add(scrollPane);

        // Правая панель — детали
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        rightPanel.setBackground(Color.WHITE);

        // Имя
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setAlignmentX(SwingConstants.CENTER);
        nameLabel.setForeground(new Color(40, 40, 40));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        rightPanel.add(nameLabel);

        // Фото
        photoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        photoLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        photoLabel.setPreferredSize(new Dimension(250, 250));
        photoLabel.setMaximumSize(new Dimension(250, 250));
        photoLabel.setMinimumSize(new Dimension(250, 250));
        photoLabel.setBackground(new Color(248, 248, 248));
        photoLabel.setOpaque(true);
        photoLabel.setForeground(new Color(160, 160, 160));
        photoLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        photoLabel.setText("<html><div style='text-align:center;'>Фото<br>недоступно</div></html>");
        rightPanel.add(photoLabel);
        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Информация
        infoLabel.setText(getDefaultInfo());
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        infoLabel.setForeground(new Color(50, 50, 50));
        infoLabel.setAlignmentX(SwingConstants.LEFT);

        JScrollPane infoScrollPane = new JScrollPane(infoLabel);
        infoScrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        infoScrollPane.setPreferredSize(new Dimension(250, 180));
        infoScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        rightPanel.add(infoScrollPane);

        // Сборка
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightPanel);
        splitPane.setResizeWeight(0.85); // 85% для левой панели
        splitPane.setBorder(null);
        splitPane.setDividerSize(8);
        splitPane.setDividerLocation(0.7);

        // Стилизация разделителя
        // Установка цвета разделителя JSplitPane
        UIDefaults dividerDefaults = new UIDefaults();
        dividerDefaults.put("SplitPaneDivider.border", BorderFactory.createLineBorder(new Color(210, 210, 210)));
        dividerDefaults.put("SplitPaneDivider.draggingColor", new Color(190, 190, 190));
        dividerDefaults.put("SplitPaneDivider.background", new Color(210, 210, 210));

// Применяем стили только к текущему сплитеру
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public void installUI(JComponent c) {
                super.installUI(c);
                getDivider().setBorder(dividerDefaults.getBorder("SplitPaneDivider.border"));
                getDivider().setBackground(dividerDefaults.getColor("SplitPaneDivider.background"));
            }
        });

        return splitPane;
    }

    private static void addMiniPhoto(JPanel panel, Satelite satelite) {
        JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        wrapper.setPreferredSize(new Dimension(150, 150));
        wrapper.setMaximumSize(new Dimension(150, 150));
        wrapper.setMinimumSize(new Dimension(150, 150));
        wrapper.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        wrapper.setBackground(Color.WHITE);
        wrapper.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton button = new JButton();
        ImageIcon icon = new ImageIcon(satelite.getPhotoMiniPath());

        button.setPreferredSize(new Dimension(130, 130));
        button.setMaximumSize(new Dimension(130, 130));
        button.setMinimumSize(new Dimension(130, 130));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setRolloverEnabled(true);

        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledImage = icon.getImage().getScaledInstance(130, 130, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
        } else {
            button.setText("Фото");
            button.setFont(new Font("Segoe UI", Font.BOLD, 16));
            button.setForeground(new Color(150, 150, 150));
        }

        button.addActionListener(e -> updateSelectedSatellite(satelite));

        // Эффекты при наведении
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                wrapper.setBackground(new Color(245, 245, 245));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                wrapper.setBackground(Color.WHITE);
            }
        });

        wrapper.add(button);
        panel.add(wrapper);
    }

    private static void updateSelectedSatellite(Satelite satelite) {
        nameLabel.setText(satelite.getName());

        // Обновляем большое фото
        ImageIcon icon = new ImageIcon(satelite.getPhotoPath());
        if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaledImage = icon.getImage().getScaledInstance(
                photoLabel.getWidth(), photoLabel.getHeight(),
                Image.SCALE_SMOOTH
            );
            photoLabel.setIcon(new ImageIcon(scaledImage));
            photoLabel.setText("");
        } else {
            photoLabel.setIcon(null);
            photoLabel.setText("<html><div style='text-align:center;'>Фото<br>недоступно</div></html>");
        }

        // Обновляем информацию
        infoLabel.setText("""
            <html>
            <b>Пол:</b> %s<br>
            <b>Род:</b> %s<br>
            <b>Возраст:</b> %d лет<br>
            <b>Коэффициент активности:</b> %.2f
            </html>""".formatted(
            satelite.getGender(),
            satelite.getClan(),
            satelite.getAge(),
            satelite.getActivityCoefficient()
        ));
    }

    private static String getDefaultInfo() {
        return """
            <html>
            <div style='text-align:center; padding:20px;'>
            Нажмите на мини-фотку,<br>
            чтобы увидеть информацию о спутнике
            </div>
            </html>""";
    }
}