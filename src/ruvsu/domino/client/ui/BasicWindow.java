package ruvsu.domino.client.ui;

import ruvsu.domino.client.network.NetworkGameProcess;
import ruvsu.domino.model.*;
import ruvsu.domino.client.ui.components.BoardTM;
import ruvsu.domino.client.ui.components.MainTM;
import ruvsu.domino.client.ui.components.PlayerBox;
import ruvsu.domino.client.ui.utils.UIDominoUtils;

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

    private final JButton buttonBeginLocal =  new JButton("Begin local");
    private final JButton buttonNextLocalStep =  new JButton("Next local Step");
    private final JButton buttonTakeFromBazarLocal =  new JButton("Взять из базара");

    private final JButton buttonBeginRemote =  new JButton("Begin Remote");
    private final JButton buttonNextRemoteStep =  new JButton("Next Remote Step");
    private final JButton buttonTakeFromBazarRemote =  new JButton("Взять из базара");

    private final JLabel labelMainPl = new JLabel("Ваш набор: ");
    private final JLabel labelBazar = new JLabel("В колоде осталось: ");
    private final JTextArea bazarArea = new JTextArea();
    private final JTextArea currentSelectionLabel = new JTextArea("");

    private IGameProcess process = new LocalGameProcess(new Player());

    //private final IGameProcess networkProcess = new NetworkGameProcess(new Player());

    private final List<JTextArea> areas = new ArrayList<>();
    private final List<JLabel> labels = new ArrayList<>();
    private final List<JRadioButton> radios = new ArrayList<>();

    public JComponent getUi() {
        return ui;
    }

    private int size = 7;

    private static int num = 2;
    private static int view = 1; //1 - локально; 2 - удаленно

    private String code = "";
    private ButtonGroup buttonGroup;

    public void setNum(int num) {
        BasicWindow.num = num;
    }

    public void setView(int view) {
        BasicWindow.view = view;
    }

    BasicWindow() {
        initUI();
    }

    public void initUI() {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        if (view == 2){
            process = new NetworkGameProcess(new Player());
        }

        process.beginGamePr(num);

        initLists(num);

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

        UIDominoUtils.beginGame((LocalGameProcess) process, f, areas, bazarArea);

        UIDominoUtils.mainPlayersTilesToTable((LocalGameProcess) process, mainPlTableModel);

        mainPanel.add(new JScrollPane((tableMain)));

        initTableMainPl();

        buttonNextLocalStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.nextStep(boardTableModel, (LocalGameProcess) process, code, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
                UIDominoUtils.mainPlayersTilesToTable((LocalGameProcess) process, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
            }
        });

        buttonBeginLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.firstStep(boardTableModel, (LocalGameProcess) process, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
                UIDominoUtils.mainPlayersTilesToTable((LocalGameProcess) process, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
            }
        });

        buttonBeginRemote.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.firstStep(boardTableModel, (LocalGameProcess) process, f, radios, areas, bazarArea);
                boardTableModel.fireTableDataChanged();
                UIDominoUtils.mainPlayersTilesToTable((LocalGameProcess) process, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
            }
        });

        buttonTakeFromBazarLocal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                UIDominoUtils.takeFromBazar((LocalGameProcess) process, mainPlTableModel);
                UIDominoUtils.mainPlayersTilesToTable((LocalGameProcess) process, mainPlTableModel);
                mainPlTableModel.fireTableDataChanged();
                UIDominoUtils.outBazar(f, bazarArea, (LocalGameProcess) process);
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
        buttonGroup = new ButtonGroup();
        for (int i = 0; i < num; i++){
            areas.add(new JTextArea());
            labels.add(new JLabel("Игрок " + i));
            JRadioButton rb = new JRadioButton();
            buttonGroup.add(rb);
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

        return new JScrollPane(tableGameBoard);
    }

    private Box createTopPanel() {
        Box topPanel = Box.createHorizontalBox();
        topPanel.add(Box.createHorizontalStrut(20));

        for(int i = 1; i < process.getPlayers().size(); i++){
            topPanel.add(PlayerBox.createPlBox(i, radios, labels, areas));
        }

        Box boxLocalButtons = Box.createVerticalBox();
        boxLocalButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonNextLocalStep.setPreferredSize(new Dimension(200, 70));
        buttonBeginLocal.setPreferredSize(new Dimension(200, 70));
        buttonTakeFromBazarLocal.setPreferredSize(new Dimension(200, 70));
        boxLocalButtons.add(buttonBeginLocal);
        boxLocalButtons.add(buttonNextLocalStep);
        boxLocalButtons.add(buttonTakeFromBazarLocal);
        topPanel.add(boxLocalButtons);

//        Box boxRemoteButtons = Box.createVerticalBox();
//        boxRemoteButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
//        buttonNextRemoteStep.setPreferredSize(new Dimension(200, 70));
//        buttonBeginRemote.setPreferredSize(new Dimension(200, 70));
//        buttonTakeFromBazarRemote.setPreferredSize(new Dimension(200, 70));
//        boxRemoteButtons.add(buttonNextRemoteStep);
//        boxRemoteButtons.add(buttonBeginRemote);
//        boxRemoteButtons.add(buttonTakeFromBazarRemote);
//        topPanel.add(boxRemoteButtons);

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
