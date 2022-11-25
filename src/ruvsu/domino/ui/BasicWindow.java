package ruvsu.domino.ui;

import ruvsu.domino.model.*;
import ruvsu.domino.ui.components.BoardTM;
import ruvsu.domino.ui.components.MainTM;
import ruvsu.domino.ui.components.PlayerBox;
import ruvsu.domino.ui.utils.UIDominoUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BasicWindow extends JFrame{
    private final JComponent ui  = new JPanel(new BorderLayout(5, 5));
    private final JPanel mainPanel = new JPanel();
    private final Font f = new Font("Monospaced", Font.PLAIN, 45);

    private final String[][] board = new String[25][25];
    private String[][] data = new String[7][2];

    private final BoardTM boardTableModel = new BoardTM(board);
    private final MainTM mainPlTableModel = new MainTM(data);

    private final JTable tableGameBoard = new JTable(boardTableModel);
    private final JTable tableMain = new JTable(mainPlTableModel);

    private final JButton buttonBeginStep =  new JButton("Begin");
    private final JButton buttonNextStep =  new JButton("Next Step");
    private final JButton buttonTakeFromBazar =  new JButton("Взять из базара");

    private final JLabel labelMainPl = new JLabel("Ваш набор: ");
    private final JLabel labelBazar = new JLabel("В колоде осталось: ");
    private final JTextArea bazarArea = new JTextArea();
    private final JTextArea currentSelectionLabel = new JTextArea("");

    private final GameProcess uiProcess = new GameProcess();

    private final List<JTextArea> areas = new ArrayList<>();
    private final List<JLabel> labels = new ArrayList<>();
    private final List<JRadioButton> radios = new ArrayList<>();

    private int size = 7;

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

        UIDominoUtils.beginGame(uiProcess, f, areas, bazarArea);

        UIDominoUtils.mainPlayersTilesToTable(uiProcess, mainPlTableModel);

        mainPanel.add(new JScrollPane((tableMain)));

        initTableMainPl();

        buttonNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.nextStep(boardTableModel, uiProcess, code, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
                UIDominoUtils.mainPlayersTilesToTable(uiProcess, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
            }
        });

        buttonBeginStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.firstStep(boardTableModel, uiProcess, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
                UIDominoUtils.mainPlayersTilesToTable(uiProcess, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
            }
        });

        buttonTakeFromBazar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.takeFromBazar(uiProcess, mainPlTableModel);
                UIDominoUtils.mainPlayersTilesToTable(uiProcess, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
                UIDominoUtils.outBazar(f, bazarArea, uiProcess);
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
        data = new String[1][size];
    }

    private JScrollPane createGameBoard(){
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
        buttonTakeFromBazar.setPreferredSize(new Dimension(200, 70));
        boxButtons.add(buttonBeginStep);
        boxButtons.add(buttonNextStep);
        boxButtons.add(buttonTakeFromBazar);
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
