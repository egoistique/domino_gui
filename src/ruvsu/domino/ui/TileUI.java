package ruvsu.domino.ui;

import ruvsu.domino.model.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

//флоу лейаут
public class TileUI extends JButton {
    private final Tile tile;

    Map<Integer, Tile> tileImages;

    public TileUI(Tile tile){
        this.tile = tile;
        setText(new String(Character.toChars(127026)));
        setPreferredSize(new Dimension(100, 100));
    }

    //создать мап с привязкой tile и его графического изображения
//    public void initTileImages(){
//        for(int i = 0; i < 7; i++){
//            for(int j = 0; j < 7; j++){
//                if(i == j){
//                    tileImages.put(127075 + i * 8, new Tile(i, j));
//                }
//                else {
//                    tileImages.put(127025 + i, new Tile(i, j));
//                }
//            }
//        }
//    }


}