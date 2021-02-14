import java.awt.*;
import java.util.ArrayList;

public class Tile {
    int[] position;
    String type;
    int num;
    boolean hasRobber,onFire;
    Point upper_left;
    Point center;
    Player firePlayer;
    Shape robberRect;
    ArrayList<Point> vertices = new ArrayList<>();

    public Tile(int[] position, String type, int num, boolean hasRobber,boolean onFire, Player firePlayer){
        this.position=position;
        this.type=type;
        this.num=num;
        this.hasRobber=hasRobber;
        this.onFire=onFire;
        this.firePlayer=firePlayer;
        upper_left = new Point(position[0],position[1]);
        int x=(int)upper_left.getX();
        int y =(int)upper_left.getY();
        vertices.add(new Point(x+72,y));
        vertices.add(new Point(x,y+39));
        vertices.add(new Point(x,y+116));
        vertices.add(new Point(x+72,y+155));
        vertices.add(new Point(x+134,y+39));
        vertices.add(new Point(x+134,y+116));
        robberRect = new Rectangle(position[0]+5,position[1]+54,124,67);
        center = new Point(x+72, y+78);
    }

    public Point getCenter(){
        return center;
    }

    public Player getFirePlayer() {
        return firePlayer;
    }

    public void setFirePlayer(Player firePlayer) {
        this.firePlayer = firePlayer;
    }

    public int[] getPosition() {
        return position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type){
        this.type=type;
    }

    public boolean isOnFire() {
        return onFire;
    }

    public void setOnFire(boolean onFire) {
        this.onFire = onFire;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isHasRobber() {
        return hasRobber;
    }

    public void setHasRobber(boolean hasRobber) {
        this.hasRobber = hasRobber;
    }

    public Shape getRobberRect() {
        return robberRect;
    }

    public ArrayList<Point> getVertices(){
        return vertices;
    }
}
