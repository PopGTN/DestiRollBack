package com.desticube.entity;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MenuHolder implements InventoryHolder {

    private List<List<PlayerBackUp>> backUps;
    private Player target;
    private Player commandSender;
    private int pageNumber;

    public MenuHolder(ArrayList<PlayerBackUp> backUps, Player target, Player commandSender) {
        this.target = target;
        this.backUps = splitArrayList(backUps,45);
        this.pageNumber = 1;
        this.commandSender = commandSender;
    }

    public List<List<PlayerBackUp>> getBackUps() {
        return backUps;
    }

    public Player getTarget() {
        return target;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public Player getCommandSender() {
        return commandSender;
    }

    public void nextPage(){
        if (pageNumber != backUps.size()) {
            pageNumber += 1;
        }
    }

    public void previousPage(){
        if (pageNumber != 1) {
            pageNumber -= 1;
        }
    }
    public List<PlayerBackUp> getPagesBackUps(){
        return backUps.get(pageNumber - 1);
    }

    public static <T> List<List<T>> splitArrayList(List<T> inputList, int sublistSize) {
        List<List<T>> result = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i += sublistSize) {
            int endIndex = Math.min(i + sublistSize, inputList.size());
            List<T> sublist = inputList.subList(i, endIndex);
            result.add(sublist);
        }

        return result;
    }

    public boolean lastpage(){
        return pageNumber == backUps.size();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}

