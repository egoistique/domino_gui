package ruvsu.domino.client.network;

import ruvsu.domino.model.*;

import java.util.*;

public class NetworkGameProcess extends AbstractGame {

//    private boolean gameOver = false;
//
//    public final Heap heap = new Heap();
//    public final Table table = new Table();
//    public final GameBoard board = new GameBoard();
//    private List<Player> players = new ArrayList<>();
//    private Map<Coordinates, Integer> activeTiles = new HashMap<>();
//
//    public Scanner sc = new Scanner(System.in);

//    private Tile currTile;

//    private int checkFor = 0;


    public NetworkGameProcess(Player pl) {
        super(pl);
    }

    @Override
    public int getCheckFor() {
        return 0;
    }

    @Override
    public Player getPl() {
        return null;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public List<Player> getPlayers() {
        return null;
    }

    @Override
    public Map<Coordinates, Integer> getActiveTiles() {
        return null;
    }

    @Override
    public void setActiveTiles(Map<Coordinates, Integer> activeTiles) {

    }

//    //создать игроков
//    private void createPlayers(int numPlayers) {
//        for (int i = 0; i < numPlayers; i++) {
//            players.add(new Player("Player " + i));
//        }
//    }


    @Override
    public Player beginGamePr(int numPl) {
        return null;
    }

    @Override
    public Player gameStep(int view, String code) {
        //server.send(pl.getPackOfTiles());
        //server.getResponse();
        return null;
    }

    @Override
    public void gameOverCheck(List<Player> players) {

    }
}
