import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import jdk.dynalink.linker.support.CompositeTypeBasedGuardingDynamicLinker;

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
    boolean playerRegistration=false;

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
    JMenu optionMenu = new JMenu("Options");
    JMenu help = new JMenu("Help");
    JMenu modes = new JMenu("Modes");
    JCheckBoxMenuItem previewMenu = new JCheckBoxMenuItem("'Preview' Frame Active");
    JCheckBoxMenuItem motionMenu = new JCheckBoxMenuItem("'Awards' Frame Active");
    JCheckBoxMenuItem specialClassMenu = new JCheckBoxMenuItem("Enable Class Special Actions");
    JCheckBoxMenuItem wildfires = new JCheckBoxMenuItem("Enable Wildfires");
    JCheckBoxMenuItem devCardTransparency = new JCheckBoxMenuItem("Enable Card Transparency");
    JCheckBoxMenuItem randomizer = new JCheckBoxMenuItem("Random Mode");
    JCheckBoxMenuItem democracyMode = new JCheckBoxMenuItem("Democracy Mode");
    JCheckBoxMenuItem monarchMode = new JCheckBoxMenuItem("Monarch Mode");
    JCheckBoxMenuItem communityMode = new JCheckBoxMenuItem("Cult Mode");
    JMenuItem helpMenu = new JMenuItem("How-To Guide");

    // New Player Number Select Method
    int numPlayers;
    JFrame playerNumberFrame = new JFrame();
    JLabel[] playerNumberLabels = new JLabel[]{new JLabel("2"),new JLabel("3"),new JLabel("4")};
    JPanel[] playersPanel = new JPanel[]{new JPanel(new BorderLayout()),new JPanel(new BorderLayout()), new JPanel(new BorderLayout())};
    JPanel numberPanel = new JPanel(new GridLayout(1,3));
    Map<String, String> numMap = new HashMap<String, String>() {{put("2","Two"); put("3","Three"); put("4","Four");}};

    //Instructions Frame
    JFrame instructions = new JFrame();
    JLabel instructionsImage = new JLabel("",SwingConstants.CENTER);

    public BeginGame() {
        openingLabel.setToolTipText("Click To Start Playing");
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

        devCardTransparency.setToolTipText("Click to allow players to know the exact number of development cards of a type that are available, hidden or not. Otherwise, you will be given an estimation of how many remain.");
        optionMenu.add(motionMenu);
        devCardTransparency.addActionListener(this);
        optionMenu.add(previewMenu);
        optionMenu.add(devCardTransparency);
        optionMenu.add(specialClassMenu);
        motionMenu.addActionListener(this);
        motionMenu.setToolTipText("Click to enable a frame showing what award a player has should they click on the corresponding checkbox in their player status screen.");
        specialClassMenu.addActionListener(this);
        modes.add(communityMode);
        modes.add(democracyMode);
        modes.add(monarchMode);
        modes.add(randomizer);
        randomizer.addActionListener(this);
        randomizer.setToolTipText("Every turn, tiles will change types. The tiles will then be given a new roll value that may or may not be consistent with probabilities.");
        democracyMode.addActionListener(this);
        democracyMode.setToolTipText("Every turn cycle, players vote for a ruler. That player can then decide what they roll on the dice when it's their turn.");
        monarchMode.addActionListener(this);
        monarchMode.setToolTipText("Every turn cycle, a random player is selected as leader.");
        communityMode.addActionListener(this);
        communityMode.setToolTipText("A group of religious zealots take up residence on Catan, and they will take and give resources out as their God dictates.");
        specialClassMenu.setToolTipText("Class-unique actions are usable in game. For example, stealing using the Highwayman's special action can be done.");
        optionMenu.add(wildfires);

        wildfires.addActionListener(this);
        wildfires.setToolTipText("If a tile is on fire, there is a 5% chance neighboring tiles will ignite after a turn cycle.");
        optionMenu.addSeparator();
        modes.addSeparator();

        this.add(borderPanel);
        borderPanel.setBorder(compound);
        borderPanel.add(options,BorderLayout.CENTER);
        options.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Special Features"));
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
        
        createPlayerNumberSelectFrame();
        commencementFrameInitiation();
        initializeInstructions();
    }

    public void createPlayerNumberSelectFrame(){
        playerNumberFrame.add(numberPanel);
        Arrays.stream(playersPanel).forEach(panel -> {numberPanel.add(panel);});

        for (int i=0; i<playersPanel.length; i++){
            playersPanel[i].add(new JLabel(numMap.get(playerNumberLabels[i].getText())+" Players",SwingConstants.CENTER){{setBorder(compound);}},BorderLayout.NORTH);
            playersPanel[i].add(playerNumberLabels[i],BorderLayout.CENTER);
        }

        Arrays.stream(playerNumberLabels).forEach(label->{
            label.addMouseListener(this);
            label.setIcon(new ImageIcon("Resources/PlayerNumber/"+label.getText()+"_Players.png"));
            label.setBorder(compound);
        });

        playerNumberFrame.setSize(630,210);
        playerNumberFrame.isAlwaysOnTop();
        playerNumberFrame.setUndecorated(true);
        playerNumberFrame.setLocation(5+dim.width/2-playerNumberFrame.getSize().width/2, dim.height/2-playerNumberFrame.getSize().height/2);
    }

    public void initializeInstructions(){
        instructions.setUndecorated(true);
        instructions.add(instructionsImage);
        instructionsImage.addMouseListener(this);
        instructionsImage.setIcon(new ImageIcon("Resources/Instructions.png"));
        instructionsImage.setBorder(compound);
        instructions.setSize(618,394);
        instructions.setLocation(dim.width/2-instructions.getSize().width/2, dim.height/2-instructions.getSize().height/2);
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
        if (e.getSource() == specialClassMenu || e.getSource() == motionMenu || e.getSource() == previewMenu || e.getSource()==wildfires || e.getSource()==devCardTransparency)
            optionMenu.doClick();

        if(e.getSource()==randomizer || e.getSource()==democracyMode || e.getSource()==monarchMode || e.getSource()==communityMode)
            modes.doClick();

        if(e.getSource()==democracyMode) 
            Arrays.stream(new JMenuItem[]{randomizer,monarchMode,communityMode}).forEach(box->{box.setSelected(false);});

        if(e.getSource()==monarchMode)
            Arrays.stream(new JMenuItem[]{randomizer,communityMode,democracyMode}).forEach(box->{box.setSelected(false);});

        if(e.getSource()==randomizer)
            Arrays.stream(new JMenuItem[]{monarchMode,communityMode,democracyMode}).forEach(box->{box.setSelected(false);});

        if(e.getSource()==communityMode)
            Arrays.stream(new JMenuItem[]{randomizer,monarchMode,democracyMode}).forEach(box->{box.setSelected(false);});

        else if(e.getSource()==generateChars){
            generateChars.setEnabled(false);
            playerNumberFrame.setVisible(true);
            JOptionPane.showMessageDialog(playerNumberFrame,"Select the number of players who will be participating in the game","Player Registration",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
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
            cbMain.monarchy=monarchMode.isSelected();
            cbMain.community=communityMode.isSelected();
            cbMain.setBounds(60, 45, 930, 1000);
            cbMain.dispose();
            cbMain.setUndecorated(true);
            cbMain.setTitle("Settlers of Catan®");
            cbMain.setVisible(true);
            cbMain.performStartingOperations();
        }

        else if(e.getSource()==helpMenu){
            /*
            //Former Operation Guide as a JOptionPane
            JOptionPane.showMessageDialog(imageFrame, "====================================================================================================\n"+
                                                               "                                                                                           Building & Operation Guide\n"+
                                                               "====================================================================================================\n"+
                                                               "Building Settlements ⇒ Select an intersection point of three hexagonal tiles (or two hexagonal tiles if you are on the coast).\n\n"+
                                                               "Building Roads ⇒ Select an index where you own a settlement. Then, select a second index in the direction of the road you'd\n                                  like to build that is one hexagonal side-length away.\n\n"+
                                                               "Cancelling Operations ⇒ Hold ALT+C while the board has focus. Your resources will be refunded accordingly and the menu\n                                               will reactivate until you end your turn (Note operations include: road building, settlement building,\n                                               upgrading settlements to cities, playing knight cards, and setting fire to tiles).\n\n"+
                                                               "Exit Game ⇒ Hold ALT+X while the board has focus. You will be given an option about whether you'd like to quit.\n" +
                                                               "====================================================================================================",
                                                          "Help Menu - How-To",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
            */

            instructions.setVisible(true);
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
            playerRegistration=false;
            for (Player player : catanPlayerList)
                if (player.getName().equals(addedPlayer.getName())) {
                    JOptionPane.showMessageDialog(findPlayerSelectFrame(player), "Another player has already registered. Choose another name.", "Name Error", JOptionPane.QUESTION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
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
            Arrays.stream(playerCreation).forEach( pc -> pc.setVisible(false));
            startGame.setEnabled(true);
        }
    }

    public void mousePressed(MouseEvent e) {
        for (JLabel label : playerNumberLabels) {
            if (e.getSource() == label) {
                playerNumberFrame.setVisible(false);
                if(JOptionPane.showConfirmDialog(imageFrame,"Are you sure you want a "+label.getText()+" player game?","Number of Players Check", JOptionPane.YES_NO_OPTION,1, new ImageIcon("Resources/Catan_Icon.png"))==JOptionPane.YES_OPTION){
                    playerNumberFrame.setVisible(false);
                    base.fixState();
                    numPlayers = Integer.parseInt(label.getText());
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
                else{
                    playerNumberFrame.setVisible(true);
                    return;
                }
                break;
            }
        }
        
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
        if(e.getSource()==instructionsImage)
            instructions.setVisible(false);

        if(e.getSource() == playerNumberLabels[0]){

        }
        if(e.getSource() == playerNumberLabels[1]){
            
        }
        if(e.getSource() == playerNumberLabels[1]){
            
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseClicked(MouseEvent e) {}
}
