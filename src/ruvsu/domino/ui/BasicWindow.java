package ruvsu.domino.ui;

import ruvsu.domino.model.*;
import ruvsu.domino.ui.components.BoardTM;
import ruvsu.domino.ui.components.MainTM;
import ruvsu.domino.ui.components.PlayerBox;
import ruvsu.domino.ui.utils.UIDominoUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BasicWindow extends JFrame{
    private JComponent ui  = new JPanel(new BorderLayout(5, 5));
    private JPanel mainPanel = new JPanel();
    private final Font f = new Font("Monospaced", Font.PLAIN, 45);

    String[][] board = new String[25][25];
    String[][] data = new String[2][7];

    BoardTM boardTableModel = new BoardTM(board);
    MainTM mainPlTableModel = new MainTM(data);

    JTable tableGameBoard = new JTable(boardTableModel);
    JTable tableMain = new JTable(mainPlTableModel);

    JButton buttonBeginStep =  new JButton("Begin");
    JButton buttonNextStep =  new JButton("Next Step");

    JLabel labelMainPl = new JLabel("Ваш набор: ");
    JLabel labelBazar = new JLabel("В колоде осталось: ");
    JTextArea bazarArea = new JTextArea();
    JTextArea currentSelectionLabel = new JTextArea("");

    GameProcess uiProcess = new GameProcess();

    String[] columnNamesBoard = new String[GameBoard.SIZE];

    List<JTextArea> areas = new ArrayList<>();
    List<JLabel> labels = new ArrayList<>();
    List<JRadioButton> radios = new ArrayList<>();

    int size = 7;
    String[] columnNames1;


    private static int num = 2;

    private String code = "";

    public void setNum(int num) {
        BasicWindow.num = num;
    }

    BasicWindow() {
        initUI();
    }

    public void initUI() {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        uiProcess.beginGamePr(num);

        initLists(num);

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

        ui.add(new JScrollPane(UIDominoUtils.beginGame(uiProcess, columnNamesBoard, f, areas, bazarArea)));

        boardTableModel.fireTableDataChanged();

        mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));

        initTableMainPl();

        buttonNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.nextStep(boardTableModel, uiProcess, code, f, radios, columnNamesBoard, areas, bazarArea, tableGameBoard);
                boardTableModel.fireTableDataChanged();
                mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));
            }
        });

        buttonBeginStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.firstStep(boardTableModel, uiProcess, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
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

        tableGameBoard.getModel().addTableModelListener(new TableModelListener() {
            public void tableChanged(TableModelEvent e) {
                System.out.println("update");
            }
        });
    }

    private void initLists(int num){
        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < num; i++){
            areas.add(new JTextArea());
            labels.add(new JLabel("Игрок " + i));
            JRadioButton rb = new JRadioButton();
            group.add(rb);
            radios.add(rb);
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
        buttonBeginStep.setPreferredSize(new Dimension(200, 70));
        boxButtons.add(buttonBeginStep);
        boxButtons.add(buttonNextStep);
        topPanel.add(boxButtons);

        return topPanel;
    }

    private Box createBottomPanel(){
        Box bottomPanel = Box.createHorizontalBox();
        bottomPanel.add(Box.createHorizontalStrut(20));

        Box mainBox = Box.createVerticalBox();
        mainBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainBox.add(Box.createVerticalStrut(20));
        mainBox.add(radios.get(0));
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
