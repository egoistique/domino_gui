package client.console;


import model.GameBoard;
import model.Player;
import model.Tile;

import java.util.List;

public class ConsoleUtils {

    public static void outPoints(List<Player> players){
        int[] sums = countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++){
            System.out.println("Игрок " + i + " набрал " + sums[i] + " очков");
        }
    }

    public static int[] countPlayersPoints(List<Player> players){
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

    public static int countTotalPoints(Player p){
        int sum = 0;
        for (Tile t :  p.getPackOfTiles()){
            sum += t.first + t.last;
            if (t.last == t.first && t.last == 0){
                sum += 10;
            }
        }
        return sum;
    }

    public static void output(List<Player> players, GameBoard field) {
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

    public static int tileToString(Tile tile, GameBoard field) {
        return field.getCode(field.tileImages, tile);
    }
}
