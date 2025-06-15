package Entity;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private static double silver = 1000;
    private static double food = 200;
    private static int slaves = 0;
    private static final List<Loot> products = new ArrayList<>();

    private Inventory() {}

    public static double getSilver() {
        return silver;
    }
    public static void setSilver(double newSilver) {
        silver = newSilver;
    }

    // Методы для еды
    public static double getFood() {
        return food;
    }
    public static void setFood(double newFood) {
        food = newFood;
    }

    public static int getSlaves() {
        return slaves;
    }
    public static void setSlaves(int newSlaves) {
        slaves = newSlaves;
    }

    public static List<Loot> getProducts() {
        return products;
    }
    public static void addProduct(Loot loot) {
        products.add(loot);
    }
    public static void removeProduct(Loot loot) {
        products.remove(loot);
    }
}
