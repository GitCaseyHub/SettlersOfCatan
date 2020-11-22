import java.util.ArrayList;

public class Player {
    String color,name,classTitle;
    int brickNum,lumberNum,grainNum,woolNum,oreNum,victoryPointTotal, referenceNumber;
    boolean turn,longestRoad,largestArmy;
    ArrayList<Index> ownedIndexes;
    ArrayList<DevelopmentCard> playedCards, unPlayedCards;

    public Player(String color, String name, String classTitle, ArrayList<Index> ownedIndexes, ArrayList<DevelopmentCard> unPlayedCards, ArrayList<DevelopmentCard> playedCards, int brickNum, int lumberNum, int grainNum, int woolNum, int oreNum, int victoryPointTotal, boolean turn, boolean longestRoad, boolean largestArmy, int referenceNumber){
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
    }

    public int getRefNumber() {
        return referenceNumber;
    }

    public void setRefNumber(int refNumber) {
        this.referenceNumber = referenceNumber;
    }

    //Testing Constructor
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

    public void setLargestArmy(boolean largestArmy) {
        this.largestArmy = largestArmy;
    }

    public int getVictoryPointTotal() {
        return victoryPointTotal;
    }

    public void changeVictoryPoints(int pointChange) {
        this.victoryPointTotal+=pointChange;
    }

    public ArrayList<DevelopmentCard> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(ArrayList<DevelopmentCard> playedCards) {
        this.playedCards = playedCards;
    }

    public ArrayList<DevelopmentCard> getUnPlayedCards() {
        return unPlayedCards;
    }

    public void setUnPlayedCards(ArrayList<DevelopmentCard> unPlayedCards) {
        this.unPlayedCards = unPlayedCards;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassTitle() {
        return classTitle;
    }

    public void setClassTitle(String classTitle) {
        this.classTitle=classTitle;
    }

    public int getBrickNum() {
        return brickNum;
    }

    public void changeBrick(int brickNum) {
        if(this.classTitle.equals("Farmer") || this.classTitle.equals("Shepard") || this.classTitle.equals("Woodsman"))
            this.brickNum+=0;
        else if(this.classTitle.equals("Mountaineer"))
            this.brickNum+=2*brickNum;
        else
            this.brickNum+=brickNum;
    }

    public int getLumberNum() {
        return lumberNum;
    }

    public void changeLumber(int lumberNum) {
        if(this.classTitle.equals("Woodsman"))
            this.lumberNum+=4*lumberNum;
        else if(this.classTitle.equals("Shepard"))
            this.lumberNum+=0;
        else
            this.lumberNum += lumberNum;
    }

    public int getGrainNum() {
        return grainNum;
    }

    public void changeGrain(int grainNum) {
        if(this.classTitle.equals("Mountaineer") || this.classTitle.equals("Woodsman")||this.classTitle.equals("Shepard"))
            this.grainNum+=0;
        else if(this.classTitle.equals("Farmer"))
            this.grainNum+=2*grainNum;
        else
            this.grainNum+= grainNum;
    }

    public int getWoolNum() {
        return woolNum;
    }

    public void changeWool(int woolNum) {
        if(this.classTitle.equals("Shepard"))
            this.woolNum+=4*woolNum;
        else if(this.classTitle.equals("Farmer"))
            this.woolNum+=2*woolNum;
        else if(this.classTitle.equals("Woodsman") || this.classTitle.equals("Mountaineer"))
            this.woolNum+=0;
        else
            this.woolNum+=woolNum;
    }

    public int getOreNum() {
        return oreNum;
    }

    public void changeOre(int oreNum) {
        if(this.classTitle.equals("Farmer") || this.classTitle.equals("Shepard") || this.classTitle.equals("Woodsman"))
            this.oreNum+=0;
        else if(this.classTitle.equals("Mountaineer"))
            this.oreNum+=2*oreNum;
        else
            this.oreNum+= oreNum;
    }

    public ArrayList<Index> getOwnedIndexes() {
        return ownedIndexes;
    }

    public void addIndex(Index newIndex){
        this.ownedIndexes.add(newIndex);
    }

    public String toString(){
        return "Player Ref Number: "+referenceNumber;
    }
}
