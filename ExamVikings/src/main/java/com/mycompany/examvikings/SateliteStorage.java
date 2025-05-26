package com.mycompany.examvikings;

import java.util.ArrayList;
import java.util.List;

public class SateliteStorage {
    private List<Satelite> vikings = new ArrayList<>();

    public SateliteStorage() {
        // Загрузка "из памяти"
        vikings.add(new Satelite(1, "Эрик Красный","м","Йомсвикинг", 35,1.2, "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг1.png", "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг11.png"));
        vikings.add(new Satelite(2, "Торвальд","м", "Скальд", 28, 0.8, "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг2.png", "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг22.png"));
        vikings.add(new Satelite(3, "Олаф","м", "Карл", 40, 1.4, "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг3.png", "C:\\Users\\GOSPOGA\\OneDrive\\Рабочий стол\\викинг33.png"));
    }

    public List<Satelite> getAllVikings() {
        return vikings;
    }

    public Satelite getVikingById(int id) {
        return vikings.stream()
                .filter(v -> v.getId() == id)
                .findFirst()
                .orElse(null);
    }
}