package org.wzpmc.dontdo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;
import org.wzpmc.dontdo.Dontdo;

import java.util.*;

public class mcdtd implements CommandExecutor {
    private Dontdo plugin;
    private FileConfiguration config;
    private Collection<? extends Player> playerslists;
    private Iterator<? extends Player> playersLists;
    private HashMap words = new HashMap();
    private int defaultworldnum;
    private HashMap wordnum = new HashMap();
    private List<String> wordsList;
    private boolean Game = false;
    private ArrayList<Player> playerlist = new ArrayList();

    public mcdtd(Dontdo plugin) {
        this.plugin = plugin; // Store the plugin in situations where you need it.
        this.config = plugin.getConfig();
        this.playerslists = plugin.getServer().getOnlinePlayers();
        this.playersLists = playerslists.iterator();
        this.defaultworldnum = this.config.getInt("wordsnum");
        this.wordsList = this.config.getStringList("words");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            String a = args[0];
            if (a.equals("reload")) {
                if (sender.hasPermission("mcdtd.reload")) {
                    sender.sendMessage(ChatColor.BLUE + "[MCDTD]开始加载配置文件，请稍后！");
                    try {
                        this.config = this.plugin.getConfig();
                        this.defaultworldnum = this.config.getInt("wordsnum");
                        this.wordsList = this.config.getStringList("words");
                        sender.sendMessage(ChatColor.GREEN + "[MCDTD]配置文件加载成功！");
                        return true;
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        if (sender instanceof Player) {
                            sender.sendMessage(ChatColor.RED + "[MCDTD]出现错误，详情请见控制台");
                            return true;
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "[MCDTD]权限不足，无法执行该指令！");
                    return true;
                }
            }
            if (a.equals("start")) {
                if (sender.hasPermission("mcdtd.start")) {
                    this.Game = true;
                    this.playerslists = plugin.getServer().getOnlinePlayers();
                    this.playersLists = playerslists.iterator();
                    while (playersLists.hasNext()) {//获取词条
                        Player player = playersLists.next();
                        ArrayList thisplayerlist = new ArrayList();
                        for (int i = 0; i < this.defaultworldnum; i++) {
                            Random rand = new Random();
                            int randNum = rand.nextInt(this.wordsList.size());
                            thisplayerlist.add(this.wordsList.get(randNum));
                        }
                        this.words.put(player, thisplayerlist);//词条写入玩家词条
                        wordnum.put(player, 0);//玩家初始词语第0个
                        this.playerlist.add(player);//玩家列表添加这个玩家
                        player.sendMessage(ChatColor.GOLD + "[MCDTD]游戏开始，开始抽取词条");
                    }
                    for (int i = 0; i < this.playerlist.size(); i++) {
                        Player thisplayer = this.playerlist.get(i);
                        for (int y = 0; y < this.playerlist.size(); y++) {
                            Player player1 = this.playerlist.get(y);
                            if (player1 != thisplayer) {
                                ArrayList list = (ArrayList) words.get(player1);
                                String word = (String) list.get((int) wordnum.get(player1));
                                Scoreboard scoreboard = thisplayer.getScoreboard();
                                if (scoreboard == null || scoreboard.equals(plugin.getServer().getScoreboardManager().getMainScoreboard())) {
                                    scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
                                }
                                Objective objective = scoreboard.registerNewObjective(player1.getName(), "dummy", "不能做的事");
                                Score score = objective.getScore(player1.getName() + "[" + word + "]");
                                score.setScore(defaultworldnum);
                                objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                thisplayer.setScoreboard(scoreboard);
                            }
                        }
                        thisplayer.sendMessage(ChatColor.GOLD + "词条抽取完成，开始游戏！");
                    }
                    return true;
                }else {
                    sender.sendMessage(ChatColor.RED + "[MCDTD]权限不足，无法执行该指令！");
                    return true;
                }
            }
            if (a.equals("next")) {
                if (sender instanceof Player) {//判断指令发送者是否为玩家
                    if (!Game) {//判断是否有游戏在进行
                        sender.sendMessage(ChatColor.RED + "[MCDTD]没有游戏在进行！");
                        return true;
                    } else {
                        if (sender.hasPermission("mcdtd.next")) {
                            wordnum.put(sender,(int) wordnum.get(sender)+1);
                            System.out.println("words = " + words.toString());
                            for (int y = 0; y < this.playerlist.size(); y++) {
                                Player player1 = this.playerlist.get(y);
                                if (player1 != sender) {
                                    ArrayList list = (ArrayList) words.get(sender);
                                    String word = (String) list.get((int) wordnum.get(sender));
                                    Scoreboard scoreboard = player1.getScoreboard();
                                    Objective objective = scoreboard.getObjective(sender.getName());
                                    assert objective != null;
                                    Score score = objective.getScore(sender.getName() + "[" + word + "]");//null
                                    score.setScore(defaultworldnum-(int) wordnum.get(sender));
                                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                    player1.setScoreboard(scoreboard);
                                }
                            }
                            return true;
                        }else {
                            sender.sendMessage(ChatColor.RED + "[MCDTD]权限不足，无法执行该指令！");
                            return true;
                        }
                    }
                    }else {
                    sender.sendMessage(ChatColor.RED + "[MCDTD]你不是玩家，无法执行该指令！");
                    return true;
                }
                }
            if (a.equals("restart")) {
                if (sender instanceof Player) {//判断指令发送者是否为玩家
                    if (!Game) {//判断是否有游戏在进行
                        sender.sendMessage(ChatColor.RED + "[MCDTD]没有游戏在进行！");
                        return true;
                    } else {
                        if (sender.hasPermission("mcdtd.restart")) {
                            this.Game = true;
                            this.playerslists = plugin.getServer().getOnlinePlayers();
                            this.playersLists = playerslists.iterator();
                            sender.sendMessage(ChatColor.GOLD + "[MCDTD]游戏重新开始，开始抽取词条");
                            while (playersLists.hasNext()) {//获取词条
                                Player player = playersLists.next();
                                ArrayList thisplayerlist = new ArrayList();
                                for (int i = 0; i < this.defaultworldnum; i++) {
                                    Random rand = new Random();
                                    int randNum = rand.nextInt(this.wordsList.size());
                                    thisplayerlist.add(this.wordsList.get(randNum));
                                }
                                this.words.put(player, thisplayerlist);//词条写入玩家词条
                                wordnum.put(player, 0);//玩家初始词语第0个
                                this.playerlist.add(player);//玩家列表添加这个玩家
                            }
                            ChatColor color[] = {ChatColor.GREEN, ChatColor.RED, ChatColor.BLUE, ChatColor.AQUA, ChatColor.GOLD, ChatColor.GRAY};
                            System.out.println(playerlist.toString());
                            for (int i = 0; i < this.playerlist.size(); i++) {
                                Player thisplayer = this.playerlist.get(i);
                                for (int y = 0; y < this.playerlist.size(); y++) {
                                    Player player1 = this.playerlist.get(y);
                                    if (player1 != thisplayer) {
                                        ArrayList list = (ArrayList) words.get(thisplayer);
                                        String word = (String) list.get((int) wordnum.get(thisplayer));
                                        Scoreboard scoreboard = thisplayer.getScoreboard();
                                        if (scoreboard == null || scoreboard.equals(plugin.getServer().getScoreboardManager().getMainScoreboard())) {
                                            scoreboard = plugin.getServer().getScoreboardManager().getNewScoreboard();
                                        }
                                        Objective objective = scoreboard.registerNewObjective(player1.getName(), "dummy", "不能做的事");
                                        Score score = objective.getScore(player1.getName() + "[" + word + "]");
                                        score.setScore(defaultworldnum);
                                        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                                        thisplayer.setScoreboard(scoreboard);
                                    }
                                }
                            }
                            sender.sendMessage(ChatColor.GOLD + "词条抽取完成，开始游戏！");
                            return true;
                        } else {
                            sender.sendMessage(ChatColor.RED + "[MCDTD]权限不足，无法执行该指令！");
                            return true;
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "[MCDTD]你不是玩家，无法执行该指令！");
                    return true;
                }
            }
            else {
                sender.sendMessage(ChatColor.RED + "[MCDTD]未知指令！");
                return true;
            }
        }
        return false;
    }
}