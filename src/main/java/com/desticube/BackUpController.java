package com.desticube;

import com.desticube.entity.PlayerBackUp;
import com.desticube.util.SpigotUtil;
import com.google.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BackUpController {

    private static ArrayList<PlayerBackUp> invBackUps = new ArrayList<PlayerBackUp>();

    public static void loadBackUps() throws IOException {

        Gson gson = new Gson();
        File file = new File(DestiRollBack.getPlugin().getDataFolder().getAbsolutePath() + "/InvBackUps.json");
        if (file.exists()) {
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
        Bukkit.getLogger().info("Backups saved.");


    }

    //leaving for reference for future plugins.
    public static void deleteBackUp(String id) throws IOException {
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getId().equalsIgnoreCase(id)) {
                invBackUps.remove(backup);
                break;
            }
        }
    }

    //leaving for reference for future plugins.
    public static PlayerBackUp getInvBackUp(String id) {
        for (PlayerBackUp backups : invBackUps) {
            if (backups.getId().equalsIgnoreCase(id)) {
                return backups;
            }
        }
        return null;
    }


    public static PlayerBackUp createInvBackUp(Player player, String eventType, World world, String eventTypeCause) {
        if (SpigotUtil.getConfigBoolean("BackUpLimit.Enable")) {
            BackUpController.LimitPLayerBackups(player, SpigotUtil.getConfigInt("BackUpLimit.limit"));
        }
        boolean isValid = false;
        String tempid;
        try {
            tempid = String.valueOf(Integer.parseInt(invBackUps.get(invBackUps.size() - 1).getId()) + 1);
        } catch (Exception e) {
            tempid = String.valueOf(invBackUps.size() + 1);
        }

        /*  //old way

        do {
            tempid = UUID.randomUUID().toString();
            int counter = 0;
            for (PlayerBackUp backup : invBackUps) {
                if (backup.getId() == tempid) {
                    counter += 1;
                }
            }
            if (counter == 0) {

                isValid = true;
            }
        } while (!isValid);*/
        PlayerBackUp backup = new PlayerBackUp(tempid, player, player.getExp(), eventType, world, eventTypeCause);



        invBackUps.add(backup);
        return backup;
    }

    public static PlayerBackUp createInvBackUp(Player player, String eventType, World world) {
        if (SpigotUtil.getConfigBoolean("BackUpLimit.Enable")) {
            BackUpController.LimitPLayerBackups(player, SpigotUtil.getConfigInt("BackUpLimit.limit"));
        }

        boolean isValid = false;
        String tempid;
        try {
            tempid = String.valueOf(Integer.parseInt(invBackUps.get(invBackUps.size() - 1).getId()) + 1);
        } catch (Exception e) {
            tempid = String.valueOf(invBackUps.size() + 1);
        }
        /* //old way
        do {
            tempid = UUID.randomUUID().toString();
            int counter = 0;
            for (PlayerBackUp backup : invBackUps) {
                if (backup.getId() == tempid) {
                    counter += 1;
                }
            }
            if (counter == 0) {

                isValid = true;
            }
        } while (!isValid);*/

        PlayerBackUp backup = new PlayerBackUp(tempid, player, player.getExp(), eventType, world);
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

    // Sorts newest to oldest backups
    public static List<PlayerBackUp> getSortedPlayerBackups(String UUID) {
        ArrayList<PlayerBackUp> matchingBackups = new ArrayList<>();

        // Collect matching backups based on playersUUID
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getPlayersUUID().equals(UUID)) {
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

    public static List<PlayerBackUp> getPlayerBackups(Player player) {
        ArrayList<PlayerBackUp> matchingBackups = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();

        // Collect matching backups based on playersUUID
        for (PlayerBackUp backup : invBackUps) {
            if (backup.getPlayersUUID().equals(playerUUID)) {
                matchingBackups.add(backup);
            }
        }

        return matchingBackups;
    }

    public static void removeOldBackUps() {

        if (SpigotUtil.getConfigBoolean("AutoDelete.Enable")) {
            invBackUps = removeOldBackups(invBackUps, SpigotUtil.getConfigInt("AutoDelete.GracePeriod"));
        }


    }

    public static ArrayList<PlayerBackUp> removeOldBackups(ArrayList<PlayerBackUp> backups, int days) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        long daysInMilliseconds = (long) days * 24 * 60 * 60 * 1000;
        for (int i = backups.size() - 1; i >= 0; i--) {
            PlayerBackUp backup = backups.get(i);
            String backupDateString = backup.getBackUpDate();
            try {
                Date backupDate = dateFormat.parse(backupDateString);
                if (now.getTime() - backupDate.getTime() > daysInMilliseconds) {
                    backups.remove(i);
                }
            } catch (ParseException e) {
                // Handle exception
            }
        }
        return backups;
    }

    public static void LimitPLayerBackups(Player player, int limit) {

        ArrayList<PlayerBackUp> output = invBackUps;
        Collections.sort(output, new Comparator<PlayerBackUp>() {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(PlayerBackUp backup1, PlayerBackUp backup2) {
                LocalDateTime date1 = LocalDateTime.parse(backup1.getBackUpDate(), formatter);
                LocalDateTime date2 = LocalDateTime.parse(backup2.getBackUpDate(), formatter);
                return date2.compareTo(date1); // Sort in descending order (newest to oldest)
            }
        });
        int counter = 0;
        for (int i = 0; i < output.size(); i++) {
            if (output.get(i).getPlayersUUID() == player.getUniqueId().toString()) {
                if (counter <= 10) {
                    counter += 1;
                } else {
                    output.remove(i);
                }
            }
        }

        invBackUps = output;

    }


}
