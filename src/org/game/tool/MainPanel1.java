/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.game.tool;

//-*- mode:java; encoding:utf-8 -*-
// vim:set fileencoding=utf-8:
//@homepage@
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public final class MainPanel1 extends JPanel {
/*    private MainPanel1() {
        super(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (String t: Arrays.asList("aa", "bbbbbbbbbbbbb", "ccc", "dddddddddddddddd", "eeeeeee")) {
            model.addElement(t);
        }
        JList<String> list = new LinkCellList<>(model);
        add(new JScrollPane(list));
        setPreferredSize(new Dimension(320, 240));
    }
    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                createAndShowGUI();
            }
        });
    }
    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException
               | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("@title@");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel1());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }*/
}
/*
class LinkCellList<E> extends JList<E> {
    private int prevIndex = -1;
    protected LinkCellList(ListModel<E> model) {
        super(model);
    }
    @Override public void updateUI() {
        setForeground(null);
        setBackground(null);
        setSelectionForeground(null);
        setSelectionBackground(null);
        super.updateUI();
        setFixedCellHeight(32);
        setCellRenderer(new LinkCellRenderer<E>());
        //TEST: putClientProperty("List.isFileList", Boolean.TRUE);
    }
    @Override protected void processMouseMotionEvent(MouseEvent e) {
        Point pt = e.getPoint();
        int i = locationToIndex(pt);
        E s = getModel().getElementAt(i);
        Component c = getCellRenderer().getListCellRendererComponent(this, s, i, false, false);
        Rectangle r = getCellBounds(i, i);
        c.setBounds(r);
        if (prevIndex != i) {
            c.doLayout();
        }
        prevIndex = i;
        pt.translate(-r.x, -r.y);
        setCursor(Optional.ofNullable(SwingUtilities.getDeepestComponentAt(c, pt.x, pt.y))
                          .map(Component::getCursor)
                          .orElse(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)));
    }
}

class LinkCellRenderer<E> implements ListCellRenderer<E> {
    private final JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final JCheckBox check = new JCheckBox("check") {
        @Override public void updateUI() {
            super.updateUI();
            setOpaque(false);
        }
    };
    private final JButton button = new JButton("button") {
        @Override public void updateUI() {
            super.updateUI();
        }
    };
    private final JLabel label = new JLabel() {
        @Override public void updateUI() {
            super.updateUI();
        }
    };
    @Override public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        p.removeAll();
        p.add(label);
        p.add(check);
        p.add(button);
        p.setOpaque(true);
        if (isSelected) {
            p.setBackground(list.getSelectionBackground());
            p.setForeground(list.getSelectionForeground());
        } else {
            p.setBackground(list.getBackground());
            p.setForeground(list.getForeground());
        }
        label.setText("<html><a href='#'>" + value);
        return p;
    }
}*/