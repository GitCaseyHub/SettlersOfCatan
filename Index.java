public class Index {
    int[] location;
    boolean taken;
    int indexID;
    Player owner;

    public Index(int[] location, boolean taken, int indexID, Player owner){
        this.location = location;
        this.taken=taken;
        this.owner=owner;
        this.indexID=indexID;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getIndexID() {
        return indexID;
    }

    public void setIndexID(int indexID) {
        this.indexID = indexID;
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

    @Override
    public String toString(){
        return "(x,y) of Index: ("+this.location[0]+","+this.location[1]+")";
    }
}
