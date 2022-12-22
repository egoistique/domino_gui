package client.ui.components;

import javax.swing.table.AbstractTableModel;

public class BoardTM extends AbstractTableModel {
    private String[][] board;
    public BoardTM(String[][] board) {
        this.board = board;
    }

    public void setBoard(String[][] board) {
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
        return board[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return "";
    }
}
