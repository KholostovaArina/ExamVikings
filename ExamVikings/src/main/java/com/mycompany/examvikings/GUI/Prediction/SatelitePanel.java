package com.mycompany.examvikings.GUI.Prediction;

import Design.Design;
import Entity.Viking;
import EntityManager.Vikings;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.net.URL;
import javax.swing.*;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 * Панель выбора спутников (викингов) для набега. Содержит список миниатюр
 * викингов и панель детальной информации о выбранном викинге.
 */
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

    private static final Set<Viking> selectedVikings = new HashSet<>();
    private static JPanel thumbnailsPanel;
    private static JPanel thumbnailsWrapperPanel; // Новое поле для обёртки
    private static List<Viking> allVikings;
    private static Viking currentlyDisplayedViking;

    /**
     * Создаёт и возвращает разделённую панель (JSplitPane) для выбора викингов.
     * Левая часть содержит список миниатюр викингов, правая — детальную
     * информацию.
     *
     * @param vikings список доступных викингов
     * @return настроенная {@link JSplitPane}, содержащая обе панели
     */
    public static JSplitPane create(List<Viking> vikings) {
        allVikings = vikings;
        initializeComponents();
        return createSplitPane(vikings);
    }

    /**
     * Инициализирует основные графические компоненты интерфейса: имя викинга,
     * фото, панель информации. Вызывается при создании панели выбора спутников.
     */
    private static void initializeComponents() {
        nameLabel = createNameLabel();
        nameLabel.setFont(Design.getBigFont().deriveFont(24f));
        nameLabel.setForeground(new Color(200, 220, 255));
        photoLabel = createPhotoLabel();
        createInfoPanel(); // только конфигурируем, сам infoPanel используется дальше
    }

    /**
     * Создаёт и настраивает разделитель панелей (JSplitPane). Позволяет
     * пользователю прокручивать список викингов слева и видеть информацию
     * справа.
     *
     * @param vikings список викингов для отображения
     * @return полностью настроенная {@link JSplitPane}
     */
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

    /**
     * Создаёт панель с мини-изображениями викингов. Позволяет пользователю
     * выбрать одного из них.
     *
     * @return {@link JPanel} с миниатюрами викингов
     */
    private static JPanel createThumbnailsPanel(List<Viking> vikings) {
        JPanel gridPanel = new JPanel(new GridLayout(0, COLUMNS, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridPanel.setBackground(new Color(245, 245, 245));

        for (Viking v : vikings) {
            gridPanel.add(createThumbnailButton(v));
        }

        thumbnailsWrapperPanel = new JPanel(new BorderLayout());
        thumbnailsWrapperPanel.setBackground(new Color(245, 245, 245));

        JLabel title = new JLabel("Спутники - выбрано: " + selectedVikings.size(), SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        title.setFont(Design.getBigFont().deriveFont(24f));
        title.setForeground(new Color(200, 220, 255));

        thumbnailsWrapperPanel.add(title, BorderLayout.NORTH);
        thumbnailsWrapperPanel.add(gridPanel, BorderLayout.CENTER);

        return thumbnailsWrapperPanel;
    }

    /**
     * Возвращает кнопку-миниатюру для отображения викинга. Если изображение не
     * найдено — показывает текст "Фото не найдено".
     *
     * @param v объект викинга
     * @return настроенная {@link JButton}
     */
    private static JButton createThumbnailButton(Viking v) {
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
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBorder(BorderFactory.createLineBorder(new Color(100, 150, 255)));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                updateButtonAppearance(button, v);
            }
        });

        return button;
    }

    /**
     * Переключает состояние выбора викинга. Если викинг уже выбран — убирает
     * его из множества выбранных. Если не выбран — добавляет в множество. После
     * изменения обновляется отображение на панели выбора.
     *
     * @param v викинг, состояние которого переключается
     */
    private static void toggleVikingSelection(Viking v) {
        if (selectedVikings.contains(v)) {
            selectedVikings.remove(v);
        } else {
            selectedVikings.add(v);
        }
        SelectionPanel.setSelectedSatellites(new HashSet<>(selectedVikings));
    }

    /**
     * Обновляет внешний вид кнопки в зависимости от активности выбора.
     *
     * @param button кнопка, связанная с викингом
     * @param v текущий викинг
     */
    private static void updateButtonAppearance(JButton button, Viking v) {
        if (selectedVikings.contains(v)) {
            button.setBackground(new Color(220, 240, 255));
            button.setBorder(BorderFactory.createLineBorder(new Color(0, 100, 255), 2));
        } else {
            button.setBackground(new Color(240, 240, 240));
            button.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        }
    }

    /**
     * Обновляет заголовок с количеством выбранных викингов. Метод вызывается
     * при изменении множества selectedVikings.
     */
    private static void updateThumbnailsTitle() {
        JLabel title = (JLabel) ((BorderLayout) thumbnailsWrapperPanel.getLayout()).getLayoutComponent(BorderLayout.NORTH);
        title.setText("Спутники - выбрано: " + selectedVikings.size());
    }

    /**
     * Создаёт заглушку-изображение для отображения вместо реального фото
     * викинга. Отображает текст "Фото" и серую рамку.
     *
     * @return изображение {@link Image}, используемое как placeholder
     */
    private static Image createPlaceholderImage() {
        BufferedImage img = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(new Color(240, 240, 240));
        g2d.fillRect(0, 0, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        g2d.setColor(new Color(180, 180, 180));
        g2d.drawRect(0, 0, THUMBNAIL_SIZE - 1, THUMBNAIL_SIZE - 1);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.drawString("Фото", 10, THUMBNAIL_SIZE / 2);
        g2d.dispose();
        return img;
    }

    /**
     * Создаёт правую панель детальной информации о викинге. Содержит имя, фото
     * и прокручиваемую панель с характеристиками.
     *
     * @return настроенная {@link JPanel} с информацией о спутнике
     */
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

    /**
     * Создаёт метку для отображения имени викинга. Используется в интерфейсе
     * выбора спутников.
     *
     * @return новая {@link JLabel} с преднастроенным стилем
     */
    private static JLabel createNameLabel() {
        JLabel label = new JLabel("Выберите спутника", SwingConstants.CENTER);
        label.setForeground(Color.white);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(40, 50, 50));
        return label;
    }

    /**
     * Возвращает метку с изображением викинга. Если изображение не найдено,
     * выводится сообщение об ошибке.
     *
     * @return настроенная {@link JLabel} с фото
     */
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

    /**
     * Создаёт панель с информацией о викинге (имя, фото, данные). Используется
     * для отображения справа при клике по миниатюре.
     */
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

    /**
     * Создаёт стандартную метку для отображения характеристик викинга.
     * Например: "Пол", "Клан", "Возраст", "Активность".
     *
     * @return настроенная {@link JLabel} с предустановленным стилем
     */
    private static JLabel createDataLabel() {
        JLabel label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0)); // немного места между строками
        return label;
    }

    /**
     * Настраивает JSplitPane, устанавливая цвет фона и шрифт.
     *
     * @param splitPane разделитель панелей
     */
    private static void configureSplitPane(JSplitPane splitPane) {
        splitPane.setResizeWeight(SPLIT_RATIO);
        splitPane.setDividerLocation(SPLIT_RATIO);
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
    }

    /**
     * Обновляет информацию о выбранном викинге: имя, фото, пол, клан, возраст,
     * активность.
     *
     * @param viking викинг, информация о котором будет отображена
     */
    private static void updateSatelliteInfo(Viking viking) {
        // 1. Обновляем имя викинга
        nameLabel.setText(viking.getName());

        // 2. Обработка фотографии
        photoLabel.setIcon(null);
        photoLabel.setText(""); // Очищаем текст

        String photoPath = viking.getPhotoPath();
        System.out.println("[DEBUG] Пытаюсь загрузить фото по пути: " + photoPath);

        if (photoPath == null || photoPath.isEmpty()) {
            showErrorImage("Нет пути к фото");
            System.err.println("[ERROR] Путь к фото не указан");
            updateVikingInfo(viking);
            return;
        }

        // Нормализация пути (добавляем / если отсутствует)
        if (!photoPath.startsWith("/")) {
            photoPath = "/" + photoPath;
        }

        try {
            // Попробуем несколько способов загрузки
            URL imgUrl = SatelitePanel.class.getResource(photoPath);

            if (imgUrl == null) {
                // Попробуем через ClassLoader
                imgUrl = Thread.currentThread().getContextClassLoader().getResource(photoPath.substring(1));
            }

            if (imgUrl != null) {
                System.out.println("[DEBUG] Найдено изображение по URL: " + imgUrl);
                ImageIcon icon = new ImageIcon(imgUrl);

                // Масштабируем с сохранением пропорций
                Image scaledImage = icon.getImage().getScaledInstance(
                        MAIN_PHOTO_SIZE,
                        MAIN_PHOTO_SIZE,
                        Image.SCALE_SMOOTH
                );
                photoLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                throw new FileNotFoundException("Изображение не найдено по пути: " + photoPath);
            }
        } catch (FileNotFoundException e) {
            System.err.println("[ERROR] Ошибка загрузки изображения: " + e.getMessage());
            showErrorImage("Ошибка загрузки");
        }

        // 3. Обновляем информацию о викинге
        updateVikingInfo(viking);
    }

    /**
     * Обновляет отображаемую информацию о викинге при наведении или клике.
     * Применяется при взаимодействии с миниатюрой.
     *
     * @param v викинг, информация о котором будет отображена
     */
    private static void updateVikingInfo(Viking viking) {
        infoPanel.removeAll();

        genderInfoLabel.setText("Пол: " + viking.getGender());
        clanInfoLabel.setText("Род: " + viking.getClan());
        ageInfoLabel.setText("Возраст: " + viking.getAge() + " лет");
        activityInfoLabel.setText("Активность: " + String.format("%.2f", viking.getActivityCoefficient()));

        infoPanel.add(genderInfoLabel);
        infoPanel.add(clanInfoLabel);
        infoPanel.add(ageInfoLabel);
        infoPanel.add(activityInfoLabel);

        infoPanel.revalidate();
        infoPanel.repaint();
    }

        /**
     * Выводит сообщение об ошибке на панели с изображением викинга.
     * Используется, если изображение не может быть загружено.
     *
     * @param message сообщение об ошибке, которое будет отображено
     */
    private static void showErrorImage(String message) {
        BufferedImage img = new BufferedImage(
                MAIN_PHOTO_SIZE,
                MAIN_PHOTO_SIZE,
                BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g2d = img.createGraphics();

        // Красный фон для ошибки
        g2d.setColor(new Color(255, 200, 200));
        g2d.fillRect(0, 0, MAIN_PHOTO_SIZE, MAIN_PHOTO_SIZE);

        // Чёрная рамка
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, MAIN_PHOTO_SIZE - 1, MAIN_PHOTO_SIZE - 1);

        // Текст ошибки
        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Разбиваем текст на несколько строк, если он длинный
        String[] lines = message.split(" ");
        FontMetrics fm = g2d.getFontMetrics();
        int y = MAIN_PHOTO_SIZE / 2 - (fm.getHeight() * lines.length) / 2 + fm.getAscent();

        for (String line : lines) {
            int x = (MAIN_PHOTO_SIZE - fm.stringWidth(line)) / 2;
            g2d.drawString(line, x, y);
            y += fm.getHeight();
        }

        g2d.dispose();
        photoLabel.setIcon(new ImageIcon(img));
    }

    /**
     * Изменяет размер иконки до указанных ширины и высоты.
     *
     * @param icon исходная иконка
     * @param width новая ширина
     * @param height новая высота
     * @return изменённый объект {@link ImageIcon}
     */
    private static ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Обновляет список викингов в интерфейсе после продажи или покупки. Удаляет
     * старых викингов (возраст >= 55) и перерисовывает миниатюры.
     */
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
