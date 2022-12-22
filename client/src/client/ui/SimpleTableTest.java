package client.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;

class SimpleTableTest extends JFrame{

    private JPanel topPanel ;
    private JTable table;
    private JScrollPane scrollPane;
    private String[] columnNames= new String[3];
    private String[][] dataValues=new String[3][3] ;

    public SimpleTableTest()    {
        setSize(300,300);
        topPanel= new JPanel();
        topPanel.setLayout(new BorderLayout());
        getContentPane().add(topPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        columnNames=new String[] {"Column 1" , "Column 2" , "Column 3"};
        dataValues = new String[][]{
                {"1", "2", "3"}};

        TableModel model = new myTableModel();

        table =new JTable( );

        table.setRowHeight(50);

        table.setModel(model);

        scrollPane=new JScrollPane(table);

        topPanel.add(scrollPane,BorderLayout.CENTER);

        table.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){
                int row=table.rowAtPoint(e.getPoint());

                int col= table.columnAtPoint(e.getPoint());

                String s = table.getValueAt(row,col).toString();

                JOptionPane.showMessageDialog(null," Value in the cell clicked :"+ " " + s);

                System.out.println(" Value in the cell clicked :"+ " " +table.getValueAt(row,col).toString());
            }
        });
    }

    public class myTableModel extends DefaultTableModel    {
        myTableModel( ) {
            super(dataValues,columnNames);
            System.out.println("Inside myTableModel");
        }
        public boolean isCellEditable(int row,int cols){
            return false;
        }

    }

    public static void main(String args[]){
        SimpleTableTest mainFrame=new SimpleTableTest();
        mainFrame.setVisible(true);
    }

}