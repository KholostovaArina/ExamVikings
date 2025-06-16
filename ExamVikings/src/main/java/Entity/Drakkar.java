package Entity;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Класс, представляющий драккар — типичное судно викингов.
 * Содержит информацию о технических характеристиках и внешнем виде корабля.
 */
public class Drakkar {
    
    /**
     * Уникальный идентификатор драккара.
     */
    private int id;

    /**
     * Название драккара.
     */
    private String name;

    /**
     * Вместимость экипажа — максимальное количество викингов на борту.
     */
    private int crewCapacity;

    /**
     * Количество пар гребцов (определяет потенциальную скорость).
     */
    private int rowersPairs;

    /**
     * Грузоподъемность драккара — объем перевозимого груза.
     */
    private int cargoCapacity;

    /**
     * Вес драккара в тоннах.
     */
    private double weight;

    /**
     * Конструктор по умолчанию. 
     * Может использоваться для создания пустого объекта перед его заполнением.
     */
    public Drakkar() {
    }

    /**
     * Основной конструктор для создания драккара с заданными параметрами.
     *
     * @param id уникальный идентификатор драккара
     * @param name название драккара
     * @param crewCapacity вместимость экипажа
     * @param rowersPairs количество пар гребцов
     * @param cargoCapacity грузоподъемность
     * @param weight вес драккара
     */
    public Drakkar(int id, String name, int crewCapacity, int rowersPairs, int cargoCapacity, double weight) {
        this.id = id;
        this.name = name;
        this.crewCapacity = crewCapacity;
        this.rowersPairs = rowersPairs;
        this.cargoCapacity = cargoCapacity;
        this.weight = weight;
    }

    /**
     * Возвращает уникальный идентификатор драккара.
     *
     * @return текущий ID драккара
     */
    public int getId() { return id; }

    /**
     * Устанавливает новый идентификатор драккара.
     *
     * @param id новый ID
     */
    public void setId(int id) { this.id = id; }

    /**
     * Возвращает название драккара.
     *
     * @return название драккара
     */
    public String getName() { return name; }

    /**
     * Устанавливает новое название драккара.
     *
     * @param name новое название
     */
    public void setName(String name) { this.name = name; }

    /**
     * Возвращает вместимость экипажа.
     *
     * @return максимальное количество викингов
     */
    public int getCrewCapacity() { return crewCapacity; }

    /**
     * Устанавливает новую вместимость экипажа.
     *
     * @param crewCapacity новое значение вместимости
     */
    public void setCrewCapacity(int crewCapacity) { this.crewCapacity = crewCapacity; }

    /**
     * Возвращает количество пар гребцов.
     *
     * @return количество пар гребцов
     */
    public int getRowersPairs() { return rowersPairs; }

    /**
     * Устанавливает новое количество пар гребцов.
     *
     * @param rowersPairs новое значение
     */
    public void setRowersPairs(int rowersPairs) { this.rowersPairs = rowersPairs; }

    /**
     * Возвращает грузоподъемность драккара.
     *
     * @return объем перевозимого груза
     */
    public int getCargoCapacity() { return cargoCapacity; }

    /**
     * Устанавливает новую грузоподъемность драккара.
     *
     * @param cargoCapacity новое значение грузоподъемности
     */
    public void setCargoCapacity(int cargoCapacity) { this.cargoCapacity = cargoCapacity; }

    /**
     * Возвращает вес драккара.
     *
     * @return вес в тоннах
     */
    public double getWeight() { return weight; }

    /**
     * Устанавливает новый вес драккара.
     *
     * @param weight новый вес
     */
    public void setWeight(double weight) { this.weight = weight; }

    /**
     * Рассчитывает максимальную скорость драккара.
     * Зависит от количества гребцов и массы корабля.
     *
     * @return максимальная скорость в км/ч
     */
    public double getMaxSpeed() {
        double koef_weight = Math.pow((double) weight / 20, 0.2); // тяжелые едут тяжелее
        return rowersPairs * 2 / koef_weight; // 1 км/ч - вклад гребца 
    }

    /**
     * Загружает изображение драккара из ресурсов приложения.
     * Файл должен находиться в ресурсах проекта с именем "/драккар{id}.png".
     *
     * @return изображение драккара или null, если файл не найден
     */
    public Image getImage(){
        Image image = null;
        try {
            image = ImageIO.read(Drakkar.class.getResourceAsStream("/драккар" + id + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image; 
    }
}