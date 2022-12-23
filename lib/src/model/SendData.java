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

    public void setGameBoard(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Heap getHeap() {
        return heap;
    }

    public void setHeap(Heap heap) {
        this.heap = heap;
    }

    public Player getPl() {
        return pl;
    }

    public void setPl(Player pl) {
        this.pl = pl;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getCheckFor() {
        return checkFor;
    }

    public void setCheckFor(int checkFor) {
        this.checkFor = checkFor;
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
