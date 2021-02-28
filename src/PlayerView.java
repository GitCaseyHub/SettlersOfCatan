import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class PlayerView extends JFrame implements ActionListener, MouseMotionListener {
    //Fancy Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

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
    JMenuItem buildingCard = new JMenuItem("Building Helper Card");
    JMenuItem rollDice = new JMenuItem("Roll Dice");
    JMenuItem remainingResources = new JMenuItem("Remaining Materials");
    JMenuItem endTurn = new JMenuItem("End Turn");

    //Development Card Prep
    String[] devCardTypes = {"Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Year of Plenty","Year of Plenty","Victory Points","Victory Points","Victory Points","Victory Points","Victory Points","Monopoly","Monopoly","Road Builder","Road Builder"};

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

    //Special Classes
    JCheckBox[] playerNames;
    Player chosenPlayer;

    //Simplification
    boolean isPirateOrSerf;

    //Turn cycles
    boolean singleRandomize=false;
    boolean singleDemocracy=false;
    int diceRoll=0;

    public PlayerView(Player player, CatanBoard reference, TradingFrame tf) {
        //Relating global variables to class variables
        this.player = player;
        this.reference=reference;
        this.tf=tf;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        build.addSeparator();
        development.addSeparator();
        options.addSeparator();
        hwm.addSeparator();
        assassin.addSeparator();
        arsonist.addSeparator();
        cultivator.addSeparator();

        //Menubar creation
        this.setJMenuBar(mb);
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
        }

        fourForOne.setText((player.getClassTitle().equals("Pirate")?"One/One Resource Trade [Pirate]":"Four/One Resource Trade"));
        isPirateOrSerf = player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf");

        //Creating the GUI
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        this.add(graphicHolder, BorderLayout.CENTER);
        this.add(borderNorth,BorderLayout.NORTH);
        borderNorth.setBorder(compound);
        borderNorth.add(infoPanel,BorderLayout.CENTER);
        borderNorth.add(colorDisplayLabel,BorderLayout.WEST);
        this.add(borderSouth,BorderLayout.SOUTH);
        borderSouth.add(vpPointHolder,BorderLayout.WEST);
        borderSouth.add(turnBorderPanel, BorderLayout.EAST);
        turnBorderPanel.setBorder(compound);
        turnBorderPanel.add(turnHolder);
        turnHolder.add(new JLabel("  "),BorderLayout.WEST);
        turnHolder.add(turnBox,BorderLayout.CENTER);
        turnHolder.add(new JLabel("  "),BorderLayout.EAST);
        turnHolder.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Turn"));
        vpPointHolder.setBorder(compound);
        vpPointHolder.add(victoryPointLabel);
        borderSouth.add(borderInfoPanel,BorderLayout.CENTER);
        turnBox.setEnabled(false);
        turnBox.setToolTipText("This box is checked if it's your turn");
        borderInfoPanel.setBorder(compound);
        borderInfoPanel.add(awardPanel);
        victoryPointLabel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"VPs"));
        borderNorth.setBackground(Color.white);
        colorDisplayLabel.setBorder(compound);
        infoPanel.add(devPanel);
        infoPanel.setBorder(compound);
        devPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED),"Development Cards"));
        devPanel.add(unplayed);
        unplayed.setBorder(compound);
        devPanel.add(played);
        played.setBorder(compound);
        unplayed.addItem("Hidden Cards");
        played.addItem("Revealed Cards");
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

        unplayed.setEnabled(player.isTurn());

        //Array manipulation stuff
        for (int x = 0; x < 5; x++) {
            graphicPanels[x] = new JPanel(new BorderLayout());
            graphicImageLabels[x] = new JLabel("", SwingConstants.CENTER);
            graphicImageLabels[x].setIcon(new ImageIcon("Resources/" + graphicStrings[x] + "_Image.png"));
            graphicPanels[x].add(graphicImageLabels[x], BorderLayout.CENTER);
            graphicImageLabels[x].setBorder(compound);
            graphicHolder.add(graphicPanels[x]);
        }
        graphicPanels[0].add(brickNum, BorderLayout.SOUTH);
        brickNum.setBorder(compound);
        graphicPanels[1].add(oreNum, BorderLayout.SOUTH);
        oreNum.setBorder(compound);
        graphicPanels[2].add(sheepNum, BorderLayout.SOUTH);
        sheepNum.setBorder(compound);
        graphicPanels[3].add(wheatNum, BorderLayout.SOUTH);
        wheatNum.setBorder(compound);
        graphicPanels[4].add(woodNum, BorderLayout.SOUTH);
        woodNum.setBorder(compound);
        build.addSeparator();
        development.addSeparator();
        options.addSeparator();
        hwm.addSeparator();
        assassin.addSeparator();
        arsonist.addSeparator();
        cultivator.addSeparator();
        initializeCostFrame();
    }

    public void update(){
        brickNum.setText((!player.isTurn()?"?":""+player.getBrickNum()));
        oreNum.setText((!player.isTurn()?"?":""+player.getOreNum()));
        sheepNum.setText((!player.isTurn()?"?":""+player.getWoolNum()));
        wheatNum.setText((!player.isTurn()?"?":""+player.getGrainNum()));
        woodNum.setText((!player.isTurn()?"?":""+player.getLumberNum()));
        largestArmyBox.setSelected(player.hasLargestArmy());
        longestRoadBox.setSelected(player.hasLongestRoad());
        victoryPointLabel.setText("   "+player.getVictoryPointTotal()+"   ");

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
        if(!singleDemocracy && !reference.doingStartup) {
            if (reference.turnNameList.get(0).equals(this.player.getName()) && reference.democracy) {
                singleDemocracy=true;
                reference.performDemocracyVoting();
            }
        }

        resetReference(true);
        rollDice.setEnabled(true);
        Arrays.stream(new JMenuItem[]{settlement,city,road,buildingCard,buyCard,playCard,exchange,fourForOne,endTurn,steal,assassinate,remainingResources,setFire,cultivate}).forEach(item -> item.setEnabled(false));
        Arrays.stream(new Boolean[]{hasStolen,hasKilled,didSteal,hasSetFire,hasCultivated}).forEach(bool -> bool=false);
    }

    public void afterRoll() {
        Arrays.stream(new JMenuItem[]{settlement,city,road,buildingCard,buyCard,playCard,exchange,fourForOne,endTurn,remainingResources}).forEach(item -> item.setEnabled(true));
        if(!loadedSpecialClasses){
            steal.setEnabled(true);
            assassinate.setEnabled(true);
            setFire.setEnabled(true);
            cultivate.setEnabled(true);
            loadedSpecialClasses=true;
        }
        rollDice.setEnabled(false);
    }

    public void initializeCostFrame(){
        costFrame.add(imageCostLabel);
        imageCostLabel.setIcon(new ImageIcon("Resources/Building_Costs.png"));

        costFrame.setBounds((int)this.getBounds().getX(),(int)this.getBounds().getY(),291,357);
        costFrame.setUndecorated(true);
    }

    public ArrayList<Player> getOtherPlayers(){
        return reference.catanPlayerList.stream().filter(players -> !players.equals(this.player)).collect(Collectors.toCollection(ArrayList::new));
    }

    public void actionPerformed(ActionEvent e){
        if (e.getSource()==settlement) {
            if (player.getSettlements()==0) {
                JOptionPane.showMessageDialog(this, "You no longer have settlements available to build with.", "Settlement Limit Reached", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            int settlementInput = JOptionPane.showConfirmDialog(this,"Would you like to create a new settlement?","Settlement Building",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(settlementInput==0){
                if((player.getBrickNum()>=1 && player.getLumberNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getBrickNum()>=2 && player.getLumberNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    reference.isPlayerActing=true;
                    this.player.changeVictoryPoints(1);
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
            if((!player.getClassTitle().equals("Pirate") && player.getLumberNum()<4 && player.getBrickNum()<4 && player.getWoolNum()<4 && player.getGrainNum()<4 && player.getOreNum()<4) || (player.getClassTitle().equalsIgnoreCase("Pirate") && player.returnTotalResources()>0))
                JOptionPane.showMessageDialog(this,"You don't have sufficient resources to do a trade of this kind.","Insufficient Resources", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            else{
                boolean isPirate = player.getClassTitle().equalsIgnoreCase("Pirate");
                int confirm = JOptionPane.showConfirmDialog(this,isPirate?"Would you like to trade in one resource for another one resource?":"Would you like to trade in four of a resource for one of another resource?","Generic Resource Exchange",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
                if(confirm==JOptionPane.YES_OPTION) {
                    try {
                        String exchangeResource = "";
                        while (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat")) {
                            exchangeResource = (String)JOptionPane.showInputDialog(this, "Type in the resource you'd like to exchange in: Sheep - Lumber - Ore - Brick - Wheat", "Resource Exchange", JOptionPane.QUESTION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"),null,null);

                            if (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat"))
                                JOptionPane.showMessageDialog(this, "That is not one of the resource choices. Please choose again.", "Invalid Resource", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                            if (!isPirate && (exchangeResource.equalsIgnoreCase("Sheep") && player.getWoolNum() < 4) || (exchangeResource.equalsIgnoreCase("Lumber") && player.getLumberNum() < 4) || (exchangeResource.equalsIgnoreCase("Ore") && player.getOreNum() < 4) || (exchangeResource.equalsIgnoreCase("Bric") && player.getBrickNum() < 4) || (exchangeResource.equalsIgnoreCase("Wheat") && player.getGrainNum() < 4)) {
                                JOptionPane.showMessageDialog(this, "You do not have at least four of that resource to complete an exchange.", "Invalid Resource", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                                exchangeResource = "";
                            }
                        }
                        JCheckBox[] optionResources = new JCheckBox[]{new JCheckBox("Sheep"), new JCheckBox("Lumber"), new JCheckBox("Ore"), new JCheckBox("Brick"), new JCheckBox("Wheat")};
                        for (JCheckBox optionResource : optionResources)
                            if (optionResource.getText().equalsIgnoreCase(exchangeResource))
                                optionResource.setEnabled(false);
                        while (findNumSelected(optionResources)) {
                            JOptionPane.showMessageDialog(this, new Object[]{"Select the resource you'd like to exchange for:", optionResources}, "Exchanging Resources", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                            if (findNumSelected(optionResources))
                                JOptionPane.showMessageDialog(this, "You must select a single resource to trade in for.", "Invalid Selection", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        }

                        if (optionResources[0].isSelected())
                            player.monoWool(1);
                        else if (optionResources[1].isSelected())
                            player.monoLumber(1);
                        else if (optionResources[2].isSelected())
                            player.monoOre(1);
                        else if (optionResources[3].isSelected())
                            player.monoBrick(1);
                        else if (optionResources[4].isSelected())
                            player.monoWheat(1);

                        if (exchangeResource.equalsIgnoreCase("Sheep"))
                            player.monoWool(isPirate?-1:-4);
                        else if (exchangeResource.equalsIgnoreCase("Lumber"))
                            player.monoLumber(isPirate?-1:-4);
                        else if (exchangeResource.equalsIgnoreCase("Brick"))
                            player.monoBrick(isPirate?-1:-4);
                        else if (exchangeResource.equalsIgnoreCase("Ore"))
                            player.monoOre(isPirate?-1:-4);
                        else if (exchangeResource.equalsIgnoreCase("Wheat"))
                            player.monoWheat(isPirate?-1:-4);

                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Exchange Complete", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NullPointerException cancelCaught) {
                        JOptionPane.showMessageDialog(this, "An invalid input has been received. This operation has been cancelled", "Cancellation", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                }
            }
        }

        else if(e.getSource()==city){
            if(player.getCities()==0) {
                JOptionPane.showMessageDialog(this, "You no longer have cities available to upgrade with.", "City Limit Reached", 1, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            int cityInput = JOptionPane.showConfirmDialog(this,"Would you like to upgrade one of your settlements into a city?","Settlement Upgrade",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(cityInput==0){
                if((player.getGrainNum()>=2 && player.getOreNum()>=3 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getGrainNum()>=4 && player.getOreNum()>=6 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    if(playerHasSettlements()) {
                        reference.isPlayerActing=true;
                        this.player.changeVictoryPoints(1);
                        JOptionPane.showMessageDialog(null, "Select the settlement you'd like to upgrade.", "City Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        reference.isCityUpgrading = true;
                        resetReference(false);

                        player.monoOre(isPirateOrSerf?-6:-3);
                        player.monoWheat(isPirateOrSerf?-4:-2);

                        update();
                    }
                    else
                        JOptionPane.showMessageDialog(null,"You have no settlements left to upgrade into cities.","Failure To Find Settlement", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
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

            int devInput = JOptionPane.showConfirmDialog(this,"Would you like to draw a development card?","Development Card Draw",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(devInput==0){
                if((player.getOreNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && !player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf")) || (player.getOreNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    DevelopmentCard newDc = reference.drawDevelopmentCard();
                    newDc.setPlayer(this.player);
                    newDc.setOtherPlayers(getOtherPlayers());
                    JOptionPane.showMessageDialog(this,"You have purchased a development card."+((reference.devCardDeck.size()>0)?" There are "+reference.devCardDeck.size()+" cards remaining.":" There are now no more development cards."),"Development Card", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    player.addDevelopmentCardToUnplayed(newDc);
                    unplayed.addItem(newDc.getType());

                    player.monoWool(isPirateOrSerf?-2:-1);
                    player.monoOre(isPirateOrSerf?-2:-1);
                    player.monoWheat(isPirateOrSerf?-2:-1);

                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to purchase a development card.","Cannot Purchase Development Card", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==playCard){
            if(unplayed.getSelectedIndex()==0)
                JOptionPane.showMessageDialog(this,"You must select a card name from your 'Hidden Cards' list. The name you have selected will be the card that is played.","Unplayable Card", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            else {
                DevelopmentCard playedCard = new DevelopmentCard();
                for (int x = 0; x < player.getUnPlayedCards().size(); x++)
                    if (Objects.requireNonNull(unplayed.getSelectedItem()).toString().equals(player.getUnPlayedCards().get(x).getType())) {
                        playedCard = player.getUnPlayedCards().get(x);
                        break;
                    }

                if ((multiples(playedCard.getType()) && !boughtAllOnSameTurn(playedCard.getType())) || !playedCard.isBoughtThisTurn()) {
                    resetReference(false);
                    JOptionPane.showMessageDialog(this, "You are playing a '" + Objects.requireNonNull(unplayed.getSelectedItem()).toString() + " Card'. Its effects are now being activated.", "Development Card Played", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    playedCard.playCard();
                    player.addDevelopmentCardToPlayed(playedCard);
                    unplayed.removeItem(playedCard.getType());
                    played.addItem(playedCard.getType());
                    player.removeDevelopmentCardFromUnplayed(playedCard);
                    reference.updateAllStatusMenus();
                    unplayed.setSelectedIndex(0);
                    playCard.setEnabled(false);
                }
                else {
                    JOptionPane.showMessageDialog(this, "You cannot play a development card the same turn it was drawn. Wait until next turn to do so.", "Development Card Action Failed", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                }
            }
        }

        else if(e.getSource()==exchange){
            if(tf.isVisible())
                JOptionPane.showMessageDialog(this,"You already have your trading frame open.","Already Open", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            else {
                reference.showBuiltImage("Resources/Preview_Images/Trade.png","Resource Exchange");
                this.tf.setVisible(true);
                this.tf.updateComboBoxes();
            }
        }

        else if(e.getSource()==endTurn){
            singleRandomize=false;
            singleDemocracy=false;
            int cataclysmOccurrence = 69;
            if(reference.cataclysmsActive){
                cataclysmOccurrence = new Random().nextInt(20);
                if(cataclysmOccurrence==0)
                    reference.cataclysm();
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
            costFrame.setVisible(false);
            unplayed.setSelectedIndex(0);
            played.setSelectedIndex(0);
            update();

            if(reference.razing)
                reference.razeTiles();
        }
        else if(e.getSource()==rollDice){
            diceRoll=0;
            if(this.player.isLeader()){
                while(diceRoll==0) {
                    try {
                        diceRoll = Integer.parseInt((String) JOptionPane.showInputDialog(this, "Select the number you'd like to roll this turn: ", "Democracy Action", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null));
                        if (diceRoll < 2 || diceRoll > 12)
                            throw new Exception();
                    } catch (Exception f) {
                        JOptionPane.showMessageDialog(this, "You must select an integer between 2 and 12.", "Improper Number Choice", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        diceRoll = 0;
                    }
                }
            }

            else
                diceRoll = new Random().nextInt(11)+2;

            if(reference.gamblerIsPresent()){
                if(new Random().nextInt(100) < 20) {
                    JOptionPane.showMessageDialog(this,"All gamblers made poor bets; each loses one of every resource.","Bad Gamble",JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));
                    reference.catanPlayerList.stream().filter(player -> player.getClassTitle().equalsIgnoreCase("Gambler")).forEach(Player::failGamble);
                }
            }

            JOptionPane.showMessageDialog(this,"You've rolled a "+((diceRoll!=7)?diceRoll+". Resources will be distributed accordingly.":"7. Click on a tile you'd like to move the robber to."),"Roll For The Turn", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

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
            possiblePlayers = reference.catanPlayerList.stream().filter(player -> !player.getClassTitle().equals("Highwayman") && !player.equals(this.player)).collect(Collectors.toCollection(ArrayList::new));
            if(possiblePlayers.size()==0)
                JOptionPane.showMessageDialog(this,"There are no players who you can steal from.","Steal Unsuccessful", JOptionPane.INFORMATION_MESSAGE,new ImageIcon("Resources/Catan_Icon.png"));

            else{
                try{
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
            if(numNonNullCategories()>=2) {
                possibleKills = reference.catanPlayerList.stream().filter(player -> !player.getClassTitle().equals("Assassin") && reference.getPlayerStatusMenu(player).getCardNames().contains("Knight")).collect(Collectors.toCollection(ArrayList::new));
                if (possibleKills.size() == 0)
                    JOptionPane.showMessageDialog(this, "None of the other players have played a knight card. You cannot assassinate this turn.", "Failed Assassination", 1, new ImageIcon("Resources/Catan_Icon.png"));
                else {
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

                    for (Player player : reference.catanPlayerList) {
                        reference.largestArmy(player);
                        player.winTheGame();
                    }
                    removedResources = assassinateResources();
                    JOptionPane.showMessageDialog(this, "You've assassinated " + chosenPlayer.getName() + "'s knight at the cost of one " + removedResources.get(0) + " and one " + removedResources.get(1) + ".", "Assassination Complete", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    reference.showBuiltImage("Resources/Preview_Images/Assassinate.jpg", "Assassination");

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
            int confirmCultivation = JOptionPane.showConfirmDialog(this,"Would you like to double production on a tile?","Cultivator Special Ability", JOptionPane.YES_NO_OPTION,1, new ImageIcon("Resources/Catan_Icon.png"));
            if(confirmCultivation==JOptionPane.YES_OPTION){
                resetReference(false);
                reference.showBuiltImage("Resources/Preview_Images/Cultivate.png","Cultivator Special Action");
                JOptionPane.showMessageDialog(this,"Select the tile you'd like to cultivate.","Cultivator Special Ability", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                reference.isCultivating=true;
                reference.isPlayerActing=true;
                hasCultivated=true;
                cultivate.setEnabled(false);
            }
        }

        else if(e.getSource()==remainingResources)
            JOptionPane.showMessageDialog(this,"Remaining Building Materials: \nRoad                ⇒     "+player.getRoads()+"\nSettlement     ⇒     "+player.getSettlements()+"\nCity                   ⇒     "+player.getCities(),"Building Supplies",1, new ImageIcon("Resources/Catan_Icon.png"));

        reference.updateAllStatusMenus();
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
        return reference.getPlayerViaName(Arrays.stream(cbs).filter(AbstractButton::isSelected).collect(Collectors.toCollection(ArrayList::new)).get(0).getText());
    }

    public ArrayList<String> getCardNames(){
        ArrayList<String> names = new ArrayList<>();
        player.playedCards.forEach(card -> names.add(card.getType()));

        return names;
    }

    public boolean playerHasSettlements(){
        return player.getOwnedIndexes().stream().anyMatch(Index::isSettlement);
    }

    public boolean multiples(String type){
        return player.getUnPlayedCards().stream().filter(card -> card.getType().equals(type)).count() > 1;
    }

    public boolean findNumSelected(JCheckBox[] checkboxes){
        return Arrays.stream(checkboxes).filter(AbstractButton::isSelected).count() > 1 || Arrays.stream(checkboxes).noneMatch(AbstractButton::isSelected);
    }

    public boolean boughtAllOnSameTurn(String type){
        return player.getUnPlayedCards().stream().filter(card -> card.getType().equals(type)).count() - (player.getUnPlayedCards()).stream().filter(card -> card.isBoughtThisTurn() && card.getType().equals(type)).count()==0;
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
        Arrays.stream(new JMenu[]{menuRef.options,menuRef.build,menuRef.development,menuRef.assassin,menuRef.hwm,menuRef.arsonist,menuRef.cultivator}).forEach(menu -> menu.setEnabled(state));
    }
}
