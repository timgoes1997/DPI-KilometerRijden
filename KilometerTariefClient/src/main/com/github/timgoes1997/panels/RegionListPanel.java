package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.list.RegionListLine;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
        //gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
        //gbl_contentPane.rowHeights = new int[]{500, 23, 0};
        //gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
        //gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
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
