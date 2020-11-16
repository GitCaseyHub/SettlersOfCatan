import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Random;

public class CatanBoard extends JFrame implements MouseListener {
    String[] types = {"Mountain","Mountain","Mountain","Brick","Brick","Brick","Trees","Trees","Trees","Trees","Plains","Plains","Plains","Plains","Wheat","Wheat","Wheat","Wheat","Desert"};
    int[] rollNums = {8,4,11,12,3,11,10,9,6,9,5,2,4,5,10,8,3,6};
    ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<Integer> rollNumList = new ArrayList<Integer>();
    Tile tiles[] = new Tile[19];

    public CatanBoard(){
        for(int x=0; x<types.length; x++)
            typeList.add(types[x]);

        for(int x=0; x<rollNums.length; x++)
            rollNumList.add(rollNums[x]);

        for(int x=0; x<types.length; x++){
            int typeIndex = new Random().nextInt(typeList.size());
            tiles[x] = new Tile(new int[]{},typeList.get(typeIndex),0,false);
            typeList.remove(typeIndex);

            if(tiles[x].getType().equals("Desert"))
                tiles[x].setNum(7);

            else{
                int rollIndex = new Random().nextInt(rollNumList.size());
                tiles[x].setNum(rollNumList.get(rollIndex));
                rollNumList.remove(rollIndex);
            }
        }
    }

    public static void main(String[] args){
        CatanBoard cb = new CatanBoard();
    }
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
