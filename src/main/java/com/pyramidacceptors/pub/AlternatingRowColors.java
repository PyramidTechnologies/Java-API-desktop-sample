package com.pyramidacceptors.pub;

import javax.swing.*;
import java.awt.*;

/**
 * Created by cory on 4/7/2016.
 */
public class AlternatingRowColors extends JLabel implements ListCellRenderer {

    public AlternatingRowColors() {
        setOpaque(true);
    }

    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        setText(value.toString());

        if (index % 2 == 0) setBackground(Color.lightGray);
        else setBackground(Color.white);

        return this;
    }
}
