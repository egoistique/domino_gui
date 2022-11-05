package ruvsu.domino.ui;

import javax.swing.*;
import java.awt.*;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        JFrame frame = new JFrame("Test"); //initialize frame
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel base = new JPanel(); //make base panel with SpringLayout
        SpringLayout baseLayout = new SpringLayout();
        base.setLayout(baseLayout);

        JPanel panel1 = new JPanel() { //initialize JPanels
            public void paintComponent(Graphics tool) {
                super.paintComponent(tool);
                tool.drawRect(50, 50, 100, 100);
            }
        };

        JPanel panel2 = new JPanel() {
            public void paintComponent(Graphics tool) {
                super.paintComponent(tool);
                tool.drawRect(75, 75, 50, 50);
            }
        };

        Container cont = new Container(); //create a container to hold panels
        SpringLayout contLayout = new SpringLayout();
        cont.setLayout(contLayout);

        contLayout.putConstraint(SpringLayout.WEST, panel1, 0, SpringLayout.WEST, cont);
        contLayout.putConstraint(SpringLayout.NORTH, panel1, 0, SpringLayout.NORTH, cont);
        cont.add(panel1); //add panel1 to top left corner of container

        contLayout.putConstraint(SpringLayout.WEST, panel2, 0, SpringLayout.WEST, cont);
        contLayout.putConstraint(SpringLayout.NORTH, panel2, 0, SpringLayout.NORTH, cont);
        cont.add(panel2); //add panel2 to top left corner of container


        cont.setPreferredSize(new Dimension(201, 201));
        panel1.setPreferredSize(cont.getPreferredSize());
        panel2.setPreferredSize(cont.getPreferredSize());

        panel1.setOpaque(false);
        panel2.setOpaque(false);

        JScrollPane scroll = new JScrollPane(cont); //add container to scroll pane
        scroll.setPreferredSize(new Dimension(200, 200));

        baseLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scroll, 0, SpringLayout.HORIZONTAL_CENTER, base);
        baseLayout.putConstraint(SpringLayout.VERTICAL_CENTER, scroll, 0, SpringLayout.VERTICAL_CENTER, base);
        base.add(scroll); //add scroll pane to center of base JPanel

        frame.add(base);
        frame.setVisible(true);
    }
}