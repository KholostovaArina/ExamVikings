package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import Entity.Drakkar;
import Reading.DrakkarLoader;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Класс, представляющий панель выбора драккара для набега.
 * Позволяет просматривать список доступных драккаров, навигировать по ним,
 * а также выбирать и обновлять данные о текущем драккаре.
 */
public class DrakkarPanel {
    
    /**
     * Размер основного изображения драккара.
     */
    private static final int MAIN_IMAGE_SIZE = 350;

    /**
     * Размер стрелок навигации.
     */
    private static final int ARROW_SIZE = 50;

    /**
     * Список всех доступных драккаров.
     */
    private static List<Drakkar> drakkars;

    /**
     * Индекс текущего выбранного драккара.
     */
    private static int currentIndex = 0;

    /**
     * Метка для отображения изображения драккара.
     */
    private static JLabel mainImageLabel;

    /**
     * Метка для отображения имени драккара.
     */
    private static JLabel nameLabel;

    /**
     * Поле для отображения информации о драккаре.
     */
    private static JTextPane descriptionPane;

    /**
     * Кнопка "Обновить" для перезагрузки списка драккаров.
     */
    public static JButton reloadButton;

    /**
     * Основная панель с разделением на части (image + info).
     */
    public static JSplitPane splitPane;

    /**
     * Создаёт и возвращает панель с интерфейсом выбора драккара.
     *
     * @return настроенная {@link JSplitPane} с элементами управления
     */
    public static JSplitPane create() {
        drakkars = DrakkarLoader.loadDrakkars();

        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                createImagePanel(),
                createInfoPanel()
        );

        splitPane.setResizeWeight(0.7);
        splitPane.setDividerLocation(0.7);
        splitPane.setOpaque(false);

        Design.makeAllNonOpaque(splitPane);
        Design.setFontForAllComponents(splitPane, Color.WHITE);

        updateDrakkarDisplay();
        return splitPane;
    }

    /**
     * Создаёт левую панель с изображением и навигацией между драккарами.
     *
     * @return настроенная {@link JPanel}
     */
    private static JPanel createImagePanel() {
        // Обёртка для центрирования изображения
        JPanel imageWrapper = new JPanel(new GridBagLayout());
        imageWrapper.setOpaque(false);

        mainImageLabel = new JLabel();
        mainImageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainImageLabel.setOpaque(false);
        mainImageLabel.setPreferredSize(new Dimension(MAIN_IMAGE_SIZE, MAIN_IMAGE_SIZE));
        imageWrapper.add(mainImageLabel);

        // Кнопки навигации
        JButton prev = navBtn(Design.getLeftImage(), () -> moveDrakkar(-1));
        JButton next = navBtn(Design.getRightImage(), () -> moveDrakkar(1));

        // Панель с кнопками и изображением
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

        // Кнопка выбора драккара
        Design.CustomButton selectButton = new Design.CustomButton("Выбрать этот драккар");
        selectButton.setFont(Design.getBaseFont().deriveFont(24f));
        selectButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        selectButton.addActionListener(e -> {
            if (drakkars != null && !drakkars.isEmpty()) {
                SelectionPanel.setSelectedDrakkar(drakkars.get(currentIndex));
            }
        });

        // Кнопка обновления
        reloadButton = new Design.CustomButton("Обновить");
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

        // Основная панель
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

    /**
     * Создаёт кнопку навигации со стрелкой.
     *
     * @param image изображение для кнопки
     * @param action действие при нажатии
     * @return настроенная кнопка
     */
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

    /**
     * Создаёт правую панель с информацией о текущем драккаре.
     *
     * @return настроенная {@link JPanel} с данными драккара
     */
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

    /**
     * Обновляет отображаемые данные о драккаре при переходе к следующему/предыдущему.
     */
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
        nameLabel.setFont(Design.getBigFont().deriveFont(20f));
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

    /**
     * Возвращает тип драккара на основе его вместимости.
     *
     * @param capacity вместимость экипажа
     * @return строковое представление типа драккара
     */
    private static String getDrakkarType(int capacity) {
        if (capacity >= 50) return "Боевой драккар (большой)";
        if (capacity >= 30) return "Боевой драккар";
        if (capacity >= 20) return "Торговый драккар";
        return "Разведывательный драккар";
    }

    /**
     * Создаёт заглушку-изображение, если реальное изображение недоступно.
     *
     * @param size размер изображения
     * @param txt текст для отображения внутри круга
     * @return объект {@link ImageIcon} с простым стилизованным изображением
     */
    private static ImageIcon placeholderIcon(int size, String txt) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();

        g.setColor(new Color(230, 236, 247)); // светло-серый фон
        g.fillOval(0, 0, size, size); // рисуем круг

        g.setColor(new Color(130, 130, 180)); // цвет контура
        g.drawOval(0, 0, size - 1, size - 1);

        g.setFont(new Font("Arial", Font.PLAIN, size / 10));
        FontMetrics f = g.getFontMetrics();
        int x = (size - f.stringWidth(txt)) / 2;
        int y = (size - f.getHeight()) / 2 + f.getAscent();

        g.setColor(new Color(60, 60, 80)); // цвет текста
        g.drawString(txt, x, y);

        g.dispose();
        return new ImageIcon(img);
    }

    /**
     * Обновляет индекс драккара и отображает новый объект.
     *
     * @param delta шаг изменения индекса (-1 или +1)
     */
    private static void moveDrakkar(int delta) {
        currentIndex = (currentIndex + delta + drakkars.size()) % drakkars.size();
        updateDrakkarDisplay();
    }
}