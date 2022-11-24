package ruvsu.domino.ui.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class PlayerBox{
//    public PlayerBox(int num) {
//        super(BoxLayout.Y_AXIS);
//        this.setSize(new Dimension(300, 300));
//        setBorder(new EmptyBorder(10, 10, 10, 10));
//        add(Box.createVerticalStrut(20));
//        add(new JRadioButton());
//        add(new JLabel(String.valueOf(num)));
//        JTextArea area = new JTextArea();
//        area.setPreferredSize(new Dimension(200, 100));
//        add(area);
//    }

    public static Box createPlBox(int n, List<JRadioButton> buttons, List<JLabel> labels, List<JTextArea> areas){
        Box box = Box.createVerticalBox();
        box.setBorder(new EmptyBorder(10, 10, 10, 10));
        box.add(Box.createVerticalStrut(20));
        box.add(buttons.get(n));
        box.add(labels.get(n));
        areas.get(n).setPreferredSize(new Dimension(200, 100));
        box.add(areas.get(n));
        return box;
    }

}
