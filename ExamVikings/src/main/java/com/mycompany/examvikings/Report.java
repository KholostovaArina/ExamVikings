package com.mycompany.examvikings;

import java.util.*;
import java.util.stream.Collectors;

public class Report {

    public static double food = 0; // меры еды
    public static List<Loot> loots;
    public static int slaves = 0;
    public static int id = 0;

    public static class ReportData {
        public final List<CityVisit> visits = new ArrayList<>();
        public City startCity;
        public double totalTime;
        public boolean foodRanOut;
        public boolean tooLong;
        public boolean noSuccess = true;
        public boolean possible;
        public int totalSlaves;
        public int totalLoots;
        public List<String> lootList = new ArrayList<>();
        public int reportId;

        public static class CityVisit {
            public final City city;
            public final double time;
            public final boolean success;
            public final List<Loot> loots;
            public final int newSlaves;
            public final double remainingFood;

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
            eat(timeHome, vikings);
        }

        Map<String, Long> lootMap = loots.stream()
                .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()));
        for (Map.Entry<String, Long> entry : lootMap.entrySet()) {
            data.lootList.add(entry.getKey() + " x" + entry.getValue());
        }

        data.possible = !(data.tooLong || data.foodRanOut || data.noSuccess);
       
        return data;
    }

    private static boolean canAddLoot(Drakkar drakkar, Set<Viking> vikings) {
        double foodWeight = food * 20;//в день по 2 кг викинги съедают
        double lootWeight = loots.size() * 20 ;
        double vikingsWeight = vikings.size() * 120; //средний вес викинга
        double slavesWeight = slaves * 80;// меньше пива пьют
        return (foodWeight + lootWeight + vikingsWeight + slavesWeight) <= drakkar.getCargoCapacity() * 1000;
    }

    private static boolean eat(double time, Set<Viking> vikings) {
        food -= (vikings.size() + slaves) * time / 240;
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

    private static double calculateTime(City c1, City c2, Set<Viking> vikings, Drakkar drakkar) {
        double speed = calculateSpeed(drakkar.getRowersPairs(), drakkar.getMaxSpeed(), vikings);
        double distance = calculateDistance(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude());
        return distance / speed;
    }

    private static boolean isSuccessfulAttack(City city, Set<Viking> vikings) {
        double probability = 1 - Math.pow(1 - 0.1 / city.getScale(), vikings.size());
        return probability > 0.5;
    }
}