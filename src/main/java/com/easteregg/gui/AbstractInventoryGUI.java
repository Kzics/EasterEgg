package com.easteregg.gui;

import com.easteregg.EasterMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.util.function.Consumer;

public abstract class AbstractInventoryGUI implements Listener {
    private final EasterMain plugin;
    private Consumer<InventoryClickEvent> clickAction;
    public AbstractInventoryGUI(EasterMain plugin) {
        this.plugin = plugin;
        this.clickAction = null;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public abstract InventoryHolder getInventoryHolder();

    public void open(Player player) {
        player.openInventory(getInventoryHolder().getInventory());
    }

    public void setClickAction(Consumer<InventoryClickEvent> action) {
        this.clickAction = action;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() != null && event.getClickedInventory().getHolder() == getInventoryHolder()) {
            event.setCancelled(true);
            if (clickAction != null) {
                clickAction.accept(event);
            }
        }
    }

}
