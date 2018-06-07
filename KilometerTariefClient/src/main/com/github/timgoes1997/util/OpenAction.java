package com.github.timgoes1997.util;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenAction implements ActionListener {

    private JPopupMenu menu;
    private JButton button;

    public OpenAction(JPopupMenu menu, JButton button) {
        this.menu = menu;
        this.button = button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        menu.show(button, 0, button.getHeight());
    }
}
