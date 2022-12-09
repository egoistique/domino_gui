package ruvsu.domino.client.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame{
    private final JButton buttonOk =  new JButton("OK");
    private final JRadioButton radio2 = new JRadioButton("2           ");
    private final JRadioButton radio3 = new JRadioButton("3           ");
    private final JRadioButton radio4 = new JRadioButton("4           ");
    private final JRadioButton radioLocal = new JRadioButton("Local           ");
    private final JRadioButton radioRemote = new JRadioButton("Remote           ");
    private final JLabel numPlLabel = new JLabel("Выберите число участников 2/3/4 ");
    private final JLabel chooserLabel = new JLabel("Выберите вид ");

    private int num = 2;
    private int view = 1;

    public StartWindow(){
        super("start");
        this.setBounds(500,500, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 4,3, 3));


        container.add(numPlLabel);
        ButtonGroup groupNum = new ButtonGroup();
        groupNum.add(radio2);
        groupNum.add(radio3);
        groupNum.add(radio4);
        radio2.setSelected(true);

        ButtonGroup groupChooser = new ButtonGroup();
        groupChooser.add(radioLocal);
        groupChooser.add(radioRemote);
        radioLocal.setSelected(true);

        Box numOfPl = Box.createHorizontalBox();
        numOfPl.setBorder(new EmptyBorder(10, 10, 10, 10));
        numOfPl.add(radio2);
        numOfPl.add(radio3);
        numOfPl.add(radio4);
        container.add(numOfPl);

        container.add(chooserLabel);

        Box chooser = Box.createHorizontalBox();
        chooser.setBorder(new EmptyBorder(10, 10, 10, 10));
        chooser.add(radioLocal);
        chooser.add(radioRemote);
        container.add(chooser);

        container.add(buttonOk);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radio3.isSelected()) {
                    num = 3;
                } else if (radio4.isSelected()) {
                    num = 4;
                }

                if(radioRemote.isSelected()){
                    view = 2;
                }

                BasicWindow basicWindow = new BasicWindow();
                basicWindow.setNum(num);
                basicWindow.setView(view);
                basicWindow.run();
            }
        });

        this.pack();
    }

}
