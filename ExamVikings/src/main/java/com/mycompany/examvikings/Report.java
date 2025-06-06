
package com.mycompany.examvikings;

import java.util.*;
import java.util.stream.Collectors;

public class Report {
    private static double food = 0; //1 мера - 10 дней = 240 часов
    private static int slaves = 0;

    public static void doReport(Set<Viking> vikings, List<City> cityList, Drakkar drakkar) {
        System.out.println("=============Report================");
        
        food = 2* vikings.size();

        City startCity = cityList.get(0);
        City currentCity = startCity;
        double time;

        for (int i = 1; i < cityList.size(); i++) {
            City nextCity = cityList.get(i);
            time = calculateTime(currentCity, nextCity, vikings, drakkar);
            System.out.println( "---time   "+time);
            food -= (vikings.size()+slaves)*time/240;// eat food
            System.out.println(" food " + food);
            System.out.println(" slaves " + slaves);   
            System.out.println("Probability? "+ isSuccessfulAttack(nextCity, vikings));
            if (isSuccessfulAttack(nextCity, vikings)){
                food += 10 * nextCity.getScale();
                slaves +=2 * nextCity.getScale();
            }
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
    
    private static boolean isSuccessfulAttack(City city, Set<Viking> vikings){
        double probability =1-Math.pow(1-0.1/city.getScale(), vikings.size());
        return (probability>0.5);
    }

}
