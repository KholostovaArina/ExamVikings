package Entity;

import java.util.Random;

/**
 * Утилитный класс для создания новых случайных объектов типа {@link Viking}.
 * Содержит предопределённые списки имен, кланов и логику генерации случайного викинга.
 */
public class NewVikings {
    
    /**
     * Массив мужских имён, используемых для генерации случайного имени викинга.
     */
    private static final String[] MALE_NAMES = {
        "Эрик", "Бьёрн", "Харальд", "Лейф", "Рагнар", "Ульф", "Сигурд", "Тор", "Свен"
    };

    /**
     * Массив женских имён, используемых для генерации случайного имени викинговки.
     */
    private static final String[] FEMALE_NAMES = {
        "Астрид", "Фрейя", "Гунна", "Сигрид", "Гудрун", "Хильда", "Рагна", "Тора"
    };

    /**
     * Массив названий кланов, из которых случайным образом выбирается клан для нового викинга.
     */
    private static final String[] CLANS = {
        "Скьёльдунги", "Инълинги", "Фолькунги", "Лёдинги"
    };
    
    /**
     * Генератор случайных чисел, используемый для всех операций рандомизации.
     */
    private static final Random random = new Random();

    /**
     * Счётчик ID для автоматического присвоения уникальных идентификаторов новым викингам.
     */
    private static int idCounter = 1000;

    /**
     * Создаёт и возвращает новый экземпляр случайно сгенерированного викинга.
     *
     * @return новый случайный объект типа {@link Viking}
     */
    public static Viking createNewViking() {
        // Определяем пол
        String gender = random.nextBoolean() ? "Мужчина" : "Женщина";

        // Выбираем имя в зависимости от пола
        String name = gender.equals("Мужчина") ?
                MALE_NAMES[random.nextInt(MALE_NAMES.length)] :
                FEMALE_NAMES[random.nextInt(FEMALE_NAMES.length)];

        // Выбираем случайный клан
        String clan = CLANS[random.nextInt(CLANS.length)];

        // Возраст: от 18 до 50 лет
        int age = 18 + random.nextInt(33);

        // Коэффициент активности — от 0.7 до 1.3
        double activityCoefficient = 0.7 + (random.nextDouble() * 0.6);

        // Определяем пути к изображениям в зависимости от пола
        String photoMiniPath, photoPath;
        if (gender.equals("Мужчина")) {
            String[] maleMini = {"/викинги/викинг12.png", "/викинги/викинг13.png"};
            String[] malePhoto = {"/викинги/викинг3.png", "/викинги/викинг2.png"};
            int i = random.nextInt(maleMini.length);
            photoMiniPath = maleMini[i];
            photoPath = malePhoto[i];
        } else {
            String[] femaleMini = {"/викинги/викинг15.png", "/викинги/викинг110.png"};
            String[] femalePhoto = {"/викинги/викинг5.png", "/викинги/викинг10.png"};
            int i = random.nextInt(femaleMini.length);
            photoMiniPath = femaleMini[i];
            photoPath = femalePhoto[i];
        }

        // Присваиваем уникальный ID
        int id = idCounter++;

        // Возвращаем новый экземпляр викинга
        return new Viking(id, name, gender, clan, age, activityCoefficient, photoMiniPath, photoPath);
    }
}