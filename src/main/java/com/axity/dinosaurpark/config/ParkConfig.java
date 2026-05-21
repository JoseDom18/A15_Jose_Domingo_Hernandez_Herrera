package com.axity.dinosaurpark.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ParkConfig {

    private static ParkConfig instance;
    private final Properties properties;

    private ParkConfig() {
        properties = new Properties();
        try(InputStream input = ParkConfig.class.getClassLoader().getResourceAsStream("park.properties");) {
            if (input == null) {
                throw new FileNotFoundException("Archivo properties no encontrado.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error cargando el archivo park.properties", e);
        }
    }

    public static ParkConfig getInstance() {
        if (instance == null) {
            instance = new ParkConfig();
        }
        return instance;
    }

    public int getInt(String key, int defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : Integer.parseInt(value);
    }

    public double getDouble(String key, double defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : Double.parseDouble(value);
    }

    public String getString(String key, String defaultValue) {
        String value = properties.getProperty(key);
        return value == null ? defaultValue : value;
    }

    public long getSeed() {
        return getInt("simulation.seed", 42);
    }

    public int getTotalSteps() {
        return getInt("simulation.totalSteps", 100);
    }

    static void resetForTesting() {
        instance = null;
    }
}
