package org.wzpmc.dontdo;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.wzpmc.dontdo.commands.mcdtd;
import org.wzpmc.dontdo.event.onPlayerChat;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Dontdo extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginCommand pluginCommand = this.getCommand("mcdtd");
        pluginCommand.setExecutor(new mcdtd(this));
        this.getServer().getPluginManager().registerEvents(new onPlayerChat(), this);
        this.saveDefaultConfig();
        List<String> strings = new ArrayList();
        strings.add("reload");
        strings.add("start");
        strings.add("next");
        strings.add("restart");
        TabCompleter tabCompleter = new TabCompleter() {
            @Override
            public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
                if (args.length == 1) {
                    List<String> completions = new ArrayList();
                    StringUtil.copyPartialMatches(args[0], strings, completions);
                    Collections.sort(completions);
                    return completions;
                } else {
                    return null;
                }
            }
        };
        pluginCommand.setTabCompleter(tabCompleter);
        getLogger().info(ChatColor.GREEN + "[MCDTD]不要做挑战插件已加载！作者：" + ChatColor.GOLD + "轻尘呦、wzpMC！" + ChatColor.GREEN + "测试者：" + ChatColor.YELLOW + "XMMMMMM");
    }
    public void onDisable() {
        getLogger().info(ChatColor.RED + "[MCDTD]不要做挑战插件已卸载！感谢使用！");
    }
}
