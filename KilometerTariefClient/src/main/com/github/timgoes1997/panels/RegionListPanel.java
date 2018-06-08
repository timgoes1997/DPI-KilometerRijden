package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.list.RegionListLine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegionListPanel {

    private DefaultListModel<RegionListLine> listModel = new DefaultListModel<RegionListLine>();
    private JList<RegionListLine> list;
    private JPanel panel;

    public RegionListPanel() {
        loadFrame();
    }

    private void loadFrame() {
        panel = new JPanel();
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        panel.setLayout(gbl_contentPane);

        JLabel updateCreateRate = new JLabel("Regions: ");
        GridBagConstraints gbc_regionList = new GridBagConstraints();
        gbc_regionList.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_regionList.gridx = 0;
        gbc_regionList.gridy = 0;
        panel.add(updateCreateRate, gbc_regionList);


        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 1;
        panel.add(scrollPane, gbc_scrollPane);

        list = new JList<RegionListLine>(listModel);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<RegionListLine> list = (JList)e.getSource();
                int index = list.locationToIndex(e.getPoint());
                RegionListLine rll = list.getModel().getElementAt(index);
                System.out.println("Pressed:" + rll.toString());
            }
        });
        list.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    JList<RegionListLine> list = (JList) e.getSource();
                    RegionListLine rll = list.getModel().getElementAt(list.getSelectedIndex());
                    System.out.println("Key:" + rll.toString());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        scrollPane.setViewportView(list);
    }

    private RegionListLine getRegion(Region region) {
        for (int i=0; i < listModel.getSize(); i++){
            RegionListLine rll = listModel.get(i);
            if(rll.equals(region)){
                return rll;
            }
        }
        return null;
    }

    public void add(Region region) {
        listModel.addElement(new RegionListLine(region));
    }

    public void update(Region region){
        RegionListLine rll = getRegion(region);
        if(rll != null && region != null){
            rll.getRegion().setBorderList(region.getBorderList());
        }
    }

    public DefaultListModel<RegionListLine> getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel<RegionListLine> listModel) {
        this.listModel = listModel;
    }

    public JList<RegionListLine> getList() {
        return list;
    }

    public void setList(JList<RegionListLine> list) {
        this.list = list;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }
}
