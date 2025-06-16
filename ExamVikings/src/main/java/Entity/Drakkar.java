package Entity;

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Drakkar {
    private int id;
    private String name;
    private int crewCapacity;
    private int rowersPairs; 
    private int cargoCapacity;
    private double weight;

    public Drakkar() {
    }

    public Drakkar(int id, String name, int crewCapacity, int rowersPairs, int cargoCapacity, double weight) {
        this.id=id;
        this.name = name;
        this.crewCapacity = crewCapacity;
        this.rowersPairs = rowersPairs;
        this.cargoCapacity = cargoCapacity;
        this.weight = weight;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    
    public int getCrewCapacity() {return crewCapacity;}
    public void setCrewCapacity(int crewCapacity) {this.crewCapacity = crewCapacity;}

    public int getRowersPairs() {return rowersPairs;}
    public void setRowersPairs(int rowersPairs) {this.rowersPairs = rowersPairs;}

    public int getCargoCapacity() {return cargoCapacity;}
    public void setCargoCapacity(int cargoCapacity) {this.cargoCapacity = cargoCapacity;}

    public double getWeight() {return weight;}
    public void setWeight(double weight) {this.weight = weight;}

    
    
    public double getMaxSpeed() {
        double koef_weight = Math.pow((double) weight / 20, 0.2); // тяжелые едут тяжелее
        return rowersPairs * 2 / koef_weight; // 1 км/ч - вклад гребца 
    }
    

    public Image getImage(){
        Image image = null;
        try {
            image = ImageIO.read(Drakkar.class.getResourceAsStream("/драккар"+ id +".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
       return image; 
    }
}