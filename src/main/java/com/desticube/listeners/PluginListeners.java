package com.desticube.listeners;

import com.desticube.BackUpController;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class PluginListeners implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        String WorldUUID = player.getWorld().getUID().toString();
        BackUpController.createInvBackUp(player, "JOIN", player.getWorld());


    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String WorldUUID = player.getWorld().getUID().toString();
        BackUpController.createInvBackUp(player, "QUIT", player.getWorld());
    }

    @EventHandler
    public void onPlayerDeathEvent(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        String WorldUUID = player.getWorld().getUID().toString();
        BackUpController.createInvBackUp(player, "DEATH", player.getWorld(), event.getDeathMessage());
    }

    @EventHandler
    public void onPlayerChangeWorldEvent(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        BackUpController.createInvBackUp(player, "WORLDCHANGE", event.getFrom());
        
        //StorageUtil.createInvBackUp(player, "WORLDCHANGE", player.getWorld());

        /*StorageUtil.createInvBackUp(player, "WORLDCHANGE", player.getWorld());*/

        //checks if the world moved to is part of the group of worlds in the
        if (!player.getWorld().getName().equals(event.getFrom().getName() + "_nether")
                || !player.getWorld().getName().equals(event.getFrom().getName() + "_the_end")) {
            BackUpController.createInvBackUp(player, "WORLDCHANGE", player.getWorld());
        }


    }
}
