package Entity;

/**
 * Класс, представляющий предмет добычи, который может быть получен после набега на город.
 */
public class Loot {
    
    /**
     * Название предмета добычи.
     * Например: "Серебряный кубок", "Пшеница".
     */
    private final String name;

    /**
     * Тип предмета добычи.
     * Определяет категорию предмета, например:
     * "Драгоценности", "Продукты питания", "Кожи".
     */
    private final String type;
     
    /**
     * Создаёт новый экземпляр предмета добычи.
     *
     * @param name название предмета
     * @param type тип предмета
     */
    public Loot(String name, String type){
        this.name = name;
        this.type = type;
    }

    /**
     * Возвращает название предмета добычи.
     *
     * @return название предмета
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает тип предмета добычи.
     *
     * @return тип предмета
     */
    public String getType() {
        return type;
    }
}