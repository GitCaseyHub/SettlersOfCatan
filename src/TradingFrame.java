import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class TradingFrame extends JFrame implements ActionListener {
    //Year of Plenty & Monopoly
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    JPanel southPanel = new JPanel(new GridLayout(1,2));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JComboBox<Integer> brickCheck = new JComboBox<>();
    JComboBox<Integer> oreCheck = new JComboBox<>();
    JComboBox<Integer> wheatCheck = new JComboBox<>();
    JComboBox<Integer> sheepCheck = new JComboBox<>();
    JComboBox<Integer> woodCheck = new JComboBox<>();
    JButton confirmButton = new JButton("Confirm Trade");
    JButton askButton = new JButton("Ask Another Player");
    ArrayList<String> rejections = new ArrayList<>();

    //Constructor variables
    Player player;
    boolean asker;
    CatanBoard cbRef;

    public TradingFrame(Player player, boolean asker){
        this.setResizable(false);
        this.asker=asker;
        this.player=player;
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
        brickCheck.addActionListener(this);

        graphicPanels[1].add(oreCheck, BorderLayout.SOUTH);
        oreCheck.setBorder(compound);
        oreCheck.addActionListener(this);

        graphicPanels[2].add(sheepCheck, BorderLayout.SOUTH);
        sheepCheck.setBorder(compound);
        sheepCheck.addActionListener(this);

        graphicPanels[3].add(wheatCheck, BorderLayout.SOUTH);
        wheatCheck.setBorder(compound);
        wheatCheck.addActionListener(this);

        graphicPanels[4].add(woodCheck, BorderLayout.SOUTH);
        woodCheck.setBorder(compound);
        woodCheck.addActionListener(this);
        this.add(graphicHolder,BorderLayout.CENTER);
        this.add(southPanel, BorderLayout.SOUTH);
        southPanel.add(askButton);
            askButton.setBorder(compound);
            askButton.addActionListener(this);
            confirmButton.setBorder(compound);
        southPanel.add(confirmButton);
            confirmButton.addActionListener(this);

        if(asker)
            confirmButton.setEnabled(false);

        if(!asker)
            askButton.setEnabled(false);

        this.add(graphicHolder, BorderLayout.CENTER);
        this.setTitle("Player "+player.getName()+"'s Trading Frame");
    }
    
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==askButton){
            try {
                String playerName = (String) JOptionPane.showInputDialog(this, "Which player would you like to trade with?", "Trading Player", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                if(!cbRef.playerExists(playerName)){
                    JOptionPane.showMessageDialog(this,"That is not a valid entry. Please trade appropriately.","Invalid Name",1, new ImageIcon("Resources/Catan_Icon.png"));
                    return;
                }

                if (cbRef.playerExists(playerName) && !playerName.equals(player.getName())) {
                    if (cbRef.getPlayerViaName(playerName).getClassTitle().equals("Assassin")) {
                        JOptionPane.showMessageDialog(this, "You cannot trade with assassins.", "Improper Trade Request", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    if(cbRef.getPlayerViaName(playerName).isInDebt()) {
                        JOptionPane.showMessageDialog(this, "You cannot trade with players who are in debt.", "Debtor Trade Denied", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }

                    if(cbRef.getPlayerViaName(playerName).returnTotalResources()==0){
                        JOptionPane.showMessageDialog(this,playerName+" has no resources. You cannot trade with them.","Player with No Resources",1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }

                    if (rejections.contains(playerName)) {
                        JOptionPane.showMessageDialog(this, "That player has already rejected your trade request.", "Previous Rejection", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }

                    String accept = "";
                    while (!accept.equalsIgnoreCase("Yes") && !accept.equalsIgnoreCase("No"))
                        accept = (String) JOptionPane.showInputDialog(this, playerName + ", would you like to trade with " + this.player.getName() + "? YES/NO?", "Trade Request", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null);

                    if (accept.equalsIgnoreCase("Yes")) {
                        cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.asker = false;
                        cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.updateComboBoxes();
                        cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setBounds((int) this.getBounds().getX() + 500, (int) this.getBounds().getY(), 475, 235);
                        cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setVisible(true);
                        cbRef.firstFrame = this;
                        cbRef.secondFrame = cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf;
                        askButton.setEnabled(false);
                    }
                    else {
                        JOptionPane.showMessageDialog(this, "Your trade request has been denied.", "Trade Failed", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        rejections.add(playerName);
                    }
                    return;
                }
                if (playerName.equals(player.getName())) {
                    JOptionPane.showMessageDialog(this, "You cannot trade with yourself.", "Trade Loop", 1, new ImageIcon("Resources/Catan_Icon.png"));
                }
            }
            catch(IndexOutOfBoundsException f){
                JOptionPane.showMessageDialog(this,"The trade request has been cancelled.","Trade Cancelled",1, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }
        else if(e.getSource()==confirmButton){
            JOptionPane.showMessageDialog(this,"The trade has been completed.","Complete Trade", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            TradingFrame firstFrame = cbRef.firstFrame;
            TradingFrame secondFrame = cbRef.secondFrame;

            //Changing resource totals
            firstFrame.player.monoBrick(Math.max((int)secondFrame.brickCheck.getSelectedItem(),0)- (Math.max((int) firstFrame.brickCheck.getSelectedItem(), 0)));
            firstFrame.player.monoLumber(Math.max((int)secondFrame.woodCheck.getSelectedItem(),0)- (Math.max((int) firstFrame.woodCheck.getSelectedItem(), 0)));
            firstFrame.player.monoOre(Math.max((int)secondFrame.oreCheck.getSelectedItem(),0)- (Math.max((int) firstFrame.oreCheck.getSelectedItem(), 0)));
            firstFrame.player.monoWheat(Math.max((int)secondFrame.wheatCheck.getSelectedItem(),0)- (Math.max((int) firstFrame.wheatCheck.getSelectedItem(), 0)));
            firstFrame.player.monoWool(Math.max((int)secondFrame.sheepCheck.getSelectedItem(),0)- (Math.max((int) firstFrame.sheepCheck.getSelectedItem(), 0)));

            secondFrame.player.monoBrick(Math.max((int)firstFrame.brickCheck.getSelectedItem(),0)- (Math.max((int) secondFrame.brickCheck.getSelectedItem(), 0)));
            secondFrame.player.monoLumber(Math.max((int)firstFrame.woodCheck.getSelectedItem(),0)- (Math.max((int) secondFrame.woodCheck.getSelectedItem(), 0)));
            secondFrame.player.monoOre(Math.max((int)firstFrame.oreCheck.getSelectedItem(),0)- (Math.max((int) secondFrame.oreCheck.getSelectedItem(), 0)));
            secondFrame.player.monoWheat(Math.max((int)firstFrame.wheatCheck.getSelectedItem(),0)- (Math.max((int) secondFrame.wheatCheck.getSelectedItem(), 0)));
            secondFrame.player.monoWool(Math.max((int)firstFrame.sheepCheck.getSelectedItem(),0)- (Math.max((int) secondFrame.sheepCheck.getSelectedItem(), 0)));

            Arrays.stream(new TradingFrame[]{firstFrame,secondFrame}).forEach(frame -> frame.setVisible(false));
            cbRef.updateAllStatusMenus();
        }
    }

    public void updateComboBoxes(){
        this.asker=player.isTurn();
        this.setBounds(100, 100, 475, 235);
        confirmButton.setEnabled(true);
        askButton.setEnabled(true);
        Arrays.stream(new JComboBox[]{brickCheck,wheatCheck,oreCheck,woodCheck,sheepCheck}).forEach(JComboBox::removeAllItems);

        if(player.getBrickNum()<=0) {
            brickCheck.addItem(player.getBrickNum());
            brickCheck.setEnabled(false);
        }
        if(player.getGrainNum()<=0) {
            wheatCheck.addItem(player.getGrainNum());
            wheatCheck.setEnabled(false);
        }
        if(player.getOreNum()<=0) {
            oreCheck.addItem(player.getOreNum());
            oreCheck.setEnabled(false);
        }
        if(player.getLumberNum()<=0) {
            woodCheck.addItem(player.getLumberNum());
            woodCheck.setEnabled(false);
        }
        if(player.getWoolNum()<=0) {
            sheepCheck.addItem(player.getWoolNum());
            sheepCheck.setEnabled(false);
        }

        for(int x=0; x<player.getBrickNum()+1; x++)
            brickCheck.addItem(x);

        for(int x=0; x<player.getGrainNum()+1; x++)
            wheatCheck.addItem(x);

        for(int x=0; x<player.getOreNum()+1; x++)
            oreCheck.addItem(x);

        for(int x=0; x<player.getLumberNum()+1; x++)
            woodCheck.addItem(x);

        for(int x=0; x<player.getWoolNum()+1; x++)
            sheepCheck.addItem(x);

        confirmButton.setEnabled(!asker);
        askButton.setEnabled(asker);
    }
}
