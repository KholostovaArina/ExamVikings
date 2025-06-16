package Entity;

import EntityManager.LootManager;
import EntityManager.HistoryReportsManager;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс, отвечающий за генерацию отчётов о набегах викингов.
 * Содержит данные о маршруте, добыче, еде и времени похода.
 */
public class Report {

    /**
     * Общее количество еды (в мерах) на момент начала похода.
     * 1 мера = 10 дней выживания для одного викинга или раба.
     */
    public static double food = 0; // меры еды

    /**
     * Список всех предметов добычи, собранных во время набега.
     */
    public static List<Loot> loots;

    /**
     * Текущее количество рабов, взятых в поход.
     */
    public static int slaves = 0;

    /**
     * Глобальный счётчик отчетов для присвоения уникальных идентификаторов.
     */
    public static int id = 0;

    /**
     * Вложенный класс, представляющий данные отчёта о набеге.
     * Содержит информацию о каждом посещённом городе, общем результате похода и прочих метриках.
     */
    public static class ReportData {
        
        /**
         * Список посещений городов в порядке их обхода.
         */
        public final List<CityVisit> visits = new ArrayList<>();
        
        /**
         * Город, с которого начался поход.
         */
        public City startCity;
        
        /**
         * Общее время похода в часах.
         */
        public double totalTime;
        
        /**
         * Флаг, указывающий, закончилась ли еда.
         */
        public boolean foodRanOut;
        
        /**
         * Флаг, указывающий, слишком ли долгий поход (более 60 дней).
         */
        public boolean tooLong;
        
        /**
         * Флаг, указывающий, не было ли ни одной успешной атаки.
         */
        public boolean noSuccess = true;
        
        /**
         * Итоговая возможность совершения похода.
         */
        public boolean possible;
        
        /**
         * Общее количество взятых рабов.
         */
        public int totalSlaves;
        
        /**
         * Общее количество собранной добычи.
         */
        public int totalLoots;
        
        /**
         * Список строк с информацией о собранной добыче.
         */
        public List<String> lootList = new ArrayList<>();
        
        /**
         * Уникальный идентификатор отчёта.
         */
        public int reportId;

        /**
         * Вложенный класс, представляющий посещение конкретного города.
         */
        public static class CityVisit {
            
            /**
             * Посещённый город.
             */
            public final City city;
            
            /**
             * Время в пути до этого города в часах.
             */
            public final double time;
            
            /**
             * Успешно ли была проведена атака на город.
             */
            public final boolean success;
            
            /**
             * Добыча, полученная в этом городе.
             */
            public final List<Loot> loots;
            
            /**
             * Количество новых рабов, взятых в этом городе.
             */
            public final int newSlaves;
            
            /**
             * Оставшееся количество еды после посещения города.
             */
            public final double remainingFood;

            /**
             * Создаёт новый объект CityVisit.
             *
             * @param city посещённый город
             * @param time время в пути
             * @param success успешность атаки
             * @param loots собранная добыча
             * @param newSlaves количество новых рабов
             * @param remainingFood оставшаяся еда
             */
            public CityVisit(City city, double time, boolean success, List<Loot> loots, int newSlaves, double remainingFood) {
                this.city = city;
                this.time = time;
                this.success = success;
                this.loots = loots;
                this.newSlaves = newSlaves;
                this.remainingFood = remainingFood;
            }
        }
    }

    /**
     * Генерирует отчёт о набеге на основе выбранных городов, экипажа и корабля.
     *
     * @param vikings множество викингов, участвующих в походе
     * @param cityList список городов в порядке посещения
     * @param drakkar используемый драккар
     * @return объект ReportData с данными о походе
     */
    public static ReportData generateReportData(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        ReportData data = new ReportData();
        id++;
        data.reportId = id;
        if (vikings.isEmpty() || cityList.size() < 2 || drakkar == null) {
            data.possible = false;
            return data;
        }

        food = 2 * vikings.size(); // 1 мера = 10 дней
        slaves = 0;
        loots = new ArrayList<>();

        LootManager lootik = new LootManager();

        City currentCity = cityList.get(0);
        data.startCity = currentCity;

        for (int i = 1; i < cityList.size(); i++) {
            City nextCity = cityList.get(i);
            double time = calculateTime(currentCity, nextCity, vikings, drakkar);

            data.totalTime += time;
            data.totalTime += 7*24;//7 дней находятся в городе

            if (data.totalTime / 24 > 60) {
                data.tooLong = true;
                break;
            }

            if (!eat(time, vikings)) {
                data.foodRanOut = true;
                break;
            }

            boolean success = isSuccessfulAttack(nextCity, vikings);
            if (isSuccessfulAttack(nextCity, vikings)){
                data.noSuccess = false;        
            }
            data.noSuccess &= !success;

            List<Loot> collectedLoots = new ArrayList<>();
            if (success) {
                for (int j = 0; j < 10 * nextCity.getScale(); j++) {
                    Loot loot = lootik.getRandomLootByCityType(nextCity.getCityType());
                    if (loot != null && canAddLoot(drakkar, vikings)) {
                        loots.add(loot);
                        collectedLoots.add(loot);
                        data.totalLoots++;
                    }
                }

                int maxSlavesAllowed = drakkar.getCrewCapacity() - vikings.size();
                int newSlavesToAdd = Math.min(2 * nextCity.getScale(), maxSlavesAllowed - slaves);

                if (newSlavesToAdd > 0) {
                    slaves += newSlavesToAdd;
                    data.totalSlaves += newSlavesToAdd;
                }
            }

            data.visits.add(new ReportData.CityVisit(
                    nextCity, time, success, collectedLoots, slaves, food
            ));
            currentCity = nextCity;
        }

        if (!data.tooLong && !data.foodRanOut) {
            double timeHome = calculateTime(currentCity, data.startCity, vikings, drakkar);
            data.totalTime += timeHome;
            
            if (data.totalTime / 24 > 60) {
                data.tooLong = true;
            }

            if (!eat(timeHome, vikings)) {
                data.foodRanOut = true;
            }
        }

        Map<String, Long> lootMap = loots.stream()
                .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()));
        for (Map.Entry<String, Long> entry : lootMap.entrySet()) {
            data.lootList.add(entry.getKey() + " x" + entry.getValue());
        }

        data.possible = !(data.tooLong || data.foodRanOut || data.noSuccess);
       
       // Добавляем отчет в историю
       HistoryReportsManager.getInstance().addReport(data);
        return data;
    }

    /**
     * Проверяет, может ли драккар вместить текущую нагрузку.
     *
     * @param drakkar драккар
     * @param vikings экипаж викингов
     * @return true, если груз помещается, иначе false
     */
    private static boolean canAddLoot(Drakkar drakkar, Set<Viking> vikings) {
        double foodWeight = food * 20;//в день по 2 кг викинги съедают
        double lootWeight = loots.size() * 20 ;
        double vikingsWeight = vikings.size() * 120; //средний вес викинга
        double slavesWeight = slaves * 80;// меньше пива пьют
        return (foodWeight + lootWeight + vikingsWeight + slavesWeight) <= drakkar.getCargoCapacity() * 1000;
    }

    /**
     * Расходует еду в зависимости от времени похода.
     *
     * @param time время в пути
     * @param vikings экипаж викингов
     * @return true, если еда ещё осталась, иначе false
     */
    private static boolean eat(double time, Set<Viking> vikings) {
        food -= (vikings.size() + slaves) * time / 240;//1 мера - 10 дней = 24 часов
        while (food < 0) {
            boolean foundFood = false;
            Iterator<Loot> it = loots.iterator();
            while (it.hasNext()) {
                Loot loot = it.next();
                if ("Продукты питания".equals(loot.getType()) || "Рыба".equals(loot.getType())) {
                    food += 1;
                    it.remove();
                    foundFood = true;
                    break;
                }
            }
            if (!foundFood) return false;
        }
        return true;
    }

    /**
     * Рассчитывает среднюю скорость драккара с учётом активности викингов.
     *
     * @param numberPairs количество пар гребцов
     * @param maxDefaultSpeed максимальная скорость без поправок
     * @param vikings экипаж викингов
     * @return рассчитанная скорость
     */
    private static double calculateSpeed(int numberPairs, double maxDefaultSpeed, Set<Viking> vikings) {
        List<Viking> sortedVikings = vikings.stream()
                .sorted(Comparator.comparingDouble(Viking::getActivityCoefficient).reversed())
                .collect(Collectors.toList());

        int count = Math.min(2 * numberPairs, sortedVikings.size());

        List<Viking> topVikings = sortedVikings.subList(0, count);

        double averageCoefficient = topVikings.stream()
                .mapToDouble(Viking::getActivityCoefficient)
                .average()
                .orElse(0.0);

        return averageCoefficient * maxDefaultSpeed;
    }

    /**
     * Рассчитывает расстояние между двумя точками на поверхности Земли.
     *
     * @param lat1 широта первой точки
     * @param lon1 долгота первой точки
     * @param lat2 широта второй точки
     * @param lon2 долгота второй точки
     * @return расстояние в километрах
     */
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    /**
     * Рассчитывает время пути между двумя городами.
     *
     * @param c1 начальный город
     * @param c2 конечный город
     * @param vikings экипаж викингов
     * @param drakkar используемый драккар
     * @return время в пути в часах
     */
    private static double calculateTime(City c1, City c2, Set<Viking> vikings, Drakkar drakkar) {
        double speed = calculateSpeed(drakkar.getRowersPairs(), drakkar.getMaxSpeed(), vikings);
        double distance = calculateDistance(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude());
        return distance / speed;
    }

    /**
     * Определяет вероятность успешной атаки на город.
     *
     * @param city атакуемый город
     * @param vikings экипаж викингов
     * @return true, если атака успешна, иначе false
     */
    private static boolean isSuccessfulAttack(City city, Set<Viking> vikings) {
        double probability = 1 - Math.pow(1 - 0.1 / city.getScale(), vikings.size());
        return probability > 0.5;
    }
}