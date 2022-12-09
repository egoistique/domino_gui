package ruvsu.domino.model;

public abstract class AbstractGame implements IGameProcess{
    public final Heap heap = new Heap();
    public final GameBoard board = new GameBoard();
    protected Player pl;

    public AbstractGame(Player pl) {
        this.pl = pl;
    }
}
