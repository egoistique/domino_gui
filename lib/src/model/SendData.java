package model;

import java.io.Serializable;
import java.util.List;

public class SendData implements Serializable {
    private GameBoard gameBoard;
    private Heap heap;
    private Player pl;
    private List<Player> players;
    private boolean gameOver;
    private int checkFor;

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Heap getHeap() {
        return heap;
    }

    public Player getPl() {
        return pl;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCheckFor() {
        return checkFor;
    }

    public SendData(GameBoard gameBoard, Heap heap, Player pl, List<Player> players, boolean gameOver, int checkFor) {
        this.gameBoard = gameBoard;
        this.heap = heap;
        this.pl = pl;
        this.players = players;
        this.gameOver = gameOver;
        this.checkFor = checkFor;
    }
}
