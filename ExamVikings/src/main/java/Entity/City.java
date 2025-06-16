package Entity;

/**
 * Класс, представляющий город с его географическими и характеристиками.
 */
public class City {

    /**
     * Уникальный идентификатор города
     */
    private final int id;

    /**
     * Название города
     */
    private final String name;

    /**
     * Географическая широта (в градусах)
     */
    private final double latitude;

    /**
     * Географическая долгота (в градусах)
     */
    private final double longitude;   
   
    /**
     * Тип города
     */
    private final String cityType;

    /**
     * Масштаб города (уровень от 1 до 10), 
     * определяющий важность/размер города/количество добычи в нём
     */
    private final int scale;

    /**
     * Создаёт новый экземпляр города
     * 
     * @param id уникальный идентификатор города
     * @param name название города
     * @param latitude географическая широта (в градусах)
     * @param longitude географическая долгота (в градусах)
     * @param cityType тип города
     * @param scale масштаб города (уровень от 1 до 10)
     */
    public City(int id, String name, double latitude, double longitude, String cityType, int scale) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityType = cityType;
        this.scale = scale;
    }

    /**
     * Возвращает уникальный идентификатор города
     * 
     * @return id города
     */
    public int getId() { return id; }

    /**
     * Возвращает название города
     * 
     * @return название города
     */
    public String getName() { return name; }

    /**
     * Возвращает географическую широту
     * 
     * @return широта в градусах
     */
    public double getLatitude() { return latitude; }

    /**
     * Возвращает географическую долготу
     * 
     * @return долгота в градусах
     */
    public double getLongitude() { return longitude; }

    /**
     * Возвращает тип города
     * 
     * @return тип города (например, "торговый пункт", "монастырь")
     */
    public String getCityType() { return cityType; }

    /**
     * Возвращает масштаб города
     * 
     * @return масштаб (уровень от 1 до 10)
     */
    public int getScale() { return scale; }
}