package ruvsu.domino.ui;

import ruvsu.domino.model.*;
import ruvsu.domino.ui.components.PlayerBox;
import ruvsu.domino.ui.utils.UIDominoUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BasicWindow extends JFrame{
    private JComponent ui  = new JPanel(new BorderLayout(5, 5));
    JPanel mainPanel = new JPanel();
    private final Font f = new Font("Monospaced", Font.PLAIN, 45);
    JTable tableGameBoard;
    JTable tableMain;

    TableModel boardTableModel = new BoardTableModel();
    TableModel mainPlTableModel = new MainPlTableModel();
    String[][] board = new String[25][25];

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
    JTextArea areaMain = new JTextArea();
    JTextArea currentSelectionLabel = new JTextArea("");

    JRadioButton radio1 = new JRadioButton("1");
    JRadioButton radio2 = new JRadioButton("2");
    JRadioButton radio3 = new JRadioButton("3");
    JRadioButton radio0 = new JRadioButton("0");

    UIProcess uiProcess = new UIProcess();

    String[] columnNamesBoard = new String[GameBoard.SIZE];

    List<JTextArea> areas = new ArrayList<>();
    List<JLabel> labels = new ArrayList<>();
    List<JRadioButton> radios = new ArrayList<>();

    int size = 7;
    String[] columnNames1;
    String[][] data;

    private static int num = 2;

    private String code = "";

    public static void setNum(int num) {
        BasicWindow.num = num;
    }

    BasicWindow() {
        initUI();
    }

    public void initUI() {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        uiProcess.beginGamePr(num);

        initLists();

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

        ui.add(new JScrollPane(beginGame()));

        mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));

        initTableMainPl();

        buttonNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.add(new JScrollPane(UIDominoUtils.nextStep(uiProcess, code, f, radios, columnNamesBoard, areas, bazarArea, tableGameBoard)));
                mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));
            }
        });

        tableMain.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){
                int row = tableMain.rowAtPoint(e.getPoint());
                int col = tableMain.columnAtPoint(e.getPoint());
                String s = tableMain.getValueAt(row,col).toString();
                currentSelectionLabel.setText(s);

                code = s;
            }
        });

    }

    private void initLists(){
        areas.add(areaMain);
        areas.add(areaPl1);
        areas.add(areaPl2);
        areas.add(areaPl3);

        labels.add(labelMainPl);
        labels.add(labelPl1);
        labels.add(labelPl2);
        labels.add(labelPl3);

        ButtonGroup group = new ButtonGroup();
        group.add(radio1);
        group.add(radio2);
        group.add(radio3);
        group.add(radio0);
        radio0.setSelected(true);

        radios.add(radio0);
        radios.add(radio1);
        radios.add(radio2);
        radios.add(radio3);
    }

    public class BoardTableModel extends DefaultTableModel {
        BoardTableModel() {
            super(board, columnNamesBoard);
            System.out.println("Inside myTableModel");
        }
        public boolean isCellEditable(int row,int cols){
            return false;
        }

        public void refresh() {
            //board = new String[25][25];
            fireTableDataChanged();
        }

    }

    private void initTableMainPl() {
        if (num == 2) {
            size = 5;
        }
        columnNames1 = new String[size];
        data = new String[1][size];
        for (int i = 0; i < size; i++) {
            columnNames1[i] = String.valueOf(i);
        }
    }

    public class MainPlTableModel extends DefaultTableModel {
        MainPlTableModel( ) {
            super(data, columnNames1);
            System.out.println("Inside mainPlTableModel");
        }
        public boolean isCellEditable(int row,int cols){
            return false;
        }

    }

    private JTable beginGame(){
        //сделать первый шаг, вывести актуальное состояние гейм борда и обновить текст филд ходящего игрока
        Tile firstTile = uiProcess.getPl().makeAFirstMove();
        uiProcess.setActiveTiles(uiProcess.board.putFirstTile(firstTile, uiProcess.getActiveTiles()));

        tableGameBoard = new JTable(uiProcess.board.getField(), columnNamesBoard);
        tableGameBoard.setFont(f);
        tableGameBoard.setRowHeight(45);

        //включить радио кнопку у того, кто сделал первый ход
        int numOfCurrentRadio = UIDominoUtils.defineFirstMover(uiProcess.getPlayers());
        radios.get(numOfCurrentRadio).setSelected(true);

        //вывести наборы плиток игроков
        UIDominoUtils.outPacks(f, uiProcess, areas);
        UIDominoUtils.outBazar(f, bazarArea, uiProcess);

        return tableGameBoard;
    }

    private JTable mainPlayersTilesToTable(){
        String[][] tiles = uiProcess.getPlayers().get(0).packToString(new GameBoard());
        String[] columnNames1 = new String[uiProcess.getPlayers().get(0).getPackOfTiles().size()];
        for (int i = 0; i < uiProcess.getPlayers().get(0).getPackOfTiles().size(); i++){
            columnNames1[i] = String.valueOf(i);
        }

        tableMain = new JTable(tiles, columnNames1);
        tableMain.setFont(f);
        tableMain.setRowHeight(45);
        return tableMain;
    }

    private JScrollPane createGameBoard(){
        for (int i = 0; i < GameBoard.SIZE; i++){
            columnNamesBoard[i] = String.valueOf(i);
        }
        tableGameBoard = new JTable();
        tableGameBoard.setRowHeight(45);
        tableGameBoard.setFont(f);
        tableGameBoard.setModel(boardTableModel);

        return new JScrollPane(tableGameBoard);
    }

    private Box createTopPanel() {
        Box topPanel = Box.createHorizontalBox();
        topPanel.add(Box.createHorizontalStrut(20));

        for(int i = 1; i < uiProcess.getPlayers().size(); i++){
            topPanel.add(PlayerBox.createPlBox(i, radios, labels, areas));
        }

        Box boxButtons = Box.createVerticalBox();
        boxButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonNextStep.setPreferredSize(new Dimension(200, 70));
        buttonForcedStep.setPreferredSize(new Dimension(200, 70));
        boxButtons.add(buttonNextStep);
        boxButtons.add(buttonForcedStep);
        topPanel.add(boxButtons);

        return topPanel;
    }

    private Box createBottomPanel(){
        Box bottomPanel = Box.createHorizontalBox();
        bottomPanel.add(Box.createHorizontalStrut(20));

        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(radio0);
        mainBox.add(labelMainPl);
        mainBox.add(createMainPlayerPanel());
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

    private JPanel createMainPlayerPanel(){
        mainPanel.setLayout(new BorderLayout());

        tableMain = new JTable();
        tableMain.setFont(f);
        tableMain.setRowHeight(45);
        tableMain.setModel(mainPlTableModel);

        JScrollPane scrollPane = new JScrollPane(tableMain);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel selLabel = new JLabel("Selected:");
        bottomPanel1.add(selLabel);


        currentSelectionLabel.setPreferredSize(new Dimension(200, 50));
        currentSelectionLabel.setFont(f);
        currentSelectionLabel.setAutoscrolls(true);
        bottomPanel1.add(currentSelectionLabel);

        mainPanel.add(bottomPanel1, BorderLayout.SOUTH);

        mainPanel.setPreferredSize(new Dimension(600, 200));

        return mainPanel;
    }

    public JComponent getUI() {
        return ui;
    }

    public void run(){
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
