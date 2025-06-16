package Entity;

/**
 * Класс, представляющий викинга — участника походов и набегов.
 * Содержит информацию о личности и боевых характеристиках викинга.
 */
public final class Viking {
    
    /**
     * Уникальный идентификатор викинга.
     */
    private int id;

    /**
     * Имя викинга.
     */
    private String name;

    /**
     * Пол викинга ("Мужчина" или "Женщина").
     */
    private String gender;

    /**
     * Название клана, к которому принадлежит викинг.
     */
    private String clan;

    /**
     * Возраст викинга в годах.
     */
    private int age;

    /**
     * Коэффициент активности — показатель боевой эффективности и выносливости.
     * Допустимые значения: от 0.1 до 2.0.
     */
    private double activityCoefficient; 

    /**
     * Путь к миниатюре изображения викинга 
     */
    private String photoMiniPath;

    /**
     * Путь к основному изображению викинга 
     */
    private String photoPath;

    /**
     * Создаёт новый экземпляр викинга с заданными параметрами.
     *
     * @param id уникальный идентификатор
     * @param name имя викинга
     * @param gender пол ("Мужчина" или "Женщина")
     * @param clan название клана
     * @param age возраст викинга
     * @param activityCoefficient коэффициент активности (должен быть от 0.1 до 2.0)
     * @param photoMiniPath путь к мини-изображению
     * @param photoPath путь к полноразмерному изображению
     */
    public Viking(int id, String name, String gender, String clan, int age, double activityCoefficient, String photoMiniPath, String photoPath) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.clan = clan;
        this.age = age;
        setActivityCoefficient(activityCoefficient);
        this.photoMiniPath = photoMiniPath;
        this.photoPath = photoPath;
    }

    /**
     * Возвращает путь к мини-изображению викинга.
     *
     * @return путь к мини-изображению
     */
    public String getPhotoMiniPath() {
        return photoMiniPath;
    }

    /**
     * Устанавливает новый путь к мини-изображению викинга.
     *
     * @param name новый путь
     */
    public void setPhotoMiniPath(String name) {
        this.photoMiniPath = name;
    }

    /**
     * Возвращает путь к полноразмерному изображению викинга.
     *
     * @return путь к изображению
     */
    public String getPhotoPath() {
        return photoPath;
    }

    /**
     * Устанавливает новый путь к полноразмерному изображению викинга.
     *
     * @param name новый путь
     */
    public void setPhotoPath(String name) {
        this.photoPath = name;
    }

    /**
     * Возвращает уникальный идентификатор викинга.
     *
     * @return ID викинга
     */
    public int getId() {
        return id;
    }

    /**
     * Устанавливает новый идентификатор викинга.
     *
     * @param id новый ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Возвращает имя викинга.
     *
     * @return имя
     */
    public String getName() {
        return name;
    }

    /**
     * Устанавливает новое имя викинга.
     *
     * @param name новое имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает пол викинга.
     *
     * @return "Мужчина" или "Женщина"
     */
    public String getGender() {
        return gender;
    }

    /**
     * Устанавливает пол викинга.
     *
     * @param gender новое значение пола
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Возвращает название клана викинга.
     *
     * @return клан
     */
    public String getClan() {
        return clan;
    }

    /**
     * Устанавливает новый клан для викинга.
     *
     * @param clan новое название клана
     */
    public void setClan(String clan) {
        this.clan = clan;
    }

    /**
     * Возвращает возраст викинга.
     *
     * @return возраст в годах
     */
    public int getAge() {
        return age;
    }

    /**
     * Устанавливает возраст викинга.
     *
     * @param age новый возраст (не может быть отрицательным)
     * @throws IllegalArgumentException если возраст меньше нуля
     */
    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
        this.age = age;
    }

    /**
     * Возвращает коэффициент активности викинга.
     *
     * @return коэффициент активности
     */
    public double getActivityCoefficient() {
        return activityCoefficient;
    }

    /**
     * Устанавливает коэффициент активности викинга.
     *
     * @param activityCoefficient новое значение (должно быть между 0.1 и 2.0)
     * @throws IllegalArgumentException если значение вне допустимого диапазона
     */
    public void setActivityCoefficient(double activityCoefficient) {
        if (activityCoefficient < 0.1 || activityCoefficient > 2.0) {
            throw new IllegalArgumentException("Коэффициент активности должен быть в диапазоне от 0.1 до 2.0");
        }
        this.activityCoefficient = activityCoefficient;
    }

    /**
     * Генерирует HTML-строку с информацией о викинге.
     * Может использоваться для отображения данных в графическом интерфейсе.
     *
     * @return HTML-представление информации о викинге
     */
    public String getInfoHTML() {
        return "<div style='padding:10px; text-align:left;'>" +
               "<b>Имя:</b> " + name + "<br>" +
               "<b>Пол:</b> " + gender + "<br>" +
               "<b>Клан:</b> " + clan + "<br>" +
               "<b>Возраст:</b> " + age + " лет<br>" +
               "<b>Коэффициент активности:</b> " + String.format("%.2f", activityCoefficient) +
               "</div>";
    }
}