package ruvsu.domino.model;

public abstract class AbstractGame implements IGameProcess{
    protected Player pl;

    public AbstractGame(Player pl) {
        this.pl = pl;
    }
}
