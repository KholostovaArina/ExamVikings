package EntityManager;

import Entity.Loot;
import java.util.*;

public class LootManager {
    private  List<Loot> lootList;
    private Map<String, List<String>> lootTypesByCityType;

    public LootManager() {
        lootList = new ArrayList<>();
        lootList.add(new Loot("Жемчужная брошь", "Драгоценности"));
        lootList.add(new Loot("Серебряный кубок", "Драгоценности"));
        lootList.add(new Loot("Ткань", "Товары народного потребления"));
        lootList.add(new Loot("Глиняная посуда", "Товары народного потребления"));
        lootList.add(new Loot("Пшеница", "Продукты питания"));
        lootList.add(new Loot("Молоко", "Продукты питания"));
        lootList.add(new Loot("Шкуры оленя", "Кожи"));
        lootList.add(new Loot("Баранья шкура", "Кожи"));
        lootList.add(new Loot("Окунь", "Рыба"));
        lootList.add(new Loot("Акула", "Рыба"));

        lootTypesByCityType = new HashMap<>();

        lootTypesByCityType.put("Монастырь", Arrays.asList("Драгоценности", "Товары народного потребления", "Продукты питания"));
        lootTypesByCityType.put("Торговый пункт", Arrays.asList("Драгоценности", "Товары народного потребления", "Продукты питания", "Кожи", "Рыба"));
        lootTypesByCityType.put("Земледельческая деревня", Arrays.asList("Продукты питания", "Товары народного потребления"));
        lootTypesByCityType.put("Скотоводческая деревня", Arrays.asList("Кожи", "Продукты питания"));
        lootTypesByCityType.put("Рыбацкая деревня", Arrays.asList("Рыба", "Продукты питания"));
        lootTypesByCityType.put("Ремесленная деревня", Arrays.asList("Товары народного потребления", "Кожи"));
    }

    // Получить случайный лут по типу города
    public Loot getRandomLootByCityType(String cityType) {
        List<String> allowedLootTypes = lootTypesByCityType.get(cityType);
        if (allowedLootTypes == null || allowedLootTypes.isEmpty()) return null;

        List<Loot> filtered = new ArrayList<>();
        for (Loot loot : lootList) {
            if (allowedLootTypes.contains(loot.getType())) {
                filtered.add(loot);
            }
        }
        if (filtered.isEmpty()) return null;
        Random random = new Random();
        return filtered.get(random.nextInt(filtered.size()));
    }
}