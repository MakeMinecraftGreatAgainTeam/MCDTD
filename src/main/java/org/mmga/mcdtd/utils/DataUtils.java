package org.mmga.mcdtd.utils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.mmga.mcdtd.Main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import static org.mmga.mcdtd.Constants.StringConstants.MYSQL;
import static org.mmga.mcdtd.Constants.StringConstants.SQLITE;
import static org.mmga.mcdtd.Main.druidDataSource;
/**
 * @author wzp
 * @version 1.0.0
 */
public class DataUtils{
    private static final JavaPlugin plugin = Main.getProvidingPlugin(Main.class);
    private static final Logger logger = plugin.getLogger();
    public DataUtils(){
        this.getConnection();
    }
    public PreparedStatement getStatement(Connection connection,String sql,Object... arguments){
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
        } catch (SQLException e) {
            showError(e,ChatColor.RED,"Problems when creating prepared statements");
            return null;
        }
        try {
            for (int i = 1; i <= arguments.length; i++) {
                preparedStatement.setObject(i, arguments[i]);
            }
        }catch (SQLException e){
            showError(e,ChatColor.RED,"Problems when injecting prepared statements");
            return null;
        }
        return preparedStatement;
    }
    public DataResults doQuery(Connection connection,String sql,Object... arguments){
        PreparedStatement statement = getStatement(connection, sql, arguments);
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery();
        } catch (SQLException e) {
            showError(e,ChatColor.RED,"execute statement ",statement,"failed!");
        }
        return new DataResults(resultSet,statement,connection);
    }
    public void doUpdate(Connection connection,String sql,Object... arguments){
        PreparedStatement statement = getStatement(connection, sql, arguments);
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            showError(e,ChatColor.RED,"execute statement ",statement,"failed!");
        }
    }
    public DataResults doQuery(String sql,Object... arguments){
        Connection connection = getConnection();
        return doQuery(connection, sql, arguments);
    }
    public void doUpdate(String sql,Object... arguments){
        Connection connection = getConnection();
        doUpdate(connection,sql,arguments);
    }
    public Connection getConnection(){
        Connection connection = null;
        try {
            connection = druidDataSource.getConnection();
        }catch (SQLException e){
            showError(e,ChatColor.RED + "Cannot get database connection!");
            return null;
        }
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            showError(e,ChatColor.YELLOW + "Cannot set connection to autoCommit");
        }
        return connection;
    }
    public void initializationDataBase(){
        FileConfiguration config = plugin.getConfig();
        String type = config.getString("datasource.type");
        String prefix = config.getString("datasource.prefix");
        DataResults settings;
        DataResults playerData;
        if (SQLITE.equals(type)){
            settings = doQuery("SELECT name FROM data WHERE type = 'table' AND name = ?;", prefix + "_" + "settings");
            playerData = doQuery("SELECT name FROM data WHERE type = 'table' AND name = ?;", prefix + "_" + "data");
        }else {
            settings = doQuery("select * from information_schema.TABLES where TABLE_NAME = ?;",prefix + "_" + "settings");
            playerData = doQuery("select * from information_schema.TABLES where TABLE_NAME = ?;",prefix + "_" + "data");
        }
        try {
            boolean hasSettings = settings.getResultSet().next();
            boolean hasPlayerData = playerData.getResultSet().next();
            if (!hasPlayerData){

            }
        } catch (SQLException e) {
            showError(e,"Get data failed!");
        }
        settings.close();
        playerData.close();
    }
    public static void showError(SQLException e,Object... custom){
        int errorCode = e.getErrorCode();
        String sqlState = e.getSQLState();
        String localizedMessage = e.getLocalizedMessage();
        logger.info(ChatColor.RED + "errorCode=" + errorCode + "\nSQLState=" + sqlState + "\nExceptionMessage=" + localizedMessage);
        for (Object s : custom) {
            logger.info(s.toString());
        }
    }
}
