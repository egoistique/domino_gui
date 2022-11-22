package ruvsu.domino.tablemodel;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;


public class GradeRenderer extends JLabel implements TableCellRenderer {

    public GradeRenderer() {
        super.setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column) {
        Student.Grade grade = (Student.Grade) value;
        if (grade == Student.Grade.A) {
            super.setBackground(Color.GREEN);
        } else if (grade == Student.Grade.B) {
            super.setBackground(Color.BLUE);
        } else if (grade == Student.Grade.C) {
            super.setBackground(Color.RED);
        }

        return this;
    }

}
