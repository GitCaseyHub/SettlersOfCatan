import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.*;

public class PlayerView extends JFrame implements MouseMotionListener {
    Player player;
    JPanel entirePanel = new JPanel(new BorderLayout());
    JLabel nameLabel = new JLabel("Name: ", 0);
    JLabel colorLabel = new JLabel("Color: ",0);
    JLabel classLabel = new JLabel("Class: ",0);
    JTextArea descriptionArea = new JTextArea();
    JLabel classImageLabel = new JLabel("",0);
    JLabel resourceLabel = new JLabel("Resources: ",0);
    JLabel brickLabel = new JLabel("Brick: ",0);
    JLabel grainLabel = new JLabel("Grain: ",0);
    JLabel lumberLabel = new JLabel("Lumber: ",0);
    JLabel oreLabel = new JLabel("Ore: ",0);
    JLabel woolLabel = new JLabel("Wool: ",0);
    JLabel resourceImageLabel = new JLabel("",0);
    ArrayList<String> unplayedCards = new ArrayList<String>();
    ArrayList<String> playedCards = new ArrayList<String>();
    JLabel victoryPointLabel = new JLabel("Victory Points: ",0);
    
    JPanel northPanel = new JPanel(new GridLayout(1,2));
    JPanel centerPanel = new JPanel(new GridLayout(2,1));
    JPanel upperHalf = new JPanel(new GridLayout(1,2));
    JPanel lowerHalf = new JPanel(new GridLayout(1,2));

    public PlayerView(Player player){
        entirePanel.add(northPanel,BorderLayout.NORTH);
            northPanel.add(nameLabel);
            northPanel.add(colorLabel);
            
        entirePanel.add(centerPanel,BorderLayout.CENTER);
            centerPanel.add(upperHalf);
            centerPanel.add(lowerHalf);
         
        entirePanel.add(victoryPointLabel,BorderLayout.SOUTH);    
        
        brickLabel.addMouseMotionListener(this);
        grainLabel.addMouseMotionListener(this);
        lumberLabel.addMouseMotionListener(this);
        woolLabel.addMouseMotionListener(this);
        oreLabel.addMouseMotionListener(this);
    }

    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {

    }

    public static void main(String[] args){
        new PlayerView(new Player());
    }
}
