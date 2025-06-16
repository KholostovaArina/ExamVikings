package EntityManager;

import Entity.City;
import Reading.SQLConnector;
import java.util.*;
import java.sql.*;

public class Cities {
    private static final List<City> cities = new ArrayList<>();

    // Загружаем города один раз при запуске программы
    public static void loadCitiesFromDB() {
        cities.clear();
        String sql = "SELECT id, name, latitude, longitude, city_type, scale FROM cities";
        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                String cityType = rs.getString("city_type");
                int scale = rs.getInt("scale");
                cities.add(new City(id, name, latitude, longitude, cityType, scale));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Получить все города
    public static List<City> getCities() {
        return new ArrayList<>(cities); // чтобы нельзя было сломать внешний список
    }

    // Быстрый поиск по имени
    public static Optional<City> findByName(String name) {
        return cities.stream()
            .filter(c -> c.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}