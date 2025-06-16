package Reading;

import Entity.Drakkar;
import EntityManager.DrakkarConfig;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import java.io.*;

public class DrakkarLoader {

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
            org.yaml.snakeyaml.Yaml yaml = new org.yaml.snakeyaml.Yaml(new org.yaml.snakeyaml.constructor.Constructor(DrakkarConfig.class));
            DrakkarConfig config = yaml.load(inputStream);
            return config.getDrakkars();
        } catch (IOException e) {
            throw new RuntimeException("Невозможно найти drakkars.yml: " + e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
        }
    }
}