package ruvsu.domino.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Heap implements Serializable {
    //базар, откуда добираются кости
    private List<Tile> bazar;

    public Heap() {
        bazar = new ArrayList<>();
        createHeap();
        shuffleHeap(bazar);
    }

    public List<Tile> getBazar() {
        return bazar;
    }

    public int getBazarSize() {
        return bazar.size();
    }

    public Tile removeTile(int index) {
        return bazar.remove(index);
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
    private void createHeap() {
        for (int i = 0; i < 7; i++) {
            for (int j = i; j < 7; j++) {
                bazar.add(new Tile(i, j));
            }
        }
    }

    private void shuffleHeap(List<Tile> tiles){
        Collections.shuffle(tiles);
    }
}
