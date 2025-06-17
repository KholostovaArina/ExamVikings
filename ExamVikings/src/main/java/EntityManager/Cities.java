package EntityManager;

import Entity.City;
import Reading.SQLConnector;
import java.util.*;
import java.sql.*;

/**
 * Класс, отвечающий за хранение и загрузку списка городов из базы данных.
 * Предоставляет методы для получения полного списка городов и поиска по имени.
 */
public class Cities {
    /**
     * Конструктор по умолчанию.
     */
    public Cities() {
        // конструктор
    }
    
    /**
     * Список всех городов, загруженных из базы данных.
     */
    private static final List<City> cities = new ArrayList<>();

    /**
     * Загружает список городов из базы данных и сохраняет их во внутреннем списке.
     * Метод очищает предыдущий список перед загрузкой новых данных.
     */
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

    /**
     * Возвращает копию списка всех загруженных городов.
     *
     * @return список объектов {@link City}
     */
    public static List<City> getCities() {
        return new ArrayList<>(cities); // чтобы нельзя было сломать внешний список
    }

    /**
     * Ищет город по его названию (без учёта регистра).
     *
     * @param name название города для поиска
     * @return объект {@link Optional}, содержащий найденный город или пустой результат, если город не найден
     */
    public static Optional<City> findByName(String name) {
        return cities.stream()
            .filter(c -> c.getName().equalsIgnoreCase(name))
            .findFirst();
    }
}