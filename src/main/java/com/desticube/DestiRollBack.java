package com.desticube;

import com.desticube.commands.InventoryRollbackCommand;
import com.desticube.listeners.PluginListeners;
import com.desticube.util.SpigotUtil;
import com.desticube.util.StorageUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;

public final class DestiRollBack extends JavaPlugin {
    private BukkitTask task;
    private static DestiRollBack plugin;




    @Override
    public void onEnable() {
        plugin = this;
        SpigotUtil.onStartUp(this);

        //Default Config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        System.out.println("DestiRollBack as been Started!");
        getServer().getPluginManager().registerEvents(new PluginListeners(), this);
        getCommand("rollback").setExecutor(new InventoryRollbackCommand(this));

        try {
            StorageUtil.loadBackUps();
        } catch (IOException e) {
            e.printStackTrace();
        }



        //This is to save the backedUp list of Inventories
        task = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                System.out.println("Any Inventory Backups that were made in the last 5 minutes are now saved.");
                try {
                    StorageUtil.saveBackups();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0L, 6000L);
    }
    @Override
    public void onDisable() {
        task.cancel();
        try {
            StorageUtil.saveBackups();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Plugin shutdown logic;
        System.out.println("DestiRollBack as been stopped!");

    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
