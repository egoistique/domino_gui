package client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;

public class StartWindow extends JFrame{
    private final JRadioButton radio3 = new JRadioButton("3           ");
    private final JRadioButton radio4 = new JRadioButton("4           ");
    private final JRadioButton radioRemote = new JRadioButton("Remote           ");

    private int num = 2;
    private int view = 2;

    public StartWindow(){
        super("start");
        this.setBounds(500,500, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 4,3, 3));


        JLabel numPlLabel = new JLabel("Выберите число участников 2/3/4 ");
        container.add(numPlLabel);
        ButtonGroup groupNum = new ButtonGroup();
        JRadioButton radio2 = new JRadioButton("2           ");
        groupNum.add(radio2);
        groupNum.add(radio3);
        groupNum.add(radio4);
        radio2.setSelected(true);

        ButtonGroup groupChooser = new ButtonGroup();
        JRadioButton radioLocal = new JRadioButton("Local           ");
        groupChooser.add(radioLocal);
        groupChooser.add(radioRemote);
        radioLocal.setSelected(true);

        Box numOfPl = Box.createHorizontalBox();
        numOfPl.setBorder(new EmptyBorder(10, 10, 10, 10));
        numOfPl.add(radio2);
        numOfPl.add(radio3);
        numOfPl.add(radio4);
        container.add(numOfPl);

        JLabel chooserLabel = new JLabel("Выберите вид ");
        container.add(chooserLabel);

        Box chooser = Box.createHorizontalBox();
        chooser.setBorder(new EmptyBorder(10, 10, 10, 10));
        chooser.add(radioLocal);
        chooser.add(radioRemote);
        container.add(chooser);

        JButton buttonOk = new JButton("OK");
        container.add(buttonOk);

        buttonOk.addActionListener(e -> {
            if(radio3.isSelected()) {
                num = 3;
            } else if (radio4.isSelected()) {
                num = 4;
            }

            if(radioRemote.isSelected()){
                view = 3;
            }

            BasicWindow basicWindow;
            try {
                basicWindow = new BasicWindow(num, view);
                basicWindow.run(basicWindow);
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        this.pack();
    }

}
