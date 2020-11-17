import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CatanBoard extends JFrame implements MouseListener {
    //Objects for Board Generation
    String[] types = {"Mountain","Mountain","Mountain","Brick","Brick","Brick","Forest","Forest","Forest","Forest","Plains","Plains","Plains","Plains","Grain","Grain","Grain","Grain","Desert"};
    int[] rollNums = {8,4,11,12,3,11,10,9,6,9,5,2,4,5,10,8,3,6};
    int[][] coords = {{267,87},{267+134,87},{267+2*134,87},{200,200},{334,200},{200+2*134,200},{200+3*134,200},{133,313},{133+134,313},{133+2*134,313},{133+3*134,313},{133+4*134,313},{200,426},{200+134,426},{200+134*2,426},{200+134*3,426},{267,426+113},{267+134,426+113},{267+134*2,426+113}};
    ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<Integer> rollNumList = new ArrayList<Integer>();
    ArrayList<int[]> coordList = new ArrayList<int[]>();
    Tile tiles[] = new Tile[19];

    //Paint/Repaint Conditions
    boolean loaded=false;
    boolean paintCondition=false;
    int chosen_x = 0;
    int chosen_y = 0;

    //Index Creation for Later Use
    ArrayList<int[]> coord = new ArrayList<int[]>();
    int[][] indexCoords = {{264,122},{330,87},{398,122},{461,87},{533,121},{599,87},{658,122},{663,200},{599,234},{532,200},{463,233},{396,201},{329,235},{262,202},{196,235},{197,312},{262,347},{329,312},{394,348},{461,311},{528,346},{599,312},{665,350},{727,313},{729,237},{797,346},{798,425},{725,461},{660,423},{597,457},{527,424},{462,459},{393,422},{327,460},{258,421},{198,459},{132,423},{130,345},{200,536},{263,572},{329,537},{393,574},{464,534},{526,574},{599,536},{661,671},{655,646},{594,683},{529,652},{461,687},{392,646},{526,684},{266,647},{332,686},{664,577},{731,536}};
    Index[] indexes = new Index[indexCoords.length];

    public CatanBoard(){
        this.addMouseListener(this);
        for(int x=0; x<types.length; x++)
            typeList.add(types[x]);

        for(int x=0; x<rollNums.length; x++)
            rollNumList.add(rollNums[x]);

        for(int x=0; x<coords.length; x++)
            coordList.add(coords[x]);

        for(int x=0; x<indexes.length; x++)
            indexes[x] = new Index(indexCoords[x],false);

        for(int x=0; x<types.length; x++){
            int typeIndex = new Random().nextInt(typeList.size());
            int coordIndex = new Random().nextInt(coordList.size());
            tiles[x] = new Tile(coordList.get(coordIndex),typeList.get(typeIndex),0,false);
            typeList.remove(typeIndex);
            coordList.remove(coordIndex);

            if(tiles[x].getType().equals("Desert"))
                tiles[x].setNum(7);

            else{
                int rollIndex = new Random().nextInt(rollNumList.size());
                tiles[x].setNum(rollNumList.get(rollIndex));
                rollNumList.remove(rollIndex);
            }
        }
    }

    public void paint(Graphics g){
        if(!loaded)
        for(int x=0; x<tiles.length; x++) {
            try{
                BufferedImage tile = ImageIO.read(new File("Tiles/"+tiles[x].getType()+".png"));
                g.drawImage(tile, tiles[x].getPosition()[0],tiles[x].getPosition()[1], null);
            }
            catch (IOException ie) {
                ie.printStackTrace();
            }
            loaded=true;
        }
        if(paintCondition){
            try {
                BufferedImage settlement = ImageIO.read(new File("Tiles/Settlement.png"));
                g.drawImage(settlement,chosen_x,chosen_y,null);
            }
            catch(IOException ie){
                ie.printStackTrace();
            }
            paintCondition=false;
        }
    }

    public static void main(String[] args){
        CatanBoard cb = new CatanBoard();
        cb.setUndecorated(true);
        cb.setBounds(100,100,1000,1000);
        cb.setVisible(true);
        cb.setTitle("Settlers of Catan");
    }
    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        //Code to Draw City
        int xLoc = e.getX();
        int yLoc = e.getY();
        for(int x=0; x<indexCoords.length; x++){
            if(Math.abs(indexCoords[x][0]-xLoc) < 20 && Math.abs(indexCoords[x][1]-yLoc)<20) {
                Index checkedIndex = returnAppropIndex(indexCoords[x][0],indexCoords[x][1]);
                for(int i=0; i<indexes.length; i++){
                    if(!checkedIndex.isTaken()) {
                        chosen_x = indexCoords[x][0] - 5;
                        chosen_y = indexCoords[x][1] - 16;
                        paintCondition = true;
                        repaint();
                        indexes[i].setTaken(true);
                        break;
                    }
                    else{
                        JOptionPane.showMessageDialog(this,"Taken");
                        break;
                    }
                }
            }
        }
    }
    public Index returnAppropIndex(int chosen_x, int chosen_y){
        for(int x=0; x<indexes.length; x++)
            if(indexes[x].getLocation()[0]==chosen_x && indexes[x].getLocation()[1]==chosen_y)
                return indexes[x];

        return null;
    }

    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
