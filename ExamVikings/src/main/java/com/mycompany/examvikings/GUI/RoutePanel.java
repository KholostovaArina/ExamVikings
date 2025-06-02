package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class RoutePanel {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 500;
    private static final int PADDING = 50;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 30;
    private static final Color MAP_BACKGROUND = new Color(220, 240, 235);
    private static final Color ROUTE_COLOR = new Color(80, 100, 245, 180);

    // Храним выбранный маршрут здесь
    private static final List<String> route = new ArrayList<>();
    private static final String START_CITY = "Готланд"; // Убедись, что такой город есть

    public static JPanel create() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel mapPanel = createMapPanel();
        mainPanel.add(new JScrollPane(mapPanel), BorderLayout.CENTER);

        return mainPanel;
    }

    private static JPanel createMapPanel() {
        JPanel mapPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintMapBackground(g);
            }
        };

        mapPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        mapPanel.setBackground(MAP_BACKGROUND);

        List<City> cities = SQLReader.readCities();
        if (cities.isEmpty()) return mapPanel;

        double[] bounds = calculateBounds(cities);
        addCityButtons(mapPanel, cities, bounds);

        // Не добавляем маршрут изначально
        // addRoutePainter(mapPanel, cities, bounds); <-- убрано

        return mapPanel;
    }

    private static void paintMapBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(MAP_BACKGROUND);
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
        g2d.dispose();
    }

    private static void addCityButtons(JPanel mapPanel, List<City> cities, double[] bounds) {
    ButtonGroup group = new ButtonGroup();

    for (City city : cities) {
        Point p = geoToPixel(city.getLatitude(), city.getLongitude(),
                bounds[0], bounds[1], bounds[2], bounds[3]);

        JRadioButton button = createCityButton(city.getName(), p);
        button.addActionListener(e -> {
            String selectedCity = button.getText();

            if (button.isSelected()) {
                // Попытка добавить новый город
                if (!isValidSelection(selectedCity)) {
                    button.setSelected(false); // Откатываем неверный выбор
                } else {
                    route.add(selectedCity);
                    SelectionPanel.updateRouteLabel(route);
                    updateRouteLine(mapPanel, cities, bounds);
                }
            } else {
                // Попытка отменить выбор
                if (!route.isEmpty() && route.get(route.size() - 1).equals(selectedCity)) {
                    // Удаляем только если это последний город
                    route.remove(route.size() - 1);
                    SelectionPanel.updateRouteLabel(route);
                    updateRouteLine(mapPanel, cities, bounds);
                } else {
                    // Не позволяем отменить не последний город
                    button.setSelected(true); // Возвращаем выбор
                }
            }
        });

        group.add(button);
        mapPanel.add(button);
    }
}
    private static boolean isValidSelection(String selectedCity) {
        if (route.isEmpty() && !selectedCity.equals(START_CITY)) {
            JOptionPane.showMessageDialog(null,
                    "Первым городом должен быть: " + START_CITY, "Ошибка", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!route.isEmpty() && route.get(route.size() - 1).equals(selectedCity)) {
            JOptionPane.showMessageDialog(null,
                    "Нельзя выбирать один и тот же город дважды подряд.", "Ошибка", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private static JRadioButton createCityButton(String name, Point position) {
        JRadioButton button = new JRadioButton(name);
        button.setBounds(position.x - BUTTON_WIDTH / 2,
                         position.y - BUTTON_HEIGHT / 2,
                         BUTTON_WIDTH,
                         BUTTON_HEIGHT);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        button.setForeground(Color.BLACK);
        return button;
    }

    // Обновляет отрисовку маршрута
    private static void updateRouteLine(JPanel mapPanel, List<City> allCities, double[] bounds) {
        Component[] components = mapPanel.getComponents();
        for (Component c : components) {
            if (c instanceof JComponent && ((JComponent) c).getToolTipText() == "route-line") {
                mapPanel.remove(c);
            }
        }

        if (route.size() < 2) return;

        List<Point> points = new ArrayList<>();
        for (String cityName : route) {
            Optional<City> cityOpt = allCities.stream()
                    .filter(c -> c.getName().equals(cityName))
                    .findFirst();
            cityOpt.ifPresent(city -> points.add(geoToPixel(
                    city.getLatitude(), city.getLongitude(),
                    bounds[0], bounds[1], bounds[2], bounds[3]
            )));
        }

        if (points.size() < 2) return;

        mapPanel.add(new JComponent() {
            {
                setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
                setOpaque(false);
                putClientProperty("route-component", true); // Для последующего удаления
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(ROUTE_COLOR);
                g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                for (int i = 0; i < points.size() - 1; i++) {
                    Point p1 = points.get(i);
                    Point p2 = points.get(i + 1);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }

                g2d.dispose();
            }
        });

        mapPanel.revalidate();
        mapPanel.repaint();
    }

    private static double[] calculateBounds(List<City> cities) {
        double minLat = Double.MAX_VALUE;
        double maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE;
        double maxLon = -Double.MAX_VALUE;

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

    private static Point geoToPixel(double lat, double lon,
                                    double minLat, double maxLat,
                                    double minLon, double maxLon) {
        double latNorm = (lat - minLat) / (maxLat - minLat);
        double lonNorm = (lon - minLon) / (maxLon - minLon);

        int x = (int) (PADDING + lonNorm * (PANEL_WIDTH - 2 * PADDING));
        int y = (int) (PADDING + (1 - latNorm) * (PANEL_HEIGHT - 2 * PADDING));

        return new Point(x, y);
    }

    // Сброс маршрута
    public static void resetRoute() {
        route.clear();
        SelectionPanel.updateRouteLabel(route);
    }

    // Получить текущий маршрут
    public static List<String> getSelectedRoute() {
        return new ArrayList<>(route);
    }
}