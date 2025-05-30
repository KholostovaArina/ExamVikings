package com.mycompany.examvikings;

import java.sql.*;
import java.util.*;

public class SQLReader {

    public static List<City> readCities() {
        List<City> cities = new ArrayList<>();
        String sql = "SELECT id, name, latitude, longitude FROM cities";

        try (Connection conn = SQLConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                double latitude = rs.getDouble("latitude");
                double longitude = rs.getDouble("longitude");
                cities.add(new City(id, name, latitude, longitude));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }
}
