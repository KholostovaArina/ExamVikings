package Reading;

import Entity.City;
import Entity.Viking;
import java.sql.*;
import java.util.*;

/**
 * Класс для чтения данных из базы данных.
 * Содержит методы для загрузки списков городов и викингов из SQL-таблиц.
 */
public class SQLReader {
    
    /**
     * Конструктор по умолчанию.
     */
    public SQLReader() {
        // конструктор
    }

    /**
     * Загружает список городов из таблицы `cities`.
     *
     * @return список объектов {@link City}, загруженных из базы данных
     */
    public static List<City> readCities() {
        List<City> cities = new ArrayList<>();
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
        return cities;
    }

    /**
     * Загружает список викингов из таблицы `vicings`.
     *
     * @return список объектов {@link Viking}, загруженных из базы данных
     */
    public static List<Viking> readVikings() {
        List<Viking> vikings = new ArrayList<>();
        String sql = "SELECT id, name, gender, clan, age, activity_coefficient, photo_mini_path, photo_path FROM vicings";

        try (Connection conn = SQLConnector.getConnection(); 
             Statement stmt = conn.createStatement(); 
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String clan = rs.getString("clan");
                int age = rs.getInt("age");
                double activityCoefficient = rs.getDouble("activity_coefficient");
                String photoMiniPath = rs.getString("photo_mini_path");
                String photoPath = rs.getString("photo_path");
                
                vikings.add(new Viking(id, name, gender, clan, age, activityCoefficient, photoMiniPath, photoPath));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vikings;
    }
}