package Entity;

import java.util.Random;

public class NewVikings {
    private static final String[] MALE_NAMES = {
        "Эрик", "Бьёрн", "Харальд", "Лейф", "Рагнар", "Ульф", "Сигурд", "Тор", "Свен"
    };
    private static final String[] FEMALE_NAMES = {
        "Астрид", "Фрейя", "Гунна", "Сигрид", "Гудрун", "Хильда", "Рагна", "Тора"
    };
    private static final String[] CLANS = {
        "Скьёльдунги", "Инълинги", "Фолькунги", "Лёдинги"
    };
    private static final String[] PHOTO_MINI = {
        "img/vikings/m1.png", "img/vikings/m2.png", "img/vikings/f1.png", "img/vikings/f2.png"
    };
    
    private static final Random random = new Random();
    private static int idCounter = 1000;

    public static Viking createNewViking() {
        String gender = random.nextBoolean() ? "Мужчина" : "Женщина";

        String name = gender.equals("Мужчина") ?
                MALE_NAMES[random.nextInt(MALE_NAMES.length)] :
                FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];

        String clan = CLANS[random.nextInt(CLANS.length)];

        int age = 18 + random.nextInt(33);

        // Activity coefficient (например, от 0.7 до 1.3)
        double activityCoefficient = 0.7 + (random.nextDouble() * 0.6);

        // Фото (примитивно: для мужчин — любые m*, для женщин — любые f*)
        String photoMiniPath, photoPath;
        if (gender.equals("Мужчина")) {
            String[] maleMini = {"vikings/m1mini.png", "vikings/m2mini.png"};
            String[] malePhoto = {"vikings/m1.png", "vikings/m2.png"};
            int i = random.nextInt(maleMini.length);
            photoMiniPath = maleMini[i];
            photoPath = malePhoto[i];
        } else {
            String[] femaleMini = {"vikings/f1mini.png", "vikings/f2mini.png"};
            String[] femalePhoto = {"vikings/f1.png", "vikings/f2.png"};
            int i = random.nextInt(femaleMini.length);
            photoMiniPath = femaleMini[i];
            photoPath = femalePhoto[i];
        }

        // ID
        int id = idCounter++;

        return new Viking(id, name, gender, clan, age, activityCoefficient, photoMiniPath, photoPath);
    }
}
