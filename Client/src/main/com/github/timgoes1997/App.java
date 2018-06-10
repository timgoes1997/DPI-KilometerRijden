package com.github.timgoes1997;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.gateway.VehicleClientGateway;
import com.github.timgoes1997.gateway.interfaces.VehicleClient;
import com.github.timgoes1997.location.Location;
import com.github.timgoes1997.request.topic.TopicReply;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class App extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private VehicleClientGateway vehicleClientGateway;
    private JPanel mainPanel;

    private JPanel panel;
    private JLabel price;
    private JComboBox<EnergyLabel> energyLabelJComboBox;
    private JComboBox<VehicleType> vehicleTypeJComboBox;
    private JSpinner locationX;
    private SpinnerModel locationXValue;
    private JSpinner locationY;
    private SpinnerModel locationYValue;
    private JButton sendInfoButton;


    public App() {
        loadFrame();
    }

    private void loadFrame() {
        setTitle("Car/Truck client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 450);
        mainPanel = new JPanel();
        mainPanel.setSize(getPreferredSize());
        mainPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        setContentPane(mainPanel);

        setupGateway();
        loadPanel();
    }

    private void setupGateway() {
        try {
            vehicleClientGateway = new VehicleClientGateway(new VehicleClient() {
                @Override
                public void onReceivePriceTopic(TopicReply topicReply) {
                    System.out.println("Received reply");
                }

                @Override
                public void onReceiveInitialPrice(RegionRate regionRate) {
                    price.setText("\u20ac" + regionRate.getKilometerPrice());
                    panel.repaint();
                }

                @Override
                public void onReceiveEmpty() {
                    price.setText("Received nothing, region might not exist!");
                }
            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    private void loadPanel() {
        panel = new JPanel();
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.setBorder(LineBorder.createBlackLineBorder());
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 30, 30, 30, 30, 0};
        gbl_contentPane.rowHeights = new int[]{30, 30, 30, 30, 30};
        gbl_contentPane.location(0, 0);
        panel.setLayout(gbl_contentPane);

        GridBagConstraints gbc_panel = new GridBagConstraints();
        gbc_panel.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_panel.gridx = 0;
        gbc_panel.gridy = 0;
        panel.setSize(500, 500);
        panel.setVisible(true);
        mainPanel.add(panel, gbc_panel);

        JLabel priceLabel = new JLabel("CurrentPrice");
        GridBagConstraints gbc_priceLabel = new GridBagConstraints();
        gbc_priceLabel.insets = new Insets(0, 0, 5, 5);
        gbc_priceLabel.gridx = 0;
        gbc_priceLabel.gridy = 1;
        panel.add(priceLabel, gbc_priceLabel);

        price = new JLabel();
        GridBagConstraints gbc_price = new GridBagConstraints();
        gbc_price.insets = new Insets(0, 0, 5, 5);
        gbc_price.gridx = 1;
        gbc_price.gridy = 1;
        panel.add(price, gbc_price);


        JLabel energyLabel = new JLabel("EnergyLabel");
        GridBagConstraints gbc_energyLabel = new GridBagConstraints();
        gbc_energyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_energyLabel.gridx = 0;
        gbc_energyLabel.gridy = 2;
        panel.add(energyLabel, gbc_energyLabel);

        energyLabelJComboBox = new JComboBox<EnergyLabel>(EnergyLabel.values());
        GridBagConstraints gbc_energyLabelJCB = new GridBagConstraints();
        gbc_energyLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_energyLabelJCB.insets = new Insets(0, 0, 5, 5);
        gbc_energyLabelJCB.gridx = gbc_energyLabel.gridx + 1;
        gbc_energyLabelJCB.gridy = gbc_energyLabel.gridy;
        panel.add(energyLabelJComboBox, gbc_energyLabelJCB);

        JLabel vehicleTypeLabel = new JLabel("VehicleType");
        GridBagConstraints gbc_vehicleType = new GridBagConstraints();
        gbc_vehicleType.insets = new Insets(0, 0, 5, 5);
        gbc_vehicleType.gridx = 0;
        gbc_vehicleType.gridy = 3;
        panel.add(vehicleTypeLabel, gbc_vehicleType);

        vehicleTypeJComboBox = new JComboBox<VehicleType>(VehicleType.values());
        GridBagConstraints gbc_vehicleTypeLabelJCB = new GridBagConstraints();
        gbc_vehicleTypeLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_vehicleTypeLabelJCB.insets = new Insets(0, 0, 5, 5);
        gbc_vehicleTypeLabelJCB.gridx = gbc_vehicleType.gridx + 1;
        gbc_vehicleTypeLabelJCB.gridy = gbc_vehicleType.gridy;
        panel.add(vehicleTypeJComboBox, gbc_vehicleTypeLabelJCB);

        locationXValue = new SpinnerNumberModel(2.501, -100, 100, 0.001d);
        locationYValue = new SpinnerNumberModel(2.501, -100, 100, 0.001d);

        JLabel locationXLabel = new JLabel("X");
        GridBagConstraints gbc_locationXLabel = new GridBagConstraints();
        gbc_locationXLabel.insets = new Insets(0, 0, 5, 5);
        gbc_locationXLabel.gridx = 0;
        gbc_locationXLabel.gridy = 4;
        panel.add(locationXLabel, gbc_locationXLabel);

        locationX = new JSpinner(locationXValue);
        disAllowTextNumberSpinner(locationX);
        GridBagConstraints gbc_locationXSpinner = new GridBagConstraints();
        gbc_locationXSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationXSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_locationXSpinner.gridx = gbc_locationXLabel.gridx + 1;
        gbc_locationXSpinner.gridy = gbc_locationXLabel.gridy;
        panel.add(locationX, gbc_locationXSpinner);

        JLabel locationYLabel = new JLabel("Y");
        GridBagConstraints gbc_locationY = new GridBagConstraints();
        gbc_locationY.insets = new Insets(0, 0, 5, 5);
        gbc_locationY.gridx = 0;
        gbc_locationY.gridy = 5;
        panel.add(locationYLabel, gbc_locationY);

        locationY = new JSpinner(locationYValue);
        disAllowTextNumberSpinner(locationY);
        GridBagConstraints gbc_locationYSpinner = new GridBagConstraints();
        gbc_locationYSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_locationYSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_locationYSpinner.gridx = gbc_locationY.gridx + 1;
        gbc_locationYSpinner.gridy = gbc_locationY.gridy;
        panel.add(locationY, gbc_locationYSpinner);

        sendInfoButton = new JButton("Send info");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 0, 5, 5);
        gbc_backButton.gridx = 1;
        gbc_backButton.gridy = 6;
        panel.add(sendInfoButton, gbc_backButton);
        sendInfoButton.addActionListener(e -> {
            EnergyLabel currentEnergyLabel = energyLabelJComboBox.getItemAt(energyLabelJComboBox.getSelectedIndex());
            VehicleType currentVehicleType = vehicleTypeJComboBox.getItemAt(vehicleTypeJComboBox.getSelectedIndex());
            double x = (double)locationXValue.getValue();
            double y = (double)locationYValue.getValue();
            Location l = new Location(x, y);

            try {
                vehicleClientGateway.sendRegionRequest(l, currentVehicleType, currentEnergyLabel);
            } catch (JMSException e1) {
                e1.printStackTrace();
            }
        });
    }

    private void disAllowTextNumberSpinner(JSpinner spinner) {
        JFormattedTextField spinnerText = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) spinnerText.getFormatter()).setAllowsInvalid(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                App app = new App();

                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
