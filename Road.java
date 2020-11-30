public class Road {
    int indexA, indexB;
    Player owner;

    public Road(int indexA, int indexB, Player owner){
        this.indexA=indexA;
        this.indexB=indexB;
        this.owner=owner;
    }

    public int getIndexA() {
        return indexA;
    }

    public void setIndexA(int indexA) {
        this.indexA = indexA;
    }

    public int getIndexB() {
        return indexB;
    }

    public void setIndexB(int indexB) {
        this.indexB = indexB;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
