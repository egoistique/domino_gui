package ruvsu.domino.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameProcess {

    private boolean gameOver = false;

    public final Heap heap = new Heap();
    public final Table table = new Table();
    public final Asking ask = new Asking();
    public final GameBoard board = new GameBoard();
    List<Player> players = new ArrayList<>();
    Map<Coordinates, Integer> activeTiles = new HashMap<>();

    private Player pl;
    private Tile firstTile;
    private Tile currTile;

    int checkFor = 0;

    public void processConsole(int numPl){
        createPlayers(numPl);

        table.givingTilesToPlayers(players, heap);

        System.out.println("до 1 хода распределение костей такое: ");
        output(players, board);

        pl = table.whoIsFirstMove(players);

        firstTile = pl.makeAFirstMove();
        activeTiles = board.putFirstTile(firstTile, activeTiles);

        board.output();

        while (!gameOver && checkFor < players.size()){
            gameStep();

            board.output();

            System.out.println("распределение костей такое: ");
            output(players, board);
            gameOverCheck(players);
        }

        System.out.println("Конец игры " + "\n Финальный результат игроков ");

        outPoints();
    }

    public String[][] returnField(){
        return board.field;
    }

    private void gameStep(){
        pl = table.defineMover(players, pl); //определить кто ходит

        currTile = pl.makeAMove(activeTiles, heap); //получить кость которой игрок хочет походить
        activeTiles = board.putTile(currTile, activeTiles);//положить на стол кость

        if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
            currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
            activeTiles = board.putTile(currTile, activeTiles);
        }

        if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить после добирания из базара счетчик увеличивается
            checkFor++;
        } else { //иначе обнуляется
            checkFor = 0;
        }

    }

    private void outPoints(){
        int[] sums = countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++){
            System.out.println("Игрок " + i + " набрал " + sums[i] + " очков");
        }
    }

    private void createPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers; i++) {
            players.add(new Player());
        }
    }

    private int[] countPlayersPoints(List<Player> players){
        int[] sums = new int[players.size()];
        int fullSum = 0;
        for (int i = 0; i < players.size(); i++){
            sums[i] = countTotalPoints(players.get(i));
            fullSum += countTotalPoints(players.get(i));
        }

        for (int i = 0; i < players.size(); i++){
            if (sums[i] == 0){
                sums[i] = fullSum;
            }
        }
        return sums;
    }

    public int countTotalPoints(Player p){
        int sum = 0;
        for (Tile t :  p.getPackOfTiles()){
            sum += t.first + t.last;
            if (t.last == t.first && t.last == 0){
                sum += 10;
            }
        }
        return sum;
    }

    private void gameOverCheck(List<Player> players){
        for (Player p : players){
            if(p.getPackOfTiles().size() == 0){
                gameOver = true;
                return;
            }
        }
    }

    private void output(List<Player> players, GameBoard field) {
        int code;
        for (int i = 0; i < players.size(); i++){
            System.out.println("Player " + i );
            for (int j = 0; j < players.get(i).getPackOfTiles().size(); j++){
                code = field.getCode(field.tileImages, players.get(i).getPackOfTiles().get(j));
                System.out.print(Character.toChars(code));
                System.out.print(" ");
            }
            System.out.println();
        }
    }
}
