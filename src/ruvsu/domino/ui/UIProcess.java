package ruvsu.domino.ui;

import ruvsu.domino.model.*;
import ruvsu.domino.ui.utils.UIDominoUtils;

import java.util.*;

public class UIProcess {

    private boolean gameOver = false;

    public final Heap heap = new Heap();
    public final Table table = new Table();
    public final GameBoard board = new GameBoard();
    private List<Player> players = new ArrayList<>();
    private Map<Coordinates, Integer> activeTiles = new HashMap<>();

    private Player pl;
    private Tile currTile;

    private int checkFor = 0;

    public int getCheckFor() {
        return checkFor;
    }

    public Player getPl() {
        return pl;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Map<Coordinates, Integer> getActiveTiles() {
        return activeTiles;
    }

    public void setActiveTiles(Map<Coordinates, Integer> activeTiles) {
        this.activeTiles = activeTiles;
    }

    //начало игры, первый ход
    public Player beginGamePr(int numPl) {
        //создать игроков
        createPlayers(numPl);

        //раздать кости игрокам и вывести в текст филды
        table.givingTilesToPlayers(players, heap);

        //определить кто ходит первым, включить радиобаттнон
        pl = table.whoIsFirstMove(players);
        return pl;
    }

    //шаг
    public Player gameStep(String code) {
        //определить кто ходит
        pl = table.defineMover(players, pl);
        //включить радиобаттон

        //получить кость которой игрок хочет походить
        if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("")) {
            currTile = pl.makeInteractiveMove(code, board);
        } else {
            currTile = pl.makeAMove(activeTiles, heap);
        }

        //положить на стол кость
        activeTiles = board.putTile(currTile, activeTiles);

        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
            currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
            activeTiles = board.putTile(currTile, activeTiles);
        }

        if (currTile.first == 99 && currTile.last == 99) {//если игрок не смог походить после добирания из базара счетчик увеличивается
            checkFor++;
        } else { //иначе обнуляется
            checkFor = 0;
        }

        return pl;
    }

    //создать игроков
    private void createPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }
    }

    //проверить условие конца игры
    public void gameOverCheck(List<Player> players) {
        for (Player p : players) {
            if (p.getPackOfTiles().size() == 0) {
                gameOver = true;
                return;
            }
        }
    }

}
