package org.mmga.mcdtd;

import com.alibaba.druid.pool.DruidDataSource;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Logger;

import static org.mmga.mcdtd.Constants.StringConstants.MYSQL;
import static org.mmga.mcdtd.Constants.StringConstants.SQLITE;

/**
 * @author 33572
 * @version 1.0.0
 */
public final class Main extends JavaPlugin {
    public static DruidDataSource druidDataSource = new DruidDataSource();
    @Override
    public void onEnable() {
        Logger logger = this.getLogger();
        Server server = this.getServer();
        PluginManager pluginManager = server.getPluginManager();
        logger.info("The plugin starts the initialization process");
        this.saveDefaultConfig();
        FileConfiguration config = this.getConfig();
        int initialSize = config.getInt("datasource.pool.initialSize");
        int maxActive = config.getInt("datasource.pool.maxActive");
        int minIdle = config.getInt("datasource.pool.minIdle");
        int maxWait = config.getInt("datasource.pool.maxWait");
        druidDataSource.setInitialSize(initialSize);
        druidDataSource.setMaxActive(maxActive);
        druidDataSource.setMinIdle(minIdle);
        druidDataSource.setMaxWait(maxWait);
        String type = config.getString("datasource.type");
        StringBuilder datasourceUrl = new StringBuilder("jdbc:");
        if (SQLITE.equals(type)){
            datasourceUrl.append("sqlite:plugins\\mcdtd\\data.db");
        } else if (MYSQL.equals(type)) {
            datasourceUrl.append("mysql://");
            String host = config.getString("datasource.host");
            String database = config.getString("datasource.database");
            int port = config.getInt("datasource.port");
            datasourceUrl.append(host).append(":").append(port).append("/").append(database);
            List<String> args = config.getStringList("datasource.args");
            if (args.size() != 0){
                datasourceUrl.append("?");
                for (String arg : args) {
                    datasourceUrl.append(arg).append("&");
                }
            }
            String username = config.getString("datasource.username");
            String password = config.getString("datasource.password");
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
        }else{
            logger.info(ChatColor.RED + "Plugin initialization process failed, reason: unknown database type");
            pluginManager.disablePlugin(this);
            return;
        }
        druidDataSource.setUrl(datasourceUrl.toString());
        logger.info("Database initialization is complete");
        logger.info("The plugin initialization is complete!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger logger = this.getLogger();
        logger.info("Plugin uninstall process begins");
        if (!druidDataSource.isClosed()) {
            logger.info("Close POOL connection");
            druidDataSource.close();
        }
        logger.info("The plugin uninstallation is complete, thank you for using it!");
    }
}
