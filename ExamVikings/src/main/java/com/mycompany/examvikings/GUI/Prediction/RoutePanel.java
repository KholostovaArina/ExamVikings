package com.mycompany.examvikings.GUI.Prediction;

import Entity.City;
import EntityManager.Cities;
import Design.Design;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

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

    private static void updateRouteLine() {
        if (routeLineComponent != null) {
            routeLineComponent.repaint();
        }
    }

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
                g2.fillRect(5, 7, getWidth()-10, getHeight()-10);
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

    // Для отрисовки маршрута
    static class RouteLineComponent extends JComponent {
        RouteLineComponent() {
            setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (route.size() < 2) { return; }
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(ROUTE_COLOR);
            g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            List<Point> points = new ArrayList<>();
            for (String cityName : route) {
                Optional<City> cityOpt = cities.stream().filter(c -> c.getName().equals(cityName)).findFirst();
                cityOpt.ifPresent(city -> points.add(geoToPixel(
                        city.getLatitude(), city.getLongitude(),
                        bounds[0], bounds[1], bounds[2], bounds[3])));
            }
            for (int i = 0; i < points.size() - 1; i++) {
                Point p1 = points.get(i);
                Point p2 = points.get(i + 1);
                g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
            }
            g2d.dispose();
        }
    }

    private static double[] calculateBounds(List<City> cities) {
        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE, minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;
        for (City city : cities) {
            minLat = Math.min(minLat, city.getLatitude());
            maxLat = Math.max(maxLat, city.getLatitude());
            minLon = Math.min(minLon, city.getLongitude());
            maxLon = Math.max(maxLon, city.getLongitude());
        }
        double latPadding = 0.2 * (maxLat - minLat);
        double lonPadding = 0.2 * (maxLon - minLon);
        return new double[]{ minLat - latPadding, maxLat + latPadding, minLon - lonPadding, maxLon + lonPadding };
    }

    private static Point geoToPixel(double lat, double lon, double minLat, double maxLat, double minLon, double maxLon) {
        double latNorm = (lat - minLat) / (maxLat - minLat);
        double lonNorm = (lon - minLon) / (maxLon - minLon);
        int x = (int) (PADDING + lonNorm * (PANEL_WIDTH - 2 * PADDING));
        int y = (int) (PADDING + (1 - latNorm) * (PANEL_HEIGHT - 2 * PADDING));
        return new Point(x, y);
    }

    // Сброс маршрута
    public static void resetRoute() {
        route.clear();
        for (JRadioButton b : radioMap.values()) {
            b.setSelected(false);
        }
        SelectionPanel.updateRouteLabel(route);
        updateRouteLine();
    }

    // Получить текущий маршрут
    public static List<String> getSelectedRoute() {
        return new ArrayList<>(route);
    }

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

// Вспомогательный метод для инфо-лейблов
    private static JLabel createInfoLabel(String field, String value) {
        JLabel label = new JLabel(field + ": " + value);
        label.setFont(Design.getBaseFont().deriveFont(16f));
        label.setForeground(Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    
    private static Color getScaleColor(int scale) {
        if (scale <= 2) return new Color(40, 200, 110);          // зелёный
        if (scale <= 4) return new Color(235, 220, 0);           // жёлтый
        if (scale <= 6) return new Color(255, 140, 0);           // оранжевый
        return new Color(230, 40, 40);                           // красный
    }
}