import java.util.ArrayList;

public class DevelopmentCard{
    String type="";
    Player player;
    CatanBoard cbReference;
    ArrayList<Player> otherPlayers;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public CatanBoard getCbReference() {
        return cbReference;
    }

    public void setCbReference(CatanBoard cbReference) {
        this.cbReference = cbReference;
    }

    public ArrayList<Player> getOtherPlayers() {
        return otherPlayers;
    }

    public DevelopmentCard(String type, Player player, ArrayList<Player> otherPlayers, CatanBoard cbReference){
        //Types of Cards: Knight, Victory Points, Road Builder, Year of Plenty, Monopoly
        this.type=type;
        this.player=player;
        this.cbReference=cbReference;
        this.otherPlayers=otherPlayers;
    }

    public void playCard(){
        if(type.equals("Knight"))
            performKnightAction();

        else if(type.equals("Victory Points"))
            performVictoryPoints();

        else if(type.equals("Road Builder"))
            performRoadBuilding();

        else if(type.equals("Year of Plenty"))
            performYearOfPlenty();

        else
            performMonopoly();
    }

    public void performKnightAction(){}
    public void performVictoryPoints(){
        player.changeVictoryPoints(1);
    }
    public void performRoadBuilding(){}
    public void performYearOfPlenty(){}
    public void performMonopoly(){}
}
