package ruvsu.domino.model;

public class Tile {

    public final int first;
    public final int last;

    public Tile(int first, int last) {
        this.first = first;
        this.last = last;
    }

    public int getSum() {
        return first + last;
    }

}
