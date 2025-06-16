package EntityManager;

import Entity.Viking;
import Reading.SQLReader;
import java.util.Iterator;
import java.util.List;


public class Vikings {
    private static final List<Viking> allVikings = SQLReader.readVikings();

    public static List<Viking> getAllVikings() {
        return allVikings;
    }
    
    public static void addViking(Viking newViking) {
        allVikings.add(newViking);
    }
    
     public static void deleteOldVikingByID(int id){
         allVikings.remove(id);
     }
    
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