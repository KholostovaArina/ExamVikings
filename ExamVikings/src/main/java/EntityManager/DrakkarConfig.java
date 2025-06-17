package EntityManager;

import Entity.Drakkar;
import java.util.List;

/**
 * Класс, представляющий конфигурацию драккаров.
 * Используется для хранения и передачи списка драккаров, загруженных из внешнего источника (в данном случае, YAML-файла).
 */
public class DrakkarConfig {
    
    /**
     * Конструктор по умолчанию.
     */
    public DrakkarConfig() {
        // конструктор
    }
    
    /**
     * Список драккаров, загруженных из конфигурационного файла.
     */
    private List<Drakkar> drakkars;

    /**
     * Возвращает список драккаров.
     *
     * @return список объектов {@link Drakkar}
     */
    public List<Drakkar> getDrakkars() {
        return drakkars;
    }

    /**
     * Устанавливает новый список драккаров.
     *
     * @param drakkars новый список драккаров
     */
    public void setDrakkars(List<Drakkar> drakkars) {
        this.drakkars = drakkars;
    }  
}