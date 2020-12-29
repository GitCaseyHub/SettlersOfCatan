import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BeginGame extends JFrame implements ActionListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JButton generateChars = new JButton("Generate Templates");
    JButton startGame = new JButton("Start Game");
    JCheckBox activePorts = new JCheckBox("Active Ports");
    JCheckBox friendlyRobber = new JCheckBox("Friendly Robber");
    JComboBox<String> players = new JComboBox<String>();
    JPanel options = new JPanel(new GridLayout(1,3));
    JPanel charGenerate = new JPanel(new GridLayout(1,2));
    String[] comboOptions = {"Active Players","Two Players","Three Players","Four Players"};
    Point[] generationPoints = new Point[]{new Point(550,100),new Point(550,455), new Point(1035,100), new Point(1035,455)};
    Point[] statusGenerationPoints = new Point[]{new Point(990,100),new Point(990,455), new Point(1440,100), new Point(1440,455)};
    PlayerSelect[] playerCreation;

    ArrayList<Player> catanPlayerList = new ArrayList<Player>();
    ArrayList<Integer> startPickOrder;
    ArrayList<Integer> turnOrder;

    boolean usablePorts=false;

    public BeginGame(){
        for (String comboOption : comboOptions)
            players.addItem(comboOption);

        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.add(options,BorderLayout.CENTER);
            options.setBorder(new TitledBorder("Game Options"));
            options.add(players);
                players.setBorder(compound);
            options.add(activePorts);
            options.add(friendlyRobber);
                activePorts.setBorderPainted(true);
                activePorts.setBorder(compound);
                activePorts.setToolTipText("Select this checkbox if you want special trading ports to be usable in game. On the board, ports will be identified by red 'x's and green 'o's.");
                friendlyRobber.setBorderPainted(true);
                friendlyRobber.setBorder(compound);
                friendlyRobber.setToolTipText("Select this checkbox to disable The Robber from stealing from players with less than 4 victory points.");
        this.add(charGenerate,BorderLayout.SOUTH);
            charGenerate.setBorder(new TitledBorder("Game Generation"));
            charGenerate.add(generateChars);
                generateChars.setBorder(compound);
                generateChars.setToolTipText("Click this button to create screens for players to choose their characters.");
            charGenerate.add(startGame);
                startGame.setBorder(compound);
                startGame.setEnabled(false);
                generateChars.setEnabled(false);
                players.addActionListener(this);
                generateChars.addActionListener(this);
                startGame.addActionListener(this);

            this.setBounds(100,100,400,142);
            this.setVisible(true);
            this.setTitle("Options and Generation");
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==players)
            generateChars.setEnabled(players.getSelectedIndex()!=0);

        else if(e.getSource()==generateChars){
            generateChars.setEnabled(false);
            players.setEnabled(false);
            playerCreation = new PlayerSelect[players.getSelectedIndex()+1];

            for(int x=0; x<players.getSelectedIndex()+1; x++) {
                playerCreation[x] = new PlayerSelect(this, x);
                playerCreation[x].setBounds((int) generationPoints[x].getX(), (int) generationPoints[x].getY(), 435, 305);
                playerCreation[x].setVisible(true);
                playerCreation[x].setTitle("Player Select Screen");
            }
            playerCreation[0].nameField.requestFocus();
        }

        else if(e.getSource()==startGame) {
            this.setVisible(false);
            if(activePorts.isSelected())
                usablePorts=true;

            CatanBoard cbMain = new CatanBoard(catanPlayerList, statusGenerationPoints, playerCreation, this);
            cbMain.friendlyRobber=friendlyRobber.isSelected();
            cbMain.setBounds(60, 45, 930, 1000);
            cbMain.dispose();
            cbMain.setUndecorated(true);
            cbMain.setTitle("Settlers of Catan®");
            cbMain.setVisible(true);
            cbMain.performStartingOperations();
            this.setVisible(false);
        }
    }

    public static void main(String[] args){new BeginGame();}

    public void addPlayer(Player addedPlayer,int referenceNumber){
        if(catanPlayerList.size()==0){
            PlayerSelect referenceView = playerCreation[referenceNumber];
            referenceView.nameField.setEditable(false);
            referenceView.classBox.setEnabled(false);
            referenceView.confirmButton.setEnabled(false);
            referenceView.colorBox.setEnabled(false);
            referenceView.setTitle(referenceView.nameField.getText() + "'s Character");
            catanPlayerList.add(addedPlayer);
            for(int y=0; y<playerCreation.length; y++)
                if(y!=referenceNumber)
                    playerCreation[y].colorBox.removeItem(referenceView.colorBox.getSelectedItem());
        }
        else{
            boolean playerRegistration=false;
            for (Player player : catanPlayerList)
                if (player.getName().equals(addedPlayer.getName())) {
                    JOptionPane.showMessageDialog(this, "Another player has already registered that name. Choose another name.", "Name Error", 3);
                    playerRegistration = true;
                    break;
                }

            if(!playerRegistration){
                PlayerSelect referenceView = playerCreation[referenceNumber];
                referenceView.nameField.setEditable(false);
                referenceView.classBox.setEnabled(false);
                referenceView.confirmButton.setEnabled(false);
                referenceView.colorBox.setEnabled(false);
                this.setTitle(referenceView.nameField.getText() + "'s Character");
                catanPlayerList.add(addedPlayer);
                for (int y = 0; y < playerCreation.length; y++)
                    if (y != referenceNumber)
                        playerCreation[y].colorBox.removeItem(referenceView.colorBox.getSelectedItem());
            }
        }

        if(catanPlayerList.size()==playerCreation.length){
            for(int x=0; x<playerCreation.length; x++)
                playerCreation[x].setVisible(false);

            startGame.setEnabled(true);
        }
    }
}
