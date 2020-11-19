import java.util.ArrayList;

public class Index {
    int[] location;
    boolean taken;
    ArrayList<Index> connections = new ArrayList<Index>();

    public Index(int[] location,boolean taken,ArrayList<Index> connections){
        this.location = location;
        this.taken=taken;
        this.connections=connections;
    }

    public int[] getLocation() {
        return location;
    }

    public void setLocation(int[] location) {
        this.location = location;
    }

    public boolean isTaken() {
        return taken;
    }

    public void setTaken(boolean taken) {
        this.taken = taken;
    }

    public void addConnection(Index secondIndex){
        this.connections.add(secondIndex);
        secondIndex.connections.add(this);
    }

    public boolean isConnectedTo(Index checkedIndex){
        if(this.connections.contains(checkedIndex) && checkedIndex.connections.contains(this))
            return true;

        else
            return false;
    }

    public ArrayList<Index> getConnections(){
        return connections;
    }

    @Override
    public String toString(){
        return "(x,y) of Index: ("+this.location[0]+","+this.location[1]+")";
    }
}
