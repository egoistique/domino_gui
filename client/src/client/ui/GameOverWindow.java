package client.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameOverWindow extends JFrame {
    private JButton buttonOk =  new JButton("OK");
    private JLabel labelWinner = new JLabel();
    private JLabel labelPoints = new JLabel();

    private String points = "";
    private String winner = "";

    public void setPoints(String points) {
        this.points = points;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public GameOverWindow(){
        super("GAME OVER");
        this.setBounds(500,500, 1000, 1000);
        this.setPreferredSize(new Dimension(500, 200));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container container = this.getContentPane();
        container.setLayout(new GridLayout(3, 4,3, 3));

        labelPoints.setText(points);
        labelWinner.setText(winner);

        container.add(labelWinner);
        container.add(labelPoints);

        container.add(buttonOk);

        buttonOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                labelPoints.setText(points);
                labelWinner.setText(winner);
            }
        });

        this.pack();
    }

}
