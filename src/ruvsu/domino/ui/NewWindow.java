package ruvsu.domino.ui;

import ruvsu.domino.model.GameProcess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewWindow extends JFrame{
    private JComponent ui;
    private final Font f = new Font("Monospaced", Font.PLAIN, 60);
    public static final int DOMINO_TILE_START = 127024;
    public static final int DOMINO_TILE_END = 127123;
    static JTextArea textArea = new JTextArea(4, 10);
 //   JButton buttonBegin =  new JButton("OK");
    JTable table;


    private static int num = 2;

    public static void setNum(int num) {
        NewWindow.num = num;
    }

    NewWindow() {
        initUI();
    }

    public void initUI() {

        ui = new JPanel(new BorderLayout(5, 5));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

//        buttonBegin.setBorder(new EmptyBorder(4, 4, 4,4));
//        buttonBegin.setSize(new Dimension(100, 50));
//        ui.add(buttonBegin, BorderLayout.NORTH);

        //textArea.setText(String.valueOf(num));
        //textArea.setFont(f);
        //ui.add(textArea, BorderLayout.LINE_END);

        String[] columnNames = new String[20];

        for (int i = 0; i < 20; i++){
            columnNames[i] = String.valueOf(i);
        }

        GameProcess game = new GameProcess();
        game.processConsole(num);
        String[][] board;
        board = game.returnField();

        table = new JTable(board, columnNames);
        table.setFont(f);
        table.setRowHeight(60);

        ui.add(new JScrollPane(table));

//        buttonBegin.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                NumPlWindow app = new NumPlWindow();
//                app.setVisible(true);
//
//                GameProcess game = new GameProcess();
//                game.processConsole(num);
//                String[][] board;
//                board = game.returnField();
//                table = new JTable(board, columnNames);
//
//                ui.add(new JScrollPane(table));
//            }
//        });
    }

    public JComponent getUI() {
        return ui;
    }


    public void run(int num){
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception useDefault) {
                }
                NewWindow o = new NewWindow();

                JFrame f = new JFrame(o.getClass().getSimpleName());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                f.setContentPane(o.getUI());
                f.pack();
                f.setSize(1400, 1200);
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

}
