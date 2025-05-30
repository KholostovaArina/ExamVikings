package com.mycompany.examvikings.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;

public class DrakkarPanel {

    public static JSplitPane create() {
        // Основная левая панель с изображением и навигационными стрелками
        JPanel leftCenterPanel = new JPanel(new BorderLayout());

        // Загрузка изображений
        ImageIcon leftIcon = new ImageIcon("/право.png");
        ImageIcon rightIcon = new ImageIcon("/право.png");
        ImageIcon originalIcon = new ImageIcon("/фото.png");

        // Масштабирование изображений
        Image scaledImage = originalIcon.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        Image left_scaledImage = leftIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image right_scaledImage = rightIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        // JLabel для изображений
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImage));
        JLabel leftArrowLabel = new JLabel(new ImageIcon(left_scaledImage));
        JLabel rightArrowLabel = new JLabel(new ImageIcon(right_scaledImage));

        // Панель для размещения трёх картинок горизонтально (с центрированием)
        JPanel imagePanel = new JPanel();
        imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.X_AXIS));
        imagePanel.setAlignmentX(JPanel.CENTER_ALIGNMENT); // выравнивание по горизонтали

        // Отступы
        imagePanel.add(Box.createHorizontalGlue()); // равномерные отступы слева
        imagePanel.add(leftArrowLabel);
        imagePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        imagePanel.add(iconLabel);
        imagePanel.add(Box.createRigidArea(new Dimension(20, 0)));
        imagePanel.add(rightArrowLabel);
        imagePanel.add(Box.createHorizontalGlue()); // и справа

        // Центральная панель — картинки + кнопка
        JPanel imageWithButtonPanel = new JPanel();
        imageWithButtonPanel.setLayout(new BoxLayout(imageWithButtonPanel, BoxLayout.Y_AXIS));
        imageWithButtonPanel.setBorder(BorderFactory.createEmptyBorder(100, 10, 10, 10));

        imageWithButtonPanel.add(imagePanel);
        imageWithButtonPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JButton iconButton = new JButton("Выбрать");
        iconButton.setAlignmentX(JButton.CENTER_ALIGNMENT);
        imageWithButtonPanel.add(iconButton);

        // Добавляем всё в основную левую панель
        leftCenterPanel.add(imageWithButtonPanel, BorderLayout.NORTH); // чтобы не растягивалось вниз

        // Правая панель с информацией
        JPanel rightCenterPanel = new JPanel();
        rightCenterPanel.setLayout(new BoxLayout(rightCenterPanel, BoxLayout.Y_AXIS));
        rightCenterPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        rightCenterPanel.add(new JLabel("Имя драккара:"));
        rightCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel nameLabel = new JLabel("Драккар 'Морской волк'");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        rightCenterPanel.add(nameLabel);
        rightCenterPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        rightCenterPanel.add(new JLabel("Описание:"));
        rightCenterPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JTextPane descriptionPane = new JTextPane();
        descriptionPane.setText("Быстроходный драккар с дубовым корпусом, вместимостью 40 воинов. Оснащён квадратным парусом с красными полосами.");
        descriptionPane.setEditable(false);
        descriptionPane.setBackground(rightCenterPanel.getBackground());
        descriptionPane.setPreferredSize(new Dimension(200, 100));
        rightCenterPanel.add(new JScrollPane(descriptionPane));

        // Сборка финального JSplitPane
        JSplitPane centerSplitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftCenterPanel,
                rightCenterPanel
        );
        centerSplitPane.setResizeWeight(0.8);
        centerSplitPane.setDividerLocation(0.8);

        return centerSplitPane;
    }
}
