package ruvsu.domino.model;

import java.util.HashMap;
import java.util.Map;

public class GameBoard {

    public static final int SIZE = 25;
    public final int DOMINO_CODE_HORIZONTAL = 127025;
    public final int DOMINO_CODE_VERTICAL = 127075;
    public final int STEP = 7;

    String[][] field;
    Map<Integer, Tile> tileImages;

    public GameBoard() {
        field = new String[SIZE][SIZE];
        tileImages = new HashMap<>();
        initEmptyBoard();
        initTileImages();
    }

    public String[][] returnField(){
        return field;
    }

    public void initTileImages(){
        int m = 0;
        for(int i = 0; i < STEP; i++){
            for(int j = 0; j < STEP; j++){
                if(i == j){
                    tileImages.put(DOMINO_CODE_VERTICAL + i * (STEP + 1), new Tile(i, j));
                } else {
                    tileImages.put(DOMINO_CODE_HORIZONTAL + m, new Tile(i, j));
                }
                m++;
            }
        }
    }

    public void initEmptyBoard(){
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                field[i][j] = "";
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

    public Map<Coordinates, Integer> putFirstTile(Tile tile, Map<Coordinates, Integer> activeTiles) {
        initTileImages();

        int code = getCode(tileImages, tile);

        field[SIZE / 2][SIZE / 2] = String.valueOf(Character.toChars(code));
        field[SIZE / 2][SIZE / 2 - 1] = ".";
        field[SIZE / 2][SIZE / 2 + 1] = ".";

        activeTiles.put(new Coordinates(SIZE / 2, SIZE / 2 - 1), tile.first);
        activeTiles.put(new Coordinates(SIZE / 2, SIZE / 2 + 1), tile.last);
        return activeTiles;
    }

    public Map<Coordinates, Integer> putTile(Tile tile, Map<Coordinates, Integer> activeTiles) {
        initTileImages();
        Coordinates location;
        int first = tile.first;
        int last = tile.last;

        if (activeTiles.containsValue(first) && first != last) { //если на столе есть свободная кость с числом равным first
            return helpToMove(activeTiles, last, first);
        } else if (activeTiles.containsValue(last) && first != last) {//если на столе есть свободная кость с числом равным last
            return helpToMove(activeTiles, first, last);
        } else if (activeTiles.containsValue(first) && first == last) {//если дубль
            location = getKey(activeTiles, last);
            int code = getCode(tileImages, tile);

            field[location.row][location.col] = String.valueOf(Character.toChars(code));
            field[location.row + 1][location.col] = ".";
            field[location.row - 1][location.col] = ".";
            activeTiles.remove(getKeyForRemove(activeTiles, location.row, location.col));

            activeTiles.put(new Coordinates(location.row + 1, location.col), last);
            activeTiles.put(new Coordinates(location.row - 1, location.col), last);
        }
        return activeTiles;
    }

    private Map<Coordinates, Integer> helpToMove(Map<Coordinates, Integer> activeTiles, int first, int last) {
        Coordinates location;
        location = getKey(activeTiles, last);

        if (location.row < location.col) {//15 16
            int code = getCode(tileImages, new Tile(last, first));

            field[location.row][location.col] = String.valueOf(Character.toChars(code));
            field[location.row][location.col + 1] = ".";

            activeTiles.remove(getKeyForRemove(activeTiles,location.row, location.col));
            activeTiles.put(new Coordinates(location.row, location.col + 1), first);
            return activeTiles;
        } else {//15 14
            int code = getCode(tileImages, new Tile(first, last));

            field[location.row][location.col] = String.valueOf(Character.toChars(code));
            field[location.row][location.col - 1] = ".";

            activeTiles.remove(getKeyForRemove(activeTiles,location.row, location.col));
            activeTiles.put(new Coordinates(location.row, location.col - 1), first);
            return activeTiles;
        }
    }

    private Coordinates getKey(Map<Coordinates, Integer> activeTiles, int m) {
        for (Map.Entry<Coordinates, Integer> entry : activeTiles.entrySet()) {
            Coordinates key = entry.getKey();
            int value = entry.getValue();
            if (value == m) {
                return key;
            }
        }
        return new Coordinates(0, 0);
    }

    private Coordinates getKeyForRemove(Map<Coordinates, Integer> activeTiles, int row, int column) {
        for (Map.Entry<Coordinates, Integer> entry : activeTiles.entrySet()) {
            Coordinates key = entry.getKey();
            if (key.col == column && key.row == row) {
                return key;
            }
        }
        return new Coordinates(row, column);
    }

    protected Integer getCode(Map<Integer, Tile> tileImages, Tile tile) {
        for (Map.Entry<Integer, Tile> entry : tileImages.entrySet()) {
            Integer key = entry.getKey();
            Tile value = entry.getValue();
            if (value.last == tile.last && value.first == tile.first) {
                return key;
            }
        }
        return 0;
    }
}
