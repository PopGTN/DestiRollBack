package com.desticube.model;

import com.desticube.util.SerializeInventory;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class PlayerBackUp {
    private String id;
    private String playersUUID;
    private String backUpDate;
    private String eventType;
    private String worldUUID;
    private String eventTypeCause;
    private String inventory;

    public PlayerBackUp(String id, Player player, String eventType, World world) {
        this.id = id;
        this.playersUUID = player.getUniqueId().toString();
        Date tempDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.backUpDate = formatter.format(tempDate);
        this.eventType = eventType;
        this.inventory = SerializeInventory.playerInventoryToBase64(player.getInventory());
        this.worldUUID = world.getUID().toString();
    }
    public PlayerBackUp(String id, Player player, String eventType, World world, String eventTypeCause ) {
     this(id, player, eventType, world);
        this.eventTypeCause = eventTypeCause;
    }

    public String getId() {
        return id;
    }

    public String getPlayersUUID() {
        return this.playersUUID;
    }

    public String getBackUpDate() {
        return backUpDate;
    }

    public String getEventType() {
        return eventType;
    }

    public String getEventTypeCause() {
        return eventTypeCause;
    }

    public void setEventTypeCause(String eventTypeCause) {
        this.eventTypeCause = eventTypeCause;
    }

    public String getWorldUUID() {
        return worldUUID;
    }

    public String getInventory() {
        return inventory;
    }
}
