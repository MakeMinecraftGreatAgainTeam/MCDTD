package org.mmga.mcdtd.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;
import org.mmga.mcdtd.utils.DataUtils;

/**
 * @author wzp
 * @version 1.0.0
 */
public class ServerStartup implements Listener {
    @EventHandler
    public static void onServerStartup(ServerLoadEvent event){
        DataUtils dataUtils = new DataUtils();
        dataUtils.initializationDataBase();
    }
}
