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

    public String[][] packToString(GameBoard field){
        int code;
        String[][] tiles = new String[2][packOfTiles.size()];
        for (int i = 0; i < packOfTiles.size(); i++){
            code = field.getCode(field.tileImages, packOfTiles.get(i));
            tiles[0][i] = String.valueOf(Character.toChars(code));
            tiles[1][i] = String.valueOf((code));
        }
        return tiles;
    }

    //сделать первый ход
    public Tile makeAFirstMove() {
        Tile fir = packOfTiles.get(0);
        packOfTiles.remove(0);
        return fir;
    }

    public Tile makeInteractiveConsoleMove(int num){
        Tile active = packOfTiles.get(num);
        packOfTiles.remove(num);
        return active;
    }

    public Tile makeInteractiveMove(String st, GameBoard gb){
        int code = Integer.parseInt(st);
        Tile active = gb.tileImages.get(code);
        int index = 0;
        for(int i = 0; i < packOfTiles.size(); i++){
            if(packOfTiles.get(i).first == active.first && packOfTiles.get(i).last == active.last){
                index = i;
            }
        }

        packOfTiles.remove(index);

        return active;
    }

    public void mainPlayerTakeFromBazar(Heap heap){
        if (heap.getBazarSize() > 0){
            Tile tile = heap.removeTile(0);
            packOfTiles.add(tile);
        }
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

        if (heap.getBazarSize() > 0){
            Tile tile = heap.removeTile(0);
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
