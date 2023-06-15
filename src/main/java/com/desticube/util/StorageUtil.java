package com.desticube.util;

import com.desticube.DestiRollBack;
import com.desticube.model.PlayerBackUp;
import com.google.gson.Gson;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class StorageUtil {

    private static ArrayList<PlayerBackUp> invBackUps = new ArrayList<PlayerBackUp>();

    public static void loadBackUps() throws IOException {
        Gson gson = new Gson();
        File file = new File(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath() + "/InvBackUps.json");
        if (file.exists()){
            Reader reader = new FileReader(file);
            PlayerBackUp[] n = gson.fromJson(reader, PlayerBackUp[].class);
            invBackUps = new ArrayList<>(Arrays.asList(n));
            System.out.println("Backups loaded.");
        }

    }

    public static void saveBackups() throws IOException {

        Gson gson = new Gson();
        System.out.println(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath());
        File file = new File(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath() + "/InvBackUps.json");
        file.getParentFile().mkdir();
        file.createNewFile();
        Writer writer = new FileWriter(file, false);
        gson.toJson(invBackUps, writer);
        writer.flush();
        writer.close();
        System.out.println("Backups saved.");

    }

    public static void deleteBackUp(String id) throws IOException {
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getId().equalsIgnoreCase(id)) {
                invBackUps.remove(backup);
                break;
            }
        }
    }
    public static PlayerBackUp getInvBackUp(String id){
        for (PlayerBackUp backups : invBackUps) {
            if (backups.getId().equalsIgnoreCase(id)) {
                return backups;
            }
        }
        return null;
    }

    public static ArrayList<PlayerBackUp> getInvBackUps(Player player){
        ArrayList<PlayerBackUp> output = new ArrayList<PlayerBackUp>();
        for (PlayerBackUp backups : invBackUps) {
            if (backups.getPlayersUUID().equalsIgnoreCase(player.getUniqueId().toString())) {
                output.add(backups);
            }
        }
        return null;
    }

    /**
     *
     * @param player the player to back up
     * @param eventType (JOIN, DEATH, FORCED) Has to be one of the 4 how to it was caused
     * @param eventTypeCause This is so that you can have another notes about the backup
     * @param WorldUUID
     * @return
     */
    public static PlayerBackUp createInvBackUp(Player player, String eventType, World world, String eventTypeCause){
        boolean isValid = false;
        String tempid;
        do {
            tempid = UUID.randomUUID().toString();
            int counter = 0;
            for (PlayerBackUp backup : invBackUps) {
                if (backup.getId() == tempid) {
                    counter += 1;
                }
            }
            if (counter == 0){
                isValid = true;
            }
        }while (!isValid);
        PlayerBackUp backup = new PlayerBackUp(tempid, player, eventType, world, eventTypeCause);

        invBackUps.add(backup);
        return backup;
    }

    /**
     *
     * @param player the player to back up
     * @param eventType (QUIT, JOIN, DEATH, FORCED) Has to be one of the 4 how to it was caused
     * @param WorldUUID what world's uuid that the player was in when the backup was made.
     * @return
     */
    public static PlayerBackUp createInvBackUp(Player player, String eventType, World world){
        boolean isValid = false;
        String tempid;
        do {
        tempid = UUID.randomUUID().toString();
            int counter = 0;
            for (PlayerBackUp backup : invBackUps) {
                if (backup.getId() == tempid) {
                    counter += 1;
                }
            }
            if (counter == 0){
                isValid = true;
            }
        }while (!isValid);

        PlayerBackUp backup = new PlayerBackUp(tempid, player, eventType, world);
        invBackUps.add(backup);
        return backup;
    }

    public List<PlayerBackUp> getSortedPlayerBackups(List<PlayerBackUp> playerBackups, Player player) {
        ArrayList<PlayerBackUp> matchingBackups = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();
        // Collect matching backups based on playersUUID
        for (PlayerBackUp backup : playerBackups) {
            if (backup.getPlayersUUID().equals(playerUUID)) {
                matchingBackups.add(backup);
            }
        }

        // Sort the backups using a custom comparator
        Collections.sort(matchingBackups, new Comparator<PlayerBackUp>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(PlayerBackUp backup1, PlayerBackUp backup2) {
                LocalDateTime date1 = LocalDateTime.parse(backup1.getBackUpDate(), formatter);
                LocalDateTime date2 = LocalDateTime.parse(backup2.getBackUpDate(), formatter);
                return date2.compareTo(date1); // Sort in descending order (newest to oldest)
            }
        });

        return matchingBackups;
    }
    public static List<PlayerBackUp> getSortedPlayerBackups(Player player) {
        ArrayList<PlayerBackUp> matchingBackups = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();

        // Collect matching backups based on playersUUID
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getPlayersUUID().equals(playerUUID)) {
                matchingBackups.add(backup);
            }
        }

        // Sort the backups using a custom comparator
        Collections.sort(matchingBackups, new Comparator<PlayerBackUp>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(PlayerBackUp backup1, PlayerBackUp backup2) {
                LocalDateTime date1 = LocalDateTime.parse(backup1.getBackUpDate(), formatter);
                LocalDateTime date2 = LocalDateTime.parse(backup2.getBackUpDate(), formatter);
                return date2.compareTo(date1); // Sort in descending order (newest to oldest)
            }
        });

        return matchingBackups;
    }

    public List<PlayerBackUp> getSortedPlayerBackups(Player player, String worldUUID) {
        ArrayList<PlayerBackUp> matchingBackups = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();
        // Collect matching backups based on playersUUID
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getPlayersUUID().equals(playerUUID) && backup.getWorldUUID().equals(worldUUID)) {
                matchingBackups.add(backup);
            }
        }

        // Sort the backups using a custom comparator
        Collections.sort(matchingBackups, new Comparator<PlayerBackUp>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(PlayerBackUp backup1, PlayerBackUp backup2) {
                LocalDateTime date1 = LocalDateTime.parse(backup1.getBackUpDate(), formatter);
                LocalDateTime date2 = LocalDateTime.parse(backup2.getBackUpDate(), formatter);
                return date2.compareTo(date1); // Sort in descending order (newest to oldest)
            }
        });

        return matchingBackups;
    }
}
