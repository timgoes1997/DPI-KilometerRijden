package com.github.timgoes1997;

import com.github.timgoes1997.dummy.DummyDataGenerator;
import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.listeners.RegionListPanelListener;
import com.github.timgoes1997.listeners.RegionRatePanelListener;
import com.github.timgoes1997.listeners.RegionRateCompletionListener;
import com.github.timgoes1997.panels.RegionRateCreationPanel;
import com.github.timgoes1997.panels.RegionListPanel;
import com.github.timgoes1997.panels.RegionRateListPanel;
import com.github.timgoes1997.util.VisiblePanel;

import javax.swing.*;

import java.awt.*;
import java.util.List;

public class KilometerTariefApp extends JFrame {

    private JPanel mainPanel;
    private RegionRateCreationPanel regionRateCreationPanel;
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
        dummyDataGenerator = new DummyDataGenerator();
        createRegionListPanel(true);
        createRegionRateListPanel(false);
        createRegionRateCreationPanel(false);
    }

    private void createRegionRateCreationPanel(boolean visibility) {
        regionRateCreationPanel = new RegionRateCreationPanel(new RegionRateCompletionListener() {
            @Override
            public void onCompletion(RegionRate regionRateList) {
                setVisiblePanel(VisiblePanel.REGION_RATE);
            }

            @Override
            public void onBack(VisiblePanel visiblePanel) {
                setVisiblePanel(VisiblePanel.REGION_RATE);
            }
        });
        GridBagConstraints gbc_contentPane = new GridBagConstraints();
        gbc_contentPane.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_contentPane.fill = GridBagConstraints.HORIZONTAL;
        gbc_contentPane.weightx = 1;
        gbc_contentPane.weighty = 1;
        JPanel panel = regionRateCreationPanel.getPanel();
        panel.setVisible(visibility);
        mainPanel.add(panel, gbc_contentPane);
    }

    private void createRegionRateListPanel(boolean visibility) {
        regionRateListPanel = new RegionRateListPanel(new RegionRatePanelListener() {

            @Override
            public void onSelectCreation() {
                setVisiblePanel(VisiblePanel.REGION_CREATION);
            }

            @Override
            public void onSelectRegionRate(RegionRate regionRate) {
                setVisiblePanel(VisiblePanel.REGION_CREATION);
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

            @Override
            public void onBack(VisiblePanel visiblePanel) {
                setVisiblePanel(VisiblePanel.REGION);
            }
        });

        GridBagConstraints gbc_regionListPane = new GridBagConstraints();
        gbc_regionListPane.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbc_regionListPane.fill = GridBagConstraints.BOTH;
        gbc_regionListPane.gridx = 0;
        gbc_regionListPane.gridy = 1;
        JPanel panel = regionRateListPanel.getPanel();
        panel.setSize(500, 500);
        panel.setVisible(visibility);
        mainPanel.add(panel, gbc_regionListPane);
    }

    private void createRegionListPanel(boolean visibility) {
        regionListPanel = new RegionListPanel(new RegionListPanelListener() {
            @Override
            public void onSelectRegion(Region region) {
                if (region == null) return;
                dummyDataGenerator.getRatesForRegion(region).forEach(regionRate -> regionRateListPanel.add(regionRate));
                setVisiblePanel(VisiblePanel.REGION_RATE);
            }
        });
        GridBagConstraints gbc_regionListPane = new GridBagConstraints();
        gbc_regionListPane.anchor = GridBagConstraints.FIRST_LINE_START;
        //gbc_regionListPane.fill = GridBagConstraints.BOTH;
        gbc_regionListPane.gridx = 0;
        gbc_regionListPane.gridy = 1;
        JPanel panel = regionListPanel.getPanel();
        panel.setSize(500, 500);
        panel.setVisible(visibility);
        mainPanel.add(panel, gbc_regionListPane);

        dummyDataGenerator.getRegionList().forEach(region -> {
            regionListPanel.add(region);
        });
    }

    private void setVisiblePanel(VisiblePanel visiblePanel) {
        switch (visiblePanel) {
            case REGION:
                regionListPanel.getPanel().setVisible(true);
                regionRateCreationPanel.getPanel().setVisible(false);
                regionRateListPanel.getPanel().setVisible(false);
                break;
            case REGION_RATE:
                regionListPanel.getPanel().setVisible(false);
                regionRateCreationPanel.getPanel().setVisible(false);
                regionRateListPanel.getPanel().setVisible(true);
                break;
            case REGION_CREATION:
                regionListPanel.getPanel().setVisible(false);
                regionRateCreationPanel.getPanel().setVisible(true);
                regionRateListPanel.getPanel().setVisible(false);
                break;
        }
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
