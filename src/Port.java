import java.awt.*;

public class Port {
    Point[] locations;
    String type;

    public Port(Point[] locations, String type){
        this.locations=locations;
        this.type=type;
    }

    public Point[] getLocations() {
        return locations;
    }

    public String getType() {
        return type;
    }
}