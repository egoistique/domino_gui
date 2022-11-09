package ruvsu.domino.ui;
import ruvsu.domino.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class BasicWindow1 extends JFrame{
    private JComponent ui  = new JPanel(new BorderLayout(5, 5));
    JPanel mainPanel = new JPanel();
    private final Font f = new Font("Monospaced", Font.PLAIN, 45);
    JTable tableGameBoard;
    JTable tableMain;

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
    List<JRadioButton> radios = new ArrayList<>();

    private static int num = 2;

    public static void setNum(int num) {
        BasicWindow1.num = num;
    }

    BasicWindow1() {
        initUI();
    }

    public void initUI() {
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        Box topPanel = createTopPanel();
        ui.add(topPanel, BorderLayout.NORTH);

        Box bottomPanel = createBottomPanel();
        ui.add(bottomPanel, BorderLayout.SOUTH);

        ui.add(createGameBoard());

        areas.add(areaMain);
        areas.add(areaPl1);
        areas.add(areaPl2);
        areas.add(areaPl3);

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

        ui.add(new JScrollPane(beginGame()));

        mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));

        buttonNextStep.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.add(new JScrollPane(nextStep()));
                mainPanel.add(new JScrollPane((mainPlayersTilesToTable())));
            }
        });

        tableMain.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseClicked(java.awt.event.MouseEvent e){
                int row = tableMain.rowAtPoint(e.getPoint());
                int col = tableMain.columnAtPoint(e.getPoint());
                String s = tableMain.getValueAt(row,col).toString();

                currentSelectionLabel.setText(s);
            }
        });

    }

    private JTable beginGame(){
        uiProcess.beginGamePr(num);
        //сделать первый шаг, вывести актуальное состояние гейм борда и обновить текст филд ходящего игрока
        Tile firstTile = uiProcess.pl.makeAFirstMove();
        uiProcess.activeTiles = uiProcess.board.putFirstTile(firstTile, uiProcess.activeTiles);

        tableGameBoard = new JTable(uiProcess.board.getField(), columnNamesBoard);
        tableGameBoard.setFont(f);
        tableGameBoard.setRowHeight(45);

        //включить радио кнопку у того, кто сделал первый ход
        int numOfCurrentRadio = defineFirstMover(uiProcess.players);
        radios.get(numOfCurrentRadio).setSelected(true);

        //вывести наборы плиток игроков
        outPacks(uiProcess.players);
        outBazar();

        return tableGameBoard;
    }

    private JTable mainPlayersTilesToTable(){
        String[][] tiles = uiProcess.players.get(0).packToString(new GameBoard());
        String[] columnNames1 = new String[uiProcess.players.get(0).getPackOfTiles().size()];
        for (int i = 0; i < uiProcess.players.get(0).getPackOfTiles().size(); i++){
            columnNames1[i] = String.valueOf(i);
        }

        tableMain = new JTable(tiles, columnNames1);
        tableMain.setFont(f);
        tableMain.setRowHeight(45);
        return tableMain;
    }

    private int defineFirstMover(List<Player> players){
        int minSize = players.get(0).getPackOfTiles().size();
        int numOfPl = 0;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getPackOfTiles().size() < minSize){
                minSize = players.get(i).getPackOfTiles().size();
                numOfPl = i;
            }
        }
        return numOfPl;
    }

    private JTable nextStep(){
        //шаг игры
        if(!uiProcess.gameOver && uiProcess.checkFor < uiProcess.players.size()) {
            //сделать ход

            Player pl = uiProcess.gameStep();

            //включить нужную радио кнопку
            int numOfRadio = findCurrPl(uiProcess.players, pl);
            radios.get(numOfRadio).setSelected(true);

            //вывести состояние гейм борда
            tableGameBoard = new JTable(uiProcess.board.getField(), columnNamesBoard);
            tableGameBoard.setFont(f);
            tableGameBoard.setRowHeight(45);

            //вывести в каждый текст филд текущие пак оф тайлсы
            outPacks(uiProcess.players);
            outBazar();

            //проверить на гейм овер
            uiProcess.gameOverCheck(uiProcess.players);
            return tableGameBoard;
        } else{
            gameOver();
        }
        return tableGameBoard;
    }

    private void gameOver(){
        GameOverWindow gameOver = new GameOverWindow();
        String points = "Итоговое число очков: " + uiProcess.outPoints();
        String winner = "Победил игрок " + uiProcess.defineWinner();
        gameOver.setPoints(points);
        gameOver.setWinner(winner);
        gameOver.setVisible(true);
    }

    private int findCurrPl(List<Player> players, Player pl){
        int num = 0;
        for(int i = 0; i < players.size(); i++){
            if (listEqualsIgnoreOrder(players.get(i).getPackOfTiles(), pl.getPackOfTiles())){
                num = i;
            }
        }
        return num;
    }

    public <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    private void outPacks(List<Player> players){
        for (int i = 0; i < players.size(); i++){
            String s = uiProcess.packTilesToString(players.get(i), uiProcess.board);
            areas.get(i).setFont(f);
            areas.get(i).setText(s);
        }
    }

    private void outBazar(){
        bazarArea.setFont(f);
        bazarArea.setText(uiProcess.bazarToString());
    }

    private JScrollPane createGameBoard(){
        for (int i = 0; i < GameBoard.SIZE; i++){
            columnNamesBoard[i] = String.valueOf(i);
        }
        String[][] board = new String[25][25];
        tableGameBoard = new JTable(board, columnNamesBoard);
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
        pl1.add(radio1);
        pl1.add(labelPl1);
        areaPl1.setPreferredSize(new Dimension(200, 100));
        pl1.add(areaPl1);
        topPanel.add(pl1);

        Box pl2 = Box.createVerticalBox();
        pl2.setBorder(new EmptyBorder(10, 10, 10, 10));
        pl2.add(Box.createVerticalStrut(20));
        pl2.add(radio2);
        pl2.add(labelPl2);
        areaPl2.setPreferredSize(new Dimension(200, 100));
        pl2.add(areaPl2);
        topPanel.add(pl2);

        Box pl3 = Box.createVerticalBox();
        pl3.setBorder(new EmptyBorder(10, 10, 10, 10));
        pl3.add(Box.createVerticalStrut(20));
        pl3.add(radio3);
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
        int size = 7;
        if (num == 2){
            size = 5;
        }

        String[] columnNames1 = new String[size];

        for (int i = 0; i < size; i++){
            columnNames1[i] = String.valueOf(i);
        }

        String[][] data = new String[1][size];

        mainPanel.setLayout(new BorderLayout());

        tableMain = new JTable(data, columnNames1);
        tableMain.setFont(f);
        tableMain.setRowHeight(45);

        JScrollPane scrollPane = new JScrollPane(tableMain);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel1 = new JPanel();
        bottomPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel selLabel = new JLabel("Selected:");
        bottomPanel1.add(selLabel);


        currentSelectionLabel.setPreferredSize(new Dimension(200, 100));
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
                BasicWindow1 o = new BasicWindow1();

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

