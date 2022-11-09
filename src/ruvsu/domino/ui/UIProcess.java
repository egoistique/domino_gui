package ruvsu.domino.ui;

import ruvsu.domino.model.*;

import javax.swing.*;
import java.util.*;

public class UIProcess {

    public boolean gameOver = false;

    public final Heap heap = new Heap();
    public final Table table = new Table();
    public final GameBoard board = new GameBoard();
    List<Player> players = new ArrayList<>();
    Map<Coordinates, Integer> activeTiles = new HashMap<>();

     Player pl;
     Tile firstTile;
     Tile currTile;

    int checkFor = 0;

    public Player beginGamePr(int numPl){
        //создать игроков
        createPlayers(numPl);

        //раздать кости игрокам и вывести в текст филды
        table.givingTilesToPlayers(players, heap);

        //определить кто ходит первым, включить радиобаттнон
        pl = table.whoIsFirstMove(players);
        return pl;
    }

    public String packTilesToString(Player pl,  GameBoard field){
        StringBuilder sb = new StringBuilder();
        int code;
        for (int i = 0; i < pl.getPackOfTiles().size(); i++){
            code = field.getCode(field.tileImages, pl.getPackOfTiles().get(i));
            String s = new String(Character.toChars(code));
            sb.append(s);
        }
        return sb.toString();
    }

    public String bazarToString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < heap.getBazarSize(); i++){
            String s = new String(Character.toChars(127074));
            sb.append(s);
        }
        return sb.toString();
    }

    public String[][] returnField(){
        return board.getField();
    }

    public Player gameStep(String code){
        //определить кто ходит
        pl = table.defineMover(players, pl);
        //включить радиобаттон

        //получить кость которой игрок хочет походить
        if (listEqualsIgnoreOrder(players.get(0).getPackOfTiles(), pl.getPackOfTiles()) && !code.equals("0")){
            currTile = pl.makeInteractiveMove(code, board);
        }else {
            currTile = pl.makeAMove(activeTiles, heap);
        }

        //положить на стол кость
        activeTiles = board.putTile(currTile, activeTiles);

        if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить (makeAMove вернул Tile(99,99)) он добрал из базара
            currTile = pl.makeAMove(activeTiles, heap); //еще одна попытка походить
            activeTiles = board.putTile(currTile, activeTiles);
        }

        if(currTile.first == 99 && currTile.last == 99){//если игрок не смог походить после добирания из базара счетчик увеличивается
            checkFor++;
        } else { //иначе обнуляется
            checkFor = 0;
        }

        return pl;
    }



    public String outPoints(){
        String s = "";
        int[] sums = countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++){
            s += ("\nИгрок " + i + " набрал " + sums[i] + " очков \n");
        }
        return s;
    }

    public String defineWinner(){
        int[] sums = countPlayersPoints(players);
        int max = Arrays.stream(sums).max().getAsInt();
        int num = 0;
        for (int i = 0; i < sums.length; i++){
            if(sums[i] == max){
                num = i;
            }
        }
        return String.valueOf(num);
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

    public void gameOverCheck(List<Player> players){
        for (Player p : players){
            if(p.getPackOfTiles().size() == 0){
                gameOver = true;
                return;
            }
        }
    }

    public <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }
}
