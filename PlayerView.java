import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class PlayerView extends JFrame {
    //Fancy Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

    //GUI Assets
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JLabel brickNum = new JLabel("0", 0);
    JLabel oreNum = new JLabel("0", 0);
    JLabel wheatNum = new JLabel("0", 0);
    JLabel sheepNum = new JLabel("0", 0);
    JLabel woodNum = new JLabel("0", 0);

    JPanel borderNorth = new JPanel(new BorderLayout());
    JPanel infoPanel = new JPanel(new GridLayout(1,2));
    JLabel colorDisplayLabel = new JLabel("",0);
    JPanel devPanel = new JPanel(new GridLayout(2,1));
        JComboBox unplayed = new JComboBox();
        JComboBox played = new JComboBox();
    JPanel awardPanel = new JPanel(new GridLayout(2,1));
        JCheckBox longestRoadBox = new JCheckBox("Longest Road");
        JCheckBox largestArmy = new JCheckBox("Largest Army");

    //Constructor Variables
    Player player;

    public PlayerView(Player player) {
        this.player = player;
        this.setLayout(new BorderLayout());
        this.add(graphicHolder, BorderLayout.CENTER);
        this.add(borderNorth,BorderLayout.NORTH);
            borderNorth.setBorder(compound);
            borderNorth.add(infoPanel,BorderLayout.CENTER);
            borderNorth.add(colorDisplayLabel,BorderLayout.WEST);
            borderNorth.setBackground(Color.white);
            System.out.println("Pieces/"+player.getColor()+"_City.png");
                colorDisplayLabel.setBorder(compound);
                colorDisplayLabel.setIcon(new ImageIcon("Pieces/Red_City.png"));
            infoPanel.add(devPanel);
                infoPanel.setBorder(compound);
                devPanel.setBorder(new TitledBorder("Development Cards"));
                //devPanel.setBorder(compound);
                devPanel.add(unplayed);
                    unplayed.setBorder(compound);
                devPanel.add(played);
                    played.setBorder(compound);
            infoPanel.add(awardPanel);
                awardPanel.setBorder(new TitledBorder("Global Awards"));
               // awardPanel.setBorder(compound);
                awardPanel.add(longestRoadBox);
                    longestRoadBox.setBorder(compound);
                    longestRoadBox.setBorderPainted(true);
                awardPanel.add(largestArmy);
                    largestArmy.setBorder(compound);
                    largestArmy.setBorderPainted(true);
        //graphicHolder.setBorder(compound);
        for (int x = 0; x < 5; x++) {
            graphicPanels[x] = new JPanel(new BorderLayout());
            graphicImageLabels[x] = new JLabel("", 0);
            graphicImageLabels[x].setIcon(new ImageIcon("Resources/" + graphicStrings[x] + "_Image.png"));
            graphicPanels[x].add(graphicImageLabels[x], BorderLayout.CENTER);
            graphicImageLabels[x].setBorder(compound);
            graphicHolder.add(graphicPanels[x]);
        }
        graphicPanels[0].add(brickNum, BorderLayout.SOUTH);
        brickNum.setBorder(compound);
        graphicPanels[1].add(oreNum, BorderLayout.SOUTH);
        oreNum.setBorder(compound);
        graphicPanels[2].add(sheepNum, BorderLayout.SOUTH);
        sheepNum.setBorder(compound);
        graphicPanels[3].add(wheatNum, BorderLayout.SOUTH);
        wheatNum.setBorder(compound);
        graphicPanels[4].add(woodNum, BorderLayout.SOUTH);
        woodNum.setBorder(compound);
        this.setVisible(true);
        this.setTitle(player.getName()+" - "+player.getClassTitle());
        this.setBounds(100, 100, 475, 205);
        update();
    }

    public static void main(String[] args) {
        new PlayerView(new Player());
    }

    public void update(){
        brickNum.setText(""+player.getBrickNum());
        oreNum.setText(""+player.getOreNum());
        sheepNum.setText(""+player.getWoolNum());
        wheatNum.setText(""+player.getGrainNum());
        woodNum.setText(""+player.getLumberNum());
    }
}
