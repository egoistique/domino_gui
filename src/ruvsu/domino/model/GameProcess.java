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
    public final GameBoard field = new GameBoard();

    List<Player> players = new ArrayList<>();
    Map<Coordinates, Integer> activeTiles = new HashMap<>();

    Tile firstTile;
    Tile currTile;

    int countMoves = 0;
    int checkFor = 0;


    public void process(){
        field.initEmptyBoard();

        heap.shuffleHeap(heap.bazar);

        int numOfPlayers = ask.askNumberOfPlayers();
        players.add(new Player());
        players.add(new Player());
        if(numOfPlayers == 3){
            players.add(new Player());
        } else if(numOfPlayers == 4){
            players.add(new Player());
            players.add(new Player());
        }

        //раздаем кости игрокам
        table.givingTilesToPlayers(players, heap);

        System.out.println(Character.toChars(127083));

        System.out.println("до 1 хода распределение костей такое: ");
        output(players, field);

        Player pl = table.whoIsFirstMove(players);

        firstTile = pl.makeAFirstMove();
        activeTiles = field.putFirstTile(firstTile, activeTiles);

        field.output();

        while (!gameOver && checkFor < players.size()){
            countMoves++;
            pl = table.defineMover(players, pl); //определить кто ходит

            currTile = pl.makeAMove(activeTiles, heap); //получить кость которой игрок хочет походить
            activeTiles = field.putTile(currTile, activeTiles);//положить на стол кость

            if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
                currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
                activeTiles = field.putTile(currTile, activeTiles);
            }

            if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить после добирания из базара счетчик увеличивается
                checkFor++;
            } else { //иначе обнуляется
                checkFor = 0;
            }

            field.output();

            System.out.println("после " + countMoves + " хода распределение костей такое: ");
            output(players, field);
            gameOverCheck(players);
        }

        System.out.println("Конец игры ");
        System.out.println("Финальный результат игроков ");

        int[] sums = countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++){
            System.out.println("Игрок " + i + " набрал " + sums[i] + " очков");
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
