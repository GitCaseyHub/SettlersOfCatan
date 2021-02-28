import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DevelopmentCard implements ActionListener, MouseListener {
    //Different class references
    String type = "";
    Player player;
    CatanBoard cbReference;
    ArrayList<Player> otherPlayers;

    //Year of Plenty & Monopoly
    JFrame choiceFrame = new JFrame();
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JCheckBox brickCheck = new JCheckBox();
    JCheckBox oreCheck = new JCheckBox();
    JCheckBox wheatCheck = new JCheckBox();
    JCheckBox sheepCheck = new JCheckBox();
    JCheckBox woodCheck = new JCheckBox();
    int counter = 0;
    boolean yearOfPlenty=false;
    boolean monopoly=false;
    boolean boughtThisTurn;

    //Card Frame
    JFrame cardFrame = new JFrame();
    JLabel cardLabel = new JLabel("",SwingConstants.CENTER);

    public boolean isBoughtThisTurn() {
        return boughtThisTurn;
    }

    public void setBoughtThisTurn(boolean boughtThisTurn) {
        this.boughtThisTurn = boughtThisTurn;
    }

    public String getType() {
        return type;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setOtherPlayers(ArrayList<Player> otherPlayers) {
        this.otherPlayers = otherPlayers;
    }

    public DevelopmentCard(String type, Player player, ArrayList<Player> otherPlayers, CatanBoard cbReference, boolean boughtThisTurn) {
        //Types of Cards: Knight, Victory Points, Road Builder, Year of Plenty, Monopoly
        this.type = type;
        this.player = player;
        this.cbReference = cbReference;
        this.otherPlayers = otherPlayers;
        this.boughtThisTurn=boughtThisTurn;
        readyYearOfPlentyFrame();
        initializeCardFrame();
    }

    public DevelopmentCard(){}

    public void initializeCardFrame(){
        cardFrame.setBounds(422,392,215,325);
        cardFrame.add(cardLabel);
        cardLabel.addMouseListener(this);
        cardLabel.setBorder(compound);
    }

    public void playCard() {
        switch (this.type) {
            case "Knight":
                performKnightAction();
                break;
            case "Victory Points":
                JOptionPane.showMessageDialog(null, "You are awarded one victory point. This will now be reflected in your status screen.", "Victory Point Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                performVictoryPoints();
                break;
            case "Road Builder":
                JOptionPane.showMessageDialog(null, "You will now select two sets of indices to build roads on.", "Road Building Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                performRoadBuilding();
                break;
            case "Year of Plenty":
                JOptionPane.showMessageDialog(null, "Select two of the following five resources. You will be given one of each. These resources will still be amplified by your class.", "Year of Plenty Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                performYearOfPlenty();
                break;
            case "Monopoly":
                JOptionPane.showMessageDialog(null, "Select one of the following five resources. You will be given all of that resource from each player. Your class will not affect the amount given.", "Monopoly Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                performMonopoly();
                break;
        }
    }

    public void performKnightAction() {
        showDevelopmentImage("Resources/Preview_Images/Knight.png");
        JOptionPane.showMessageDialog(null,"Move the robber to a tile. Note that you may keep the robber in the same position by click on the tile it is already located on.","Knight Card Action",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        cbReference.isMovingRobber=true;
        cbReference.isPlayerActing=true;
    }

    public void performVictoryPoints() {
        switch(new Random().nextInt(3)){
            case 0:
                showDevelopmentImage("Resources/Preview_Images/University.png");
                break;

            case 1:
                showDevelopmentImage("Resources/Preview_Images/Chapel.png");
                break;

            case 2:
                showDevelopmentImage("Resources/Preview_Images/Governors_House.png");
                break;
        }
        player.changeVictoryPoints(1);
    }

    public void performRoadBuilding() {
        if(player.getRoads()==0){
            JOptionPane.showMessageDialog(cbReference.getPlayerStatusMenu(player), "You have no roads left to build with. You cannot play this card.", "Insufficient Roads", 1, new ImageIcon("Resources/Catan_Icon.png"));
            return;
        }

        if (player.getRoads() ==1)
            JOptionPane.showMessageDialog(cbReference.getPlayerStatusMenu(player), "You only have one road left to build with, so this development card will only create a single road.", "Single Road Building", 1, new ImageIcon("Resources/Catan_Icon.png"));

        showDevelopmentImage("Resources/Preview_Images/Road_Building.png");
        Arrays.stream(new Boolean[]{cbReference.roadDevCard,cbReference.isRoadBuilding,cbReference.isPlayerActing}).forEach(bool -> bool=true);
        cbReference.finishedRoadCard=false;
    }

    public void performYearOfPlenty() {
        showDevelopmentImage("Resources/Preview_Images/Year_Of_Plenty.png");
        counter = 0;
        yearOfPlenty=true;
        choiceFrame.setTitle("Year of Plenty Resource Choices");
        choiceFrame.setVisible(true);
    }

    public void performMonopoly() {
        showDevelopmentImage("Resources/Preview_Images/Monopoly.png");
        counter=0;
        monopoly=true;
        choiceFrame.setTitle("Monopoly Resource Choice");
        choiceFrame.setVisible(true);
    }

    public void readyYearOfPlentyFrame() {
        //Array manipulation stuff
        for (int x = 0; x < 5; x++) {
            graphicPanels[x] = new JPanel(new BorderLayout());
            graphicImageLabels[x] = new JLabel("", SwingConstants.CENTER);
            graphicImageLabels[x].setIcon(new ImageIcon("Resources/" + graphicStrings[x] + "_Image.png"));
            graphicPanels[x].add(graphicImageLabels[x], BorderLayout.CENTER);
            graphicImageLabels[x].setBorder(compound);
            graphicHolder.add(graphicPanels[x]);
        }

        //Adding Checkboxes
        graphicPanels[0].add(brickCheck, BorderLayout.SOUTH);
        brickCheck.setBorder(compound);
        brickCheck.setHorizontalAlignment(SwingConstants.CENTER);
        brickCheck.addActionListener(this);
        brickCheck.setBorderPainted(true);

        graphicPanels[1].add(oreCheck, BorderLayout.SOUTH);
        oreCheck.setBorder(compound);
        oreCheck.setHorizontalAlignment(SwingConstants.CENTER);
        oreCheck.addActionListener(this);
        oreCheck.setBorderPainted(true);

        graphicPanels[3].add(wheatCheck, BorderLayout.SOUTH);
        wheatCheck.setBorder(compound);
        wheatCheck.setHorizontalAlignment(SwingConstants.CENTER);
        wheatCheck.addActionListener(this);
        wheatCheck.setBorderPainted(true);

        graphicPanels[2].add(sheepCheck, BorderLayout.SOUTH);
        sheepCheck.setBorder(compound);
        sheepCheck.setHorizontalAlignment(SwingConstants.CENTER);
        sheepCheck.addActionListener(this);
        sheepCheck.setBorderPainted(true);

        graphicPanels[4].add(woodCheck, BorderLayout.SOUTH);
        woodCheck.setBorder(compound);
        woodCheck.setHorizontalAlignment(SwingConstants.CENTER);
        woodCheck.addActionListener(this);
        woodCheck.setBorderPainted(true);

        choiceFrame.add(graphicHolder);
        choiceFrame.setBounds(100, 100, 475, 200);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == oreCheck) {
            counter += (oreCheck.isSelected()) ? 1 : -1;
            if ((counter == 2 && yearOfPlenty) || (counter==1 && monopoly))
                disableAppropriateCheckBoxes();

            else
                changeAllCheckBoxes(true);
        }
        else if (e.getSource() == sheepCheck) {
            counter += (sheepCheck.isSelected()) ? 1 : -1;
            if ((counter == 2 && yearOfPlenty) || (counter==1 && monopoly))
                disableAppropriateCheckBoxes();

            else
                changeAllCheckBoxes(true);
        }
        else if (e.getSource() == brickCheck) {
            counter += (brickCheck.isSelected()) ? 1 : -1;
            if ((counter == 2 && yearOfPlenty) || (counter==1 && monopoly))
                disableAppropriateCheckBoxes();

            else
                changeAllCheckBoxes(true);
        }
        else if (e.getSource() == woodCheck) {
            counter += (woodCheck.isSelected()) ? 1 : -1;
            if ((counter == 2 && yearOfPlenty) || (counter==1 && monopoly))
                disableAppropriateCheckBoxes();

            else
                changeAllCheckBoxes(true);
        }
        else if (e.getSource() == wheatCheck) {
            counter += (wheatCheck.isSelected()) ? 1 : -1;
            if ((counter == 2 && yearOfPlenty) || (counter==1 && monopoly))
                disableAppropriateCheckBoxes();

            else
                changeAllCheckBoxes(true);
        }

        if (counter == 2 && yearOfPlenty) {
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you would like these two resources?", "Confirmation", JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if (confirmation == 0) {
                JOptionPane.showMessageDialog(null, "Okay. You will be given your chosen resources.", "Resources Chosen", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                player.changeAll((woodCheck.isSelected() ? 1 : 0),(sheepCheck.isSelected() ? 1 : 0),(brickCheck.isSelected() ? 1 : 0),(oreCheck.isSelected() ? 1 : 0),(wheatCheck.isSelected() ? 1 : 0));
                cbReference.getPlayerStatusMenu(player).update();
                choiceFrame.setVisible(false);
                deselectAll();
                cbReference.getPlayerStatusMenu(player).resetReference(true);
                yearOfPlenty=false;

            } else
                JOptionPane.showMessageDialog(null, "Okay. Reselect the resources you want.", "Cancellation",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        }

        if(counter==1 && monopoly){
            if(cbReference.highwaymanIsPresent())
                otherPlayers.removeIf(player -> player.getClassTitle().equals("Highwayman"));

            int confirm = JOptionPane.showConfirmDialog(null,"Are you sure this is the resource you'd like to steal from other players?","Confirmation",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(confirm==0) {
                JOptionPane.showMessageDialog(null, "Okay. You will be given all resources of that type from other players.", "Monopoly Action",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                for (Player otherPlayer : otherPlayers) {
                    if (brickCheck.isSelected()) {
                        player.monoBrick(otherPlayer.getBrickNum());
                        otherPlayer.setBrickNum(0);
                    } else if (sheepCheck.isSelected()) {
                        player.monoWool(otherPlayer.getWoolNum());
                        otherPlayer.setWoolNum(0);
                    } else if (oreCheck.isSelected()) {
                        player.monoOre(otherPlayer.getOreNum());
                        otherPlayer.setOreNum(0);
                    } else if (woodCheck.isSelected()) {
                        player.monoLumber(otherPlayer.getLumberNum());
                        otherPlayer.setLumberNum(0);
                    } else if (wheatCheck.isSelected()) {
                        player.monoWheat(otherPlayer.getGrainNum());
                        otherPlayer.setGrainNum(0);
                    }
                    cbReference.updateAllStatusMenus();
                }
                choiceFrame.setVisible(false);
                deselectAll();
                cbReference.getPlayerStatusMenu(player).resetReference(true);
                monopoly = false;
            }
            else
                JOptionPane.showMessageDialog(null, "Reselect the resource you want.", "Cancellation",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        }
    }

    public void disableAppropriateCheckBoxes() {
        Arrays.stream(new JCheckBox[]{oreCheck,sheepCheck,brickCheck,woodCheck,wheatCheck}).forEach(box -> box.setEnabled(box.isSelected()));
    }

    public void changeAllCheckBoxes(boolean state) {
        Arrays.stream(new JCheckBox[]{oreCheck,sheepCheck,brickCheck,woodCheck,wheatCheck}).forEach(box -> box.setEnabled(state));
    }

    public void deselectAll() {
        Arrays.stream(new JCheckBox[]{oreCheck,sheepCheck,brickCheck,woodCheck,wheatCheck}).forEach(box -> box.setSelected(false));
    }

    public void showDevelopmentImage(String name){
        cardLabel.setIcon(new ImageIcon(name));
        cardFrame.setVisible(true);
    }

    public void mousePressed(MouseEvent e) {
        if(e.getSource()==cardLabel)
            cardFrame.setVisible(false);
    }

    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
