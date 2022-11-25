package ruvsu.domino.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartWindow extends JFrame{
    private final JButton buttonOk =  new JButton("OK");
    private final JRadioButton radio2 = new JRadioButton("2");
    private final JRadioButton radio3 = new JRadioButton("3");
    private final JRadioButton radio4 = new JRadioButton("4");
    private final JLabel numPlLabel = new JLabel("Выберите число участников 2/3/4 ");

    private int num = 2;

    public StartWindow(){
        super("start");
        this.setBounds(500,500, 1000, 1000);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 4,3, 3));


        container.add(numPlLabel);
        ButtonGroup group = new ButtonGroup();
        group.add(radio2);
        group.add(radio3);
        group.add(radio4);

        container.add(radio2);
        radio2.setSelected(true);
        container.add(radio3);
        container.add(radio4);
        container.add(buttonOk);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(radio3.isSelected()) {
                    num = 3;
                } else if (radio4.isSelected()) {
                    num = 4;
                }
                BasicWindow basicWindow = new BasicWindow();
                basicWindow.setNum(num);
                basicWindow.run();
            }
        });

        this.pack();
    }

}
