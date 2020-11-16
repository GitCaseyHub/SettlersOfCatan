import java.awt.*;

public class Tile {
    int[] position;
    String type;
    int num;
    boolean hasRobber;

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Tile(int[] position, String type, int num, boolean hasRobber){
        this.position=position;
        this.type=type;
        this.num=num;
        this.hasRobber=hasRobber;
    }
}
