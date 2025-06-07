package Entity;

public class City {

    private final int id;
    private final String name;
    private final double latitude;
    private final double longitude;   
    private final String cityType;
    private final int scale;

    public City(int id, String name, double latitude, double longitude, String cityType, int scale) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cityType = cityType;
        this.scale = scale;
    }

    public int getId() {return id;}

    public String getName() {return name;}

    public double getLatitude() {return latitude;}

    public double getLongitude() {return longitude;}

    public String getCityType() {return cityType;}

    public int getScale() {return scale;}
    
    
    
}