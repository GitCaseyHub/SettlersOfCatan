import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class BeginGame extends JFrame implements ActionListener, MouseListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JButton generateChars = new JButton("Generate Templates");
    JButton startGame = new JButton("Start Game");
    JCheckBox activePorts = new JCheckBox("Active Port Trading");
    JCheckBox friendlyRobber = new JCheckBox("Friendly Robber");
    JCheckBox cataclysms = new JCheckBox("Active Cataclysms");
    JComboBox<String> players = new JComboBox<>();
    JPanel options = new JPanel(new GridLayout(1,4));
    JPanel charGenerate = new JPanel(new GridLayout(1,2));
    String[] comboOptions = {"Active Players","Two Players","Three Players","Four Players"};
    Point[] generationPoints = new Point[]{new Point(195,169), new Point(195,524), new Point(1290,169), new Point(1290,524)};
    Point[] statusGenerationPoints = new Point[]{new Point(990,100),new Point(990,455), new Point(1440,100), new Point(1440,455)};
    PlayerSelect[] playerCreation;

    ArrayList<Player> catanPlayerList = new ArrayList<>();

    //Opening Frame
    JFrame openingFrame = new JFrame();
    JLabel openingLabel = new JLabel("",SwingConstants.CENTER);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    //Bonus space filling objects when on player select screen
    JFrame imageFrame = new JFrame();
    JLabel imageLabel = new JLabel();

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
            options.add(cataclysms);
            options.add(friendlyRobber);
                activePorts.setBorderPainted(true);
                activePorts.setBorder(compound);
                activePorts.setToolTipText("Select this checkbox if you want special trading ports to be usable in game. On the board, ports will be identified by hollow green circles.");
                friendlyRobber.setBorderPainted(true);
                friendlyRobber.setBorder(compound);
                friendlyRobber.setToolTipText("Select this checkbox to disable the robber from stealing from players with less than 4 victory points.");
                cataclysms.setBorderPainted(true);
                cataclysms.setBorder(compound);
                cataclysms.setToolTipText("Select this checkbox to activate weather events that inflict damages upon the players at random intervals.");
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

        commencementFrameInitiation();
    }

    public void commencementFrameInitiation(){
        openingFrame.add(openingLabel);
        openingLabel.setIcon(new ImageIcon("Resources/CatanOpening.png"));
        openingLabel.setBorder(compound);
        openingFrame.setUndecorated(true);
        openingLabel.addMouseListener(this);
        openingFrame.setSize(715,716);
        openingFrame.setLocation(dim.width/2-openingFrame.getSize().width/2, dim.height/2-openingFrame.getSize().height/2);
        openingFrame.setVisible(true);
        imageLabel.setIcon(new ImageIcon("Resources/BonusOpening.png"));
        imageFrame.add(imageLabel);
        imageLabel.setBorder(compound);
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

            CatanBoard cbMain = new CatanBoard(catanPlayerList, statusGenerationPoints, playerCreation, this);
            cbMain.friendlyRobber=friendlyRobber.isSelected();
            cbMain.cataclysmsActive=cataclysms.isSelected();
            usablePorts=activePorts.isSelected();
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
                    JOptionPane.showMessageDialog(this, "Another player has already registered that name. Choose another name.", "Name Error", JOptionPane.QUESTION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
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
            for (PlayerSelect playerSelect : playerCreation)
                playerSelect.setVisible(false);

            startGame.setEnabled(true);
        }
    }
    public void mousePressed(MouseEvent e) {
        if(e.getSource()==openingLabel){
            JOptionPane.showMessageDialog(this,"Welcome to Settlers of Catan","Welcome",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
            openingFrame.setVisible(false);
            this.setSize(560,142);
            this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2-300);
            this.setVisible(true);
            this.setTitle("Options and Game Creation");
            imageFrame.setUndecorated(true);
            imageFrame.setLocation(dim.width/2-this.getSize().width/2+8, dim.height/2-this.getSize().height/2-300+150);
            imageFrame.setSize(544,502);
            imageFrame.setVisible(true);
        }
    }

    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}
