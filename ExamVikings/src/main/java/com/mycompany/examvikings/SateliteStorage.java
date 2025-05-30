package com.mycompany.examvikings;

import java.util.ArrayList;
import java.util.List;

public class SateliteStorage {
    private List<Satelite> vikings = new ArrayList<>();

    public SateliteStorage() {
        // Загрузка "из памяти"
        vikings.add(new Satelite(1, "Эрик Красный","м","Йомсвикинг", 35,1.2, "/викинг1.png", "\\викинг11.png"));
        vikings.add(new Satelite(2, "Торвальд","м", "Скальд", 28, 0.8, "\\викинг2.png", "\\викинг22.png"));
        vikings.add(new Satelite(3, "Олаф","м", "Карл", 40, 1.4, "\\викинг3.png", "\\викинг33.png"));
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