import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class CatanBoard extends JFrame implements MouseListener {
    //Line 228: Road conflict and road building simultaneous

    //Objects for Board Generation
    String[] types = {"Mountain","Mountain","Mountain","Brick","Brick","Brick","Forest","Forest","Forest","Forest","Plains","Plains","Plains","Plains","Grain","Grain","Grain","Grain","Desert"};
    int[] rollNums = {8,4,11,12,3,11,10,9,6,9,5,2,4,5,10,8,3,6};
    int[][] coords = {{267,87},{267+134,87},{267+2*134,87},{200,200},{334,200},{200+2*134,200},{200+3*134,200},{133,313},{133+134,313},{133+2*134,313},{133+3*134,313},{666,313},{200,426},{200+134,426},{200+134*2,426},{200+134*3,426},{267,426+113},{267+134,426+113},{267+134*2,426+113}};
    ArrayList<String> typeList = new ArrayList<String>();
    ArrayList<Integer> rollNumList = new ArrayList<Integer>();
    ArrayList<int[]> coordList = new ArrayList<int[]>();
    Tile tiles[] = new Tile[19];

    //Water Tiles
    Point[] waterPoints = new Point[]{new Point(201,-30), new Point(334,-30), new Point(467,-30), new Point(600,-30), new Point(133,84), new Point(66,198), new Point(0,312), new Point(67,429), new Point(133,543), new Point(333,656), new Point(466,656), new Point(600,656), new Point(201,656), new Point(668,84), new Point(734,198), new Point(800,312), new Point(668, 543), new Point(734,429)};
    Point[] outLinePoints = new Point[]{new Point(1,431), new Point(66,467), new Point(66,545), new Point(133,584), new Point(133,659), new Point(200,698), new Point(200,770), new Point(246, 799), new Point(287,799), new Point(333,774), new Point(377,798), new Point(417,798), new Point(465,773), new Point(512,798), new Point(551,798), new Point(599,772), new Point(645,798), new Point(686,798),new Point(733,773),new Point(733,697),new Point(800,659), new Point(800,584), new Point(866,546), new Point(866,468), new Point(929,429), new Point(929,348), new Point(868,313), new Point(868,237), new Point(800,198), new Point(800,122), new Point(732,84), new Point(732,8), new Point(719, 0), new Point(613,0), new Point(599,8), new Point(585,0), new Point(479,0), new Point(467,8), new Point(451,0), new Point(348,0), new Point(334,8), new Point(319,0), new Point(212,0), new Point(202,8), new Point(202,84), new Point(133,122), new Point(133,197), new Point(65,236), new Point(65,311), new Point(1,348)};

    //Paint/Repaint Conditions
    boolean loaded=false;
    boolean settlementPaintCondition=false;
    int chosen_x = 0;
    int chosen_y = 0;

    //Booleans for conditions on when MouseListeners should activate
    boolean isRoadBuilding=false, isSettlementBuilding=false;
    boolean isDoneRoadBuilding=false, isDoneSettlementBuilding=false;
    boolean doingStartup=false;
    boolean found=false;


    //Index Creation
    ArrayList<int[]> coord = new ArrayList<int[]>();
    int[][] indexCoords = {{264,122},{330,87},{398,122},{461,87},{533,121},{599,87},{658,122},{663,200},{599,234},{532,200},{463,233},{396,201},{329,235},{262,202},{196,235},{197,312},{262,347},{329,312},{394,348},{461,311},{528,346},{599,312},{665,350},{727,313},{729,237},{797,346},{798,425},{725,461},{660,423},{597,457},{527,424},{462,459},{393,422},{327,460},{258,421},{198,459},{132,423},{130,345},{200,536},{263,572},{329,537},{393,574},{464,534},{526,574},{599,536},{655,646},{594,683},{461,687},{392,646},{526,654},{266,647},{332,686},{664,577},{731,536}};
    Index[] indexes = new Index[indexCoords.length];
    Index checked;

    //Port Creation
    Point[][] portPoints = new Point[][]{new Point[]{new Point(264,122),new Point(330,87), new Point(286,89)},
                                         new Point[]{new Point(461,87),new Point(533,121), new Point(516,90)},
                                         new Point[]{new Point(663,200),new Point(729,237), new Point(707,199)},
                                         new Point[]{new Point(797,346),new Point(798,425), new Point(826,392)},
                                         new Point[]{new Point(731,536),new Point(664,577), new Point(713,578)},
                                         new Point[]{new Point(526,654),new Point(461,687), new Point(512,689)},
                                         new Point[]{new Point(332,686),new Point(266,647), new Point(288,694)},
                                         new Point[]{new Point(200,536),new Point(198,459), new Point(185,493)},
                                         new Point[]{new Point(198,235),new Point(197,312), new Point(179,278)}};
    String[] portTypes = {"Generic","Generic","Generic","Generic","Wool","Wheat","Ore","Brick","Wood"};
    Port[] ports = new Port[9];

    //Building Variables
    int roadCondition=0;
    ArrayList<Index> checkedIndexes = new ArrayList<Index>();
    Object[] roadInfo;
    ArrayList<int[]> indexConnections = new ArrayList<int[]>();
    
    //Constructor Variables
    PlayerView[] statusViewer;
    ArrayList<Player> catanPlayerList;
    Point[] statusGenerationalPoints;
    PlayerSelect[] playerCreation;
    ArrayList<Player> duplicates = new ArrayList<Player>();

    public CatanBoard(ArrayList<Player> catanPlayerList,PlayerView[] statusViewer, Point[] statusGenerationalPoints, PlayerSelect[] playerCreation){
        this.addMouseListener(this);
        this.statusViewer=statusViewer;
        this.catanPlayerList=catanPlayerList;
        this.statusGenerationalPoints=statusGenerationalPoints;
        this.playerCreation=playerCreation;
        
        for(int x=0; x<types.length; x++)
            typeList.add(types[x]);

        for(int x=0; x<rollNums.length; x++)
            rollNumList.add(rollNums[x]);

        for(int x=0; x<coords.length; x++)
            coordList.add(coords[x]);

        for(int x=0; x<indexes.length; x++)
            indexes[x] = new Index(indexCoords[x],false,x,null);

        for(int x=0; x<types.length; x++){
            int typeIndex = new Random().nextInt(typeList.size());
            int coordIndex = new Random().nextInt(coordList.size());
            tiles[x] = new Tile(coordList.get(coordIndex),typeList.get(typeIndex),0,false);
            typeList.remove(typeIndex);
            coordList.remove(coordIndex);

            if(tiles[x].getType().equals("Desert"))
                tiles[x].setNum(7);

            else{
                int rollIndex = new Random().nextInt(rollNumList.size());
                tiles[x].setNum(rollNumList.get(rollIndex));
                rollNumList.remove(rollIndex);
            }
        }
        //Methods to take care of things before startup; the method names are pretty self-explanatory on what they are doing
        constructPorts();
    }

    public void paint(Graphics g) {
        try {
            if (!loaded) {
                for (int x = 0; x < tiles.length; x++) {
                    BufferedImage tile = ImageIO.read(new File("Tiles/" + tiles[x].getType() + ".png"));
                    g.drawImage(tile, tiles[x].getPosition()[0], tiles[x].getPosition()[1], null);
                }
                //When Ports Are Ready, Uncomment This
                /*
                for(int x=0; x<ports.length; x++){
                    BufferedImage port = ImageIO.read(new File("Resources/Port/"+ports[x].getType()+"_Port.png"));
                    g.drawImage(port,(int)ports[x].getLocations()[2].getX(),(int)ports[x].getLocations()[2].getY(),null);
                }
                */

                BufferedImage water = ImageIO.read(new File("Tiles/Water_Tile.png"));
                for(int x=0; x<waterPoints.length; x++)
                    g.drawImage(water,(int)waterPoints[x].getX(),(int)waterPoints[x].getY(),null);


                Graphics2D g2 = (Graphics2D) g;
                g2.setStroke(new BasicStroke(3));
                g2.setColor(new Color(10,30,150));
                for(int x=0; x<outLinePoints.length; x++)
                    g2.drawLine((int)outLinePoints[x].getX(),(int)outLinePoints[x].getY(),(int)outLinePoints[(x+1)%outLinePoints.length].getX(), (int)outLinePoints[(x+1)%outLinePoints.length].getY());

                loaded=true;
            }
            if (settlementPaintCondition) {
                //Drawing Settlements Test
                BufferedImage settlement = ImageIO.read(new File("Pieces/"+getCurrentPlayer().getColor()+"_Settlement.png"));
                g.drawImage(settlement, chosen_x, chosen_y, null);
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

                if(doingStartup)
                    if(duplicates.size()>0) {
                        duplicates.get(0).setTurn(false);
                        duplicates.remove(0);

                        if (duplicates.size() > 0){
                            duplicates.get(0).setTurn(true);
                            JOptionPane.showMessageDialog(this, "Now, " + duplicates.get(0).getName() + ", select an index to build on, and then a direction you'd like to build a road.", "Road and Settlement Building", 1);
                            isSettlementBuilding = true;
                        }
                    }

                    if(duplicates.size()==0) {
                        doingStartup = false;
                        catanPlayerList.get(0).setTurn(true);
                        getPlayerStatusMenu(catanPlayerList.get(0)).update(catanPlayerList.get(0));
                        isDoneSettlementBuilding=true;
                        JOptionPane.showMessageDialog(this, "The game is ready to officially start. " + catanPlayerList.get(0).getName() + ", you proceed first.", "Free Play", 1);
                        givePlayersStartingResources();
                    }
            }

            for(int x=0; x<portPoints.length; x++) {
                g.fillRect((int) portPoints[x][0].getX(), (int) portPoints[x][0].getY(), 10, 10);
                g.fillRect((int) portPoints[x][1].getX(), (int) portPoints[x][1].getY(), 10, 10);
            }
        }
        catch(IOException ie){
            ie.printStackTrace();
        }
    }

    public void mouseClicked(MouseEvent e){}
    public void mousePressed(MouseEvent e){
        int xLoc = e.getX();
        int yLoc = e.getY();

        /* Convenient way to find specific coordinates
        System.out.println("X: "+xLoc);
        System.out.println("Y: "+yLoc);
        */

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
                if(indexConnections.size()==0) {
                    roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                    indexConnections.add(new int[]{checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID()});
                    repaint();
                }
                else{
                    found=false;
                    for(int x=0; x<indexConnections.size(); x++){
                        if((indexConnections.get(x)[0]==checkedIndexes.get(0).getIndexID() && indexConnections.get(x)[1]==checkedIndexes.get(1).getIndexID()) ||(indexConnections.get(x)[0]==checkedIndexes.get(1).getIndexID() && indexConnections.get(x)[1]==checkedIndexes.get(0).getIndexID())){
                            JOptionPane.showMessageDialog(this,"There is already a road here. Choose two different indexes that do not contain a road between them.","Road Error",3);
                            roadCondition=0;
                            checkedIndexes.clear();
                            found=true;
                            break;
                        }
                    }
                    if(checkedIndexes.get(0).getOwner()!=getCurrentPlayer() && checkedIndexes.get(1).getOwner()!=getCurrentPlayer()){
                        JOptionPane.showMessageDialog(this,"You need to attach a road to a settlement/city you own. Choose indices that touch at least one of your buildings.","Road Error",3);
                        roadCondition=0;
                        checkedIndexes.clear();
                        found=true;
                    }
                    if(!found){
                        roadInfo = getRoadPositionAndType(checkedIndexes.get(0), checkedIndexes.get(1));
                        indexConnections.add(new int[]{checkedIndexes.get(0).getIndexID(), checkedIndexes.get(1).getIndexID()});
                        repaint();
                    }
                }
            }
        }

        //Code to draw buildings when boolean is in correct state
        if (isSettlementBuilding){
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
                            checked=indexes[i];
                            breakCheck = false;
                            break;
                        }
                    }
                    if (checkedIndex.isTaken() && breakCheck)
                        JOptionPane.showMessageDialog(this, "This spot is unavailable. Pick another spot.","Spot Taken",3);

                    else if(!buildable(checkedIndex))
                        JOptionPane.showMessageDialog(this,"You are within one road-length of another settlement/city. Please choose again.","Spot Proximity Too Close",3);

                    else {
                        isSettlementBuilding = false;
                        settlementPaintCondition=true;
                        checked.setTaken(true);
                        checked.setOwner(getCurrentPlayer());
                        repaint();
                    }
                }
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
            ports[x] = new Port(portPointList.get(pointIndex),portTypesList.get(stringIndex),false);
            portPointList.remove(portPointList.get(pointIndex));
            portTypesList.remove(portTypesList.get(stringIndex));
        }
    }

    public Index returnAppropIndex(int chosen_x, int chosen_y){
        for(int x=0; x<indexes.length; x++)
            if(indexes[x].getLocation()[0]==chosen_x && indexes[x].getLocation()[1]==chosen_y)
                return indexes[x];

        return null;
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

    //Returns the current player (obviously)
    public Player getCurrentPlayer(){
        for(int x=0; x<catanPlayerList.size(); x++)
            if(catanPlayerList.get(x).isTurn())
                return catanPlayerList.get(x);

        return null;
    }

    public boolean buildable(Index newSpot){
        for(int x=0; x<indexes.length; x++)
            if((indexes[x].isTaken() && distance(new Point(indexes[x].getLocation()[0],indexes[x].getLocation()[1]),new Point(newSpot.getLocation()[0],newSpot.getLocation()[1]))<80) &&
                    (Math.abs(indexes[x].getLocation()[0]-newSpot.getLocation()[0])!=0 && Math.abs(indexes[x].getLocation()[1]-newSpot.getLocation()[1])!=0))
                return false;
        return true;
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
            statusViewer[x] = new PlayerView(catanPlayerList.get(x), this);
            statusViewer[x].setBounds((int)statusGenerationalPoints[x].getX(),(int)statusGenerationalPoints[x].getY(),475,353);
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
        ArrayList<String> turnNameList = new ArrayList<String>();

        for(int x=0; x<startPickOrder.size(); x++)
            for(int y=0; y<catanPlayerList.size(); y++)
                if(catanPlayerList.get(y).getRefNumber() == turnOrder.get(x)) {
                    buildingOrder += catanPlayerList.get(y).getName() + ((x != turnOrder.size() - 1) ? ", " : ".");
                    turnNameList.add(catanPlayerList.get(y).getName());
                }
        JOptionPane.showMessageDialog(this, "You're ready to begin play. Enjoy Settlers of Catan®.", "Generating Game", 1);
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

    public void givePlayersStartingResources() {
        for (int x = 0; x < catanPlayerList.size(); x++) {
            ArrayList<Index> ownedIndexes = getOwnedIndexes(catanPlayerList.get(x));
            Player currentPlayer = catanPlayerList.get(x);
            PlayerView priorView = getPlayerStatusMenu(currentPlayer);
            for(int y=0; y<ownedIndexes.size(); y++) {
                ArrayList<String> startResources = getAdjacentResources(ownedIndexes.get(y).getLocation()[0], ownedIndexes.get(y).getLocation()[1]);
                for (int z = 0; z < startResources.size(); z++) {
                    if (startResources.get(z).equals("Grain"))
                        currentPlayer.changeGrain(1);
                    else if (startResources.get(z).equals("Brick"))
                        currentPlayer.changeBrick(1);
                    else if (startResources.get(z).equals("Forest"))
                        currentPlayer.changeLumber(1);
                    else if (startResources.get(z).equals("Plains"))
                        currentPlayer.changeGrain(1);
                    else if (startResources.get(z).equals("Mountain"))
                        currentPlayer.changeOre(1);
                }
            }
            priorView.update(currentPlayer);
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

    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
