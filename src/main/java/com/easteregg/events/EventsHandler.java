package com.easteregg.events;

import com.easteregg.managers.AbstractManager;
import com.easteregg.managers.ManagerHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EventsHandler extends AbstractManager implements Listener {
    public EventsHandler(ManagerHandler managerHandler) {
        super(managerHandler);

        managerHandler.getEasterMain().getServer().getPluginManager().registerEvents(this,managerHandler.getEasterMain());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){getManagerHandler().getAbstractListeners().forEach(l->l.onInteract(e));}
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){getManagerHandler().getAbstractListeners().forEach(l->l.onChat(e));}
    @EventHandler
    public void onInventoryClick(InventoryClickEvent e){getManagerHandler().getAbstractListeners().forEach(l->l.onClick(e));}
    @EventHandler
    public void onBreak(BlockBreakEvent e){getManagerHandler().getAbstractListeners().forEach(l->l.onBreak(e));}
}
