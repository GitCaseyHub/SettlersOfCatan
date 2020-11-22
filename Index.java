import java.util.ArrayList;

public class Index {
    int[] location;
    boolean taken;
    ArrayList<Index> connections = new ArrayList<Index>();
    int indexID;

    public int getIndexID() {
        return indexID;
    }

    public void setIndexID(int indexID) {
        this.indexID = indexID;
    }

    public Index(int[] location, boolean taken, ArrayList<Index> connections, int indexID){
        this.location = location;
        this.taken=taken;
        this.connections=connections;
        this.indexID=indexID;
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
