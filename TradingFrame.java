import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TradingFrame extends JFrame implements ActionListener {
    //Year of Plenty & Monopoly
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    JPanel southPanel = new JPanel(new GridLayout(1,2));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JComboBox<Integer> brickCheck = new JComboBox<Integer>();
    JComboBox<Integer> oreCheck = new JComboBox<Integer>();
    JComboBox<Integer> wheatCheck = new JComboBox<Integer>();
    JComboBox<Integer> sheepCheck = new JComboBox<Integer>();
    JComboBox<Integer> woodCheck = new JComboBox<Integer>();
    JButton confirmButton = new JButton("Confirm Trade");
    JButton askButton = new JButton("Ask Another Player");
    ArrayList<String> rejections = new ArrayList<String>();

    //Constructor variables
    Player player;
    boolean asker;
    CatanBoard cbRef;

    public TradingFrame(Player player, boolean asker){
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

        graphicPanels[3].add(wheatCheck, BorderLayout.SOUTH);
        wheatCheck.setBorder(compound);
        wheatCheck.addActionListener(this);

        graphicPanels[2].add(sheepCheck, BorderLayout.SOUTH);
        sheepCheck.setBorder(compound);
        sheepCheck.addActionListener(this);

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
            askButton.setEnabled(false);
            String playerName = (String)JOptionPane.showInputDialog(this,"Which player would you like to trade with?","Trading Player",1,new ImageIcon("Resources/Catan_Icon.png"),null,null);
            int playerCounter=0;
            for(int x=0; x<cbRef.catanPlayerList.size(); x++)
                if(cbRef.catanPlayerList.get(x).getName().equals(playerName))
                    playerCounter++;

            if(playerCounter==0){
                askButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,"There is no player with "+playerName+" as a name. Please type their name correctly.","No Such Player Exists",3, new ImageIcon("Resources/Catan_Icon.png"));
            }

            else if(playerCounter==1 && !rejections.contains(playerName)){
                String accept="";
                while (accept.equals(""))
                    accept = (String)JOptionPane.showInputDialog(this,playerName+", would you like to trade with "+this.player.getName()+"?","Trade Request",1,new ImageIcon("Resources/Catan_Icon.png"),null,null);

                if(accept.equalsIgnoreCase("Yes")) {
                    cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.asker = false;
                    cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.updateComboBoxes();
                    cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setBounds((int) this.getBounds().getX() + 500, (int) this.getBounds().getY(), 475, 235);
                    cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setVisible(true);
                    cbRef.firstFrame = this;
                    cbRef.secondFrame = cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf;
                }
                else {
                    JOptionPane.showMessageDialog(this, "Your trade request has been denied.", "Trade Failed", 3, new ImageIcon("Resources/Catan_Icon.png"));
                    rejections.add(playerName);
                    askButton.setEnabled(true);
                }
            }
            else{
                askButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,"This player has already rejected your trade request this turn. Try again on your next turn.","Already Rejected",3, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }
        else if(e.getSource()==confirmButton){
            JOptionPane.showMessageDialog(this,"The trade has been completed.","Complete Trade",1, new ImageIcon("Resources/Catan_Icon.png"));
            TradingFrame firstFrame = cbRef.firstFrame;
            TradingFrame secondFrame = cbRef.secondFrame;
            
            //Changing resource totals
            firstFrame.player.monoBrick((int)secondFrame.brickCheck.getSelectedItem()- (int) firstFrame.brickCheck.getSelectedItem());
            firstFrame.player.monoLumber((int)secondFrame.woodCheck.getSelectedItem()-(int)firstFrame.woodCheck.getSelectedItem());
            firstFrame.player.monoOre((int)secondFrame.oreCheck.getSelectedItem()-(int)firstFrame.oreCheck.getSelectedItem());
            firstFrame.player.monoWheat((int)secondFrame.wheatCheck.getSelectedItem()-(int)firstFrame.wheatCheck.getSelectedItem());
            firstFrame.player.monoWool((int)secondFrame.sheepCheck.getSelectedItem()-(int)firstFrame.sheepCheck.getSelectedItem());

            secondFrame.player.monoBrick((int)firstFrame.brickCheck.getSelectedItem()-(int)secondFrame.brickCheck.getSelectedItem());
            secondFrame.player.monoLumber((int)firstFrame.woodCheck.getSelectedItem()-(int)secondFrame.woodCheck.getSelectedItem());
            secondFrame.player.monoOre((int)firstFrame.oreCheck.getSelectedItem()-(int)secondFrame.oreCheck.getSelectedItem());
            secondFrame.player.monoWheat((int)firstFrame.wheatCheck.getSelectedItem()-(int)secondFrame.wheatCheck.getSelectedItem());
            secondFrame.player.monoWool((int)firstFrame.sheepCheck.getSelectedItem()- (int) secondFrame.sheepCheck.getSelectedItem());
            
            firstFrame.setVisible(false);
            secondFrame.setVisible(false);
            secondFrame.cbRef.updateAllStatusMenus();
        }
    }

    public void updateComboBoxes(){
        this.asker=(player.isTurn());
        this.setBounds(100, 100, 475, 235);
        confirmButton.setEnabled(true);
        askButton.setEnabled(true);
        brickCheck.removeAllItems();
        wheatCheck.removeAllItems();
        oreCheck.removeAllItems();
        woodCheck.removeAllItems();
        sheepCheck.removeAllItems();
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

        if(asker)
            confirmButton.setEnabled(false);

        if(!asker)
            askButton.setEnabled(false);
    }
}
