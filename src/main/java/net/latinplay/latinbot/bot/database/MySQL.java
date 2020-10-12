package net.latinplay.latinbot.bot.database;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.*;
import java.util.Objects;

public class MySQL
{
    private final String url;
    private final String username;
    private final String password;
    private Connection connection;
    private HikariDataSource ds;

    public MySQL(String host, String port, String database, String username, String password) {
        this.url = "jdbc:mysql://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;

        try {
            setConnectionArguments();
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                System.out.println("Invalid database arguments! Please check your configuration!");
                System.out.println("If this error persists, please report it to the developer!");
                throw new IllegalArgumentException(e);
            }
            if (e instanceof HikariPool.PoolInitializationException) {
                System.out.println("Can't initialize database connection! Please check your configuration!");
                System.out.println("If this error persists, please report it to the developer!");
                throw new HikariPool.PoolInitializationException(e);
            }
            System.out.println("Can't use the Hikari Connection Pool! Please, report this error to the developer!");
            throw e;
        }

        this.setupTable();
    }

    public void setupTable() {
        try {
            Statement statement = this.connection.createStatement();
            Objects.requireNonNull(statement).executeUpdate("CREATE TABLE IF NOT EXISTS Tickets_ (IDUser VARCHAR(100), IDChannel VARCHAR(100), NameLowerCase VARCHAR(40), Date text)");
            Objects.requireNonNull(statement).executeUpdate("CREATE TABLE IF NOT EXISTS Apelaciones_ (IDUser VARCHAR(100), IDChannel VARCHAR(100), NameLowerCase VARCHAR(40), Date text)");
            Objects.requireNonNull(statement).executeUpdate("CREATE TABLE IF NOT EXISTS Reportes_ (IDUser VARCHAR(100), IDChannel VARCHAR(100), NameLowerCase VARCHAR(40), Date text)");
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized void setConnectionArguments() throws RuntimeException {
        this.ds = new HikariDataSource();
        this.ds.setPoolName("LatinBot MySQL");
        this.ds.setDriverClassName("com.mysql.jdbc.Driver");
        this.ds.setJdbcUrl(this.url);
        this.ds.addDataSourceProperty("cachePrepStmts", "true");
        this.ds.addDataSourceProperty("prepStmtCacheSize", "250");
        this.ds.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        this.ds.addDataSourceProperty("characterEncoding", "utf8");
        this.ds.addDataSourceProperty("encoding", "UTF-8");
        this.ds.addDataSourceProperty("useUnicode", "true");
        this.ds.addDataSourceProperty("useSSL", "false");
        this.ds.setUsername(username);
        this.ds.setPassword(password);
        this.ds.setMaxLifetime(180000);
        this.ds.setIdleTimeout(60000);
        this.ds.setMinimumIdle(1);
        this.ds.setMaximumPoolSize(8);
        try {
            this.connection = this.ds.getConnection();
        } catch (SQLException e) {
            System.out.println("Error on setting connection!");
        }
        System.out.println("Connection arguments loaded, Hikari ConnectionPool ready!");
    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(this.url, this.username, this.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || !this.connection.isValid(5)) {
                this.setConnectionArguments();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public void update(String qry) {
        if (this.connection != null) {
            try {
                this.connection.createStatement().executeUpdate(qry);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet query(String query) {
        try {
            Statement stmt = this.connection.createStatement();
            return stmt.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}