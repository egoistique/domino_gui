package ruvsu.domino.ui;

import ruvsu.domino.model.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void processUI(int numPl){
        beginGamePr(numPl);
        //сделать первый шаг, вывести актуальное состояние гейм борда и обновить текст филд ходящего игрока
        firstTile = pl.makeAFirstMove();
        activeTiles = board.putFirstTile(firstTile, activeTiles);


        while (!gameOver && checkFor < players.size()){
            //шаг игры
            gameStep();

            //вывести состояние гейм борда

            //вывести в каждый текст филд текущие пак оф тайлсы

            //проверить на гейм овер
            gameOverCheck(players);
        }

        //вывести в каждый текст филд суммы очков
    }

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

    public String[][] returnField(){
        return board.getField();
    }

    public void gameStep(){
        //определить кто ходит
        pl = table.defineMover(players, pl);
        //включить радиобаттон

        //получить кость которой игрок хочет походить
        currTile = pl.makeAMove(activeTiles, heap);

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

    public void gameOverCheck(List<Player> players){
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
