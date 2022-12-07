package ruvsu.domino.client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Vector;

public class WindowUI {
    private JComponent ui;
    private final Font f = new Font("Monospaced", Font.PLAIN, 60);
    public static final int DOMINO_TILE_START = 127024;
    public static final int DOMINO_TILE_END = 127123;
    JTextArea textArea = new JTextArea(4, 10);
    JTextArea textAreaBazar = new JTextArea(2, 5);

    WindowUI() {
        initUI();
    }

/**СПИСОК ВОПРОСОВ
    1. в виде какого компонента лучше представить кости домино, как их связать с внутриигровым представлением Tile?
    2. как представить игровое поле, у меня уже есть алгоритм преставления поля в виде квадратной матрицы строк, подойдет ли такое здесь?
    3. достаточно ли будет для пользователя иметь такой набор действий:
        а)нажать кнопку начать игру и выбрать число игроков (2/3/4)
        б)в компоненте где будет выводиться набор костей пользователя, он кликает на кость которой хочет походить,
            но кость сама становится в допустимую область на поле
    4. на какие классы разбить весь ui, чтобы соблюдалсь принципы ооп?

 */
    public void initUI() {
        ui = new JPanel(new BorderLayout(5, 5));
        ui.setBorder(new EmptyBorder(4, 4, 4, 4));

        //делаем строку из всех возможных костей домино
        StringBuilder sb = new StringBuilder();
        for (int i = DOMINO_TILE_START; i <= DOMINO_TILE_END; i++) {
            String s = new String(Character.toChars(i));
            sb.append(s);
        }

        //добавляем это в textArea
        textArea.setText(sb.toString());
        textArea.setLineWrap(true);
        ui.add(new JScrollPane(textArea));

        textArea.setFont(f);

        /////////////////////////////////////////////

        final Vector<String> mainPlayer = new Vector<>();
        mainPlayer.add("Main players tiles");
        final JList list1 = new JList(mainPlayer);
        ui.add(new JScrollPane(list1), BorderLayout.AFTER_LAST_LINE);

        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i <= 5; i++) {
            String s1 = new String(Character.toChars(127074));
            sb1.append(s1);
        }
        textAreaBazar.setText("Bazar: " + sb1);
        textAreaBazar.setFont(f);
        ui.add(new JScrollPane(textAreaBazar), BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.add(new JScrollPane(new TextArea("pl1: ")));
        ui.add(leftPanel, BorderLayout.WEST);
    }

    public JComponent getUI() {
        return ui;
    }


    public static void main(String[] args) {
//        Tile tile11 = new Tile(1, 1);
//        Tile tile33 = new Tile(3, 3);
//
//        JFrame frame = new DominoFrame("DOMINO", List.of(tile11, tile33));
//        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setVisible(true);
//        System.out.println("\u2764 ");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception useDefault) {
                }
                WindowUI o = new WindowUI();

                JFrame f = new JFrame(o.getClass().getSimpleName());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);

                f.setContentPane(o.getUI());
                f.pack();
                f.setSize(1200, 800);
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
