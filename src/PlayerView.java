import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.EtchedBorder;

public class PlayerView extends JFrame implements ActionListener, MouseMotionListener, MouseListener {
    //GUI Assets
    JPanel[] graphicPanels = new JPanel[5];
    JPanel graphicHolder = new JPanel(new GridLayout(1, 5));
    String[] graphicStrings = {"Brick", "Ore", "Sheep", "Wheat", "Wood"};
    JLabel[] graphicImageLabels = new JLabel[5];
    JLabel brickNum = new JLabel("0", SwingConstants.CENTER);
    JLabel oreNum = new JLabel("0", SwingConstants.CENTER);
    JLabel wheatNum = new JLabel("0", SwingConstants.CENTER);
    JLabel sheepNum = new JLabel("0", SwingConstants.CENTER);
    JLabel woodNum = new JLabel("0", SwingConstants.CENTER);
    JPanel borderNorth = new JPanel(new BorderLayout());
    JPanel infoPanel = new JPanel(new GridLayout(1,1));
    JPanel borderInfoPanel = new JPanel(new GridLayout(1,1));
    JLabel colorDisplayLabel = new JLabel("", SwingConstants.CENTER);
    JLabel statusDisplayLabel = new JLabel("",SwingConstants.CENTER);
    JPanel devPanel = new JPanel(new GridLayout(1,2));
    JComboBox<Object> unplayed = new JComboBox<Object>();
    JComboBox<Object> played = new JComboBox<Object>();
    JPanel awardPanel = new JPanel(new GridLayout(1,2));
    JCheckBox longestRoadBox = new JCheckBox("Longest Road");
    JCheckBox largestArmyBox = new JCheckBox("Largest Army");
    JPanel vpPointHolder = new JPanel();
    JPanel borderSouth = new JPanel(new BorderLayout());
    JLabel victoryPointLabel = new JLabel("0", SwingConstants.CENTER);
    JPanel turnBorderPanel = new JPanel();
    JPanel turnHolder = new JPanel(new BorderLayout());
    JCheckBox turnBox = new JCheckBox("");

    //Constructor Variables
    Player player;
    CatanBoard reference;
    TradingFrame tf;

    //MenuBar manipulation
    boolean hasRolled=false;
    boolean loadedSpecialClasses=false;
    boolean playedOneDevCard=false;

    //MenuBar
    JMenuBar mb = new JMenuBar();
    JMenu build = new JMenu("Build");
    JMenuItem settlement = new JMenuItem("Build Settlement");
    JMenuItem city = new JMenuItem("Upgrade Settlement to City");
    JMenuItem road = new JMenuItem("Build Road");
    JMenu development = new JMenu("Development");
    JMenuItem buyCard = new JMenuItem("Buy Development Card");
    JMenuItem playCard = new JMenuItem("Play Development Card");
    JMenu options = new JMenu("Options");
    JMenuItem exchange = new JMenuItem("Trade w/ Other Players");
    JMenuItem fourForOne = new JMenuItem();
    JMenuItem buildingCard = new JMenuItem("Building Cost Card");
    JMenuItem rollDice = new JMenuItem("Roll Dice");
    JMenuItem remainingResources = new JMenuItem("Remaining Materials");
    JMenuItem devCardsRemaining = new JMenuItem("Remaining Development Cards");
    JMenuItem endTurn = new JMenuItem("End Turn");

    //Development Card Prep
    String[] lookAppr = new String[]{"Knight","Monopoly","Road Building","Year of Plenty","Victory Points"};

    //Building Costs Frame
    JFrame costFrame = new JFrame();
    JLabel imageCostLabel = new JLabel("", SwingConstants.CENTER);

    //Highwayman menu
    JMenu hwm = new JMenu("Highwayman");
    JMenuItem steal = new JMenuItem("Steal");
    boolean hasStolen=false;
    ArrayList<Player> possiblePlayers;
    String chosenResource;
    boolean didSteal=false;

    //Assassin menu
    JMenu assassin = new JMenu("Assassin");
    JMenuItem assassinate = new JMenuItem("Assassinate");
    boolean hasKilled=false;
    ArrayList<Player> possibleKills;
    ArrayList<String> removedResources;

    //Arsonist menu
    JMenu arsonist = new JMenu("Arsonist");
    JMenuItem setFire = new JMenuItem("Burn Tile");
    boolean hasSetFire = false;
    ArrayList<Tile> before, after;
    boolean spread=false,putOut=false;

    //Cultivator menu
    JMenu cultivator = new JMenu("Cultivator");
    JMenuItem cultivate = new JMenuItem("Cultivate");
    boolean hasCultivated = false;

    //Pirate Menu
    JMenu pirate = new JMenu("Pirate");
    JMenuItem pillage = new JMenuItem("Pillage");
    boolean hasPillaged=false;
    boolean isPirate;

    //Brewer Menu
    JMenu brewer = new JMenu("Brewer");
    JMenuItem confound = new JMenuItem("Confound");
    boolean hasConfounded=false;

    //Shepherd Menu
    JMenu shepherd = new JMenu("Shepherd");
    JMenuItem sheepify = new JMenuItem("Expand Sheep Plains");
    boolean hasSheepified = false;
    ArrayList<Tile> compatibleTiles;

    //Shepherd Menu
    JMenu woodsman = new JMenu("Woodsman");
    JMenuItem forestExpansion = new JMenuItem("Expand Forests");
    boolean hasForestified = false;

    //Farmer Menu
    JMenu farmer = new JMenu("Farmer");
    JMenuItem farmExpansion = new JMenuItem("Expand Wheat Fields");
    boolean hasFarmed = false;

    //Mouontaineer Menu
    JMenu mountaineer = new JMenu("Mountaineer");
    JMenuItem mountainExpansion = new JMenuItem("Expand Mountains");
    boolean hasMountainified = false;

    //Special Classes
    JCheckBox[] playerNames;
    Player chosenPlayer;

    //Simplification
    boolean isPirateOrSerf;

    //Turn cycles
    boolean singleRandomize=false;
    boolean singleDemocracy=false;
    boolean singleMonarchy=false;
    int diceRoll=0;

    //DevCard Frame
    JFrame devFrame = new JFrame();
    JPanel devImagePanel =new JPanel(new GridLayout(1,5));
    JLabel[] devImages = new JLabel[5];
    String[] devPaths = new String[]{"Knight","Monopoly","Road_Building","Year_Of_Plenty","University"};
    long num;
    int numRepeats;

    //Hashmap String manipulation
    HashMap<String,String> plurals = new HashMap<>();
    String[] pluralStrings = new String[]{"Knights","Monopolies","Road Buildings","Year of Plenties","Victory Points"};
    HashMap<Integer,String> alphaNumeric = new HashMap<>();
    String[] strNums = {"Zero","One","Two","Three","Four","Five","Six","Seven","Eight","Nine","Ten","Eleven","Twelve","Thirteen","Fourteen"};
    int[] actNums = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14};

    //Playing Development Cards
    JFrame playFrame = new JFrame();
    JPanel playImagePanel = new JPanel(new GridLayout(1,5));
    JLabel[] playImages = new JLabel[5];

    //Dice Frame
    JFrame diceOne = new JFrame();
    JFrame diceTwo = new JFrame();
    JLabel diceOneLabel = new JLabel("", SwingConstants.CENTER);
    JLabel diceTwoLabel = new JLabel("", SwingConstants.CENTER);
    int diceOneInt = 0;
    int diceTwoInt = 0;
    boolean revealDiceAsNeeded=false;
    Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
    boolean intermediaryCheck=false;
    Color standard = brickNum.getBackground();

    public PlayerView(){}

    public PlayerView(Player player, CatanBoard reference, TradingFrame tf) {
        this.setUndecorated(true);

        //Relating global variables to class variables
        this.player = player;
        this.reference=reference;
        this.tf=tf;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Arrays.stream(new JMenu[]{build,development,options,hwm,assassin,arsonist,cultivator,pirate,brewer,shepherd,woodsman,mountaineer,farmer}).forEach(JMenu::addSeparator);

        //Menubar creation
        this.setJMenuBar(mb);
        mb.setBorder(reference.compound);
        mb.add(build);
        build.add(road);
        road.addActionListener(this);
        build.add(settlement);
        settlement.addActionListener(this);
        build.add(city);
        city.addActionListener(this);
        mb.add(development);
        development.add(buyCard);
        buyCard.addActionListener(this);
        development.add(playCard);
        playCard.addActionListener(this);
        mb.add(options);
        options.add(rollDice);
        rollDice.addActionListener(this);
        options.add(fourForOne);
        fourForOne.addActionListener(this);
        options.add(exchange);
        exchange.addActionListener(this);
        options.add(buildingCard);
        buildingCard.addActionListener(this);
        options.add(remainingResources);
        options.add(devCardsRemaining);
        devCardsRemaining.addActionListener(this);
        options.add(endTurn);
        endTurn.addActionListener(this);
        options.setEnabled(false);
        development.setEnabled(false);
        build.setEnabled(false);
        remainingResources.setEnabled(false);
        remainingResources.addActionListener(this);

        if(reference.specialActions) {
            //Highwayman
            if (player.getClassTitle().equals("Highwayman")) {
                mb.add(hwm);
                hwm.add(steal);
                hwm.setEnabled(false);
                steal.addActionListener(this);
                steal.setEnabled(false);
            }

            if (player.getClassTitle().equals("Assassin")) {
                mb.add(assassin);
                assassin.add(assassinate);
                assassinate.addActionListener(this);
                assassinate.setEnabled(false);
                assassin.setEnabled(false);
            }

            if(player.getClassTitle().equals("Arsonist")){
                mb.add(arsonist);
                arsonist.add(setFire);
                setFire.addActionListener(this);
                setFire.setEnabled(false);
                arsonist.setEnabled(false);
            }
            if(player.getClassTitle().equals("Cultivator")){
                mb.add(cultivator);
                cultivator.add(cultivate);
                cultivate.addActionListener(this);
                cultivator.setEnabled(false);
                cultivate.setEnabled(false);
            }
            if(player.getClassTitle().equals("Pirate")){
                mb.add(pirate);
                pirate.add(pillage);
                pillage.addActionListener(this);
                pillage.setEnabled(false);
                pirate.setEnabled(false);
            }
            if(player.getClassTitle().equals("Brewer")){
                mb.add(brewer);
                brewer.add(confound);
                confound.addActionListener(this);
                confound.setEnabled(false);
                brewer.setEnabled(false);
            }
            if(player.getClassTitle().equals("Shepherd")){
                mb.add(shepherd);
                shepherd.add(sheepify);
                sheepify.addActionListener(this);
                sheepify.setEnabled(false);
                shepherd.setEnabled(false);
            }
            if(player.getClassTitle().equals("Woodsman")){
                mb.add(woodsman);
                woodsman.add(forestExpansion);
                forestExpansion.addActionListener(this);
                forestExpansion.setEnabled(false);
                woodsman.setEnabled(false);
            }
            if(player.getClassTitle().equals("Mountaineer")){
                mb.add(mountaineer);
                mountaineer.add(mountainExpansion);
                mountainExpansion.addActionListener(this);
                mountainExpansion.setEnabled(false);
                mountaineer.setEnabled(false);
            }
            if(player.getClassTitle().equals("Farmer")){
                mb.add(farmer);
                farmer.add(farmExpansion);
                farmExpansion.addActionListener(this);
                farmExpansion.setEnabled(false);
                farmer.setEnabled(false);
            }
        }

        fourForOne.setText((player.getClassTitle().equals("Pirate")?"One/One Resource Trade [Pirate]":"Four/One Resource Trade"));
        isPirateOrSerf = player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf");

        //Creating the GUI
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.add(graphicHolder, BorderLayout.CENTER);
        this.add(borderNorth,BorderLayout.NORTH);
        borderNorth.setBorder(reference.compound);
        borderNorth.add(infoPanel,BorderLayout.CENTER);
        borderNorth.add(colorDisplayLabel,BorderLayout.WEST);
        borderNorth.add(statusDisplayLabel,BorderLayout.EAST);
        this.add(borderSouth,BorderLayout.SOUTH);
        borderSouth.add(vpPointHolder,BorderLayout.WEST);
        borderSouth.add(turnBorderPanel, BorderLayout.EAST);
        turnBorderPanel.setBorder(reference.compound);
        turnBorderPanel.add(turnHolder);
        turnHolder.add(new JLabel("  "),BorderLayout.WEST);
        turnHolder.add(turnBox,BorderLayout.CENTER);
        turnHolder.add(new JLabel("  "),BorderLayout.EAST);
        turnHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Turn"));
        vpPointHolder.setBorder(reference.compound);
        vpPointHolder.add(victoryPointLabel);
        borderSouth.add(borderInfoPanel,BorderLayout.CENTER);
        turnBox.setEnabled(false);
        turnBox.setToolTipText("This box is checked if it's your turn");
        borderInfoPanel.setBorder(reference.compound);
        borderInfoPanel.add(awardPanel);
        victoryPointLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"VPs"));
        borderNorth.setBackground(Color.white);
        colorDisplayLabel.setBorder(reference.compound);
        statusDisplayLabel.setBorder(reference.compound);
        colorDisplayLabel.setToolTipText("Player Color: "+player.getColor());
        statusDisplayLabel.setToolTipText("Status: Normal");
        infoPanel.add(devPanel);
        infoPanel.setBorder(reference.compound);
        devPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Development Cards"));
        devPanel.add(unplayed);
        unplayed.setBorder(reference.compound);
        devPanel.add(played);
        played.setBorder(reference.compound);
        unplayed.addItem("Hidden Cards");
        played.addItem("Played Cards");
        victoryPointLabel.setFont(new Font(victoryPointLabel.getFont().getName(),Font.PLAIN,16));

        if(player.getClassTitle().equals("Assassin"))
            options.remove(exchange);

        awardPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Awards"));
        awardPanel.add(longestRoadBox);
        longestRoadBox.setBorderPainted(true);
        longestRoadBox.setEnabled(false);
        awardPanel.add(largestArmyBox);
        largestArmyBox.setBorderPainted(true);
        largestArmyBox.setEnabled(false);
        largestArmyBox.addMouseMotionListener(this);
        longestRoadBox.addMouseMotionListener(this);
        longestRoadBox.setToolTipText("Awarded to the player who has the longest continuous road that exceeds 4 separate segments");
        largestArmyBox.setToolTipText("Awarded to the player who has played the most 'Knight' DCs that exceeds 2 separate cards");
        victoryPointLabel.setToolTipText("Your victory point total; get to 10 and you win");

        //Code for implementation
        colorDisplayLabel.setIcon(new ImageIcon("Pieces/Large_Icons/"+player.getColor()+"_City_Large.png"));
        statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Normal_"+player.getColor()+".png"));

        unplayed.setEnabled(player.isTurn());

        //Array manipulation stuff
        for (int x = 0; x < 5; x++) {
            graphicPanels[x] = new JPanel(new BorderLayout());
            graphicImageLabels[x] = new JLabel("", SwingConstants.CENTER);
            graphicImageLabels[x].setIcon(new ImageIcon("Resources/" + graphicStrings[x] + "_Image.png"));
            graphicPanels[x].add(graphicImageLabels[x], BorderLayout.CENTER);
            graphicImageLabels[x].setBorder(reference.compound);
            graphicHolder.add(graphicPanels[x]);
        }
        graphicPanels[0].add(brickNum, BorderLayout.SOUTH);
        brickNum.setBorder(reference.compound);
        graphicPanels[1].add(oreNum, BorderLayout.SOUTH);
        oreNum.setBorder(reference.compound);
        graphicPanels[2].add(sheepNum, BorderLayout.SOUTH);
        sheepNum.setBorder(reference.compound);
        graphicPanels[3].add(wheatNum, BorderLayout.SOUTH);
        wheatNum.setBorder(reference.compound);
        graphicPanels[4].add(woodNum, BorderLayout.SOUTH);
        woodNum.setBorder(reference.compound);

        Arrays.stream(new JMenu[]{build,development,options,hwm,assassin,arsonist,cultivator,brewer,pirate,shepherd,woodsman,mountaineer,farmer}).forEach(JMenu::addSeparator);

        pluralInitialization();
        initializeCostFrame();
        initializeDevCardFrame();
        initializePlayFrame();
        initializeDiceFrame();
    }

    public void pluralInitialization(){
        for(int x=0; x<pluralStrings.length; x++)
            plurals.put(lookAppr[x],pluralStrings[x]);
    }

    public void initializeDiceFrame(){
        Arrays.stream(new JFrame[]{diceOne,diceTwo}).forEach(frame->{
            frame.setUndecorated(true);
            frame.setSize(75,75);
        });

        diceOne.add(diceOneLabel);
        diceTwo.add(diceTwoLabel);
        diceOneLabel.setBorder(reference.compound);
        diceTwoLabel.setBorder(reference.compound);
    }

    public void update(){
        brickNum.setText((!player.isTurn()?"-":""+player.getBrickNum()));
        oreNum.setText((!player.isTurn()?"-":""+player.getOreNum()));
        sheepNum.setText((!player.isTurn()?"-":""+player.getWoolNum()));
        wheatNum.setText((!player.isTurn()?"-":""+player.getGrainNum()));
        woodNum.setText((!player.isTurn()?"-":""+player.getLumberNum()));
        largestArmyBox.setSelected(player.hasLargestArmy());
        longestRoadBox.setSelected(player.hasLongestRoad());
        victoryPointLabel.setText("   "+player.getVictoryPointTotal()+"   ");

        Arrays.stream(new JLabel[]{brickNum,oreNum,sheepNum,wheatNum,woodNum}).forEach(label -> label.setBackground(standard));

        unplayed.setEnabled(player.isTurn());
        longestRoadBox.setSelected(player.hasLongestRoad());
        largestArmyBox.setSelected(player.hasLargestArmy());
        turnBox.setSelected(player.isTurn());

        //Workaround for revalidate() / invalidate() validate()
        this.setSize(this.getWidth()+1,this.getHeight());
        this.setSize(this.getWidth()-1,this.getHeight());

        if(player.isTurn() && !reference.doingStartup)
            if(hasRolled)
                afterRoll();
            else
                startTurn();
    }
    
    public void initializePlayFrame(){
        playFrame.add(playImagePanel);
        playFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        playFrame.setBounds(devFrame.getBounds());
        playFrame.setTitle("Development Card Frame");

        for(int x=0; x<5; x++){
            playImages[x]= new JLabel("", SwingConstants.CENTER);
            playImages[x].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            playImages[x].setIcon(new ImageIcon("Resources/Preview_Images/DevCardsResized/"+devPaths[x]+".png"));
            playImages[x].addMouseListener(this);
            playImagePanel.add(playImages[x]);
        }
        playFrame.revalidate();
    }

    public void initializeDevCardFrame(){
        devFrame.add(devImagePanel);
        devFrame.setBounds(100,100,820,270);
        devFrame.setTitle("Development Cards Remaining");

        for(int x=0; x<5; x++){
            devImages[x]= new JLabel("", SwingConstants.CENTER);
            devImages[x].setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
            devImages[x].setIcon(new ImageIcon("Resources/Preview_Images/DevCardsResized/"+devPaths[x]+".png"));
            devImages[x].addMouseListener(this);
            devImagePanel.add(devImages[x]);
        }

        for(int x=0; x<actNums.length; x++)
            alphaNumeric.put(actNums[x],strNums[x].toLowerCase());

        devFrame.revalidate();
    }

    public void startTurn() {
        loadedSpecialClasses=false;
        if(reference.wildfire){
            before = Arrays.stream(reference.tiles).filter(tile -> tile.isOnFire() && tile.getFirePlayer().equals(player)).collect(Collectors.toCollection(ArrayList::new));
            for(Tile tile:before){
                for(Tile tileBase: reference.tiles){
                    if(reference.distance(tile.getCenter(),tileBase.getCenter())<200 && !tileBase.isOnFire() && new Random().nextInt(100)<5 && !tileBase.equals(tile)){
                        tileBase.setOnFire(true);
                        tileBase.setFirePlayer(player);
                        spread=true;
                    }
                }
                tile.setOnFire(false);
                tile.setFirePlayer(new Player());
                putOut=true;
            }
            after = Arrays.stream(reference.tiles).filter(tile -> tile.isOnFire() && tile.getFirePlayer().equals(player)).collect(Collectors.toCollection(ArrayList::new));

            if(!before.equals(after)) {
                reference.redrawEverything = true;
                reference.repaint();
                JOptionPane.showMessageDialog(this,(!putOut && spread)?"The fire has spread from the original site, which has died down.":"The original fire has died down.","Arson Results",1, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else {
            if (Arrays.stream(reference.tiles).anyMatch(tile -> tile.isOnFire() && tile.getFirePlayer().equals(player))) {
                Arrays.stream(reference.tiles).filter(tile -> tile.isOnFire() && tile.getFirePlayer().equals(player)).forEach(tile -> {
                    tile.setFirePlayer(new Player());
                    tile.setOnFire(false);
                });
                reference.redrawEverything = true;
                reference.repaint();
            }
        }
        if(Arrays.stream(reference.tiles).anyMatch(tile -> tile.isCultivated() && tile.getCultivatingPlayer().equals(player))){
            Arrays.stream(reference.tiles).filter(tile -> tile.isCultivated() && tile.getCultivatingPlayer().equals(player)).forEach(tile -> {
                tile.setCultivatingPlayer(new Player());
                tile.setCultivated(false);
            });
            reference.redrawEverything = true;
            reference.repaint();
            JOptionPane.showMessageDialog(this,"The cultivated tile has returned to normal.","Normal Cultivation",1,new ImageIcon("Resources/Catan_Icon.png"));
        }

        if(!singleRandomize)
            if(reference.randomize && !reference.doingStartup) {
                singleRandomize = true;
                reference.randomize();
            }

        if(!singleDemocracy && !reference.doingStartup)
            if (reference.turnNameList.get(0).equals(this.player.getName()) && reference.democracy) {
                singleDemocracy=true;
                reference.performDemocracyVoting();
            }

        if(!singleMonarchy && !reference.doingStartup)
            if (reference.turnNameList.get(0).equals(this.player.getName()) && reference.monarchy) {
                singleMonarchy=true;
                reference.performMonarchSelection();
            }

        resetReference(true);
        rollDice.setEnabled(true);
        playedOneDevCard=false;
        Arrays.stream(new JMenuItem[]{settlement,city,road,buildingCard,buyCard,playCard,exchange,fourForOne,endTurn,steal,assassinate,remainingResources,devCardsRemaining,setFire,cultivate,confound,sheepify,mountainExpansion,farmExpansion,forestExpansion}).forEach(item -> item.setEnabled(false));
        Arrays.stream(new Boolean[]{hasStolen,hasKilled,didSteal,hasSetFire,hasCultivated,hasConfounded}).forEach(bool -> bool=false);

        if(player.isDrunk)
            JOptionPane.showMessageDialog(this,"You are confounded, so some of your actions this turn have a 50% chance of failing.","Confounded Warning",1,new ImageIcon("Resources/Catan_Icon.png"));
    }

    public void afterRoll() {
        playCard.setEnabled(!playedOneDevCard);
        Arrays.stream(new JMenuItem[]{settlement,city,road,buildingCard,buyCard,exchange,fourForOne,endTurn,remainingResources,devCardsRemaining}).forEach(item -> item.setEnabled(true));
        if(!loadedSpecialClasses){
            Arrays.stream(new JMenuItem[]{steal,assassinate,setFire,cultivate,confound}).forEach(item->item.setEnabled(true));
            pillage.setEnabled(!hasPillaged && reference.usablePorts);
            sheepify.setEnabled(!hasSheepified);
            forestExpansion.setEnabled(!hasForestified);
            mountainExpansion.setEnabled(!hasMountainified);
            farmExpansion.setEnabled(!hasFarmed);
            loadedSpecialClasses=true;
        }
        rollDice.setEnabled(false);
    }

    public void initializeCostFrame(){
        costFrame.add(imageCostLabel);
        imageCostLabel.setIcon(new ImageIcon("Resources/Building_Costs.png"));
        imageCostLabel.addMouseListener(this);

        costFrame.setBounds((int)this.getBounds().getX(),(int)this.getBounds().getY(),291,357);
        costFrame.setUndecorated(true);
    }

    public ArrayList<Player> getOtherPlayers(){
        return reference.catanPlayerList.stream().filter(players -> !players.equals(this.player)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource()==settlement) {
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to create a new settlement.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                settlement.setEnabled(false);
                return;
            }
            if (player.getSettlements()==0) {
                JOptionPane.showMessageDialog(this, "You no longer have settlements available to build with.", "Settlement Limit Reached", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            int settlementInput = JOptionPane.showConfirmDialog(this,"Would you like to create a new settlement?","Settlement Building",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(settlementInput==0){
                if((player.getBrickNum()>=1 && player.getLumberNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getBrickNum()>=2 && player.getLumberNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    reference.isPlayerActing=true;
                    JOptionPane.showMessageDialog(this,"Select the index you'd like to build a new settlement on.","Settlement Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    reference.isSettlementBuilding=true;
                    resetReference(false);

                    player.monoBrick(isPirateOrSerf?-2:-1);
                    player.monoWheat(isPirateOrSerf?-2:-1);
                    player.monoLumber(isPirateOrSerf?-2:-1);
                    player.monoWool(isPirateOrSerf?-2:-1);
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a new settlement.","Settlement Building Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==fourForOne) {
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to exchange resources through a 4:1 trade.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                fourForOne.setEnabled(false);
                return;
            }
            if((!player.getClassTitle().equals("Pirate") && player.getLumberNum()<4 && player.getBrickNum()<4 && player.getWoolNum()<4 && player.getGrainNum()<4 && player.getOreNum()<4) || (player.getClassTitle().equalsIgnoreCase("Pirate") && player.returnTotalResources()==0))
                JOptionPane.showMessageDialog(this,"You don't have sufficient resources to do a trade of this kind.","Insufficient Resources", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            else{
                if (player.isInDebt()) {
                    JOptionPane.showMessageDialog(this, "You are in debt. You cannot make 4:1 exchanges until you have all non-negative resource values.", "In-Debt Player", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    return;
                }
                isPirate = player.getClassTitle().equalsIgnoreCase("Pirate");
                int confirm = JOptionPane.showConfirmDialog(this,isPirate?"Would you like to trade in one resource for another one resource?":"Would you like to trade in four of a resource for one of another resource?","Generic Resource Exchange",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
                if(confirm==JOptionPane.YES_OPTION) {
                    try {
                        String exchangeResource = "";
                        while (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat")) {
                            exchangeResource = (String)JOptionPane.showInputDialog(this, "Type in the resource you'd like to exchange in: Sheep - Lumber - Ore - Brick - Wheat", "Resource Exchange", JOptionPane.QUESTION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"),null,null);

                            if (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat"))
                                JOptionPane.showMessageDialog(this, "That is not one of the resource choices. Please choose again.", "Invalid Resource", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                            if (!isPirate && (exchangeResource.equalsIgnoreCase("Sheep") && player.getWoolNum() < 4) || !isPirate && (exchangeResource.equalsIgnoreCase("Lumber") && player.getLumberNum() < 4) || !isPirate && (exchangeResource.equalsIgnoreCase("Ore") && player.getOreNum() < 4) || !isPirate && (exchangeResource.equalsIgnoreCase("Bric") && player.getBrickNum() < 4) || !isPirate && (exchangeResource.equalsIgnoreCase("Wheat") && player.getGrainNum() < 4)) {
                                JOptionPane.showMessageDialog(this, "You do not have at least four of that resource to complete an exchange.", "Invalid Resource", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                                exchangeResource = "";
                            }
                        }
                        JCheckBox[] optionResources = new JCheckBox[]{new JCheckBox("Brick"), new JCheckBox("Ore"), new JCheckBox("Sheep"), new JCheckBox("Wheat"), new JCheckBox("Lumber")};
                        for (JCheckBox optionResource : optionResources)
                            if (optionResource.getText().equalsIgnoreCase(exchangeResource))
                                optionResource.setEnabled(false);
                        while (findNumSelected(optionResources)) {
                            JOptionPane.showMessageDialog(this, new Object[]{"Select the resource you'd like to exchange for:", optionResources}, "Exchanging Resources", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                            if (findNumSelected(optionResources))
                                JOptionPane.showMessageDialog(this, "You must select a single resource to trade in for.", "Invalid Selection", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        try {
                            numRepeats = Integer.parseInt((String) JOptionPane.showInputDialog(this, "How many times would you like to make this resource exchange?", "Resource Exchange Repetition", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null));

                            if(!canRepeat(numRepeats,exchangeResource))
                                JOptionPane.showMessageDialog(this,"You cannot make that exchange "+alphaNumeric.get(numRepeats) +" times. It will be done a single time by default.","Resource Exchange Denial",1, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        catch(Exception f){
                            numRepeats = 1;
                            JOptionPane.showMessageDialog(this,"That is not an acceptable value. You will make the exchange a single time.","Resource Exchange Denial",1, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        int reps = (canRepeat(numRepeats,exchangeResource)?numRepeats:1);
                        if (optionResources[0].isSelected())
                            player.monoBrick(reps);
                        else if (optionResources[1].isSelected())
                            player.monoOre(reps);
                        else if (optionResources[2].isSelected())
                            player.monoWool(reps);
                        else if (optionResources[3].isSelected())
                            player.monoWheat(reps);
                        else if (optionResources[4].isSelected())
                            player.monoLumber(reps);

                        if (exchangeResource.equalsIgnoreCase("Sheep"))
                            player.monoWool(reps*(isPirate ? -1 : -4));
                        else if (exchangeResource.equalsIgnoreCase("Lumber"))
                            player.monoLumber(reps*(isPirate ? -1 : -4));
                        else if (exchangeResource.equalsIgnoreCase("Brick"))
                            player.monoBrick(reps*(isPirate ? -1 : -4));
                        else if (exchangeResource.equalsIgnoreCase("Ore"))
                            player.monoOre(reps*(isPirate ? -1 : -4));
                        else if (exchangeResource.equalsIgnoreCase("Wheat"))
                            player.monoWheat(reps*(isPirate ? -1 : -4));

                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Exchange Complete", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NullPointerException cancelCaught) {
                        JOptionPane.showMessageDialog(this, "An invalid input has been received. This operation has been cancelled", "Cancellation", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                }
            }
        }

        else if(e.getSource()==city){
            if(Arrays.stream(reference.indexes).noneMatch(index -> index.isSettlement() && index.isTaken() && index.getOwner().equals(reference.getCurrentPlayer()))){
                JOptionPane.showMessageDialog(this, "You don't have any settlements to upgrade.", "No Settlements Available to Upgrade", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to upgrade a settlement into a city.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                city.setEnabled(false);
                return;
            }
            if(player.getCities()==0) {
                JOptionPane.showMessageDialog(this, "You no longer have cities available to upgrade with.", "City Limit Reached", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            int cityInput = JOptionPane.showConfirmDialog(this,"Would you like to upgrade one of your settlements into a city?","Settlement Upgrade",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));

            if(cityInput==0){
                if((player.getGrainNum()>=2 && player.getOreNum()>=3 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getGrainNum()>=4 && player.getOreNum()>=6 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    if(playerHasSettlements()) {
                        reference.isPlayerActing=true;
                        JOptionPane.showMessageDialog(this, "Select the settlement you'd like to upgrade.", "City Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        reference.isCityUpgrading = true;
                        resetReference(false);

                        player.monoOre(isPirateOrSerf?-6:-3);
                        player.monoWheat(isPirateOrSerf?-4:-2);

                        update();
                    }
                    else
                        JOptionPane.showMessageDialog(this,"You have no settlements left to upgrade into cities.","Failure To Find Settlement", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to upgrade a settlement.","City Building Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==road){
            if(player.getRoads()==0) {
                JOptionPane.showMessageDialog(this, "You no longer have roads available to build with.", "Road Limit Reached", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to build a road.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                road.setEnabled(false);
                return;
            }

            int roadInput = JOptionPane.showConfirmDialog(this,"Would you like to create a road?","Road Building",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(roadInput==0){
                if((player.getBrickNum()>=1 && player.getLumberNum()>=1 && !player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf")) || (player.getBrickNum()>=2 && player.getLumberNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))) {
                    reference.isPlayerActing=true;
                    JOptionPane.showMessageDialog(this,"Choose the two indices you'd like to build a road between.","Road Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    reference.isRoadBuilding=true;
                    resetReference(false);

                    player.monoBrick(isPirateOrSerf?-2:-1);
                    player.monoLumber(isPirateOrSerf?-2:-1);

                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a road.","Road Building Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==buyCard){
            if(reference.devCardDeck.size()==0){
                JOptionPane.showMessageDialog(this,"There are no development cards left.","Development Card Deck Empty",1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to buy a development card.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                buyCard.setEnabled(false);
                return;
            }

            if(JOptionPane.showConfirmDialog(this,"Would you like to draw a development card?","Development Card Draw",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))==0){
                if((player.getOreNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && !player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf")) || (player.getOreNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    DevelopmentCard newDc = reference.drawDevelopmentCard();
                    newDc.setPlayer(this.player);
                    newDc.setOtherPlayers(getOtherPlayers());
                    JOptionPane.showMessageDialog(this,"You have purchased a development card."+((reference.devCardDeck.size()>0)?" There "+((reference.devCardDeck.size()==1)?"is ":"are ")+reference.devCardDeck.size()+" cards remaining.":" There are now no more development cards."),"Development Card", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    player.addDevelopmentCardToUnplayed(newDc);
                    unplayed.addItem(newDc.getType());
                    readdDevCards(unplayed);
                    player.monoWool(isPirateOrSerf?-2:-1);
                    player.monoOre(isPirateOrSerf?-2:-1);
                    player.monoWheat(isPirateOrSerf?-2:-1);
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to purchase a development card.","Cannot Purchase Development Card", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==playCard) {
            if (player.getUnPlayedCards().size() == 0) {
                JOptionPane.showMessageDialog(this, "You don't have any development cards.", "No Development Cards Available", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to play a development card.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                playCard.setEnabled(false);
                return;
            }
            if (player.getUnPlayedCards().stream().allMatch(DevelopmentCard::isBoughtThisTurn)){
                 JOptionPane.showMessageDialog(this, "All of your development cards have been purchased this turn. You must wait at least one turn to play one.", "All Development Cards Bought This Turn", 1, new ImageIcon("Resources/Catan_Icon.png"));
                 return;
            }
            if(JOptionPane.showConfirmDialog(this,"Would you like to play a development card?","Playing Development Card",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))!=JOptionPane.YES_OPTION)
                return;

            enableAppropriateDevCardImages();
            resetReference(false);
            playFrame.setVisible(true);
            JOptionPane.showMessageDialog(this,"Select the card you'd like to play.","Development Card Selection",1, new ImageIcon("Resources/Catan_Icon.png"));
        }

        else if(e.getSource()==exchange){
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed this action.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                exchange.setEnabled(false);
                return;
            }
            if(tf.isVisible()) {
                JOptionPane.showMessageDialog(this, "You already have your trading frame open.", "Already Open", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if (player.isInDebt()) {
                JOptionPane.showMessageDialog(this, "You are in debt. You cannot trade with other players until you have all non-negative resource values.", "In-Debt Player", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(player.returnTotalResources()==0){
                JOptionPane.showMessageDialog(this, "You have no resources. You cannot trade with other players until you have at least one of a single resource.", "No Resources To Trade With", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            if(reference.getOtherPlayers().stream().allMatch(player -> player.returnTotalResources() == 0)){
                JOptionPane.showMessageDialog(this, "No other players have resources. You cannot trade currently.", "All Other Players Have No Resources", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            reference.showBuiltImage("Resources/Preview_Images/Trade.png", "Resource Exchange");
            this.tf.setVisible(true);
            this.tf.updateComboBoxes();
        }

        else if(e.getSource()==endTurn){
            Arrays.stream(new Boolean[]{singleRandomize,singleDemocracy,singleMonarchy}).forEach(bool->bool=false);
            int cataclysmOccurrence = 69;
            if(reference.cataclysmsActive){
                cataclysmOccurrence = new Random().nextInt(20);
                if(cataclysmOccurrence==0)
                    reference.cataclysm();
            }
            if(player.isDrunk){
                statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/"+(player.isLeader()?"Leader_":"Normal_")+player.getColor()+".png"));
                statusDisplayLabel.setToolTipText("Status: "+(player.isLeader()?"Leader":"Normal"));
                JOptionPane.showMessageDialog(this,"You sober up and are no longer confounded.","Sobering Up",1, new ImageIcon("Resources/Catan_Icon.png"));
                player.isDrunk=false;
            }

            ArrayList<Player> turnOrder = new ArrayList<>();
            String name="";

            for(int x=0; x<reference.catanPlayerList.size(); x++)
                turnOrder.add(reference.turnOrder(reference.turnNameList).get(x));

            for(int x=0; x<turnOrder.size(); x++)
                if(turnOrder.get(x).getName().equals(this.player.getName()))
                    try {
                        turnOrder.get((x + 1) % turnOrder.size()).setTurn(true);
                        name=turnOrder.get((x+1)%turnOrder.size()).getName();
                    }
                    catch(IndexOutOfBoundsException f){
                        turnOrder.get(0).setTurn(true);
                        name=turnOrder.get(0).getName();
                    }

            Arrays.stream(new JFrame[]{costFrame,playFrame,devFrame}).forEach(frame->frame.setVisible(false));

            if((cataclysmOccurrence!=0 && reference.cataclysmsActive) || cataclysmOccurrence==69)
                JOptionPane.showMessageDialog(this,"You have passed the turn. "+name+", it is now your turn.","Ending the Turn", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

            this.player.setTurn(false);
            this.turnBox.setSelected(false);
            resetReference(false);
            hasRolled=false;

            for(int x=0; x<player.getUnPlayedCards().size(); x++)
                player.getUnPlayedCards().get(x).setBoughtThisTurn(false);
            tf.rejections.clear();
            tf.setVisible(false);
            unplayed.setSelectedIndex(0);
            played.setSelectedIndex(0);
            update();

            if(reference.razing)
                reference.razeTiles();
        }
        else if(e.getSource()==rollDice){
            diceRoll=0;
            revealDiceAsNeeded=false;
            if(this.player.isLeader() || this.player.getClassTitle().equals("Deity")){
                while(diceRoll==0) {
                    try {
                        diceRoll = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Select the number you'd like to roll this turn: ", "Democracy Action", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null));

                        if (diceRoll < 2 || diceRoll > 12)
                            throw new Exception();
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(this, "You must select an integer between 2 and 12.", "Improper Number Choice", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        diceRoll = 0;
                    }
                    revealDiceAsNeeded=true;
                }
            }

            else {
                diceOneInt = new Random().nextInt(6) + 1;
                diceTwoInt = new Random().nextInt(6) + 1;
                diceRoll = diceOneInt + diceTwoInt;
            }

            if(reference.gamblerIsPresent()){
                if(new Random().nextInt(100) < 20) {
                    JOptionPane.showMessageDialog(this,"All gamblers made poor bets; each loses one of every resource.","Bad Gamble",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
                    reference.catanPlayerList.stream().filter(player -> player.getClassTitle().equalsIgnoreCase("Gambler")).forEach(Player::failGamble);
                }
            }
            if(!revealDiceAsNeeded)
                showDiceFrames(diceOneInt,diceTwoInt);
            intermediaryCheck=reference.resourceValsGiven(diceRoll);
            JOptionPane.showMessageDialog(null,((diceRoll!=7)?((intermediaryCheck)?"No players receive resources this turn.":"Resources are being distributed now."):"A 7 has been rolled. Click a tile the robber will be moved to."),this.player.getName()+" rolls a "+diceRoll, JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            hideDice();
            if(diceRoll==7) {
                resetReference(false);
                reference.isMovingRobber = true;
            }

            else
                reference.giveOutResources(diceRoll);
            hasRolled=true;
            update();
        }

        else if(e.getSource()==buildingCard){
            costFrame.setVisible(!costFrame.isVisible());
        }

        else if(e.getSource()==steal){
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to steal.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                steal.setEnabled(false);
                return;
            }
            possiblePlayers = reference.catanPlayerList.stream().filter(player -> !player.getClassTitle().equals("Highwayman") && !player.equals(this.player)).collect(Collectors.toCollection(ArrayList::new));
            if(possiblePlayers.size()==0)
                JOptionPane.showMessageDialog(this,"There are no players who you can steal from.","Steal Unsuccessful", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));

            else{
                try{
                    if(JOptionPane.showConfirmDialog(this,"Would you like to attempt to steal from another player?","Highwayman Special Action",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))!=JOptionPane.YES_OPTION)
                        return;

                    reference.showBuiltImage("Resources/Preview_Images/Steal.jpg","Stealing from Opponents");
                    playerNames = new JCheckBox[possiblePlayers.size()];
                    for(int x=0; x<playerNames.length; x++)
                        playerNames[x] = new JCheckBox(possiblePlayers.get(x).getName());

                    while(findNumSelected(playerNames)){
                        JOptionPane.showMessageDialog(this,new Object[]{"Select the player you would like to steal from: ",playerNames},"Highwayman Special Action",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));

                        if(findNumSelected(playerNames))
                            JOptionPane.showMessageDialog(this,"You must select one player to steal from.","Highwayman Special Action",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
                    }
                    chosenPlayer = findPlayerMatch(playerNames);

                    chosenResource = "";
                    while(!chosenResource.equalsIgnoreCase("Sheep") && !chosenResource.equalsIgnoreCase("Wheat") && !chosenResource.equalsIgnoreCase("Lumber") && !chosenResource.equalsIgnoreCase("Ore") && !chosenResource.equalsIgnoreCase("Brick")) {
                        chosenResource = (String) JOptionPane.showInputDialog(this, "Choose the resource you want to attempt to steal: Sheep, Wheat, Lumber, Ore, Brick", "Highwayman Special Action", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null);

                        if(!chosenResource.equalsIgnoreCase("Sheep") && !chosenResource.equalsIgnoreCase("Wheat") && !chosenResource.equalsIgnoreCase("Lumber") && !chosenResource.equalsIgnoreCase("Ore") && !chosenResource.equalsIgnoreCase("Brick"))
                            JOptionPane.showMessageDialog(this,"That is not one of the specified resources.","Incorrect Resource Title",1,new ImageIcon("Resources/Catan_Icon.png"));

                    }

                    //Stealing Process
                    if(chosenResource.equalsIgnoreCase("Sheep"))
                        if(chosenPlayer.getWoolNum()>0) {
                            didSteal=true;
                            chosenPlayer.monoWool(-1);
                            this.player.monoWool(1);
                        }

                    if(chosenResource.equalsIgnoreCase("Wheat"))
                        if(chosenPlayer.getGrainNum()>0) {
                            didSteal=true;
                            chosenPlayer.monoWheat(-1);
                            this.player.monoWheat(1);
                        }

                    if(chosenResource.equalsIgnoreCase("Brick"))
                        if(chosenPlayer.getBrickNum()>0) {
                            didSteal=true;
                            chosenPlayer.monoBrick(-1);
                            this.player.monoBrick(1);
                        }

                    if(chosenResource.equalsIgnoreCase("Ore"))
                        if(chosenPlayer.getOreNum()>0) {
                            didSteal=true;
                            chosenPlayer.monoOre(-1);
                            this.player.monoOre(1);
                        }

                    if(chosenResource.equalsIgnoreCase("Lumber"))
                        if(chosenPlayer.getLumberNum()>0) {
                            didSteal=true;
                            chosenPlayer.monoLumber(-1);
                            this.player.monoLumber(1);
                        }
                    JOptionPane.showMessageDialog(this,((didSteal)?"You've successfully stolen "+chosenResource.toLowerCase()+" from "+chosenPlayer.getName()+".":chosenPlayer.getName()+" did not have any "+chosenResource.toLowerCase()+" to steal. You gain nothing."),((didSteal)?"Successfully Stole":"Unsuccessful Attempt"),1,new ImageIcon("Resources/Catan_Icon.png"));
                    hasStolen=true;
                    steal.setEnabled(false);
                }
                catch(NullPointerException f){
                    JOptionPane.showMessageDialog(this,"You have exited out of the stealing screen. Your highwayman action has been cancelled.","Improper Action",1, new ImageIcon("Resources/Catan_Icon.png"));
                }
            }
        }
        else if(e.getSource()==assassinate) {
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to assassinate.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                assassinate.setEnabled(false);
                return;
            }
            if(numNonNullCategories()>=2) {
                possibleKills = reference.catanPlayerList.stream().filter(player -> !player.getClassTitle().equals("Assassin") && reference.getPlayerStatusMenu(player).getCardNames().contains("Knight")).collect(Collectors.toCollection(ArrayList::new));
                if (possibleKills.size() == 0)
                    JOptionPane.showMessageDialog(this, "None of the other players have played a knight card. You cannot assassinate this turn.", "Failed Assassination", 1, new ImageIcon("Resources/Catan_Icon.png"));
                else {
                    if(JOptionPane.showConfirmDialog(this,"Would you like to assassinate another player's knight?","Assassin Special Action",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))!=JOptionPane.YES_OPTION)
                        return;

                    playerNames = new JCheckBox[possibleKills.size()];
                    for (int x = 0; x < playerNames.length; x++)
                        playerNames[x] = new JCheckBox(possibleKills.get(x).getName());

                    while (findNumSelected(playerNames)) {
                        JOptionPane.showMessageDialog(this, new Object[]{"Select the player you want to remove a knight from: ", playerNames}, "Assassin Special Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                        if (findNumSelected(playerNames))
                            JOptionPane.showMessageDialog(this, "You must select one player to remove a knight from.", "Assassin Special Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                    chosenPlayer = findPlayerMatch(playerNames);
                    chosenPlayer.removeDevelopmentCardFromPlayed(new DevelopmentCard("Knight", chosenPlayer, reference.genericGetOtherPlayers(chosenPlayer), reference, false));
                    reference.getPlayerStatusMenu(chosenPlayer).played.removeItem("Knight");

                    if (reference.largestArmyPlayer.equals(chosenPlayer))
                        reference.currentLargestArmy -= 1;

                    for (Player player : reference.catanPlayerList)
                        reference.largestArmy(player);

                    removedResources = assassinateResources();
                    reference.showBuiltImage("Resources/Preview_Images/Assassinate.jpg", "Assassination");
                    JOptionPane.showMessageDialog(this, "You've assassinated " + chosenPlayer.getName() + "'s knight at the cost of one " + removedResources.get(0) + " and one " + removedResources.get(1) + ".", "Assassination Complete", 1, new ImageIcon("Resources/Catan_Icon.png"));

                    hasKilled = true;
                    assassinate.setEnabled(false);
                }
            }
            else
                JOptionPane.showMessageDialog(this,"You must have at least one of each of two distinct resources to assassinate another player's knight.","Assassination Requirements Not Met",3,new ImageIcon("Resources/Catan_Icon.png"));
        }
        else if(e.getSource()==setFire){
            if(numNonNullCategories()==0){
                JOptionPane.showMessageDialog(this,"You have no resources. You cannot use the 'arsonist' special ability.","No Resources",1,new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()) {
                JOptionPane.showMessageDialog(this, "You are confounded and have failed to set fire to a tile.", "Action Failed", 1, new ImageIcon("Resources/Catan_Icon.png"));
                setFire.setEnabled(false);
                return;
            }

            int confirmFire = JOptionPane.showConfirmDialog(this,"Would you like to set fire to a tile?","Arson Special Ability", JOptionPane.YES_NO_OPTION,1, new ImageIcon("Resources/Catan_Icon.png"));
            if(confirmFire==JOptionPane.YES_OPTION){
                resetReference(false);
                reference.showBuiltImage("Resources/Preview_Images/Arson.png","Arson Special Action");
                JOptionPane.showMessageDialog(this,"Select the tile you'd like to set fire to.","Arson Special Ability", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                reference.isPlayerActing=true;
                reference.isSettingFire=true;
                hasSetFire=true;
                setFire.setEnabled(false);
            }
        }
        else if(e.getSource()==cultivate){
            if(numNonNullCategories()==0){
                JOptionPane.showMessageDialog(this,"You have no resources. You cannot use the 'cultivator' special ability.","No Resources",1,new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }

            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to cultivate.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                cultivate.setEnabled(false);
                return;
            }

            if(JOptionPane.showConfirmDialog(this,"Would you like to double production on a tile?","Cultivator Special Ability", JOptionPane.YES_NO_OPTION,1, new ImageIcon("Resources/Catan_Icon.png"))!=JOptionPane.YES_OPTION)
                return;

            resetReference(false);
            reference.showBuiltImage("Resources/Preview_Images/Cultivate.png","Cultivator Special Action");
            JOptionPane.showMessageDialog(this,"Select the tile you'd like to cultivate.","Cultivator Special Ability", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            reference.isCultivating=true;
            hasCultivated=true;
            cultivate.setEnabled(false);

        }

        else if(e.getSource() == devCardsRemaining)
            devFrame.setVisible(!devFrame.isVisible());

        else if(e.getSource()==pillage){
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to pillage.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                pillage.setEnabled(false);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Would you like to pillage a port?", "Settlement Building", JOptionPane.YES_NO_OPTION, 1, new ImageIcon("Resources/Catan_Icon.png")) != JOptionPane.YES_OPTION)
                return;

            resetReference(false);
            reference.isPortDestroying=true;
            reference.showBuiltImage("Resources/Preview_Images/Pillage.png","Pirate Special Action");
            JOptionPane.showMessageDialog(this,"Select a port to pillage and destroy.","Pirate Pillaging",1, new ImageIcon("Resources/Catan_Icon.png"));
            pillage.setEnabled(false);
        }

        else if(e.getSource()==confound) {
            if(player.getGrainNum()<3){
                JOptionPane.showMessageDialog(this,"You do not have the necessary three wheat to create the alcohol to confound another player.","Inadequate Resources",1,new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to confound another player.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                confound.setEnabled(false);
                return;
            }
            if (JOptionPane.showConfirmDialog(this, "Would you like to confound a player?", "Confound Action", JOptionPane.YES_NO_OPTION, 1, new ImageIcon("Resources/Catan_Icon.png")) != JOptionPane.YES_OPTION)
                return;
            reference.showBuiltImage("Resources/Preview_Images/Confound.png", "Player Confounding");

            possiblePlayers = reference.catanPlayerList.stream().filter(players -> !players.getName().equals(player.getName())).collect(Collectors.toCollection(ArrayList::new));
            playerNames = new JCheckBox[possiblePlayers.size()];
            for (int x = 0; x < playerNames.length; x++)
                playerNames[x] = new JCheckBox(possiblePlayers.get(x).getName());

            while(findNumSelected(playerNames)){
                JOptionPane.showMessageDialog(this,new Object[]{"Select the player you would like to confound: ",playerNames},"Brewer Special Action",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));

                if(findNumSelected(playerNames))
                    JOptionPane.showMessageDialog(this,"You must select one player to confound.","Brewer Special Action",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
            }
            chosenPlayer = findPlayerMatch(playerNames);
            if(chosenPlayer.isDrunk){
                JOptionPane.showMessageDialog(this,"That player is already drunk.","Brewer Special Action",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            chosenPlayer.isDrunk=true;

            if(chosenPlayer.isLeader()) {
                reference.getPlayerStatusMenu(chosenPlayer).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_Confounded_" + chosenPlayer.getColor() + ".png"));
                reference.getPlayerStatusMenu(chosenPlayer).statusDisplayLabel.setToolTipText("Status: Leader & Confounded");
            }
            else{
                reference.getPlayerStatusMenu(chosenPlayer).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Confounded_" + chosenPlayer.getColor() + ".png"));
                reference.getPlayerStatusMenu(chosenPlayer).statusDisplayLabel.setToolTipText("Status: Confounded");
            }

            player.monoWheat(-3);
            hasConfounded=true;
            confound.setEnabled(false);
            reference.getPlayerStatusMenu(chosenPlayer).statusDisplayLabel.revalidate();
            JOptionPane.showMessageDialog(this,"You have successfully confounded "+chosenPlayer.getName()+". Their actions may fail this turn.","Brewer Special Action", 1, new ImageIcon("Resources/Catan_Icon.png"));
        }

        else if(e.getSource()==sheepify){
            if(player.getWoolNum()==0){
                JOptionPane.showMessageDialog(this,"You don't have the sheep necessary to perform this action.","Insufficient Sheep",1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to sheepify.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                sheepify.setEnabled(false);
                return;
            }

            if(JOptionPane.showConfirmDialog(this,"Would you like to force a tile to now produce sheep?","Sheepify Tile",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))==0){
                compatibleTiles = Arrays.stream(reference.tiles).filter(tile -> !tile.getType().equals("Plains") && !tile.getType().equals("Desert")).collect(Collectors.toCollection(ArrayList::new));
                compatibleTiles.get(new Random().nextInt(compatibleTiles.size())).setType("Plains");
                hasSheepified=true;
                player.monoWool(-1);
                reference.redrawEverything=true;
                reference.repaint();
                update();
                reference.showBuiltImage("Resources/Preview_Images/Sheepify.png", "Tile Conversion");
                JOptionPane.showMessageDialog(this,"A tile has converted. More sheep abound on Catan.","Sheepification Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                sheepify.setEnabled(false);
                return;
            }
        }

        else if(e.getSource()==forestExpansion) {
            if (player.getLumberNum() == 0) {
                JOptionPane.showMessageDialog(this, "You don't have the wood necessary to perform this action.", "Insufficient Wood", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if (isConfounded()) {
                JOptionPane.showMessageDialog(this, "You are confounded and have failed to expand the forests.", "Action Failed", 1, new ImageIcon("Resources/Catan_Icon.png"));
                forestExpansion.setEnabled(false);
                return;
            }

            if (JOptionPane.showConfirmDialog(this, "Would you like to force a tile to now produce wood?", "Forestify Tile", JOptionPane.YES_NO_OPTION, 1, new ImageIcon("Resources/Catan_Icon.png")) == 0) {
                compatibleTiles = Arrays.stream(reference.tiles).filter(tile -> !tile.getType().equals("Forest") && !tile.getType().equals("Desert")).collect(Collectors.toCollection(ArrayList::new));
                compatibleTiles.get(new Random().nextInt(compatibleTiles.size())).setType("Forest");
                hasForestified = true;
                player.monoLumber(-1);
                reference.redrawEverything = true;
                reference.repaint();
                update();
                reference.showBuiltImage("Resources/Preview_Images/Forestify.png", "Tile Conversion");
                JOptionPane.showMessageDialog(this, "A tile has converted. A forest has grown overnight in Catan.", "Forestification Successful", 1, new ImageIcon("Resources/Catan_Icon.png"));
                forestExpansion.setEnabled(false);
                return;
            }
        }

        else if(e.getSource()==mountainExpansion){
            if(player.getOreNum()==0){
                JOptionPane.showMessageDialog(this,"You don't have the ore necessary to perform this action.","Insufficient Ore",1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to expand the mountains.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                mountainExpansion.setEnabled(false);
                return;
            }

            if(JOptionPane.showConfirmDialog(this,"Would you like to force a tile to now produce ore?","Mountainify Tile",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))==0){
                compatibleTiles = Arrays.stream(reference.tiles).filter(tile -> !tile.getType().equals("Mountain") && !tile.getType().equals("Desert")).collect(Collectors.toCollection(ArrayList::new));
                compatibleTiles.get(new Random().nextInt(compatibleTiles.size())).setType("Mountain");
                hasMountainified=true;
                player.monoOre(-1);
                reference.redrawEverything=true;
                reference.repaint();
                update();
                reference.showBuiltImage("Resources/Preview_Images/Mountainified.png", "Tile Conversion");
                JOptionPane.showMessageDialog(this,"A tile has converted. A mountain has been created in Catan.","Mountainification Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                mountainExpansion.setEnabled(false);
                return;
            }
        }

        else if(e.getSource()==farmExpansion){
            if(player.getGrainNum()==0){
                JOptionPane.showMessageDialog(this,"You don't have the wheat necessary to perform this action.","Insufficient Wheat",1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if(isConfounded()){
                JOptionPane.showMessageDialog(this,"You are confounded and have failed to expand the wheat fields.","Action Failed",1, new ImageIcon("Resources/Catan_Icon.png"));
                farmExpansion.setEnabled(false);
                return;
            }

            if(JOptionPane.showConfirmDialog(this,"Would you like to force a tile to now produce wheat?","Wheatify Tile",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"))==0){
                compatibleTiles = Arrays.stream(reference.tiles).filter(tile -> !tile.getType().equals("Grain") && !tile.getType().equals("Desert")).collect(Collectors.toCollection(ArrayList::new));
                compatibleTiles.get(new Random().nextInt(compatibleTiles.size())).setType("Grain");
                hasFarmed=true;
                player.monoWheat(-1);
                reference.redrawEverything=true;
                reference.repaint();
                update();
                reference.showBuiltImage("Resources/Preview_Images/Wheatify.png", "Tile Conversion");
                JOptionPane.showMessageDialog(this,"A tile has converted. Vast wheat fields have grown overnight in Catan.","Wheatification Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                farmExpansion.setEnabled(false);
                return;
            }
        }


        else if(e.getSource()==remainingResources)
            JOptionPane.showMessageDialog(this,"Remaining Building Materials: \nRoad                ⇒     "+player.getRoads()+"\nSettlement     ⇒     "+player.getSettlements()+"\nCity                   ⇒     "+player.getCities(),"Building Supplies",1, new ImageIcon("Resources/Catan_Icon.png"));

        reference.updateAllStatusMenus();
        player.winTheGame();
    }

    public long numNonNullCategories(){
        return Arrays.stream(new int[]{player.brickNum,player.oreNum,player.woolNum,player.lumberNum,player.grainNum}).filter(resourceNum -> resourceNum!=0).count();
    }

    public ArrayList<String> assassinateResources(){
        ArrayList<String> possibilities = new ArrayList<>();
        ArrayList<String> appropriatelyIndexed = new ArrayList<>();
        int indexOne=0,indexTwo=0;
        if(player.brickNum>0)
            possibilities.add("Brick");
        if(player.oreNum>0)
            possibilities.add("Ore");
        if(player.grainNum>0)
            possibilities.add("Wheat");
        if(player.lumberNum>0)
            possibilities.add("Lumber");
        if(player.woolNum>0)
            possibilities.add("Sheep");

        while(indexOne==indexTwo){
            indexOne = new Random().nextInt(possibilities.size());
            indexTwo = new Random().nextInt(possibilities.size());
        }
        Collections.addAll(appropriatelyIndexed, possibilities.get(indexOne),possibilities.get(indexTwo));

        if(indexOne==0 || indexTwo==0){player.monoBrick(-1);}
        if(indexOne==1 || indexTwo==1){player.monoOre(-1);}
        if(indexOne==2 || indexTwo==2){player.monoWheat(-1);}
        if(indexOne==3 || indexTwo==3){player.monoLumber(-1);}
        if(indexOne==4 || indexTwo==4){player.monoWool(-1);}

        return appropriatelyIndexed;
    }
    public Player findPlayerMatch(JCheckBox[] cbs){
        return reference.getPlayerViaName(Arrays.stream(cbs).filter(AbstractButton::isSelected).findFirst().get().getText());
    }

    public ArrayList<String> getCardNames(){
        ArrayList<String> names = new ArrayList<>();
        player.playedCards.forEach(card -> names.add(card.getType()));

        return names;
    }

    public boolean isConfounded(){
        return player.isDrunk && new Random().nextInt(2)>0;
    }

    public boolean playerHasSettlements(){
        return player.getOwnedIndexes().stream().anyMatch(Index::isSettlement);
    }

    public boolean findNumSelected(JCheckBox[] checkboxes){
        return Arrays.stream(checkboxes).filter(AbstractButton::isSelected).count() > 1 || Arrays.stream(checkboxes).noneMatch(AbstractButton::isSelected);
    }

    public boolean canRepeat(int numRepetitions, String resource){
        if(resource.equalsIgnoreCase("Sheep"))
            return player.getWoolNum()>=numRepetitions*((isPirate)?1:4);

        if(resource.equalsIgnoreCase("Lumber"))
            return player.getLumberNum()>=numRepetitions*((isPirate)?1:4);

        if(resource.equalsIgnoreCase("Brick"))
            return player.getBrickNum()>=numRepetitions*((isPirate)?1:4);

        if(resource.equalsIgnoreCase("Ore"))
            return player.getOreNum()>=numRepetitions*((isPirate)?1:4);

        if(resource.equalsIgnoreCase("Wheat"))
            return player.getGrainNum()>=numRepetitions*((isPirate)?1:4);

        return false;
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        if (reference.isUsingMotionFrame) {
            if (e.getSource() == largestArmyBox && largestArmyBox.isSelected())
                JOptionPane.showMessageDialog(reference, new Object[]{reference.largestArmyLabel}, "Largest Army Award", JOptionPane.PLAIN_MESSAGE);

            else if (e.getSource() == longestRoadBox && longestRoadBox.isSelected())
                JOptionPane.showMessageDialog(reference, new Object[]{reference.longestRoadLabel}, "Longest Road Award", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void resetReference(boolean state){
        PlayerView menuRef = reference.getPlayerStatusMenu(player);
        Arrays.stream(new JMenu[]{menuRef.options,menuRef.build,menuRef.development,menuRef.assassin,menuRef.hwm,menuRef.arsonist,menuRef.cultivator, menuRef.pirate,menuRef.brewer,menuRef.shepherd, menuRef.woodsman,menuRef.farmer,menuRef.mountaineer}).forEach(menu -> menu.setEnabled(state));
    }

    public void enableAppropriateDevCardImages(){
        Arrays.stream(playImages).forEach(image -> image.setEnabled(false));
        for(int x=0; x<lookAppr.length; x++) {
            int finalX = x;
            playImages[x].setEnabled(player.getUnPlayedCards().stream().anyMatch(card -> card.getType().equals(lookAppr[finalX]) && !card.isBoughtThisTurn()));
        }
    }

    public void mousePressed(MouseEvent e) {
        if(e.getSource()==devImages[0])
            JOptionPane.showMessageDialog(devFrame, "There "+(reference.numDevCardsOfATypeLeft("Knight")==1?"is":"are") + (reference.devCardTransparency || reference.devCardDeck.size() == reference.size ? " " : ", at most, ") + alphaNumeric.get((int) reference.numDevCardsOfATypeLeft("Knight")) + " 'Knight' card"+(reference.numDevCardsOfATypeLeft("Knight")==1?"":"s")+ " remaining in the deck.","Knight Cards Remaining",1, new ImageIcon("Resources/Catan_Icon.png"));
        if(e.getSource()==devImages[1])
            JOptionPane.showMessageDialog(devFrame,"There "+(reference.numDevCardsOfATypeLeft("Monopoly")==1?"is":"are")+(reference.devCardTransparency || reference.devCardDeck.size() == reference.size ? " " : ", at most, ")+alphaNumeric.get((int)(reference.numDevCardsOfATypeLeft("Monopoly")))+" 'Monopoly' card"+(reference.numDevCardsOfATypeLeft("Monopoly")==1?"":"s")+" remaining in the deck.","Monopoly Cards Remaining",1, new ImageIcon("Resources/Catan_Icon.png"));
        if(e.getSource()==devImages[2])
            JOptionPane.showMessageDialog(devFrame,"There "+(reference.numDevCardsOfATypeLeft("Road Building")==1?"is":"are")+(reference.devCardTransparency || reference.devCardDeck.size() == reference.size ? " " : ", at most, ")+alphaNumeric.get((int)(reference.numDevCardsOfATypeLeft("Road Building")))+" 'Road Building' card"+(reference.numDevCardsOfATypeLeft("Road Building")==1?"":"s")+" remaining in the deck.","Road Building Cards Remaining",1, new ImageIcon("Resources/Catan_Icon.png"));
        if(e.getSource()==devImages[3])
            JOptionPane.showMessageDialog(devFrame,"There "+(reference.numDevCardsOfATypeLeft("Year of Plenty")==1?"is":"are")+(reference.devCardTransparency || reference.devCardDeck.size() == reference.size ? " " : ", at most, ")+alphaNumeric.get((int)(reference.numDevCardsOfATypeLeft("Year of Plenty")))+" 'Year of Plenty' card"+(reference.numDevCardsOfATypeLeft("Year of Plenty")==1?"":"s")+" remaining in the deck.","Year of Plenty Cards Remaining",1, new ImageIcon("Resources/Catan_Icon.png"));
        if(e.getSource()==devImages[4])
            JOptionPane.showMessageDialog(devFrame,"There "+(reference.numDevCardsOfATypeLeft("Victory Points")==1?"is":"are")+(reference.devCardTransparency || reference.devCardDeck.size() == reference.size ? " " : ", at most, ")+alphaNumeric.get((int)(reference.numDevCardsOfATypeLeft("Victory Points")))+" 'Victory Point' card"+(reference.numDevCardsOfATypeLeft("Victory Points")==1?"":"s")+" remaining in the deck.","Victory Point Cards Remaining",1, new ImageIcon("Resources/Catan_Icon.png"));

        if(e.getSource() == imageCostLabel) {
            try {Thread.sleep(200);}
            catch (InterruptedException ignored) {}
            costFrame.setVisible(false);
        }

        if(e.getSource()==playImages[0] && playImages[0].isEnabled())playAppropriateCard("Knight");
        if(e.getSource()==playImages[1] && playImages[1].isEnabled())playAppropriateCard("Monopoly");
        if(e.getSource()==playImages[2] && playImages[2].isEnabled())playAppropriateCard("Road Building");
        if(e.getSource()==playImages[3] && playImages[3].isEnabled())playAppropriateCard("Year of Plenty");
        if(e.getSource()==playImages[4] && playImages[4].isEnabled())playAppropriateCard("Victory Points");
    }

    public void playAppropriateCard(String devCard){
        DevelopmentCard playedCard = player.getUnPlayedCards().stream().filter(card -> card.getType().equals(devCard) && !card.isBoughtThisTurn()).findFirst().orElse(new DevelopmentCard());
        playFrame.setVisible(false);
        //JOptionPane.showMessageDialog(this, "You are playing a '"+devCard+" Card'. Its effects are now being activated.", "Development Card Played", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        playedCard.playCard();
        reference.managedCards.add(playedCard.getType());
        player.addDevelopmentCardToPlayed(playedCard);
        player.removeDevelopmentCardFromUnplayed(playedCard);
        readdDevCards(played);
        readdDevCards(unplayed);
        reference.updateAllStatusMenus();
        unplayed.setSelectedIndex(0);
        playCard.setEnabled(false);
        playedOneDevCard=true;
        resetReference(true);
    }

    public void readdDevCards(JComboBox<Object> box){
        box.removeAllItems();
        box.addItem((box.equals(played)?"Played Cards":"Hidden Cards"));
        for (String s : lookAppr) {
            if((box.equals(played) ? player.getPlayedCards() : player.getUnPlayedCards()).stream().anyMatch(card -> card.getType().equalsIgnoreCase(s))) {
                num = (box.equals(played) ? player.getPlayedCards() : player.getUnPlayedCards()).stream().filter(card -> card.getType().equalsIgnoreCase(s)).count();
                box.addItem(num+" "+((num>1)?plurals.get(s):s));
            }
        }
    }

    public void showDiceFrames(int diceOneVal, int diceTwoVal){
        Arrays.stream(new JFrame[]{diceOne,diceTwo}).forEach(frame -> frame.setVisible(true));
        diceOneLabel.setIcon(new ImageIcon("Resources/Dice_Faces/"+diceOneVal+".png"));
        diceTwoLabel.setIcon(new ImageIcon("Resources/Dice_Faces/"+diceTwoVal+".png"));
        diceTwo.setLocation((int)screen.getWidth()/2,(int)screen.getHeight()/2 + 45);
        diceOne.setLocation((int)screen.getWidth()/2 - 80, (int)screen.getHeight()/2 + 45);
        Arrays.stream(new JFrame[]{diceOne,diceTwo}).forEach(Window::toFront);
    }

    public void showResourceChanges(int bricks, int ores, int wheats, int sheeps, int woods){
        brickNum.setBackground((bricks>0)?new Color(155,20,30):standard);
        oreNum.setBackground((ores>0)?new Color(51,51,51):standard);
        wheatNum.setBackground((wheats>0)?new Color(255,204,51):standard);
        sheepNum.setBackground((sheeps>0)?new Color(0,204,0):standard);
        woodNum.setBackground((woods>0)?new Color(0,102,0):standard);

        brickNum.setText("+ "+bricks);
        oreNum.setText("+ "+ores);
        wheatNum.setText("+ "+wheats);
        sheepNum.setText("+ "+sheeps);
        woodNum.setText("+ "+woods);
    }

    public void hideDice() {
        Arrays.stream(new JFrame[]{diceOne,diceTwo}).forEach(frame -> frame.setVisible(false));
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
