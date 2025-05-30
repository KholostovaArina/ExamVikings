package com.mycompany.examvikings.GUI;

import com.mycompany.examvikings.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;

public class RoutePanel {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 500;
    private static final int PADDING = 50; // Увеличили отступы
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 30;
    private static final Color MAP_BACKGROUND = new Color(220, 240, 235);
    private static final Color ROUTE_COLOR = new Color(80, 100, 245, 180);

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
        addRoutePainter(mapPanel, cities, bounds);

        return mapPanel;
    }

    private static void paintMapBackground(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(MAP_BACKGROUND);
        g2d.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
    }

    private static void addCityButtons(JPanel mapPanel, List<City> cities, double[] bounds) {
        ButtonGroup group = new ButtonGroup();
        
        for (City city : cities) {
            Point p = geoToPixel(city.getLatitude(), city.getLongitude(), 
                               bounds[0], bounds[1], bounds[2], bounds[3]);
            
            JRadioButton button = createCityButton(city.getName(), p);
            group.add(button);
            mapPanel.add(button);
        }
    }

    private static JRadioButton createCityButton(String name, Point position) {
        JRadioButton button = new JRadioButton(name);
        button.setBounds(position.x - BUTTON_WIDTH/2, position.y - BUTTON_HEIGHT/2, 
                       BUTTON_WIDTH, BUTTON_HEIGHT);
        
        // Убираем фон у текста, оставляем только у кнопки
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLUE, 1));
        button.setForeground(Color.BLACK);
        
        return button;
    }

    private static void addRoutePainter(JPanel mapPanel, List<City> cities, double[] bounds) {
        mapPanel.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRoutes(g, cities, bounds);
            }
        });
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

        // Увеличиваем отступы для более свободного расположения
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

    private static void drawRoutes(Graphics g, List<City> cities, double[] bounds) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(ROUTE_COLOR);
        g2d.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        for (int i = 0; i < cities.size() - 1; i++) {
            Point p1 = geoToPixel(
                cities.get(i).getLatitude(), 
                cities.get(i).getLongitude(),
                bounds[0], bounds[1], bounds[2], bounds[3]
            );
            
            Point p2 = geoToPixel(
                cities.get(i+1).getLatitude(), 
                cities.get(i+1).getLongitude(),
                bounds[0], bounds[1], bounds[2], bounds[3]
            );
            
            g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }
}