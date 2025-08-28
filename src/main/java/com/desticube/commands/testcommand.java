package com.desticube.commands;

import com.desticube.entity.PlayerBackUp;
import com.desticube.util.SpigotUtil;
import com.desticube.BackUpController;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class testcommand implements TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtil.getLangMsg("players-only-message"));
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("destirollback.rollback"))) {
            sender.sendMessage(SpigotUtil.getLangMsg("players-only-message"));
            return true;
        }

        Inventory inventory = Bukkit.createInventory(player, 54, "§l§cSteve's Inventory Backups");
        ItemStack item = null;
        ItemMeta itemMeta = null;
        item = new ItemStack(Material.SKELETON_SKULL, 1);
        itemMeta = item.getItemMeta();

        itemMeta.setDisplayName("§l§c2023/06/29 5:28pm");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("§r§l§fWorld:");
        lore.add("§r§7 A WONDERFUL WOLRD");
        lore.add("§r§l§fEvent:");
        lore.add("§r§l§7 DEATH");
        lore.add("§r§l§fEvent Cause:");
        lore.add("§r§l§7 Falling out of the world");

        ArrayList<PlayerBackUp> backups = (ArrayList<PlayerBackUp>) BackUpController.getPlayerBackups(player);



        player.sendMessage("Backups for u are " + backups.size());
        item.setLore(lore);
        for (int i = 0; i < 44; i++) {
            inventory.setItem(i, item);
        }
        player.openInventory(inventory);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
