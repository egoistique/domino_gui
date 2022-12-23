package client.ui;

import client.network.NetworkGameProcess;
import client.ui.components.BoardTM;
import client.ui.components.MainTM;
import client.ui.components.PlayerBox;
import client.ui.utils.UIDominoUtils;
import model.AbstractGame;
import model.IGameProcess;
import model.LocalGameProcess;
import model.Player;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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

    private final JButton buttonBegin =  new JButton("Begin");
    private final JButton buttonNextStep =  new JButton("Next Step");
    private final JButton buttonTakeFromBazaar =  new JButton("Взять из базара");

    private final JLabel labelMainPl = new JLabel("Ваш набор: ");
    private final JLabel labelBazaar = new JLabel("В колоде осталось: ");
    private final JTextArea bazaarArea = new JTextArea();
    private final JTextArea currentSelectionLabel = new JTextArea("");

    private IGameProcess process = new LocalGameProcess(new Player());

    private final List<JTextArea> areas = new ArrayList<>();
    private final List<JLabel> labels = new ArrayList<>();
    private final List<JRadioButton> radios = new ArrayList<>();

    private int size = 7;

    private final int num;
    private final int view; //2 - локально; 3 - удаленно

    private String code = "";

    BasicWindow(int num1, int view1) throws IOException, ClassNotFoundException {
        num = num1;
        view = view1;
        initUI();
    }

    public void initUI() throws IOException, ClassNotFoundException {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        if (view == 3){
            process = new NetworkGameProcess(new Player(), "localhost", 9999);
        }

        process.beginGamePr(view, num);

        initLists(num);

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

        UIDominoUtils.beginGame((AbstractGame) process, f, areas, bazaarArea);

        UIDominoUtils.mainPlayersTilesToTable((AbstractGame) process, mainPlTableModel);

        mainPanel.add(new JScrollPane((tableMain)));

        initTableMainPl();

        buttonNextStep.addActionListener(e -> {
            try {
                UIDominoUtils.nextStep(boardTableModel, (AbstractGame) process, code, f, radios, areas, bazaarArea);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            boardTableModel.fireTableDataChanged();
            UIDominoUtils.mainPlayersTilesToTable((AbstractGame) process, mainPlTableModel);
            mainPlTableModel.fireTableDataChanged();
        });

        buttonBegin.addActionListener(e -> {
            try {
                UIDominoUtils.firstStep(boardTableModel, (AbstractGame) process, f, radios, areas, bazaarArea);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            boardTableModel.fireTableDataChanged();
            UIDominoUtils.mainPlayersTilesToTable((AbstractGame) process, mainPlTableModel);
            mainPlTableModel.fireTableDataChanged();
        });

        buttonTakeFromBazaar.addActionListener(e -> {
            try {
                UIDominoUtils.takeFromBazaar((AbstractGame) process);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
            UIDominoUtils.mainPlayersTilesToTable((AbstractGame) process, mainPlTableModel);
            mainPlTableModel.fireTableDataChanged();
            UIDominoUtils.outBazar(f, bazaarArea, (AbstractGame) process);
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
        ButtonGroup buttonGroup = new ButtonGroup();
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

        System.out.println();
        for(int i = 1; i < process.getPlayers().size(); i++){
            topPanel.add(PlayerBox.createPlBox(i, radios, labels, areas));
        }

        Box boxLocalButtons = Box.createVerticalBox();
        boxLocalButtons.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonNextStep.setPreferredSize(new Dimension(200, 70));
        buttonBegin.setPreferredSize(new Dimension(200, 70));
        buttonTakeFromBazaar.setPreferredSize(new Dimension(200, 70));
        boxLocalButtons.add(buttonBegin);
        boxLocalButtons.add(buttonNextStep);
        boxLocalButtons.add(buttonTakeFromBazaar);
        topPanel.add(boxLocalButtons);

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

        Box bazaarBox = Box.createVerticalBox();
        bazaarBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        bazaarBox.add(Box.createVerticalStrut(20));
        bazaarBox.add(labelBazaar);
        bazaarArea.setPreferredSize(new Dimension(200, 100));
        bazaarBox.add(bazaarArea);

        bottomPanel.add(bazaarBox, BorderLayout.EAST);

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

    public void run(BasicWindow basicWindow){
        Runnable r = () -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception useDefault) {
                useDefault.printStackTrace();
            }

            JFrame f = new JFrame(basicWindow.getClass().getSimpleName());
            f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            f.setLocationByPlatform(true);

            f.setContentPane(basicWindow.getUI());
            f.pack();
            f.setSize(1600, 1050);
            f.setVisible(true);
        };
        SwingUtilities.invokeLater(r);
    }

}
