package com.example.lab8_cli.controller.tool;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ResourceBundle;

public class ObservableResourceFactory {
    private ObjectProperty<ResourceBundle> resourceBundleObjectProperty = new SimpleObjectProperty<>();

    public ObjectProperty<ResourceBundle> getResourceBundleObjectProperty() {
        return resourceBundleObjectProperty;
    }

    public final ResourceBundle getResources() {
        return getResourceBundleObjectProperty().get();
    }

    public final void setResourceBundleObjectProperty(ResourceBundle resourceBundle) {
        getResourceBundleObjectProperty().set(resourceBundle);
    }

    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            {
                bind(getResourceBundleObjectProperty());
            }

            @Override
            protected String computeValue() {
                return getResources().getString(key);
            }
        };
    }
}
