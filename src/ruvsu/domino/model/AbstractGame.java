package ruvsu.domino.model;

import java.util.Scanner;

public abstract class AbstractGame implements IGameProcess{
    public Heap heap = new Heap();
    public final GameBoard board = new GameBoard();
    public final Table table = new Table();
    public Scanner sc = new Scanner(System.in);
    protected Player pl;
    protected boolean gameOver = false;

    public AbstractGame(Player pl) {
        this.pl = pl;
    }
}
