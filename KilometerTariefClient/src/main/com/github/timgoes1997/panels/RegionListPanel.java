package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.list.RegionListLine;
import com.github.timgoes1997.listeners.RegionListPanelListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegionListPanel implements PanelInfo{
    private DefaultListModel<RegionListLine> listModel = new DefaultListModel<RegionListLine>();
    private JList<RegionListLine> list;
    private JPanel panel;
    private JLabel info;
    private RegionListPanelListener regionListPanelListener;

    public RegionListPanel(RegionListPanelListener regionListPanelListener) {
        loadFrame();
        this.regionListPanelListener = regionListPanelListener;
    }

    private void loadFrame() {
        panel = new JPanel();
        panel.setBorder(LineBorder.createBlackLineBorder());
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
                if(e.getClickCount() == 2) {
                    JList<RegionListLine> list = (JList) e.getSource();
                    int index = list.locationToIndex(e.getPoint());
                    RegionListLine rll = list.getModel().getElementAt(index);
                    if (rll != null && regionListPanelListener != null) {
                        regionListPanelListener.onSelectRegion(rll.getRegion());
                    }
                }
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
                    if(rll != null && regionListPanelListener != null){
                        regionListPanelListener.onSelectRegion(rll.getRegion());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        scrollPane.setViewportView(list);
        scrollPane.setPreferredSize(panel.getPreferredSize());

        info = new JLabel("Info");
        GridBagConstraints gbc_info = new GridBagConstraints();
        gbc_info.gridx = 0;
        gbc_info.gridy = 2;
        panel.add(info, gbc_info);
    }

    private RegionListLine getRegion(Region region) {
        for (int i=0; i < listModel.getSize(); i++){
            RegionListLine rll = listModel.get(i);
            if(rll.getRegion().equals(region)){
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

    @Override
    public void onReceiveInfo(String message) {
        info.setText(message);
    }
}
