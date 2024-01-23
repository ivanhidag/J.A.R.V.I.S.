package com.example.jarvis_project_versionpc.Supplements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ObservableString {
    private String value;
    private PropertyChangeSupport propertyChangeSupport;

    public ObservableString(String initialValue) {
        this.value = initialValue;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String newValue) {
        String oldValue = this.value;
        this.value = newValue;
        propertyChangeSupport.firePropertyChange("value", oldValue, newValue);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    /*public static void main(String[] args) {
        ObservableString observableString = new ObservableString("Initial Value");

        observableString.addPropertyChangeListener(evt ->
                System.out.println("Value changed: " + evt.getNewValue()));

        observableString.setValue("New Value");
    }*/
}