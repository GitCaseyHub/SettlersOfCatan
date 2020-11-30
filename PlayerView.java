import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class PlayerView extends JFrame implements ActionListener {
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
    JPanel infoPanel = new JPanel(new GridLayout(1,1));
    JPanel borderInfoPanel = new JPanel(new GridLayout(1,1));
    JLabel colorDisplayLabel = new JLabel("",0);
    JPanel devPanel = new JPanel(new GridLayout(1,2));
    JComboBox unplayed = new JComboBox();
    JComboBox played = new JComboBox();
    JPanel awardPanel = new JPanel(new GridLayout(1,2));
    JCheckBox longestRoadBox = new JCheckBox("Longest Road");
    JCheckBox largestArmyBox = new JCheckBox("Largest Army");
    JPanel vpPointHolder = new JPanel();
    JPanel borderSouth = new JPanel(new BorderLayout());
    JLabel victoryPointLabel = new JLabel("0",0);
    JPanel turnBorderPanel = new JPanel();
    JPanel turnHolder = new JPanel(new BorderLayout());
    JCheckBox turnBox = new JCheckBox("");

    //Constructor Variables
    Player player;
    CatanBoard reference;

    //MenuBar
    JMenuBar mb = new JMenuBar();
    JMenu build = new JMenu("Build");
    JMenuItem settlement = new JMenuItem("Build Settlement");
    JMenuItem city = new JMenuItem("Upgrade Settlement to City");
    JMenuItem road = new JMenuItem("Build Road");
    JMenu development = new JMenu("Develop");
    JMenuItem buyCard = new JMenuItem("Buy Development Card");
    JMenuItem playCard = new JMenuItem("Play Development Card");
    JMenu options = new JMenu("Options");
    JMenuItem exchange = new JMenuItem("Trade Resources");
    JMenuItem buildingCard = new JMenuItem("Building Helper Card");
    JMenuItem rollDice = new JMenuItem("Roll Dice");
    JMenuItem endTurn = new JMenuItem("End Turn");

    //Development Card Prep
    String[] devCardTypes = {"Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Year of Plenty","Year of Plenty","Victory Points","Victory Points","Victory Points","Victory Points","Victory Points","Monopoly","Monopoly","Road Builder","Road Builder"};

    //Building Costs Frame
    JFrame costFrame = new JFrame();
    JLabel imageCostLabel = new JLabel("",0);

    public PlayerView(Player player, CatanBoard reference) {
        //Relating global variables to class variables
        this.player = player;
        this.reference=reference;

        //Menubar creation
        this.setJMenuBar(mb);
        mb.add(build);
        build.add(road);
        road.addActionListener(this);
        build.add(settlement);
        settlement.addActionListener(this);
        build.add(city);
        city.addActionListener(this);
        mb.add(development);
        development.add(buyCard);
        buyCard.addActionListener(this);
        development.add(playCard);
        playCard.addActionListener(this);
        mb.add(options);
        options.add(exchange);
        exchange.addActionListener(this);
        options.add(rollDice);
        rollDice.addActionListener(this);
        options.add(buildingCard);
        buildingCard.addActionListener(this);
        options.add(endTurn);
        endTurn.addActionListener(this);

        //Creating the GUI
        this.setLayout(new BorderLayout());
        this.add(graphicHolder, BorderLayout.CENTER);
        this.add(borderNorth,BorderLayout.NORTH);
        borderNorth.setBorder(compound);
        borderNorth.add(infoPanel,BorderLayout.CENTER);
        borderNorth.add(colorDisplayLabel,BorderLayout.WEST);
        this.add(borderSouth,BorderLayout.SOUTH);
        borderSouth.add(vpPointHolder,BorderLayout.WEST);
        borderSouth.add(turnBorderPanel, BorderLayout.EAST);
        turnBorderPanel.setBorder(compound);
            turnBorderPanel.add(turnHolder);
            turnHolder.add(new JLabel("  "),BorderLayout.WEST);
        turnHolder.add(turnBox,BorderLayout.CENTER);
        turnHolder.add(new JLabel("  "),BorderLayout.EAST);
            turnHolder.setBorder(new TitledBorder("Turn"));
        vpPointHolder.setBorder(compound);
        vpPointHolder.add(victoryPointLabel);
        borderSouth.add(borderInfoPanel,BorderLayout.CENTER);
        turnBox.setEnabled(false);
        turnBox.setToolTipText("This box is checked if it's your turn");
        borderInfoPanel.setBorder(compound);
        borderInfoPanel.add(awardPanel);
        victoryPointLabel.setBorder(new TitledBorder("VPs"));
        borderNorth.setBackground(Color.white);
        colorDisplayLabel.setBorder(compound);
        infoPanel.add(devPanel);
        infoPanel.setBorder(compound);
        devPanel.setBorder(new TitledBorder("Development Cards"));
        devPanel.add(unplayed);
        unplayed.setBorder(compound);
        devPanel.add(played);
        played.setBorder(compound);
        unplayed.addItem("Hidden Cards");
        played.addItem("Revealed Cards");
        victoryPointLabel.setFont(new Font(victoryPointLabel.getFont().getName(),Font.PLAIN,16));

        awardPanel.setBorder(new TitledBorder("Awards"));
        awardPanel.add(longestRoadBox);
        longestRoadBox.setBorderPainted(true);
        longestRoadBox.setEnabled(false);
        awardPanel.add(largestArmyBox);
        largestArmyBox.setBorderPainted(true);
        largestArmyBox.setEnabled(false);
        longestRoadBox.setToolTipText("Awarded to the player who has the longest continuous road that exceeds 4 separate segments");
        largestArmyBox.setToolTipText("Awarded to the player who has played the most 'Knight' DCs that exceeds 2 separate cards");
        victoryPointLabel.setToolTipText("Your victory point total; get to 10 and you win");

        //Code for implementation
        colorDisplayLabel.setIcon(new ImageIcon("Pieces/Large_Icons/"+player.getColor()+"_City_Large.png"));

        //Code for testing
        //colorDisplayLabel.setIcon(new ImageIcon("Pieces/Large_Icons/Blue_City_Large.png"));

        unplayed.setEnabled(player.isTurn());

        //Array manipulation stuff
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
        update();
        initializeCostFrame();
    }

    public void update(){
        brickNum.setText(""+player.getBrickNum());
        oreNum.setText(""+player.getOreNum());
        sheepNum.setText(""+player.getWoolNum());
        wheatNum.setText(""+player.getGrainNum());
        woodNum.setText(""+player.getLumberNum());
        largestArmyBox.setSelected(player.hasLargestArmy());
        longestRoadBox.setSelected(player.hasLongestRoad());
        victoryPointLabel.setText("   "+player.getVictoryPointTotal()+"   ");

        unplayed.setEnabled(player.isTurn());
        build.setEnabled(player.isTurn());
        development.setEnabled(player.isTurn());
        longestRoadBox.setSelected(player.hasLongestRoad());
        largestArmyBox.setSelected(player.hasLargestArmy());
        options.setEnabled(player.isTurn());
        turnBox.setSelected(player.isTurn());

        //Workaround for revalidate() / invalidate() validate()
        this.setSize(this.getWidth()+1,this.getHeight());
        this.setSize(this.getWidth()-1,this.getHeight());
    }

    public void initializeCostFrame(){
        costFrame.add(imageCostLabel);
        imageCostLabel.setIcon(new ImageIcon("Resources/Building_Costs.png"));

        //costFrame bounds need to be adjusted
        costFrame.setBounds((int)this.getBounds().getX(),(int)this.getBounds().getY(),291,357);
        costFrame.setUndecorated(true);
    }

    public ArrayList<Player> getOtherPlayers(){
        ArrayList<Player> others = new ArrayList<Player>();
        for(int x=0; x<reference.catanPlayerList.size(); x++)
            if(reference.catanPlayerList.get(x)!=player)
                others.add(reference.catanPlayerList.get(x));

        return others;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==settlement){
            int settlementInput = JOptionPane.showConfirmDialog(this,"Would you like to create a new settlement?","Settlement Creation",JOptionPane.YES_NO_OPTION);
            if(settlementInput==0){
                if(player.getBrickNum()>=1 && player.getLumberNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1){
                    reference.isSettlementBuilding=true;
                    this.options.setEnabled(false);
                    player.brickNum-=1;
                    player.lumberNum-=1;
                    player.woolNum-=1;
                    player.grainNum-=1;
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a new settlement.","Settlement Building Error",3);
            }
        }

        else if(e.getSource()==city){}

        else if(e.getSource()==road){
            int roadInput = JOptionPane.showConfirmDialog(this,"Would you like to create a road?","Road Creation",JOptionPane.YES_NO_OPTION);
            if(roadInput==0){
                if(player.getBrickNum()>=1 && player.getLumberNum()>=1){
                    reference.isRoadBuilding=true;
                    this.options.setEnabled(false);
                    player.brickNum-=1;
                    player.lumberNum-=1;
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a road.","Road Building Error",3);
            }
        }

        else if(e.getSource()==buyCard){
            DevelopmentCard newDc = new DevelopmentCard(devCardTypes[new Random().nextInt(25)],player,getOtherPlayers(),reference);
            int devInput = JOptionPane.showConfirmDialog(this,"Would you like to draw a development card?","Development Card Draw",JOptionPane.YES_NO_OPTION);
            if(devInput==0){
                if(player.getOreNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1){
                    JOptionPane.showMessageDialog(this,"You have purchased a development card.","Development Card",1);
                    player.addDevelopmentCardToUnplayed(newDc);
                    unplayed.addItem(newDc.getType());
                    player.woolNum-=1;
                    player.oreNum-=1;
                    player.grainNum-=1;
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to purchase a development card.","Cannot Purchase Development Card",3);
            }
        }

        else if(e.getSource()==playCard){}
        else if(e.getSource()==exchange){}
        else if(e.getSource()==endTurn){}
        else if(e.getSource()==rollDice){}

        else if(e.getSource()==buildingCard){
            costFrame.setVisible(!costFrame.isVisible());
        }

        update();
    }
}
