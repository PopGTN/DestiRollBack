package com.desticube.commands;

import com.desticube.DestiRollBack;
import com.desticube.model.PlayerBackUp;
import com.desticube.util.SpigotUtil;
import com.desticube.util.StorageUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class InventoryRollbackCommand implements TabExecutor {
    private DestiRollBack plugin;

    public InventoryRollbackCommand(DestiRollBack plugin){
        this.plugin = plugin;
        plugin.getCommand("rollback").setDescription(SpigotUtil.getLangMsg("rollback.description"));
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage(SpigotUtil.getLangMsg("players-only-message"));
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("destirollback.rollback"))){
            sender.sendMessage(SpigotUtil.getLangMsg("players-only-message"));
            return true;
        }
        String playerName = args[0];
        Player target = Bukkit.getPlayerExact(playerName);
        assert target != null;
        ArrayList<PlayerBackUp> backUps = (ArrayList<PlayerBackUp>) StorageUtil.getSortedPlayerBackups(target);
        System.out.println();
        String output = "";
        if (backUps != null || backUps.size() <= 0){
            for (PlayerBackUp backup : backUps) {

                player.spigot().sendMessage(ChatMessageType.CHAT, TextComponent.fromLegacyText(backup.getBackUpDate() + " | " + backup.getEventType() + " | " + backup.getEventTypeCause()));
                output += ( "\n" + backup.getBackUpDate() + " | " + backup.getEventType() + " | " + backup.getEventTypeCause());
            }
            System.out.println(output);

        }
        if (output.length() <= 0){
            sender.sendMessage("Error");
        } else {
            sender.sendMessage("Error");
        }






        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> allPlayers = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                allPlayers.add(player.getName());
            }
            for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                allPlayers.add(player.getName());
            }
            return allPlayers;
        }
        return null;
    }
}
