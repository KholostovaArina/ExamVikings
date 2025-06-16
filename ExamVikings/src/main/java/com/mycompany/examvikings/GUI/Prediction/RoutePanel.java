package com.mycompany.examvikings.GUI.Prediction;

import Entity.City;
import EntityManager.Cities;
import Design.Design;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Панель выбора маршрута для набега. Содержит карту и список городов, позволяет
 * пользователю выбрать стартовый город и маршрут.
 */
public class RoutePanel {

    private static final int PANEL_WIDTH = 1000;
    private static final int PANEL_HEIGHT = 800;
    private static final int PADDING = 50;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 30;
    private static final int RADIO_RADIUS = 11;
    private static final Color ROUTE_COLOR = new Color(112, 33, 33);

    private static final List<String> route = new ArrayList<>();
    private static final String START_CITY = "Готланд";
    private static final Map<String, JRadioButton> radioMap = new HashMap<>();

    // ---- Загрузка городов единожды ----
    private static final List<City> cities = Cities.getCities();
    private static final double[] bounds = calculateBounds(cities);
    private static JPanel infoPanel; // Снаружи всех методов

    private static RouteLineComponent routeLineComponent; // Для обновления маршрута

    /**
     * Создаёт и возвращает панель маршрута. Эта панель используется при
     * планировании похода.
     *
     * @return основная панель маршрута в виде {@link Component}
     */
    public static JPanel create() {
        JPanel mainPanel = Design.createPanelWithPhoto(Design.getMapImage());
        // Карта
        JPanel mapPanel = createMapPanel();
        // Правая панель с информацией
        infoPanel = new JPanel();

        mainPanel.add(mapPanel, BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.EAST);
        infoPanel.setOpaque(false);
        return mainPanel;
    }

    /**
     * Создаёт панель с картой и компонентом для отрисовки маршрута.
     *
     * @return настроенная {@link JPanel} с картой
     */
    private static JPanel createMapPanel() {
        JPanel mapPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        mapPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));

        if (cities.isEmpty()) {
            return mapPanel;
        }

        addCityButtons(mapPanel, cities, bounds);

        // Добавляем линию маршрута только ОДИН раз
        routeLineComponent = new RouteLineComponent();
        mapPanel.add(routeLineComponent);

        return mapPanel;
    }

    /**
     * Добавляет радио-кнопки для каждого города на панель карты. Каждый город
     * можно выбрать для маршрута.
     *
     * @param mapPanel панель, на которую будут добавлены кнопки
     */
    private static void addCityButtons(JPanel mapPanel, List<City> cities, double[] bounds) {
        ButtonGroup group = new ButtonGroup();
        radioMap.clear();

        for (City city : cities) {
            Point p = geoToPixel(city.getLatitude(), city.getLongitude(),
                    bounds[0], bounds[1], bounds[2], bounds[3]);
            final String cityName = city.getName();
            JRadioButton button = createCityButton(cityName, p);
            radioMap.put(cityName, button);

            button.addActionListener(e -> {
                City selectedCity = cities.stream()
                        .filter(c -> c.getName().equals(cityName))
                        .findFirst()
                        .orElse(null);

                // Если город уже последний в маршруте — убираем его
                if (!route.isEmpty() && route.get(route.size() - 1).equals(cityName)) {
                    route.remove(route.size() - 1);
                    button.setSelected(false);
                    SelectionPanel.updateRouteLabel(route);
                    updateRouteLine();
                    updateCityInfo(null);
                    return;
                }

                // Запрет перескакивания назад
                if (route.contains(cityName)) {
                    for (JRadioButton b : radioMap.values()) {
                        b.setSelected(route.contains(b.getText()));
                    }
                    return;
                }

                // Только стартовый город первым
                if (route.isEmpty() && !cityName.equals(START_CITY)) {
                    JOptionPane.showMessageDialog(null,
                            "Первым городом должен быть: " + START_CITY,
                            "Ошибка", JOptionPane.WARNING_MESSAGE);
                    button.setSelected(false);
                    return;
                }

                // Пропуск двойного нажатия
                if (!route.isEmpty() && route.get(route.size() - 1).equals(cityName)) {
                    button.setSelected(false);
                    return;
                }

                // Добавляем в маршрут
                route.add(cityName);
                for (JRadioButton b : radioMap.values()) {
                    b.setSelected(route.contains(b.getText())
                            && route.indexOf(b.getText()) == route.size() - 1
                            && b.getText().equals(cityName));
                }

                SelectionPanel.updateRouteLabel(route);
                updateRouteLine();

                // Обновляем информацию о городе
                updateCityInfo(selectedCity);
            });

            group.add(button);
            mapPanel.add(button);
        }
    }

    /**
     * Обновляет графическое представление маршрута — линию между городами.
     * Используется для визуализации маршрута на карте.
     */
    private static void updateRouteLine() {
        if (routeLineComponent != null) {
            routeLineComponent.repaint();
        }
    }

     /**
     * Создаёт и настраивает радио-кнопку для отображения города на карте.
     * Кнопка имеет кастомную отрисовку фона с полупрозрачным цветом,
     * соответствующим масштабу города, и поддерживает клики для выбора маршрута.
     *
     * @param name имя города, связанного с кнопкой
     * @param pos позиция на карте (координаты X и Y), где будет размещена кнопка
     * @return настроенная {@link JRadioButton}, представляющая город на карте
     */
    private static JRadioButton createCityButton(String name, Point pos) {
        City city = cities.stream()
                .filter(c -> c.getName().equals(name))
                .findFirst().orElse(null);

        int scale = city != null ? city.getScale() : 1;
        Color scaleColor = getScaleColor(scale);

        // Используем анонимный класс с custom paintComponent
        JRadioButton button = new JRadioButton(name) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                // используем полу-прозрачный вариант цвета
                Color translucent = new Color(
                        scaleColor.getRed(),
                        scaleColor.getGreen(),
                        scaleColor.getBlue(),
                        150 // альфа-канал (например, 120...180 — оптимально)
                );
                g2.setColor(translucent);
                g2.fillRect(5, 7, getWidth() - 10, getHeight() - 10);
                g2.dispose();
                super.paintComponent(g); // обязательно после заливки!
            }
        };

        int textWidth = button.getFontMetrics(button.getFont()).stringWidth(name);
        int x = pos.x - RADIO_RADIUS;
        int y = pos.y - BUTTON_HEIGHT / 2;
        int width = Math.max(BUTTON_WIDTH, RADIO_RADIUS * 2 + 8 + textWidth);
        button.setBounds(x, y, width, BUTTON_HEIGHT);

        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.setFont(Design.getBaseFont().deriveFont(10f));
        button.setForeground(Color.WHITE);

        button.setContentAreaFilled(false); // чтобы не перекрывал фон paintComponent
        button.setOpaque(false); // swing по сути не трогает фон сам
        return button;
    }

    /**
     * Вспомогательный класс для отрисовки линии маршрута между городами.
     * Реализован как внутренний статический класс панели маршрута.
     */
    static class RouteLineComponent extends JComponent {

        /**
         * Конструктор, устанавливающий размеры и прозрачность компонента.
         */
        RouteLineComponent() {
            setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            setOpaque(false);
        }

        /**
         * Переопределённый метод отрисовки, рисует маршрут — линию между всеми
         * выбранными городами.
         *
         * @param g графический контекст
         */
        @Override
        protected void paintComponent(Graphics g) {
            if (route.size() < 2) {
                return;
            }

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(ROUTE_COLOR);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            List<Point> points = new ArrayList<>();
            for (String cityName : route) {
                Optional<City> cityOpt = cities.stream()
                        .filter(c -> c.getName().equals(cityName))
                        .findFirst();

                cityOpt.ifPresent(city -> points.add(geoToPixel(
                        city.getLatitude(), city.getLongitude(),
                        bounds[0], bounds[1], bounds[2], bounds[3])));
            }

            // Рисуем маршрут по точкам
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }

            g2d.dispose();
        }
    }

    /**
     * Рассчитывает географические границы всех городов. Используется для
     * корректного масштабирования координат на карте.
     *
     * @param cities список доступных городов
     * @return массив из 4 значений: { minLat, maxLat, minLon, maxLon }
     */
    private static double[] calculateBounds(List<City> cities) {
        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;

        for (City city : cities) {
            minLat = Math.min(minLat, city.getLatitude());
            maxLat = Math.max(maxLat, city.getLatitude());
            minLon = Math.min(minLon, city.getLongitude());
            maxLon = Math.max(maxLon, city.getLongitude());
        }

        double latPadding = 0.2 * (maxLat - minLat);
        double lonPadding = 0.2 * (maxLon - minLon);

        return new double[]{
            minLat - latPadding,
            maxLat + latPadding,
            minLon - lonPadding,
            maxLon + lonPadding
        };
    }

    /**
     * Преобразует географические координаты (широта, долгота) в пиксельные
     * координаты на карте.
     *
     * @param lat широта города
     * @param lon долгота города
     * @param minLat минимальная широта среди всех городов
     * @param maxLat максимальная широта среди всех городов
     * @param minLon минимальная долгота среди всех городов
     * @param maxLon максимальная долгота среди всех городов
     * @return точка (x, y) для размещения элемента на панели карты
     */
    private static Point geoToPixel(double lat, double lon, double minLat, double maxLat, double minLon, double maxLon) {
        double latNorm = (lat - minLat) / (maxLat - minLat);
        double lonNorm = (lon - minLon) / (maxLon - minLon);

        int x = (int) (PADDING + lonNorm * (PANEL_WIDTH - 2 * PADDING));
        int y = (int) (PADDING + (1 - latNorm) * (PANEL_HEIGHT - 2 * PADDING));

        return new Point(x, y);
    }

    /**
     * Сбрасывает текущий маршрут и очищает выбор всех городов. Метод
     * вызывается, когда пользователь хочет начать выбор маршрута заново.
     */
    public static void resetRoute() {
        route.clear();
        for (JRadioButton b : radioMap.values()) {
            b.setSelected(false);
        }
        SelectionPanel.updateRouteLabel(route);
        updateRouteLine();
    }

    /**
     * Возвращает копию текущего выбранного маршрута в виде списка названий
     * городов. Используется для отображения и дальнейшей обработки в других
     * частях приложения.
     *
     * @return список строк с названиями городов в порядке их следования по
     * маршруту
     */
    public static List<String> getSelectedRoute() {
        return new ArrayList<>(route);
    }

    /**
     * Обновляет информацию о выбранном городе справа на панели. Отображает
     * детали последнего выбранного города.
     *
     * @param cityName имя выбранного города
     */
    private static void updateCityInfo(City city) {
        infoPanel.removeAll();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(18, 16, 18, 16));

        if (city == null) {
            infoPanel.repaint();
            infoPanel.revalidate();
            return;
        }

        // Цвета в зависимости от масштаба
        Color scaleColor = getScaleColor(city.getScale());

        // ---- Название города с цветным фоном или с подчеркиванием ----
        String[] words = city.getName().split("\\s+");
        StringBuilder sb = new StringBuilder("<html>");
        for (String word : words) {
            sb.append(word).append("<br>");
        }
        sb.append("</html>");

        JLabel nameLabel = new JLabel(sb.toString());
        nameLabel.setFont(Design.getBigFont().deriveFont(22f));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Для рамки-подчеркивания
        nameLabel.setBorder(BorderFactory.createMatteBorder(0, 0, 4, 0, scaleColor));
        // Или так для цветного фона:
        //nameLabel.setOpaque(true); nameLabel.setBackground(scaleColor);

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 14)));

        // ---- Остальные лейблы ----
        infoPanel.add(createInfoLabel("ID", String.valueOf(city.getId())));
        infoPanel.add(createInfoLabel("Тип", city.getCityType()));
        infoPanel.add(createInfoLabel("Широта", String.valueOf(city.getLatitude())));
        infoPanel.add(createInfoLabel("Долгота", String.valueOf(city.getLongitude())));
        // Масштаб с фоном
        JLabel scaleLabel = createInfoLabel("Масштаб", String.valueOf(city.getScale()));
        scaleLabel.setOpaque(true);
        scaleLabel.setBackground(scaleColor);
        scaleLabel.setForeground(scaleColor.getRed() + scaleColor.getGreen() > 390 ? Color.BLACK : Color.WHITE); // если цвет очень светлый, чёрный текст
        infoPanel.add(scaleLabel);

        infoPanel.revalidate();
        infoPanel.repaint();
    }

    /**
     * Создаёт информационную метку с заданным полем и значением.
     *
     * @param field название поля
     * @param value значение поля
     * @return настроенная метка
     */
    private static JLabel createInfoLabel(String field, String value) {
        JLabel label = new JLabel(field + ": " + value);
        label.setFont(Design.getBaseFont().deriveFont(16f));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Возвращает цвет, соответствующий масштабу города. Масштаб определяет
     * важность города.
     *
     * @param scale масштаб города (от 1 до 10)
     * @return цвет, связанный с масштабом
     */
    private static Color getScaleColor(int scale) {
        if (scale <= 2) return new Color(40, 200, 110);          // зелёный
        if (scale <= 4) return new Color(235, 220, 0);           // жёлтый
        if (scale <= 6) return new Color(255, 140, 0);           // оранжевый
        return new Color(230, 40, 40);                           // красный
    }
}