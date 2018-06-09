package com.github.timgoes1997;

import com.github.timgoes1997.gateway.VehicleClientGateway;
import com.github.timgoes1997.gateway.interfaces.VehicleClient;
import com.github.timgoes1997.request.topic.TopicReply;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Logger;

public class App extends JFrame {

    private static final Logger LOGGER = Logger.getLogger(App.class.getName());

    private JPanel mainPanel;
    private VehicleClientGateway vehicleClientGateway;

    public App(){
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
    }

    private void setupGateway() {
        try {
            vehicleClientGateway = new VehicleClientGateway(topicReply -> {

            });
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
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
