import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

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
    Point[] statusGenerationPoints = new Point[]{new Point(1080,100),new Point(1080,553), new Point(1555,100), new Point(1605,553)};
    PlayerSelect[] playerCreation;
    PlayerView[] statusViewer;

    ArrayList<Player> catanPlayerList = new ArrayList<Player>();
    ArrayList<Integer> startPickOrder;
    ArrayList<Integer> turnOrder;
    CatanBoard cbMain = new CatanBoard();

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
                playerCreation[x] = new PlayerSelect(this,x);
                playerCreation[x].setBounds((int) generationPoints[x].getX(), (int) generationPoints[x].getY(), 435, 305);
                playerCreation[x].setVisible(true);
                playerCreation[x].setTitle("Player Select Screen");
            }
            playerCreation[0].nameField.requestFocus();
        }

        else if(e.getSource()==startGame) {
            JOptionPane.showMessageDialog(this, "You're ready to begin play. Enjoy Settlers of Catan®.", "Generating Game", 1);
            int startingPlayer = new Random().nextInt(playerCreation.length);
            for (int x = 0; x < catanPlayerList.size(); x++) {
                if (catanPlayerList.get(x).getRefNumber() == startingPlayer) {
                    catanPlayerList.get(x).setTurn(true);
                }
            }

            //Creating status screen for each player
            statusViewer = new PlayerView[catanPlayerList.size()];
            for (int x = 0; x < catanPlayerList.size(); x++){
                statusViewer[x] = new PlayerView(catanPlayerList.get(x), cbMain);
                statusViewer[x].setBounds((int)statusGenerationPoints[x].getX(),(int)statusGenerationPoints[x].getY(),475,353);
                statusViewer[x].setVisible(true);
                statusViewer[x].setTitle(catanPlayerList.get(x).getName()+" - "+catanPlayerList.get(x).getClassTitle());
            }

            //Construct starting pick order
            startPickOrder = new ArrayList<Integer>();
            turnOrder=new ArrayList<Integer>();
            for(int x=0; x<catanPlayerList.size(); x++) {
                startPickOrder.add((startingPlayer+x)% catanPlayerList.size());
            }
            turnOrder = startPickOrder;
            startPickOrder.addAll(reverse(startPickOrder));
            this.setVisible(false);
            cbMain.setUndecorated(true);
            cbMain.setBounds(100,100,930,800);
            cbMain.setTitle("Settlers of Catan®");
            cbMain.setVisible(true);
        }
    }

    public static void main(String[] nipTownUSA){new BeginGame();}

    public ArrayList<Integer> reverse(ArrayList<Integer> list){
        ArrayList<Integer> reversed = new ArrayList<Integer>();
        for(int x=list.size()-1; x>-1; x--)
            reversed.add(list.get(x));
        return reversed;
    }

    public void addPlayer(Player addedPlayer,int referenceNumber){
        if(catanPlayerList.size()==0){
            JOptionPane.showMessageDialog(this, "You've created your character.", "Character Creation", 1);
            PlayerSelect referenceView = playerCreation[referenceNumber];
            referenceView.nameField.setEditable(false);
            referenceView.classBox.setEnabled(false);
            referenceView.confirmButton.setEnabled(false);
            referenceView.colorBox.setEnabled(false);
            this.setTitle(referenceView.nameField.getText() + "'s Character");
            catanPlayerList.add(addedPlayer);
            for(int y=0; y<playerCreation.length; y++)
                if(y!=referenceNumber)
                    playerCreation[y].colorBox.removeItem(referenceView.colorBox.getSelectedItem());
        }

        else{
            for(int x=0; x<catanPlayerList.size(); x++) {
                if (catanPlayerList.get(x).getName().equals(addedPlayer.getName()))
                    JOptionPane.showMessageDialog(this, "Another player has already registered that name. Choose another name.", "Name Error", 3);

                else {
                    PlayerSelect referenceView = playerCreation[referenceNumber];
                    JOptionPane.showMessageDialog(this, "You've created your character.", "Character Creation", 1);
                    referenceView.nameField.setEditable(false);
                    referenceView.classBox.setEnabled(false);
                    referenceView.confirmButton.setEnabled(false);
                    referenceView.colorBox.setEnabled(false);
                    this.setTitle(referenceView.nameField.getText() + "'s Character");
                    catanPlayerList.add(addedPlayer);
                    for (int y = 0; y < playerCreation.length; y++)
                        if (y != referenceNumber)
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

    public void passTurn(){}
}
