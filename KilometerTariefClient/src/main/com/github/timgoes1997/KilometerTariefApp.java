package com.github.timgoes1997;

import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormatSymbols;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KilometerTariefApp extends JFrame{

    private List<DayOfWeek> selectedDayOfWeeks = new ArrayList<>();
    private JPanel contentPanel;

    public KilometerTariefApp(){
        loadFrame();
    }

    private void loadFrame(){
        setTitle("Kilometertarief client");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 720, 720);
        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] {0, 0, 40, 40, 40, 40, 0};
        gbl_contentPane.rowHeights = new int[] {30,  30, 30, 30, 30};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
        contentPanel.setLayout(gbl_contentPane);

        JLabel updateCreateRate = new JLabel("Update/Create new rate");
        GridBagConstraints gbc_updateCreateRate = new GridBagConstraints();
        gbc_updateCreateRate.insets = new Insets(0, 0, 5, 5);
        gbc_updateCreateRate.gridx = 0;
        gbc_updateCreateRate.gridy = 0;
        contentPanel.add(updateCreateRate, gbc_updateCreateRate);

        JLabel energyLabel = new JLabel("EnergyLabel");
        GridBagConstraints gbc_energyLabel = new GridBagConstraints();
        gbc_energyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_energyLabel.gridx = 0;
        gbc_energyLabel.gridy = gbc_updateCreateRate.gridy + 1;
        contentPanel.add(energyLabel, gbc_energyLabel);

        final JComboBox<EnergyLabel> energyLabelJComboBox = new JComboBox<EnergyLabel>(EnergyLabel.values());
        GridBagConstraints gbc_energyLabelJCB = new GridBagConstraints();
        gbc_energyLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_energyLabelJCB.insets = new Insets(0,0,5,5);
        gbc_energyLabelJCB.gridx = gbc_energyLabel.gridx + 1;
        gbc_energyLabelJCB.gridy = gbc_energyLabel.gridy;
        contentPanel.add(energyLabelJComboBox, gbc_energyLabelJCB);

        JLabel vehicleTypeLabel = new JLabel("VehicleType");
        GridBagConstraints gbc_vehicleType = new GridBagConstraints();
        gbc_vehicleType.insets = new Insets(0, 0, 5, 5);
        gbc_vehicleType.gridx = gbc_energyLabel.gridx;
        gbc_vehicleType.gridy = gbc_energyLabel.gridy + 1;
        contentPanel.add(vehicleTypeLabel, gbc_vehicleType);

        final JComboBox<VehicleType> vehicleTypeJComboBox = new JComboBox<VehicleType>(VehicleType.values());
        GridBagConstraints gbc_vehicleTypeLabelJCB = new GridBagConstraints();
        gbc_vehicleTypeLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_vehicleTypeLabelJCB.insets = new Insets(0,0,5,5);
        gbc_vehicleTypeLabelJCB.gridx = gbc_vehicleType.gridx + 1;
        gbc_vehicleTypeLabelJCB.gridy = gbc_vehicleType.gridy;
        contentPanel.add(vehicleTypeJComboBox, gbc_vehicleTypeLabelJCB);

        JLabel dayLabel = new JLabel("");
        GridBagConstraints gbc_dayLabel = new GridBagConstraints();
        gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dayLabel.gridx = gbc_vehicleType.gridx + 1;
        gbc_dayLabel.gridy = gbc_vehicleTypeLabelJCB.gridy + 1;
        contentPanel.add(dayLabel, gbc_dayLabel);

        final JPopupMenu menu = new JPopupMenu();
        List<JMenuItem> menuItems = new ArrayList<>();
        for(DayOfWeek day : DayOfWeek.values()){
            JMenuItem dayItem = new JCheckBoxMenuItem(day.toString());
            dayItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(dayItem.isSelected()){
                        selectedDayOfWeeks.add(DayOfWeek.valueOf(dayItem.getText()));
                    }else{
                        selectedDayOfWeeks.remove(DayOfWeek.valueOf(dayItem.getText()));
                    }
                    UpdateDayLabel(dayLabel);
                }
            });
            menuItems.add(dayItem);
            menu.add(dayItem);
        }

        final JButton button = new JButton("Select days");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0,0,0,0);
        gbc_button.gridx = gbc_vehicleType.gridx;
        gbc_button.gridy = gbc_vehicleTypeLabelJCB.gridy + 1;
        contentPanel.add(button, gbc_button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!menu.isVisible()) {
                    Point p = button.getLocationOnScreen();
                    menu.setInvoker(button);
                    menu.setLocation((int) p.getX(),
                            (int) p.getY() + button.getHeight());
                    menu.setVisible(true);
                } else {
                    menu.setVisible(false);
                }

            }
        });

        for(JMenuItem menuItem : menuItems){
            menuItem.addActionListener(new OpenAction(menu, button));
        }

        JLabel startHour = new JLabel("Starting Hour");
        GridBagConstraints gbc_startHour = new GridBagConstraints();
        gbc_startHour.insets = new Insets(0, 0, 5, 5);
        gbc_startHour.gridx = gbc_button.gridx;
        gbc_startHour.gridy = gbc_button.gridy + 1;
        contentPanel.add(startHour, gbc_startHour);

        SpinnerModel hourValue = new SpinnerNumberModel(0, 0, 24, 1);
        final JSpinner startHourSpinner = new JSpinner(hourValue);

    }

    private void UpdateDayLabel(JLabel label){
        String days = "";
        if(selectedDayOfWeeks.size() == DayOfWeek.values().length){
            days = "EVERY DAY OF THE WEEK";
        }else {
            for (DayOfWeek day :
                    selectedDayOfWeeks) {
                days += day.toString() + ", ";
            }
        }
        label.setText(days);
    }

    private static class OpenAction implements ActionListener {

        private JPopupMenu menu;
        private JButton button;

        private OpenAction(JPopupMenu menu, JButton button) {
            this.menu = menu;
            this.button = button;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            menu.show(button, 0, button.getHeight());
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    KilometerTariefApp app = new KilometerTariefApp();

                    app.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
