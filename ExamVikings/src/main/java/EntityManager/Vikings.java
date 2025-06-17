package EntityManager;

import Entity.Viking;
import Reading.SQLReader;
import java.util.Iterator;
import java.util.List;

/**
 * Класс, реализующий менеджер викингов.
 * Содержит статические методы для управления списком всех викингов.
 * Включает операции добавления, удаления и обновления информации о викингах.
 */
public class Vikings {
    
    /**
     * Конструктор по умолчанию.
     */
    public Vikings() {
        // конструктор
    }
    
    /**
     * Список всех викингов, загруженных из базы данных.
     */
    private static final List<Viking> allVikings = SQLReader.readVikings();

    /**
     * Возвращает список всех викингов.
     *
     * @return неизменяемый список объектов {@link Viking}
     */
    public static List<Viking> getAllVikings() {
        return allVikings;
    }
    
    /**
     * Добавляет нового викинга в общий список.
     *
     * @param newViking новый объект {@link Viking}
     */
    public static void addViking(Viking newViking) {
        allVikings.add(newViking);
    }
    
  
    /**
     * Увеличивает возраст всех викингов на 1 год.
     * Если возраст достигает 55 лет, викинг удаляется из списка.
     */
    public static void increaseVikingsAge() {
        if (allVikings == null || allVikings.isEmpty()) {
            return;
        }

        Iterator<Viking> it = allVikings.iterator();
        while (it.hasNext()) {
            Viking v = it.next();
            v.setAge(v.getAge() + 1);
            if (v.getAge() >= 55) {
                it.remove(); // безопасно удаляем
            }
        }
    }
}