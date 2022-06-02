package org.mmga.mcdtd;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * @author 33572
 * @version 1.0.0
 */
public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        Logger logger = this.getLogger();
        logger.info("插件开始初始化流程");
        // Plugin startup logic
        logger.info("插件初始化完成！");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Logger logger = this.getLogger();
        logger.info("插件卸载流程开始");
        logger.info("插件卸载完成，感谢使用！");
    }
}
