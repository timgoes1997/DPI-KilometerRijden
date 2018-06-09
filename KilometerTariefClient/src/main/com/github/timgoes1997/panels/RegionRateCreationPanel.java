package com.github.timgoes1997.panels;

import com.github.timgoes1997.entities.Region;
import com.github.timgoes1997.entities.RegionRate;
import com.github.timgoes1997.entities.enums.EnergyLabel;
import com.github.timgoes1997.entities.enums.VehicleType;
import com.github.timgoes1997.listeners.RegionRateCompletionListener;
import com.github.timgoes1997.request.rate.RegionRateRequestType;
import com.github.timgoes1997.util.OpenAction;
import com.github.timgoes1997.util.VisiblePanel;
import javafx.scene.layout.Pane;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RegionRateCreationPanel implements PanelInfo{

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
    private JSpinner priceSpinner;
    private SpinnerModel priceValue;
    private JPopupMenu menu;
    private JButton dayButton;
    private JButton completionButton;
    private JButton backButton;
    private JButton removeButton;
    private List<JMenuItem> menuItems;
    private JLabel dayLabel;
    private JLabel info;
    private RegionRateCompletionListener regionRateCompletionListener;

    private RegionRate current;
    private Region currentRegion;

    public RegionRateCreationPanel(RegionRateCompletionListener listener) {
        this.regionRateCompletionListener = listener;
        loadFrame();
    }

    private void loadFrame() {
        panel = new JPanel();
        panel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        panel.setBorder(LineBorder.createBlackLineBorder());
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
        disAllowTextNumberSpinner(startHourSpinner);
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
        disAllowTextNumberSpinner(startMinuteSpinner);
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
        disAllowTextNumberSpinner(endHourSpinner);
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
        disAllowTextNumberSpinner(endMinuteSpinner);
        GridBagConstraints gbc_endMinuteSpinner = new GridBagConstraints();
        gbc_endMinuteSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_endMinuteSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_endMinuteSpinner.gridx = gbc_endMinute.gridx + 1;
        gbc_endMinuteSpinner.gridy = gbc_endMinute.gridy;
        panel.add(endMinuteSpinner, gbc_endMinuteSpinner);

        JLabel price = new JLabel("Price");
        GridBagConstraints gbc_price = new GridBagConstraints();
        gbc_price.insets = new Insets(0, 0, 5, 5);
        gbc_price.gridx = 0;
        gbc_price.gridy = 8;
        panel.add(price, gbc_price);

        priceValue = new SpinnerNumberModel(0.010d, 0.001d, 10.0d, 0.001d);
        priceSpinner = new JSpinner(priceValue);
        disAllowTextNumberSpinner(priceSpinner);
        GridBagConstraints gbc_priceSpinner = new GridBagConstraints();
        gbc_priceSpinner.fill = GridBagConstraints.HORIZONTAL;
        gbc_priceSpinner.insets = new Insets(0, 0, 5, 5);
        gbc_priceSpinner.gridx = gbc_price.gridx + 1;
        gbc_priceSpinner.gridy = gbc_price.gridy;
        panel.add(priceSpinner, gbc_priceSpinner);

        //Day Selector
        dayLabel = new JLabel("");
        GridBagConstraints gbc_dayLabel = new GridBagConstraints();
        gbc_dayLabel.insets = new Insets(0, 0, 5, 5);
        gbc_dayLabel.gridx = 1;
        gbc_dayLabel.gridy = 9;
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

        addDaysToComboBoxListners();

        completionButton = new JButton("Create");
        GridBagConstraints gbc_completionButton = new GridBagConstraints();
        gbc_completionButton.insets = new Insets(0, 0, 5, 5);
        gbc_completionButton.gridx = 1;
        gbc_completionButton.gridy = 10;
        panel.add(completionButton, gbc_completionButton);
        completionButton.addActionListener(e -> {
            List<RegionRate> regionRates = new ArrayList<>();
            if(this.current != null){
                if(selectedDayOfWeeks.size() != 1){
                    onReceiveInfo("Updating a regionrate is limited to 1 day, please select the one day you prefer");
                }
                current.setEnergyLabel(energyLabelJComboBox.getItemAt(energyLabelJComboBox.getSelectedIndex()));
                current.setVehicleType(vehicleTypeJComboBox.getItemAt(vehicleTypeJComboBox.getSelectedIndex()));
                current.setDayOfWeek(getSelectedDayOfWeeks().get(0));
                current.setKilometerPrice(new BigDecimal((double)priceValue.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP));
                current.setStartTime((int)startHourValue.getValue(), (int)startMinuteValue.getValue());
                current.setEndTime((int)endHourValue.getValue(), (int)endMinuteValue.getValue());
                regionRates.add(current);
                regionRateCompletionListener.onCompletion(regionRates, RegionRateRequestType.UPDATE);
            }else{
                for(DayOfWeek dayOfWeek : selectedDayOfWeeks){
                    RegionRate regionRate = new RegionRate(
                            currentRegion,
                            vehicleTypeJComboBox.getItemAt(vehicleTypeJComboBox.getSelectedIndex()),
                            new BigDecimal((double)priceValue.getValue()).setScale(3, BigDecimal.ROUND_HALF_UP),
                            energyLabelJComboBox.getItemAt(energyLabelJComboBox.getSelectedIndex()),
                            dayOfWeek,
                            (int)startHourValue.getValue(),
                            (int)startMinuteValue.getValue(),
                            (int)endHourValue.getValue(),
                            (int)endMinuteValue.getValue());
                    regionRates.add(regionRate);
                }
                regionRateCompletionListener.onCompletion(regionRates, RegionRateRequestType.CREATE);
            }
        });

        info = new JLabel("Info");
        GridBagConstraints gbc_info = new GridBagConstraints();
        gbc_info.gridx = 1;
        gbc_info.gridy = 11;
        panel.add(info, gbc_info);
    }

    private void addDaysToComboBox() {
        for (DayOfWeek day : DayOfWeek.values()) {
            JMenuItem dayItem = new JCheckBoxMenuItem(day.toString());
            addDayItemListener(dayItem);
            menuItems.add(dayItem);
            menu.add(dayItem);
        }
    }

    private void addDayItemListener(JMenuItem dayItem) {
        dayItem.addActionListener(e -> {
            if (dayItem.isSelected()) {
                selectedDayOfWeeks.add(DayOfWeek.valueOf(dayItem.getText()));
            } else {
                selectedDayOfWeeks.remove(DayOfWeek.valueOf(dayItem.getText()));
            }
            updateDayLabel(dayLabel);
        });
    }

    private void addDaysToComboBox(DayOfWeek selectedDay){
        for (DayOfWeek day : DayOfWeek.values()) {
            JMenuItem dayItem = new JCheckBoxMenuItem(day.toString());
            if(day == selectedDay) dayItem.setSelected(true);
            addDayItemListener(dayItem);
            menuItems.add(dayItem);
            menu.add(dayItem);
        }
    }

    private void addDaysToComboBoxListners(){
        for (JMenuItem menuItem : menuItems) {
            menuItem.addActionListener(new OpenAction(menu, dayButton));
        }
    }

    public void resetValues() {
        startHourSpinner.setValue(0);
        startMinuteSpinner.setValue(0);
        endHourSpinner.setValue(0);
        endMinuteSpinner.setValue(0);
        priceSpinner.setValue(0.001d);
        menuItems.forEach(menu::remove);
        menuItems.clear();
        selectedDayOfWeeks.clear();
        energyLabelJComboBox.setSelectedIndex(0);
        vehicleTypeJComboBox.setSelectedIndex(0);
        addDaysToComboBox();
        addDaysToComboBoxListners();
        this.currentRegion = null;
        this.current = null;
        completionButton.setText("Create");
        updateDayLabel(dayLabel);
    }

    public void insert(Region region){
        this.currentRegion = region;
    }

    public void insert(RegionRate regionRate){
        startHourSpinner.setValue(regionRate.getStartTime().get(Calendar.HOUR_OF_DAY));
        startMinuteSpinner.setValue(regionRate.getStartTime().get(Calendar.MINUTE));
        endHourSpinner.setValue(regionRate.getEndTime().get(Calendar.HOUR_OF_DAY));
        endMinuteSpinner.setValue(regionRate.getEndTime().get(Calendar.MINUTE));
        priceSpinner.setValue(regionRate.getKilometerPrice().doubleValue());
        menuItems.forEach(menu::remove);
        menuItems.clear();
        selectedDayOfWeeks.clear();
        energyLabelJComboBox.setSelectedIndex(regionRate.getEnergyLabel().ordinal());
        vehicleTypeJComboBox.setSelectedIndex(regionRate.getVehicleType().ordinal());
        addDaysToComboBox(regionRate.getDayOfWeek());
        addDaysToComboBoxListners();
        selectedDayOfWeeks.add(regionRate.getDayOfWeek());
        this.current = regionRate;
        completionButton.setText("Update");
        updateDayLabel(dayLabel);
    }

    private void disAllowTextNumberSpinner(JSpinner spinner) {
        JFormattedTextField spinnerText = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
        ((NumberFormatter) spinnerText.getFormatter()).setAllowsInvalid(false);
    }

    private void updateDayLabel(JLabel label) {
        StringBuilder days = new StringBuilder();
        if (selectedDayOfWeeks.size() == DayOfWeek.values().length) {
            days = new StringBuilder("EVERY DAY OF THE WEEK");
        } else {
            for (DayOfWeek day :
                    selectedDayOfWeeks) {
                days.append(day.toString().substring(0, 3)).append(", ");
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

    @Override
    public void onReceiveInfo(String message) {
        info.setText(message);
    }

    public void setCurrentRegion(Region currentRegion) {
        this.currentRegion = currentRegion;
    }
}
