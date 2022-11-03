package ruvsu.domino.ui;

import ruvsu.domino.Tile;

import javax.swing.*;
import java.util.List;

public class WindowUI {
    public static void main(String[] args) {
        Tile tile11 = new Tile(1, 1);
        Tile tile33 = new Tile(3, 3);

        JFrame frame = new DominoFrame("DOMINO", List.of(tile11, tile33));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
        System.out.println("\u2764 ");
    }
}
