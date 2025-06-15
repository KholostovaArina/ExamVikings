package EntityManager;

import Entity.Viking;
import Reading.SQLReader;
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
        if (allVikings == null || allVikings.isEmpty()) return;

        for (Viking v : allVikings) {
            v.setAge(v.getAge() + 1);
        }
    }
}
