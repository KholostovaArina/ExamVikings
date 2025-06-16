package Reading;

import Entity.Drakkar;
import EntityManager.DrakkarConfig;
import java.io.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Класс, отвечающий за загрузку конфигурации драккаров из YAML-файла.
 * Поддерживает загрузку как с локального диска, так и из ресурсов JAR-файла.
 */
public class DrakkarLoader {

    /**
     * Загружает список драккаров из файла "drakkars.yml".
     * Сначала пытается найти файл на диске, если не найден — ищет в ресурсах приложения (JAR).
     *
     * @return список объектов {@link Drakkar}, загруженных из YAML-файла
     * @throws RuntimeException если файл не найден или произошла ошибка чтения
     */
    public static List<Drakkar> loadDrakkars() {
        InputStream inputStream = null;
        try {
            File f = new File("drakkars.yml");
            if (f.exists()) {
                inputStream = new FileInputStream(f);
            } else {
                System.out.println("Файл drakkars.yml не найден на диске! Читаю ресурс JAR");
                inputStream = DrakkarLoader.class.getResourceAsStream("/drakkars.yml");
                if (inputStream == null) {
                    throw new FileNotFoundException("drakkars.yml не найден ни на диске, ни в jar");
                }
            }

            // Используем Yaml для парсинга конфигурации
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml(
                    new org.yaml.snakeyaml.constructor.Constructor(DrakkarConfig.class)
            );
            DrakkarConfig config = yaml.load(inputStream);

            return config.getDrakkars();
        } catch (IOException e) {
            throw new RuntimeException("Невозможно найти drakkars.yml: " + e);
        } finally {
            // Закрываем поток ввода в блоке finally, чтобы гарантировать его закрытие
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                // Игнорируем ошибки при закрытии
            }
        }
    }
}