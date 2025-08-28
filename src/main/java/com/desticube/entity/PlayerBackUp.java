package com.desticube.entity;

import com.desticube.util.SerializeInventory;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PlayerBackUp {
    private String id;
    private String playersUUID;
    private String backUpDate;
    private String eventType;
    private String worldUUID;
    private float playersXP;
    private String eventTypeCause;
    private String inventory;

    /*
     * For Databasestorage
     * */
    public PlayerBackUp() {}
    public PlayerBackUp(String id, String playersUUID, String backUpDate, String eventType, String worldUUID, float playersXP,String eventTypeCause, String inventory) {
        this.id = id;
        this.playersUUID = playersUUID;
        this.backUpDate = backUpDate;
        this.eventType = eventType;
        this.worldUUID = worldUUID;
        this.playersXP = playersXP;
        this.eventTypeCause = eventTypeCause;
        this.inventory = inventory;
    }

    /*
    * For local Storage
    * */
    public PlayerBackUp(String id, Player player, float playersXP, String eventType, World world) {
        this.id = id;
        this.playersUUID = player.getUniqueId().toString();
        Date tempDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.backUpDate = formatter.format(tempDate);
        this.eventType = eventType;
        this.playersXP = playersXP;
        this.inventory = SerializeInventory.playerInventoryToBase64(player.getInventory());
        this.worldUUID = world.getUID().toString();
    }

    public PlayerBackUp(String id, Player player, float playersXP, String eventType, World world, String eventTypeCause ) {
     this(id, player, playersXP, eventType, world);
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

    public float getPlayersXP() {
        return playersXP;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPlayersUUID(String playersUUID) {
        this.playersUUID = playersUUID;
    }

    public void setBackUpDate(String backUpDate) {
        this.backUpDate = backUpDate;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setWorldUUID(String worldUUID) {
        this.worldUUID = worldUUID;
    }

    public void setPlayersXP(float playersXP) {
        this.playersXP = playersXP;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
}
