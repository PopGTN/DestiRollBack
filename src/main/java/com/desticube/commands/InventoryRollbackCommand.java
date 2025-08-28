package com.desticube.commands;

import com.desticube.DestiRollBack;
import com.desticube.entity.MenuHolder;
import com.desticube.entity.PlayerBackUp;
import com.desticube.util.SerializeInventory;
import com.desticube.util.SpigotUtil;
import com.desticube.BackUpController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.bukkit.Bukkit.getWorld;

public class InventoryRollbackCommand implements Listener, TabExecutor {
    private DestiRollBack plugin;
    final MiniMessage mm = MiniMessage.miniMessage();

    public InventoryRollbackCommand(DestiRollBack plugin) {
        this.plugin = plugin;
        plugin.getCommand("rollback").setDescription(SpigotUtil.getLangMsg("rollback.description"));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Handles all the events that happen in the inventory
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        if (e.getInventory().getHolder() instanceof MenuHolder) {
            e.setCancelled(true);
            MenuHolder menuHolder = (MenuHolder) e.getInventory().getHolder();
            Inventory inv = e.getClickedInventory();
            List<PlayerBackUp> backUps = menuHolder.getPagesBackUps();
            int slot = e.getSlot();

            /**
             * This controls the functions of the back and forth buttons*/
            if (((e.getSlot() == 53) || (e.getSlot() == 45)) && e.getCurrentItem() != null) {
                if (e.getSlot() == 45) {
                    menuHolder.previousPage();
                    backUps = menuHolder.getPagesBackUps();
                }
                if (e.getSlot() == 53) {
                    menuHolder.nextPage();
                    backUps = menuHolder.getPagesBackUps();
                    if(menuHolder.lastpage()){
                        inv.clear(53);
                    }
                }
                if (menuHolder.getPageNumber() <= 1){
                    inv.clear(45);
                }
                //clears the preview items
                for (int i = 0; i < 45; i++) {
                    inv.clear(i);

                }


                if (backUps != null || backUps.size() <= 0) {

                    for (int i = 0; i < backUps.size(); i++) {
                        if (i > 44) {
                            System.out.println("Error with the list's lenght. If you see this error please contact the developer asap");
                            menuHolder.getCommandSender().sendMessage(mm.deserialize("<red><bold>Error with the list's lenght. If you see this error please contact the developer asap"));

                            break;
                        }
                        inv.setItem(i, getItem(backUps.get(i)));
                    }


                    if (menuHolder.getPageNumber() > 1) {
                        ItemStack nextArrow = new ItemStack(Material.ARROW, 1);
                        ItemMeta nextArrowMeta = nextArrow.getItemMeta();
                        nextArrowMeta.displayName(mm.deserialize("<white><bold>Next Page"));
                        nextArrow.setItemMeta(nextArrowMeta);
                        inv.setItem(53, nextArrow);


                    }
                    if (menuHolder.getPageNumber() > 1) {
                        ItemStack BackArrow = new ItemStack(Material.ARROW, 1);
                        ItemMeta BackArrowMeta = BackArrow.getItemMeta();
                        BackArrowMeta.displayName(mm.deserialize("<white><bold>Previous Page"));
                        BackArrow.setItemMeta(BackArrowMeta);
                        inv.setItem(45, BackArrow);
                    }

                }
            }
            /**This controlls the Function if any of the backups are choosen.*/
            if (e.getSlot() <= 44 && e.getSlot() >= 0) {
                PlayerInventory playerInventory = menuHolder.getTarget().getInventory();
                ItemStack[] items = SerializeInventory.itemStackArrayFromBase64(backUps.get(slot).getInventory());
                playerInventory.setContents(items);
                menuHolder.getTarget().setExp(backUps.get(slot).getPlayersXP());
                inv.close();
            }

        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String
            label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(SpigotUtil.getLangMsg("players-only-message"));
            return true;
        }
        Player player = (Player) sender;
        if (!(player.hasPermission("destirollback.rollback"))) {
            sender.sendMessage(SpigotUtil.getLangMsg("permission-message"));
            return true;

        }
        if (args.length == 0) {
            sender.sendMessage(mm.deserialize(SpigotUtil.getLangMsg("rollback.missing-args")));
            return true;
        }




        if (args.length == 1) {
            String playerName = args[0];
            Player target = Bukkit.getPlayerExact(playerName);
            if (target != null) {
                ArrayList<PlayerBackUp> backUps = (ArrayList<PlayerBackUp>) BackUpController.getSortedPlayerBackups(target.getUniqueId().toString());

                MenuHolder tempHolder = new MenuHolder(backUps, target, player);
                //inventory sizes 9 18 27 36 45 54
                Inventory inventory = Bukkit.createInventory(tempHolder, 54, mm.deserialize("<white><bold>" + target.getName() + "'s Inventory Backups"));

                if (backUps != null || backUps.size() <= 0) {

                    for (int i = 0; i < tempHolder.getPagesBackUps().size(); i++) {
                        if (i > 44) {
                            break;
                        } else {
                            inventory.setItem(i, getItem(backUps.get(i)));
                        }

                        getItem(backUps.get(i));
                    }
                    if (backUps.size() > 45) {
                        ItemStack nextArrow = new ItemStack(Material.ARROW, 1);
                        ItemMeta nextArrowMeta = nextArrow.getItemMeta();
                        nextArrowMeta.displayName(mm.deserialize("<white><bold>Next Page"));
                        nextArrow.setItemMeta(nextArrowMeta);
                        inventory.setItem(53, nextArrow);
                    }

                }
                player.openInventory(inventory);
                return true;
            } else {
                SpigotUtil.sendLangMsg("player-target-error", player);
                return true;
            }
        } else if (args.length == 2){
            String playerName = args[0];
            Player target = Bukkit.getPlayerExact(playerName);
            if(target != null){
                switch (args[1].toLowerCase()){
                    case "filter":
                    case "search":
                        break;
                    case "backup":
                    case "forcebackup":
                    case "force":
                        break;

                }
            } else {
                SpigotUtil.sendLangMsg("player-target-error", player);
                return true;
            }
        }
        return true;
    }

    /*
     * Check what was the cause of the backup and sets the right block*/
    private ItemStack getItem(PlayerBackUp backUp) {
        ItemStack item = null;
        ItemMeta itemMeta = null;
        ArrayList<Component> lore = new ArrayList<>();
        switch (backUp.getEventType()) {
            case "JOIN":
                item = new ItemStack(Material.getMaterial(SpigotUtil.getConfigString("SavesBlocks.JOIN")), 1);
                itemMeta = item.getItemMeta();

                itemMeta.setDisplayName("§l§c" + backUp.getBackUpDate());
                itemMeta.displayName(mm.deserialize("<red><bold>" + backUp.getBackUpDate() + "</bold>"));
                lore.add(mm.deserialize("<white>World: <gray>" + getWorld(UUID.fromString(backUp.getWorldUUID())).getName()));
                lore.add(mm.deserialize("<white>Event: <gray>" + backUp.getEventType()));

                break;
            case "FORCED":
                item = new ItemStack(Material.getMaterial(SpigotUtil.getConfigString("SavesBlocks.FORCED")), 1);
                itemMeta = item.getItemMeta();



                /*Component comp = mm.deserialize("<white><bold>"+ backUp.getBackUpDate() +"</bold>");*/
                itemMeta.displayName(mm.deserialize("<red><bold>" + backUp.getBackUpDate() + "</bold>"));
                lore.add(mm.deserialize("<white>World: <gray>" + getWorld(UUID.fromString(backUp.getWorldUUID())).getName()));
                lore.add(mm.deserialize("<white>Event: <gray>" + backUp.getEventType()));
                lore.add(mm.deserialize("<white>Backup Reason:"));
                lore.add(mm.deserialize("<gray>" + (backUp.getEventTypeCause().isEmpty() ? "No Reason Provided": backUp.getEventTypeCause())));
                break;
            case "DEATH":
                item = new ItemStack(Material.getMaterial(SpigotUtil.getConfigString("SavesBlocks.DEATH")), 1);
                itemMeta = item.getItemMeta();
                itemMeta.displayName(mm.deserialize("<red><bold>" + backUp.getBackUpDate() + "</bold>"));
                lore.add(mm.deserialize("<white>World: <gray>" + getWorld(UUID.fromString(backUp.getWorldUUID())).getName()));
                lore.add(mm.deserialize("<white>Event: <gray>" + backUp.getEventType()));
                lore.add(mm.deserialize("<gray>" + backUp.getEventTypeCause()));
                break;
            case "QUIT":
                item = new ItemStack(Material.BARRIER, 1);
                itemMeta = item.getItemMeta();
                itemMeta.displayName(mm.deserialize("<red><bold>" + backUp.getBackUpDate() + "</bold>"));
                lore.add(mm.deserialize("<white>World: <gray>" + getWorld(UUID.fromString(backUp.getWorldUUID())).getName()));
                lore.add(mm.deserialize("<white>Event: <gray>" + backUp.getEventType()));
                break;
            case "WORLDCHANGE":
                item = new ItemStack(Material.getMaterial(SpigotUtil.getConfigString("SavesBlocks.WORLDCHANGE")), 1);
                itemMeta = item.getItemMeta();
                itemMeta.displayName(mm.deserialize("<red><bold>" + backUp.getBackUpDate() + "</bold>"));
                lore.add(mm.deserialize("<white>World: <gray>" + getWorld(UUID.fromString(backUp.getWorldUUID())).getName()));
                lore.add(mm.deserialize("<white>Event: <gray>" + backUp.getEventType()));
                break;
        }
        if (item != null) {
            itemMeta.lore(lore);
            item.setItemMeta(itemMeta);
        }
        return item;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command
            command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> allPlayers = new ArrayList<String>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                allPlayers.add(player.getName());
            }
            if (SpigotUtil.getConfigBoolean("offline-player-support")) {
                for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                    allPlayers.add(player.getName());
                }
            }
            return allPlayers;

        } else if (args.length == 2){
            ArrayList<String> options = new ArrayList<String>();
            options.add("backup");
            options.add("filter");
            options.add("search");
            options.add("force");
            options.add("forcebackup");
            return options;

        } else if (args.length == 3 && (args[2].equalsIgnoreCase("filter") || args[2].equalsIgnoreCase("search"))){
            ArrayList<String> options = new ArrayList<String>();
            options.add("join");
            options.add("quit");
            options.add("worldchange");
            options.add("forced");
            return options;
        }
        return null;
    }
}
