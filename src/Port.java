import java.awt.*;

public class Port {
    Point[] locations;
    String type;
    boolean isDestroyed=false;

    public Port(Point[] locations, String type){
        this.locations=locations;
        this.type=type;
    }

    public Port(){}

    public Point[] getLocations() {
        return locations;
    }

    public String getType() {
        return type;
    }
    
    public void destroy(){
        this.isDestroyed=true;
    }
}
