package com.desticube.util;

import com.desticube.DestiRollBack;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

public class SpigotUtil {

    private static ConfigUtil langFile /* = new ConfigUtil(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath() + "/" + "Messages.yml")*/;

    private static Plugin plugin;
    private static final MiniMessage mm = MiniMessage.miniMessage();

//    public static boolean isBedrockPlayer(UUID uuid) {
//        FloodgateApi api = FloodgateApi.getInstance();
//        return api.isFloodgatePlayer(uuid);
//    }

    public World getDefaultWorld() {
        Properties properties = new Properties();
        String mainWorldName = null;
        try {
            properties.load(Files.newInputStream(Paths.get("server.properties")));
            mainWorldName = properties.getProperty("level-name");
            if (mainWorldName == null) {
                // default world name
                mainWorldName = "world";
            }
        } catch (IOException e) {
            // handle exception
        }
        World defaultWorld = Bukkit.getWorld(mainWorldName);

        return defaultWorld;
    }

    public static Player getOnlinePlayer(String target, CommandSender sender) {
        Player output = Bukkit.getPlayerExact(target);
        if (output == null) {
            sender.sendMessage(mm.deserialize(langFile.getConfig().getString("player-target-error")));
            return null;
        }
        return output;
    }

    public static List getlistOfOp() {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.isOp()) {
                players.add(player);
            }
        }

        return players;
    }

    public static List whoHasPermission(String PermNode) {
        List<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PermNode)) {
                players.add(player);
            }
        }

        return players;
    }

    /**
     * This will message all the players with a certain permisson
     *
     * @param PermNode
     * @param msg
     */
    public static void MsgtoPplWithPerm(String PermNode, String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PermNode)) {
                player.sendMessage(msg);
            }
        }
    }

    /**
     * This Will Message everyone with the perm. Except the targeted Player
     *
     * @param PermNode
     * @param target
     * @param msg
     */
    public static void MsgtoPplWithPerm(String PermNode, Player target, String msg) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PermNode) && !player.getName().equalsIgnoreCase(target.getName())) {
                player.sendMessage(msg);
            }
        }

    }

    public static String getLangMsg(String path) {
        String output;
        if (langFile.getConfig().getBoolean("show-prefix") == true) {
            output = langFile.getConfig().getString("prefix").replaceAll("&", "§") + " " + langFile.getConfig().getString(path).replaceAll("&", "§");
        } else {
            output = langFile.getConfig().getString(path).replaceAll("&", "§");
        }

        return output;
    }

    public static boolean getCofigBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }

    public static boolean getMsgBoolean(String path) {
        return langFile.getConfig().getBoolean(path);
    }


    public static void sendLangMsg(String path, CommandSender sender) {
        String output;
        if (langFile.getConfig().getBoolean("show-prefix")) {
            output = plugin.getConfig().getString("prefix").replaceAll("&", "§") + " " + langFile.getConfig().getString(path).replaceAll("&", "§");
        } else {
            output = langFile.getConfig().getString(path).replaceAll("&", "§");
        }
        sender.sendMessage(mm.deserialize(output));
    }

    public static void sendLangMsg(String path, Player sender) {
        String output;
        if (langFile.getConfig().getBoolean("show-prefix")) {
            output = langFile.getConfig().getString("prefix").replaceAll("&", "§") + " " + langFile.getConfig().getString(path).replaceAll("&", "§");
        } else {
            output = langFile.getConfig().getString(path).replaceAll("&", "§");
        }
        sender.sendMessage(mm.deserialize(output));
    }

    public static boolean isPlayerOnline(String playername) throws Exception {
        Player target = Bukkit.getPlayerExact(playername);
        if (target == null) {
            return false;
        } else {
            return true;
        }
    }

    public static ArrayList<String> getOnlinePlayerNames() {
        List<Player> listOfPlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        ArrayList<String> ListOfPlayersNames = new ArrayList<String>();
        for (Player player : listOfPlayers) {
            ListOfPlayersNames.add(player.getName());
        }
        return ListOfPlayersNames;
    }

/*    public static <T> List<List<T>> splitArrayList(List<T> list, int size) {
        List<List<T>> result = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            int end = Math.min(list.size(), i + size);
            result.add(new ArrayList<>(list.subList(i, end)));
        }
        return result;
    }

    public static <T> List<List<T>> splitArrayListintoGroups(List<T> list, int groups) {
        List<List<T>> result = new ArrayList<>();
        int size = (int) Math.ceil((double) list.size() / groups);
        for (int i = 0; i < list.size(); i += size) {
            int end = Math.min(list.size(), i + size);
            result.add(new ArrayList<>(list.subList(i, end)));
        }
        return result;
    }*/

    public static String getConfigStringColor(String path) {
        return plugin.getConfig().getString(path).replaceAll("&", "§");
    }

    public static String getConfigString(String path) {
        return plugin.getConfig().getString(path);
    }
    public static int getConfigInt(String path) {
        return plugin.getConfig().getInt(path);
    }

    public static boolean getConfigBoolean(String path) {
        return plugin.getConfig().getBoolean(path);
    }

    public static void onStartUp(DestiRollBack plugin2) {
        plugin = plugin2;
        langFile = new ConfigUtil(plugin2, "Messages.yml");


        ArrayList<String> header = new ArrayList<>();
        header.add("============================================================");
        header.add(" ");
        header.add(" ");
        header.add(" Customize the plugin's Message");
        header.add(" This is the documentation for all the Messages Below");
        header.add(" ");
        header.add(" ");
        header.add("============================================================");
        header.add("You can customize every message here for different Languages can be used");
        header.add("You can use color codes with & instead of put this § everytime.");
        header.add("Prefix's: %Sender%, %TargetedPlayer%, %Command%");
        header.add("============================================================");
        header.add(" ");
        langFile.getConfig().options().setHeader(header);

        //For all Commands
        langFile.getConfig().addDefault("prefix", "<black>[<rainbow>DESTIROLLBACK</rainbow><black>]<white>");
        langFile.getConfig().addDefault("show-prefix", true);
        langFile.getConfig().addDefault("permission-message", "<red>You don't have Permission to use this command. If you feel like this is a mistake please contact the server's administrator.");
        langFile.getConfig().addDefault("player-target-error", "<red>couldn't find targeted player");
        langFile.getConfig().addDefault("players-only-message", "Players only");

        langFile.getConfig().addDefault("rollback.description", "<red>This is the main command for inventory Rollbacks");
        langFile.getConfig().addDefault("rollback.missing-args", "<red>Please Provide a player's name");
        langFile.getConfig().addDefault("players-only-message", "<red>Players only");
        langFile.getConfig().options().copyDefaults(true);
        langFile.save();
    }

}
