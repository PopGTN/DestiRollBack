package com.desticube;

import com.desticube.commands.InventoryRollbackCommand;
import com.desticube.dao.PlayerBackUpDAO;
import com.desticube.listeners.PluginListeners;
import com.desticube.util.SpigotUtil;
import org.bukkit.Bukkit;
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

        //Load Default Config if not there
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        System.out.println("DestiRollBack as been Started!");
        getServer().getPluginManager().registerEvents(new PluginListeners(), this);
        getCommand("rollback").setExecutor(new InventoryRollbackCommand(this));

        if (getPlugin().getConfig().getBoolean("database.enable")) {
            PlayerBackUpDAO.connect();
            PlayerBackUpDAO.initDB();
        } else {
            try {
                BackUpController.loadBackUps();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        getCommand("rollback").setExecutor(new InventoryRollbackCommand(this));
        /*getCommand("testcommand").setExecutor(new testcommand());*/


        //This is to save the backedUp list of Inventories also checks for backups older than set amount of days and removes them
        task = Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                BackUpController.removeOldBackUps();
                boolean isDatabaseEnabled = DestiRollBack.getPlugin().getConfig().getBoolean("database.enable");
                if (!isDatabaseEnabled) {
                    try {
                        BackUpController.saveBackups();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0L, 6000L);
    }

    @Override
    public void onDisable() {
        PlayerBackUpDAO.disconnect();
        task.cancel();
        try {
            BackUpController.saveBackups();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Plugin shutdown logic;
        Bukkit.getLogger().info("DestiRollBack as been stopped!");

    }

    public static DestiRollBack getPlugin() {
        return plugin;
    }
}
