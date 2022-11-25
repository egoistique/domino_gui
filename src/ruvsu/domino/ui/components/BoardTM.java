package ruvsu.domino.ui.components;

import javax.swing.table.AbstractTableModel;

public class BoardTM extends AbstractTableModel {
    String[][] board;
    public BoardTM(String[][] board) {
        super();
        this.board = board;
    }

    @Override
    public int getRowCount() {
        return board.length;
    }

    @Override
    public int getColumnCount() {
        return board.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
