package ruvsu.domino.ui.components;

import javax.swing.table.AbstractTableModel;

public class MainTM extends AbstractTableModel {
    private String[][] mainPlayerTiles;
    public MainTM(String[][] mainPlayerTiles) {
        this.mainPlayerTiles = mainPlayerTiles;
    }

    public void setMainPlayerTiles(String[][] mainPlayerTiles) {
        this.mainPlayerTiles = mainPlayerTiles;
    }

    @Override
    public int getRowCount() {
        return mainPlayerTiles.length;
    }

    @Override
    public int getColumnCount() {
        return mainPlayerTiles.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return mainPlayerTiles[rowIndex][columnIndex];
    }
}

