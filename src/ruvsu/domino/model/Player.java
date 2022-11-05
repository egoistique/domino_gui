package ruvsu.domino.model;

import java.util.*;

public class Player {
    //набор костей на руках игрока
    private List<Tile> packOfTiles;

    public Player() {
        packOfTiles = new ArrayList<>();
    }

    public List<Tile> getPackOfTiles() {
        return packOfTiles;
    }

    public void setPackOfTiles(List<Tile> packOfTiles) {
        this.packOfTiles = packOfTiles;
    }


    //сделать первый ход
    public Tile makeAFirstMove() {
        Tile fir = packOfTiles.get(0);
        packOfTiles.remove(0);
        return fir;
    }

    public Tile makeAMove(Map<Coordinates, Integer> activeTiles, Heap heap) {
        Tile active;
        for (int i = 0; i < packOfTiles.size(); i++) {
            int first = packOfTiles.get(i).first;
            int last = packOfTiles.get(i).last;
            active = packOfTiles.get(i);
            if (activeTiles.containsValue(first) || activeTiles.containsValue(last)) {
                packOfTiles.remove(i);
                return active;
            }
        }

        if (heap.bazar.size() > 0){
            Tile tile = heap.bazar.remove(0);
            packOfTiles.add(tile);
        }

        System.out.println("not found");

        return new Tile(99, 99);
    }

    //отсортировать набор
    protected List<Tile> sorting() {
        packOfTiles.sort(Comparator.comparing(Tile::getSum));
        Tile dub;
        for (int i = 0; i < packOfTiles.size(); i++) {
            if (packOfTiles.get(i).first == packOfTiles.get(i).last) {
                dub = packOfTiles.remove(i);
                packOfTiles.add(dub);
            }
        }
        Collections.reverse(packOfTiles);
        return packOfTiles;
    }
}
