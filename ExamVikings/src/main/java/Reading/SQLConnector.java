package Reading;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Класс для установки соединения с базой данных PostgreSQL.
 * Содержит параметры подключения и статический метод для получения соединения.
 */
public class SQLConnector {
    
    /**
     * URL-адрес базы данных PostgreSQL.
     * Используется для подключения к удалённому серверу Supabase.
     */
    private static final String URL = "jdbc:postgresql://aws-0-eu-north-1.pooler.supabase.com:5432/postgres";

    /**
     * Имя пользователя для аутентификации в базе данных.
     */
    private static final String USER = "postgres.maautvohzrkkfmyjpjhz";

    /**
     * Пароль для аутентификации в базе данных.
     */
    private static final String PASSWORD = "Cheburashka_777";

    /**
     * Возвращает открытое соединение с базой данных.
     *
     * @return объект {@link Connection}, представляющий соединение с БД
     * @throws SQLException если произошла ошибка при подключении к базе данных
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}