import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CatanBoard extends JFrame implements KeyListener,MouseListener {
    //Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

    //Objects for Board Generation
    String[] types = {"Mountain", "Mountain", "Mountain", "Brick", "Brick", "Brick", "Forest", "Forest", "Forest", "Forest", "Plains", "Plains", "Plains", "Plains", "Grain", "Grain", "Grain", "Grain", "Desert"};
    int[] rollNums = {2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12};
    int[][] coords1 = {{267, 87}, {267 + 134, 87}, {267 + 2 * 134, 87}, {200, 200}, {334, 200}, {200 + 2 * 134, 200}, {200 + 3 * 134, 200}, {133, 313}, {133 + 133, 313}, {133 + 2 * 134, 313}, {133 + 3 * 134, 313}, {666, 313}, {200, 426}, {200 + 134, 426}, {200 + 134 * 2, 426}, {200 + 134 * 3, 426}, {267, 426 + 113}, {267 + 134, 426 + 113}, {267 + 134 * 2, 426 + 113}};
    ArrayList<String> typeList = new ArrayList<>();
    ArrayList<Integer> rollNumList = new ArrayList<>();
    ArrayList<int[]> coordList = new ArrayList<>();
    Tile[] tiles = new Tile[19];
    PlayerView[] statusViewer;
    ArrayList<String> turnNameList;
    String fireResource="";

    //Water Tiles
    Point[] waterPoints1 = new Point[]{new Point(201, -30), new Point(334, -30), new Point(467, -30), new Point(600, -30), new Point(133, 84), new Point(66, 198), new Point(0, 312), new Point(67, 429), new Point(133, 543), new Point(333, 656), new Point(466, 656), new Point(600, 656), new Point(201, 656), new Point(668, 84), new Point(734, 198), new Point(800, 312), new Point(668, 543), new Point(734, 429)};
    Point[] outLinePoints1 = new Point[]{new Point(1, 431), new Point(66, 467), new Point(66, 545), new Point(133, 584), new Point(133, 659), new Point(200, 698), new Point(200, 770), new Point(266, 811), new Point(333, 774), new Point(398, 811), new Point(465, 774), new Point(530, 811), new Point(599, 772), new Point(662, 811), new Point(733, 773), new Point(733, 697), new Point(800, 659), new Point(800, 584), new Point(866, 546), new Point(866, 468), new Point(929, 429), new Point(929, 348), new Point(868, 313), new Point(868, 237), new Point(800, 198), new Point(800, 122), new Point(732, 84), new Point(732, 8), new Point(664, -30), new Point(599, 8), new Point(532, -30), new Point(467, 8), new Point(400, -30), new Point(334, 8), new Point(268, -30), new Point(202, 8), new Point(202, 84), new Point(133, 122), new Point(133, 197), new Point(65, 236), new Point(65, 311), new Point(1, 348)};

    //Frame shaping objects
    int[] outline_x = new int[outLinePoints1.length];
    int[] outline_y = new int[outLinePoints1.length];

    //Paint/Repaint Conditions
    boolean loaded = false;
    boolean settlementPaintCondition = false;
    int chosen_x = 0;
    int chosen_y = 0;

    //Cataclysm Objects
    String[] cataclysms = {"Famine","Locust","Fire","Strike","Monsoon"};
    boolean cataclysmsActive=false;

    //Booleans for conditions on when MouseListeners should activate
    boolean isRoadBuilding = false, isSettlementBuilding = false;
    boolean isDoneRoadBuilding = false;
    boolean doingStartup = false, found = false;
    boolean isMovingRobber = false, isDoneMovingRobber = false;
    boolean roadDevCard = false, finishedRoadCard = false;
    boolean isCityUpgrading = false;
    boolean redrawEverything = false;
    boolean isPlayerActing = false;
    boolean isSettingFire=false;
    boolean isCultivating=false;
    boolean failureCultivate=false;
    boolean failureFire=false;
    boolean wildfire=false;
    boolean devCardTransparency=false;
    boolean isPortDestroying=false;
    int count = 0;

    //Awards
    int currentLongestRoad = 4;
    Player longestRoadPlayer = new Player();
    int currentLargestArmy = 2;
    Player largestArmyPlayer = new Player();

    //Award OptionPane assets
    JLabel longestRoadLabel = new JLabel("",JLabel.CENTER);
    JLabel largestArmyLabel = new JLabel("",JLabel.CENTER);

    //Trading Variables
    TradingFrame firstFrame;
    TradingFrame secondFrame;

    //Settlement/City index objects
    int[][] indexCoords1 = {{264, 122}, {330, 87}, {398, 122}, {461, 87}, {533, 121}, {599, 87}, {658, 122}, {663, 200}, {599, 234}, {532, 200}, {463, 233}, {396, 201}, {329, 235}, {262, 202}, {196, 235}, {197, 312}, {262, 347}, {329, 312}, {394, 348}, {461, 311}, {528, 346}, {599, 312}, {665, 350}, {727, 313}, {729, 237}, {797, 346}, {798, 425}, {725, 461}, {660, 423}, {597, 457}, {527, 424}, {462, 459}, {393, 422}, {327, 460}, {258, 421}, {198, 459}, {132, 423}, {130, 345}, {200, 536}, {263, 572}, {329, 537}, {393, 574}, {464, 534}, {526, 574}, {599, 536}, {655, 646}, {594, 683}, {461, 687}, {392, 646}, {526, 654}, {266, 647}, {332, 686}, {664, 577}, {731, 536}};
    Index[] indexes = new Index[indexCoords1.length];
    Index checked;

    //Port Creation
    Point[][] portPoints1 = new Point[][]{new Point[]{new Point(264, 122), new Point(330, 87), new Point(219, 18)},
            new Point[]{new Point(461, 87), new Point(533, 121), new Point(486, 18)},
            new Point[]{new Point(663, 200), new Point(729, 237), new Point(681, 132)},
            new Point[]{new Point(797, 346), new Point(798, 425), new Point(810, 351)},
            new Point[]{new Point(731, 536), new Point(664, 577), new Point(681, 556)},
            new Point[]{new Point(526, 654), new Point(461, 687), new Point(486, 685)},
            new Point[]{new Point(332, 686), new Point(266, 647), new Point(219, 685)},
            new Point[]{new Point(200, 536), new Point(198, 459), new Point(88, 468)},
            new Point[]{new Point(198, 235), new Point(197, 312), new Point(88, 244)}};
    String[] portTypes = {"Generic", "Generic", "Generic", "Generic", "Brick", "Ore", "Sheep", "Wheat", "Lumber"};
    Port[] ports = new Port[9];
    JCheckBox[] checkOptions;
    Port currentPort;
    JCheckBox chosenPort;

    //Robber Objects
    Tile robberTile;
    JCheckBox[] possibleTargets;

    //Check boolean trackers
    int checkCounter = 0;
    boolean razing=false;
    boolean previewFrames=false;
    boolean isUsingMotionFrame =false;
    boolean usablePorts=false;
    boolean specialActions=false;
    boolean randomize=false;
    boolean democracy=false;
    boolean monarchy=false;
    boolean community=false;

    //Building Variables
    int roadCondition = 0;
    ArrayList<Index> checkedIndexes = new ArrayList<>();
    Object[] roadInfo;
    ArrayList<Road> indexConnections = new ArrayList<>();
    int settlementIndex = 0;

    //Constructor Variables
    ArrayList<Player> catanPlayerList;
    Point[] statusGenerationalPoints;
    PlayerSelect[] playerCreation;
    ArrayList<Player> duplicates = new ArrayList<>();
    BeginGame bgReference;

    //Duplicates
    int[][] coords = new int[coords1.length][];
    int[][] indexCoords = new int[indexCoords1.length][];
    Point[] waterPoints = new Point[waterPoints1.length];
    Point[] outLinePoints = new Point[outLinePoints1.length];
    Point[][] portPoints = new Point[portPoints1.length][];

    //Frame for appropriate image reveal when building
    JFrame buildFrame = new JFrame();
    JLabel buildLabel = new JLabel();
    ArrayList<String> alreadyPreviewed = new ArrayList<>();

    //Democracy
    JCheckBox[] democracyBoxes;
    ArrayList<Integer> votes;
    ArrayList<Player> maxVotesPlayer;
    int leaderIndex;
    boolean singleShowDemocracy=false;

    //Monarchy
    boolean singleShowMonarchy=false;
    int aggregateTurn=0;

    //Cultivator
    String cultivateResource;

    //DevelopmentCard Deck
    ArrayList<String> managedCards = new ArrayList<>();
    ArrayList<DevelopmentCard> devCardDeck = new ArrayList<>();
    String[] devCards = new String[]{"Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Knight","Victory Points","Victory Points","Victory Points","Victory Points","Victory Points","Road Building","Road Building","Monopoly","Monopoly","Year of Plenty","Year of Plenty"};
    HashMap<String,Integer> properNum = new HashMap<>();
    int size = devCards.length;

    //Razing
    boolean completeDesertification=false;

    //Cheat menu
    String cheat = "";
    String[] cheatCodes = {"Unlimited","Decimate","Development","Builder","Activate Ports","Deactivate Ports","VP Manipulation"};
    int vpCheat = 0;
    ArrayList<String> codes = new ArrayList<>();

    //Religious Group
    Player zealot = new Player("Black","Paul","Proselytizer",new ArrayList<Index>(),new ArrayList<DevelopmentCard>(),new ArrayList<DevelopmentCard>(),0,0,0,0,0,0,false,false,false,666,0,0,0,false,0,false);
    String takenResource="";

    public CatanBoard(ArrayList<Player> catanPlayerList, Point[] statusGenerationalPoints, PlayerSelect[] playerCreation, BeginGame bgReference) {
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new Polygon(outline_x,outline_y,outLinePoints.length));
            }
        });

        this.addMouseListener(this);

        for (int x = 0; x < coords1.length; x++)
            coords[x] = new int[]{coords1[x][0], coords1[x][1] + 50};

        for (int x = 0; x < indexCoords1.length; x++)
            indexCoords[x] = new int[]{indexCoords1[x][0], indexCoords1[x][1] + 50};

        for (int x = 0; x < waterPoints1.length; x++)
            waterPoints[x] = new Point((int) waterPoints1[x].getX(), (int) waterPoints1[x].getY() + 50);

        for (int x = 0; x < outLinePoints1.length; x++)
            outLinePoints[x] = new Point((int) outLinePoints1[x].getX(), (int) outLinePoints1[x].getY() + 50);

        for (int x = 0; x < portPoints1.length; x++)
            portPoints[x] = new Point[]{new Point((int) portPoints1[x][0].getX(), (int) portPoints1[x][0].getY() + 50), new Point((int) portPoints1[x][1].getX(), (int) portPoints1[x][1].getY() + 50), new Point((int) portPoints1[x][2].getX(), (int) portPoints1[x][2].getY() + 50)};

        this.addKeyListener(this);
        this.catanPlayerList = catanPlayerList;
        this.statusGenerationalPoints = statusGenerationalPoints;
        this.playerCreation = playerCreation;
        this.bgReference = bgReference;
        Collections.addAll(typeList,types);
        Collections.addAll(coordList,coords);

        for (int x: rollNums)
            rollNumList.add(x);

        for (int x = 0; x < indexes.length; x++)
            indexes[x] = new Index(indexCoords[x], false, x, new Player("", "", "", new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), 0, 0, 0, 0, 0, 0, false, false, false, 69,0,0,0,false,0,false), false, false);

        for (int x = 0; x < types.length; x++) {
            int typeIndex = new Random().nextInt(typeList.size());
            int coordIndex = new Random().nextInt(coordList.size());
            tiles[x] = new Tile(coordList.get(coordIndex), typeList.get(typeIndex), 0, false,false, new Player(),false,new Player());
            typeList.remove(typeIndex);
            coordList.remove(coordIndex);

            if (tiles[x].getType().equals("Desert")) {
                tiles[x].setNum(7);
                tiles[x].setHasRobber(true);
            }

            else {
                int rollIndex = new Random().nextInt(rollNumList.size());
                tiles[x].setNum(rollNumList.get(rollIndex));
                rollNumList.remove(rollIndex);
            }
        }

        for (int x = 0; x < outLinePoints.length; x++) {
            outline_x[x] = (int) outLinePoints[x].getX();
            outline_y[x] = (int) outLinePoints[x].getY();
        }

        //Methods to take care of things before startup
        constructPorts();
        getPortsReady();
        givePlayersCatanBoardReference();
        initializeAwardOptionPanes();
        constructBuildingPreviewFrame();
        createDemocracyComponents();
        initializeDevelopmentDeck();
        initializeDevCardNonTransparencyHashMap();
        initializeCheats();
    }

    public void initializeCheats(){
        Collections.addAll(codes,cheatCodes);
    }

    public void initializeDevelopmentDeck(){
        for(String str: devCards)
            devCardDeck.add(new DevelopmentCard(str,new Player(),new ArrayList<Player>(),this,true));
    }

    public DevelopmentCard drawDevelopmentCard(){
        int index = new Random().nextInt(devCardDeck.size());
        DevelopmentCard cardAtIndex = devCardDeck.get(index);
        devCardDeck.remove(index);
        devCardDeck.trimToSize();
        return cardAtIndex;
    }

    public void createDemocracyComponents(){
        democracyBoxes = new JCheckBox[catanPlayerList.size()];
        for(int x=0; x<catanPlayerList.size(); x++)
            democracyBoxes[x] = new JCheckBox(catanPlayerList.get(x).getName()+" the "+catanPlayerList.get(x).getClassTitle());
    }

    public void constructBuildingPreviewFrame(){
        buildFrame.add(buildLabel);
        buildLabel.setBorder(compound);
        buildFrame.setSize(433,312);
        buildLabel.addMouseListener(this);
    }

    public void initializeDevCardNonTransparencyHashMap(){
        for(int x=0; x<5; x++)
            properNum.put(new String[]{"Knight","Year of Plenty","Road Building","Monopoly","Victory Points"}[x],new Integer[]{14,2,2,2,5}[x]);
    }

    public void initializeAwardOptionPanes(){
        longestRoadLabel.setIcon(new ImageIcon("Resources/Awards/Longest_Road.png"));
        longestRoadLabel.setBorder(compound);
        largestArmyLabel.setIcon(new ImageIcon("Resources/Awards/Largest_Army.png"));
        largestArmyLabel.setBorder(compound);
    }

    public void paint(Graphics g) {
        try {
            if (!loaded) {
                this.setVisible(false);
                for (Tile element : tiles) {
                    BufferedImage tile = ImageIO.read(new File("Tiles/" + element.getType() + ".png"));
                    g.drawImage(tile, element.getPosition()[0], element.getPosition()[1], null);
                }

                for (Tile tile : tiles) {
                    if(tile.getNum()!=7) {
                        BufferedImage dice = ImageIO.read(new File("Rolls/" + tile.getNum() + ".png"));
                        g.drawImage(dice, tile.getPosition()[0] + 42, tile.getPosition()[1] + 25, null);
                    }
                }

                BufferedImage water = ImageIO.read(new File("Tiles/Water_Tile.png"));
                for (Point waterPoint : waterPoints)
                    g.drawImage(water, (int) waterPoint.getX(), (int) waterPoint.getY(), null);

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(0,0,255));
                for (int x = 0; x < outLinePoints.length; x++)
                    g2.drawLine((int) outLinePoints[x].getX(), (int) outLinePoints[x].getY(), (int) outLinePoints[(x + 1) % outLinePoints.length].getX(), (int) outLinePoints[(x + 1) % outLinePoints.length].getY());

                if (usablePorts) {
                    for (Port value : ports) {
                        BufferedImage port = ImageIO.read(new File("Resources/Port/" + value.getType() + "_Port_Ship.png"));
                        g.drawImage(port, (int) value.getLocations()[2].getX(), (int) value.getLocations()[2].getY(), null);
                    }
                }

                for (Tile tile : tiles)
                    if (tile.isHasRobber()) {
                        BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                        g.drawImage(robber, tile.getPosition()[0] + 57, tile.getPosition()[1] + 88, null);
                    }

                //Draws Ports
                if (usablePorts) {
                    //Port Indexes if Active
                    BufferedImage circle = ImageIO.read(new File("Pieces/Active_Ports.png"));
                    for (Point[] portPoint : portPoints) {
                        g.drawImage(circle, (int) portPoint[0].getX(), (int) portPoint[0].getY(), null);
                        g.drawImage(circle, (int) portPoint[1].getX(), (int) portPoint[1].getY(), null);
                    }
                }
                loaded = true;
                this.setVisible(true);
            }
            if (isDoneMovingRobber) {
                BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                redrawEverything = true;
                repaint();
                for (Tile tile : tiles)
                    if (tile.isHasRobber()) {
                        g.drawImage(robber, tile.getPosition()[0] + 57, tile.getPosition()[1] + 88, null);
                        isDoneMovingRobber = false;
                        break;
                    }

                if (checkCounter != 0)
                    JOptionPane.showMessageDialog(this, "The robber has been moved.", "Robber Moved", 1,new ImageIcon("Resources/Catan_Icon.png"));

                ArrayList<Player> availablePlayers = getPlayersOnTile(robberTile);

                if(highwaymanIsPresent())
                    availablePlayers.removeIf(player -> player.getClassTitle().equals("Highwayman"));

                if (availablePlayers.size() != 0) {
                    possibleTargets = new JCheckBox[availablePlayers.size()];
                    for (int x = 0; x < availablePlayers.size(); x++)
                        possibleTargets[x] = new JCheckBox(availablePlayers.get(x).getName() + " the "+availablePlayers.get(x).getClassTitle());

                    String message = "Which player would you like to steal a resource from?";
                    String playerName = "";
                    Object[] params = {message, possibleTargets};
                    int selectCounter = 0;

                    while (selectCounter == 0 || (selectCounter == possibleTargets.length && possibleTargets.length != 1)) {
                        selectCounter = 0;
                        JOptionPane.showMessageDialog(this, params, "Robber Action", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));

                        for (JCheckBox possibleTarget : possibleTargets)
                            if (possibleTarget.isSelected())
                                selectCounter++;

                        if (selectCounter == 0)
                            JOptionPane.showMessageDialog(this, "You have to steal from someone. You cannot elect out of this.", "Try Again",3, new ImageIcon("Resources/Catan_Icon.png"));

                        if (selectCounter == possibleTargets.length && possibleTargets.length != 1) {
                            for (JCheckBox possibleTarget : possibleTargets)
                                possibleTarget.setSelected(false);

                            JOptionPane.showMessageDialog(this, "You can only choose a single player.", "Try Again",3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                    }

                    for (JCheckBox possibleTarget : possibleTargets)
                        if (possibleTarget.isSelected()) {
                            playerName = possibleTarget.getText();
                            break;
                        }

                    Arrays.stream(possibleTargets).forEach(box -> box.setSelected(false));

                    Player playerToStealFrom = getPlayerViaNameWithClassTitle(playerName);
                    String stolenResource = giveRandomResource(playerToStealFrom);
                    ArrayList<Player> highwaymen = catanPlayerList.stream().filter(player -> player.getClassTitle().equals("Highwayman")).collect(Collectors.toCollection(ArrayList::new));
                    highwaymen.removeIf(player -> player.equals(getCurrentPlayer()) || player.equals(playerToStealFrom));

                    if (stolenResource.equals(""))
                        JOptionPane.showMessageDialog(this, "Unfortunately, " + playerToStealFrom.getName() + " has no resources to steal.", "Robber Failure",3, new ImageIcon("Resources/Catan_Icon.png"));

                    else {
                        if (stolenResource.equals("Sheep")) {
                            getCurrentPlayer().monoWool(1);
                            playerToStealFrom.monoWool(-1);
                            highwaymen.forEach(player-> player.monoWool(1));
                        }
                        else if (stolenResource.equals("Ore")) {
                            getCurrentPlayer().monoOre(1);
                            playerToStealFrom.monoOre(-1);
                            highwaymen.forEach(player-> player.monoOre(1));
                        }
                        else if (stolenResource.equals("Brick")) {
                            getCurrentPlayer().monoBrick(1);
                            playerToStealFrom.monoBrick(-1);
                            highwaymen.forEach(player-> player.monoBrick(1));
                        }
                        else if (stolenResource.equals("Lumber")) {
                            getCurrentPlayer().monoLumber(1);
                            playerToStealFrom.monoLumber(-1);
                            highwaymen.forEach(player-> player.monoLumber(1));
                        }
                        else if (stolenResource.equals("Wheat")) {
                            getCurrentPlayer().monoWheat(1);
                            playerToStealFrom.monoWheat(-1);
                            highwaymen.forEach(player-> player.monoWheat(1));
                        }
                        JOptionPane.showMessageDialog(this, "You've stolen " + (stolenResource.equalsIgnoreCase("Ore")?"an ":"a ")+stolenResource.toLowerCase() + " resource from " + playerToStealFrom.getName() + "." + ((highwaymen.size()>0)?" All highwaymen will also receive  "+stolenResource+".":""), "Robber Success", 1,new ImageIcon("Resources/Catan_Icon.png"));
                        updateAllStatusMenus();
                    }
                }
                largestArmy(getCurrentPlayer());
                isDoneMovingRobber = false;
            }
            if (settlementPaintCondition) {
                //Drawing Settlements
                BufferedImage settlement = ImageIO.read(new File("Pieces/" + getCurrentPlayer().getColor() + "_Settlement.png"));
                g.drawImage(settlement, chosen_x, chosen_y, null);
                settlementPaintCondition = false;
                if(!doingStartup){
                    showBuiltImage("Resources/Preview_Images/Settlement.png","Settlement Construction");
                    JOptionPane.showMessageDialog(this,"You've built a new settlement.","Settlement Construction",1,new ImageIcon("Resources/Catan_Icon.png"));
                }
                else
                    isRoadBuilding = true;
            }

            if (roadCondition == 2 && !found) {
                Point testPoint = (Point) roadInfo[0];
                BufferedImage road = ImageIO.read(new File("Pieces/" + roadInfo[1] + "_" + getCurrentPlayer().getColor() + "_Road.png"));
                g.drawImage(road, (int) testPoint.getX(), (int) testPoint.getY(), null);

                roadCondition = 0;
                checkedIndexes.clear();
                isDoneRoadBuilding = true;
                isRoadBuilding = false;

                if (runRoadAlgorithm() && longestRoadPlayer != getCurrentPlayer()) {
                    if (currentLongestRoad != 4) {
                        for (Player player : catanPlayerList)
                            if (player.hasLongestRoad()) {
                                player.setLongestRoad(false);
                                player.changeVictoryPoints(-2);
                                break;
                            }
                    }
                    getCurrentPlayer().setLongestRoad(true);
                    getCurrentPlayer().changeVictoryPoints(2);
                    JOptionPane.showMessageDialog(this, (currentLongestRoad != 4) ? "There is a new longest road comprised of " + currentLongestRoad + " segments by " + getCurrentPlayer().getName() + "." : "The longest road award has been claimed by " + getCurrentPlayer().getName() + ", who has a road of " + currentLongestRoad + " continuous segments.", "New Longest Road", 1,new ImageIcon("Resources/Catan_Icon.png"));
                    this.longestRoadPlayer = getCurrentPlayer();
                    updateAllStatusMenus();

                }
                getCurrentPlayer().changeRoadNum(-1);

                if (doingStartup) {
                    if (duplicates.size() > 0) {
                        duplicates.get(0).setTurn(false);
                        duplicates.remove(0);

                        if (duplicates.size() > 0) {
                            duplicates.get(0).setTurn(true);
                            JOptionPane.showMessageDialog(this, "Place a settlement. Build road from that settlement.", "Road and Settlement Building - "+duplicates.get(0).getName()+(duplicates.get(0).getName().substring(duplicates.get(0).getName().length()-1).equalsIgnoreCase("s")?"'":"'s")+" Building Phase", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            isSettlementBuilding = true;
                        }
                    }

                    if (duplicates.size() == 0) {
                        for (Player player : catanPlayerList)
                            if (player.getName().equals(turnNameList.get(0)))
                                player.setTurn(true);

                        getPlayerStatusMenu(catanPlayerList.get(0)).update();
                        JOptionPane.showMessageDialog(this, "You will now receive your starting resources. Let the games begin.", "Official Start",1, new ImageIcon("Resources/Catan_Icon.png"));
                        givePlayersStartingResources();
                        doingStartup = false;
                    }
                }
                else {
                    if (!roadDevCard && !finishedRoadCard) {
                        showBuiltImage("Resources/Preview_Images/Road.png","Road Construction");
                        JOptionPane.showMessageDialog(this, "You've built a new road.", "Road Building", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    }

                    else if (roadDevCard) {
                        JOptionPane.showMessageDialog(this, (getCurrentPlayer().getRoads()!=0)?"You've built a new road. Now, create your second road.":"You've created your single available road.", "Road Building",1, new ImageIcon("Resources/Catan_Icon.png"));
                        if(getCurrentPlayer().getRoads()!=0) {
                            isRoadBuilding = true;
                            roadDevCard = false;
                            finishedRoadCard = true;
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "You've created your two roads.", "Finished Action",1, new ImageIcon("Resources/Catan_Icon.png"));
                        performStaleReferenceReset(true);
                        finishedRoadCard = false;
                    }
                }
            }

            if (redrawEverything) {
                this.setVisible(false);
                //Redraws Tiles
                for (Tile value : tiles) {
                    BufferedImage tile = ImageIO.read(new File("Tiles/" + value.getType() + ".png"));
                    g.drawImage(tile, value.getPosition()[0], value.getPosition()[1], null);
                }

                //Redraws Water Tiles
                BufferedImage water = ImageIO.read(new File("Tiles/Water_Tile.png"));
                for (Point waterPoint : waterPoints)
                    g.drawImage(water, (int) waterPoint.getX(), (int) waterPoint.getY(), null);

                //Redraws Water Border
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(0, 0, 255));
                for (int x = 0; x < outLinePoints.length; x++)
                    g2.drawLine((int) outLinePoints[x].getX(), (int) outLinePoints[x].getY(), (int) outLinePoints[(x + 1) % outLinePoints.length].getX(), (int) outLinePoints[(x + 1) % outLinePoints.length].getY());

                //Redraws Port Ships
                if(usablePorts) {
                    for (Port value : ports) {
                        if(!value.isDestroyed()) {
                            BufferedImage port = ImageIO.read(new File("Resources/Port/" + value.getType() + "_Port_Ship.png"));
                            g.drawImage(port, (int) value.getLocations()[2].getX(), (int) value.getLocations()[2].getY(), null);
                        }
                        else{
                            BufferedImage pPort = ImageIO.read(new File("Resources/Port/Pillaged_Port.png"));
                            g.drawImage(pPort, (int)value.getLocations()[2].getX(),(int)value.getLocations()[2].getY(),null);
                        }
                    }
                }

                //Redraws Roll Tiles
                for (Tile tile : tiles) {
                    if(!tile.isOnFire()) {
                        if(tile.getNum()!=7) {
                            BufferedImage dice = ImageIO.read(new File("Rolls/" + tile.getNum() + ".png"));
                            g.drawImage(dice, tile.getPosition()[0] + 42, tile.getPosition()[1] + 25, null);
                        }
                    }
                    else{
                        BufferedImage fire = ImageIO.read(new File("Resources/Preview_Images/Fire.png"));
                        g.drawImage(fire,tile.getPosition()[0]+47,tile.getPosition()[1]+20,null);
                    }
                }

                //Draws Robber
                for (Tile tile : tiles)
                    if (tile.isHasRobber()) {
                        BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                        g.drawImage(robber, tile.getPosition()[0] + 57, tile.getPosition()[1] + 88, null);
                    }

                //Redraws Ports
                if (usablePorts) {
                    //Port Indexes if Active
                    BufferedImage circle = ImageIO.read(new File("Pieces/Active_Ports.png"));
                    BufferedImage redX = ImageIO.read(new File("Pieces/Inactive_Ports.png"));

                    for (Point[] portPoint : portPoints) {
                        currentPort = getPortAtLocation(portPoint);
                        if (sharesLocation(new Point((int) portPoint[0].getX(), (int) portPoint[0].getY())))
                            g.drawImage((currentPort.isDestroyed())?redX:circle, (int) portPoint[0].getX(), (int) portPoint[0].getY(), null);
                        if (sharesLocation(new Point((int) portPoint[1].getX(), (int) portPoint[1].getY())))
                            g.drawImage((currentPort.isDestroyed())?redX:circle, (int) portPoint[1].getX(), (int) portPoint[1].getY(), null);
                    }
                }

                //Draws Cities/Settlements
                for (Index index : indexes) {
                    if (index.isTaken()) {
                        if (index.isSettlement()) {
                            BufferedImage settlement = ImageIO.read(new File("Pieces/" + index.getOwner().getColor() + "_Settlement.png"));
                            g.drawImage(settlement, index.getLocation()[0] - 5, index.getLocation()[1] - 16, null);
                        } else if (index.isCity()) {
                            BufferedImage city = ImageIO.read(new File("Pieces/" + index.getOwner().getColor() + "_City.png"));
                            g.drawImage(city, index.getLocation()[0] - 5, index.getLocation()[1] - 16, null);
                        }
                    }
                }

                //Redraw Roads
                for (Road indexConnection : indexConnections) {
                    BufferedImage road = ImageIO.read(new File("Pieces/" + indexConnection.getRoadType() + "_" + indexConnection.getOwner().getColor() + "_Road.png"));
                    g.drawImage(road, (int) indexConnection.getPosition().getX(), (int) indexConnection.getPosition().getY(), null);
                }

                //Draws Aura for Cultivator
                for(Tile tile: tiles){
                    if(tile.isCultivated()) {
                        BufferedImage aura = ImageIO.read(new File("Tiles/Sun_Aura.png"));
                        g.drawImage(aura, tile.getPosition()[0]+29, tile.getPosition()[1]+12, null);
                    }
                }

                redrawEverything = false;
                this.setVisible(true);
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }

        if (!doingStartup)
            performStaleReferenceReset(true);

        updateAllStatusMenus();
    }

    public void resetRoadBuilding(){
        isRoadBuilding=true;
        roadDevCard=true;
        finishedRoadCard=false;
    }

    public void mousePressed(MouseEvent e) {
        if(completeDesertification) {
            JOptionPane.showMessageDialog(this, "Thanks for playing.", "Draw Game", 1, new ImageIcon("Resources/Catan_Icon.png"));
            System.exit(1);
        }

        int xLoc = e.getX();
        int yLoc = e.getY();

        //Code to draw ports
        if(buildFrame.isVisible()){
            if(e.getSource()==buildLabel)
                buildFrame.setVisible(false);

            return;
        }

        if(isPortDestroying){
            for (Port port : ports)
                if (new Rectangle(xLoc, yLoc, 10, 10).intersects(new Rectangle((int) port.getLocations()[2].getX() + 25, (int) port.getLocations()[2].getY() + 25, 50, 50))) {
                    if(port.isDestroyed()){
                        JOptionPane.showMessageDialog(this,(port.getType().toLowerCase().equals("generic")?"That ":"The ")+port.getType().toLowerCase()+" port has already been pillaged.","Port Already Destroyed",1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    port.destroy();
                    getPlayerStatusMenu(getCurrentPlayer()).hasPillaged=true;
                    isPortDestroying=false;
                    redrawEverything=true;
                    repaint();
                    JOptionPane.showMessageDialog(this,"You have pillaged "+(port.getType().toLowerCase().equals("generic")?"a ":"the ")+ port.getType().toLowerCase()+" port. It is no longer accessible.","Port Pillaged",1, new ImageIcon("Resources/Catan_Icon.png"));
                    return;
                }
            return;
        }

        if (usablePorts && !doingStartup) {
            for (Port port : ports) {
                if (new Rectangle(xLoc, yLoc, 10, 10).intersects(new Rectangle((int) port.getLocations()[2].getX() + 25, (int) port.getLocations()[2].getY() + 25, 50, 50))) {
                    for (Index index : indexes) {
                        if ((Math.abs(port.getLocations()[0].getX() - index.getLocation()[0]) < 25 && Math.abs(port.getLocations()[0].getY() - index.getLocation()[1]) < 25) ||
                                (Math.abs(port.getLocations()[1].getX() - index.getLocation()[0]) < 25 && Math.abs(port.getLocations()[1].getY() - index.getLocation()[1]) < 25)) {

                            if (index.isTaken() && index.getOwner().getName().equals(getCurrentPlayer().getName())) {
                                if(!port.isDestroyed()) {
                                    if (getCurrentPlayer().isInDebt()) {
                                        JOptionPane.showMessageDialog(this, "You are in debt. You cannot use ports until you have all non-negative resource values.", "In-Debt Player", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                                        return;
                                    }
                                    usePort(port);
                                    return;
                                }

                                else
                                    JOptionPane.showMessageDialog(this,(port.getType().toLowerCase().equals("generic")?"This ":"The ")+port.getType().toLowerCase()+" port has been pillaged and is now controlled by pirates. It can no longer be used.","Port Pillaged",1, new ImageIcon("Resources/Catan_Icon.png"));
                                return;
                            }
                        }
                    }
                }
            }
        }
        //Code to draw roads if boolean is in correct state
        if (isRoadBuilding) {
            if (roadCondition != 2) {
                for (int[] indexCoord : indexCoords) {
                    if (Math.abs(indexCoord[0] - xLoc) < 20 && Math.abs(indexCoord[1] - yLoc) < 20) {
                        Index checkedIndex = returnAppropriateIndex(indexCoord[0], indexCoord[1]);
                        for (Index index : indexes) {
                            if (index == checkedIndex) {
                                checkedIndexes.add(index);
                                roadCondition++;
                            }
                        }
                    }
                }
            }

            if (roadCondition == 2) {
                found = false;
                if (indexConnections.size() == 0) {
                    if (checkedIndexes.get(0).getOwner() != getCurrentPlayer() && checkedIndexes.get(1).getOwner() != getCurrentPlayer()) {
                        JOptionPane.showMessageDialog(this, "You need to attach a road to a settlement/city you own. Choose indices that touch at least one of your buildings.", "Road Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (distance(new Point(checkedIndexes.get(0).getLocation()[0], checkedIndexes.get(0).getLocation()[1]), new Point(checkedIndexes.get(1).getLocation()[0], checkedIndexes.get(1).getLocation()[1])) > 100) {
                        JOptionPane.showMessageDialog(this, "Your road can only extend one space. Choose road indices that are next to one another.", "Road Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (distance(new Point(checkedIndexes.get(0).getLocation()[0], checkedIndexes.get(0).getLocation()[1]), new Point(checkedIndexes.get(1).getLocation()[0], checkedIndexes.get(1).getLocation()[1])) == 0) {
                        JOptionPane.showMessageDialog(this, "You cannot choose the same index twice. Your road needs to connect two indices.", "Same Index Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (distance(new Point(checkedIndexes.get(0).getLocation()[0], checkedIndexes.get(0).getLocation()[1]), new Point(checkedIndexes.get(1).getLocation()[0], checkedIndexes.get(1).getLocation()[1])) < 90) {
                        roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                        indexConnections.add(new Road(checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID(), getCurrentPlayer(), new Point((int) ((Point) roadInfo[0]).getX(), (int) ((Point) roadInfo[0]).getY()), roadInfo[1].toString()));
                        repaint();
                    }
                }
                else {
                    for (Road indexConnection : indexConnections) {
                        if ((indexConnection.getIndexA() == checkedIndexes.get(0).getIndexID() && indexConnection.getIndexB() == checkedIndexes.get(1).getIndexID()) || (indexConnection.getIndexA() == checkedIndexes.get(1).getIndexID() && indexConnection.getIndexB() == checkedIndexes.get(0).getIndexID())) {
                            JOptionPane.showMessageDialog(this, "There is already a road here. Choose two different indexes that do not contain a road between them.", "Road Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                            roadCondition = 0;
                            checkedIndexes.clear();
                            found = true;
                            break;
                        }
                    }
                    count = 0;
                    for (Road indexConnection : indexConnections) {
                        if (!doingStartup) {
                            if (indexConnection.getIndexA() == checkedIndexes.get(0).getIndexID())
                                if (indexConnection.getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnection.getIndexA() == checkedIndexes.get(1).getIndexID())
                                if (indexConnection.getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnection.getIndexB() == checkedIndexes.get(0).getIndexID())
                                if (indexConnection.getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnection.getIndexB() == checkedIndexes.get(1).getIndexID())
                                if (indexConnection.getOwner() == getCurrentPlayer())
                                    count++;
                        }
                    }
                    if (!doingStartup && count == 0) {
                        JOptionPane.showMessageDialog(this, "If you want to build a road, attach it to another road you own or another settlement/city you own.", "Road Building Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (checkedIndexes.get(0).getOwner() != getCurrentPlayer() && checkedIndexes.get(1).getOwner() != getCurrentPlayer() && count == 0) {
                        JOptionPane.showMessageDialog(this, "You need to attach a road to a settlement/city you own. Choose indices that touch at least one of your buildings.", "Road Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (distance(new Point(checkedIndexes.get(0).getLocation()[0], checkedIndexes.get(0).getLocation()[1]), new Point(checkedIndexes.get(1).getLocation()[0], checkedIndexes.get(1).getLocation()[1])) == 0) {
                        JOptionPane.showMessageDialog(this, "You cannot choose the same index twice. Your road needs to connect two indices.", "Same Index Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (distance(new Point(checkedIndexes.get(0).getLocation()[0], checkedIndexes.get(0).getLocation()[1]), new Point(checkedIndexes.get(1).getLocation()[0], checkedIndexes.get(1).getLocation()[1])) > 100 && !found) {
                        JOptionPane.showMessageDialog(this, "Your road can only extend one space. Choose road indices that are next to one another.", "Road Error",3, new ImageIcon("Resources/Catan_Icon.png"));
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    } else if (!found) {
                        roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                        indexConnections.add(new Road(checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID(), getCurrentPlayer(), new Point((int) ((Point) roadInfo[0]).getX(), (int) ((Point) roadInfo[0]).getY()), roadInfo[1].toString()));
                        repaint();
                    }
                }
            }
        }

        //Code to draw buildings when boolean is in correct state
        if (isSettlementBuilding) {
            checkedIndexes.clear();
            boolean breakCheck = false;
            for (int[] indexCoord : indexCoords) {
                if (Math.abs(indexCoord[0] - xLoc) < 20 && Math.abs(indexCoord[1] - yLoc) < 20) {
                    Index checkedIndex = returnAppropriateIndex(indexCoord[0], indexCoord[1]);
                    for (int i = 0; i < indexes.length; i++) {
                        breakCheck = true;
                        if (indexes[i] == checkedIndex && !indexes[i].isTaken()) {
                            chosen_x = indexCoord[0] - 5;
                            chosen_y = indexCoord[1] - 16;
                            settlementIndex = i;
                            checked = indexes[i];
                            breakCheck = false;
                            break;
                        }
                    }
                    if (checkedIndex.isTaken() && breakCheck)
                        JOptionPane.showMessageDialog(this, "This spot has already been built upon. Please choose again.", "Spot Taken",3, new ImageIcon("Resources/Catan_Icon.png"));

                    else if (!buildable(checkedIndex))
                        JOptionPane.showMessageDialog(this, "You are within one road-length of another settlement/city. Please choose again.", "Spot Proximity Too Close",3, new ImageIcon("Resources/Catan_Icon.png"));

                    else if (!settlementBuildable(checkedIndex) && !doingStartup)
                        JOptionPane.showMessageDialog(this, "You cannot build here as you aren't connecting this settlement to a road you own.", "Spot Unconnected",3, new ImageIcon("Resources/Catan_Icon.png"));

                    else {
                        indexes[settlementIndex].setTaken(true);
                        indexes[settlementIndex].setOwner(getCurrentPlayer());
                        indexes[settlementIndex].setSettlement(true);
                        getCurrentPlayer().addIndex(indexes[settlementIndex]);
                        getCurrentPlayer().changeVictoryPoints(1);
                        getCurrentPlayer().changeSettlementNum(-1);
                        isSettlementBuilding = false;
                        settlementPaintCondition = true;
                        repaint();
                    }
                }
            }
        }

        if (isMovingRobber) {
            Arrays.stream(tiles).forEach(tile -> tile.setHasRobber(false));

            checkCounter = 0;
            for (Tile tile : tiles)
                if (tile.getRobberRect().intersects(new Rectangle(xLoc, yLoc, 5, 5))) {
                    tile.setHasRobber(true);
                    isDoneMovingRobber = true;
                    isMovingRobber = false;
                    robberTile = tile;
                    redrawEverything=true;
                    checkCounter++;
                    performStaleReferenceReset(true);
                    repaint();
                    break;
                }

            if (checkCounter == 0)
                JOptionPane.showMessageDialog(this, "Click in the center of the tile you'd like to move the robber to.", "Incorrect Robber Positioning",3, new ImageIcon("Resources/Catan_Icon.png"));
        }

        if (isCityUpgrading) {
            int cityCounter = 0;
            for (int x = 0; x < getCurrentPlayer().getOwnedIndexes().size(); x++) {
                if (Math.abs(xLoc - getCurrentPlayer().getOwnedIndexes().get(x).getLocation()[0]) < 25 && Math.abs(yLoc - getCurrentPlayer().getOwnedIndexes().get(x).getLocation()[1]) < 25) {
                    if (getCurrentPlayer().getOwnedIndexes().get(x).isSettlement()) {
                        cityCounter += 1;
                        getCurrentPlayer().getOwnedIndexes().get(x).setSettlement(false);
                        getCurrentPlayer().getOwnedIndexes().get(x).setCity(true);
                        break;
                    }
                }
            }
            if (cityCounter == 1) {
                isCityUpgrading = false;
                redrawEverything = true;
                performStaleReferenceReset(true);
                repaint();
                getCurrentPlayer().changeVictoryPoints(1);
                showBuiltImage("Resources/Preview_Images/City.png","City Construction");
                JOptionPane.showMessageDialog(this, "Your settlement has been upgraded. Your city grants you double the resources it would normally provide.", "Settlement Upgrade Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                getCurrentPlayer().changeCityNum(-1);
                getCurrentPlayer().changeSettlementNum(1);
            }
        }

        if(isSettingFire){
            checkCounter = 0;
            fireResource="";
            for (Tile tile : tiles)
                if (tile.getRobberRect().intersects(new Rectangle(xLoc, yLoc, 5, 5))) {
                    if (!tile.isOnFire()) {
                        if (tile.getType().equals("Mountain")) {
                            failureFire = getCurrentPlayer().getOreNum() == 0;
                            fireResource = "Ore";
                        }

                        if (tile.getType().equals("Grain")) {
                            failureFire = getCurrentPlayer().getGrainNum() == 0;
                            fireResource = "Wheat";
                        }

                        if (tile.getType().equals("Brick")) {
                            failureFire = getCurrentPlayer().getBrickNum() == 0;
                            fireResource = "Brick";
                        }

                        if (tile.getType().equals("Forest")) {
                            failureFire = getCurrentPlayer().getLumberNum() == 0;
                            fireResource = "Lumber";
                        }

                        if (tile.getType().equals("Plains")) {
                            failureFire = getCurrentPlayer().getWoolNum() == 0;
                            fireResource = "Sheep";
                        }

                        if (fireResource.equals("")) {
                            JOptionPane.showMessageDialog(this, "You cannot set fire to the desert.", "Desert Choice", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }

                        if (failureFire) {
                            performStaleReferenceReset(true);
                            JOptionPane.showMessageDialog(this, "You do not have the necessary resource to set fire to that tile. Choose a different tile.", "Arson Match Failure", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }

                        getCurrentPlayer().monoOre(fireResource.equals("Ore") ? -1 : 0);
                        getCurrentPlayer().monoBrick(fireResource.equals("Brick") ? -1 : 0);
                        getCurrentPlayer().monoWheat(fireResource.equals("Wheat") ? -1 : 0);
                        getCurrentPlayer().monoWool(fireResource.equals("Sheep") ? -1 : 0);
                        getCurrentPlayer().monoLumber(fireResource.equals("Lumber") ? -1 : 0);

                        tile.setOnFire(true);
                        tile.setFirePlayer(getCurrentPlayer());
                        redrawEverything = true;
                        checkCounter++;
                        performStaleReferenceReset(true);
                        isSettingFire = false;
                        repaint();
                        JOptionPane.showMessageDialog(this,"You've successfully committed arson. That tile cannot produce resources until your next turn.","Arson Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                        break;
                    }

                    else {
                        JOptionPane.showMessageDialog(this, "That tile is already on fire. Choose a tile that isn't.","Tile On Fire",1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                }

            if (checkCounter == 0)
                JOptionPane.showMessageDialog(this, "Click in the center of the tile you'd like to set fire to.", "Incorrect Arson Positioning",3, new ImageIcon("Resources/Catan_Icon.png"));
        }
        if(isCultivating){
            checkCounter = 0;
            cultivateResource="";
            for (Tile tile : tiles)
                if (tile.getRobberRect().intersects(new Rectangle(xLoc, yLoc, 5, 5))) {
                    if (!tile.isOnFire()) {
                        if (tile.getType().equals("Mountain")) {
                            failureCultivate = getCurrentPlayer().getOreNum() == 0;
                            cultivateResource = "Ore";
                        }

                        if (tile.getType().equals("Grain")) {
                            failureCultivate = getCurrentPlayer().getGrainNum() == 0;
                            cultivateResource = "Wheat";
                        }

                        if (tile.getType().equals("Brick")) {
                            failureCultivate = getCurrentPlayer().getBrickNum() == 0;
                            cultivateResource = "Brick";
                        }

                        if (tile.getType().equals("Forest")) {
                            failureCultivate = getCurrentPlayer().getLumberNum() == 0;
                            cultivateResource = "Lumber";
                        }

                        if (tile.getType().equals("Plains")) {
                            failureCultivate = getCurrentPlayer().getWoolNum() == 0;
                            cultivateResource = "Sheep";
                        }

                        if (cultivateResource.equals("")) {
                            JOptionPane.showMessageDialog(this, "You cannot cultivate a desert. It is completely barren.", "Desert Choice", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }

                        if (failureCultivate) {
                            performStaleReferenceReset(true);
                            JOptionPane.showMessageDialog(this, "You do not have the necessary resource to cultivate this tile. Choose a different tile.", "Cultivation Failure", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }

                        getCurrentPlayer().monoOre(cultivateResource.equals("Ore") ? -1 : 0);
                        getCurrentPlayer().monoBrick(cultivateResource.equals("Brick") ? -1 : 0);
                        getCurrentPlayer().monoWheat(cultivateResource.equals("Wheat") ? -1 : 0);
                        getCurrentPlayer().monoWool(cultivateResource.equals("Sheep") ? -1 : 0);
                        getCurrentPlayer().monoLumber(cultivateResource.equals("Lumber") ? -1 : 0);

                        tile.setCultivated(true);
                        tile.setCultivatingPlayer(getCurrentPlayer());
                        redrawEverything = true;
                        checkCounter++;
                        performStaleReferenceReset(true);
                        isCultivating = false;
                        repaint();
                        JOptionPane.showMessageDialog(this,"You've successfully cultivated. The tile now produces double the resources until your next turn.","New Tile Cultivated",1, new ImageIcon("Resources/Catan_Icon.png"));
                        break;
                    }

                    else {
                        JOptionPane.showMessageDialog(this, "That tile has already been cultivated. Select another tile.","Tile Already Cultivated",1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                }

            if (checkCounter == 0)
                JOptionPane.showMessageDialog(this, "Click in the center of the tile you'd like to cultivate.", "Incorrect Arson Positioning",3, new ImageIcon("Resources/Catan_Icon.png"));

        }
    }

    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    public void constructPorts() {
        ArrayList<Point[]> portPointList = new ArrayList<>();
        ArrayList<String> portTypesList = new ArrayList<>();

        Collections.addAll(portPointList,portPoints);
        Collections.addAll(portTypesList,portTypes);

        for (int x = 0; x < 9; x++) {
            int pointIndex = new Random().nextInt(portPointList.size());
            int stringIndex = new Random().nextInt(portPointList.size());
            ports[x] = new Port(portPointList.get(pointIndex), portTypesList.get(stringIndex));
            portPointList.remove(portPointList.get(pointIndex));
            portTypesList.remove(portTypesList.get(stringIndex));
        }
    }

    public ArrayList<Player> genericGetOtherPlayers(Player thisPlayer){
        return catanPlayerList.stream().filter(player -> !player.equals(thisPlayer)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Player> getOtherPlayers() {
        return catanPlayerList.stream().filter(player -> !player.equals(getCurrentPlayer())).collect(Collectors.toCollection(ArrayList::new));
    }

    public Index returnAppropriateIndex(int chosen_x, int chosen_y) {
        return Arrays.stream(indexes).filter(index -> index.getLocation()[0] == chosen_x && index.getLocation()[1] == chosen_y).findFirst().orElse(new Index());
    }

    public Player getPlayerViaName(String name) {
        return catanPlayerList.stream().filter(player -> player.getName().equals(name)).findFirst().orElse(new Player());
    }

    public Player getPlayerViaNameWithClassTitle(String totalTitle) {
        return catanPlayerList.stream().filter(player -> player.getName().equals(totalTitle.split(" ")[0])).findFirst().orElse(new Player());
    }

    public boolean playerExists(String name){
        return catanPlayerList.stream().anyMatch(player -> player.getName().equals(name));
    }

    public ArrayList<Player> turnOrder(ArrayList<String> nameList) {
        ArrayList<Player> players = new ArrayList<>();
        for (String name : nameList)
            for (Player player : catanPlayerList)
                if (name.equals(player.getName()))
                    players.add(player);

        return players;
    }

    //Checks which road type to draw and where to do so
    public Object[] getRoadPositionAndType(Index indexOne, Index indexTwo) {
        Point indexOneLoc = new Point(indexOne.getLocation()[0], indexOne.getLocation()[1]);
        Point indexTwoLoc = new Point(indexTwo.getLocation()[0], indexTwo.getLocation()[1]);
        int oneX = (int) indexOneLoc.getX();
        int oneY = (int) indexOneLoc.getY();
        int twoX = (int) indexTwoLoc.getX();
        int twoY = (int) indexTwoLoc.getY();

        if (Math.abs(oneX - twoX) < 10 && Math.abs(distance(indexOneLoc, indexTwoLoc) - 77.5) < 10) {
            //Values for creating vertical roads
            if (oneY > twoY)
                return new Object[]{new Point(twoX + 2, twoY + 15), "Vertical"};

            else
                return new Object[]{new Point(oneX + 2, oneY + 15), "Vertical"};
        } else {
            //Values necessary for creating points for the diagonal road polygon
            if (oneX < twoX && oneY < twoY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX + 19, oneY + 10), "Up_To_Down"};

            if (oneX < twoX && oneY > twoY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX + 22, oneY - 30), "Down_To_Up"};

            if (oneX > twoX && oneY > twoY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(twoX + 19, twoY + 10), "Up_To_Down"};

            if (twoX < oneX && twoY > oneY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX - 48, oneY + 8), "Down_To_Up"};
        }
        //Will never get here; cannot figure out a generic way to code this to avoid the gratuitous return statement
        return new Object[]{new Point(0, 0), ""};
    }

    //Finds the distance between two points in cartesian space
    public double distance(Point one, Point two) {
        return Math.sqrt(Math.pow(one.getX() - two.getX(), 2) + Math.pow(one.getY() - two.getY(), 2));
    }

    //Gets resources that would be obtained for an index at start of game
    public ArrayList<String> getAdjacentResources(int xLoc, int yLoc) {
        ArrayList<String> adjacentResources = new ArrayList<>();
        for (Index index : indexes) {
            if (Math.abs(index.getLocation()[0] - xLoc) < 20 && Math.abs(index.getLocation()[1] - yLoc) < 20) {
                for (Tile tile : tiles) {
                    for (int b = 0; b < 6; b++) {
                        if (Math.abs(tile.getVertices().get(b).getX() - xLoc) < 20 && Math.abs(tile.getVertices().get(b).getY() - yLoc) < 20) {
                            adjacentResources.add(tile.getType());
                        }
                    }
                }
            }
        }
        return adjacentResources;
    }

    //Returns the player whose turn is currently taking place
    public Player getCurrentPlayer(){
        return catanPlayerList.stream().filter(Player::isTurn).findFirst().orElse(new Player());
    }

    //Building condition for settlements (one of them)
    public boolean buildable(Index newSpot) {
        for (Index index : indexes)
            if ((index.isTaken() && newSpot != index && distance(new Point(index.getLocation()[0], index.getLocation()[1]), new Point(newSpot.getLocation()[0], newSpot.getLocation()[1])) < 100))
                return false;

        return true;
    }

    public long numDevCardsOfATypeLeft(String devName){
        return (devCardTransparency)?(devCardDeck.stream().filter(card ->card.getType().equals(devName)).count()):((long)properNum.get(devName) - managedCards.stream().filter(card -> card.equals(devName)).count());
    }

    //Building condition for settlements (another of them)
    public boolean settlementBuildable(Index newSpot) {
        int counter = 0;

        for (Road indexConnection : indexConnections) {
            if (newSpot.getIndexID() == indexConnection.getIndexA() || newSpot.getIndexID() == indexConnection.getIndexB()) {
                if (indexConnection.getOwner() == getCurrentPlayer())
                    counter++;
            }
        }
        return counter > 0;
    }

    public void performStartingOperations() {
        int startingPlayer = new Random().nextInt(playerCreation.length);
        doingStartup = true;
        catanPlayerList.forEach(player -> {
            if(player.getRefNumber()==startingPlayer)
                player.setTurn(true);
        });

        //Creating status screen for each player
        statusViewer = new PlayerView[catanPlayerList.size()];
        for (int x = 0; x < catanPlayerList.size(); x++) {
            statusViewer[x] = new PlayerView(catanPlayerList.get(x), this, new TradingFrame(catanPlayerList.get(x), true));
            statusViewer[x].tf.cbRef = this;
            statusViewer[x].setBounds((int) statusGenerationalPoints[x].getX(), (int) statusGenerationalPoints[x].getY(), 475, 353);
            statusViewer[x].pack();
            statusViewer[x].setVisible(true);
            statusViewer[x].colorDisplayLabel.setToolTipText(catanPlayerList.get(x).getName() + " - " + catanPlayerList.get(x).getClassTitle());
        }

        //Construct starting pick order
        ArrayList<Integer> startPickOrder = new ArrayList<>();
        ArrayList<Integer> turnOrder;
        for (int x = 0; x < catanPlayerList.size(); x++) {
            startPickOrder.add((startingPlayer + x) % catanPlayerList.size());
        }
        turnOrder = startPickOrder;

        startPickOrder.addAll(reverse(startPickOrder));
        turnNameList = new ArrayList<>();

        for (int x = 0; x < startPickOrder.size(); x++)
            for (Player player : catanPlayerList)
                if (player.getRefNumber() == turnOrder.get(x)) {
                    turnNameList.add(player.getName());
                }
        JOptionPane.showMessageDialog(this, "You're ready to begin play. Enjoy Settlers of Catan®.", "Beginning Game",1, new ImageIcon("Resources/Catan_Icon.png"));
        JOptionPane.showMessageDialog(this, getTurnOrder(turnNameList), "Turn Order",1, new ImageIcon("Resources/Catan_Icon.png"));

        Arrays.stream(statusViewer).forEach( pv -> {
            pv.options.setEnabled(false);
            pv.build.setEnabled(false);
            pv.development.setEnabled(false);
            pv.arsonist.setEnabled(false);
            pv.hwm.setEnabled(false);
            pv.assassin.setEnabled(false);
            pv.cultivator.setEnabled(false);
            pv.brewer.setEnabled(false);
            pv.shepherd.setEnabled(false);
            pv.woodsman.setEnabled(false);
            pv.mountaineer.setEnabled(false);
            pv.farmer.setEnabled(false);
        });

        for (String s : turnNameList)
            for (Player player : catanPlayerList)
                if (s.equals(player.getName()))
                    duplicates.add(player);

        JOptionPane.showMessageDialog(this, "Place a settlement. Build road from that settlement.", "Road and Settlement Building - "+duplicates.get(0).getName()+(duplicates.get(0).getName().substring(duplicates.get(0).getName().length()-1).equalsIgnoreCase("s")?"'":"'s")+" Building Phase", 1, new ImageIcon("Resources/Catan_Icon.png"));
        this.isSettlementBuilding = true;
        duplicates.get(0).setTurn(true);
    }

    public String getTurnOrder(ArrayList<String> order){
        String[] appositives = {"1  ⇒  ","2  ⇒  ","3  ⇒  ","4  ⇒  "};
        StringBuilder turnString = new StringBuilder("The turn order: \n");
        for(int x=0; x<order.size()/2; x++)
            turnString.append(appositives[x]).append("'").append(order.get(x)).append("'").append(" the ").append(getPlayerViaName(order.get(x)).getClassTitle()).append("\n");

        return turnString.toString();
    }

    //Method to reverse arraylist so as to make a list for placement when game starts
    public ArrayList<Integer> reverse(ArrayList<Integer> list) {
        ArrayList<Integer> reversed = new ArrayList<>();
        for (int x = list.size() - 1; x > -1; x--)
            reversed.add(list.get(x));
        return reversed;
    }

    //Method for resource dispersement
    public ArrayList<Player> getPlayersOnTile(Tile t) {
        ArrayList<Player> players = new ArrayList<>();
        for (int x = 0; x < t.getVertices().size(); x++) {
            for (Index index : indexes) {
                if (Math.abs(t.getVertices().get(x).getX() - index.getLocation()[0]) < 30 && Math.abs(t.getVertices().get(x).getY() - index.getLocation()[1]) < 30)
                    if (index.getOwner() != getCurrentPlayer() && !players.contains(index.getOwner()) && !index.getOwner().getName().equals(""))
                        players.add(index.getOwner());
            }
        }
        return players;
    }

    public void givePlayersStartingResources() {
        for (Player player : catanPlayerList) {
            ArrayList<Index> ownedIndexes = getOwnedIndexes(player);
            for (Index ownedIndex : ownedIndexes) {
                ArrayList<String> startResources = getAdjacentResources(ownedIndex.getLocation()[0], ownedIndex.getLocation()[1]);
                for (String startResource : startResources) {
                    switch (startResource) {
                        case "Grain":
                            player.changeGrain(1);
                            break;
                        case "Brick":
                            player.changeBrick(1);
                            break;
                        case "Forest":
                            player.changeLumber(1);
                            break;
                        case "Plains":
                            player.changeWool(1);
                            break;
                        case "Mountain":
                            player.changeOre(1);
                            break;
                    }
                }
            }
            getPlayerStatusMenu(player).update();
        }
    }

    public PlayerView getPlayerStatusMenu(Player player) {
        return Arrays.stream(statusViewer).filter(players -> players.player.equals(player)).findFirst().orElse(new PlayerView());
    }

    public ArrayList<Index> getOwnedIndexes(Player player) {
        return Optional.ofNullable(player).map(p -> Arrays.stream(indexes).filter(index -> index.getOwner().equals(player)).collect(Collectors.toCollection(ArrayList::new))).orElse(new ArrayList<>());
    }

    public void updateAllStatusMenus() {
        Arrays.stream(statusViewer).forEach(PlayerView::update);
    }

    //Give out appropriate resources after roll
    public void giveOutResources(int roll) {
        for (Tile tile : tiles)
            if (tile.getNum() == roll)
                for (int y = 0; y < 6; y++)
                    for (Index index : indexes)
                        if (Math.abs(tile.getVertices().get(y).getX() - index.getLocation()[0]) < 35 && Math.abs(tile.getVertices().get(y).getY() - index.getLocation()[1]) < 35 && !tile.isHasRobber())
                            for (Player player : catanPlayerList)
                                if (index.getOwner().equals(player) && !getPlayerStatusMenu(player).hasStolen && !tile.isOnFire())
                                    switch (tile.getType()) {
                                        case "Grain":
                                            player.changeGrain((tile.isCultivated()?2:1)*(index.isCity()?2:1));
                                            break;
                                        case "Brick":
                                            player.changeBrick((tile.isCultivated()?2:1)*(index.isCity()?2:1));
                                            break;
                                        case "Forest":
                                            player.changeLumber((tile.isCultivated()?2:1)*(index.isCity()?2:1));
                                            break;
                                        case "Plains":
                                            player.changeWool((tile.isCultivated()?2:1)*(index.isCity()?2:1));
                                            break;
                                        case "Mountain":
                                            player.changeOre((tile.isCultivated()?2:1)*(index.isCity()?2:1));
                                            break;
                                    }

    }

    //Updates status menus after dice is rolled
    public boolean resourceValsGiven(int roll) {
        ArrayList<String> interim = new ArrayList<>();
        int check = 0;
        for (Player player : catanPlayerList) {
            interim.clear();
            interim.add(player.getName());
            for (Tile tile : tiles)
                if (tile.getNum() == roll)
                    for (int y = 0; y < 6; y++)
                        for (Index index : indexes)
                            if (Math.abs(tile.getVertices().get(y).getX() - index.getLocation()[0]) < 35 && Math.abs(tile.getVertices().get(y).getY() - index.getLocation()[1]) < 35 && !tile.isHasRobber())
                                if (index.getOwner().equals(player) && !getPlayerStatusMenu(player).hasStolen && !tile.isOnFire())
                                    switch (tile.getType()) {
                                        case "Grain":
                                            for (int x = 0; x < player.grainMult() * (tile.isCultivated() ? 2 : 1) * (index.isCity() ? 2 : 1); x++)
                                                interim.add("Grain");
                                            break;
                                        case "Plains":
                                            for (int x = 0; x < player.woolMult() * (tile.isCultivated() ? 2 : 1) * (index.isCity() ? 2 : 1); x++)
                                                interim.add("Plains");
                                            break;
                                        case "Brick":
                                            for (int x = 0; x < player.brickMult() * (tile.isCultivated() ? 2 : 1) * (index.isCity() ? 2 : 1); x++)
                                                interim.add("Brick");
                                            break;
                                        case "Forest":
                                            for (int x = 0; x < player.lumberMult() * (tile.isCultivated() ? 2 : 1) * (index.isCity() ? 2 : 1); x++)
                                                interim.add("Forest");
                                            break;
                                        case "Mountain":
                                            for (int x = 0; x < player.oreMult() * (tile.isCultivated() ? 2 : 1) * (index.isCity() ? 2 : 1); x++)
                                                interim.add("Mountain");
                                            break;
                                    }


            check += (interim.size() > 1) ? 1 : 0;
            getPlayerStatusMenu(getPlayerViaName(interim.get(0))).showResourceChanges((int) interim.stream().filter(str -> str.equals("Brick")).count(), (int) interim.stream().filter(str -> str.equals("Mountain")).count(), (int) interim.stream().filter(str -> str.equals("Grain")).count(), (int) interim.stream().filter(str -> str.equals("Plains")).count(), (int) interim.stream().filter(str -> str.equals("Forest")).count());
        }
        if(check==0)
            catanPlayerList.forEach(player -> getPlayerStatusMenu(player).update());

        return check==0;
    }

    public String giveRandomResource(Player player) {
        ArrayList<String> resources = new ArrayList<>();
        if (player.getLumberNum() > 0)
            resources.add("Lumber");
        if (player.getBrickNum() > 0)
            resources.add("Brick");
        if (player.getGrainNum() > 0)
            resources.add("Wheat");
        if (player.getWoolNum() > 0)
            resources.add("Sheep");
        if (player.getOreNum() > 0)
            resources.add("Ore");

        return (resources.size()!=0) ? resources.get(new Random().nextInt(resources.size())) : "";
    }

    public void largestArmy(Player player) {
        int knightCounter = (int)(player.getPlayedCards()).stream().filter(card -> card.getType().equals("Knight")).count();

        if (knightCounter > currentLargestArmy && largestArmyPlayer != getCurrentPlayer()) {
            if(currentLargestArmy!=2) {
                largestArmyPlayer.setLargestArmy(false);
                largestArmyPlayer.changeVictoryPoints(-2);
            }
            player.setLargestArmy(true);
            player.changeVictoryPoints(2);
            largestArmyPlayer = player;
            currentLargestArmy = knightCounter;
            JOptionPane.showMessageDialog(this, "There is a new largest army of size " + getPlayerStatusMenu(getCurrentPlayer()).alphaNumeric.get(knightCounter) + " controlled by " + player.getName() + ".", "New Largest Army",1, new ImageIcon("Resources/Catan_Icon.png"));
            updateAllStatusMenus();
        }
    }

    public void usePort(Port port) {
        showBuiltImage("Resources/Preview_Images/Port.png", "Port Usage");
        try {
            String use = "";
            switch (port.getType()) {
                case "Wheat":
                    if (getCurrentPlayer().getGrainNum() < 2) {
                        JOptionPane.showMessageDialog(this, "You need at least two wheat to use this port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade two wheat for a single resource?", "Wheat Port", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {
                        while (numberChecked() == 0 || numberChecked() > 1) {
                            Arrays.stream(checkOptions).filter(checkBox-> checkBox.getText().equals("Wheat")).findFirst().orElse(new JCheckBox()).setEnabled(false);
                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange wheat for:", checkOptions}, "Wheat Exchange", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Sheep"))
                            getCurrentPlayer().monoWool(1);

                        if (chosenPort.getText().equals("Ore"))
                            getCurrentPlayer().monoOre(1);

                        if (chosenPort.getText().equals("Brick"))
                            getCurrentPlayer().monoBrick(1);

                        if (chosenPort.getText().equals("Lumber"))
                            getCurrentPlayer().monoLumber(1);

                        getCurrentPlayer().monoWheat(-2);
                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                    break;

                case "Sheep":
                    if (getCurrentPlayer().getWoolNum() < 2) {
                        JOptionPane.showMessageDialog(this, "You need at least two sheep to use this port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade two sheep for a single resource?", "Sheep Port", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {

                        while (numberChecked() == 0 || numberChecked() > 1) {
                            Arrays.stream(checkOptions).filter(checkBox-> checkBox.getText().equals("Sheep")).findFirst().orElse(new JCheckBox()).setEnabled(false);
                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange sheep for:", checkOptions}, "Sheep Exchange", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }

                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Ore"))
                            getCurrentPlayer().monoOre(1);

                        if (chosenPort.getText().equals("Brick"))
                            getCurrentPlayer().monoBrick(1);

                        if (chosenPort.getText().equals("Lumber"))
                            getCurrentPlayer().monoLumber(1);

                        if(chosenPort.getText().equals("Wheat"))
                            getCurrentPlayer().monoWheat(1);

                        getCurrentPlayer().monoWool(-2);
                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                    break;

                case "Ore":
                    if (getCurrentPlayer().getOreNum() < 2) {
                        JOptionPane.showMessageDialog(this, "You need at least two ore to use this port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade two ore for a single resource?", "Ore Port", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {

                        while (numberChecked() == 0 || numberChecked() > 1) {
                            Arrays.stream(checkOptions).filter(checkBox-> checkBox.getText().equals("Ore")).findFirst().orElse(new JCheckBox()).setEnabled(false);
                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange ore for:", checkOptions}, "Ore Exchange", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Sheep"))
                            getCurrentPlayer().monoWool(1);

                        if (chosenPort.getText().equals("Brick"))
                            getCurrentPlayer().monoBrick(1);

                        if (chosenPort.getText().equals("Lumber"))
                            getCurrentPlayer().monoLumber(1);

                        if(chosenPort.getText().equals("Wheat"))
                            getCurrentPlayer().monoWheat(1);

                        getCurrentPlayer().monoOre(-2);
                        JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    }
                    break;

                case "Brick":
                    if (getCurrentPlayer().getBrickNum() < 2) {
                        JOptionPane.showMessageDialog(this, "You need at least two brick to use this port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade two brick for a single resource?", "Brick Port", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {
                        while (numberChecked() == 0 || numberChecked() > 1) {
                            Arrays.stream(checkOptions).filter(checkBox-> checkBox.getText().equals("Brick")).findFirst().orElse(new JCheckBox()).setEnabled(false);
                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange brick for:", checkOptions}, "Brick Exchange", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }

                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Sheep"))
                            getCurrentPlayer().monoWool(1);

                        if (chosenPort.getText().equals("Ore"))
                            getCurrentPlayer().monoOre(1);

                        if (chosenPort.getText().equals("Lumber"))
                            getCurrentPlayer().monoLumber(1);

                        if(chosenPort.getText().equals("Wheat"))
                            getCurrentPlayer().monoWheat(1);
                    }

                    getCurrentPlayer().monoBrick(-2);
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    break;

                case "Lumber":
                    if (getCurrentPlayer().getLumberNum() < 2) {
                        JOptionPane.showMessageDialog(this, "You need at least two lumber to use this port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }
                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade two lumber for a single resource?", "Lumber Port", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {
                        while (numberChecked() == 0 || numberChecked() > 1) {
                            Arrays.stream(checkOptions).filter(checkBox-> checkBox.getText().equals("Lumber")).findFirst().orElse(new JCheckBox()).setEnabled(false);
                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange lumber for:", checkOptions}, "Lumber Exchange", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Sheep"))
                            getCurrentPlayer().monoWool(1);

                        if (chosenPort.getText().equals("Ore"))
                            getCurrentPlayer().monoOre(1);

                        if (chosenPort.getText().equals("Brick"))
                            getCurrentPlayer().monoBrick(1);

                        if(chosenPort.getText().equals("Wheat"))
                            getCurrentPlayer().monoWheat(1);
                    }
                    getCurrentPlayer().monoLumber(-2);
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1, new ImageIcon("Resources/Catan_Icon.png"));
                    break;

                case "Generic":
                    if(getCurrentPlayer().hasInsufficientGenericResources(3)){
                        JOptionPane.showMessageDialog(this, "You need at least three of any one resource to use this port.", "Port Not Available", 1, new ImageIcon("Resources/Catan_Icon.png"));
                        return;
                    }

                    while (use.equals(""))
                        use = (String) JOptionPane.showInputDialog(this, "Would you like to trade three of a resource for another single resource?", "Generic Port", 1, new ImageIcon("Resources/Catan_Icon.png"), null, null);
                    if (use.equalsIgnoreCase("yes")) {
                        String resourceChoice = "";
                        while (!(resourceChoice.equalsIgnoreCase("Sheep") || resourceChoice.equalsIgnoreCase("Lumber") || resourceChoice.equalsIgnoreCase("Brick") || resourceChoice.equalsIgnoreCase("Ore") || resourceChoice.equalsIgnoreCase("Wheat"))) {
                            resourceChoice = JOptionPane.showInputDialog(this, "Type in the resource you'd like to trade three in of: Sheep - Lumber - Ore - Brick - Wheat", "Generic Action", 1);

                            if (!(resourceChoice.equalsIgnoreCase("Sheep") || resourceChoice.equalsIgnoreCase("Lumber") || resourceChoice.equalsIgnoreCase("Brick") || resourceChoice.equalsIgnoreCase("Ore") || resourceChoice.equalsIgnoreCase("Wheat")))
                                JOptionPane.showMessageDialog(this, "That is not an accepted resource. Try again.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        if (resourceChoice.equalsIgnoreCase("Sheep") && getCurrentPlayer().getWoolNum() < 3) {
                            JOptionPane.showMessageDialog(this, "You don't have enough sheep to use this generic port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        if (resourceChoice.equalsIgnoreCase("Wheat") && getCurrentPlayer().getGrainNum() < 3) {
                            JOptionPane.showMessageDialog(this, "You don't have enough wheat to use this generic port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        if (resourceChoice.equalsIgnoreCase("Brick") && getCurrentPlayer().getBrickNum() < 3) {
                            JOptionPane.showMessageDialog(this, "You don't have enough brick to use this generic port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        if (resourceChoice.equalsIgnoreCase("Ore") && getCurrentPlayer().getOreNum() < 3) {
                            JOptionPane.showMessageDialog(this, "You don't have enough ore to use this generic port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        if (resourceChoice.equalsIgnoreCase("Lumber") && getCurrentPlayer().getLumberNum() < 3) {
                            JOptionPane.showMessageDialog(this, "You don't have enough lumber to use this generic port.", "Inadequate Resources", 1, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }

                        while (numberChecked() == 0 || numberChecked() > 1) {
                            String finalResourceChoice = resourceChoice;
                            Arrays.stream(checkOptions).filter(box -> box.getText().equalsIgnoreCase(finalResourceChoice)).findFirst().orElse(new JCheckBox()).setEnabled(false);

                            JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange for:", checkOptions}, "Generic Exchange", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                            if (numberChecked() > 1)
                                JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3, new ImageIcon("Resources/Catan_Icon.png"));
                        }
                        chosenPort = Arrays.stream(checkOptions).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());

                        if (chosenPort.getText().equals("Sheep"))
                            getCurrentPlayer().monoWool(1);

                        if (chosenPort.getText().equals("Ore"))
                            getCurrentPlayer().monoOre(1);

                        if (chosenPort.getText().equals("Brick"))
                            getCurrentPlayer().monoBrick(1);

                        if(chosenPort.getText().equals("Wheat"))
                            getCurrentPlayer().monoWheat(1);

                        if(chosenPort.getText().equals("Lumber"))
                            getCurrentPlayer().monoLumber(1);

                        if (resourceChoice.equalsIgnoreCase("Wheat"))
                            getCurrentPlayer().monoWheat(-3);

                        if (resourceChoice.equalsIgnoreCase("Sheep"))
                            getCurrentPlayer().monoWool(-3);

                        if (resourceChoice.equalsIgnoreCase("Ore"))
                            getCurrentPlayer().monoOre(-3);

                        if (resourceChoice.equalsIgnoreCase("Brick"))
                            getCurrentPlayer().monoBrick(-3);

                        if (resourceChoice.equalsIgnoreCase("Lumber"))
                            getCurrentPlayer().monoLumber(-3);
                    }
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    break;
            }
            updateAllStatusMenus();
            resetPortBoxes();
        } catch (NullPointerException e) {
            JOptionPane.showMessageDialog(this, "The port use has been cancelled.", "Port Error", JOptionPane.WARNING_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        }
    }

    public void getPortsReady() {
        checkOptions = new JCheckBox[5];
        for (int x = 0; x < 5; x++)
            checkOptions[x] = new JCheckBox(portTypes[x + 4]);
    }

    public void resetPortBoxes() {
        Arrays.stream(checkOptions).forEach(box -> {
            box.setEnabled(true);
            box.setSelected(false);
        });
    }

    public void givePlayersCatanBoardReference() {
        catanPlayerList.forEach(player -> player.cb=this);
    }

    public long numberChecked() {
        return Arrays.stream(checkOptions).filter(AbstractButton::isSelected).count();
    }

    //Longest road algorithm using recursion
    public boolean runRoadAlgorithm(){
        ArrayList<Road> singleConnections = new ArrayList<>();
        ArrayList<Integer> lengths = new ArrayList<>();

        for (Road indexConnection : indexConnections) {
            if (indexConnection.getOwner() == getCurrentPlayer())
                singleConnections.add(indexConnection);
        }

        for (Road singleConnection : singleConnections)
            lengths.add(roadRecursion(getPlayerRoads(getCurrentPlayer()), new ArrayList<>(), 0, singleConnection));

        int previous = this.currentLongestRoad;
        this.currentLongestRoad = (Collections.max(lengths)>this.currentLongestRoad)?Collections.max(lengths):this.currentLongestRoad;
        return Collections.max(lengths)>previous;
    }

    public int roadRecursion(ArrayList<Road> potential, ArrayList<Road> usedAlready, int currentLength, Road currentRoad){
        ArrayList<Road> possibleSplittings= new ArrayList<>();
        ArrayList<Integer> splits = new ArrayList<>();
        boolean stillHasConnections = true;
        int subConnections;
        usedAlready.add(currentRoad);
        currentLength++;

        while(stillHasConnections) {
            potential.removeAll(usedAlready);
            stillHasConnections=false;
            possibleSplittings.clear();
            subConnections=0;
            for (Road road : potential)
                if (currentRoad.isConnectedTo(road) && currentRoad != road) {
                    subConnections++;
                    possibleSplittings.add(road);
                }

            if (subConnections > 1) {
                usedAlready.addAll(possibleSplittings);
                for (Road possibleSplitting : possibleSplittings)
                    splits.add(roadRecursion(potential, usedAlready, 0, possibleSplitting));

                currentLength += Collections.max(splits);
                return currentLength;
            }
            else if (subConnections == 1) {
                currentRoad = possibleSplittings.get(0);
                usedAlready.add(currentRoad);
                currentLength++;
                stillHasConnections=true;
            }
        }
        return currentLength;
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_X){
            int quit = JOptionPane.showConfirmDialog(this,"Would you like to stop playing Settlers of Catan®?","Quit Game",JOptionPane.YES_NO_OPTION,1,new ImageIcon("Resources/Catan_Icon.png"));
            if(quit==JOptionPane.YES_OPTION){
                JOptionPane.showMessageDialog(this,"Thank you for playing.","See you next time",JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                System.exit(1);
            }
            return;
        }
        if(e.getKeyCode()==KeyEvent.VK_C && isPlayerActing && !doingStartup) {
            int confirmCancellation = JOptionPane.showConfirmDialog(this, "Would you like to cancel your current action?", "Cancellation", JOptionPane.YES_NO_OPTION, 1, new ImageIcon("Resources/Catan_Icon.png"));
            if (confirmCancellation != 0) {
                JOptionPane.showMessageDialog(this, "Please continue the action you were performing.", "Action Continued", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                return;
            }
            if (!doingStartup && isPlayerActing) {
                if (isSettlementBuilding) {
                    isSettlementBuilding = false;
                    getCurrentPlayer().monoAll(1,1,1,0,1);
                } else if (isMovingRobber) {
                    isMovingRobber = false;
                    isDoneMovingRobber = false;
                    getCurrentPlayer().addDevelopmentCardToUnplayed(new DevelopmentCard("Knight", getCurrentPlayer(), getOtherPlayers(), this, false));
                    getCurrentPlayer().removeDevelopmentCardFromPlayed(new DevelopmentCard("Knight", getCurrentPlayer(), getOtherPlayers(), this, false));
                    getPlayerStatusMenu(getCurrentPlayer()).unplayed.addItem("Knight");
                    getPlayerStatusMenu(getCurrentPlayer()).played.removeItem("Knight");
                    managedCards.remove("Knight");
                } else if (isRoadBuilding) {
                    isRoadBuilding = false;
                    isDoneRoadBuilding = false;
                    roadCondition = 0;
                    checkedIndexes.clear();
                    getCurrentPlayer().monoAll(1,0,1,0,0);

                } else if (isCityUpgrading) {
                    isCityUpgrading = false;
                    getCurrentPlayer().monoAll(0,0,0,3,2);
                }
                else if(isSettingFire){
                    isSettingFire=false;
                    getPlayerStatusMenu(getCurrentPlayer()).hasSetFire=false;
                    JOptionPane.showMessageDialog(this,"Your arsonist action has been cancelled. Please continue with your turn.","Cancellation Successful",1, new ImageIcon("Resources/Catan_Icon.png"));
                    isPlayerActing=false;
                    getPlayerStatusMenu(getCurrentPlayer()).resetReference(true);
                    updateAllStatusMenus();
                    return;
                }
            }
            isPlayerActing = false;
            getPlayerStatusMenu(getCurrentPlayer()).resetReference(true);
            updateAllStatusMenus();
            JOptionPane.showMessageDialog(this, "Your action has been cancelled and your resources have been refunded. Please continue with your turn.", "Cancellation Successful", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
        }

        if(e.getKeyCode()==KeyEvent.VK_1) {
            try {
                cheat = JOptionPane.showInputDialog(this, "Enter a cheat code: ", "Cheat Menu", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null).toString();

                switch (cheat) {
                    case "Unlimited":
                        getCurrentPlayer().monoAll(1000, 1000, 1000, 1000, 1000);
                        break;

                    case "Decimate":
                        getOtherPlayers().forEach(Player::empty);
                        getOtherPlayers().forEach(Player::destroyVPs);
                        break;

                    case "Development":
                        for (DevelopmentCard devCard : devCardDeck) {
                            devCard.boughtThisTurn = false;
                            getCurrentPlayer().addDevelopmentCardToUnplayed(devCard);
                            devCard.setPlayer(getCurrentPlayer());
                            devCard.setOtherPlayers(getOtherPlayers());
                            getPlayerStatusMenu(getCurrentPlayer()).unplayed.addItem(devCard.getType());
                            getPlayerStatusMenu(getCurrentPlayer()).readdDevCards(getPlayerStatusMenu(getCurrentPlayer()).unplayed);
                        }
                        devCardDeck.clear();
                        break;

                    case "Builder":
                        getCurrentPlayer().updateBuildingMaterials(100, 100, 100);
                        break;

                    case "Activate Ports":
                        if (usablePorts) {
                            JOptionPane.showMessageDialog(this, "Ports are already active.", "Ports Active", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        usablePorts = true;
                        redrawEverything = true;
                        repaint();
                        break;

                    case "Deactivate Ports":
                        if (!usablePorts) {
                            JOptionPane.showMessageDialog(this, "Ports are already disabled.", "Ports Active", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                            return;
                        }
                        usablePorts = false;
                        redrawEverything = true;
                        repaint();
                        break;

                    case "VP Manipulation":
                        while(vpCheat==0) {
                            try {
                                vpCheat = Integer.parseInt((String) JOptionPane.showInputDialog(this, "What is your victory point total?", "Victory Point Change", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"), null, null));

                                if (vpCheat < 1 || vpCheat > 11)
                                    throw new Exception();

                            } 
                            catch (Exception repeat) {
                                JOptionPane.showMessageDialog(this, "You must select an integer between 1 and 10.", "Improper Number Choice", 1, new ImageIcon("Resources/Catan_Icon.png"));
                                vpCheat = 0;
                            }
                        }
                        getCurrentPlayer().changeVictoryPoints(vpCheat);
                        break;
                }

                if (codes.contains(cheat)) {
                    JOptionPane.showMessageDialog(this, "Cheat code activated.", "Cheat Entered", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
                    updateAllStatusMenus();
                    return;
                }
                JOptionPane.showMessageDialog(this, "There is no such cheat code.", "Invalid Cheat", JOptionPane.INFORMATION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
            }
            catch(Exception ignored){}
        }
    }

    public ArrayList<Road> getPlayerRoads(Player player){
        return indexConnections.stream().filter(conn -> conn.getOwner().equals(player)).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean sharesLocation(Point point){
        return Arrays.stream(indexes).filter(index -> (Math.abs(point.getX() - index.getLocation()[0]) < 10 && Math.abs(point.getY() - index.getLocation()[1]) < 10) && index.isTaken()).count() <=0;
    }

    public Port getPortAtLocation(Point[] points){
        return Arrays.stream(ports).filter(port -> Arrays.equals(port.getLocations(), points)).findFirst().orElse(new Port());
    }

    public void cataclysm(){
        String chosenCataclysm = cataclysms[new Random().nextInt(cataclysms.length)];
        Player afflictedPlayer = catanPlayerList.get(new Random().nextInt(catanPlayerList.size()));
        switch(chosenCataclysm) {
            case "Famine":
                showBuiltImage("Resources/Preview_Images/Famine.png","Cataclysm - Famine");
                JOptionPane.showMessageDialog(this,"A famine sweeps over "+afflictedPlayer.getName()+"'s  lands, forcing them to eat all their sheep to stay alive. "+afflictedPlayer.getName()+" loses all sheep resources.","Ravaging Famine",3, new ImageIcon("Resources/Catan_Icon.png"));
                afflictedPlayer.setWoolNum(0);
                break;

            case "Locust":
                showBuiltImage("Resources/Preview_Images/Locust.png","Cataclysm - Swarm of Locust");
                JOptionPane.showMessageDialog(this,"A swarm of locusts flood from the desert and ravage all of "+afflictedPlayer.getName()+"'s wheat fields. "+afflictedPlayer.getName()+" loses all wheat resources.","Locust Invasion",3, new ImageIcon("Resources/Catan_Icon.png"));
                afflictedPlayer.setGrainNum(0);
                break;

            case "Fire":
                showBuiltImage("Resources/Preview_Images/Fires.png","Cataclysm - Forest Fires");
                JOptionPane.showMessageDialog(this,"A terrible drought engulfs "+afflictedPlayer.getName()+"'s woodlands and causes massive fires, burning down their forests. "+afflictedPlayer.getName()+" loses all lumber resources.","Massive Drought",3, new ImageIcon("Resources/Catan_Icon.png"));
                afflictedPlayer.setLumberNum(0);
                break;

            case "Strike":
                showBuiltImage("Resources/Preview_Images/Revolt.jpg","Cataclysm - Serf Uprising");
                JOptionPane.showMessageDialog(this,afflictedPlayer.getName()+"'s serf laborers revolt because of poor treatment and take all their building supplies with them. "+afflictedPlayer.getName()+" loses all brick resources.","Labor Revolt",3, new ImageIcon("Resources/Catan_Icon.png"));
                afflictedPlayer.setBrickNum(0);
                break;

            case "Monsoon":
                showBuiltImage("Resources/Preview_Images/Monsoon.png","Cataclysm - Rainstorms");
                JOptionPane.showMessageDialog(this,"There are massive rainstorms emerging over "+afflictedPlayer.getName()+"'s mountains, instigating quick-erosion of their ore. "+afflictedPlayer.getName()+" loses all ore resources.","Labor Revolt",3, new ImageIcon("Resources/Catan_Icon.png"));
                afflictedPlayer.setOreNum(0);
                break;
        }
        updateAllStatusMenus();
    }

    public void showBuiltImage(String name, String title){
        if(previewFrames && !alreadyPreviewed.contains(name)) {
            alreadyPreviewed.add(name);
            buildLabel.setIcon(new ImageIcon(name));
            buildFrame.setTitle(title);
            buildFrame.setVisible(true);
            buildFrame.setLocation(getPlayerStatusMenu(getCurrentPlayer()).getX()+Math.abs((getPlayerStatusMenu(getCurrentPlayer()).getWidth()-buildFrame.getWidth())/2),getPlayerStatusMenu(getCurrentPlayer()).getY()+Math.abs((getPlayerStatusMenu(getCurrentPlayer()).getHeight()-buildFrame.getWidth())/2));
        }
    }

    public boolean gamblerIsPresent(){
        return catanPlayerList.stream().anyMatch(player -> player.getClassTitle().equals("Gambler"));
    }

    public boolean highwaymanIsPresent(){
        return catanPlayerList.stream().anyMatch(player -> player.getClassTitle().equals("Highwayman"));
    }

    public void performStaleReferenceReset(boolean state){
        PlayerView menuRef = getPlayerStatusMenu(getCurrentPlayer());
        Arrays.stream(new JMenu[]{menuRef.options,menuRef.development,menuRef.build,menuRef.assassin,menuRef.hwm,menuRef.arsonist, menuRef.cultivator, menuRef.pirate,menuRef.brewer, menuRef.shepherd,menuRef.woodsman,menuRef.farmer,menuRef.mountaineer}).forEach(menu -> menu.setEnabled(state));
    }

    public void randomize(){
        Arrays.stream(tiles).forEach(tile ->{
            tile.setType(types[new Random().nextInt(19)]);
            tile.setNum((tile.getType().equalsIgnoreCase("Desert"))?7:new Random().nextInt(11)+2);
        });
        Arrays.stream(tiles).filter(tile -> tile.getNum()==7 && !tile.getType().equalsIgnoreCase("Desert")).forEach(tile -> tile.setNum(tile.getNum()+(new Random().nextInt(5)+1)*(new Random().nextInt(2)==0?-1:1)));
        redrawEverything=true;
        this.repaint();
    }

    public void razeTiles() {
        long before = Arrays.stream(tiles).filter(tile->tile.getType().equals("Desert")).count();
        Arrays.stream(tiles).filter(tile -> !tile.getType().equals("Desert") && new Random().nextInt(100) < 1).forEach(tile -> {
            tile.setType("Desert");
            tile.setNum(7);
        });

        if(Arrays.stream(tiles).filter(tile -> tile.getType().equals("Desert")).count()!=before) {
            redrawEverything = true;
            repaint();
            showBuiltImage("Resources/Preview_Images/Desertification.png","Deserts Formed");
            JOptionPane.showMessageDialog(this, "New deserts have been formed. The resource production of Catan shrinks!", "Desert Formation", 1, new ImageIcon("Resources/Catan_Icon.png"));
        }

        if(Arrays.stream(tiles).allMatch(tile -> tile.getType().equals("Desert")) && !randomize){
            JOptionPane.showMessageDialog(this,"The entirety of Catan has become a desert. The game is a draw. Click the board to end the game.","Catan Has Been Forsaken",1, new ImageIcon("Resources/Catan_Icon.png"));
            completeDesertification=true;
        }
    }

    public void performMonarchSelection(){
        leaderIndex = new Random().nextInt(catanPlayerList.size());

        catanPlayerList.forEach(player -> {
            player.setLeader(false);
        });

        if(!singleShowMonarchy) {
            showBuiltImage("Resources/Preview_Images/Monarchy.jpg", "New Monarch Selected");
            singleShowMonarchy = true;
        }

        JOptionPane.showMessageDialog(this,catanPlayerList.get(leaderIndex).getName()+" the "+catanPlayerList.get(leaderIndex).getClassTitle()+" has been selected as the leader for this round.","Monarch Chosen",1, new ImageIcon("Resources/Catan_Icon.png"));
        catanPlayerList.get(leaderIndex).setLeader(true);
        catanPlayerList.forEach(player ->{
            if(player.isDrunk && player.isLeader()){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_Confounded_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader & Confounded");
            }
            else if(player.isDrunk){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Confounded_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Confounded");
            }
            else if(player.isLeader()){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader");
            }
            else {
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Normal_" + player.getColor() + ".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Normal");
            }
            getPlayerStatusMenu(player).update();
        });
    }

    public void performDemocracyVoting(){
        leaderIndex= new Random().nextInt(catanPlayerList.size());
        votes = new ArrayList<>();
        maxVotesPlayer = new ArrayList<>();

        catanPlayerList.forEach(player -> player.setLeader(false));

        if(!singleShowDemocracy) {
            showBuiltImage("Resources/Preview_Images/Democracy.jpg", "Electing a New Leader");
            singleShowDemocracy = true;
        }

        if(catanPlayerList.size()==2){
            JOptionPane.showMessageDialog(this,catanPlayerList.get(leaderIndex).getName()+" the "+catanPlayerList.get(leaderIndex).getClassTitle()+" has been selected as the leader for this round.","Leader Randomized",1, new ImageIcon("Resources/Catan_Icon.png"));
            catanPlayerList.get(leaderIndex).setLeader(true);
            catanPlayerList.forEach(player ->{
                if(player.isDrunk && player.isLeader()){
                    getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_Confounded_"+player.getColor()+".png"));
                    getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader & Confounded");
                }
                else if(player.isDrunk){
                    getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Confounded_"+player.getColor()+".png"));
                    getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Confounded");
                }
                else if(player.isLeader()){
                    getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_"+player.getColor()+".png"));
                    getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader");
                }
                else {
                    getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Normal_" + player.getColor() + ".png"));
                    getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Normal");
                }
                getPlayerStatusMenu(player).update();
                player.votes=0;
            });
            return;
        }

        for(int x=0; x<catanPlayerList.size(); x++){
            while(statusViewer[0].findNumSelected(democracyBoxes)) {
                JOptionPane.showMessageDialog(getPlayerStatusMenu(catanPlayerList.get(x)), new Object[]{"Vote for the next leader:", democracyBoxes}, "Democracy Voting", 1, new ImageIcon("Resources/Catan_Icon.png"));

                if (statusViewer[0].findNumSelected(democracyBoxes))
                    JOptionPane.showMessageDialog(getPlayerStatusMenu(catanPlayerList.get(x)), "You must vote for a single player to be leader.", "Democracy Failure", 1, new ImageIcon("Resources/Catan_Icon.png"));
            }
            JCheckBox selectedBox = Arrays.stream(democracyBoxes).filter(AbstractButton::isSelected).findFirst().orElse(new JCheckBox());
            for(Player player:catanPlayerList)
                player.addVotes(selectedBox.getText().equals(player.getName())?1:0);

            Arrays.stream(democracyBoxes).forEach(box->box.setEnabled(true));
            Arrays.stream(democracyBoxes).forEach(box->box.setSelected(false));
        }

        for(Player player:catanPlayerList)
            votes.add(player.getVotes());

        for(Player player:catanPlayerList)
            if(Collections.max(votes).equals(player.getVotes()))
                maxVotesPlayer.add(player);

        if(maxVotesPlayer.size()>2){
            JOptionPane.showMessageDialog(this, (maxVotesPlayer.size() == 3 ? "Three" : "Four") + " players have received the same number of votes, so " + catanPlayerList.get(leaderIndex).getName() + " the " + catanPlayerList.get(leaderIndex).getClassTitle() + " has been randomly selected to be the leader.","Leader Randomized",1, new ImageIcon("Resources/Catan_Icon.png"));
            catanPlayerList.get(leaderIndex).setLeader(true);
        }
        else{
            JOptionPane.showMessageDialog(this,maxVotesPlayer.get(0).getName()+" the "+maxVotesPlayer.get(0).getClassTitle()+" has been elected leader for this turn cycle.","Leader Elected",1, new ImageIcon("Resources/Catan_Icon.png"));
            maxVotesPlayer.get(0).setLeader(true);
        }
        catanPlayerList.forEach(player ->{
            if(player.isDrunk && player.isLeader()){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_Confounded_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader & Confounded");
            }
            else if(player.isDrunk && !player.isLeader()){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Confounded_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Confounded");
            }
            else if(player.isLeader() && !player.isDrunk){
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Leader_"+player.getColor()+".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Leader");
            }
            else {
                getPlayerStatusMenu(player).statusDisplayLabel.setIcon(new ImageIcon("Resources/Status/Normal_" + player.getColor() + ".png"));
                getPlayerStatusMenu(player).statusDisplayLabel.setToolTipText("Status: Normal");
            }
            getPlayerStatusMenu(player).update();
            player.votes=0;
        });
    }

    public void endGame(){
        this.setVisible(false);
        Arrays.stream(statusViewer).forEach(frame -> frame.setVisible(false));
    }

    @Override
    public void keyTyped(KeyEvent e){}
    public void keyReleased(KeyEvent e){}
    public void mouseClicked(MouseEvent e){}
}
