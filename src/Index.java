public class Index {
    int[] location;
    boolean taken, isSettlement, isCity;
    int indexID;
    Player owner;

    public Index(int[] location, boolean taken, int indexID, Player owner, boolean isSettlement, boolean isCity){
        this.location = location;
        this.taken=taken;
        this.owner=owner;
        this.indexID=indexID;
        this.isCity=isCity;
        this.isSettlement=isSettlement;
    }

    public boolean isSettlement() {
        return isSettlement;
    }

    public void setSettlement(boolean settlement) {
        isSettlement = settlement;
    }

    public boolean isCity() {
        return isCity;
    }

    public void setCity(boolean city) {
        isCity = city;
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

    public int[] getLocation() {
        return location;
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