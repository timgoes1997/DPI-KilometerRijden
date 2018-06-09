package com.github.timgoes1997;

import com.github.timgoes1997.dummy.DummyDataGenerator;
import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.gateway.RegionRateClientGateway;
import com.github.timgoes1997.gateway.interfaces.RegionRateClientListener;
import com.github.timgoes1997.listeners.RegionListPanelListener;
import com.github.timgoes1997.listeners.RegionRatePanelListener;
import com.github.timgoes1997.listeners.RegionRateCompletionListener;
import com.github.timgoes1997.panels.PanelInfo;
import com.github.timgoes1997.panels.RegionRateCreationPanel;
import com.github.timgoes1997.panels.RegionListPanel;
import com.github.timgoes1997.panels.RegionRateListPanel;
import com.github.timgoes1997.request.rate.RegionRateRequest;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.request.region.RegionRequestType;
import com.github.timgoes1997.util.VisiblePanel;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;

import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class KilometerTariefApp extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(KilometerTariefApp.class.getName());

    private JPanel mainPanel;
    private RegionRateCreationPanel regionRateCreationPanel;
    private RegionListPanel regionListPanel;
    private RegionRateListPanel regionRateListPanel;
    private DummyDataGenerator dummyDataGenerator; //Remove once JMS queue to region service is working
    private RegionRateClientGateway regionRateClientGateway;
    private Region selectedRegion;

    public KilometerTariefApp() {
        loadFrame();
    }

    private void loadFrame() {
        setTitle("Kilometertarief client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        mainPanel = new JPanel();
        mainPanel.setSize(getPreferredSize());
        mainPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        setContentPane(mainPanel);

        setupGateway();

//        dummyDataGenerator = new DummyDataGenerator();
        //Creating kilometer tarief creation panel
        createRegionListPanel(true);
        createRegionRateListPanel(false);
        createRegionRateCreationPanel(false);
        getRegions();
    }

    private void setupGateway() {
        System.out.println("Setting up gateway");
        try {
            regionRateClientGateway = new RegionRateClientGateway(new RegionRateClientListener() {
                @Override
                public void onReceiveRegions(List<Region> regions) {
                    LOGGER.info("Received regions");
                    regionListPanel.getListModel().clear();
                    regions.forEach(regionListPanel::add);
                    setVisiblePanel(VisiblePanel.REGION);
                    regionListPanel.getList().repaint();
                }

                @Override
                public void onReceiveRegionRates(List<RegionRate> regionRates) {
                    LOGGER.info("Received rates");
                    regionRates.forEach(regionRateListPanel::add);
                    setVisiblePanel(VisiblePanel.REGION_RATE);
                    regionRateListPanel.getList().repaint();
                }

                @Override
                public void onClientRequestCanceled(RegionRateRequest request) {
                    PanelInfo panelInfo = getVisiblePanelInfo();
                    if (panelInfo != null) {
                        panelInfo.onReceiveInfo("Failed to execute request rate " +
                                request.getRegionRateRequestType().toString() + " for region " +
                                request.getRegionRate().getRegion().getName());
                    }
                }

                @Override
                public void onReceiveRegionRateCreate(RegionRate regionRate) {
                    if (regionRate.getRegion() == null && regionRate.getRegion().getName() == null) {
                        regionRateListPanel.onReceiveInfo("Received empty create region rate");
                    }

                    regionRateListPanel.add(regionRate);
                    setVisiblePanel(VisiblePanel.REGION_RATE);
                    regionRateListPanel.onReceiveInfo("Received new regionRate for region " + regionRate.getRegion().getName());
                    regionRateListPanel.getPanel().repaint();
                }

                @Override
                public void onReceiveRegionRateUpdate(RegionRate regionRate) {
                    if (regionRate.getRegion() == null && regionRate.getRegion().getName() == null) {
                        regionRateListPanel.onReceiveInfo("Received empty update region rate");
                    }

                    regionRateListPanel.update(regionRate);
                    setVisiblePanel(VisiblePanel.REGION_RATE);
                    regionRateListPanel.onReceiveInfo("Received updated regionRate " + regionRate.getId().toString() +
                            " for region " + regionRate.getRegion().getName());
                    regionRateListPanel.getPanel().repaint();
                }

                @Override
                public void onReceiveRegionRateRemove(RegionRate regionRate) {
                    if (regionRate.getRegion() == null && regionRate.getRegion().getName() == null) {
                        regionRateListPanel.onReceiveInfo("Received empty remove region rate");
                    }

                    regionRateListPanel.remove(regionRate);
                    setVisiblePanel(VisiblePanel.REGION_RATE);
                    regionRateListPanel.onReceiveInfo("Received remove regionRate " + regionRate.getId().toString() +
                            " for region " + regionRate.getRegion().getName());
                    regionRateListPanel.getPanel().repaint();
                }
            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    private void createRegionRateCreationPanel(boolean visibility) {
        regionRateCreationPanel = new RegionRateCreationPanel(new RegionRateCompletionListener() {
            @Override
            public void onCompletion(List<RegionRate> regionRateList, RegionRateRequestType type) {
                regionRateList.forEach(regionRate -> {
                    requestForRegionRate(regionRate, type);
                });
            }

            @Override
            public void onBack(VisiblePanel visiblePanel) {
                setVisiblePanel(VisiblePanel.REGION_RATE);
            }
        });
        setPanelConstraints(visibility, regionRateCreationPanel.getPanel());
    }

    private void createRegionRateListPanel(boolean visibility) {
        regionRateListPanel = new RegionRateListPanel(new RegionRatePanelListener() {

            @Override
            public void onSelectCreation() {
                regionRateCreationPanel.resetValues();
                regionRateCreationPanel.insert(selectedRegion);
                setVisiblePanel(VisiblePanel.REGION_CREATION);
            }

            @Override
            public void onSelectRegionRate(RegionRate regionRate) {
                regionRateCreationPanel.insert(regionRate);
                regionRateCreationPanel.insert(selectedRegion);
                setVisiblePanel(VisiblePanel.REGION_CREATION);
            }

            @Override
            public void onRegionRateUpdate(RegionRate regionRate) {
                requestForRegionRate(regionRate, RegionRateRequestType.UPDATE);
            }

            @Override
            public void onRegionRateDelete(RegionRate regionRate) {
                requestForRegionRate(regionRate, RegionRateRequestType.DELETE);
            }

            @Override
            public void onRegionRateCreate(RegionRate regionRate) {
                requestForRegionRate(regionRate, RegionRateRequestType.CREATE);
            }

            @Override
            public void onBack(VisiblePanel visiblePanel) {
                getRegions();
            }
        });

        setPanelConstraints(visibility, regionRateListPanel.getPanel());
    }

    private void requestForRegionRate(RegionRate regionRate, RegionRateRequestType rrt) {
        LOGGER.info("Client: " + rrt.toString());
        try {
            regionRateClientGateway.request(regionRate, rrt);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void createRegionListPanel(boolean visibility) {
        regionListPanel = new RegionListPanel(new RegionListPanelListener() {
            @Override
            public void onSelectRegion(Region region) {
                if (region == null) return;
                regionRateListPanel.getListModel().removeAllElements();
                regionRateListPanel.getList().repaint();
                selectedRegion = region;
                getRegionRates(region);
//                dummyDataGenerator.getRatesForRegion(region).forEach(regionRate -> regionRateListPanel.add(regionRate));
            }

        });
        setPanelConstraints(visibility, regionListPanel.getPanel());
    }

    private void getRegionRates(Region region) {
        try {
            regionRateClientGateway.getRegionRates(region);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void getRegions() {
        try {
            regionRateClientGateway.getRegions();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    private void setPanelConstraints(boolean visibility, JPanel panel) {
        GridBagConstraints gbc_regionListPane = new GridBagConstraints();
        gbc_regionListPane.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_regionListPane.gridx = 0;
        gbc_regionListPane.gridy = 1;
        panel.setSize(500, 500);
        panel.setVisible(visibility);
        mainPanel.add(panel, gbc_regionListPane);
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

    private PanelInfo getVisiblePanelInfo() {
        if (regionListPanel.getPanel().isVisible()) return regionListPanel;
        if (regionRateCreationPanel.getPanel().isVisible()) return regionRateCreationPanel;
        if (regionRateListPanel.getPanel().isVisible()) return regionRateListPanel;
        return null;
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
