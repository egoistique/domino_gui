package ruvsu.domino;

import java.util.Map;

public class Console {
    public static final int SIZE = 30;
    String[][] field = new String[SIZE][SIZE];

    public void create(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                field[i][j] = "  ";
            }
        }
    }

    public void output(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public Map<Coords, Integer> putFirstTile(Tile tile, Map<Coords, Integer> activeTiles) {
        field[SIZE / 2][SIZE / 2] = tile.first + "" + tile.last;
        field[SIZE / 2][SIZE / 2 - 1] = ".";
        field[SIZE / 2][SIZE / 2 + 1] = ".";

        activeTiles.put(new Coords(SIZE / 2, SIZE / 2 - 1), tile.first);
        activeTiles.put(new Coords(SIZE / 2, SIZE / 2 + 1), tile.last);
        return activeTiles;
    }

    public Map<Coords, Integer> putTile(Tile tile, Map<Coords, Integer> activeTiles) {
        Coords location;
        int first = tile.first;
        int last = tile.last;

        if (activeTiles.containsValue(first) && first != last) { //если на столе есть свободная кость с числом равным first
            return helpToMove(activeTiles, last, first);
        } else if (activeTiles.containsValue(last) && first != last) {//если на столе есть свободная кость с числом равным last
            return helpToMove(activeTiles, first, last);
        } else if (activeTiles.containsValue(first) && first == last) {//если дубль
            location = getKey(activeTiles, last);

            field[location.row][location.col] = "||";

            if (location.row < location.col) { //15 16
                field[location.row + 1][location.col] = "" + first;
                field[location.row - 1][location.col] = "" + first;
                field[location.row + 1][location.col + 1] = ".";
                field[location.row - 1][location.col + 1] = ".";
                activeTiles.remove(getKeyForRemove(activeTiles, location.row, location.col));

                activeTiles.put(new Coords(location.row + 1, location.col + 1), last);
                activeTiles.put(new Coords(location.row - 1, location.col + 1), last);
            } else { //15 14 вроде норм
                field[location.row + 1][location.col] = first + "";
                field[location.row - 1][location.col] = first + "";
                field[location.row + 1][location.col - 1] = ".";
                field[location.row - 1][location.col - 1] = ".";

                activeTiles.remove(getKeyForRemove(activeTiles, location.row, location.col));
                activeTiles.put(new Coords(location.row + 1, location.col - 1), last);
                activeTiles.put(new Coords(location.row - 1, location.col - 1), last);
            }
        }
        return activeTiles;
    }

    private Map<Coords, Integer> helpToMove(Map<Coords, Integer> activeTiles, int first, int last) {
        Coords location;
        location = getKey(activeTiles, last);

        if (location.row < location.col) {//15 16
            field[location.row][location.col] = last + "" + first;
            field[location.row][location.col + 1] = ".";

            activeTiles.remove(getKeyForRemove(activeTiles,location.row, location.col));
            activeTiles.put(new Coords(location.row, location.col + 1), first);
            return activeTiles;
        } else {//15 14
            field[location.row][location.col] = first + "" + last;
            field[location.row][location.col - 1] = ".";

            activeTiles.remove(getKeyForRemove(activeTiles,location.row, location.col));
            activeTiles.put(new Coords(location.row, location.col - 1), first);
            return activeTiles;
        }
    }

    private Coords getKey(Map<Coords, Integer> activeTiles, int m) {
        for (Map.Entry<Coords, Integer> entry : activeTiles.entrySet()) {
            Coords key = entry.getKey();
            int value = entry.getValue();
            if (value == m) {
                return key;
            }
        }
        return new Coords(0, 0);
    }

    private Coords getKeyForRemove(Map<Coords, Integer> activeTiles, int row, int column) {
        for (Map.Entry<Coords, Integer> entry : activeTiles.entrySet()) {
            Coords key = entry.getKey();
            if (key.col == column && key.row == row) {
                return key;
            }
        }
        return new Coords(row, column);
    }
}
