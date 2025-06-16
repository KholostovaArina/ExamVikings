package Entity;

public final class Viking {
    private int id;
    private String name;
    private String gender;
    private String clan;          
    private int age;
    private double activityCoefficient; // от 0.1 до 2.0
    private String photoMiniPath;
    private String photoPath;    

    // Конструктор
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

    // Геттеры и сеттеры
    public String getPhotoMiniPath() {
        return photoMiniPath;
    }

    public void setPhotoMiniPath(String name) {
        this.photoMiniPath = name;
    }
    
    // Геттеры и сеттеры
    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String name) {
        this.photoPath = name;
    }    
    
    public int getId(){
        return id;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getClan() {
        return clan;
    }

    public void setClan(String clan) {
        this.clan = clan;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Возраст не может быть отрицательным");
        }
        this.age = age;
    }

    public double getActivityCoefficient() {
        return activityCoefficient;
    }

    public void setActivityCoefficient(double activityCoefficient) {
        if (activityCoefficient < 0.1 || activityCoefficient > 2.0) {
            throw new IllegalArgumentException("Коэффициент активности должен быть в диапазоне от 0.1 до 2.0");
        }
        this.activityCoefficient = activityCoefficient;
    }
    
        // Метод, возвращающий HTML-информацию о викинге
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