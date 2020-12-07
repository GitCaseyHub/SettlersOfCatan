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

    public void setLocations(Point[] locations) {
        this.locations = locations;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
