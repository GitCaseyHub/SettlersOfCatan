import javax.swing.*;
import java.util.ArrayList;

public class Player {
    String color,name,classTitle;
    int brickNum,lumberNum,grainNum,woolNum,oreNum,victoryPointTotal, referenceNumber;
    boolean turn,longestRoad,largestArmy;
    ArrayList<Index> ownedIndexes;
    ArrayList<DevelopmentCard> playedCards, unPlayedCards;
    CatanBoard cb;

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
        if(this.classTitle.equals("Farmer") || this.classTitle.equals("Shepard") || this.classTitle.equals("Woodsman")) {
            ;
        }
        else if(this.classTitle.equals("Mountaineer") || this.classTitle.equals("King") || this.classTitle.equals("Gambler"))
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
        else if(this.classTitle.equals("Shepard")) {
            ;
        }
        else if(this.classTitle.equals("King") || this.classTitle.equals("Gambler")){
            this.lumberNum+=2*lumberNum;
        }
        else
            this.lumberNum += lumberNum;
    }

    public int getGrainNum() {
        return grainNum;
    }

    public void changeGrain(int grainNum) {
        if(this.classTitle.equals("Mountaineer") || this.classTitle.equals("Woodsman")||this.classTitle.equals("Shepard")) {
            ;
        }
        else if(this.classTitle.equals("Farmer") || this.classTitle.equals("King") || this.classTitle.equals("Gambler"))
            this.grainNum+=2*grainNum;
        else
            this.grainNum+= grainNum;
    }

    public int getWoolNum() {
        return woolNum;
    }

    public void changeWool(int woolNum) {
        switch (this.classTitle) {
            case "Shepard":
                this.woolNum += 4 * woolNum;
                break;
            case "Farmer":
            case "Gambler":
            case "King":
                this.woolNum += 2 * woolNum;
                break;
            case "Woodsman":
                break;
            case "Mountaineer":
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
        if(this.classTitle.equals("Farmer") || this.classTitle.equals("Shepard") || this.classTitle.equals("Woodsman")) {
            ;
        }
        else if(this.classTitle.equals("Mountaineer") || this.classTitle.equals("King") || this.classTitle.equals("Gambler"))
            this.oreNum+=2*oreNum;
        else
            this.oreNum+= oreNum;
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

    public void winTheGame(){
        cb.updateAllStatusMenus();
        if(this.getVictoryPointTotal()>=10){
            JOptionPane.showMessageDialog(null,this.getName()+", you've won this 'Settlers of Catan'® game. Please play again, everyone.","Game's End",1, new ImageIcon("Resources/Catan_Icon.png"));
            System.exit(0);
        }
    }
}
