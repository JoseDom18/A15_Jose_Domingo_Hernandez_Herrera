package com.axity.dinosaurpark.persistence;

import com.axity.dinosaurpark.config.ParkConfig;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class CsvWriter {
    private final String outputDir;
    private long revenueIdCounter = 1;
    private long expenseIdCounter = 1;

    public CsvWriter() {
        this.outputDir = ParkConfig.getInstance().getString("output.directory", "output");
        initializeDirectoryAndFiles();
    }

    private void initializeDirectoryAndFiles() {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            boolean isCreated = dir.mkdir();
        }
        File revenues = new File(outputDir + File.separator + "revenues.csv");
        try (FileWriter fw = new FileWriter(revenues, false);
        PrintWriter pw = new PrintWriter(fw);) {

            pw.println("id,type,amount,tourist_id,zone,timestamp");

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }

        File expenses = new File(outputDir + File.separator + "expenses.csv");
        try   (FileWriter fw = new FileWriter(expenses, false);
        PrintWriter pw = new PrintWriter(fw);) {
            pw.println("id,type,amount,description,timestamp");

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }

        File events = new File(outputDir + File.separator + "events.csv");
        try (FileWriter fw = new FileWriter(events, false);
        PrintWriter pw = new PrintWriter(fw);) {

            pw.println("step,event_name,description,affected_entities,timestamp");
        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    public void recordRevenue(String type, double amount, int touristId, String zone) {
        RevenueRecord record = new RevenueRecord(this.revenueIdCounter++, type, amount, touristId,zone, LocalDateTime.now());
        try (FileWriter fw = new FileWriter(this.outputDir + File.separator + "revenues.csv", true);
        PrintWriter pw = new PrintWriter(fw);) {
            pw.println(record.toString());

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    public void recordExpense(String type, double amount, String description) {
        ExpenseRecord record = new ExpenseRecord(expenseIdCounter++, type, amount, description, LocalDateTime.now());
        try (FileWriter fw = new FileWriter(this.outputDir + File.separator + "expenses.csv", true);
        PrintWriter pw = new PrintWriter(fw);) {
            pw.println(record.toString());

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

    public void recordEvent(long step, String eventName, String description, String affectedEntities) {
        EventRecord record = new EventRecord(step, eventName, description, affectedEntities, LocalDateTime.now());
        try (FileWriter fw = new FileWriter(this.outputDir + File.separator + "events.csv", true);
             PrintWriter pw = new PrintWriter(fw);) {
            pw.println(record.toString());

        } catch (IOException e) {
            System.err.println("Error writing CSV file: " + e.getMessage());
        }
    }

}
