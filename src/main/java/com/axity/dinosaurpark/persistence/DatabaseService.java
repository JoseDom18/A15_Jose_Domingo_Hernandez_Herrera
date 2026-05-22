package com.axity.dinosaurpark.persistence;

import com.axity.dinosaurpark.config.ParkConfig;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.*;
import java.time.LocalDateTime;

public class DatabaseService {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    private double totalRevenueAccumulated = 0.0;
    private double totalExpensesAccumulated = 0.0;

    public DatabaseService() {
        ParkConfig config = ParkConfig.getInstance();
        String outputDir = config.getString("output.directory", "output");

        this.dbUrl = "jdbc:h2:./" + outputDir + "/parkdb;DB_CLOSE_DELAY=-1";
        this.dbUser = "sa";
        this.dbPassword = "";

        initializeDatabase();
    }

    private void initializeDatabase() {
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            liquibase.resource.ResourceAccessor accessor = new liquibase.resource.DirectoryResourceAccessor(
                    new java.io.File(System.getProperty("user.dir"), "src/main/resources").toPath()
            );

            Liquibase liquibase = new Liquibase("db/changelog/db.changelog-master.xml", accessor, database);

            liquibase.update(new liquibase.Contexts(), new liquibase.LabelExpression());
            System.out.println("Base de datos H2 inicializada y esquemas de Liquibase aplicados.");
        } catch (Exception e) {
            System.err.println("Fallo crítico al inicializar la base de datos con Liquibase: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void recordRevenue(String type, double amount, int touristId, String zone) {
        this.totalRevenueAccumulated += amount;
        String sql = "INSERT INTO revenues (type, amount, tourist_id, zone, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setInt(3, touristId);
            ps.setString(4, zone);
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de ingreso: " + e.getMessage());
        }
    }

    public void recordExpense(String type, double amount, String description) {
        this.totalExpensesAccumulated += amount;
        String sql = "INSERT INTO expenses (type, amount, description, created_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, type);
            ps.setDouble(2, amount);
            ps.setString(3, description);
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de gasto: " + e.getMessage());
        }
    }

    public void recordEvent(EventRecord event) {
        if (event == null) return;

        String sql = "INSERT INTO events (step, event_name, description, affected_entities, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, event.step());
            ps.setString(2, event.eventName());
            ps.setString(3, event.description());
            ps.setString(4, event.affectedEntities());
            ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));

            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro de evento: " + e.getMessage());
        }
    }

    public void shutdown() {
        // En H2, podemos forzar un cierre limpio y matar esos hilos fantasma
        try (Connection connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
             Statement stmt = connection.createStatement()) {
            stmt.execute("SHUTDOWN");
        } catch (SQLException e) {
            System.err.println("Error al apagar H2: " + e.getMessage());
        }
    }

    public double getTotalRevenueAccumulated() {
        return totalRevenueAccumulated;
    }

    public double getTotalExpenseAccumulated() {
        return totalExpensesAccumulated;
    }
}