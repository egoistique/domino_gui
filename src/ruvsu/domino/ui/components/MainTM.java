package ruvsu.domino.ui.components;

import javax.swing.table.AbstractTableModel;

public class MainTM extends AbstractTableModel {
    String[][] mainPlayrTiles;
    public MainTM(String[][] board) {
        super();
        this.mainPlayrTiles = board;
    }

    @Override
    public int getRowCount() {
        return mainPlayrTiles.length;
    }

    @Override
    public int getColumnCount() {
        return mainPlayrTiles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}

