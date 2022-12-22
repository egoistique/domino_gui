package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    //определить кто ходит первым
    public Player whoIsFirstMove(List<Player> players) {
        for (Player p : players) {
            p.sorting();
        }
        Map<Integer, Player> map = new HashMap<>();
        int maxKey = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPackOfTiles().get(0).first == players.get(i).getPackOfTiles().get(0).last) {
                map.put(players.get(i).getPackOfTiles().get(0).first * 2, players.get(i));
                if (players.get(i).getPackOfTiles().get(0).first * 2 > maxKey) {
                    maxKey = players.get(i).getPackOfTiles().get(0).first * 2;
                }
            }
        }

        if (map.size() == 0) {
            //Логика, которая выбирает максимальное значение Tile у игрока
            int max = 0;
            Player p1 = new Player();
            for (Player p : players) {
                if (p.getPackOfTiles().get(0).first + p.getPackOfTiles().get(0).last > max){
                    max = p.getPackOfTiles().get(0).first + p.getPackOfTiles().get(0).last;
                    p1 = p;
                }
            }
            return p1;
        } else {
            return map.get(maxKey);
        }

    }

    //раздает кости игрокам
    public void givingTilesToPlayers(List<Player> players, Heap heap){
        for(Player p : players){
            if(players.size() == 2){
                p.setPackOfTiles(heap.give(heap.getBazar(), 7));
            } else {
                p.setPackOfTiles(heap.give(heap.getBazar(), 5));
            }
        }
    }

    //определяет очередность хода
    public Player defineMover(List<Player> players, Player p){
        for (int i = 0; i < players.size(); i++){
            if(players.get(i).equals(p)){
                return (i == players.size() - 1) ? players.get(0) : players.get(i + 1);
            }
        }
        return players.get(0);
    }
}
