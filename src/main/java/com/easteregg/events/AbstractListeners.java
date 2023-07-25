package com.easteregg.events;

import com.easteregg.managers.ManagerHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class AbstractListeners {

    protected void onInteract(PlayerInteractEvent e){}
    protected void onChat(AsyncPlayerChatEvent e){}
    protected void onClick(InventoryClickEvent e){}
    protected void onBreak(BlockBreakEvent e){}

    public static void addListener(AbstractListeners listener, ManagerHandler managerHandler){
        managerHandler.getAbstractListeners().add(listener);
    }

}
