package client.console;


import client.ui.utils.UIDominoUtils;
import model.GameBoard;
import model.Player;

import java.util.List;

public class ConsoleUtils {

    public static void outPoints(List<Player> players){
        int[] sums = UIDominoUtils.countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++){
            System.out.println("Игрок " + i + " набрал " + sums[i] + " очков");
        }
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
}
