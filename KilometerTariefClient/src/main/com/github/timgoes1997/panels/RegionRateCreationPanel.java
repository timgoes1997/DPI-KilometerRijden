package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.listeners.RegionRateCompletionListener;
import com.github.timgoes1997.util.OpenAction;
import com.github.timgoes1997.util.VisiblePanel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

public class RegionRateCreationPanel {

    private List<DayOfWeek> selectedDayOfWeeks = new ArrayList<>();
    private JPanel panel;
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
    private JButton completionButton;
    private JButton backButton;
    private List<JMenuItem> menuItems;
    private JLabel dayLabel;
    private RegionRateCompletionListener regionRateCompletionListener;

    private RegionRate current;

    public RegionRateCreationPanel(RegionRateCompletionListener listener){
        this.regionRateCompletionListener = listener;
        loadFrame();
    }

    private void loadFrame() {
        panel = new JPanel();
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        //contentPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 30, 30, 30, 30, 0};
        gbl_contentPane.rowHeights = new int[]{30, 30, 30, 30, 30};
        gbl_contentPane.location(0, 0);
        panel.setLayout(gbl_contentPane);
        GridBagConstraints gbc_contentPane = new GridBagConstraints();

        backButton = new JButton("Back");
        GridBagConstraints gbc_backButton = new GridBagConstraints();
        gbc_backButton.insets = new Insets(0, 0, 5, 5);
        gbc_backButton.gridx = 1;
        gbc_backButton.gridy = 0;
        panel.add(backButton, gbc_backButton);
        backButton.addActionListener(e -> {
            regionRateCompletionListener.onBack(VisiblePanel.REGION_CREATION);
        });

        JLabel updateCreateRate = new JLabel("Update/Create new rate");
        GridBagConstraints gbc_updateCreateRate = new GridBagConstraints();
        gbc_updateCreateRate.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc_updateCreateRate.gridx = 0;
        gbc_updateCreateRate.gridy = 1;
        panel.add(updateCreateRate, gbc_updateCreateRate);

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


        JLabel startHour = new JLabel("Starting Hour");
        GridBagConstraints gbc_startHour = new GridBagConstraints();
        gbc_startHour.insets = new Insets(0, 0, 10, 10);
        gbc_startHour.gridx = 0;
        gbc_startHour.gridy = 4;
        panel.add(startHour, gbc_startHour);

        startHourValue = new SpinnerNumberModel(0, 0, 23, 1);
        startMinuteValue = new SpinnerNumberModel(0, 0, 59, 1);

        startHourSpinner = new JSpinner(startHourValue);
        DisAllowTextNumberSpinner(startHourSpinner);
        GridBagConstraints gbc_startHourSpinner = new GridBagConstraints();
        gbc_startHourSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_startHourSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_startHourSpinner.gridx = gbc_startHour.gridx + 1;
        gbc_startHourSpinner.gridy = gbc_startHour.gridy;
        panel.add(startHourSpinner, gbc_startHourSpinner);

        JLabel startMinute = new JLabel("Starting Minute");
        GridBagConstraints gbc_startMinute = new GridBagConstraints();
        gbc_startMinute.insets = new Insets(0, 0, 5, 5);
        gbc_startMinute.gridx = 0;
        gbc_startMinute.gridy = 5;
        panel.add(startMinute, gbc_startMinute);

        startMinuteSpinner = new JSpinner(startMinuteValue);
        DisAllowTextNumberSpinner(startMinuteSpinner);
        GridBagConstraints gbc_startMinuteSpinner = new GridBagConstraints();
        gbc_startMinuteSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_startMinuteSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_startMinuteSpinner.gridx = gbc_startMinute.gridx + 1;
        gbc_startMinuteSpinner.gridy = gbc_startMinute.gridy;
        panel.add(startMinuteSpinner, gbc_startMinuteSpinner);


        endHourValue = new SpinnerNumberModel(0, 0, 23, 1);
        endMinuteValue = new SpinnerNumberModel(0, 0, 59, 1);

        JLabel endHour = new JLabel("End Hour");
        GridBagConstraints gbc_endHour = new GridBagConstraints();
        gbc_endHour.insets = new Insets(0, 0, 10, 10);
        gbc_endHour.gridx = 0;
        gbc_endHour.gridy = 6;
        panel.add(endHour, gbc_endHour);

        endHourSpinner = new JSpinner(endHourValue);
        DisAllowTextNumberSpinner(endHourSpinner);
        GridBagConstraints gbc_endHourSpinner = new GridBagConstraints();
        gbc_endHourSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_endHourSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_endHourSpinner.gridx = gbc_endHour.gridx + 1;
        gbc_endHourSpinner.gridy = gbc_endHour.gridy;
        panel.add(endHourSpinner, gbc_endHourSpinner);

        JLabel endMinute = new JLabel("Ending Minute");
        GridBagConstraints gbc_endMinute = new GridBagConstraints();
        gbc_endMinute.insets = new Insets(0, 0, 5, 5);
        gbc_endMinute.gridx = 0;
        gbc_endMinute.gridy = 7;
        panel.add(endMinute, gbc_endMinute);

        endMinuteSpinner = new JSpinner(endMinuteValue);
        DisAllowTextNumberSpinner(endMinuteSpinner);
        GridBagConstraints gbc_endMinuteSpinner = new GridBagConstraints();
        gbc_endMinuteSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_endMinuteSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_endMinuteSpinner.gridx = gbc_endMinute.gridx + 1;
        gbc_endMinuteSpinner.gridy = gbc_endMinute.gridy;
        panel.add(endMinuteSpinner, gbc_endMinuteSpinner);

        //Day Selector
        dayLabel = new JLabel("");
        GridBagConstraints gbc_dayLabel = new GridBagConstraints();
        gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dayLabel.gridx = 1;
        gbc_dayLabel.gridy = 8;
        panel.add(dayLabel, gbc_dayLabel);

        menu = new JPopupMenu();
        menuItems = new ArrayList<>();
        addDaysToComboBox();

        dayButton = new JButton("Select days");
        GridBagConstraints gbc_button = new GridBagConstraints();
        gbc_button.insets = new Insets(0, 0, 5, 5);
        gbc_button.gridx = 0;
        gbc_button.gridy = gbc_dayLabel.gridy;
        panel.add(dayButton, gbc_button);
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

        completionButton = new JButton("Create");
        GridBagConstraints gbc_completionButton = new GridBagConstraints();
        gbc_completionButton.insets = new Insets(0, 0, 5, 5);
        gbc_completionButton.gridx = 1;
        gbc_completionButton.gridy = 9;
        panel.add(completionButton, gbc_completionButton);
        completionButton.addActionListener(e -> {
            regionRateCompletionListener.onCompletion(null);
        });
    }

    private void addDaysToComboBox(){
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
    }

    private void ResetValues(){
        startHourSpinner.setValue(0);
        startMinuteSpinner.setValue(0);
        endHourSpinner.setValue(0);
        endMinuteSpinner.setValue(0);
        menuItems.forEach(menu::remove);
        menuItems.clear();
    }

    private void DisAllowTextNumberSpinner(JSpinner spinner){
        JFormattedTextField spinnerText = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) spinnerText.getFormatter()).setAllowsInvalid(false);
    }

    private void UpdateDayLabel(JLabel label) {
        StringBuilder days = new StringBuilder();
        if (selectedDayOfWeeks.size() == DayOfWeek.values().length) {
            days = new StringBuilder("EVERY DAY OF THE WEEK");
        } else {
            for (DayOfWeek day :
                    selectedDayOfWeeks) {
                days.append(day.toString()).append(", ");
            }
        }
        label.setText(days.toString());
    }

    public List<DayOfWeek> getSelectedDayOfWeeks() {
        return selectedDayOfWeeks;
    }

    public void setSelectedDayOfWeeks(List<DayOfWeek> selectedDayOfWeeks) {
        this.selectedDayOfWeeks = selectedDayOfWeeks;
    }

    public JPanel getPanel() {
        return panel;
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
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

    public JButton getCompletionButton() {
        return completionButton;
    }

    public void setCompletionButton(JButton completionButton) {
        this.completionButton = completionButton;
    }

    public RegionRateCompletionListener getRegionRateCompletionListener() {
        return regionRateCompletionListener;
    }

    public void setRegionRateCompletionListener(RegionRateCompletionListener regionRateCompletionListener) {
        this.regionRateCompletionListener = regionRateCompletionListener;
    }
}
