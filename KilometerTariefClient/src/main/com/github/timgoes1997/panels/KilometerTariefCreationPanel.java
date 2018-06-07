package com.github.timgoes1997.panels;

import com.github.timgoes1997.KilometerTariefApp;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.listeners.TariefCompletionListener;
import com.github.timgoes1997.util.OpenAction;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class KilometerTariefCreationPanel {

    private List<DayOfWeek> selectedDayOfWeeks = new ArrayList<>();
    private JPanel tariefCreationPanel;
    private JComboBox<EnergyLabel> energyLabelJComboBox;
    private JComboBox<VehicleType> vehicleTypeJComboBox;
    private JSpinner startHourSpinner;
    private SpinnerModel startHourValue;
    private JSpinner startMinuteSpinner;
    private SpinnerModel startMinuteValue;
    private JSpinner endHourSpinner;
    private SpinnerModel endHourValue;
    private JSpinner endMinuteSpinner;
    private SpinnerModel endMinuteValue;
    private JPopupMenu menu;
    private JButton dayButton;
    private JButton createUpdateTarief;
    private TariefCompletionListener tariefCompletionListener;

    public KilometerTariefCreationPanel(TariefCompletionListener listener){
        this.tariefCompletionListener = listener;
        loadFrame();
    }

    private void loadFrame() {
        tariefCreationPanel = new JPanel();
        tariefCreationPanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        tariefCreationPanel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        //contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 30, 30, 30, 30, 0};
        gbl_contentPane.rowHeights = new int[]{30, 30, 30, 30, 30};
        gbl_contentPane.location(0, 0);
        tariefCreationPanel.setLayout(gbl_contentPane);
        GridBagConstraints gbc_contentPane = new GridBagConstraints();

        JLabel updateCreateRate = new JLabel("Update/Create new rate");
        GridBagConstraints gbc_updateCreateRate = new GridBagConstraints();
        gbc_updateCreateRate.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_updateCreateRate.gridx = 0;
        gbc_updateCreateRate.gridy = 0;
        tariefCreationPanel.add(updateCreateRate, gbc_updateCreateRate);

        JLabel energyLabel = new JLabel("EnergyLabel");
        GridBagConstraints gbc_energyLabel = new GridBagConstraints();
        gbc_energyLabel.insets = new Insets(0, 0, 5, 5);
        gbc_energyLabel.gridx = 0;
        gbc_energyLabel.gridy = 1;
        tariefCreationPanel.add(energyLabel, gbc_energyLabel);

        energyLabelJComboBox = new JComboBox<EnergyLabel>(EnergyLabel.values());
        GridBagConstraints gbc_energyLabelJCB = new GridBagConstraints();
        gbc_energyLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_energyLabelJCB.insets = new Insets(0, 0, 5, 5);
        gbc_energyLabelJCB.gridx = gbc_energyLabel.gridx + 1;
        gbc_energyLabelJCB.gridy = gbc_energyLabel.gridy;
        tariefCreationPanel.add(energyLabelJComboBox, gbc_energyLabelJCB);

        JLabel vehicleTypeLabel = new JLabel("VehicleType");
        GridBagConstraints gbc_vehicleType = new GridBagConstraints();
        gbc_vehicleType.insets = new Insets(0, 0, 5, 5);
        gbc_vehicleType.gridx = 0;
        gbc_vehicleType.gridy = 2;
        tariefCreationPanel.add(vehicleTypeLabel, gbc_vehicleType);

        vehicleTypeJComboBox = new JComboBox<VehicleType>(VehicleType.values());
        GridBagConstraints gbc_vehicleTypeLabelJCB = new GridBagConstraints();
        gbc_vehicleTypeLabelJCB.fill = GridBagConstraints.HORIZONTAL;
        gbc_vehicleTypeLabelJCB.insets = new Insets(0, 0, 5, 5);
        gbc_vehicleTypeLabelJCB.gridx = gbc_vehicleType.gridx + 1;
        gbc_vehicleTypeLabelJCB.gridy = gbc_vehicleType.gridy;
        tariefCreationPanel.add(vehicleTypeJComboBox, gbc_vehicleTypeLabelJCB);


        JLabel startHour = new JLabel("Starting Hour");
        GridBagConstraints gbc_startHour = new GridBagConstraints();
        gbc_startHour.insets = new Insets(0, 0, 10, 10);
        gbc_startHour.gridx = 0;
        gbc_startHour.gridy = 3;
        tariefCreationPanel.add(startHour, gbc_startHour);

        startHourValue = new SpinnerNumberModel(0, 0, 24, 1);
        startMinuteValue = new SpinnerNumberModel(0, 0, 60, 1);

        startHourSpinner = new JSpinner(startHourValue);
        DisAllowTextNumberSpinner(startHourSpinner);
        GridBagConstraints gbc_startHourSpinner = new GridBagConstraints();
        gbc_startHourSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_startHourSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_startHourSpinner.gridx = gbc_startHour.gridx + 1;
        gbc_startHourSpinner.gridy = gbc_startHour.gridy;
        tariefCreationPanel.add(startHourSpinner, gbc_startHourSpinner);

        JLabel startMinute = new JLabel("Starting Minute");
        GridBagConstraints gbc_startMinute = new GridBagConstraints();
        gbc_startMinute.insets = new Insets(0, 0, 5, 5);
        gbc_startMinute.gridx = 0;
        gbc_startMinute.gridy = 4;
        tariefCreationPanel.add(startMinute, gbc_startMinute);

        startMinuteSpinner = new JSpinner(startMinuteValue);
        DisAllowTextNumberSpinner(startMinuteSpinner);
        GridBagConstraints gbc_startMinuteSpinner = new GridBagConstraints();
        gbc_startMinuteSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_startMinuteSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_startMinuteSpinner.gridx = gbc_startMinute.gridx + 1;
        gbc_startMinuteSpinner.gridy = gbc_startMinute.gridy;
        tariefCreationPanel.add(startMinuteSpinner, gbc_startMinuteSpinner);


        endHourValue = new SpinnerNumberModel(0, 0, 24, 1);
        endMinuteValue = new SpinnerNumberModel(0, 0, 60, 1);

        JLabel endHour = new JLabel("End Hour");
        GridBagConstraints gbc_endHour = new GridBagConstraints();
        gbc_endHour.insets = new Insets(0, 0, 10, 10);
        gbc_endHour.gridx = 0;
        gbc_endHour.gridy = 5;
        tariefCreationPanel.add(endHour, gbc_endHour);

        endHourSpinner = new JSpinner(endHourValue);
        DisAllowTextNumberSpinner(endHourSpinner);
        GridBagConstraints gbc_endHourSpinner = new GridBagConstraints();
        gbc_endHourSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_endHourSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_endHourSpinner.gridx = gbc_endHour.gridx + 1;
        gbc_endHourSpinner.gridy = gbc_endHour.gridy;
        tariefCreationPanel.add(endHourSpinner, gbc_endHourSpinner);

        JLabel endMinute = new JLabel("Ending Minute");
        GridBagConstraints gbc_endMinute = new GridBagConstraints();
        gbc_endMinute.insets = new Insets(0, 0, 5, 5);
        gbc_endMinute.gridx = 0;
        gbc_endMinute.gridy = 6;
        tariefCreationPanel.add(endMinute, gbc_endMinute);

        endMinuteSpinner = new JSpinner(endMinuteValue);
        DisAllowTextNumberSpinner(endMinuteSpinner);
        GridBagConstraints gbc_endMinuteSpinner = new GridBagConstraints();
        gbc_endMinuteSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_endMinuteSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_endMinuteSpinner.gridx = gbc_endMinute.gridx + 1;
        gbc_endMinuteSpinner.gridy = gbc_endMinute.gridy;
        tariefCreationPanel.add(endMinuteSpinner, gbc_endMinuteSpinner);

        //Day Selector
        JLabel dayLabel = new JLabel("");
        GridBagConstraints gbc_dayLabel = new GridBagConstraints();
        gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dayLabel.gridx = 1;
        gbc_dayLabel.gridy = 7;
        tariefCreationPanel.add(dayLabel, gbc_dayLabel);

        menu = new JPopupMenu();
        List<JMenuItem> menuItems = new ArrayList<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            JMenuItem dayItem = new JCheckBoxMenuItem(day.toString());
            dayItem.addActionListener(e -> {
                if (dayItem.isSelected()) {
                    selectedDayOfWeeks.add(DayOfWeek.valueOf(dayItem.getText()));
                } else {
                    selectedDayOfWeeks.remove(DayOfWeek.valueOf(dayItem.getText()));
                }
                UpdateDayLabel(dayLabel);
            });
            menuItems.add(dayItem);
            menu.add(dayItem);
        }

        dayButton = new JButton("Select days");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0, 0, 5, 5);
        gbc_button.gridx = 0;
        gbc_button.gridy = gbc_dayLabel.gridy;
        tariefCreationPanel.add(dayButton, gbc_button);
        dayButton.addActionListener(e -> {
            if (!menu.isVisible()) {
                Point p = dayButton.getLocationOnScreen();
                menu.setInvoker(dayButton);
                menu.setLocation((int) p.getX(),
                        (int) p.getY() + dayButton.getHeight());
                menu.setVisible(true);
            } else {
                menu.setVisible(false);
            }

        });

        for (JMenuItem menuItem : menuItems) {
            menuItem.addActionListener(new OpenAction(menu, dayButton));
        }
    }

    private void DisAllowTextNumberSpinner(JSpinner spinner){
        JFormattedTextField spinnerText = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) spinnerText.getFormatter()).setAllowsInvalid(false);
    }

    private void UpdateDayLabel(JLabel label) {
        String days = "";
        if (selectedDayOfWeeks.size() == DayOfWeek.values().length) {
            days = "EVERY DAY OF THE WEEK";
        } else {
            for (DayOfWeek day :
                    selectedDayOfWeeks) {
                days += day.toString() + ", ";
            }
        }
        label.setText(days);
    }

    public List<DayOfWeek> getSelectedDayOfWeeks() {
        return selectedDayOfWeeks;
    }

    public void setSelectedDayOfWeeks(List<DayOfWeek> selectedDayOfWeeks) {
        this.selectedDayOfWeeks = selectedDayOfWeeks;
    }

    public JPanel getTariefCreationPanel() {
        return tariefCreationPanel;
    }

    public void setTariefCreationPanel(JPanel tariefCreationPanel) {
        this.tariefCreationPanel = tariefCreationPanel;
    }

    public JComboBox<EnergyLabel> getEnergyLabelJComboBox() {
        return energyLabelJComboBox;
    }

    public void setEnergyLabelJComboBox(JComboBox<EnergyLabel> energyLabelJComboBox) {
        this.energyLabelJComboBox = energyLabelJComboBox;
    }

    public JComboBox<VehicleType> getVehicleTypeJComboBox() {
        return vehicleTypeJComboBox;
    }

    public void setVehicleTypeJComboBox(JComboBox<VehicleType> vehicleTypeJComboBox) {
        this.vehicleTypeJComboBox = vehicleTypeJComboBox;
    }

    public JSpinner getStartHourSpinner() {
        return startHourSpinner;
    }

    public void setStartHourSpinner(JSpinner startHourSpinner) {
        this.startHourSpinner = startHourSpinner;
    }

    public SpinnerModel getStartHourValue() {
        return startHourValue;
    }

    public void setStartHourValue(SpinnerModel startHourValue) {
        this.startHourValue = startHourValue;
    }

    public JSpinner getStartMinuteSpinner() {
        return startMinuteSpinner;
    }

    public void setStartMinuteSpinner(JSpinner startMinuteSpinner) {
        this.startMinuteSpinner = startMinuteSpinner;
    }

    public SpinnerModel getStartMinuteValue() {
        return startMinuteValue;
    }

    public void setStartMinuteValue(SpinnerModel startMinuteValue) {
        this.startMinuteValue = startMinuteValue;
    }

    public JSpinner getEndHourSpinner() {
        return endHourSpinner;
    }

    public void setEndHourSpinner(JSpinner endHourSpinner) {
        this.endHourSpinner = endHourSpinner;
    }

    public SpinnerModel getEndHourValue() {
        return endHourValue;
    }

    public void setEndHourValue(SpinnerModel endHourValue) {
        this.endHourValue = endHourValue;
    }

    public JSpinner getEndMinuteSpinner() {
        return endMinuteSpinner;
    }

    public void setEndMinuteSpinner(JSpinner endMinuteSpinner) {
        this.endMinuteSpinner = endMinuteSpinner;
    }

    public SpinnerModel getEndMinuteValue() {
        return endMinuteValue;
    }

    public void setEndMinuteValue(SpinnerModel endMinuteValue) {
        this.endMinuteValue = endMinuteValue;
    }

    public JPopupMenu getMenu() {
        return menu;
    }

    public void setMenu(JPopupMenu menu) {
        this.menu = menu;
    }

    public JButton getDayButton() {
        return dayButton;
    }

    public void setDayButton(JButton dayButton) {
        this.dayButton = dayButton;
    }

    public JButton getCreateUpdateTarief() {
        return createUpdateTarief;
    }

    public void setCreateUpdateTarief(JButton createUpdateTarief) {
        this.createUpdateTarief = createUpdateTarief;
    }
}
