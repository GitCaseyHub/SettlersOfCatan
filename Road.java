import java.awt.*;

public class Road {
    int indexA, indexB;
    Player owner;
    Point position;
    String roadType;

    public Point getPosition() {
        return position;
    }

    public Road(int indexA, int indexB, Player owner, Point position, String roadType){
        this.indexA=indexA;
        this.indexB=indexB;
        this.owner=owner;
        this.roadType=roadType;
        this.position=position;
    }

    public Road(){}

    public String getRoadType() {
        return roadType;
    }

    public int getIndexA() {
        return indexA;
    }

    public void setIndexA(int indexA) {
        this.indexA = indexA;
    }

    public int getIndexB() {
        return indexB;
    }

    public void setIndexB(int indexB) {
        this.indexB = indexB;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    @Override
    public String toString(){
        return "Index (A,B): ("+getIndexA()+","+getIndexB()+")";
    }

    public boolean isConnectedTo(Road road){
        return road.getIndexA() == this.getIndexA() || road.getIndexA() == this.getIndexB() || road.getIndexB() == this.getIndexA() || road.getIndexB() == this.getIndexB();
    }
}
