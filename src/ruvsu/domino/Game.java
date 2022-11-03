package ruvsu.domino;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private boolean gameOver = false;

    public void process(){
        Heap heap = new Heap();
        Table table = new Table();
        Asking ask = new Asking();
        Console field = new Console();

        Tile firstTile;

        field.create();

        List<Player> players = new ArrayList<>();

        Map<Coords, Integer> activeTiles = new HashMap<>();

        heap.bazar = heap.createHeap();
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

        System.out.println("до 1 хода распределение костей такое: ");
        output(players);

        Player pl = table.whoIsFirstMove(players);

        firstTile = pl.makeAFirstMove();
        activeTiles = field.putFirstTile(firstTile, activeTiles);

        field.output();

        int countMoves = 0;
        int checkforExept = 0;

        Tile currTile;

        while (!gameOver && checkforExept < players.size()){
            countMoves++;
            pl = table.defineMover(players, pl); //определить кто ходит

            currTile = pl.makeAMove(activeTiles, heap); //получить кость которой игрок хочет походить
            activeTiles = field.putTile(currTile, activeTiles);//положить на стол кость

            if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
                currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
                activeTiles = field.putTile(currTile, activeTiles);
            }

            if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить после добирания из базара счетчик увеличивается
                checkforExept++;
            } else { //иначе обнуляется
                checkforExept = 0;
            }

            field.output();

            System.out.println("после " + countMoves + " хода распределение костей такое: ");
            output(players);
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
        int fullSumm = 0;
        for (int i = 0; i < players.size(); i++){
            sums[i] = countTotalPoints(players.get(i));
            fullSumm += countTotalPoints(players.get(i));
        }

        for (int i = 0; i < players.size(); i++){
            if (sums[i] == 0){
                sums[i] = fullSumm;
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

    private void output(List<Player> players) {
        for (int i = 0; i < players.size(); i++){
            System.out.println("Player " + i );
            for (int j = 0; j < players.get(i).getPackOfTiles().size(); j++){
                System.out.print(players.get(i).getPackOfTiles().get(j).first + ". "
                        + players.get(i).getPackOfTiles().get(j).last + "    ");
            }
            System.out.println();
        }
    }
}
