import java.awt.*;

public class Port {
    Point[] locations;
    String type;
    boolean occupied;

    public Port(Point[] locations, String type, boolean occupied){
        this.locations=locations;
        this.type=type;
        this.occupied=occupied;
    }

    public Point[] getLocations() {
        return locations;
    }

    public void setLocations(Point[] locations) {
        this.locations = locations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}
