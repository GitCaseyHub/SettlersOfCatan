import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;

public class BeginGame extends JFrame implements ActionListener, MouseListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JPanel borderPanel = new JPanel(new BorderLayout());
    JButton generateChars = new JButton("Create Characters");
    JButton startGame = new JButton("Start Game");
    CustomSwitch activePorts = new CustomSwitch("Port Exchanges",false);
    CustomSwitch cataclysms = new CustomSwitch("Cataclysms",false);
    CustomSwitch base = new CustomSwitch("Character Uniformity",false);
    CustomSwitch razing = new CustomSwitch("Tile Desertification",false);
    JPanel options = new JPanel(new GridLayout(1,4));
    JPanel charGenerate = new JPanel(new GridLayout(1,2));
    Point[] generationPoints = new Point[]{new Point(195,165), new Point(195,523), new Point(1290,165), new Point(1290,523)};
    Point[] statusGenerationPoints = new Point[]{new Point(990,100),new Point(1440,100),new Point(990,455), new Point(1440,455)};
    PlayerSelect[] playerCreation;
    ArrayList<Player> catanPlayerList = new ArrayList<>();

    //Opening Frame
    JFrame openingFrame = new JFrame();
    JLabel openingLabel = new JLabel("",SwingConstants.CENTER);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
    CatanBoard cbMain;

    //Bonus space filling objects when on player select screen
    JFrame imageFrame = new JFrame();
    JLabel imageLabel = new JLabel("",SwingConstants.CENTER);

    //JMenuBar
    JMenuBar mb = new JMenuBar();
    JMenu optionMenu = new JMenu("Features");
    JMenu help = new JMenu("Help");
    JMenu modes = new JMenu("Modes");
    JCheckBoxMenuItem previewMenu = new JCheckBoxMenuItem("Enable 'Preview' Frame");
    JCheckBoxMenuItem motionMenu = new JCheckBoxMenuItem("Enable 'Awards' Frame");
    JCheckBoxMenuItem specialClassMenu = new JCheckBoxMenuItem("Enable Class Special Actions");
    JCheckBoxMenuItem wildfires = new JCheckBoxMenuItem("Enable Wildfires");
    JCheckBoxMenuItem devCardTransparency = new JCheckBoxMenuItem("Enable Card Transparency");
    JCheckBoxMenuItem randomizer = new JCheckBoxMenuItem("Random Mode");
    JCheckBoxMenuItem democracyMode = new JCheckBoxMenuItem("Democracy Mode");
    JMenuItem helpMenu = new JMenuItem("How-To Guide");

    //Number of Players Question
    int numPlayers;
    JCheckBox[] playerNumOptions = new JCheckBox[3];
    String[] boxString = new String[]{"     ⇒     2 Players","     ⇒     3 Players","     ⇒     4 Players"};
    String[] imageTitles = new String[]{"Two_Players.png","Three_Players.png","Four_Players.png"};

    public BeginGame() {
        this.setUndecorated(true);
        this.setJMenuBar(mb);
        mb.setBorder(compound);
        mb.setBorderPainted(true);
        mb.add(optionMenu);
        mb.add(modes);
        modes.addSeparator();
        mb.add(help);
        help.addSeparator();
        help.add(helpMenu);
        help.addSeparator();
        helpMenu.addActionListener(this);
        optionMenu.addSeparator();
        helpMenu.setToolTipText("Click here to learn how to perform certain actions in-game and what the short-cut keys are for performing special operations.");
        previewMenu.setToolTipText("Click to enable frame depicting what your current action is (i.e. an image of road construction appears when you build a road).");
        previewMenu.addActionListener(this);
        optionMenu.add(specialClassMenu);
        optionMenu.add(devCardTransparency);
        devCardTransparency.setToolTipText("Click to allow players to know the exact number of development cards of a type that are available, hidden or not. Otherwise, you will be given an estimation of how many remain.");
        optionMenu.add(motionMenu);
        devCardTransparency.addActionListener(this);
        optionMenu.add(previewMenu);
        motionMenu.addActionListener(this);
        motionMenu.setToolTipText("Click to enable a frame showing what award a player has should they hover over the checkbox in their player status screen.");
        specialClassMenu.addActionListener(this);
        modes.add(democracyMode);
        modes.add(randomizer);
        randomizer.addActionListener(this);
        randomizer.setToolTipText("Every turn, tiles will change types. The tiles will then be given a new roll value that may or may not be consistent with probabilities.");
        democracyMode.addActionListener(this);
        democracyMode.setToolTipText("Every turn cycle, players vote for a ruler. That player can then decide what they roll on the dice when it's their turn.");
        specialClassMenu.setToolTipText("Class-unique actions are usable in game. For example, stealing using the Highwayman's special action can be done.");
        optionMenu.add(wildfires);
        wildfires.addActionListener(this);
        wildfires.setToolTipText("If a tile is on fire, there is a 5% chance neighboring tiles will ignite after a turn cycle.");
        optionMenu.addSeparator();
        modes.addSeparator();

        this.add(borderPanel);
        borderPanel.setBorder(compound);
        borderPanel.add(options,BorderLayout.CENTER);
            options.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Options"));
            options.add(base);
            options.add(activePorts);
            options.add(cataclysms);
            options.add(razing);
                activePorts.setToolTipText("Select the 'YES' option if you want ports to be usable. On the board, ports will be marked with hollow green circles.");
                cataclysms.setToolTipText("Select the 'YES' option to activate weather events that inflict damages upon the players at random intervals.");
                base.setToolTipText("Select the 'YES' option to force all players to be the same class.");
                razing.setToolTipText("Select the 'YES' option to have a 0.1% chance that a single tile will turn into a desert every end of turn.");
        borderPanel.add(charGenerate,BorderLayout.SOUTH);
            charGenerate.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Setup & Startup"));
            charGenerate.add(generateChars);
                generateChars.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                generateChars.setToolTipText("Click this button to create screens for players to choose their characters.");
            charGenerate.add(startGame);
                startGame.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
                startGame.setEnabled(false);
                generateChars.addActionListener(this);
                startGame.addActionListener(this);

        commencementFrameInitiation();
        initializeCheckBoxes();
    }

    public void initializeCheckBoxes() {
        for (int x = 0; x < playerNumOptions.length; x++) {
            playerNumOptions[x] = new JCheckBox(boxString[x],new ImageIcon("Resources/People/"+imageTitles[x]));
            playerNumOptions[x].addActionListener(this);
        }
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

    public boolean appropriateNumSelected(JCheckBox[] boxes){
        return Arrays.stream(boxes).filter(AbstractButton::isSelected).count() != 1;
    }

    public void actionPerformed(ActionEvent e) {
        for(JCheckBox box: playerNumOptions)
            if(e.getSource()==box) {
                Arrays.stream(playerNumOptions).filter(boxCheck -> !boxCheck.equals(box)).forEach(oldBox -> oldBox.setSelected(false));
                return;
            }

        if (e.getSource() == specialClassMenu || e.getSource() == motionMenu || e.getSource() == previewMenu || e.getSource()==wildfires || e.getSource()==devCardTransparency)
            optionMenu.doClick();

        if(e.getSource()==randomizer || e.getSource()==democracyMode)
            modes.doClick();

        if(e.getSource()==democracyMode && randomizer.isSelected())
            randomizer.setSelected(false);

        if(e.getSource()==randomizer && democracyMode.isSelected())
            democracyMode.setSelected(false);

        else if(e.getSource()==generateChars){
            while(appropriateNumSelected(playerNumOptions)) {
                JOptionPane.showMessageDialog(imageFrame, new Object[]{"=======================\n   Number of People Playing\n=======================", playerNumOptions,"======================="}, "Number of Players", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                if(appropriateNumSelected(playerNumOptions)) {
                    JOptionPane.showMessageDialog(imageFrame, "You must select one of the options.", "Failed Registration", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    Arrays.stream(playerNumOptions).forEach(box -> box.setSelected(false));
                }
            }
            generateChars.setEnabled(false);
            base.fixState();
            numPlayers = (playerNumOptions[0].isSelected()?2:(playerNumOptions[1].isSelected()?3:4));
            playerCreation = new PlayerSelect[numPlayers];

            for(int x=0; x<numPlayers; x++) {
                playerCreation[x] = new PlayerSelect(this, x,false);
                playerCreation[x].setBounds((int) generationPoints[x].getX(), (int) generationPoints[x].getY(), 435, 305);
                playerCreation[x].setVisible(true);
                playerCreation[x].setTitle("Player "+(playerCreation[x].referenceNumber+1)+" Select Screen");
            }
            Arrays.stream(playerCreation).forEach(player -> player.loadedIn=true);
            playerCreation[0].nameField.requestFocus();
        }

        else if(e.getSource()==startGame) {
            if(JOptionPane.showConfirmDialog(imageFrame,"Are you sure about the game settings you have selected?","Game Check", JOptionPane.YES_NO_OPTION,1, new ImageIcon("Resources/Catan_Icon.png"))!=JOptionPane.YES_OPTION)
                return;

            this.setVisible(false);
            imageFrame.setVisible(false);
            cbMain = new CatanBoard(catanPlayerList, statusGenerationPoints, playerCreation, this);
            cbMain.razing = razing.isSelected();
            cbMain.cataclysmsActive=cataclysms.isSelected();
            cbMain.previewFrames=previewMenu.isSelected();
            cbMain.isUsingMotionFrame = motionMenu.isSelected();
            cbMain.usablePorts=activePorts.isSelected();
            cbMain.specialActions = specialClassMenu.isSelected();
            cbMain.wildfire = wildfires.isSelected();
            cbMain.randomize = randomizer.isSelected();
            cbMain.democracy = democracyMode.isSelected();
            cbMain.devCardTransparency = devCardTransparency.isSelected();
            cbMain.setBounds(60, 45, 930, 1000);
            cbMain.dispose();
            cbMain.setUndecorated(true);
            cbMain.setTitle("Settlers of Catan®");
            cbMain.setVisible(true);
            cbMain.performStartingOperations();
        }

        else if(e.getSource()==helpMenu){
            JOptionPane.showMessageDialog(imageFrame, "====================================================================================================\n"+
                                                               "                                                                                           Building & Operation Guide\n"+
                                                               "====================================================================================================\n"+
                                                               "Building Settlements ⇒ Select an intersection point of three hexagonal tiles (or two hexagonal tiles if you are on the coast).\n\n"+
                                                               "Building Roads ⇒ Select an index where you own a settlement. Then, select a second index in the direction of the road you'd\n                                  like to build that is one hexagonal side-length away.\n\n"+
                                                               "Cancelling Operations ⇒ Hold ALT+C while the board has focus. Your resources will be refunded accordingly and the menu\n                                               will reactivate until you end your turn (Note operations include: road building, settlement building,\n                                               upgrading settlements to cities, playing knight cards, and setting fire to tiles).\n\n"+
                                                               "Exit Game ⇒ Hold ALT+X while the board has focus. You will be given an option about whether you'd like to quit.\n" +
                                                               "====================================================================================================",
                                                          "Help Menu - How-To",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
        }
    }

    public static void main(String[] args){new BeginGame();}

    public PlayerSelect findPlayerSelectFrame(Player player){
        return Arrays.stream(playerCreation).filter(frame -> frame.nameField.getText().equalsIgnoreCase(player.getName()) && !frame.submitted).findFirst().orElse(new PlayerSelect());
    }

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
                    JOptionPane.showMessageDialog(findPlayerSelectFrame(player), "That name has already been registered. Choose another name.", "Name Error", JOptionPane.QUESTION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
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
            Arrays.stream(playerCreation).forEach( ps -> ps.setVisible(false));
            startGame.setEnabled(true);
        }
    }
    public void mousePressed(MouseEvent e) {
        if(e.getSource()==openingLabel){
            JOptionPane.showMessageDialog(this,"Let's Play Settlers of Catan","Settlers of Catan",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
            openingFrame.setVisible(false);
            this.setSize(543,148);
            this.setLocation(dim.width/2-this.getSize().width/2+8, dim.height/2-this.getSize().height/2-300);
            this.setVisible(true);
            imageFrame.setUndecorated(true);
            imageFrame.setLocation(dim.width/2-this.getSize().width/2+8, dim.height/2-this.getSize().height/2-300+152);
            imageFrame.setSize(544,502);
            imageFrame.setVisible(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}
