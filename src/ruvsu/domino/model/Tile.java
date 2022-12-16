package ruvsu.domino.model;

import java.io.Serializable;

public class Tile implements Serializable {

    public final int first;
    public final int last;

    public Tile(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public int getSum() {
        return first + last;
    }
//
//    public String getCode(GameBoard field){
//        field.getCode(pl.getPackOfTiles().get(i));
//    }
}
