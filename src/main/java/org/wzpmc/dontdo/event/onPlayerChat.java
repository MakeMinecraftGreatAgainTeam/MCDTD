package org.wzpmc.dontdo.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.wzpmc.dontdo.Dontdo;

public class onPlayerChat implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event){
        String name = event.getPlayer().getName();
        String content = event.getMessage();
        Plugin plugin = Dontdo.getPlugin(Dontdo.class);
        event.setFormat("<"+name+">"+content);
    }
}
