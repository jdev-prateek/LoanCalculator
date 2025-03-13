package org.example.loancalculator.utils;

import javafx.application.HostServices;

import java.util.Map;

public class AppState {
    private static Map<String, String> settings;
    private static HostServices hostServices;

    public static Map<String, String> getSettings() {
        return settings;
    }

    public static void setSettings(Map<String, String> settings) {
        AppState.settings = settings;
    }

    public static HostServices getHostServices() {
        return hostServices;
    }

    public static void setHostServices(HostServices hostServices) {
        AppState.hostServices = hostServices;
    }
}
