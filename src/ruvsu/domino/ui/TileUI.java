package ruvsu.domino.ui;

import ruvsu.domino.Tile;

import javax.swing.*;
import java.awt.*;

public class TileUI extends JButton {
    private final Tile tile;

    public TileUI(Tile tile){
        this.tile = tile;
        setText(new String(Character.toChars(127026)));
        setPreferredSize(new Dimension(100, 100));
    }


}