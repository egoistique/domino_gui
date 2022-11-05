package ruvsu.domino.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Heap {
    //базар, откуда добираются кости
    public List<Tile> bazar = new ArrayList<>();

    public Heap() {
        this.bazar = createHeap();
    }

    //раздача камней игрокам из колоды
    public List<Tile> give(List<Tile> list, int numOfTiles) {
        List<Tile> given = new ArrayList<>();
        for (int i = 0; i < numOfTiles; i++) {
            Tile tile = list.remove(i);
            given.add(tile);
        }
        return given;
    }

    //создание колоды из 28 камней
    protected List<Tile> createHeap() {
        List<Tile> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                list.add(new Tile(i, j));
            }
        }
        return list;
    }

    protected void shuffleHeap(List<Tile> tiles){
        Collections.shuffle(tiles);
    }
}
