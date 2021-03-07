import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

public class Player {
    String color,name,classTitle;
    int brickNum,lumberNum,grainNum,woolNum,oreNum,victoryPointTotal, referenceNumber, cities, settlements, roads,votes;
    boolean turn,longestRoad,largestArmy,leader;
    ArrayList<Index> ownedIndexes;
    ArrayList<DevelopmentCard> playedCards, unPlayedCards;
    CatanBoard cb;
    boolean costMultiplier=false;

    //Victory Frame
    JFrame victory = new JFrame();
    JLabel victoryLabel = new JLabel("",SwingConstants.CENTER);
    Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

    public Player(String color, String name, String classTitle, ArrayList<Index> ownedIndexes, ArrayList<DevelopmentCard> unPlayedCards, ArrayList<DevelopmentCard> playedCards, int brickNum, int lumberNum, int grainNum, int woolNum, int oreNum, int victoryPointTotal, boolean turn, boolean longestRoad, boolean largestArmy, int referenceNumber, int cities, int settlements, int roads, boolean leader, int votes){
        this.color=color;
        this.name=name;
        this.classTitle=classTitle;
        this.ownedIndexes=ownedIndexes;
        this.unPlayedCards=unPlayedCards;
        this.playedCards=playedCards;
        this.lumberNum=lumberNum;
        this.brickNum=brickNum;
        this.grainNum=grainNum;
        this.woolNum=woolNum;
        this.oreNum=oreNum;
        this.victoryPointTotal=victoryPointTotal;
        this.turn=turn;
        this.longestRoad=longestRoad;
        this.largestArmy=largestArmy;
        this.referenceNumber=referenceNumber;
        this.cities=cities;
        this.settlements=settlements;
        this.roads=roads;
        this.leader=leader;
        this.votes=votes;
        this.costMultiplier = (classTitle.equals("Pirate") || classTitle.equals("Serf"));
    }

    public void loadUpVictoryFrame(){
        victory.setUndecorated(true);
        victory.setSize(810,533);
        victory.setLocation(dim.width/2-victory.getSize().width/2, dim.height/2-victory.getSize().height/2);
        victory.add(victoryLabel);
        victoryLabel.setIcon(new ImageIcon("Resources/Victory.png"));
        victoryLabel.setBorder(cb.compound);
        victory.setVisible(true);
    }

    public boolean isLeader() {
        return leader;
    }

    public void setLeader(boolean leader) {
        this.leader = leader;
    }

    public int getVotes() {
        return votes;
    }

    public void addVotes(int vote) {
        this.votes+=vote;
    }

    public int getCities() {
        return cities;
    }

    public void changeCityNum(int cities) {
        this.cities+=cities;
    }

    public int getSettlements() {
        return settlements;
    }

    public void changeSettlementNum(int settlements) {
        this.settlements+=settlements;
    }

    public int getRoads() {
        return roads;
    }

    public void changeRoadNum(int roads) {
        this.roads+=roads;
    }

    public int getRefNumber() {
        return referenceNumber;
    }

    //Generic Constructor
    public Player(){}

    public boolean isTurn() {
        return turn;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }

    public void setBrickNum(int brickNum) {
        this.brickNum = brickNum;
    }

    public boolean hasLongestRoad() {
        return longestRoad;
    }

    public void setLongestRoad(boolean longestRoad) {
        this.longestRoad = longestRoad;
    }

    public boolean hasLargestArmy() {
        return largestArmy;
    }

    public void setLumberNum(int lumberNum) {
        this.lumberNum = lumberNum;
    }

    public void setGrainNum(int grainNum) {
        this.grainNum = grainNum;
    }

    public void setWoolNum(int woolNum) {
        this.woolNum = woolNum;
    }

    public void setOreNum(int oreNum) {
        this.oreNum = oreNum;
    }

    public void setLargestArmy(boolean largestArmy) {
        this.largestArmy = largestArmy;
    }

    public int getVictoryPointTotal() {
        return victoryPointTotal;
    }

    public void changeVictoryPoints(int pointChange) {
        this.victoryPointTotal+=pointChange;
        winTheGame();
    }

    public ArrayList<DevelopmentCard> getPlayedCards() {
        return playedCards;
    }

    public ArrayList<DevelopmentCard> getUnPlayedCards(){
        return unPlayedCards;
    }

    public void addDevelopmentCardToPlayed(DevelopmentCard dc){
        this.playedCards.add(dc);
    }

    public void addDevelopmentCardToUnplayed(DevelopmentCard dc){
        this.unPlayedCards.add(dc);
    }

    public void removeDevelopmentCardFromUnplayed(DevelopmentCard dc){
        this.unPlayedCards.remove(dc);
    }

    public void removeDevelopmentCardFromPlayed(DevelopmentCard dc){
        this.playedCards.remove(dc);
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public int getBrickNum() {
        return brickNum;
    }

    public void changeBrick(int brickNum) {
        switch(classTitle) {
            case "Farmer":
            case "Shepard":
            case "Woodsman":
                pass();
                break;
            case "Mountaineer":
            case "Emperor":
            case "Gambler":
                this.brickNum += 2 * brickNum;
                break;
            case "Deity":
                this.brickNum+=4*brickNum;
                break;
            default:
                this.brickNum += brickNum;
        }
    }

    public int getLumberNum() {
        return lumberNum;
    }

    public void changeLumber(int lumberNum) {
        switch (this.classTitle) {
            case "Woodsman":
            case "Deity":
                this.lumberNum += 4 * lumberNum;
                break;
            case "Shepard":
                pass();
                break;
            case "Emperor":
            case "Gambler":
                this.lumberNum += 2 * lumberNum;
                break;
            default:
                this.lumberNum += lumberNum;
                break;
        }
    }

    public int getGrainNum() {
        return grainNum;
    }

    public void changeGrain(int grainNum) {
        switch(classTitle) {
            case "Deity":
                this.grainNum+=4*grainNum;
                break;
            case "Mountaineer":
            case "Shepard":
            case "Woodsman":
                pass();
                break;
            case "Farmer":
            case "Emperor":
            case "Gambler":
                this.grainNum += 2 * grainNum;
                break;
            default:
                this.grainNum += grainNum;
        }
    }

    public int getWoolNum() {
        return woolNum;
    }

    public void changeWool(int woolNum) {
        switch (this.classTitle) {
            case "Shepard":
            case "Deity":
                this.woolNum += 4 * woolNum;
                break;
            case "Farmer":
            case "Gambler":
            case "Emperor":
                this.woolNum += 2 * woolNum;
                break;
            case "Woodsman":
            case "Mountaineer":
                pass();
                break;
            default:
                this.woolNum += woolNum;
                break;
        }
    }

    public int getOreNum() {
        return oreNum;
    }

    public void changeOre(int oreNum) {
        switch(classTitle) {
            case "Deity":
                this.oreNum+=4*oreNum;
                break;
            case "Farmer":
            case "Shepard":
            case "Woodsman":
                pass();
                break;
            case "Mountaineer":
            case "Emperor":
            case "Gambler":
                this.oreNum += 2 * oreNum;
                break;
            default:
                this.oreNum += oreNum;
        }
    }

    public void monoOre(int num){
        this.oreNum+=num;
    }
    public void monoWheat(int num){
        this.grainNum+=num;
    }
    public void monoBrick(int num){
        this.brickNum+=num;
    }
    public void monoWool(int num){
        this.woolNum+=num;
    }
    public void monoLumber(int num){
        this.lumberNum+=num;
    }

    public ArrayList<Index> getOwnedIndexes() {
        return ownedIndexes;
    }

    public void addIndex(Index newIndex){
        this.ownedIndexes.add(newIndex);
    }

    public int returnTotalResources(){
        return grainNum+brickNum+lumberNum+woolNum+oreNum;
    }

    public void failGamble(){
        this.lumberNum-=1;
        this.woolNum-=1;
        this.brickNum-=1;
        this.oreNum-=1;
        this.grainNum-=1;
    }

    public void monoAll(int lumber, int wool, int brick, int ore, int grain){
        this.monoLumber(costMultiplier?2*lumber:lumber);
        this.monoWool(costMultiplier?2*wool:wool);
        this.monoBrick(costMultiplier?2*brick:brick);
        this.monoOre(costMultiplier?2*ore:ore);
        this.monoWheat(costMultiplier?2*grain:grain);
    }

    public void pass(){
        //Tells the class that nothing needs to be done, but an if statement needs to be caught because of the 'else' statement. Python-inspired
    }

    public void winTheGame(){
        cb.updateAllStatusMenus();
        if (this.getVictoryPointTotal() >= 10) {
            loadUpVictoryFrame();
            JOptionPane.showMessageDialog(victory, this.getName() + ", you've won this 'Settlers of Catan'® game.", "Game's End", 1, new ImageIcon("Resources/Catan_Icon.png"));
            System.exit(0);
        }

    }
}
