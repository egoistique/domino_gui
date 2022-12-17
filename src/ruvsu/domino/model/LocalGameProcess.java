package ruvsu.domino.model;

import ruvsu.domino.client.ui.utils.UIDominoUtils;

import java.util.*;

public class LocalGameProcess extends AbstractGame {

    private List<Player> players = new ArrayList<>();
    private Map<Coordinates, Integer> activeTiles = new HashMap<>();

    private Tile currTile;

    private int checkFor = 0;

    public LocalGameProcess(Player pl) {
        super(pl);
    }

    public Heap getHeap(){
        return heap;
    }

    public GameBoard getBoard(){
        return board;
    }

    @Override
    public int getCheckFor() {
        return checkFor;
    }

    @Override
    public Player getPl() {
        return pl;
    }

    @Override
    public boolean isGameOver() {
        return gameOver;
    }

    @Override
    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public Map<Coordinates, Integer> getActiveTiles() {
        return activeTiles;
    }

    @Override
    public void setActiveTiles(Map<Coordinates, Integer> activeTiles) {
        this.activeTiles = activeTiles;
    }

    //начало игры, первый ход
    @Override
    public Player beginGamePr(int view, int numPl) {
        //создать игроков
        createPlayers(numPl);

        //раздать кости игрокам
        table.givingTilesToPlayers(players, heap);

        //определить кто ходит первым
        pl = table.whoIsFirstMove(players);
        return pl;
    }

    //шаг
    @Override
    public Player gameStep(int view, String code) {
        //определить кто ходит
        pl = table.defineMover(players, pl);

        //получить кость которой игрок хочет походить
        if(view == 1){ //если view = 1 то это консоль
            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles())) {
                System.out.println("Выберите плитку, которой хотите походить");
                int num = sc.nextInt();
                currTile = pl.makeInteractiveConsoleMove(num);
            } else {
                currTile = pl.makeAMove(activeTiles, heap);
            }
        }else if (view == 2){ //если view = 2 то это оконное приложение
            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("")) {
                currTile = pl.makeInteractiveMove(code, board);
            } else{
                currTile = pl.makeAMove(activeTiles, heap);
            }
        } else if(view == 3){//если view = 3 то это клиент сервер
            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("")) {
                currTile = pl.makeInteractiveMove(code, board);
            } else{
                currTile = pl.makeAMove(activeTiles, heap);
            }
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

    @Override
    public void firstStep(){
        Tile firstTile = getPl().makeAFirstMove();
        setActiveTiles(board.putFirstTile(firstTile, getActiveTiles()));
    }

    //создать игроков
    public List<Player> createPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }
        return players;
    }

    //проверить условие конца игры
    @Override
    public void gameOverCheck(List<Player> players) {
        for (Player p : players) {
            if (p.getPackOfTiles().size() == 0) {
                gameOver = true;
                return;
            }
        }
    }

}
