
package com.mycompany.examvikings;

import java.util.*;
import java.util.stream.Collectors;

public class Report {

    public static void doReport(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        System.out.println("=============Report================");

        City startCity = cityList.get(0);
        City currentCity = startCity;

        for (int i = 1; i < cityList.size(); i++) {
            City nextCity = cityList.get(i);
            System.out.println(calculateTime(currentCity, nextCity, vikings, drakkar));
            currentCity = nextCity;
        }

        System.out.println(calculateTime(currentCity, startCity, vikings, drakkar));
    }


    
    //перерасчет скорости с коэффициентом активности гребцов
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

    //длина дороги
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS_KM = 6371; // радиус Земли в км

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // результат в километрах
    }
    
    private static double calculateTime(City c1, City c2, Set<Viking>  vikings, Drakkar drakkar){
        double speed = calculateSpeed(drakkar.getRowersPairs(), drakkar.getMaxSpeed(), vikings);
        double distance = calculateDistance(c1.getLatitude(), c1.getLongitude(), c2.getLatitude(), c2.getLongitude());
        return distance/speed ;
    }

}
