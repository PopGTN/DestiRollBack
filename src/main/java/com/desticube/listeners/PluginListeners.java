package com.desticube.listeners;

import com.desticube.util.StorageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PluginListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        String WorldUUID = player.getWorld().getUID().toString();
        StorageUtil.createInvBackUp(player, "JOIN", player.getWorld());
    }
    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event){
        Player player = event.getPlayer();
        String WorldUUID = player.getWorld().getUID().toString();
        StorageUtil.createInvBackUp(player, "QUIT", player.getWorld());
    }
    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event){
        Player player = event.getPlayer();
        String WorldUUID = player.getWorld().getUID().toString();
        StorageUtil.createInvBackUp(player, "DEATH", player.getWorld(), event.getDeathMessage());
    }
    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event){
        Player player = event.getPlayer();
        StorageUtil.createInvBackUp(player, "WORLDCHANGE", event.getFrom());


        String WorldUUID2 = player.getWorld().getUID().toString();
        StorageUtil.createInvBackUp(player, "WORLDCHANGE", player.getWorld());


    }
}
