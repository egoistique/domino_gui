package ruvsu.domino.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NumPlWindow extends JFrame{
    private JButton buttonOk =  new JButton("OK");
    private final JRadioButton radio2 = new JRadioButton("2");
    private final JRadioButton radio3 = new JRadioButton("3");
    private JRadioButton radio4 = new JRadioButton("4");
    private JLabel numPlLabel = new JLabel("Выберите число участников 2/3/4 ");

    private JTextArea bazarArea = new JTextArea();

    private int num = 2;

    public int getNum() {
        return num;
    }

    public NumPlWindow(){
        super("si");
        this.setBounds(500,500, 1000, 1000);
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
                    NewWindow.setNum(num);
                } else {
                    num = 4;
                    NewWindow.setNum(num);
                }
                NewWindow newWindow = new NewWindow();
                newWindow.run(num);
            }
        });

        this.pack();
    }

    public static void main(String[] args) {
        NumPlWindow app = new NumPlWindow();
        app.setVisible(true);
    }
}
