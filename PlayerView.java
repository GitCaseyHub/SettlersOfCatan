import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class PlayerView extends JFrame implements ActionListener, MouseMotionListener {
    //Added JOptionPanes that appear when the player hovers over the award boxes (but the listener only activates if that player has the award)
    
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

    //MenuBar
    JMenuBar mb = new JMenuBar();
    JMenu build = new JMenu("Build");
    JMenuItem settlement = new JMenuItem("Build Settlement");
    JMenuItem city = new JMenuItem("Upgrade Settlement to City");
    JMenuItem road = new JMenuItem("Build Road");
    JMenu development = new JMenu("Develop");
    JMenuItem buyCard = new JMenuItem("Buy Development Card");
    JMenuItem playCard = new JMenuItem("Play Development Card");
    JMenu options = new JMenu("Options");
    JMenuItem exchange = new JMenuItem("Trade w/ Other Players");
    JMenuItem fourForOne = new JMenuItem("Four/One Resource Trade");
    JMenuItem buildingCard = new JMenuItem("Building Helper Card");
    JMenuItem rollDice = new JMenuItem("Roll Dice");
    JMenuItem endTurn = new JMenuItem("End Turn");

    //Development Card Prep
    String[] devCardTypes = {"Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Year of Plenty","Year of Plenty","Victory Points","Victory Points","Victory Points","Victory Points","Victory Points","Monopoly","Monopoly","Road Builder","Road Builder"};

    //Building Costs Frame
    JFrame costFrame = new JFrame();
    JLabel imageCostLabel = new JLabel("", SwingConstants.CENTER);

    public PlayerView(Player player, CatanBoard reference, TradingFrame tf) {
        //Relating global variables to class variables
        this.player = player;
        this.reference=reference;
        this.tf=tf;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

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
        options.add(endTurn);
        endTurn.addActionListener(this);
        options.setEnabled(false);
        development.setEnabled(false);
        build.setEnabled(false);

        //Creating the GUI
        this.setLayout(new BorderLayout());
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
            turnHolder.setBorder(new TitledBorder("Turn"));
        vpPointHolder.setBorder(compound);
        vpPointHolder.add(victoryPointLabel);
        borderSouth.add(borderInfoPanel,BorderLayout.CENTER);
        turnBox.setEnabled(false);
        turnBox.setToolTipText("This box is checked if it's your turn");
        borderInfoPanel.setBorder(compound);
        borderInfoPanel.add(awardPanel);
        victoryPointLabel.setBorder(new TitledBorder("VPs"));
        borderNorth.setBackground(Color.white);
        colorDisplayLabel.setBorder(compound);
        infoPanel.add(devPanel);
        infoPanel.setBorder(compound);
        devPanel.setBorder(new TitledBorder("Development Cards"));
        devPanel.add(unplayed);
        unplayed.setBorder(compound);
        devPanel.add(played);
        played.setBorder(compound);
        unplayed.addItem("Hidden Cards");
        played.addItem("Revealed Cards");
        victoryPointLabel.setFont(new Font(victoryPointLabel.getFont().getName(),Font.PLAIN,16));

        awardPanel.setBorder(new TitledBorder("Awards"));
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
        reference.getPlayerStatusMenu(player).options.setEnabled(true);
        reference.getPlayerStatusMenu(player).build.setEnabled(true);
        reference.getPlayerStatusMenu(player).development.setEnabled(true);
        settlement.setEnabled(false);
        city.setEnabled(false);
        road.setEnabled(false);
        buildingCard.setEnabled(false);
        buyCard.setEnabled(false);
        playCard.setEnabled(false);
        exchange.setEnabled(false);
        fourForOne.setEnabled(false);
        rollDice.setEnabled(true);
        endTurn.setEnabled(false);
    }

    public void afterRoll() {
        settlement.setEnabled(true);
        city.setEnabled(true);
        road.setEnabled(true);
        buyCard.setEnabled(true);
        playCard.setEnabled(true);
        exchange.setEnabled(true);
        endTurn.setEnabled(true);
        fourForOne.setEnabled(true);
        rollDice.setEnabled(false);
        buildingCard.setEnabled(true);
    }

    public void initializeCostFrame(){
        costFrame.add(imageCostLabel);
        imageCostLabel.setIcon(new ImageIcon("Resources/Building_Costs.png"));

        //costFrame bounds need to be adjusted
        costFrame.setBounds((int)this.getBounds().getX(),(int)this.getBounds().getY(),291,357);
        costFrame.setUndecorated(true);
    }

    public ArrayList<Player> getOtherPlayers(){
        ArrayList<Player> others = new ArrayList<Player>();
        for(int x=0; x<reference.catanPlayerList.size(); x++)
            if(reference.catanPlayerList.get(x)!=player)
                others.add(reference.catanPlayerList.get(x));

        return others;
    }

    public void actionPerformed(ActionEvent e){
        if(e.getSource()==settlement){
            int settlementInput = JOptionPane.showConfirmDialog(this,"Would you like to create a new settlement?","Settlement Building",JOptionPane.YES_NO_OPTION);
            if(settlementInput==0){
                if((player.getBrickNum()>=1 && player.getLumberNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getBrickNum()>=2 && player.getLumberNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    reference.isPlayerActing=true;
                    this.player.changeVictoryPoints(1);
                    JOptionPane.showMessageDialog(this,"Select the index you'd like to build a new settlement on.","Settlement Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    reference.isSettlementBuilding=true;
                    reference.getPlayerStatusMenu(player).options.setEnabled(false);
                    reference.getPlayerStatusMenu(player).build.setEnabled(false);
                    reference.getPlayerStatusMenu(player).development.setEnabled(false);

                    if(player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")){
                        player.monoBrick(-2);
                        player.monoWheat(-2);
                        player.monoLumber(-2);
                        player.monoWool(-2);
                    }
                    else{
                        player.monoBrick(-1);
                        player.monoWheat(-1);
                        player.monoLumber(-1);
                        player.monoWool(-1);
                    }
                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a new settlement.","Settlement Building Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==fourForOne) {
            if(player.getLumberNum()<4 && player.getBrickNum()<4 && player.getWoolNum()<4 && player.getGrainNum()<4 && player.getOreNum()<4)
                JOptionPane.showMessageDialog(this,"You don't have four or more of a single resource. You cannot use this 'option'.","Insufficient Resources", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            else{
                int confirm = JOptionPane.showConfirmDialog(this,"Would you like to trade in four of a resource for one of another resource?","Generic Resource Exchange",JOptionPane.YES_NO_OPTION);

                if(confirm==JOptionPane.YES_OPTION) {
                    try {
                        String exchangeResource = "";
                        while (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat")) {
                            exchangeResource = JOptionPane.showInputDialog(this, "Type in the resource you'd like to trade four in of: Sheep - Lumber - Ore - Brick - Wheat", "Resource Exchange", JOptionPane.QUESTION_MESSAGE);

                            if (!exchangeResource.equalsIgnoreCase("Sheep") && !exchangeResource.equalsIgnoreCase("Lumber") && !exchangeResource.equalsIgnoreCase("Ore") && !exchangeResource.equalsIgnoreCase("Brick") && !exchangeResource.equalsIgnoreCase("Wheat"))
                                JOptionPane.showMessageDialog(this, "That is not one of the resource choices. Please choose again.", "Invalid Resource", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                            if ((exchangeResource.equalsIgnoreCase("Sheep") && player.getWoolNum() < 4) || (exchangeResource.equalsIgnoreCase("Lumber") && player.getLumberNum() < 4) || (exchangeResource.equalsIgnoreCase("Ore") && player.getOreNum() < 4) || (exchangeResource.equalsIgnoreCase("Bric") && player.getBrickNum() < 4) || (exchangeResource.equalsIgnoreCase("Wheat") && player.getGrainNum() < 4)) {
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
                            player.monoWool(-4);
                        else if (exchangeResource.equalsIgnoreCase("Lumber"))
                            player.monoLumber(-4);
                        else if (exchangeResource.equalsIgnoreCase("Brick"))
                            player.monoBrick(-4);
                        else if (exchangeResource.equalsIgnoreCase("Ore"))
                            player.monoOre(-4);
                        else if (exchangeResource.equalsIgnoreCase("Wheat"))
                            player.monoWheat(-4);

                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Exchange Complete", JOptionPane.INFORMATION_MESSAGE);
                    } catch (NullPointerException cancelCaught) {
                        JOptionPane.showMessageDialog(this, "An invalid input has been received. This operation has been cancelled", "Cancellation", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                }
            }
        }

        else if(e.getSource()==city){
            int cityInput = JOptionPane.showConfirmDialog(this,"Would you like to upgrade one of your settlements into a city?","Settlement Upgrade",JOptionPane.YES_NO_OPTION);
            if(cityInput==0){
                if((player.getGrainNum()>=2 && player.getOreNum()>=3 && (!player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf"))) || (player.getGrainNum()>=4 && player.getOreNum()>=6 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    if(playerHasSettlements()) {
                        reference.isPlayerActing=true;
                        this.player.changeVictoryPoints(1);
                        JOptionPane.showMessageDialog(null, "Select the settlement you'd like to upgrade.", "City Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                        reference.isCityUpgrading = true;
                        reference.getPlayerStatusMenu(player).options.setEnabled(false);
                        reference.getPlayerStatusMenu(player).build.setEnabled(false);
                        reference.getPlayerStatusMenu(player).development.setEnabled(false);

                        if(player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")){
                            player.monoOre(-6);
                            player.monoWheat(-4);
                        }
                        else{
                            player.monoOre(-3);
                            player.monoWheat(-2);
                        }
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
            int roadInput = JOptionPane.showConfirmDialog(this,"Would you like to create a road?","Road Building",JOptionPane.YES_NO_OPTION);
            if(roadInput==0){
                if((player.getBrickNum()>=1 && player.getLumberNum()>=1 && !player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf")) || (player.getBrickNum()>=2 && player.getLumberNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))) {
                    reference.isPlayerActing=true;
                    JOptionPane.showMessageDialog(this,"Choose the two indices you'd like to build a road between.","Road Building", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    reference.isRoadBuilding=true;
                    reference.getPlayerStatusMenu(player).options.setEnabled(false);
                    reference.getPlayerStatusMenu(player).build.setEnabled(false);
                    reference.getPlayerStatusMenu(player).development.setEnabled(false);

                    if(player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")){
                        player.monoBrick(-2);
                        player.monoLumber(-2);
                    }
                    else{
                        player.monoBrick(-1);
                        player.monoLumber(-1);
                    }

                    update();
                }
                else
                    JOptionPane.showMessageDialog(this,"You do not have the necessary resources to build a road.","Road Building Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
        }

        else if(e.getSource()==buyCard){
            DevelopmentCard newDc = new DevelopmentCard(devCardTypes[new Random().nextInt(25)],player,getOtherPlayers(),reference,true);
            int devInput = JOptionPane.showConfirmDialog(this,"Would you like to draw a development card?","Development Card Draw",JOptionPane.YES_NO_OPTION);
            if(devInput==0){
                if((player.getOreNum()>=1 && player.getWoolNum()>=1 && player.getGrainNum()>=1 && !player.getClassTitle().equals("Pirate") && !player.getClassTitle().equals("Serf")) || (player.getOreNum()>=2 && player.getWoolNum()>=2 && player.getGrainNum()>=2 && (player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")))){
                    JOptionPane.showMessageDialog(this,"You have purchased a development card.","Development Card", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    player.addDevelopmentCardToUnplayed(newDc);
                    unplayed.addItem(newDc.getType());

                    if(player.getClassTitle().equals("Pirate") || player.getClassTitle().equals("Serf")){
                        player.monoWool(-2);
                        player.monoOre(-2);
                        player.monoWheat(-2);
                    }
                    else{
                        player.monoWool(-1);
                        player.monoOre(-1);
                        player.monoWheat(-1);
                    }
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
                    reference.getPlayerStatusMenu(player).options.setEnabled(false);
                    reference.getPlayerStatusMenu(player).build.setEnabled(false);
                    reference.getPlayerStatusMenu(player).development.setEnabled(false);
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
                this.tf.setVisible(true);
                this.tf.updateComboBoxes();
            }
        }

        else if(e.getSource()==endTurn){
            if(reference.cataclysmsActive){
                int cataclysmOccurrence = new Random().nextInt(20);
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

            JOptionPane.showMessageDialog(this,"You have passed the turn. "+name+", it is now your turn.","Ending the Turn", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            this.player.setTurn(false);
            this.turnBox.setSelected(false);
            reference.getPlayerStatusMenu(player).options.setEnabled(false);
            reference.getPlayerStatusMenu(player).build.setEnabled(false);
            reference.getPlayerStatusMenu(player).development.setEnabled(false);
            hasRolled=false;

            for(int x=0; x<player.getUnPlayedCards().size(); x++)
                player.getUnPlayedCards().get(x).setBoughtThisTurn(false);
            tf.rejections.clear();
            tf.setVisible(false);
            costFrame.setVisible(false);
            update();
        }
        else if(e.getSource()==rollDice){
            int diceRoll = new Random().nextInt(10)+2;
            JOptionPane.showMessageDialog(this,"You've rolled a "+((diceRoll!=7)?diceRoll+". Resources will be distributed accordingly.":"7. Click on a tile you'd like to move the robber to."),"Roll For The Turn", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            if(diceRoll==7) {
                reference.getPlayerStatusMenu(player).options.setEnabled(false);
                reference.getPlayerStatusMenu(player).build.setEnabled(false);
                reference.getPlayerStatusMenu(player).development.setEnabled(false);
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
        reference.updateAllStatusMenus();
    }

    public boolean playerHasSettlements(){
        int counter=0;
        for(int x=0; x<player.getOwnedIndexes().size(); x++)
            if(player.getOwnedIndexes().get(x).isSettlement())
                counter++;

        return counter!=0;
    }

    public boolean multiples(String type){
        int counter=0;
        for(int x=0; x<player.getUnPlayedCards().size(); x++)
            if(player.getUnPlayedCards().get(x).getType().equals(type))
                counter++;

        return counter>1;
    }

    public boolean findNumSelected(JCheckBox[] checkboxes){
        int val=0;
        for (JCheckBox checkbox : checkboxes)
            val += (checkbox.isSelected() ? 1 : 0);

        return val <= 1 && val != 0;
    }

    public boolean boughtAllOnSameTurn(String type){
        int counter=0;
        for(int x=0; x<player.unPlayedCards.size(); x++) {
            if (player.unPlayedCards.get(x).getType().equals(type))
                counter++;
            if (player.unPlayedCards.get(x).isBoughtThisTurn())
                counter--;
        }
        return counter==0;
    }

    public void mouseDragged(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        if(e.getSource()==largestArmyBox && largestArmyBox.isSelected())
            JOptionPane.showOptionDialog(null, null, "Largest Army Award", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), new Object[]{reference.largestArmyLabel}, null);

        else if(e.getSource() == longestRoadBox && longestRoadBox.isSelected())
            JOptionPane.showOptionDialog(null, null, "Longest Road Award", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE , new ImageIcon("Resources/Catan_Icon.png"), new Object[]{reference.longestRoadLabel}, null);
    }
}
