package ruvsu.domino.ui;

import ruvsu.domino.model.GameBoard;
import ruvsu.domino.model.GameProcess;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BasicWindow extends JFrame{
    private JComponent ui  = new JPanel(new BorderLayout(5, 5));
    private final Font f = new Font("Monospaced", Font.PLAIN, 45);
    static JTextArea textArea = new JTextArea(4, 10);
    JButton buttonBegin =  new JButton("OK");
    JTable tableGameBoard;

    JLabel labelPl1 = new JLabel("Игрок 1: ");
    JLabel labelPl2 = new JLabel("Игрок 2: ");
    JLabel labelPl3 = new JLabel("Игрок 3: ");

    JTextArea areaPl1 = new JTextArea();
    JTextArea areaPl2 = new JTextArea();
    JTextArea areaPl3 = new JTextArea();

    JButton buttonNextStep =  new JButton("Next Step");
    JButton buttonForcedStep =  new JButton("Forced Next Step");

    JLabel labelMainPl = new JLabel("Ваш набор: ");
    JLabel labelBazar = new JLabel("В колоде осталось: ");
    JTextArea bazarArea = new JTextArea();

    private static int num = 2;

    public static void setNum(int num) {
        BasicWindow.num = num;
    }

    BasicWindow() {
        initUI();
    }

    public void initUI() {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

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

    private JScrollPane createGameBoard(){
        String[] columnNames = new String[GameBoard.SIZE];

        for (int i = 0; i < GameBoard.SIZE; i++){
            columnNames[i] = String.valueOf(i);
        }

        GameProcess game = new GameProcess();
        game.processConsole(num);
        String[][] board;
        board = game.returnField();

        tableGameBoard = new JTable(board, columnNames);
        tableGameBoard.setFont(f);
        tableGameBoard.setRowHeight(45);

        return new JScrollPane(tableGameBoard);
    }

    private Box createTopPanel() {
        Box topPanel = Box.createHorizontalBox();
        topPanel.add(Box.createHorizontalStrut(20));

        Box pl1 = Box.createVerticalBox();
        pl1.setBorder(new EmptyBorder(10, 10, 10, 10));
        pl1.add(Box.createVerticalStrut(20));
        pl1.add(labelPl1);
        areaPl1.setPreferredSize(new Dimension(200, 100));
        pl1.add(areaPl1);
        topPanel.add(pl1);

        Box pl2 = Box.createVerticalBox();
        pl2.setBorder(new EmptyBorder(10, 10, 10, 10));
        pl2.add(Box.createVerticalStrut(20));
        pl2.add(labelPl2);
        areaPl2.setPreferredSize(new Dimension(200, 100));
        pl2.add(areaPl2);
        topPanel.add(pl2);

        Box pl3 = Box.createVerticalBox();
        pl3.setBorder(new EmptyBorder(10, 10, 10, 10));
        pl3.add(Box.createVerticalStrut(20));
        pl3.add(labelPl3);
        areaPl3.setPreferredSize(new Dimension(200, 100));
        pl3.add(areaPl3);
        topPanel.add(pl3);

        Box boxButtons = Box.createVerticalBox();
        boxButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonNextStep.setPreferredSize(new Dimension(200, 70));
        buttonForcedStep.setPreferredSize(new Dimension(200, 70));
        boxButtons.add(buttonNextStep);
        boxButtons.add(buttonForcedStep);
        topPanel.add(boxButtons);

        buttonNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        buttonForcedStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        return topPanel;
    }

    private Box createBottomPanel(){
        Box bottomPanel = Box.createHorizontalBox();
        bottomPanel.add(Box.createHorizontalStrut(20));

        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(labelMainPl);

        bottomPanel.add(mainBox, BorderLayout.WEST);

        Box bazarBox = Box.createVerticalBox();
        bazarBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        bazarBox.add(Box.createVerticalStrut(20));
        bazarBox.add(labelBazar);
        bazarArea.setPreferredSize(new Dimension(200, 100));
        bazarBox.add(bazarArea);

        bottomPanel.add(bazarBox, BorderLayout.EAST);

        return bottomPanel;
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
                BasicWindow o = new BasicWindow();

                JFrame f = new JFrame(o.getClass().getSimpleName());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                f.setContentPane(o.getUI());
                f.pack();
                f.setSize(1600, 1050);
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }

}
