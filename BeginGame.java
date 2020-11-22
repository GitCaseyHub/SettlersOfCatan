import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class BeginGame extends JFrame implements ActionListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JButton generateChars = new JButton("Generate Characters");
    JButton startGame = new JButton("Start Game");
    JCheckBox activePorts = new JCheckBox("Active Ports");
    JCheckBox multipleRobbers =new JCheckBox("Multiple Robbers");
    JComboBox<String> players = new JComboBox<String>();
    JPanel options = new JPanel(new GridLayout(1,3));
    JPanel charGenerate = new JPanel(new GridLayout(1,2));
    String[] comboOptions = {"Active Players","Two Players","Three Players","Four Players"};
    Point[] generationPoints = new Point[]{new Point(550,100),new Point(550,455), new Point(1035,100), new Point(1035,455)};
    PlayerSelect[] playerCreation;
    String[] numToString = {"One","Two","Three","Four"};

    ArrayList<Player> catanPlayerList = new ArrayList<Player>();

    public BeginGame(){
        for(int x=0; x<comboOptions.length; x++)
            players.addItem(comboOptions[x]);

        this.setLayout(new BorderLayout());
        this.add(options,BorderLayout.CENTER);
            options.setBorder(new TitledBorder("Game Options"));
            options.add(players);
                players.setBorder(compound);
            options.add(activePorts);
            options.add(multipleRobbers);
                activePorts.setBorderPainted(true);
                activePorts.setBorder(compound);
                multipleRobbers.setBorder(compound);
                multipleRobbers.setBorderPainted(true);
        this.add(charGenerate,BorderLayout.SOUTH);
            charGenerate.setBorder(new TitledBorder("Game Generation"));
            charGenerate.add(generateChars);
                generateChars.setBorder(compound);
            charGenerate.add(startGame);
                startGame.setBorder(compound);
                startGame.setEnabled(false);
                generateChars.setEnabled(false);

                players.addActionListener(this);
                generateChars.addActionListener(this);
                startGame.addActionListener(this);

            this.setBounds(100,100,400,145);
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

            for(int x=0; x<players.getSelectedIndex()+1; x++){
                playerCreation[x] = new PlayerSelect(this,(x+1));
                playerCreation[x].setBounds((int) generationPoints[x].getX(), (int) generationPoints[x].getY(), 435, 305);
                playerCreation[x].setVisible(true);
                playerCreation[x].setTitle("Player "+numToString[x]+" Creation");
            }
            playerCreation[0].nameField.requestFocus();
        }

        else if(e.getSource()==startGame){
            JOptionPane.showMessageDialog(this,"You're ready to begin play. Enjoy Settlers of Catan®.","Generating Game",1);
            this.setVisible(false);
        }
    }

    public static void main(String[] nipTownUSA){new BeginGame();}

    public void addPlayer(Player addedPlayer,int referenceNumber){
        if(catanPlayerList.size()==0){
            JOptionPane.showMessageDialog(this, "You've created your character.", "Character Creation", 1);
            PlayerSelect referenceView = playerCreation[referenceNumber-1];
            referenceView.nameField.setEditable(false);
            referenceView.classBox.setEnabled(false);
            referenceView.confirmButton.setEnabled(false);
            referenceView.colorBox.setEnabled(false);
            this.setTitle(referenceView.nameField.getText() + "'s Character");
            catanPlayerList.add(addedPlayer);
            for(int y=0; y<playerCreation.length; y++)
                if(y!=referenceNumber-1)
                    playerCreation[y].colorBox.removeItem(referenceView.colorBox.getSelectedItem());
        }

        else{
            for(int x=0; x<catanPlayerList.size(); x++) {
                if (catanPlayerList.get(x).getName().equals(addedPlayer.getName()))
                    JOptionPane.showMessageDialog(this, "Another player has already registered that name. Choose another name.", "Name Error", 3);

                else {
                    PlayerSelect referenceView = playerCreation[referenceNumber - 1];
                    JOptionPane.showMessageDialog(this, "You've created your character.", "Character Creation", 1);
                    referenceView.nameField.setEditable(false);
                    referenceView.classBox.setEnabled(false);
                    referenceView.confirmButton.setEnabled(false);
                    referenceView.colorBox.setEnabled(false);
                    this.setTitle(referenceView.nameField.getText() + "'s Character");
                    catanPlayerList.add(addedPlayer);
                    for (int y = 0; y < playerCreation.length; y++)
                        if (y != referenceNumber - 1)
                            playerCreation[y].colorBox.removeItem(referenceView.colorBox.getSelectedItem());
                    x=catanPlayerList.size();
                }
            }
        }

        if(catanPlayerList.size()==playerCreation.length){
            for(int x=0; x<playerCreation.length; x++)
                playerCreation[x].setVisible(false);

            startGame.setEnabled(true);
        }
    }
}
