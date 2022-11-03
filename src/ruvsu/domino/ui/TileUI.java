package ruvsu.domino.ui;

import ruvsu.domino.Tile;

import javax.swing.*;
import java.awt.*;

public class TileUI extends JButton {
    private final Tile tile;

    public TileUI(Tile tile){
        this.tile = tile;
        setText("\u1F038" + " " + tile.first + " " + tile.last);
        //add(new JLabel("\\U+1F039"));
        setPreferredSize(new Dimension(100, 100));
    }


}