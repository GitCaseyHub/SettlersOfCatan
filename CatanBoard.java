import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CatanBoard extends JFrame implements MouseListener, KeyListener {
    //Longest Road needs to deal with forking issue; causes it to think there is an additional road there. I think that I need to do this by forcing the program to check at only one index

    //Objects for Board Generation
    String[] types = {"Mountain","Mountain","Mountain","Brick","Brick","Brick","Forest","Forest","Forest","Forest","Plains","Plains","Plains","Plains","Grain","Grain","Grain","Grain","Desert"};
    int[] rollNums = {8,4,11,12,3,11,10,9,6,9,5,2,4,5,10,8,3,6};
    int[][] coords1 = {{267,87},{267+134,87},{267+2*134,87},{200,200},{334,200},{200+2*134,200},{200+3*134,200},{133,313},{133+134,313},{133+2*134,313},{133+3*134,313},{666,313},{200,426},{200+134,426},{200+134*2,426},{200+134*3,426},{267,426+113},{267+134,426+113},{267+134*2,426+113}};
    ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<Integer> rollNumList = new ArrayList<Integer>();
    ArrayList<int[]> coordList = new ArrayList<int[]>();
    Tile tiles[] = new Tile[19];
    PlayerView[] statusViewer;
    ArrayList<String> turnNameList;

    //Water Tiles
    Point[] waterPoints1 = new Point[]{new Point(201,-30), new Point(334,-30), new Point(467,-30), new Point(600,-30), new Point(133,84), new Point(66,198), new Point(0,312), new Point(67,429), new Point(133,543), new Point(333,656), new Point(466,656), new Point(600,656), new Point(201,656), new Point(668,84), new Point(734,198), new Point(800,312), new Point(668, 543), new Point(734,429)};
    Point[] outLinePoints1 = new Point[]{new Point(1,431), new Point(66,467), new Point(66,545), new Point(133,584), new Point(133,659), new Point(200,698), new Point(200,770), new Point(266,811), new Point(333,774), new Point(398,811), new Point(465,774), new Point(530,811), new Point(599,772), new Point(662,811),new Point(733,773),new Point(733,697),new Point(800,659), new Point(800,584), new Point(866,546), new Point(866,468), new Point(929,429), new Point(929,348), new Point(868,313), new Point(868,237), new Point(800,198), new Point(800,122), new Point(732,84), new Point(732,8), new Point(664,-30), new Point(599,8), new Point(532,-30), new Point(467,8), new Point(400,-30), new Point(334,8), new Point(268,-30), new Point(202,8), new Point(202,84), new Point(133,122), new Point(133,197), new Point(65,236), new Point(65,311), new Point(1,348)};

    //Frame shaping objects
    int[] framex = new int[outLinePoints1.length];
    int[] framey = new int[outLinePoints1.length];

    //Paint/Repaint Conditions
    boolean loaded=false;
    boolean settlementPaintCondition=false;
    int chosen_x = 0;
    int chosen_y = 0;

    //Booleans for conditions on when MouseListeners should activate
    boolean isRoadBuilding=false, isSettlementBuilding=false;
    boolean isDoneRoadBuilding=false, isDoneSettlementBuilding=false;
    boolean doingStartup=false, found=false;
    boolean isMovingRobber=false, isDoneMovingRobber=false;
    boolean roadDevCard=false, finishedRoadCard = false;
    boolean isCityUpgrading=false;
    boolean redrawEverything=false;
    boolean usablePorts=false;
    boolean isPlayerActing=false;
    int count=0;

    //Awards
    int currentLongestRoad=4;
    Player longestRoadPlayer = new Player();
    int currentLargestArmy=2;
    Player largestArmyPlayer = new Player();

    //Trading Variables
    TradingFrame firstFrame;
    TradingFrame secondFrame;

    //Index Creation
    ArrayList<int[]> coord = new ArrayList<int[]>();
    int[][] indexCoords1 = {{264,122},{330,87},{398,122},{461,87},{533,121},{599,87},{658,122},{663,200},{599,234},{532,200},{463,233},{396,201},{329,235},{262,202},{196,235},{197,312},{262,347},{329,312},{394,348},{461,311},{528,346},{599,312},{665,350},{727,313},{729,237},{797,346},{798,425},{725,461},{660,423},{597,457},{527,424},{462,459},{393,422},{327,460},{258,421},{198,459},{132,423},{130,345},{200,536},{263,572},{329,537},{393,574},{464,534},{526,574},{599,536},{655,646},{594,683},{461,687},{392,646},{526,654},{266,647},{332,686},{664,577},{731,536}};
    Index[] indexes = new Index[indexCoords1.length];
    Index checked;

    //Port Creation
    Point[][] portPoints1 = new Point[][]{new Point[]{new Point(264,122),new Point(330,87), new Point(219,18)},
                                         new Point[]{new Point(461,87),new Point(533,121), new Point(486,18)},
                                         new Point[]{new Point(663,200),new Point(729,237), new Point(681,132)},
                                         new Point[]{new Point(797,346),new Point(798,425), new Point(810,351)},
                                         new Point[]{new Point(731,536),new Point(664,577), new Point(681,556)},
                                         new Point[]{new Point(526,654),new Point(461,687), new Point(486,685)},
                                         new Point[]{new Point(332,686),new Point(266,647), new Point(219,685)},
                                         new Point[]{new Point(200,536),new Point(198,459), new Point(88,468)},
                                         new Point[]{new Point(198,235),new Point(197,312), new Point(88,244)}};
    String[] portTypes = {"Generic","Generic","Generic","Generic","Sheep","Wheat","Ore","Brick","Wood"};
    Port[] ports = new Port[9];
    int portCount=0;
    JCheckBox[] checkOptions;

    //Robber Objects
    Tile robberTile;
    JCheckBox[] possibleTargets;
    int checkCounter=0;

    //Building Variables
    int roadCondition=0;
    ArrayList<Index> checkedIndexes = new ArrayList<Index>();
    Object[] roadInfo;
    ArrayList<Road> indexConnections = new ArrayList<Road>();
    int settlementIndex=0;
    
    //Constructor Variables
    ArrayList<Player> catanPlayerList;
    Point[] statusGenerationalPoints;
    PlayerSelect[] playerCreation;
    ArrayList<Player> duplicates = new ArrayList<Player>();
    BeginGame bgReference;

    //Duplicates
    int[][] coords = new int[coords1.length][];
    int[][] indexCoords = new int[indexCoords1.length][];
    Point[] waterPoints= new Point[waterPoints1.length];
    Point[] outLinePoints = new Point[outLinePoints1.length];
    Point[][] portPoints = new Point[portPoints1.length][];

    public CatanBoard(ArrayList<Player> catanPlayerList, Point[] statusGenerationalPoints, PlayerSelect[] playerCreation, BeginGame bgReference){
        for(int x=0; x<coords1.length; x++)
            coords[x] = new int[]{coords1[x][0],coords1[x][1]+50};

        for(int x=0; x<indexCoords1.length; x++)
            indexCoords[x] = new int[]{indexCoords1[x][0],indexCoords1[x][1]+50};

        for(int x=0; x<waterPoints1.length; x++)
            waterPoints[x] = new Point((int)waterPoints1[x].getX(),(int)waterPoints1[x].getY()+50);

        for(int x=0; x<outLinePoints1.length; x++)
            outLinePoints[x] = new Point((int)outLinePoints1[x].getX(),(int)outLinePoints1[x].getY()+50);

        for(int x=0; x<portPoints1.length; x++)
            portPoints[x] = new Point[]{new Point((int)portPoints1[x][0].getX(),(int)portPoints1[x][0].getY()+50),new Point((int)portPoints1[x][1].getX(),(int)portPoints1[x][1].getY()+50),new Point((int)portPoints1[x][2].getX(),(int)portPoints1[x][2].getY()+50)};

        this.addMouseListener(this);
        this.addKeyListener(this);
        this.catanPlayerList=catanPlayerList;
        this.statusGenerationalPoints=statusGenerationalPoints;
        this.playerCreation=playerCreation;
        this.bgReference=bgReference;
        
        for(int x=0; x<types.length; x++)
            typeList.add(types[x]);

        for(int x=0; x<rollNums.length; x++)
            rollNumList.add(rollNums[x]);

        for(int x=0; x<coords.length; x++)
            coordList.add(coords[x]);

        for(int x=0; x<indexes.length; x++)
            indexes[x] = new Index(indexCoords[x], false, x, new Player("","","",new ArrayList<Index>(),new ArrayList<DevelopmentCard>(),new ArrayList<DevelopmentCard>(),0,0,0,0,0,0,false,false,false,69), false, false);

        for(int x=0; x<types.length; x++){
            int typeIndex = new Random().nextInt(typeList.size());
            int coordIndex = new Random().nextInt(coordList.size());
            tiles[x] = new Tile(coordList.get(coordIndex),typeList.get(typeIndex),0,false);
            typeList.remove(typeIndex);
            coordList.remove(coordIndex);

            if(tiles[x].getType().equals("Desert")) {
                tiles[x].setNum(7);
                tiles[x].setHasRobber(true);
            }

            else{
                int rollIndex = new Random().nextInt(rollNumList.size());
                tiles[x].setNum(rollNumList.get(rollIndex));
                rollNumList.remove(rollIndex);
            }
        }

        for(int x=0; x<outLinePoints.length; x++) {
            framex[x] = (int) outLinePoints[x].getX();
            framey[x] = (int) outLinePoints[x].getY();
        }

        //Methods to take care of things before startup; the method names are pretty self-explanatory on what they are doing
        constructPorts();
        getPortsReady();
        givePlayersCatanBoardReference();

        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                setShape(new Polygon(framex,framey,outLinePoints.length));
            }
        });
    }

    public void paint(Graphics g) {
        try {
            if (!loaded) {
                for (int x = 0; x < tiles.length; x++) {
                    BufferedImage tile = ImageIO.read(new File("Tiles/" + tiles[x].getType() + ".png"));
                    g.drawImage(tile, tiles[x].getPosition()[0], tiles[x].getPosition()[1], null);
                }

                for(int x=0; x<tiles.length; x++){
                    BufferedImage dice = ImageIO.read(new File("Rolls/"+tiles[x].getNum()+".png"));
                    g.drawImage(dice,(int)tiles[x].getPosition()[0]+42,(int)tiles[x].getPosition()[1]+25,null);
                }

                BufferedImage water = ImageIO.read(new File("Tiles/Water_Tile.png"));
                for(int x=0; x<waterPoints.length; x++)
                    g.drawImage(water,(int)waterPoints[x].getX(),(int)waterPoints[x].getY(),null);

                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(10,30,150));
                for(int x=0; x<outLinePoints.length; x++)
                    g2.drawLine((int)outLinePoints[x].getX(),(int)outLinePoints[x].getY(),(int)outLinePoints[(x+1)%outLinePoints.length].getX(), (int)outLinePoints[(x+1)%outLinePoints.length].getY());

                for(int x=0; x<ports.length; x++){
                    BufferedImage port = ImageIO.read(new File("Resources/Port/"+ports[x].getType()+"_Port_Ship.png"));
                    g.drawImage(port,(int)ports[x].getLocations()[2].getX(),(int)ports[x].getLocations()[2].getY(),null);
                }

                for(int x=0; x<tiles.length; x++)
                    if(tiles[x].isHasRobber()){
                        BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                        g.drawImage(robber,tiles[x].getPosition()[0]+57,tiles[x].getPosition()[1]+80,null);
                    }

                //Draws Ports
                if(bgReference.usablePorts) {
                    //Port Indexes if Active
                    BufferedImage circle = ImageIO.read(new File("Pieces/Active_Ports.png"));
                    for (int x = 0; x < portPoints.length; x++) {
                        g.drawImage(circle, (int) portPoints[x][0].getX(), (int) portPoints[x][0].getY(), null);
                        g.drawImage(circle, (int) portPoints[x][1].getX(), (int) portPoints[x][1].getY(), null);
                    }
                }
                else{
                    //Port Indexes if Inactive
                    BufferedImage cross = ImageIO.read(new File("Pieces/Cross_Ports.png"));
                    for (int x = 0; x < portPoints.length; x++) {
                        g.drawImage(cross,(int) portPoints[x][0].getX(), (int) portPoints[x][0].getY(), null);
                        g.drawImage(cross,(int) portPoints[x][1].getX(), (int) portPoints[x][1].getY(), null);
                    }
                }
                loaded=true;
            }
            if(isDoneMovingRobber){
                BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                redrawEverything=true;
                for(int x=0; x<tiles.length; x++)
                    if(tiles[x].isHasRobber()){
                        g.drawImage(robber,tiles[x].getPosition()[0]+57,tiles[x].getPosition()[1]+80,null);
                        isDoneMovingRobber=false;
                        break;
                    }

               if(checkCounter!=0)
                    JOptionPane.showMessageDialog(this,"The robber has been moved.","Robber Moved",1);

                ArrayList<Player> availablePlayers = getPlayersOnTile(robberTile);
                if(availablePlayers.size()!=0) {
                    possibleTargets = new JCheckBox[availablePlayers.size()];
                    for(int x=0; x<availablePlayers.size(); x++)
                        possibleTargets[x] = new JCheckBox(availablePlayers.get(x).getName());

                    String message = "Which player would you like to steal a resource from?";
                    String playerName="";
                    Object[] params = {message, possibleTargets};
                    int selectCounter=0;
                    while(selectCounter==0 || (selectCounter==possibleTargets.length && possibleTargets.length!=1)){
                        selectCounter=0;
                        JOptionPane.showMessageDialog(this, params, "Robber Action", JOptionPane.INFORMATION_MESSAGE);

                        for(int x=0; x<possibleTargets.length; x++)
                            if(possibleTargets[x].isSelected())
                                selectCounter++;

                        if(selectCounter==0)
                            JOptionPane.showMessageDialog(this,"You have to steal from someone. You cannot elect out of this.","Try Again",3);

                        if(selectCounter==possibleTargets.length && possibleTargets.length!=1) {
                            for (int x = 0; x < possibleTargets.length; x++)
                                possibleTargets[x].setSelected(false);

                            JOptionPane.showMessageDialog(this, "You may only steal from one player.", "Try Again", 3);
                        }
                    }

                    for(int x=0; x<possibleTargets.length; x++)
                        if(possibleTargets[x].isSelected()) {
                            playerName = possibleTargets[x].getText();
                            break;
                        }

                    for(int x=0; x<possibleTargets.length; x++)
                        possibleTargets[x].setSelected(false);

                    Player playerToStealFrom = getPlayerViaName(playerName);
                    String stolenResource = giveRandomResource(playerToStealFrom);
                    if(stolenResource.equals(""))
                        JOptionPane.showMessageDialog(this,"Unfortunately, "+playerToStealFrom.getName()+" has no resources. So, you've stolen nothing.","Robber Failure",3);

                    else{
                        if(stolenResource.equals("Sheep")) {
                            getCurrentPlayer().monoWool(1);
                            playerToStealFrom.monoWool(-1);
                        }
                        if(stolenResource.equals("Ore")) {
                            getCurrentPlayer().monoOre(1);
                            playerToStealFrom.monoOre(-1);
                        }
                        if(stolenResource.equals("Brick")) {
                            getCurrentPlayer().monoBrick(1);
                            playerToStealFrom.monoBrick(-1);
                        }
                        if(stolenResource.equals("Lumber")) {
                            getCurrentPlayer().monoLumber(1);
                            playerToStealFrom.monoLumber(-1);
                        }
                        if(stolenResource.equals("Wheat")) {
                            getCurrentPlayer().monoWheat(1);
                            playerToStealFrom.monoWheat(-1);
                        }
                        JOptionPane.showMessageDialog(this,"You've stolen "+stolenResource+" from "+playerToStealFrom.getName()+".","Robber Success",1);
                        updateAllStatusMenus();
                    }
                }
                largestArmy(getCurrentPlayer(),currentLargestArmy);
                isDoneMovingRobber=false;
            }
            if (settlementPaintCondition) {
                //Drawing Settlements Test
                BufferedImage settlement = ImageIO.read(new File("Pieces/"+getCurrentPlayer().getColor()+"_Settlement.png"));
                g.drawImage(settlement, chosen_x, chosen_y, null);
                getCurrentPlayer().changeVictoryPoints(1);
                settlementPaintCondition = false;
                isDoneSettlementBuilding=true;
                if(doingStartup)
                    isRoadBuilding=true;
            }

            if(roadCondition==2 && !found){
                Point testPoint = (Point)roadInfo[0];
                BufferedImage road = ImageIO.read(new File("Pieces/"+roadInfo[1]+"_"+getCurrentPlayer().getColor()+"_Road.png"));
                g.drawImage(road,(int)testPoint.getX(),(int)testPoint.getY(),null);
                roadCondition=0;
                checkedIndexes.clear();
                isDoneRoadBuilding=true;
                isRoadBuilding=false;

                if(longestRoad(currentLongestRoad) && longestRoadPlayer!=getCurrentPlayer()){
                    if(currentLongestRoad!=4) {
                        for (int x = 0; x < catanPlayerList.size(); x++)
                            if (catanPlayerList.get(x).hasLongestRoad()) {
                                catanPlayerList.get(x).setLongestRoad(false);
                                catanPlayerList.get(x).changeVictoryPoints(-2);
                                break;
                            }
                    }
                    getCurrentPlayer().setLongestRoad(true);
                    getCurrentPlayer().changeVictoryPoints(2);
                    JOptionPane.showMessageDialog(this,(currentLongestRoad!=4)?"There is a new longest road comprised of "+currentLongestRoad+" segments by "+getCurrentPlayer().getName()+".":"The longest road award has been claimed by "+getCurrentPlayer().getName()+", who has a road of "+currentLongestRoad+" continuous segments.","New Longest Road",1);
                    this.longestRoadPlayer=getCurrentPlayer();
                    updateAllStatusMenus();
                }

                if(doingStartup) {
                    if (duplicates.size() > 0) {
                        duplicates.get(0).setTurn(false);
                        duplicates.remove(0);

                        if (duplicates.size() > 0) {
                            duplicates.get(0).setTurn(true);
                            JOptionPane.showMessageDialog(this, "Now, " + duplicates.get(0).getName() + ", select an index to build on, and then a direction you'd like to build a road.", "Road and Settlement Building", 1);
                            isSettlementBuilding = true;
                        }
                    }

                    if (duplicates.size() == 0) {
                        doingStartup = false;
                        for(int x=0; x<catanPlayerList.size(); x++)
                            if(catanPlayerList.get(x).getName().equals(turnNameList.get(0)))
                                catanPlayerList.get(x).setTurn(true);

                        getPlayerStatusMenu(catanPlayerList.get(0)).update();
                        isDoneSettlementBuilding = true;
                        JOptionPane.showMessageDialog(this, "The game is ready to officially start. Based on your first settlement placements, you will be given the appropriate resources. " + catanPlayerList.get(0).getName() + ", you proceed first.", "Free Play", 1);
                        givePlayersStartingResources();
                    }
                }
                else {
                    if(!roadDevCard && !finishedRoadCard)
                        JOptionPane.showMessageDialog(this, "You've built a new road.", "Road Building", 1);

                    else if (roadDevCard){
                        JOptionPane.showMessageDialog(this,"You've built a new road. Now, create your second road.","Road Building",1);
                        isRoadBuilding=true;
                        roadDevCard=false;
                        finishedRoadCard=true;
                    }
                    else if(finishedRoadCard){
                        JOptionPane.showMessageDialog(this,"You've created your two roads.","Finished Action",1);
                        getPlayerStatusMenu(getCurrentPlayer()).options.setEnabled(true);
                        getPlayerStatusMenu(getCurrentPlayer()).development.setEnabled(true);
                        getPlayerStatusMenu(getCurrentPlayer()).build.setEnabled(true);
                        finishedRoadCard=false;
                    }
                }
            }

            if(redrawEverything){
                //Redraws Tiles
                for (int x = 0; x < tiles.length; x++) {
                    BufferedImage tile = ImageIO.read(new File("Tiles/" + tiles[x].getType() + ".png"));
                    g.drawImage(tile, tiles[x].getPosition()[0], tiles[x].getPosition()[1], null);
                }

                //Redraws Water Tiles
                BufferedImage water = ImageIO.read(new File("Tiles/Water_Tile.png"));
                for(int x=0; x<waterPoints.length; x++)
                    g.drawImage(water,(int)waterPoints[x].getX(),(int)waterPoints[x].getY(),null);

                //Redraws Water Border
                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(10,30,150));
                for(int x=0; x<outLinePoints.length; x++)
                    g2.drawLine((int)outLinePoints[x].getX(),(int)outLinePoints[x].getY(),(int)outLinePoints[(x+1)%outLinePoints.length].getX(), (int)outLinePoints[(x+1)%outLinePoints.length].getY());

                //Redraws Port Ships
                for(int x=0; x<ports.length; x++){
                    BufferedImage port = ImageIO.read(new File("Resources/Port/"+ports[x].getType()+"_Port_Ship.png"));
                    g.drawImage(port,(int)ports[x].getLocations()[2].getX(),(int)ports[x].getLocations()[2].getY(),null);
                }

                //Redraws Roll Tiles
                for(int x=0; x<tiles.length; x++){
                    BufferedImage dice = ImageIO.read(new File("Rolls/"+tiles[x].getNum()+".png"));
                    g.drawImage(dice,(int)tiles[x].getPosition()[0]+42,(int)tiles[x].getPosition()[1]+25,null);
                }

                //Draws Robber
                for(int x=0; x<tiles.length; x++)
                    if(tiles[x].isHasRobber()){
                        BufferedImage robber = ImageIO.read(new File("Pieces/Robber.png"));
                        g.drawImage(robber,tiles[x].getPosition()[0]+57,tiles[x].getPosition()[1]+80,null);
                    }

                //Redraws Ports
                if(bgReference.usablePorts) {
                    //Port Indexes if Active
                    BufferedImage circle = ImageIO.read(new File("Pieces/Active_Ports.png"));
                    for (int x = 0; x < portPoints.length; x++) {
                        g.drawImage(circle, (int) portPoints[x][0].getX(), (int) portPoints[x][0].getY(), null);
                        g.drawImage(circle, (int) portPoints[x][1].getX(), (int) portPoints[x][1].getY(), null);
                    }
                }
                else{
                    //Port Indexes if Inactive
                    BufferedImage cross = ImageIO.read(new File("Pieces/Cross_Ports.png"));
                    for (int x = 0; x < portPoints.length; x++) {
                        g.drawImage(cross,(int) portPoints[x][0].getX(), (int) portPoints[x][0].getY(), null);
                        g.drawImage(cross,(int) portPoints[x][1].getX(), (int) portPoints[x][1].getY(), null);
                    }
                }

                //Draws Cities/Settlements
                for(int x=0; x<indexes.length; x++){
                    if(indexes[x].isTaken()){
                        if(indexes[x].isSettlement()){
                            BufferedImage settlement = ImageIO.read(new File("Pieces/"+indexes[x].getOwner().getColor()+"_Settlement.png"));
                            g.drawImage(settlement,indexes[x].getLocation()[0]-5, indexes[x].getLocation()[1]-16,null);
                        }
                        else if(indexes[x].isCity()) {
                            BufferedImage city = ImageIO.read(new File("Pieces/"+indexes[x].getOwner().getColor()+"_City.png"));
                            g.drawImage(city,indexes[x].getLocation()[0]-5, indexes[x].getLocation()[1]-16,null);
                        }
                    }
                }

                //Redraw Roads
                for(int x=0; x<indexConnections.size(); x++){
                    BufferedImage road = ImageIO.read(new File("Pieces/"+indexConnections.get(x).getRoadType()+"_"+indexConnections.get(x).getOwner().getColor()+"_Road.png"));
                    g.drawImage(road,(int)indexConnections.get(x).getPosition().getX(),(int)indexConnections.get(x).getPosition().getY(),null);
                }

                redrawEverything=false;
            }
        }
        catch(IOException ie){
            ie.printStackTrace();
        }

        if(!doingStartup) {
            getPlayerStatusMenu(getCurrentPlayer()).options.setEnabled(true);
            getPlayerStatusMenu(getCurrentPlayer()).build.setEnabled(true);
            getPlayerStatusMenu(getCurrentPlayer()).development.setEnabled(true);
        }
        updateAllStatusMenus();
    }

    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        int xLoc = e.getX();
        int yLoc = e.getY();
        
        //Testing
        //System.out.println("(X,Y): ( "+xLoc+" , "+yLoc+" )");

        //Code to draw ports
        if(bgReference.usablePorts && !doingStartup) {
            portCount = 0;
            for (int x = 0; x < ports.length; x++) {
                if (new Rectangle(xLoc, yLoc, 10, 10).intersects(new Rectangle((int) ports[x].getLocations()[2].getX() + 25, (int) ports[x].getLocations()[2].getY() + 25, 50, 50))) {
                    for (int y = 0; y < indexes.length; y++) {
                        if ((Math.abs(ports[x].getLocations()[0].getX() - indexes[y].getLocation()[0]) < 25 && Math.abs(ports[x].getLocations()[0].getY() - indexes[y].getLocation()[1]) < 25) ||
                                (Math.abs(ports[x].getLocations()[1].getX() - indexes[y].getLocation()[0]) < 25 && Math.abs(ports[x].getLocations()[1].getY() - indexes[y].getLocation()[1]) < 25)) {

                            if(indexes[y].isTaken() && indexes[y].getOwner().getName().equals(getCurrentPlayer().getName()))
                                portCount++;

                        }
                    }
                    if(portCount>0)
                        usePort(ports[x]);

                    else
                        JOptionPane.showMessageDialog(this,"You don't have access to this port.","Port inaccessible",3);
                }
            }
        }
        //Code to draw roads if boolean is in correct state
        if(isRoadBuilding) {
            if (roadCondition != 2) {
                for (int x = 0; x < indexCoords.length; x++) {
                    if (Math.abs(indexCoords[x][0] - xLoc) < 20 && Math.abs(indexCoords[x][1] - yLoc) < 20) {
                        Index checkedIndex = returnAppropIndex(indexCoords[x][0], indexCoords[x][1]);
                        for (int i = 0; i < indexes.length; i++) {
                            if (indexes[i] == checkedIndex) {
                                checkedIndexes.add(indexes[i]);
                                roadCondition++;
                            }
                        }
                    }
                }
            }

            if (roadCondition == 2) {
                found=false;
                if(indexConnections.size()==0){
                    if(checkedIndexes.get(0).getOwner()!=getCurrentPlayer() && checkedIndexes.get(1).getOwner()!=getCurrentPlayer()){
                        JOptionPane.showMessageDialog(this,"You need to attach a road to a settlement/city you own. Choose indices that touch at least one of your buildings.","Road Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }
                    else if(distance(new Point(checkedIndexes.get(0).getLocation()[0],checkedIndexes.get(0).getLocation()[1]),new Point(checkedIndexes.get(1).getLocation()[0],checkedIndexes.get(1).getLocation()[1]))>100){
                        JOptionPane.showMessageDialog(this,"Your road can only extend one space. Choose road indices that are next to one another.","Road Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }

                    else if(distance(new Point(checkedIndexes.get(0).getLocation()[0],checkedIndexes.get(0).getLocation()[1]),new Point(checkedIndexes.get(1).getLocation()[0],checkedIndexes.get(1).getLocation()[1]))==0){
                        JOptionPane.showMessageDialog(this,"You cannot choose the same index twice. Your road needs to connect two indices.","Same Index Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }

                    else if(distance(new Point(checkedIndexes.get(0).getLocation()[0],checkedIndexes.get(0).getLocation()[1]),new Point(checkedIndexes.get(1).getLocation()[0],checkedIndexes.get(1).getLocation()[1]))<90) {
                        roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                        indexConnections.add(new Road(checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID(),getCurrentPlayer(),new Point((int)((Point)roadInfo[0]).getX(),(int)((Point)roadInfo[0]).getY()),roadInfo[1].toString()));
                        repaint();
                    }
                }
                else{
                    for(int x=0; x<indexConnections.size(); x++){
                        if((indexConnections.get(x).getIndexA()==checkedIndexes.get(0).getIndexID() && indexConnections.get(x).getIndexB()==checkedIndexes.get(1).getIndexID()) ||(indexConnections.get(x).getIndexA()==checkedIndexes.get(1).getIndexID() && indexConnections.get(x).getIndexB()==checkedIndexes.get(0).getIndexID())){
                            JOptionPane.showMessageDialog(this,"There is already a road here. Choose two different indexes that do not contain a road between them.","Road Error",3);
                            roadCondition=0;
                            checkedIndexes.clear();
                            found=true;
                            break;
                        }
                    }
                    count=0;
                    for(int x=0; x<indexConnections.size(); x++) {
                        if(!doingStartup) {
                            if (indexConnections.get(x).getIndexA() == checkedIndexes.get(0).getIndexID())
                                if (indexConnections.get(x).getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnections.get(x).getIndexA() == checkedIndexes.get(1).getIndexID())
                                if (indexConnections.get(x).getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnections.get(x).getIndexB() == checkedIndexes.get(0).getIndexID())
                                if (indexConnections.get(x).getOwner() == getCurrentPlayer())
                                    count++;

                            if (indexConnections.get(x).getIndexB() == checkedIndexes.get(1).getIndexID())
                                if (indexConnections.get(x).getOwner() == getCurrentPlayer())
                                    count++;
                        }
                    }
                    if(!doingStartup && count==0) {
                        JOptionPane.showMessageDialog(this, "If you want to build a road, attach it to another road you own or another settlement/city you own.", "Road Building Error", 3);
                        roadCondition = 0;
                        checkedIndexes.clear();
                        found = true;
                    }

                    else if(checkedIndexes.get(0).getOwner()!=getCurrentPlayer() && checkedIndexes.get(1).getOwner()!=getCurrentPlayer() && count==0){
                        JOptionPane.showMessageDialog(this,"You need to attach a road to a settlement/city you own. Choose indices that touch at least one of your buildings.","Road Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }
                    else if(distance(new Point(checkedIndexes.get(0).getLocation()[0],checkedIndexes.get(0).getLocation()[1]),new Point(checkedIndexes.get(1).getLocation()[0],checkedIndexes.get(1).getLocation()[1]))==0){
                        JOptionPane.showMessageDialog(this,"You cannot choose the same index twice. Your road needs to connect two indices.","Same Index Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }

                    else if(distance(new Point(checkedIndexes.get(0).getLocation()[0],checkedIndexes.get(0).getLocation()[1]),new Point(checkedIndexes.get(1).getLocation()[0],checkedIndexes.get(1).getLocation()[1]))>100 && !found){
                        JOptionPane.showMessageDialog(this,"Your road can only extend one space. Choose road indices that are next to one another.","Road Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }
                    else if(!found){
                        roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                        indexConnections.add(new Road(checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID(),getCurrentPlayer(),new Point((int)((Point)roadInfo[0]).getX(),(int)((Point)roadInfo[0]).getY()),roadInfo[1].toString()));
                        repaint();
                    }
                }
            }
        }

        //Code to draw buildings when boolean is in correct state
        if(isSettlementBuilding){
            checkedIndexes.clear();
            boolean breakCheck = false;
            for (int x = 0; x < indexCoords.length; x++) {
                if (Math.abs(indexCoords[x][0] - xLoc) < 20 && Math.abs(indexCoords[x][1] - yLoc) < 20) {
                    Index checkedIndex = returnAppropIndex(indexCoords[x][0], indexCoords[x][1]);
                    for (int i = 0; i < indexes.length; i++) {
                        breakCheck = true;
                        if (indexes[i] == checkedIndex && !indexes[i].isTaken()) {
                            chosen_x = indexCoords[x][0] - 5;
                            chosen_y = indexCoords[x][1] - 16;
                            settlementIndex=i;
                            checked=indexes[i];
                            breakCheck = false;
                            break;
                        }
                    }
                    if (checkedIndex.isTaken() && breakCheck)
                        JOptionPane.showMessageDialog(this, "This spot has already been built upon. Please choose again..","Spot Taken",3);

                    else if(!buildable(checkedIndex))
                        JOptionPane.showMessageDialog(this,"You are within one road-length of another settlement/city. Please choose again.","Spot Proximity Too Close",3);

                    else if(!settlementBuildable(checkedIndex) && !doingStartup)
                        JOptionPane.showMessageDialog(this,"You cannot build here as you aren't connecting this settlement to a road you own.","Spot Unconnected",3);

                    else {
                        indexes[settlementIndex].setTaken(true);
                        indexes[settlementIndex].setOwner(getCurrentPlayer());
                        indexes[settlementIndex].setSettlement(true);
                        getCurrentPlayer().addIndex(indexes[settlementIndex]);
                        isSettlementBuilding = false;
                        settlementPaintCondition=true;
                        repaint();
                    }
                }
            }
        }

        if(isMovingRobber) {
            checkCounter = 0;
            int reCheckRobber=0;

            for (int x = 0; x < tiles.length; x++)
                if (tiles[x].getRobberRect().intersects(new Rectangle(xLoc, yLoc, 5, 5))) {
                    tiles[x].setHasRobber(true);
                    isDoneMovingRobber = true;
                    isMovingRobber = false;
                    robberTile = tiles[x];
                    reCheckRobber=x;
                    repaint();
                    checkCounter++;
                    getPlayerStatusMenu(getCurrentPlayer()).options.setEnabled(true);
                    getPlayerStatusMenu(getCurrentPlayer()).build.setEnabled(true);
                    getPlayerStatusMenu(getCurrentPlayer()).development.setEnabled(true);

                    for (int y = 0; y < tiles.length; y++)
                        if (tiles[y].isHasRobber() && y!=x)
                            tiles[x].setHasRobber(false);
                    break;
                }
            redrawEverything=true;
            if (checkCounter == 0)
                JOptionPane.showMessageDialog(this, "Click in the center of the tile you'd like to move the robber to.", "Incorrect Robber Positioning", 3);
        }

        if(isCityUpgrading) {
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
                redrawEverything=true;
                getPlayerStatusMenu(getCurrentPlayer()).options.setEnabled(true);
                getPlayerStatusMenu(getCurrentPlayer()).build.setEnabled(true);
                getPlayerStatusMenu(getCurrentPlayer()).development.setEnabled(true);
                repaint();
                JOptionPane.showMessageDialog(this, "Your settlement has been upgraded. Your city grants you double the resources it would normally provide.", "Settlement Upgrade Successful",1);
            }
        }
    }

    public void constructPorts(){
        ArrayList<Point[]> portPointList = new ArrayList<Point[]>();
        ArrayList<String> portTypesList = new ArrayList<String>();
        for(int x=0; x<9; x++){
            portPointList.add(portPoints[x]);
            portTypesList.add(portTypes[x]);
        }

        for(int x=0; x<9; x++){
            int pointIndex = new Random().nextInt(portPointList.size());
            int stringIndex = new Random().nextInt(portPointList.size());
            ports[x] = new Port(portPointList.get(pointIndex),portTypesList.get(stringIndex));
            portPointList.remove(portPointList.get(pointIndex));
            portTypesList.remove(portTypesList.get(stringIndex));
        }
    }


    public ArrayList<Player> getOtherPlayers(){
        ArrayList<Player> others = new ArrayList<Player>();
        for(int x=0; x<catanPlayerList.size(); x++)
            if(catanPlayerList.get(x)!=getCurrentPlayer())
                others.add(catanPlayerList.get(x));
        return others;
    }

    public ArrayList<Integer> getIndexIDs() {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (int x = 0; x < indexConnections.size(); x++){
            ids.add(indexConnections.get(x).getIndexA());
            ids.add(indexConnections.get(x).getIndexB());
        }
        return ids;
    }

    public Index returnAppropIndex(int chosen_x, int chosen_y){
        for(int x=0; x<indexes.length; x++)
            if(indexes[x].getLocation()[0]==chosen_x && indexes[x].getLocation()[1]==chosen_y)
                return indexes[x];

        return null;
    }

    public Player getPlayerViaName(String name){
        for(int x=0; x<catanPlayerList.size(); x++)
            if(catanPlayerList.get(x).getName().equals(name))
                return catanPlayerList.get(x);

        return null;
    }

    public ArrayList<Player> turnOrder(ArrayList<String> nameList){
        ArrayList<Player> players=new ArrayList<Player>();
        for(int x=0; x<nameList.size(); x++)
            for(int y=0; y<catanPlayerList.size(); y++)
                if(nameList.get(x).equals(catanPlayerList.get(y).getName()))
                    players.add(catanPlayerList.get(y));

        return players;
    }

    //Checks which road type to draw and where to do so
    public Object[] getRoadPositionAndType(Index indexOne, Index indexTwo){
        Point indexOneLoc = new Point(indexOne.getLocation()[0],indexOne.getLocation()[1]);
        Point indexTwoLoc = new Point(indexTwo.getLocation()[0],indexTwo.getLocation()[1]);
        int oneX = (int)indexOneLoc.getX();
        int oneY = (int)indexOneLoc.getY();
        int twoX = (int)indexTwoLoc.getX();
        int twoY = (int)indexTwoLoc.getY();

        if(Math.abs(oneX-twoX)<10 && Math.abs(distance(indexOneLoc,indexTwoLoc)-77.5)<10){
            //Values for creating vertical roads
            if(oneY>twoY)
                return new Object[]{new Point(twoX+2,twoY+15),"Vertical"};

            else
                return new Object[]{new Point(oneX+2,oneY+15),"Vertical"};
        }

        else {
            //Values necessary for creating points for the diagonal road polygon
            if (oneX < twoX && oneY < twoY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX+19,oneY+10),"Up_To_Down"};

            if (oneX < twoX && oneY > twoY && distance(indexOneLoc, indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX+22,oneY-30),"Down_To_Up"};

            if (oneX>twoX && oneY>twoY && distance(indexOneLoc,indexTwoLoc) -77.5 <10)
                return new Object[]{new Point(twoX+19,twoY+10),"Up_To_Down"};

            if (twoX < oneX && twoY > oneY && distance(indexOneLoc,indexTwoLoc) - 77.5 < 10)
                return new Object[]{new Point(oneX-48,oneY+8),"Down_To_Up"};
        }
        return new Object[]{new Point(0,0),""};
    }

    //Finds the distance between two points in cartesian space (good-ole-pythagorean theorem)
    public double distance(Point one, Point two){
        return Math.sqrt(Math.pow(one.getX()-two.getX(),2) + Math.pow(one.getY()-two.getY(),2));
    }

    //Gets resources that would be obtained for an index at start of game
    public ArrayList<String> getAdjacentResources(int xLoc, int yLoc){
        ArrayList<String> adjacentResources = new ArrayList<String>();
        for(int x=0; x<indexes.length; x++){
            if(Math.abs(indexes[x].getLocation()[0] - xLoc)<20 && Math.abs(indexes[x].getLocation()[1] - yLoc)<20){
                for(int a=0; a<tiles.length; a++){
                    for(int b=0; b<6; b++){
                        if(Math.abs(tiles[a].getVertices().get(b).getX()-xLoc)<20 && Math.abs(tiles[a].getVertices().get(b).getY()-yLoc)<20){
                            adjacentResources.add(tiles[a].getType());
                        }
                    }
                }
            }
        }
        return adjacentResources;
    }

    //Returns the player whose turn is currently taking place
    public Player getCurrentPlayer(){
        for(int x=0; x<catanPlayerList.size(); x++)
            if(catanPlayerList.get(x).isTurn())
                return catanPlayerList.get(x);

        return null;
    }

    public boolean buildable(Index newSpot){
        for(int x=0; x<indexes.length; x++)
            if ((indexes[x].isTaken() && newSpot!=indexes[x] && distance(new Point(indexes[x].getLocation()[0], indexes[x].getLocation()[1]), new Point(newSpot.getLocation()[0], newSpot.getLocation()[1])) < 100))
                return false;

        return true;
    }

    public boolean settlementBuildable(Index newSpot){
        int counter=0;
        for(int x=0; x<indexConnections.size(); x++){
            if(newSpot.getIndexID()==indexConnections.get(x).getIndexA() || newSpot.getIndexID()==indexConnections.get(x).getIndexB()){
                if(indexConnections.get(x).getOwner()==getCurrentPlayer())
                    counter++;
            }
        }
        if(counter>0)
            return true;

        else
            return false;
    }

    public void performStartingOperations(){
        int startingPlayer = new Random().nextInt(playerCreation.length);
        doingStartup=true;
        for (int x = 0; x < catanPlayerList.size(); x++) {
            if (catanPlayerList.get(x).getRefNumber() == startingPlayer) {
                catanPlayerList.get(x).setTurn(true);
            }
        }

        //Creating status screen for each player
        statusViewer = new PlayerView[catanPlayerList.size()];
        for (int x = 0; x < catanPlayerList.size(); x++){
            statusViewer[x] = new PlayerView(catanPlayerList.get(x), this, new TradingFrame(catanPlayerList.get(x),true));
            statusViewer[x].tf.cbRef=this;
            statusViewer[x].setBounds((int)statusGenerationalPoints[x].getX(),(int)statusGenerationalPoints[x].getY(),475,353);
            statusViewer[x].pack();
            statusViewer[x].setVisible(true);
            statusViewer[x].setTitle(catanPlayerList.get(x).getName()+" - "+catanPlayerList.get(x).getClassTitle());
        }

        //Construct starting pick order
        ArrayList<Integer>startPickOrder = new ArrayList<Integer>();
        ArrayList<Integer> turnOrder=new ArrayList<Integer>();
        for(int x=0; x<catanPlayerList.size(); x++) {
            startPickOrder.add((startingPlayer+x)% catanPlayerList.size());
        }
        turnOrder = startPickOrder;
        String turnOrderString = "The turn order is as follows: ";
        for(int x=0; x<turnOrder.size(); x++)
            for(int y=0; y<catanPlayerList.size(); y++)
                if(catanPlayerList.get(y).getRefNumber() == turnOrder.get(x))
                    turnOrderString+=catanPlayerList.get(y).getName()+((x!=turnOrder.size()-1)?", ":". ");

        startPickOrder.addAll(reverse(startPickOrder));
        String buildingOrder="With that in mind, the starting order placement will be: ";
        turnNameList = new ArrayList<String>();

        for(int x=0; x<startPickOrder.size(); x++)
            for(int y=0; y<catanPlayerList.size(); y++)
                if(catanPlayerList.get(y).getRefNumber() == turnOrder.get(x)) {
                    buildingOrder += catanPlayerList.get(y).getName() + ((x != turnOrder.size() - 1) ? ", " : ".");
                    turnNameList.add(catanPlayerList.get(y).getName());
                }
        JOptionPane.showMessageDialog(this, "You're ready to begin play. Enjoy Settlers of Catan®.", "Beginning Game", 1);
        JOptionPane.showMessageDialog(this,turnOrderString+ buildingOrder,"Turn and Building Order",1);

        for(int y=0; y<statusViewer.length; y++) {
            statusViewer[y].options.setEnabled(false);
            statusViewer[y].build.setEnabled(false);
            statusViewer[y].development.setEnabled(false);
        }

        for(int y=0; y<turnNameList.size(); y++)
            for(int x=0; x<catanPlayerList.size(); x++)
                if(turnNameList.get(y).equals(catanPlayerList.get(x).getName()))
                    duplicates.add(catanPlayerList.get(x));

        JOptionPane.showMessageDialog(this,"So, "+duplicates.get(0).getName()+", select an index to build on, and then a direction you'd like to build a road.","Road and Settlement Building",1);
        this.isSettlementBuilding=true;
        duplicates.get(0).setTurn(true);
    }

    public ArrayList<Integer> reverse(ArrayList<Integer> list){
        ArrayList<Integer> reversed = new ArrayList<Integer>();
        for(int x=list.size()-1; x>-1; x--)
            reversed.add(list.get(x));
        return reversed;
    }

    public ArrayList<Player> getPlayersOnTile(Tile t){
        ArrayList<Player> players = new ArrayList<Player>();
        for(int x=0; x<t.getVertices().size(); x++){
            for(int y=0; y<indexes.length; y++){
                if(Math.abs(t.getVertices().get(x).getX() - indexes[y].getLocation()[0]) < 30 && Math.abs(t.getVertices().get(x).getY() - indexes[y].getLocation()[1])<30)
                    if(indexes[y].getOwner()!=getCurrentPlayer() && !players.contains(indexes[y].getOwner()) && !indexes[y].getOwner().getName().equals(""))
                        players.add(indexes[y].getOwner());
            }
        }
        return players;
    }

    public void givePlayersStartingResources() {
        for (int x = 0; x < catanPlayerList.size(); x++) {
            ArrayList<Index> ownedIndexes = getOwnedIndexes(catanPlayerList.get(x));
            for(int y=0; y<ownedIndexes.size(); y++) {
                ArrayList<String> startResources = getAdjacentResources(ownedIndexes.get(y).getLocation()[0], ownedIndexes.get(y).getLocation()[1]);
                for (int z = 0; z < startResources.size(); z++) {
                    if (startResources.get(z).equals("Grain"))
                        catanPlayerList.get(x).changeGrain(1);
                    else if (startResources.get(z).equals("Brick"))
                        catanPlayerList.get(x).changeBrick(1);
                    else if (startResources.get(z).equals("Forest"))
                        catanPlayerList.get(x).changeLumber(1);
                    else if (startResources.get(z).equals("Plains"))
                        catanPlayerList.get(x).changeWool(1);
                    else if (startResources.get(z).equals("Mountain"))
                        catanPlayerList.get(x).changeOre(1);
                }
            }
            getPlayerStatusMenu(catanPlayerList.get(x)).update();
        }
    }

    public PlayerView getPlayerStatusMenu(Player player){
        for(int x=0; x<statusViewer.length; x++)
            if(statusViewer[x].player==player)
                return statusViewer[x];

        return null;
    }

    public ArrayList<Index> getOwnedIndexes(Player player){
        ArrayList<Index> ownedIndexes = new ArrayList<Index>();
        for(int x=0; x<indexes.length; x++)
            if(indexes[x].getOwner()==player)
                ownedIndexes.add(indexes[x]);

        return ownedIndexes;
    }

    public void updateAllStatusMenus(){
        for(int x=0; x<statusViewer.length; x++)
            statusViewer[x].update();
    }

    //This method is an abomination
    public void giveOutResources(int roll){
        for(int x=0; x<tiles.length; x++)
            if(tiles[x].getNum()==roll)
                for(int y=0; y<6; y++)
                    for(int z=0; z<indexes.length; z++)
                        if(Math.abs(tiles[x].getVertices().get(y).getX()-indexes[z].getLocation()[0])<35 && Math.abs(tiles[x].getVertices().get(y).getY()-indexes[z].getLocation()[1])<35 && !tiles[x].isHasRobber())
                            for(int a=0; a<catanPlayerList.size(); a++)
                                if (indexes[z].getOwner() == catanPlayerList.get(a)) {
                                    if (tiles[x].getType().equals("Grain"))
                                        catanPlayerList.get(a).changeGrain((indexes[z].isSettlement() ? 1 : 2));
                                    else if (tiles[x].getType().equals("Brick"))
                                        catanPlayerList.get(a).changeBrick((indexes[z].isSettlement() ? 1 : 2));
                                    else if (tiles[x].getType().equals("Forest"))
                                        catanPlayerList.get(a).changeLumber((indexes[z].isSettlement() ? 1 : 2));
                                    else if (tiles[x].getType().equals("Plains"))
                                        catanPlayerList.get(a).changeWool((indexes[z].isSettlement() ? 1 : 2));
                                    else if (tiles[x].getType().equals("Mountain"))
                                        catanPlayerList.get(a).changeOre((indexes[z].isSettlement() ? 1 : 2));
                                }
    }

    public String giveRandomResource(Player player){
        ArrayList<String> resources = new ArrayList<String>();
        if(player.getLumberNum()>0)
            resources.add("Lumber");
        if(player.getBrickNum()>0)
            resources.add("Brick");
        if(player.getGrainNum()>0)
            resources.add("Wheat");
        if(player.getWoolNum()>0)
            resources.add("Sheep");
        if(player.getOreNum()>0)
            resources.add("Ore");

        if(resources.size()!=0)
            return resources.get(new Random().nextInt(resources.size()));

        else
            return "";
    }

    public void largestArmy(Player player, int largestArmy){
        int knightCounter=0;
        for(int x=0; x<player.getPlayedCards().size(); x++)
            if(player.getPlayedCards().get(x).getType().equals("Knight"))
                knightCounter++;

        if(knightCounter>currentLargestArmy && largestArmyPlayer!=getCurrentPlayer()){
            largestArmyPlayer.setLargestArmy(false);
            largestArmyPlayer.changeVictoryPoints(-2);
            player.setLargestArmy(true);
            player.changeVictoryPoints(2);
            largestArmyPlayer=player;
            currentLargestArmy=knightCounter;
            JOptionPane.showMessageDialog(this,"There is a new largest army of size "+knightCounter+" controlled by "+player.getName()+".","New Largest Army",1);
            updateAllStatusMenus();
        }
    }

    public int commonIndex(Road roadA, Road roadB){
        if(roadA.getIndexA()==roadB.getIndexA() || roadA.getIndexA()==roadB.getIndexB())
            return roadA.getIndexA();

        else if(roadA.getIndexB()==roadB.getIndexA() || roadA.getIndexB()==roadB.getIndexB())
            return roadA.getIndexB();

        return 0;
    }

    public void usePort(Port port){
        try {
            String use = "";
            if (port.getType().equals("Wheat")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade two wheat for a single resource?", "Wheat Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        checkOptions[1].setEnabled(false);
                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange wheat for:", checkOptions}, "Wheat Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }

                    if (checkOptions[0].isSelected())
                        getCurrentPlayer().monoWool(1);

                    if (checkOptions[2].isSelected())
                        getCurrentPlayer().monoOre(1);

                    if (checkOptions[3].isSelected())
                        getCurrentPlayer().monoBrick(1);

                    if (checkOptions[4].isSelected())
                        getCurrentPlayer().monoLumber(1);

                    getCurrentPlayer().monoWheat(-2);
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1);
                }
            } else if (port.getType().equals("Sheep")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade two sheep for a single resource?", "Sheep Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        checkOptions[0].setEnabled(false);
                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange sheep for:", checkOptions}, "Sheep Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }

                    if (checkOptions[1].isSelected())
                        getCurrentPlayer().monoWheat(1);

                    if (checkOptions[2].isSelected())
                        getCurrentPlayer().monoOre(1);

                    if (checkOptions[3].isSelected())
                        getCurrentPlayer().monoBrick(1);

                    if (checkOptions[4].isSelected())
                        getCurrentPlayer().monoLumber(1);

                    getCurrentPlayer().monoWool(-2);
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1);
                }
            } else if (port.getType().equals("Ore")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade two ore for a single resource?", "Ore Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        checkOptions[2].setEnabled(false);
                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange ore for:", checkOptions}, "Ore Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }

                    if (checkOptions[0].isSelected())
                        getCurrentPlayer().monoWool(1);

                    if (checkOptions[1].isSelected())
                        getCurrentPlayer().monoWheat(1);

                    if (checkOptions[3].isSelected())
                        getCurrentPlayer().monoBrick(1);

                    if (checkOptions[4].isSelected())
                        getCurrentPlayer().monoLumber(1);

                    getCurrentPlayer().monoOre(-2);
                    JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1);
                }
            } else if (port.getType().equals("Brick")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade two brick for a single resource?", "Brick Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        checkOptions[3].setEnabled(false);
                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange brick for:", checkOptions}, "Brick Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }

                    if (checkOptions[1].isSelected())
                        getCurrentPlayer().monoWheat(1);

                    if (checkOptions[2].isSelected())
                        getCurrentPlayer().monoOre(1);

                    if (checkOptions[0].isSelected())
                        getCurrentPlayer().monoWool(1);

                    if (checkOptions[4].isSelected())
                        getCurrentPlayer().monoLumber(1);
                }

                getCurrentPlayer().monoBrick(-2);
                JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1);
            } else if (port.getType().equals("Wood")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade two lumber for a single resource?", "Lumber Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        checkOptions[4].setEnabled(false);
                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange lumber for:", checkOptions}, "Lumber Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }

                    if (checkOptions[1].isSelected())
                        getCurrentPlayer().monoWheat(1);

                    if (checkOptions[2].isSelected())
                        getCurrentPlayer().monoOre(1);

                    if (checkOptions[0].isSelected())
                        getCurrentPlayer().monoWool(1);

                    if (checkOptions[3].isSelected())
                        getCurrentPlayer().monoBrick(1);
                }
                getCurrentPlayer().monoLumber(-2);
            } else if (port.getType().equals("Generic")) {
                while (use.equals(""))
                    use = JOptionPane.showInputDialog(this, "Would you like to trade three of a resource for another single resource?", "Generic Port", 1);
                if (use.equalsIgnoreCase("yes")) {
                    String resourceChoice = "";
                    while (!(resourceChoice.equalsIgnoreCase("Sheep") || resourceChoice.equalsIgnoreCase("Lumber") || resourceChoice.equalsIgnoreCase("Brick") || resourceChoice.equalsIgnoreCase("Ore") || resourceChoice.equalsIgnoreCase("Wheat"))) {
                        resourceChoice = JOptionPane.showInputDialog(this, "Type the resource you'd like to trade three in of: Sheep, Lumber, Ore, Brick, or Wheat", "Generic Action", 1);

                        if (!(resourceChoice.equalsIgnoreCase("Sheep") || resourceChoice.equalsIgnoreCase("Lumber") || resourceChoice.equalsIgnoreCase("Brick") || resourceChoice.equalsIgnoreCase("Ore") || resourceChoice.equalsIgnoreCase("Wheat")))
                            JOptionPane.showMessageDialog(this, "That is not an accepted resource. Try again.", "Improper Choice", 3);
                    }
                    while (numberChecked() == 0 || numberChecked() > 1) {
                        if (resourceChoice.equalsIgnoreCase("Sheep"))
                            checkOptions[0].setEnabled(false);
                        if (resourceChoice.equalsIgnoreCase("Wheat"))
                            checkOptions[1].setEnabled(false);
                        if (resourceChoice.equalsIgnoreCase("Ore"))
                            checkOptions[2].setEnabled(false);
                        if (resourceChoice.equalsIgnoreCase("Brick"))
                            checkOptions[3].setEnabled(false);
                        if (resourceChoice.equalsIgnoreCase("Wood"))
                            checkOptions[4].setEnabled(false);

                        JOptionPane.showMessageDialog(this, new Object[]{"Choose the resource you'd like to exchange for:", checkOptions}, "Generic Exchange", 1);
                        if (numberChecked() > 1)
                            JOptionPane.showMessageDialog(this, "You must select a single resource.", "Improper Choice", 3);
                    }
                    if (checkOptions[0].isSelected())
                        getCurrentPlayer().monoWool(1);

                    if (checkOptions[1].isSelected())
                        getCurrentPlayer().monoWheat(1);

                    if (checkOptions[2].isSelected())
                        getCurrentPlayer().monoOre(1);

                    if (checkOptions[3].isSelected())
                        getCurrentPlayer().monoBrick(1);

                    if (checkOptions[4].isSelected())
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
                JOptionPane.showMessageDialog(this, "The exchange has been made.", "Port Used", 1);

            }

            resetPortBoxes();
            getPlayerStatusMenu(getCurrentPlayer()).update();
        }
        catch(NullPointerException e) {
            JOptionPane.showMessageDialog(this, "The port use has been cancelled for declining to follow the instructions.", "Port Error", 3);
        }
    }

    public void getPortsReady(){
        checkOptions = new JCheckBox[5];
        for(int x=0; x<5; x++)
            checkOptions[x] = new JCheckBox(portTypes[x+4]);
    }

    public void resetPortBoxes(){
        for(int x=0; x<checkOptions.length; x++){
            checkOptions[x].setEnabled(true);
            checkOptions[x].setSelected(false);
        }
    }
    public void givePlayersCatanBoardReference(){
        for(int x=0; x<catanPlayerList.size(); x++)
            catanPlayerList.get(x).cb=this;
    }

    public int numberChecked(){
        int counter=0;
        for(int x=0; x<checkOptions.length; x++)
            if(checkOptions[x].isSelected())
                counter++;
        return counter;
    }

    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}

    //Method doesn't work if there is a fork in the road. Still need to work on that
    public boolean longestRoad(int previousLongestRoad){
        ArrayList<Road> playerRoads = new ArrayList<Road>();
        ArrayList<Integer> roadLengths = new ArrayList<Integer>();
        ArrayList<Road> alreadyUsed = new ArrayList<Road>();
        ArrayList<Integer> alreadyJumpedFrom = new ArrayList<Integer>();
        boolean stillHasConnections=true;

        for(int x=0; x<indexConnections.size(); x++){
            if(indexConnections.get(x).getOwner()==getCurrentPlayer())
                playerRoads.add(indexConnections.get(x));
        }

        for(int a=0; a<playerRoads.size(); a++){
            alreadyUsed.clear();
            alreadyJumpedFrom.clear();
            alreadyUsed.add(playerRoads.get(a));
            Road current = playerRoads.get(a);
            int counter=1;
            while(stillHasConnections){
                stillHasConnections=false;
                for(int y=0; y<playerRoads.size(); y++){
                    if(current.getIndexA()==playerRoads.get(y).getIndexA() || current.getIndexA()==playerRoads.get(y).getIndexB() || current.getIndexB()==playerRoads.get(y).getIndexA() || current.getIndexB()==playerRoads.get(y).getIndexB()){
                        if(!alreadyUsed.contains(playerRoads.get(y))) {
                            current = playerRoads.get(y);
                            alreadyUsed.add(current);
                            counter++;
                            stillHasConnections=true;
                            System.out.println(current.toString());
                        }
                    }
                }
            }
            roadLengths.add(counter);
        }

        for(int x=0; x<roadLengths.size(); x++){
            System.out.println(roadLengths.get(x));
            if(roadLengths.get(x)>previousLongestRoad) {
                this.currentLongestRoad = roadLengths.get(x);
                return true;
            }
        }
        return false;
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_C) {
            if (!doingStartup) {
                int confirmCancellation = JOptionPane.showConfirmDialog(this, "Would you like to cancel your current action?", "Cancellation", JOptionPane.YES_NO_OPTION);
                if (confirmCancellation == 0) {
                    if (!doingStartup && isPlayerActing) {
                        if (isSettlementBuilding) {
                            isSettlementBuilding = false;
                            isDoneSettlementBuilding = false;
                            getCurrentPlayer().changeWool(1);
                            getCurrentPlayer().changeBrick(1);
                            getCurrentPlayer().changeGrain(1);
                            getCurrentPlayer().changeLumber(1);
                            getCurrentPlayer().changeVictoryPoints(-1);
                        }
                        else if (isMovingRobber) {
                            isMovingRobber = false;
                            isDoneMovingRobber = false;
                            getCurrentPlayer().addDevelopmentCardToUnplayed(new DevelopmentCard("Knight",getCurrentPlayer(),getOtherPlayers(),this,false));
                            getCurrentPlayer().removeDevelopmentCardFromPlayed(new DevelopmentCard("Knight",getCurrentPlayer(),getOtherPlayers(),this,false));
                            getPlayerStatusMenu(getCurrentPlayer()).unplayed.addItem("Knight");
                            getPlayerStatusMenu(getCurrentPlayer()).played.removeItem("Knight");
                            isPlayerActing=false;
                        }
                        else if (isRoadBuilding) {
                            isRoadBuilding = false;
                            isDoneRoadBuilding = false;
                            roadCondition = 0;
                            checkedIndexes.clear();
                            getCurrentPlayer().changeLumber(1);
                            getCurrentPlayer().changeBrick(1);

                        } else if (isCityUpgrading) {
                            isCityUpgrading = false;
                            getCurrentPlayer().changeGrain(2);
                            getCurrentPlayer().changeOre(3);
                            getCurrentPlayer().changeVictoryPoints(-1);
                        }
                    }
                    isPlayerActing = false;
                    getPlayerStatusMenu(getCurrentPlayer()).options.setEnabled(true);
                    getPlayerStatusMenu(getCurrentPlayer()).build.setEnabled(true);
                    getPlayerStatusMenu(getCurrentPlayer()).development.setEnabled(true);
                    updateAllStatusMenus();
                    JOptionPane.showMessageDialog(this, "Your action has been cancelled and your resources have been refunded. Please continue with your turn.", "Cancellation Successful", 1);
                } else
                    JOptionPane.showMessageDialog(this, "Then continue the action you were performing.", "Action Continued", 1);
            }
        }
    }
}
