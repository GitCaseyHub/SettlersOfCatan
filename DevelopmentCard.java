import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class DevelopmentCard implements ActionListener {
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

    public boolean isBoughtThisTurn() {
        return boughtThisTurn;
    }

    public void setBoughtThisTurn(boolean boughtThisTurn) {
        this.boughtThisTurn = boughtThisTurn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CatanBoard getCbReference() {
        return cbReference;
    }

    public void setCbReference(CatanBoard cbReference) {
        this.cbReference = cbReference;
    }

    public ArrayList<Player> getOtherPlayers() {
        return otherPlayers;
    }

    public DevelopmentCard(String type, Player player, ArrayList<Player> otherPlayers, CatanBoard cbReference, boolean boughtThisTurn) {
        //Types of Cards: Knight, Victory Points, Road Builder, Year of Plenty, Monopoly
        this.type = type;
        this.player = player;
        this.cbReference = cbReference;
        this.otherPlayers = otherPlayers;
        this.boughtThisTurn=boughtThisTurn;
        readyYearOfPlentyFrame();
    }

    public DevelopmentCard(){}

    public void playCard() {
        if (type.equals("Knight"))
            performKnightAction();

        else if (type.equals("Victory Points")) {
            JOptionPane.showMessageDialog(null, "You are awarded one victory point. This will now be reflected in your status screen.", "Victory Point Action", 1);
            performVictoryPoints();
        }

        else if (type.equals("Road Builder")) {
            JOptionPane.showMessageDialog(null,"You will now select two sets of indices to build roads on.","Road Building Action",1);
            performRoadBuilding();
        }

        else if (type.equals("Year of Plenty")) {
            JOptionPane.showMessageDialog(null, "Select two of the following five resources. You will be given one of each. These resources will still be amplified by your class.", "Year of Plenty Action", 1);
            performYearOfPlenty();
        }

        else {
            JOptionPane.showMessageDialog(null,"Select one of the following five resources. You will be given all of that resource from each player. Your class will not affect the amount given.","Monopoly Action",1);
            performMonopoly();
        }
    }

    public void performKnightAction() {
        JOptionPane.showMessageDialog(null,"Move the robber to a tile. Note that you may keep the robber in the same position by click on the tile it is already located on.","Knight Card Action",1);
        cbReference.isMovingRobber=true;
        cbReference.isPlayerActing=true;
    }

    public void performVictoryPoints() {
        player.changeVictoryPoints(1);
    }

    public void performRoadBuilding() {
        cbReference.roadDevCard=true;
        cbReference.finishedRoadCard=false;
        cbReference.isRoadBuilding=true;
        cbReference.isPlayerActing=true;
    }

    public void performYearOfPlenty() {
        counter = 0;
        yearOfPlenty=true;
        choiceFrame.setTitle("Year of Plenty Resource Choices");
        choiceFrame.setVisible(true);
    }

    public void performMonopoly() {
        counter=0;
        monopoly=true;
        choiceFrame.setTitle("Monopoly Resource Choice");
        choiceFrame.setVisible(true);
    }

    public void readyYearOfPlentyFrame() {
        //Array manipulation stuff
        for (int x = 0; x < 5; x++) {
            graphicPanels[x] = new JPanel(new BorderLayout());
            graphicImageLabels[x] = new JLabel("", 0);
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
            int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you would like these two resources?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == 0) {
                JOptionPane.showMessageDialog(null, "Okay. You will be given your chosen resources.", "Resources Chosen", 1);
                player.changeBrick((brickCheck.isSelected() ? 1 : 0));
                player.changeWool((sheepCheck.isSelected() ? 1 : 0));
                player.changeGrain((wheatCheck.isSelected() ? 1 : 0));
                player.changeLumber((woodCheck.isSelected() ? 1 : 0));
                player.changeOre((oreCheck.isSelected() ? 1 : 0));
                cbReference.getPlayerStatusMenu(player).update();
                choiceFrame.setVisible(false);
                deselectAll();
                cbReference.getPlayerStatusMenu(player).options.setEnabled(true);
                cbReference.getPlayerStatusMenu(player).build.setEnabled(true);
                cbReference.getPlayerStatusMenu(player).development.setEnabled(true);
                yearOfPlenty=false;
            } else
                JOptionPane.showMessageDialog(null, "Okay. Reselect the resources you want.", "Cancellation", 1);
        }

        if(counter==1 && monopoly){
            int confirm = JOptionPane.showConfirmDialog(null,"Are you sure this is the resource you'd like to steal from other players?","Confirmation",JOptionPane.YES_NO_OPTION);
            if(confirm==0) {
                JOptionPane.showMessageDialog(null, "Okay. You will be given all resources of that type from other players.", "Monopoly Action", 1);
                for (int x = 0; x < otherPlayers.size(); x++) {
                    if (brickCheck.isSelected()) {
                        player.monoBrick(otherPlayers.get(x).getBrickNum());
                        otherPlayers.get(x).setBrickNum(0);
                    }
                    else if (sheepCheck.isSelected()) {
                        player.monoWool(otherPlayers.get(x).getWoolNum());
                        otherPlayers.get(x).setWoolNum(0);
                    }
                    else if (oreCheck.isSelected()) {
                        player.monoOre(otherPlayers.get(x).getOreNum());
                        otherPlayers.get(x).setOreNum(0);
                    }
                    else if (woodCheck.isSelected()) {
                        player.monoLumber(otherPlayers.get(x).getLumberNum());
                        otherPlayers.get(x).setLumberNum(0);
                    }
                    else if (wheatCheck.isSelected()) {
                        player.monoWheat(otherPlayers.get(x).getGrainNum());
                        otherPlayers.get(x).setGrainNum(0);
                    }
                    cbReference.getPlayerStatusMenu(player).update();
                    cbReference.getPlayerStatusMenu(otherPlayers.get(x)).update();
                }
                choiceFrame.setVisible(false);
                deselectAll();
                cbReference.getPlayerStatusMenu(player).options.setEnabled(true);
                cbReference.getPlayerStatusMenu(player).build.setEnabled(true);
                cbReference.getPlayerStatusMenu(player).development.setEnabled(true);
                monopoly = false;
            }
            else
                JOptionPane.showMessageDialog(null, "Okay. Reselect the resource you want.", "Cancellation", 1);
        }
    }

    public void disableAppropriateCheckBoxes() {
        oreCheck.setEnabled(oreCheck.isSelected());
        sheepCheck.setEnabled(sheepCheck.isSelected());
        brickCheck.setEnabled(brickCheck.isSelected());
        woodCheck.setEnabled(woodCheck.isSelected());
        wheatCheck.setEnabled(wheatCheck.isSelected());
    }

    public void changeAllCheckBoxes(boolean state) {
        oreCheck.setEnabled(state);
        sheepCheck.setEnabled(state);
        brickCheck.setEnabled(state);
        woodCheck.setEnabled(state);
        wheatCheck.setEnabled(state);
    }

    public void deselectAll() {
        oreCheck.setSelected(false);
        sheepCheck.setSelected(false);
        brickCheck.setSelected(false);
        wheatCheck.setSelected(false);
        woodCheck.setSelected(false);
    }
}
