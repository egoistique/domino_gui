package ruvsu.domino.client.ui.utils;

import ruvsu.domino.client.ui.components.BoardTM;
import ruvsu.domino.client.ui.components.MainTM;
import ruvsu.domino.model.*;
import ruvsu.domino.client.ui.GameOverWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * класс, хранящий в себе методы, нужные для представления игрового процесса в оконном интерфейсе
 */
public class UIDominoUtils {
    //привести packTiles к строке
    public static String packTilesToString(Player pl, GameBoard field) {
        StringBuilder sb = new StringBuilder();
        int code;
        for (int i = 0; i < pl.getPackOfTiles().size(); i++) {
            code = field.getCode(field.tileImages, pl.getPackOfTiles().get(i));
            String s = new String(Character.toChars(code));
            sb.append(s);
        }
        return sb.toString();
    }

    //привести базар к строке
    public static String bazarToString(Heap heap) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heap.getBazarSize(); i++) {
            String s = new String(Character.toChars(127074));
            sb.append(s);
        }
        return sb.toString();
    }

    //вывести очки в окно
    public static String outPoints(List<Player> players) {
        String s = "";
        int[] sums = countPlayersPoints(players);
        for (int i = 0; i < players.size(); i++) {
            s += ("\nИгрок " + i + " набрал " + sums[i] + " очков \n");
        }
        return s;
    }

    //определить победителя
    public static String defineWinner(List<Player> players) {
        int[] sums = countPlayersPoints(players);
        int max = Arrays.stream(sums).max().getAsInt();
        int num = 0;
        for (int i = 0; i < sums.length; i++) {
            if (sums[i] == max) {
                num = i;
            }
        }
        return String.valueOf(num);
    }

    //посчитать суммы очков всеъ игроков
    public static int countTotalPoints(Player p) {
        int sum = 0;
        for (Tile t : p.getPackOfTiles()) {
            sum += t.first + t.last;
            if (t.last == t.first && t.last == 0) {
                sum += 10;
            }
        }
        return sum;
    }

    //посчитать суммы очков всеъ игроков
    private static int[] countPlayersPoints(List<Player> players) {
        int[] sums = new int[players.size()];
        int fullSum = 0;
        for (int i = 0; i < players.size(); i++) {
            sums[i] = countTotalPoints(players.get(i));
            fullSum += countTotalPoints(players.get(i));
        }

        for (int i = 0; i < players.size(); i++) {
            if (sums[i] == 0) {
                sums[i] = fullSum;
            }
        }
        return sums;
    }

    //проверка, одинаковы ли листы по содержанию
    public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

    //сделать 1 шаг
    public static BoardTM firstStep(BoardTM boardTM, AbstractGame uiProcess, Font f, List<JRadioButton> radios, List<JTextArea> areas, JTextArea bazarArea) throws IOException, ClassNotFoundException {
        //сделать первый шаг, вывести актуальное состояние гейм борда и обновить текст филд ходящего игрока
        uiProcess.firstStep();

        //включить радио кнопку у того, кто сделал первый ход
        int numOfCurrentRadio = defineFirstMover(uiProcess.getPlayers());
        if (numOfCurrentRadio == uiProcess.getPlayers().size() - 1) {
            numOfCurrentRadio = 0;
        } else numOfCurrentRadio++;
        radios.get(numOfCurrentRadio).setSelected(true);

        //вывести наборы плиток игроков
        outPacks(f, uiProcess, areas);
        outBazar(f, bazarArea, uiProcess);

        boardTM.setBoard(uiProcess.board.getField());
        boardTM.fireTableDataChanged();

        return boardTM;
    }

    //вывести начальное состояние игроков
    public static void beginGame(AbstractGame uiProcess, Font f, List<JTextArea> areas, JTextArea bazarArea){
        //вывести наборы плиток игроков
        UIDominoUtils.outPacks(f, uiProcess, areas);
        UIDominoUtils.outBazar(f, bazarArea, uiProcess);
    }

    //определить 1 игрока
    public static int defineFirstMover(List<Player> players) {
        int minSize = players.get(0).getPackOfTiles().size();
        int numOfPl = 0;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getPackOfTiles().size() < minSize) {
                minSize = players.get(i).getPackOfTiles().size();
                numOfPl = i;
            }
        }
        return numOfPl;
    }

    //вывести наборы игроков в соответствующие поля
    public static void outPacks(Font f, AbstractGame uiProcess, List<JTextArea> areas) {
        for (int i = 0; i < uiProcess.getPlayers().size(); i++) {
            String s = UIDominoUtils.packTilesToString(uiProcess.getPlayers().get(i), uiProcess.board);
            areas.get(i).setFont(f);
            areas.get(i).setText(s);
        }
    }

    //вывести базар
    public static void outBazar(Font f, JTextArea bazarArea, AbstractGame uiProcess) {
        bazarArea.setFont(f);
        bazarArea.setText(UIDominoUtils.bazarToString(uiProcess.heap));
    }

    //найти номер ходящего игрока, нужно для включения нужной радио кнопки
    public static int findCurrPl(List<Player> players, Player pl) {
        int num = 0;
        for (int i = 0; i < players.size(); i++) {
            if (UIDominoUtils.listEqualsIgnoreOrder(players.get(i).getPackOfTiles(), pl.getPackOfTiles())) {
                num = i;
            }
        }
        return num;
    }

    //следующий шаг
    public static void nextStep(BoardTM boardTM, AbstractGame uiProcess, String code, Font f, List<JRadioButton> radios, List<JTextArea> areas, JTextArea bazarArea) throws IOException, ClassNotFoundException {
        //шаг игры
        if (!uiProcess.isGameOver() && uiProcess.getCheckFor() < uiProcess.getPlayers().size()) {
            //сделать ход
            Player pl = uiProcess.gameStep(2, code);

            //включить нужную радио кнопку
            int numOfRadio = UIDominoUtils.findCurrPl(uiProcess.getPlayers(), pl);
            if (numOfRadio == uiProcess.getPlayers().size() - 1) {
                numOfRadio = 0;
            } else numOfRadio++;
            radios.get(numOfRadio).setSelected(true);

            //вывести в каждый текст филд текущие пак оф тайлсы
            UIDominoUtils.outPacks(f, uiProcess, areas);
            UIDominoUtils.outBazar(f, bazarArea, uiProcess);

            boardTM.setBoard(uiProcess.board.getField());

            //проверить на гейм овер
            uiProcess.gameOverCheck(uiProcess.getPlayers());
        } else {
            gameOver(uiProcess);
        }
    }

    //взять из базара
    public static void takeFromBazar(AbstractGame uiProcess, MainTM mainPlTableModel){
        uiProcess.getPlayers().get(0).interactiveTakeFromBazar(uiProcess.heap);
        mainPlayersTilesToTable(uiProcess, mainPlTableModel);
    }

    //вывести набор главного игрока
    public static void mainPlayersTilesToTable(AbstractGame uiProcess, MainTM mainPlTableModel){
        String[][] tiles = uiProcess.getPlayers().get(0).packToString(new GameBoard());

        String[][] mainPlTiles = new String[2][7];

        for (int i = 0; i < 7; i++){
            if(i < uiProcess.getPlayers().get(0).getPackOfTiles().size()){
                mainPlTiles[0][i] = tiles[0][i];
                mainPlTiles[1][i] = tiles[1][i];
            } else{
                mainPlTiles[0][i] = "";
                mainPlTiles[1][i] = "";
            }
        }

        mainPlTableModel.setMainPlayerTiles(mainPlTiles);
    }

    //окончание игры, вызов финального окна
    public static void gameOver(AbstractGame uiProcess) {
        GameOverWindow gameOver = new GameOverWindow();
        String points = "Итоговое число очков: " + UIDominoUtils.outPoints(uiProcess.getPlayers());
        String winner = "Победил игрок " + UIDominoUtils.defineWinner(uiProcess.getPlayers());
        gameOver.setPoints(points);
        gameOver.setWinner(winner);
        gameOver.setVisible(true);
    }

}
