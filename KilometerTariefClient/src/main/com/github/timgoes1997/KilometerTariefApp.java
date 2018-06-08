package com.github.timgoes1997;

import com.github.timgoes1997.dummy.DummyDataGenerator;
import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.list.RegionRateListLine;
import com.github.timgoes1997.listeners.RegionListPanelListener;
import com.github.timgoes1997.listeners.RegionRatePanelListener;
import com.github.timgoes1997.listeners.TariefCompletionListener;
import com.github.timgoes1997.panels.KilometerTariefCreationPanel;
import com.github.timgoes1997.panels.RegionListPanel;
import com.github.timgoes1997.panels.RegionRateListPanel;
import com.github.timgoes1997.util.OpenAction;

import javax.swing.*;
import javax.swing.text.NumberFormatter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class KilometerTariefApp extends JFrame {

    private JPanel mainPanel;
    private KilometerTariefCreationPanel kilometerTariefCreationPanel;
    private RegionListPanel regionListPanel;
    private RegionRateListPanel regionRateListPanel;
    private DummyDataGenerator dummyDataGenerator; //Remove once JMS queue to region service is working

    public KilometerTariefApp() {
        loadFrame();
    }

    private void loadFrame() {
        setTitle("Kilometertarief client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 720, 720);
        mainPanel = new JPanel();
        mainPanel.setSize(getPreferredSize());
        mainPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        setContentPane(mainPanel);

//        //Creating kilometer tarief creation panel
//        kilometerTariefCreationPanel = new KilometerTariefCreationPanel(new TariefCompletionListener() {
//            @Override
//            public void OnCompletion(List<RegionRate> regionRateList) {
//                kilometerTariefCreationPanel.getTariefCreationPanel().setVisible(false);
//            }
//        });
//        GridBagConstraints gbc_contentPane = new GridBagConstraints();
//        gbc_contentPane.anchor = GridBagConstraints.FIRST_LINE_START;
//        gbc_contentPane.fill = GridBagConstraints.HORIZONTAL;
//        gbc_contentPane.weightx = 1;
//        gbc_contentPane.weighty = 1;
//        mainPanel.add(kilometerTariefCreationPanel.getTariefCreationPanel(), gbc_contentPane);

        dummyDataGenerator = new DummyDataGenerator();
        createRegionListPanel();
        createRegionRateListPanel();
    }

    private void createRegionRateListPanel(){
        regionRateListPanel = new RegionRateListPanel(new RegionRatePanelListener() {

            @Override
            public void onSelectCreation() {

            }

            @Override
            public void onSelectRegionRate(RegionRate regionRate) {

            }

            @Override
            public void onRegionRateUpdate(RegionRate regionRate) {

            }

            @Override
            public void onRegionRateDelete(RegionRate regionRate) {

            }

            @Override
            public void onRegionRateCreate(RegionRate regionRate) {

            }
        });

        GridBagConstraints gbc_regionListPane = new GridBagConstraints();
        gbc_regionListPane.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbc_regionListPane.fill = GridBagConstraints.BOTH;
        gbc_regionListPane.gridx = 0;
        gbc_regionListPane.gridy = 1;
        JPanel panel = regionRateListPanel.getPanel();
        panel.setSize(500,500 );
        panel.setVisible(false);
        mainPanel.add(panel, gbc_regionListPane);
    }

    private void createRegionListPanel(){
        regionListPanel = new RegionListPanel(new RegionListPanelListener() {
            @Override
            public void onSelectRegion(Region region) {
                if(region == null) return;
                dummyDataGenerator.getRatesForRegion(region).forEach(regionRate -> regionRateListPanel.add(regionRate));
                regionListPanel.getPanel().setVisible(false);
                regionRateListPanel.getPanel().setVisible(true);
            }
        });
        GridBagConstraints gbc_regionListPane = new GridBagConstraints();
        gbc_regionListPane.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbc_regionListPane.fill = GridBagConstraints.BOTH;
        gbc_regionListPane.gridx = 0;
        gbc_regionListPane.gridy = 1;
        JPanel panel= regionListPanel.getPanel();
        panel.setSize(500,500 );
        mainPanel.add(panel, gbc_regionListPane);

        dummyDataGenerator.getRegionList().forEach(region -> {
            regionListPanel.add(region);
        });
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                KilometerTariefApp app = new KilometerTariefApp();

                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
