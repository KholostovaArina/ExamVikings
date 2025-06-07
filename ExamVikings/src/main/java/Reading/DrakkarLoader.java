package Reading;

import Entity.Drakkar;
import EntityManager.DrakkarConfig;
import java.io.IOException;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import java.io.InputStream;
import java.util.List;

public class DrakkarLoader {

    public static List<Drakkar> loadDrakkars() {
        InputStream inputStream = DrakkarLoader.class
                .getClassLoader()
                .getResourceAsStream("drakkars.yml");

        if (inputStream == null) {
            throw new RuntimeException("YAML file not found in resources");
        }

        try {
            Yaml yaml = new Yaml(new Constructor(DrakkarConfig.class));
            DrakkarConfig config = yaml.load(inputStream);
            return config.getDrakkars();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
