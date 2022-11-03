package ruvsu.domino.ui;

import ruvsu.domino.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DominoFrame extends JFrame {

    public DominoFrame(String title, List<? extends Tile> tiles) throws HeadlessException {
        super(title);
        setLayout(new FlowLayout());
        List<JButton> uiList = tiles.stream().map(a -> {
            if(a instanceof Tile){
                return new TileUI((Tile) a);
            }
            return new JButton(a.first + " " + a.last);
        }).toList();
        uiList.forEach(c -> add(c));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pack();
    }
}
