import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TradingFrame extends JFrame implements ActionListener {
    //Year of Plenty & Monopoly
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    JPanel southPanel = new JPanel(new GridLayout(1,2));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JComboBox brickCheck = new JComboBox();
    JComboBox oreCheck = new JComboBox();
    JComboBox wheatCheck = new JComboBox();
    JComboBox sheepCheck = new JComboBox();
    JComboBox woodCheck = new JComboBox();
    JButton confirmButton = new JButton("Confirm Trade");
    JButton askButton = new JButton("Ask Another Player");

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
            graphicImageLabels[x] = new JLabel("", 0);
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
            String playerName = JOptionPane.showInputDialog(this,"Which player would you like to trade with?","Trading Player",1);
            int playerCounter=0;
            for(int x=0; x<cbRef.catanPlayerList.size(); x++)
                if(cbRef.catanPlayerList.get(x).getName().equals(playerName))
                    playerCounter++;

            if(playerCounter==0){
                askButton.setEnabled(true);
                JOptionPane.showMessageDialog(this,"There is no player with "+playerName+" as a name. Please type their name correctly.","No Such Player Exists",3);
            }

            else if(playerCounter==1){
                cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.asker=false;
                cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.updateComboBoxes();
                cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setBounds((int)this.getBounds().getX()+500,(int)this.getBounds().getY(),475,235);
                cbRef.getPlayerStatusMenu(cbRef.getPlayerViaName(playerName)).tf.setVisible(true);
            }

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