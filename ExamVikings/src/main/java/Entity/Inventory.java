package Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, представляющий инвентарь викингов.
 * Содержит ресурсы, такие как серебро, еда, рабы и предметы добычи.
 * 
 * <p>Этот класс является синглтоном по своей природе — содержит только статические методы и поля.
 * Конструктор приватный, чтобы предотвратить создание экземпляров.</p>
 */
public class Inventory {
    
    /**
     * Количество серебра у команды викингов.
     */
    private static double silver = 1000;

    /**
     * Запасы еды (в мерах).
     */
    private static double food = 200;

    /**
     * Количество рабов, находящихся в распоряжении команды.
     */
    private static int slaves = 0;

    /**
     * Список предметов добычи, собранных во время набегов.
     */
    private static final List<Loot> products = new ArrayList<>();

    /**
     * Приватный конструктор для предотвращения создания экземпляров класса.
     */
    private Inventory() {}

    /**
     * Возвращает текущее количество серебра.
     *
     * @return количество серебра
     */
    public static double getSilver() {
        return silver;
    }

    /**
     * Устанавливает новое значение количества серебра.
     *
     * @param newSilver новое значение серебра
     */
    public static void setSilver(double newSilver) {
        silver = newSilver;
    }

    /**
     * Возвращает текущий запас еды.
     *
     * @return запас еды
     */
    public static double getFood() {
        return food;
    }

    /**
     * Устанавливает новый уровень запасов еды.
     *
     * @param newFood новое значение запаса еды
     */
    public static void setFood(double newFood) {
        food = newFood;
    }

    /**
     * Возвращает количество рабов.
     *
     * @return количество рабов
     */
    public static int getSlaves() {
        return slaves;
    }

    /**
     * Устанавливает новое количество рабов.
     *
     * @param newSlaves новое значение количества рабов
     */
    public static void setSlaves(int newSlaves) {
        slaves = newSlaves;
    }

    /**
     * Возвращает список предметов добычи.
     *
     * @return список предметов добычи
     */
    public static List<Loot> getProducts() {
        return products;
    }

    /**
     * Добавляет новый предмет добычи в инвентарь.
     *
     * @param loot предмет добычи для добавления
     */
    public static void addProduct(Loot loot) {
        products.add(loot);
    }

    /**
     * Удаляет указанный предмет добычи из инвентаря.
     *
     * @param loot предмет добычи для удаления
     */
    public static void removeProduct(Loot loot) {
        products.remove(loot);
    }
}