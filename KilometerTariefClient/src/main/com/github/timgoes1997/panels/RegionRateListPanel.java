package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.list.RegionRateListLine;
import com.github.timgoes1997.listeners.RegionRatePanelListener;
import com.github.timgoes1997.util.VisiblePanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class RegionRateListPanel {
    private DefaultListModel<RegionRateListLine> listModel = new DefaultListModel<RegionRateListLine>();
    private JList<RegionRateListLine> list;
    private JPanel panel;
    private RegionRatePanelListener regionRatePanelListener;
    private JButton createButton;
    private JButton backButton;

    public RegionRateListPanel(RegionRatePanelListener regionRatePanelListener) {
        this.regionRatePanelListener = regionRatePanelListener;
        loadFrame();
    }

    private void loadFrame(){
        panel = new JPanel();
        panel.setBorder(LineBorder.createBlackLineBorder());
        panel.setSize(500, 500);
        panel.setLayout( new GridBagLayout());

        backButton = new JButton("Back");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 0, 5, 5);
        gbc_backButton.gridx = 0;
        gbc_backButton.gridy = 0;
        panel.add(backButton, gbc_backButton);
        backButton.addActionListener(e -> {
            regionRatePanelListener.onBack(VisiblePanel.REGION_RATE);
        });

        JLabel updateCreateRate = new JLabel("Region rates: ");
        GridBagConstraints gbc_regionList = new GridBagConstraints();
        gbc_regionList.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_regionList.gridx = 0;
        gbc_regionList.gridy = 1;
        panel.add(updateCreateRate, gbc_regionList);

        JScrollPane scrollPane = new JScrollPane();
        GridBagConstraints gbc_scrollPane = new GridBagConstraints();
        gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
        gbc_scrollPane.fill = GridBagConstraints.BOTH;
        gbc_scrollPane.gridx = 0;
        gbc_scrollPane.gridy = 2;
        panel.add(scrollPane, gbc_scrollPane);

        list = new JList<RegionRateListLine>(listModel);
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JList<RegionRateListLine> list = (JList)e.getSource();
                int index = list.locationToIndex(e.getPoint());
                RegionRateListLine rll = list.getModel().getElementAt(index);
                if(rll != null && regionRatePanelListener != null){
                    regionRatePanelListener.onSelectRegionRate(rll.getRegionRate());
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
                    JList<RegionRateListLine> list = (JList) e.getSource();
                    RegionRateListLine rll = list.getModel().getElementAt(list.getSelectedIndex());
                    if(rll != null && regionRatePanelListener != null){
                        regionRatePanelListener.onSelectRegionRate(rll.getRegionRate());
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        scrollPane.setViewportView(list);
        scrollPane.setPreferredSize(panel.getPreferredSize());

        createButton = new JButton("Create");
        GridBagConstraints gbc_completionButton = new GridBagConstraints();
        gbc_completionButton.insets = new Insets(0, 0, 5, 5);
        gbc_completionButton.gridx = 0;
        gbc_completionButton.gridy = 3;
        panel.add(createButton, gbc_completionButton);
        createButton.addActionListener(e -> {
            regionRatePanelListener.onSelectCreation();
        });
    }

    private RegionRateListLine getRegion(RegionRate region) {
        for (int i=0; i < listModel.getSize(); i++){
            RegionRateListLine rll = listModel.get(i);
            if(rll.getRegionRate().equals(region)){
                return rll;
            }
        }
        return null;
    }

    public void add(RegionRate regionRate) {
        listModel.addElement(new RegionRateListLine(regionRate));
    }

    public DefaultListModel<RegionRateListLine> getListModel() {
        return listModel;
    }

    public void setListModel(DefaultListModel<RegionRateListLine> listModel) {
        this.listModel = listModel;
    }

    public JList<RegionRateListLine> getList() {
        return list;
    }

    public void setList(JList<RegionRateListLine> list) {
        this.list = list;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public RegionRatePanelListener getRegionRatePanelListener() {
        return regionRatePanelListener;
    }

    public void setRegionRatePanelListener(RegionRatePanelListener regionRatePanelListener) {
        this.regionRatePanelListener = regionRatePanelListener;
    }
}
