public class Index {
    int[] location;
    boolean taken;

    public Index(int[] location,boolean taken){
        this.location = location;
        this.taken=taken;
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
}
